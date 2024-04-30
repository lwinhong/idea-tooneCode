package com.tooneCode.core;

import com.intellij.openapi.util.Pair;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.progress.ProgressIndicator;
import com.intellij.openapi.progress.util.ProgressIndicatorUtils;
import com.intellij.openapi.project.Project;
import com.tooneCode.common.CodeBundle;
import com.tooneCode.common.CodeConfig;
import com.tooneCode.common.CodeSetting;
import com.tooneCode.constants.Constants;
import com.tooneCode.core.enums.CodeCompletionCandidateEnum;
import com.tooneCode.core.lsp.LanguageWebSocketService;
import com.tooneCode.core.model.model.AuthStatus;
import com.tooneCode.core.model.model.IdePlatformType;
import com.tooneCode.core.model.model.IdeSeriesType;
import com.tooneCode.core.model.model.InitializeResultExt;
import com.tooneCode.core.model.params.InitializeParamsWithConfig;
import com.tooneCode.editor.enums.CodeCompletionModeEnum;
import com.tooneCode.services.FeatureService;
import com.tooneCode.ui.config.CodePersistentSetting;
import com.tooneCode.ui.notifications.GrantAuthorNotification;
import com.tooneCode.util.*;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.io.FileUtils;
import org.eclipse.lsp4j.WorkspaceFolder;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;


public class TooneCoder {
    public static TooneCoder INSTANCE = new TooneCoder();
    private static final Logger log = LogUtil.getLogger();
    private static final long STARTUP_TIMEOUT = 10000L;

    //    public static final int DEFAULT_LINGMA_PORT = 36510;
    public Map<String, Boolean> readyMap = new ConcurrentHashMap();
    private Map<String, LanguageWebSocketService> languageServiceMap = new ConcurrentHashMap();
    private BinaryRunner binaryRunner;
    public int cosyStartRetryTimes = 0;
    //    public static final String COSY_INFO_FILE_NAME = ".info";
//    public static final int MAX_COSY_START_RETRY_TIMES = 3;
//    public static final int INFO_FILE_EXISTS_WAIT_TIME_MS = 500;
//    public static final int MAX_INFO_FILE_RETRY_TIMES = 20;
//    public static final int INFO_FILE_LINE_COUNT = 2;
//    private SlideWindowStatQps statQps = new SlideWindowStatQps();
    private Map<String, Lock> startLockMap = new ConcurrentHashMap();
    private Map<String, AtomicBoolean> startingStateMap = new ConcurrentHashMap();

    @Contract(
            pure = true
    )
    private TooneCoder() {
    }

    public void start(Project project) {
        this.start(project, Collections.emptyList());
    }

    public void start(Project project, List<CodeStartupListener> listeners) {
        if (project != null && project.getBasePath() != null) {
            AtomicBoolean startingState = (AtomicBoolean) this.startingStateMap.computeIfAbsent(project.getName(), (e) -> {
                return new AtomicBoolean(false);
            });
            if (startingState.get()) {
                log.warn("Project " + project.getName() + " is starting cosy, ignore repeat starting");
                listeners.forEach(CodeStartupListener::onCancelled);
            } else {
                synchronized (startingState) {
                    if (startingState.get()) {
                        log.warn("Project " + project.getName() + " is starting cosy, ignore repeat starting");
                        listeners.forEach(CodeStartupListener::onCancelled);
                    } else {
                        startingState.getAndSet(true);
                        ThreadUtil.execute(() -> {
                            this.doStart(project, listeners, startingState);
                        });
                    }
                }
            }
        } else {
            log.warn("Project is not defined when Cosy is starting");
            listeners.forEach(CodeStartupListener::onCancelled);
        }
    }

    private void doStart(Project project, List<CodeStartupListener> listeners, AtomicBoolean startingState) {
        List<WorkspaceFolder> workspaceFolders = new ArrayList();
        WorkspaceFolder folder = new WorkspaceFolder();
        folder.setName(project.getName());
        folder.setUri(project.getBasePath());
        workspaceFolders.add(folder);
        InitializeParamsWithConfig params = new InitializeParamsWithConfig();
        params.setWorkspaceFolders(workspaceFolders);
        Lock startLock = (Lock) this.startLockMap.computeIfAbsent(project.getName(), (e) -> {
            return new ReentrantLock();
        });
        if (!startLock.tryLock()) {
            log.warn("Cannot get start lock for project:" + project.getName());
            listeners.forEach(CodeStartupListener::onCancelled);
        } else {
            try {
                log.info(CodeBundle.message("check.cosy.version.state", new Object[0]));
                BinaryManager.INSTANCE.checkBinary(false);
                log.info("starting to startup cosy");
                log.info(CodeBundle.message("start.cosy.process.state", new Object[0]));
                if (INSTANCE.startup(project, params, listeners, false)) {
                    log.info("succeed to startup cosy");
                    GrantAuthorNotification.notifyNeedLogin(project, false);
                    this.cosyStartRetryTimes = 0;
                } else {
                    log.info("failed to startup cosy");
                    GrantAuthorNotification.notifyError(project);
                    listeners.forEach(CodeStartupListener::onFailed);
                }
            } finally {
                startingState.getAndSet(false);
                startLock.unlock();
            }

        }
    }

