package com.tooneCode.services.impl;

import com.alibaba.fastjson2.JSON;
import com.intellij.openapi.command.CommandEvent;
import com.intellij.openapi.components.Service;
import com.intellij.openapi.diagnostic.Logger;
import com.tooneCode.core.enums.TrackEventTypeEnum;
import com.tooneCode.core.TooneCoder;
import com.tooneCode.core.model.CompletionParams;
import com.tooneCode.editor.enums.CompletionTriggerModeEnum;
import com.tooneCode.editor.model.CodeEditorInlayItem;
import com.tooneCode.editor.model.CodeEditorInlayList;
import com.tooneCode.editor.model.InlayDisposeEventEnum;
import com.tooneCode.services.TelemetryService;
import com.tooneCode.services.TelemetryThread;
import com.tooneCode.services.model.*;
import com.tooneCode.util.Debouncer;
import com.tooneCode.util.LanguageUtil;
import com.tooneCode.util.TypingSpeeder;
import org.apache.commons.lang3.StringUtils;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.tooneCode.common.CodeCacheKeys;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

@Service
public final class TelemetryServiceImpl implements TelemetryService {
    private static final Logger logger = Logger.getInstance(TelemetryServiceImpl.class);
    private AtomicLong lastAcceptTime = new AtomicLong(0L);
    private AtomicReference<Boolean> lastAccepted = new AtomicReference((Object) null);
    private AtomicReference<Long> lastAcceptTimeMs = new AtomicReference((Object) null);
    private AtomicReference<String> lastCommand = new AtomicReference((Object) null);
    private AtomicReference<String> currentCommand = new AtomicReference((Object) null);
    private AtomicReference<String> latestDisposeType = new AtomicReference((Object) null);
    private AtomicReference<String> latestDisposeCommandName = new AtomicReference((Object) null);
    private AtomicReference<Long> lastDisposeTimeMs = new AtomicReference((Object) null);
    private final Map<String, Timer> timerMap = new ConcurrentHashMap<>();
    private Map<String, Debouncer> textChangeDebouncerMap = new ConcurrentHashMap<>();
    private TypingSpeeder typingSpeeder = new TypingSpeeder();
    static Set<String> ignoreCommands = new HashSet<>(Arrays.asList("Async Render TooneCode Suggestion", "Chat Insert Code"));

    public TelemetryServiceImpl() {
    }

    public void initTelemetry(Project project) {
        long delayTime = TimeUnit.SECONDS.toMillis(Features.REPORT_TEXT_CHANGE_FIXED_TIME.longValue());

        try {
            logger.info("start telemetry:" + project.getName());
            Timer timer = (Timer) this.timerMap.computeIfAbsent(project.getBasePath(), (s) -> {
                return new Timer();
            });
            timer.scheduleAtFixedRate(new TelemetryThread(project), delayTime, delayTime);
        } catch (Exception var6) {
            Exception e = var6;
            logger.warn("Failed to start telemetry thread. and retry", e);
            this.timerMap.remove(project.getBasePath());
            Timer timer = (Timer) this.timerMap.computeIfAbsent(project.getBasePath(), (s) -> {
                return new Timer();
            });
            timer.scheduleAtFixedRate(new TelemetryThread(project), delayTime, delayTime);
        }

    }

    public void destroyTelemetry(Project project) {
        if (this.timerMap.containsKey(project.getBasePath())) {
            Timer timer = (Timer) this.timerMap.remove(project.getBasePath());
            if (timer != null) {
                timer.cancel();
            }
        }

    }

    public void applyCompletion(Editor editor, CodeEditorInlayItem inlayItem, Integer acceptLineCount) {
        this.lastAcceptTime.getAndSet(System.currentTimeMillis());
        Map<String, String> data = new HashMap();
        data.put("acceptLineCount", String.valueOf(acceptLineCount));
        data.put("totalLineCount", String.valueOf(inlayItem.getTotalLineCount()));
        data.put("totalChars", String.valueOf(inlayItem.getContent().length()));
        data.put("index", String.valueOf(inlayItem.getRankIndex()));
        if (StringUtils.isNotEmpty(inlayItem.getCacheId())) {
            data.put("cacheId", inlayItem.getCacheId());
        }

        if (StringUtils.isNotBlank(inlayItem.getBatchId())) {
            data.put("batchId", inlayItem.getBatchId());
        }

        this.buildMeasurements(data, inlayItem);
        this.telemetry(editor.getProject(), TrackEventTypeEnum.INLINE_COMPLETION_ACCEPT, inlayItem.getRequestId(), data);
        this.lastAccepted.getAndSet(true);
        this.lastAcceptTimeMs.getAndSet(System.currentTimeMillis());
    }

