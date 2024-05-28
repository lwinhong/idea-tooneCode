package com.tooneCode.listeners;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.EditorKind;
import com.intellij.openapi.editor.EditorModificationUtil;
import com.intellij.openapi.project.Project;
import com.tooneCode.common.CodeCacheKeys;
import com.tooneCode.common.CodeSetting;
import com.tooneCode.completion.model.CodeCompletionItem;
import com.tooneCode.completion.template.TemplateSettingLoader;
import com.tooneCode.editor.CodeInlayManager;
import com.tooneCode.editor.model.CompletionTriggerConfig;
import com.tooneCode.editor.model.InlayDisposeEventEnum;
import com.tooneCode.editor.model.InlayTriggerEventEnum;
import com.tooneCode.services.TelemetryService;
import com.tooneCode.services.model.Features;
import com.tooneCode.services.model.TextChangeContext;
import com.tooneCode.ui.config.CodePersistentSetting;
import com.tooneCode.util.CompletionUtil;
import com.tooneCode.util.DocumentUtils;
import com.tooneCode.util.EditorUtil;
import com.tooneCode.util.PsiUtils;
import com.tooneCode.constants.CodeCommands;
import com.tooneCode.util.LanguageUtil;
import org.jetbrains.annotations.NotNull;

import com.intellij.codeInsight.lookup.Lookup;
import com.intellij.codeInsight.lookup.LookupElement;
import com.intellij.codeInsight.lookup.LookupManager;
import com.intellij.codeInsight.template.Template;
import com.intellij.codeInsight.template.TemplateManager;
import com.intellij.openapi.command.CommandEvent;
import com.intellij.openapi.command.CommandListener;
import com.intellij.openapi.editor.VisualPosition;
import com.intellij.openapi.editor.ex.DocumentEx;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiElement;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Stream;

import lombok.Generated;
import org.apache.commons.lang3.StringUtils;

public class CodeCommandListener implements CommandListener {
    private static final Logger LOG = Logger.getInstance(CodeCommandListener.class);
    private static final List<String> skipCommands = Arrays.asList("Undo", "Backspace", "Paste", "Redo", "Copy", "撤消粘贴", "退格", "粘贴", "重做粘贴", "复制");
    private static final Set<String> TEXT_CHANGE_COMMANDS = Set.of("Paste", "粘贴", "重做粘贴", "Typing", "输入", "Choose Lookup Item", "选择查询条目", "选择查询项");
    private static final Set<String> PASTE_COMMANDS = Set.of("Paste", "粘贴", "重做粘贴");
    private static final Set<String> SELECT_LOOKUP_COMMANDS = Set.of("Choose Lookup Item", "选择查询条目", "选择查询项");
    private static final Set<String> LEFT_BRACKET_CHARS = Set.of("{", "(");
    private static final Set<String> RIGHT_BRACKET_CHARS = Set.of("}", ")");
    private static final Set<String> ALLOW_COMMANDS = Set.of("Typing", "输入");
    private static final List<String> ignoreCommands = Arrays.asList("Apply tooneCode Inline Suggestion", "Async Render TooneCode Suggestion", "Chat Insert Code");
    private final Project project;
    private final AtomicInteger activeCommands;
    private final AtomicBoolean startedWithEditor;
    private final AtomicReference<UndoTransparentActionState> undoTransparentActionStamp;

    public CodeCommandListener(@NotNull Project project) {
        super();
        this.activeCommands = new AtomicInteger();
        this.startedWithEditor = new AtomicBoolean(false);
        this.undoTransparentActionStamp = new AtomicReference<>();
        this.project = project;
    }

    public void commandStarted(@NotNull CommandEvent event) {
        if (this.activeCommands.getAndIncrement() > 0) {
            LOG.info("Skipping nested commandStarted. Event: " + event);
        } else {
            Editor editor = EditorUtil.getSelectedEditorSafely(this.project);
            if (editor != null) {
                this.startedWithEditor.set(true);
                CodeCacheKeys.COMMAND_STATE_KEY.set(editor, createCommandState(editor));
            } else {
                this.startedWithEditor.set(false);
            }

        }
    }