    public boolean startup(Project project, InitializeParamsWithConfig params, boolean debugMode) {
        return this.startup(project, params, Collections.emptyList(), debugMode);
    }

    public boolean startup(Project project, InitializeParamsWithConfig params, List<CodeStartupListener> listeners, boolean debugMode) {
        try {
            File homeDir = this.getCodeHomeDir();
            if (homeDir == null) {
                log.warn("Invalid binary directory");
                return false;
            } else {
                if (this.binaryRunner == null) {
                    this.binaryRunner = new BinaryRunner(homeDir);
                }

                if (this.cosyStartRetryTimes == 3) {
                    log.warn(String.format("Init Cosy language service error, reached maximum retry times (%d)", this.cosyStartRetryTimes));
                    return false;
                } else {
                    boolean isInitSuccess = this.initCosyLanguageService(project, homeDir, debugMode);
                    if (!isInitSuccess) {
                        log.warn("Cosy init failed");
                        return true;
                    } else {
                        LanguageWebSocketService languageService = (LanguageWebSocketService) this.languageServiceMap.get(project.getLocationHash());
                        if (languageService.getServer() == null) {
                            log.warn("The server of language service is null");
                            return false;
                        } else {
                            this.addConfigToInitializeParams(params);
                            CompletableFuture<InitializeResultExt> future = languageService.getServer().initialize(params);
                            InitializeResultExt latestInitializeResult = (InitializeResultExt) future.get(10000L, TimeUnit.MILLISECONDS);
                            if (latestInitializeResult != null) {
                                if (log.isDebugEnabled()) {
                                    log.debug("get cosy initialize result" + JsonUtil.toJson(latestInitializeResult));
                                }

                                FeatureService.getInstance().updateFeatures(latestInitializeResult.getExperimental());
                            }

                            this.readyMap.put(project.getLocationHash(), Boolean.TRUE);
                            AuthStatus status = languageService.authStatus(3000L);
                            if (status != null) {
                                log.info("get login status:" + status.getStatus());
                                LoginUtil.updateAuthStatus(project, status, false);
                            }

                            listeners.forEach(CodeStartupListener::onStartup);
                            return true;
                        }
                    }
                }
            }
        } catch (TimeoutException var11) {
            log.warn("startup cosy timeout");
            listeners.forEach(CodeStartupListener::onTimeout);
            return true;
        } catch (Exception var12) {
            Exception e = var12;
            log.error("startup cosy failed.", e);
            return false;
        }
    }

    private void addConfigToInitializeParams(InitializeParamsWithConfig params) {
        params.setIdeSeries(IdeSeriesType.JETBRAINS.getName());
        params.setIdePlatform(IdePlatformType.IDEA.getName());
        params.setPluginVersion(Constants.getPluginVersion());
        params.setIdeVersion(ApplicationUtil.getApplicationVersion());
        CodeSetting setting = CodePersistentSetting.getInstance().getState();
        boolean isAllowStatistics = setting != null && setting.isAllowReportUsage();
        params.setAllowStatistics(isAllowStatistics);
        String inferenceModeChanged = setting != null && setting.getParameter() != null
                ? setting.getParameter().getLocal().getInferenceMode() : CodeCompletionModeEnum.AUTO.mode;
        params.setInferenceMode(inferenceModeChanged);
        int maxCandidateNumChanged = setting != null && setting.getParameter() != null
                ? setting.getParameter().getLocal().getMaxCandidateNum() : CodeCompletionCandidateEnum.DEFAULT.num;
        params.setMaxCandidateNum(maxCandidateNumChanged);
    }

    private boolean initCosyLanguageService(Project project, File homeDir, boolean debugMode) throws IOException {
        File infoFile = new File(homeDir, ".info");
        if (infoFile.exists()) {
            log.info(".info exists, start to connect Cosy server");
            return this.connectCosyServer(project, homeDir, debugMode);
        } else {
            log.info(".info not exist, start to create Cosy process");
            return this.startCosy(project, homeDir, debugMode);
        }
    }

    private boolean startCosy(Project project, File homeDir, boolean debugMode) throws IOException {
        ++this.cosyStartRetryTimes;
        if (this.cosyStartRetryTimes >= 3) {
            return false;
        } else {
            if (this.binaryRunner == null) {
                this.binaryRunner = new BinaryRunner(homeDir);
            }

            if (!this.binaryRunner.run(debugMode)) {
                log.warn("Cosy binary run failed");
                return false;
            } else {
                return this.connectCosyServer(project, homeDir, debugMode);
            }
        }
    }