    @Override
    public void telemetryTextChange(TextChangeContext context) {
        String addedText = context.getAddedText();
        if (addedText != null && (!addedText.trim().isEmpty() || addedText.length() <= 1)) {
            long timestamp = System.currentTimeMillis();
            Project project = context.getProject();
            String filePath = context.getFilePath();
            Integer startLineNumber = context.getStartLineNumber();
            if (startLineNumber == null) {
                startLineNumber = 0;
            }

            try {
                synchronized (CodeCacheKeys.KEY_TEXT_CHANGE_STAT) {
                    List<TextChangeStat> changeStats = (List) CodeCacheKeys.KEY_TEXT_CHANGE_STAT.get(project);
                    logger.debug("text change addedText:" + addedText.replace("\n", "\\n"));
                    if (changeStats == null) {
                        changeStats = new ArrayList();
                    }

                    if (addedText.startsWith("\n")) {
                        String clearText = StringUtils.stripStart(addedText, "\n");
                        startLineNumber = startLineNumber + (addedText.length() - clearText.length());
                        addedText = StringUtils.stripEnd(clearText, "\n");
                    }

                    TextChangeStat changeStat = new TextChangeStat();
                    changeStat.setFilePath(filePath == null ? UUID.randomUUID().toString() : filePath);
                    changeStat.setStartLineNumber(startLineNumber);
                    long validLineCount = com.tooneCode.util.StringUtils.getNotEmptyLineCount(addedText);
                    String[] addedLines = addedText.split("\n");
                    changeStat.setChangeCharChangeLength((long) addedText.length());
                    changeStat.setChangeNewLineCount((long) addedLines.length);
                    changeStat.setValidNewLineCount(validLineCount);
                    changeStat.setAccepted(context.isAccepted());
                    changeStat.setTimestamp(timestamp);
                    if (context.getLanguage() != null) {
                        changeStat.setLanguage(context.getLanguage().toLowerCase(Locale.ROOT));
                    }

                    changeStat.setSource(context.getSource());
                    if (startLineNumber != null) {
                        changeStat.setChangeLineNumbers(new HashSet());

                        for (int i = 0; (long) i < validLineCount; ++i) {
                            changeStat.getChangeLineNumbers().add(startLineNumber + i);
                        }
                    }

                    logger.debug("text change stat:" + changeStat);
                    ((List) changeStats).add(changeStat);
                    CodeCacheKeys.KEY_TEXT_CHANGE_STAT.set(project, changeStats);
                }

                if (TextChangeReportStrategy.DELAY.getValue().equals(Features.REPORT_TEXT_CHANGE_STRATEGY.stringValue())) {
                    Debouncer textChangeDebouncer = (Debouncer) this.textChangeDebouncerMap.computeIfAbsent(project.getBasePath(), (s) -> {
                        return new Debouncer();
                    });
                    textChangeDebouncer.debounce(() -> {
                        this.flushTelemetryTextChange(project);
                    }, Features.REPORT_TEXT_CHANGE_DELAY_TIME.longValue(), TimeUnit.SECONDS);
                }

                synchronized (this.timerMap) {
                    if (!this.timerMap.containsKey(project.getBasePath())) {
                        this.initTelemetry(project);
                    }
                }
            } catch (Exception var19) {
                Exception e = var19;
                logger.warn("telemetryTextChange error", e);
            }

        }
    }

    public void telemetry(Project project, TrackEventTypeEnum eventType, String requestId, Map<String, String> data) {
        try {
            if (!TooneCoder.INSTANCE.checkCosy(project, false)) {
                logger.warn("invalid cosy service, ignore telemetry");
                return;
            }

            logger.debug("send telemetry: " + eventType.getName() + " " + requestId + " " + data);
            TooneCoder.INSTANCE.getLanguageService(project).telemetry(eventType.getName(), requestId, data);
        } catch (Exception var6) {
            Exception e = var6;
            logger.warn("telemetry error." + e.getMessage());
        }

    }