    public void commandFinished(@NotNull CommandEvent event) {

        if (this.activeCommands.decrementAndGet() > 0) {
            LOG.info("Skipping nested commandFinished. Event: " + event);
        } else if (this.startedWithEditor.get() && event.getCommandName() != null) {
            Editor editor = EditorUtil.getSelectedEditorSafely(this.project);
            if (editor == null) {
                LOG.debug("invalid editor from project:" + this.project);
            } else {
                Document document = event.getDocument();
                if (document == null) {
                    document = editor.getDocument();
                }

                if (!DocumentUtils.isValidEditorDocument(document)) {
                    LOG.debug("invalid document:" + document);
                } else {
                    CodeInlayManager editorManager = CodeInlayManager.getInstance();
                    if (editorManager.isAvailable(editor) && editor.getCaretModel().getCaretCount() <= 1 && EditorUtil.isAvailableLanguage(editor)) {
                        if (EditorUtil.isActiveProjectEditor(editor)) {
                            CommandEditorState commandStartState = CodeCacheKeys.COMMAND_STATE_KEY.get(editor);
                            if (commandStartState != null) {
                                CommandEditorState commandEndState = createCommandState(editor);
                                this.recordTextChange(editor, event, commandStartState, commandEndState);
                            }

                            TemplateManager templateManager = TemplateManager.getInstance(this.project);
                            if (templateManager.getActiveTemplate(editor) != null) {
                                LOG.debug("Skipping template completion. Event: " + event.getCommandName());
                                editorManager.disposeInlays(editor, InlayDisposeEventEnum.LIVE_TEMPLATE, event.getCommandName());
                            } else if (commandStartState == null || !StringUtils.isNotBlank(commandStartState.getSelectionText())) {
                                String commandName = event.getCommandName().toLowerCase(Locale.ROOT);
                                if (!ignoreCommands.contains(event.getCommandName()) && !commandName.contains("TooneCode") && !CodeCommands.ALL_TOONECODE_COMMANDS.contains(event.getCommandName())) {
                                    if (event.getCommandName() != null && this.isSkipCommand(event.getCommandName())) {
                                        LOG.debug("Skipping invalid command. Event: " + event.getCommandName());
                                        editor.getDocument().putUserData(CodeCacheKeys.KEY_LAST_CHANGE_TEXT, null);
                                        editorManager.disposeInlays(editor, InlayDisposeEventEnum.CHANGE_COMMAND, event.getCommandName());
                                    } else {
                                        if (commandStartState != null) {
                                            CommandEditorState commandEndState = createCommandState(editor);
                                            this.recordType(editor, event, commandStartState, commandEndState);
                                            if (!"".equals(event.getCommandName()) && LOG.isDebugEnabled()) {
                                                LOG.debug("document command: " + event.getCommandName());
                                            }

                                            boolean needTrggier = false;
                                            if (ALLOW_COMMANDS.contains(event.getCommandName())) {
                                                needTrggier = true;
                                            } else if (this.triggerWhenLookup(editor)) {
                                                needTrggier = true;
                                            }

                                            if (needTrggier && this.isDocumentModified(editor, commandStartState, commandEndState) && !this.isCompletionTemplate(editor)) {
                                                editorManager.editorChanged(CompletionTriggerConfig.defaultConfig(InlayTriggerEventEnum.TYPING), editor);
                                            } else if (isCaretPositionChange(commandStartState, commandEndState)) {
                                                editorManager.disposeInlays(editor, InlayDisposeEventEnum.CHANGE_CARET, event.getCommandName());
                                            } else {
                                                editorManager.cancelCompletion(editor);
                                            }
                                        }

                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private boolean triggerWhenLookup(@NotNull Editor editor) {

        boolean needTrggier = false;
        Lookup activeLookup = LookupManager.getActiveLookup(editor);
        if (activeLookup != null) {
            CodeSetting settings = CodePersistentSetting.getInstance().getState();
            if (settings != null && settings.getParameter() != null && settings.getParameter().getCloud().isShowInlineWhenIDECompletion()) {
                needTrggier = true;
            } else {
                List<LookupElement> items = activeLookup.getItems();
                if (!items.isEmpty()) {
                    int validCount = 0;
                    Iterator var7 = items.iterator();

                    while (var7.hasNext()) {
                        LookupElement item = (LookupElement) var7.next();
                        if (!(item.getObject() instanceof CodeCompletionItem) && !item.getClass().getName().contains("LookupElementDecorator$InsertingDecorator")) {
                            ++validCount;
                        }
                    }

                    if (validCount == 0) {
                        needTrggier = true;
                    }
                }
            }
        }

        return needTrggier;
    }

    private boolean isSkipCommand(String commandName) {
        Stream<String> var10000 = skipCommands.stream();
        Objects.requireNonNull(commandName);
        boolean skip = var10000.anyMatch(commandName::contains);
        if (!skip && commandName.toLowerCase(Locale.ROOT).contains("vim")) {
            skip = true;
        }

        return skip;
    }

    public void undoTransparentActionStarted() {
        Editor editor = EditorUtil.getSelectedEditorSafely(this.project);
        this.undoTransparentActionStamp.set(editor != null ? createUndoTransparentState(editor) : null);
    }

    public void undoTransparentActionFinished() {
    }

    private static long getDocumentStamp(@NotNull Document document) {
        return document instanceof DocumentEx ? (long) ((DocumentEx) document).getModificationSequence() : document.getModificationStamp();
    }

    @NotNull
    private static CommandEditorState createCommandState(@NotNull Editor editor) {

        int offset = editor.getCaretModel().getOffset();
        int line = editor.getDocument().getLineNumber(offset);
        int lineStartOffset = editor.getDocument().getLineStartOffset(line);
        int lineEndOffset = editor.getDocument().getLineEndOffset(line);
        String linePrefix = editor.getDocument().getText(new TextRange(lineStartOffset, offset));
        String lineSuffix = editor.getDocument().getText(new TextRange(offset, lineEndOffset));
        return new CommandEditorState(getDocumentStamp(editor.getDocument()), editor.getCaretModel().getVisualPosition(),
                linePrefix, lineSuffix, editor.getSelectionModel().getSelectedText(), editor.getCaretModel().getOffset());
    }

    @NotNull
    private static UndoTransparentActionState createUndoTransparentState(@NotNull Editor editor) {
        return new UndoTransparentActionState(editor, getDocumentStamp(editor.getDocument()));
    }

    private boolean isDocumentModified(@NotNull Editor editor, @NotNull CommandEditorState first, @NotNull CommandEditorState second) {

        if (first.modificationStamp == second.modificationStamp) {
            return false;
        } else if (!editor.getEditorKind().equals(EditorKind.MAIN_EDITOR) && !ApplicationManager.getApplication().isUnitTestMode()) {
            return true;
        } else if (!EditorModificationUtil.checkModificationAllowed(editor)) {
            return true;
        } else {
            try {
                PsiElement element = PsiUtils.getCaratElement(editor);
                if (element != null) {
                    Logger var10000 = LOG;
                    Class var10001 = element.getClass();
                    var10000.debug("caret element class:" + var10001 + " parent:" + element.getParent().getClass());
                } else {
                    LOG.debug("caret commandFinished isDocumentModification element is null");
                }

                if (element != null && PsiUtils.checkCaretAround(editor) && !PsiUtils.isJavaMethodNewLine(editor, element, second.getOffset(), first.getOffset())
                        && !PsiUtils.isLiteralElement(editor, element) && !PsiUtils.isImportElement(element, editor)) {
                    return CompletionUtil.isValidDocumentChange(editor.getDocument(), second.getOffset(), first.getOffset(), element);
                } else {
                    LOG.debug("commandFinished isDocumentModification invalid psi");
                    return false;
                }
            } catch (Exception var5) {
                Exception e = var5;
                LOG.error("fail to check document modified.", e);
                return false;
            }
        }
    }

    private boolean isCompletionTemplate(@NotNull Editor editor) {
        String currentContent = DocumentUtils.getCurrentLine(editor);
        if (StringUtils.isNotBlank(currentContent) && TemplateSettingLoader.getInstance().getCurrentTemplateKeys(editor).contains(currentContent.trim())) {
            LOG.debug(String.format("Current content %s matches with live template, skip", currentContent));
            return true;
        } else {
            return false;
        }
    }

    private void recordType(Editor editor, CommandEvent event, CommandEditorState first, CommandEditorState second) {
        int previousOffset = first.getOffset();
        int newOffset = second.getOffset();
        if (newOffset >= 0 && previousOffset <= newOffset) {
            String addedText = editor.getDocument().getText(new TextRange(previousOffset, newOffset));
            TelemetryService.getInstance().typeRecord(editor, addedText);
            TelemetryService.getInstance().telemetryCommand(event);
        }
    }

    private void recordTextChange(Editor editor, CommandEvent event, CommandEditorState first, CommandEditorState second) {
        String commandName = event.getCommandName();
        if (TEXT_CHANGE_COMMANDS.contains(commandName)) {
            int previousOffset = first.getOffset();
            int newOffset = second.getOffset();
            if (newOffset >= 0 && previousOffset <= newOffset) {
                String addedText = editor.getDocument().getText(new TextRange(previousOffset, newOffset));
                int lineNumber = editor.getDocument().getLineNumber(previousOffset);
                boolean accept = false;
                String source = "completion";
                String filePath;
                if (PASTE_COMMANDS.contains(commandName)) {
                    filePath = EditorUtil.getCopyPasteText();
                    if (filePath != null) {
                        addedText = filePath;
                    }

                    String copyContent = (String) CodeCacheKeys.KEY_COPY_PASTE.get(ApplicationManager.getApplication());
                    if (StringUtils.isNotBlank(copyContent) && com.tooneCode.util.StringUtils.matchCopyContent(addedText, copyContent)) {
                        accept = true;
                        source = "chat";
                    } else if (!Features.REPORT_TEXT_CHANGE_PASTE_ENABLE.booleanValue()) {
                        LOG.debug("ignore other paste event for " + addedText);
                        return;
                    }
                } else if (SELECT_LOOKUP_COMMANDS.contains(commandName)) {
                    filePath = (String) CodeCacheKeys.KEY_SELECT_LOOKUP_ITEM.get(editor);
                    if (StringUtils.isNotBlank(filePath) && filePath.contains(addedText.trim())) {
                        CodeCacheKeys.KEY_SELECT_LOOKUP_ITEM.set(editor, null);
                        return;
                    }
                } else if (previousOffset > 1 && addedText.startsWith("\n") && addedText.trim().isEmpty()) {
                    if (StringUtils.isNotBlank(first.getCaretPrevChar()) && StringUtils.isNotBlank(first.getCaretNextChar()) && LEFT_BRACKET_CHARS.contains(first.getCaretPrevChar()) && RIGHT_BRACKET_CHARS.contains(first.getCaretNextChar())) {
                        addedText = addedText + "\n}";
                    }
                } else {
                    TemplateManager templateManager = TemplateManager.getInstance(this.project);
                    Template template = templateManager.getActiveTemplate(editor);
                    if (template != null && StringUtils.isNotBlank(template.getKey())) {
                    }
                }

                LOG.debug("recordTextChange commandName:" + commandName + " accept:" + accept + " addedText:" + addedText);
                filePath = EditorUtil.getEditorFilePath(editor);
                TelemetryService.getInstance().telemetryTextChange(new TextChangeContext(editor.getProject(), filePath, addedText, lineNumber, accept,
                        LanguageUtil.getLanguageByFilePath(filePath), source));
            }
        }
    }

    private static boolean isCaretPositionChange(@NotNull CommandEditorState first, @NotNull CommandEditorState second) {
        return !first.visualPosition.equals(second.visualPosition);
    }

    public static final class CommandEditorState {
        private final long modificationStamp;
        private final VisualPosition visualPosition;
        private final String lineSuffix;
        private final String linePrefix;
        private final String selectionText;
        private final int offset;

        public CommandEditorState(long modificationStamp, VisualPosition visualPosition, String linePrefix, String lineSuffix, String selectionText, int offset) {
            this.modificationStamp = modificationStamp;
            this.visualPosition = visualPosition;
            this.linePrefix = linePrefix;
            this.lineSuffix = lineSuffix;
            this.selectionText = selectionText;
            this.offset = offset;
        }

        public String getCaretPrevChar() {
            return this.linePrefix != null && !this.linePrefix.isEmpty() ? this.linePrefix.substring(this.linePrefix.length() - 1) : null;
        }

        public String getCaretNextChar() {
            return this.lineSuffix != null && !this.lineSuffix.isEmpty() ? this.lineSuffix.substring(0, 1) : null;
        }

        @Generated
        public long getModificationStamp() {
            return this.modificationStamp;
        }

        @Generated
        public VisualPosition getVisualPosition() {
            return this.visualPosition;
        }

        @Generated
        public String getLineSuffix() {
            return this.lineSuffix;
        }

        @Generated
        public String getLinePrefix() {
            return this.linePrefix;
        }

        @Generated
        public String getSelectionText() {
            return this.selectionText;
        }

        @Generated
        public int getOffset() {
            return this.offset;
        }

        @Generated
        public boolean equals(Object o) {
            if (o == this) {
                return true;
            } else if (!(o instanceof CommandEditorState)) {
                return false;
            } else {
                CommandEditorState other = (CommandEditorState) o;
                if (this.getModificationStamp() != other.getModificationStamp()) {
                    return false;
                } else {
                    label63:
                    {
                        Object this$visualPosition = this.getVisualPosition();
                        Object other$visualPosition = other.getVisualPosition();
                        if (this$visualPosition == null) {
                            if (other$visualPosition == null) {
                                break label63;
                            }
                        } else if (this$visualPosition.equals(other$visualPosition)) {
                            break label63;
                        }

                        return false;
                    }

                    Object this$lineSuffix = this.getLineSuffix();
                    Object other$lineSuffix = other.getLineSuffix();
                    if (this$lineSuffix == null) {
                        if (other$lineSuffix != null) {
                            return false;
                        }
                    } else if (!this$lineSuffix.equals(other$lineSuffix)) {
                        return false;
                    }

                    Object this$linePrefix = this.getLinePrefix();
                    Object other$linePrefix = other.getLinePrefix();
                    if (this$linePrefix == null) {
                        if (other$linePrefix != null) {
                            return false;
                        }
                    } else if (!this$linePrefix.equals(other$linePrefix)) {
                        return false;
                    }

                    label42:
                    {
                        Object this$selectionText = this.getSelectionText();
                        Object other$selectionText = other.getSelectionText();
                        if (this$selectionText == null) {
                            if (other$selectionText == null) {
                                break label42;
                            }
                        } else if (this$selectionText.equals(other$selectionText)) {
                            break label42;
                        }

                        return false;
                    }

                    if (this.getOffset() != other.getOffset()) {
                        return false;
                    } else {
                        return true;
                    }
                }
            }
        }

        @Generated
        public int hashCode() {
            int result = 1;
            long $modificationStamp = this.getModificationStamp();
            result = result * 59 + (int) ($modificationStamp >>> 32 ^ $modificationStamp);
            Object $visualPosition = this.getVisualPosition();
            result = result * 59 + ($visualPosition == null ? 43 : $visualPosition.hashCode());
            Object $lineSuffix = this.getLineSuffix();
            result = result * 59 + ($lineSuffix == null ? 43 : $lineSuffix.hashCode());
            Object $linePrefix = this.getLinePrefix();
            result = result * 59 + ($linePrefix == null ? 43 : $linePrefix.hashCode());
            Object $selectionText = this.getSelectionText();
            result = result * 59 + ($selectionText == null ? 43 : $selectionText.hashCode());
            result = result * 59 + this.getOffset();
            return result;
        }

        @Generated
        public String toString() {
            long var10000 = this.getModificationStamp();
            return "CosyCommandListener.CommandEditorState(modificationStamp=" + var10000 + ", visualPosition=" + this.getVisualPosition() + ", lineSuffix=" + this.getLineSuffix() + ", linePrefix=" + this.getLinePrefix() + ", selectionText=" + this.getSelectionText() + ", offset=" + this.getOffset() + ")";
        }
    }

    private static final class UndoTransparentActionState {
        private final @NotNull Editor editor;
        private final long modificationStamp;

        public UndoTransparentActionState(@NotNull Editor editor, long modificationStamp) {
            super();
            this.editor = editor;
            this.modificationStamp = modificationStamp;
        }

        @Generated
        public @NotNull Editor getEditor() {
            return this.editor;
        }

        @Generated
        public long getModificationStamp() {
            return this.modificationStamp;
        }

        @Generated
        public boolean equals(Object o) {
            if (o == this) {
                return true;
            } else if (!(o instanceof UndoTransparentActionState)) {
                return false;
            } else {
                UndoTransparentActionState other = (UndoTransparentActionState) o;
                Object this$editor = this.getEditor();
                Object other$editor = other.getEditor();
                if (this$editor == null) {
                    if (other$editor == null) {
                        return this.getModificationStamp() == other.getModificationStamp();
                    }
                } else if (this$editor.equals(other$editor)) {
                    return this.getModificationStamp() == other.getModificationStamp();
                }

                return false;
            }
        }

        @Generated
        public int hashCode() {
            int result = 1;
            Object $editor = this.getEditor();
            result = result * 59 + $editor.hashCode();
            long $modificationStamp = this.getModificationStamp();
            result = result * 59 + Long.hashCode($modificationStamp);
            return result;
        }

        @Generated
        public String toString() {
            Editor var10000 = this.getEditor();
            return "CodeCommandListener.UndoTransparentActionState(editor=" + var10000 + ", modificationStamp=" + this.getModificationStamp() + ")";
        }
    }
}