    private boolean connectCosyServer(Project project, File homeDir, boolean debugMode) throws IOException {
        new File(homeDir, ".info");
        Pair<Integer, Long> infoPair = this.readCosyInfoFile(20);
        if (infoPair == null) {
            log.warn("Cannot read from .info, connect to Cosy server failed");
            return false;
        } else {
            Integer port = (Integer) infoPair.first;
            Long pid = (Long) infoPair.second;
            if (port != null && pid != null) {
                try {
                    log.info("Connecting port " + port + " pid:" + pid);
                    LanguageWebSocketService languageService = LanguageWebSocketService.createService(port);
                    languageService.connect();
                    this.languageServiceMap.put(project.getLocationHash(), languageService);
                    Thread heartBeat = new Thread(new CodeHeartbeatRunner(project, null /*languageService.getWebSocketClient().getSession()*/, languageService.getServer()));
                    heartBeat.start();
                    return true;
                } catch (Exception var10) {
                    Exception e = var10;
                    log.warn("Connect to Cosy server error, try to kill process and restart", e);
                    INSTANCE.killProcessAndDeleteInfoFile(pid);
                    return this.startCosy(project, homeDir, debugMode);
                }
            } else {
                log.warn("Cannot get port and pid from .info file, connect failed");
                return false;
            }
        }
    }


    public boolean checkCosy(Project project) {

        return this.checkCosy(project, true);
    }

    public boolean checkCosy(Project project, boolean autoRestart) {
        return this.checkCosy(project, autoRestart, Collections.emptyList());
    }

    public boolean checkCosy(Project project, boolean autoRestart, List<CodeStartupListener> listeners) {
        LanguageWebSocketService languageWebSocketService = INSTANCE.getLanguageService(project);
        boolean isReady = this.isReady(project);
        if (isReady && languageWebSocketService != null && languageWebSocketService.isSessionOpen()) {
            return true;
        } else {
            if (autoRestart) {
                this.restart(project, listeners);
            }

            return false;
        }

        //检查服务是否已连接
        //return true;
    }

    private boolean isReady(Project project) {
        if (this.readyMap != null && project != null) {
            return this.readyMap.containsKey(project.getLocationHash()) && Boolean.TRUE.equals(this.readyMap.get(project.getLocationHash()));
        } else {
            return false;
        }
    }

    public void restart(Project project, List<CodeStartupListener> listeners) {
        AtomicBoolean startingState = (AtomicBoolean) this.startingStateMap.computeIfAbsent(project.getName(), (e) -> {
            return new AtomicBoolean(false);
        });
        if (startingState.get()) {
            log.warn("Project " + project.getName() + " is restarting cosy, ignore repeat restarting");
        } else {
            synchronized (startingState) {
                if (startingState.get()) {
                    log.warn("Project " + project.getName() + " is restarting cosy, ignore repeat restarting");
                    return;
                }

                this.close(project);
            }

            if (listeners == null) {
                listeners = Collections.emptyList();
            }

            this.start(project, listeners);
        }
    }

    public LanguageWebSocketService getLanguageService(Project project) {
        if (project == null) {
            return null;
        } else {
            return this.languageServiceMap.containsKey(project.getLocationHash())
                    && this.languageServiceMap.get(project.getLocationHash()) != null
                    ? this.languageServiceMap.get(project.getLocationHash()) : null;
        }
    }

    public void killProcessAndDeleteInfoFile(Long pid) {
        if (pid > 0L && ProcessUtils.isProcessAlive(pid)) {
            log.info(String.format("%d is alive, try to kill", pid));
            ProcessUtils.killProcess(pid);
        }

        this.deleteInfoFile();
    }

    public void deleteInfoFile() {
        File homeDir = this.getCodeHomeDir();
        if (homeDir != null) {
            File infoFile = new File(homeDir, ".info");
            if (infoFile.exists()) {
                try {
                    infoFile.delete();
                    log.info("Delete .info file success.");
                } catch (Exception var4) {
                    Exception deleteException = var4;
                    log.warn("Delete .info file encountered exception", deleteException);
                }
            }

        }
    }

    private File getCodeHomeDir() {
        File homeDir = CodeConfig.getHomeDirectory().toFile();
        log.info("cosy home dir " + homeDir);
        if (!homeDir.exists() && !homeDir.mkdirs()) {
            log.error("fail to create directory " + homeDir);
            return null;
        } else {
            return homeDir;
        }
    }