    private void buildMeasurements(Map<String, String> data, CodeEditorInlayItem item) {
        data.put("measurementLastCommand", (String) this.lastCommand.get());
        data.put("measurementCurrentCommand", (String) this.currentCommand.get());
        data.put("measurementLatestDisposeType", (String) this.latestDisposeType.get());
        data.put("measurementLatestDisposeCommand", (String) this.latestDisposeCommandName.get());
        if (this.lastAccepted.get() != null) {
            data.put("measurementLastAccepted", String.valueOf(this.lastAccepted.get()));
        }

        if (this.lastAcceptTimeMs.get() != null && (Long) this.lastAcceptTimeMs.get() > 0L) {
            data.put("measurementLastAcceptTimeMs", String.valueOf(System.currentTimeMillis() - (Long) this.lastAcceptTimeMs.get()));
        }

        if (this.lastDisposeTimeMs.get() != null && (Long) this.lastDisposeTimeMs.get() > 0L) {
            data.put("measurementLastDisposeTimeMs", String.valueOf(System.currentTimeMillis() - (Long) this.lastDisposeTimeMs.get()));
        }

        if (item != null) {
            data.put("measurementFirstTimeSinceDisplayedMillis", String.valueOf(System.currentTimeMillis() - item.getFirstDisplayTimeMs()));
            data.put("measurementTimeSinceDisplayedMillis", String.valueOf(System.currentTimeMillis() - item.getDisplayTimeMs()));
        }
    }

    public void updateTimestamp(String type, long timestamp) {
        if (com.tooneCode.services.model.TimestampEnum.ACCEPT_TIMESTAMP_TYPE.getType().equals(type)) {
            this.lastAcceptTime.getAndSet(timestamp);
        }

    }

