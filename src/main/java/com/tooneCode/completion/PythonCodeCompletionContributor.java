package com.tooneCode.completion;

import com.intellij.codeInsight.completion.CompletionContributor;
import com.intellij.codeInsight.completion.CompletionParameters;
import com.intellij.codeInsight.completion.CompletionResultSet;
import com.intellij.codeInsight.completion.CompletionSorter;
import com.intellij.codeInsight.completion.InsertHandler;
import com.intellij.codeInsight.completion.InsertionContext;
import com.intellij.codeInsight.completion.PrefixMatcher;
import com.intellij.codeInsight.lookup.Lookup;
import com.intellij.codeInsight.lookup.LookupElement;
import com.intellij.codeInsight.lookup.LookupElementBuilder;
import com.intellij.codeInsight.lookup.LookupElementPresentation;
import com.intellij.codeInsight.lookup.LookupElementRenderer;
import com.intellij.codeInsight.lookup.LookupEx;
import com.intellij.codeInsight.lookup.LookupManager;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.LogicalPosition;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.TextRange;
import com.tooneCode.common.CodeBundle;
import com.tooneCode.common.CodeSetting;
import com.tooneCode.completion.model.CodeCompletionItem;
import com.tooneCode.completion.model.CompletionContext;
import com.tooneCode.completion.template.TemplateSettingLoader;
import com.tooneCode.core.TooneCoder;
import com.tooneCode.core.model.CompletionParams;
import com.tooneCode.listeners.CodeLookupListener;
import com.tooneCode.ui.config.CodePersistentSetting;
import com.tooneCode.util.CompletionUtil;
import com.tooneCode.util.DocumentUtils;
import icons.CommonIcons;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.annotation.Nonnull;

import org.eclipse.lsp4j.CompletionItem;
import org.eclipse.lsp4j.Position;
import org.eclipse.lsp4j.Range;
import org.eclipse.lsp4j.TextDocumentIdentifier;
import org.eclipse.lsp4j.TextEdit;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class PythonCodeCompletionContributor extends CompletionContributor {
    private static final Logger log = Logger.getInstance(PythonCodeCompletionContributor.class);
    private static final long COMPLETION_TIMEOUT = 500L;
    private static final String PLACEHOLDER_PATTERN_STR = "(\\$\\d)|(\\$\\{\\d\\:([a-z0-9_]+)\\})";
    private static final Pattern DEFAULT_PLACEHOLDER_PATTERN = Pattern.compile("\\$\\{\\d\\:([a-z0-9_]+)\\}");
    private static final Pattern DOLLAR_ZERO_PATTERN = Pattern.compile("\\$0");

    public void fillCompletionVariants(@NotNull CompletionParameters parameters, @NotNull CompletionResultSet result) {
        if (true) {
            //这里直接返回了。以下逻辑是实现自动完成的，逻辑没有问题。
            //有inlay了感觉这个可有可无，后面服务器够强大可以放开
            return;
        }
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
                                    if (log.isDebugEnabled()) {
                                        log.debug(items.toString());
                                    }

                                    result = result.withPrefixMatcher((new CodePrefixMatcher(originMatcher)).cloneWithPrefix(originMatcher.getPrefix())).withRelevanceSorter(CompletionSorter.defaultSorter(parameters, originMatcher).weigh(new CodeLookupElementWeigher()));
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
        Set<String> completionItems = new HashSet();
        Iterator var5 = items.iterator();

        while (var5.hasNext()) {
            CompletionItem item = (CompletionItem) var5.next();
            String completionText = CompletionUtil.getCompletionText(item);
            completionItems.add(completionText);
        }

        try {
            result.runRemainingContributors(params, (itemx) -> {
                String lookupString = itemx.getLookupElement().getLookupString();
                if (!completionItems.contains(lookupString.trim())) {
                    result.passResult(itemx);
                } else {
                    log.info("filter other completion item:" + lookupString);
                }

            }, true);
        } catch (Exception var8) {
            Exception e = var8;
            log.warn("fail to run remaining contributors, caused by " + e.getMessage());
        }

    }

    private ArrayList<LookupElement> createCompletions(CompletionContext context, List<CompletionItem> items) {
        ArrayList<LookupElement> elements = new ArrayList();
        int limit = this.getCompletionLimit(context);
        String prefix = context.getPrefix();
        LookupEx lookupEx = LookupManager.getActiveLookup(context.getParameters().getEditor());

        for (int i = 0; i < items.size() && elements.size() < limit; ++i) {
            CompletionItem item = (CompletionItem) items.get(i);
            String newText = CompletionUtil.getCompletionText(item);
            if (prefix != null && !"".equals(prefix.trim()) && !newText.trim().startsWith(prefix)) {
                int cutStart;
                for (cutStart = newText.length(); cutStart > 0; --cutStart) {
                    String head = newText.substring(0, cutStart);
                    if (prefix.toLowerCase(Locale.ROOT).trim().endsWith(head.toLowerCase(Locale.ROOT))) {
                        break;
                    }
                }

                newText = prefix + newText.substring(cutStart);
            }

            if (newText.startsWith(".")) {
                newText = newText.substring(1);
            }

            CompletionUtil.setCompletionText(item, newText);
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
                PythonCodeCompletionContributor.this.generateLookupElementPresentation(presentation, lookupElement.getOriginItem().getLabel());
            }
        }).withInsertHandler(new InsertHandler<LookupElement>() {
            public void handleInsert(@NotNull InsertionContext insertionContext, @NotNull LookupElement element) {
                Document document = insertionContext.getDocument();
                if (document != null) {
                    int tail = insertionContext.getTailOffset();
                    CodeCompletionItem lookupElement = (CodeCompletionItem) element.getObject();
                    TextEdit textEdit = (TextEdit) lookupElement.getOriginItem().getTextEdit().getLeft();
                    Range lookupElementRange = textEdit.getRange();
                    String newText = CompletionUtil.getCompletionText(lookupElement.getOriginItem());
                    String prefix = context.getPrefix();
                    int startPivot = document.getLineStartOffset(lookupElementRange.getStart().getLine()) + lookupElementRange.getStart().getCharacter();
                    int endPivot = document.getLineStartOffset(lookupElementRange.getEnd().getLine()) + lookupElementRange.getEnd().getCharacter();
                    DocumentUtils.getCompleteLine(document, lookupElementRange.getStart().getLine());

                    try {
                        int caretPivot = 0;
                        boolean dollarIsReplaced = PythonCodeCompletionContributor.this.replaceDollarZeroAndMoveCaret(insertionContext, true);
                        if (!dollarIsReplaced && caretPivot > 0) {
                            insertionContext.getEditor().getCaretModel().moveToOffset(caretPivot);
                        }
                    } catch (RuntimeException var15) {
                        RuntimeException re = var15;
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
        presentation.setTypeText(CodeBundle.message("local.completion.flag.text"));
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
