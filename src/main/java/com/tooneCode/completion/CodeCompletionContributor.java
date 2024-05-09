package com.tooneCode.completion;

import com.intellij.codeInsight.completion.*;
import com.intellij.codeInsight.lookup.*;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.LogicalPosition;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.TextRange;
import com.intellij.codeInsight.editorActions.smartEnter.SmartEnterProcessor;
import com.intellij.psi.PsiFile;
import com.tooneCode.common.CodeBundle;
import com.tooneCode.common.CodeSetting;
import com.tooneCode.completion.model.CodeCompletionItem;
import com.tooneCode.completion.model.CompletionContext;
import com.tooneCode.completion.template.TemplateSettingLoader;
import com.tooneCode.core.TooneCoder;
import com.tooneCode.core.model.CompletionParams;
import com.tooneCode.listeners.CodeLookupListener;
import com.tooneCode.ui.config.CodePersistentSetting;
import com.tooneCode.util.DocumentUtils;
import icons.CommonIcons;
import org.eclipse.lsp4j.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import com.tooneCode.util.CompletionUtil;
import com.tooneCode.util.StringUtils;

import javax.annotation.Nonnull;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CodeCompletionContributor extends CompletionContributor {
    private static final Logger log = Logger.getInstance(CodeCompletionContributor.class);
    private static final long COMPLETION_TIMEOUT = 500L;
    private static final String PLACEHOLDER_PATTERN_STR = "(\\$\\d)|(\\$\\{\\d\\:([a-z0-9_]+)\\})";
    private static final Pattern DEFAULT_PLACEHOLDER_PATTERN = Pattern.compile("\\$\\{\\d\\:([a-z0-9_]+)\\}");
    private static final Pattern DOLLAR_ZERO_PATTERN = Pattern.compile("\\$0");

    public CodeCompletionContributor() {
    }

    public void fillCompletionVariants(@NotNull CompletionParameters parameters, @NotNull CompletionResultSet result) {
        if (parameters.getEditor().getProject() != null) {
            CodeSetting setting = CodePersistentSetting.getInstance().getState();
            if (setting != null && !setting.getParameter().getLocal().getEnable()) {
                log.info("ignore completion, the switch closed");
            } else {
                Project project = parameters.getEditor().getProject();
                if (TooneCoder.INSTANCE.checkCosy(project)) {
                    Document document = parameters.getEditor().getDocument();
                    if (parameters.getEditor().getCaretModel().getCaretCount() > 1) {
                        log.info("Multi editing mode, skip completion");
                    } else {
                        this.addCosyLookupListener(parameters);
                        PrefixMatcher originMatcher = result.getPrefixMatcher();
                        CompletionParams completionParams = new CompletionParams();
                        completionParams.setUseLocalModel(true);
                        completionParams.setUseRemoteModel(false);
                        String filePath = parameters.getOriginalFile().getVirtualFile().getPath();
                        String fileContent = parameters.getEditor().getDocument().getText();
                        if (fileContent.length() >= 5000000) {
                            log.warn("File content is larger than 10M, skip completion");
                        } else {
                            CompletionContext context = this.createCompletionContext(parameters, result);
                            completionParams.setFileContent(fileContent);
                            int line = context.getPosition().line;
                            int column = parameters.getOffset() - document.getLineStartOffset(line);
                            column = column > 0 ? column : context.getPosition().column;
                            completionParams.setPosition(new Position(line, column));
                            completionParams.setTextDocument(new TextDocumentIdentifier(filePath));
                            completionParams.setUseRemoteModel(false);
                            completionParams.setUseLocalModel(true);
                            completionParams.setRequestId(UUID.randomUUID().toString());
                            String currentContent = DocumentUtils.getCompleteLine(document, line);
                            if (currentContent != null && TemplateSettingLoader.getInstance().getTemplateKeys().contains(currentContent.trim())) {
                                log.info(String.format("Current content %s matches with live template, skip", currentContent));
                            } else {
                                List<CompletionItem> items = null;
                                if (TooneCoder.INSTANCE.getLanguageService(project) != null) {
                                    items = TooneCoder.INSTANCE.getLanguageService(project).completionWithDebouncer(completionParams, 500L);
                                }

                                if (items != null && !items.isEmpty()) {
                                    log.debug(items.toString());
                                    result = result.withPrefixMatcher((new CodePrefixMatcher(originMatcher))
                                            .cloneWithPrefix(originMatcher.getPrefix()));

                                    var sort = CompletionSorter.defaultSorter(parameters, originMatcher);
                                    result = result.withRelevanceSorter(sort
                                            .weigh(new CodeLookupElementWeigher()));
                                    result.restartCompletionOnAnyPrefixChange();
                                    result.addAllElements(this.createCompletions(context, items));
                                    this.duplicateOtherItems(parameters, result, items);
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private void duplicateOtherItems(CompletionParameters params, @Nonnull CompletionResultSet result, List<CompletionItem> items) {
        Set<String> completionItems = new HashSet<>();

        for (CompletionItem item : items) {
            String completionText = CompletionUtil.getCompletionText(item);
            completionItems.add(completionText);
        }

        result.runRemainingContributors(params, (itemx) -> {
            String lookupString = itemx.getLookupElement().getLookupString();
            if (!completionItems.contains(lookupString.trim())) {
                result.passResult(itemx);
            } else {
                log.info("filter other completion item:" + lookupString);
            }

        }, true);
    }

    private ArrayList<LookupElement> createCompletions(CompletionContext context, List<CompletionItem> items) {
        ArrayList<LookupElement> elements = new ArrayList<>();
        int limit = this.getCompletionLimit(context);
        LookupEx lookupEx = LookupManager.getActiveLookup(context.getParameters().getEditor());

        for (int i = 0; i < items.size() && elements.size() < limit; ++i) {
            CompletionItem item = items.get(i);
            LookupElement lookupElement = this.createLookupElement(context, i, item, lookupEx);
            elements.add(lookupElement);
        }

        return elements;
    }

    private LookupElement createLookupElement(final CompletionContext context, int index, CompletionItem item, @Nullable Lookup lookup) {
        CodeCompletionItem cosyItem = new CodeCompletionItem(index, item, context.getCursorPrefix(), context.getCursorSuffix());
        String completionText = CompletionUtil.getCompletionText(item);
        LookupElementBuilder lookupElementBuilder = LookupElementBuilder.create(cosyItem, completionText).withRenderer(new LookupElementRenderer<LookupElement>() {
            public void renderElement(LookupElement element, LookupElementPresentation presentation) {
                CodeCompletionItem lookupElement = (CodeCompletionItem) element.getObject();
                CodeCompletionContributor.this.generateLookupElementPresentation(presentation, lookupElement.getOriginItem().getLabel());
            }
        }).withInsertHandler(new InsertHandler<LookupElement>() {
            public void handleInsert(@NotNull InsertionContext insertionContext, @NotNull LookupElement element) {

                int tail = insertionContext.getTailOffset();
                CodeCompletionItem lookupElement = (CodeCompletionItem) element.getObject();
                TextEdit textEdit = (TextEdit) lookupElement.getOriginItem().getTextEdit().getLeft();
                Range lookupElementRange = textEdit.getRange();
                String newText = CompletionUtil.getCompletionText(lookupElement.getOriginItem());
                Document document = insertionContext.getDocument();
                if (document != null) {
                    String prefix = context.getPrefix();
                    int startPivot = document.getLineStartOffset(lookupElementRange.getStart().getLine()) + lookupElementRange.getStart().getCharacter();
                    int endPivot = document.getLineStartOffset(lookupElementRange.getEnd().getLine()) + lookupElementRange.getEnd().getCharacter();
                    String startLineContent = DocumentUtils.getCompleteLine(document, lookupElementRange.getStart().getLine());

                    try {
                        document.deleteString(tail - newText.length() + prefix.length(), tail);
                        document.deleteString(startPivot, endPivot);
                        int caretPivot = 0;
                        if (newText.endsWith("\n}")) {
                            String leadingWhitespaces = StringUtils.countLeadingLength(startLineContent, StringUtils.TAB_SPACE_PATTERN);
                            String tabContent = StringUtils.tabContent(leadingWhitespaces);
                            StringBuilder sb = new StringBuilder();
                            if (newText.endsWith("\n$0\n}")) {
                                sb.append(newText, 0, newText.length() - 4).append(leadingWhitespaces).append(tabContent).append("$0\n").append(leadingWhitespaces).append("}");
                                newText = sb.toString();
                            } else {
                                int endCutLength = 1;
                                if (newText.length() > 2 && newText.charAt(newText.length() - 2) == '\n') {
                                    endCutLength = 2;
                                }

                                int beforeLength = newText.length() - endCutLength;
                                sb.append(newText, 0, newText.length() - endCutLength).append(leadingWhitespaces).append(tabContent).append("\n").append(leadingWhitespaces).append("}");
                                newText = sb.toString();
                                caretPivot = startPivot + beforeLength + leadingWhitespaces.length() + tabContent.length();
                            }
                        }

                        document.insertString(startPivot, newText);
                        insertionContext.setTailOffset(startPivot + newText.length());
                        insertionContext.getOffsetMap().addOffset(CompletionInitializationContext.START_OFFSET, startPivot);
                        int tailOffset = insertionContext.getTailOffset();
                        int lineEndOffset = document.getLineEndOffset(lookupElementRange.getEnd().getLine());
                        boolean isEndMatch = tailOffset == lineEndOffset;
                        boolean isBeginWithAt = startLineContent.trim().startsWith("@");
                        boolean isBeginWithLoopSymbol = startLineContent.trim().startsWith("for ") || startLineContent.trim().startsWith("for(");
                        boolean isBeginWithIf = startLineContent.trim().startsWith("if ") || startLineContent.trim().startsWith("if(");
                        boolean isContainingStream = startLineContent.trim().contains("stream()") || startLineContent.trim().contains("parallelStream()");
                        boolean dollarIsReplaced = CodeCompletionContributor.this.replaceDollarZeroAndMoveCaret(insertionContext, true);
                        if (!dollarIsReplaced && caretPivot > 0) {
                            insertionContext.getEditor().getCaretModel().moveToOffset(caretPivot);
                        }

                        if (!dollarIsReplaced && isEndMatch && newText.endsWith(")") && !isBeginWithAt && !isBeginWithLoopSymbol && !isBeginWithIf && !isContainingStream) {
                            SmartEnterProcessor javaSmartEnterProcessor = new SmartEnterProcessor() {
                                @Override
                                public boolean process(@NotNull Project project, @NotNull Editor editor, @NotNull PsiFile psiFile) {
                                    return false;
                                }
                            };
                            javaSmartEnterProcessor.process(insertionContext.getProject(), insertionContext.getEditor(), insertionContext.getFile());
                        }
                    } catch (RuntimeException var23) {
                        RuntimeException re = var23;
                        Logger.getInstance(this.getClass()).warn("Error inserting new suffix. End = " + tail + ", old suffix length = " + lookupElement.getCursorSuffix() + ", new suffix length = " + lookupElement.getCursorSuffix(), re);
                    }

                }
            }
        });
        return lookupElementBuilder;
    }

    /**
     * @deprecated
     */
    @Deprecated
    private void replaceDefaultParameter(InsertionContext insertionContext) {
        String completionText = insertionContext.getDocument().getText(new TextRange(insertionContext.getStartOffset(), insertionContext.getTailOffset()));
        Matcher matcher = DEFAULT_PLACEHOLDER_PATTERN.matcher(completionText);
        int start = insertionContext.getStartOffset();
        int startIndex;
        int endIndex;
        if (matcher != null) {
            for (int lastOffset = 0; matcher.find(); lastOffset = endIndex - startIndex - 1) {
                startIndex = matcher.start();
                endIndex = matcher.end();
                String replacedText = matcher.group(1);
                insertionContext.getDocument().replaceString(start + startIndex - lastOffset, start + endIndex - lastOffset, replacedText);
            }
        }

        insertionContext.commitDocument();
        insertionContext.commitDocument();
    }

    private boolean replaceDollarZeroAndMoveCaret(InsertionContext insertionContext, boolean needMoveCaret) {
        String completionText = insertionContext.getDocument().getText(new TextRange(insertionContext.getStartOffset(), insertionContext.getTailOffset()));
        Matcher matcher = DOLLAR_ZERO_PATTERN.matcher(completionText);
        int start = insertionContext.getStartOffset();
        int caretOffset = insertionContext.getTailOffset();
        boolean isFound = false;
        if (matcher != null) {
            for (int lastOffset = 0; matcher.find(); isFound = true) {
                int startIndex = matcher.start();
                int endIndex = matcher.end();
                String replacedText = "";
                insertionContext.getDocument().replaceString(start + startIndex - lastOffset, start + endIndex - lastOffset, replacedText);
                caretOffset = start + startIndex - lastOffset;
                lastOffset = endIndex - startIndex;
            }
        }

        insertionContext.commitDocument();
        if (needMoveCaret) {
            insertionContext.getEditor().getCaretModel().moveToOffset(caretOffset);
        }

        return isFound;
    }

    private int getCompletionLimit(CompletionContext context) {
        return 4;
    }

    private CompletionContext createCompletionContext(@NotNull CompletionParameters parameters, @NotNull CompletionResultSet result) {
        LogicalPosition position = parameters.getEditor().getCaretModel().getLogicalPosition();
        String cursorPrefix = CompletionUtil.getCursorPrefix(parameters);
        String cursorSuffix = CompletionUtil.getCursorSuffix(parameters);
        return new CompletionContext(cursorPrefix, cursorSuffix, result.getPrefixMatcher().getPrefix(), parameters, result, position);
    }

    private void generateLookupElementPresentation(LookupElementPresentation presentation, String insertionText) {
        presentation.setTypeText(CodeBundle.message("local.completion.flag.text", new Object[0]));
        presentation.setItemTextBold(false);
        presentation.setItemText(insertionText);
        presentation.setIcon(CommonIcons.AI);
    }

    private void addCosyLookupListener(CompletionParameters parameters) {
        if (parameters != null && parameters.getEditor() != null) {
            LookupEx lookupEx = LookupManager.getActiveLookup(parameters.getEditor());
            if (lookupEx != null) {
                lookupEx.removeLookupListener(CodeLookupListener.getInstance());
                lookupEx.addLookupListener(CodeLookupListener.getInstance());
            }
        }
    }
}