    public void flushTelemetryTextChange(Project project) {
        logger.debug("flush telemetry text change");
        Map<String, String> data = new HashMap();
        synchronized (CodeCacheKeys.KEY_TEXT_CHANGE_STAT) {
            List<TextChangeStat> stats = (List) CodeCacheKeys.KEY_TEXT_CHANGE_STAT.get(project);
            if (stats != null && !stats.isEmpty()) {
                Map<String, List<TextChangeStat>> statMap = (Map) stats.stream().collect(Collectors.groupingBy((e) -> {
                    return String.format("%s|%s", e.getFilePath(), e.getLanguage());
                }, Collectors.collectingAndThen(Collectors.toList(), (list) -> {
                    list.sort(Comparator.comparing(TextChangeStat::getTimestamp));
                    return list;
                })));
                long mergeAcceptCharLength = 0L;
                long mergeTotalCharLength = 0L;
                long mergeAcceptNewLineCount = 0L;
                long mergeTotalNewLineCount = 0L;
                long mergeStartTimestamp = 0L;
                long mergeEndTimestamp = 0L;
                List<Map<String, String>> statDataList = new ArrayList();
                Iterator var19 = statMap.entrySet().iterator();

                while (true) {
                    if (!var19.hasNext()) {
                        data.put("acceptNewLineCount", String.valueOf(mergeAcceptNewLineCount));
                        data.put("acceptCharLength", String.valueOf(mergeAcceptCharLength));
                        data.put("totalNewLineCount", String.valueOf(mergeTotalNewLineCount));
                        data.put("totalCharLength", String.valueOf(mergeTotalCharLength));
                        data.put("startTimestamp", String.valueOf(mergeStartTimestamp));
                        data.put("endTimestamp", String.valueOf(mergeEndTimestamp));
                        data.put("details", JSON.toJSONString(statDataList));
                        CodeCacheKeys.KEY_TEXT_CHANGE_STAT.set(project, null);
                        break;
                    }

                    Map.Entry<String, List<TextChangeStat>> entry = (Map.Entry) var19.next();
                    long acceptCharLength = 0L;
                    long totalCharLength = 0L;
                    long acceptNewLineCount = 0L;
                    long totalNewLineCount = 0L;
                    Map<String, String> statData = new HashMap();
                    List<TextChangeStat> statList = (List) entry.getValue();
                    long lineNumber = -1L;
                    long curValidLineCount = 0L;
                    boolean acceptCurrentLine = false;
                    long startTimestamp = 0L;
                    long endTimestamp = 0L;
                    Iterator var40 = statList.iterator();

                    while (true) {
                        while (var40.hasNext()) {
                            TextChangeStat stat = (TextChangeStat) var40.next();
                            boolean statLine = true;
                            if (stat.getStartLineNumber() != null && lineNumber == (long) stat.getStartLineNumber() && curValidLineCount > 0L) {
                                statLine = false;
                            }

                            if (stat.getValidNewLineCount() == 1L) {
                                if (statLine) {
                                    if (stat.isAccepted()) {
                                        acceptNewLineCount += stat.getValidNewLineCount();
                                    }

                                    totalNewLineCount += stat.getValidNewLineCount();
                                } else if (!acceptCurrentLine && stat.isAccepted()) {
                                    acceptCurrentLine = true;
                                    acceptNewLineCount += stat.getValidNewLineCount();
                                }
                            } else if (stat.getValidNewLineCount() > 1L) {
                                if (statLine) {
                                    if (stat.isAccepted()) {
                                        acceptNewLineCount += stat.getValidNewLineCount();
                                    }

                                    totalNewLineCount += stat.getValidNewLineCount();
                                } else {
                                    if (stat.isAccepted()) {
                                        if (!acceptCurrentLine) {
                                            acceptNewLineCount += stat.getValidNewLineCount();
                                        } else {
                                            acceptNewLineCount += stat.getValidNewLineCount() - 1L;
                                        }
                                    }

                                    totalNewLineCount += stat.getValidNewLineCount() - 1L;
                                }
                            }

                            if (stat.isAccepted()) {
                                acceptCharLength += stat.getChangeCharChangeLength();
                            }

                            totalCharLength += stat.getChangeCharChangeLength();
                            if (stat.getLanguage() != null) {
                                statData.put("language", stat.getLanguage());
                            }

                            if (stat.getSource() != null) {
                                statData.put("source", stat.getSource());
                            }

                            if (stat.getStartLineNumber() != null && stat.getChangeNewLineCount() <= 1L) {
                                if (lineNumber != (long) stat.getStartLineNumber()) {
                                    acceptCurrentLine = false;
                                    curValidLineCount = 0L;
                                }

                                if (stat.isAccepted()) {
                                    acceptCurrentLine = true;
                                }

                                lineNumber = (long) stat.getStartLineNumber();
                                curValidLineCount += stat.getValidNewLineCount();
                            } else {
                                lineNumber = -1L;
                                acceptCurrentLine = false;
                                curValidLineCount = 0L;
                            }

                            if (startTimestamp != 0L && endTimestamp != 0L) {
                                startTimestamp = Math.min(startTimestamp, stat.getTimestamp());
                                endTimestamp = Math.max(endTimestamp, stat.getTimestamp());
                            } else {
                                startTimestamp = stat.getTimestamp();
                                endTimestamp = stat.getTimestamp();
                            }
                        }

                        if (acceptNewLineCount > totalNewLineCount) {
                            totalNewLineCount = acceptNewLineCount;
                        }

                        mergeTotalNewLineCount += totalNewLineCount;
                        mergeTotalCharLength += totalCharLength;
                        mergeAcceptNewLineCount += acceptNewLineCount;
                        mergeAcceptCharLength += acceptCharLength;
                        if (mergeStartTimestamp != 0L && mergeEndTimestamp != 0L) {
                            mergeStartTimestamp = Math.min(mergeStartTimestamp, startTimestamp);
                            mergeEndTimestamp = Math.max(mergeEndTimestamp, endTimestamp);
                        } else {
                            mergeStartTimestamp = startTimestamp;
                            mergeEndTimestamp = endTimestamp;
                        }

                        statData.put("acceptNewLineCount", String.valueOf(acceptNewLineCount));
                        statData.put("acceptCharLength", String.valueOf(acceptCharLength));
                        statData.put("totalNewLineCount", String.valueOf(totalNewLineCount));
                        statData.put("totalCharLength", String.valueOf(totalCharLength));
                        statData.put("startTimestamp", String.valueOf(startTimestamp));
                        statData.put("endTimestamp", String.valueOf(endTimestamp));
                        statDataList.add(statData);
                        break;
                    }
                }
            }
        }

        if (!data.isEmpty()) {
            this.telemetry(project, TrackEventTypeEnum.TEXT_CHANGE, UUID.randomUUID().toString(), data);
        }

    }

    public long getTimestamp(String type) {
        return TimestampEnum.ACCEPT_TIMESTAMP_TYPE.getType().equals(type) ? this.lastAcceptTime.get() : 0L;
    }

    public void clearTypeCommandRecord() {
        this.lastCommand.set(null);
        this.currentCommand.set(null);
        this.typingSpeeder.clear();
    }