    public boolean checkAndWaitCosyState(ProgressIndicator progressIndicator, @NotNull Project project) {
        if (project == null) {
            //$$$reportNull$$$0(1);
        }

        boolean succeed = false;
        long maxTime = TimeUnit.SECONDS.toMillis(20L);
        long startTime = System.currentTimeMillis();
        log.info("Waiting to start cosy and checking state");

        while (true) {
            ProgressIndicatorUtils.checkCancelledEvenWithPCEDisabled(progressIndicator);
            if (INSTANCE.checkCosy(project, false)) {
                succeed = true;
                log.info("Checking cosy state: OK");
                break;
            }

            if (System.currentTimeMillis() - startTime > maxTime) {
                log.info("Checking cosy state: timeout");
                break;
            }

            log.info("Checking cosy state: waiting");

            try {
                Thread.sleep(1000L);
            } catch (InterruptedException var9) {
                break;
            }
        }

        return succeed;
    }

    public void close(Project project) {
        if (project != null) {
            String key = project.getLocationHash();
            if (this.languageServiceMap.containsKey(key) && this.languageServiceMap.get(key) != null) {
                ((LanguageWebSocketService) this.languageServiceMap.get(key)).closeSession();
                this.languageServiceMap.remove(key);
            }

            if (this.readyMap.containsKey(key) && this.readyMap.get(key) != null) {
                this.readyMap.remove(key);
            }

            if (this.readyMap.size() == 0 && this.languageServiceMap.size() == 0) {
                this.binaryRunner = null;
            }

        }
    }

    public Pair<Integer, Long> readCosyInfoFile(int maxRetryTimes) {
        File homeDir = this.getCodeHomeDir();
        if (homeDir == null) {
            return null;
        } else {
            File infoFile = new File(homeDir, ".info");
            Integer port = null;
            Long pid = null;
            int i = 0;

            while (i < maxRetryTimes) {
                Pair<Integer, Long> infoPair = this.checkInfoFile(infoFile);
                if (infoPair != null) {
                    port = (Integer) infoPair.first;
                    pid = (Long) infoPair.second;
                    break;
                }

                try {
                    Thread.sleep(500L);
                } catch (InterruptedException var9) {
                    log.warn("Thread sleep is interrupted when waiting for .info file");
                }

                ++i;
                log.info(String.format("Retry for fetching .info for %d times, %d times left", i, maxRetryTimes - i));
            }

            return port != null && pid != null ? new Pair(port, pid) : this.findProcessAndPortByName();
        }
    }

    private Pair<Integer, Long> findProcessAndPortByName() {
        log.info("try find process and port by name");
        List<Long> cosyPidList = ProcessUtils.findCosyPidList();
        String pidListStr = org.apache.commons.lang3.StringUtils.join(cosyPidList, ",");
        log.info(String.format("Found cosy pid list: %s", pidListStr == null ? "null" : pidListStr));
        if (CollectionUtils.isNotEmpty(cosyPidList)) {
            return new Pair(36510, (Long) cosyPidList.get(0));
        } else {
            return ProcessUtils.isWindowsPlatform() ? new Pair(36510, 0L) : null;
        }
    }

    private Pair<Integer, Long> checkInfoFile(@NotNull File infoFile) {
        if (infoFile == null) {
            //$$$reportNull$$$0(0);
        }

        if (!infoFile.exists()) {
            log.info(".info file not exist, wait 100ms and retry");
        } else {
            try {
                String rawText = FileUtils.readFileToString(infoFile, StandardCharsets.UTF_8);
                if (rawText != null && !rawText.isEmpty()) {
                    String[] lines = rawText.split("\r\n|\n");
                    if (lines.length != 2) {
                        log.warn(".info file is empty or has more or less than 2 lines:" + rawText);
                        return null;
                    }

                    Long port = com.tooneCode.util.StringUtils.getNumberFromString(lines[0]);
                    Long pid = com.tooneCode.util.StringUtils.getNumberFromString(lines[1]);
                    log.info("Read.info file get port:" + port + ", pid:" + pid);
                    if (port != null && pid != null) {
                        return new Pair(port.intValue(), pid);
                    }

                    log.warn("Cannot get port and pid from.info file, check failed");
                    return null;
                }

                log.warn(".info file is empty, check failed");
                return null;
            } catch (IOException var6) {
                IOException e = var6;
                log.warn("Parsing .info file encountered Exception", e);
            } catch (Throwable var7) {
                Throwable throwable = var7;
                log.warn("Check info file encountered Throwable", throwable);
            }
        }

        return null;
    }

    private List<LanguageWebSocketService> getAllLanguageServices() {
        return (List) this.languageServiceMap.values().stream().filter((languageService) -> {
            return languageService != null;
        }).collect(Collectors.toList());
    }

    public void updateConfig(com.tooneCode.core.model.params.ChangeUserSettingParams params) {
        INSTANCE.getAllLanguageServices().forEach((languageService) -> {
            languageService.changeUsageStatisticsSetting(params);
        });
    }
}