    public void triggerCompletion(CompletionTriggerModeEnum triggerMode, Editor editor, CompletionParams params) {
        Map<String, String> data = new HashMap();
        data.put("triggerMode", triggerMode.getName());
        data.put("useLocalModel", String.valueOf(params.getUseLocalModel()));
        data.put("useRemoteModel", String.valueOf(params.getUseRemoteModel()));
        TypingStat stat = this.getTypeStat();
        data.put("avgTypingSpeed", String.valueOf(stat.getAvgTypingSpeed()));
        data.put("lastTypingSpeed", String.valueOf(stat.getLastTypingSpeed()));
        data.put("typingLength", String.valueOf(stat.getTypingLength()));
        data.put("lastTypedChars", stat.getLastTypedChars());
        data.put("currentTypedChars", stat.getCurrentTypedChars());
        data.put("typedCharRowDiff", stat.getTypedCharRowDiff() == null ? null : String.valueOf(stat.getTypedCharRowDiff()));
        if (params.getTextDocument() != null) {
            data.put("language", LanguageUtil.getLanguageByFilePath(params.getTextDocument().getUri()));
        }

        this.buildMeasurements(data, null);
        if (params.getCompletionContextParams() != null) {
            data.put("context", JSON.toJSONString(params.getCompletionContextParams()));
        }

        this.telemetry(editor.getProject(), TrackEventTypeEnum.INLINE_COMPLETION_TRIGGER, params.getRequestId(), data);
    }

    public TypingStat getTypeStat() {
        TypingStat stat = new TypingStat();
        stat.setAvgTypingSpeed(this.typingSpeeder.getAvgSpeed());
        stat.setLastTypingSpeed(this.typingSpeeder.getLastSpeed());
        stat.setTypingLength(this.typingSpeeder.getTypedLength());
        stat.setLastTypedChars(this.typingSpeeder.getLastTypedChars());
        stat.setCurrentTypedChars(this.typingSpeeder.getCurrentTypedChars());
        stat.setTypedCharRowDiff(this.typingSpeeder.getTypedCharRowDiff());
        return stat;
    }

    public void disposeCompletion(InlayDisposeEventEnum disposeAction, Editor editor, CodeEditorInlayList inlayList) {
        this.disposeCompletion(disposeAction, editor, inlayList, (String) null);
    }

    public void disposeCompletion(InlayDisposeEventEnum disposeAction, Editor editor, CodeEditorInlayList inlayList, String commandName) {
        if (!InlayDisposeEventEnum.GENERATING.getName().equals(disposeAction.getName()) && !InlayDisposeEventEnum.ACCEPTED.getName().equals(disposeAction.getName()) && inlayList != null && inlayList.getItems() != null && !inlayList.getItems().isEmpty()) {
            Iterator var5 = inlayList.getItems().iterator();

            while (var5.hasNext()) {
                CodeEditorInlayItem item = (CodeEditorInlayItem) var5.next();
                if (item.isAccepted()) {
                    return;
                }
            }

            CodeEditorInlayItem headItem = (CodeEditorInlayItem) inlayList.getItems().get(0);
            String batchId = headItem.getRequestId();

            for (int i = 0; i < inlayList.getItems().size(); ++i) {
                CodeEditorInlayItem item = (CodeEditorInlayItem) inlayList.getItems().get(i);
                Map<String, String> data = new HashMap();
                data.put("disposeType", disposeAction.getName());
                data.put("rendered", String.valueOf(item.isRendered()));
                data.put("totalLineCount", String.valueOf(item.getTotalLineCount()));
                data.put("totalChars", String.valueOf(item.getContent().length()));
                data.put("index", String.valueOf(i));
                if (i == inlayList.getSelectIndex() && InlayDisposeEventEnum.ACCEPTED.getName().equals(disposeAction.getName())) {
                    data.put("accepted", "true");
                } else {
                    data.put("accepted", "false");
                }

                if (commandName != null) {
                    data.put("commandName", commandName);
                }

                if (StringUtils.isNotEmpty(item.getCacheId())) {
                    data.put("cacheId", item.getCacheId());
                }

                if (StringUtils.isNotBlank(item.getBatchId())) {
                    batchId = item.getBatchId();
                }

                data.put("batchId", batchId);
                this.buildMeasurements(data, item);
                this.telemetry(editor.getProject(), TrackEventTypeEnum.INLINE_COMPLETION_DISPOSE, item.getRequestId(), data);
            }

            this.latestDisposeType.getAndSet(disposeAction.getName());
            this.latestDisposeCommandName.getAndSet(commandName);
            this.lastAccepted.getAndSet(false);
            this.lastDisposeTimeMs.getAndSet(System.currentTimeMillis());
        }
    }

    public void typeRecord(Editor editor, String addedText) {
        this.typingSpeeder.keyTyped(addedText);
        this.typingSpeeder.recordTyping(editor, addedText);
    }

    public void telemetryCommand(@NotNull CommandEvent event) {

        if (!ignoreCommands.contains(event.getCommandName())) {
            String lastCmd = (String) this.currentCommand.getAndSet(event.getCommandName());
            this.lastCommand.getAndSet(lastCmd);
        }
    }
}
