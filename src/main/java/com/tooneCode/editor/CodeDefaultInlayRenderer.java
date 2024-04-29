package com.tooneCode.editor;

import com.intellij.openapi.editor.DefaultLanguageHighlighterColors;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.Inlay;
import com.intellij.openapi.editor.colors.EditorColorsScheme;
import com.intellij.openapi.editor.colors.TextAttributesKey;
import com.intellij.openapi.editor.markup.TextAttributes;
import com.intellij.ui.JBColor;
import com.tooneCode.editor.model.CodeEditorInlayItem;
import com.tooneCode.editor.model.InlayCompletionRequest;
import com.tooneCode.util.FontUtil;
import com.tooneCode.util.KeyboardUtil;
import com.tooneCode.util.ReflectUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.apache.commons.lang3.StringUtils;

import java.awt.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;
import lombok.Generated;


public class CodeDefaultInlayRenderer implements CodeInlayRenderer {

    private static final String INLAY_TEXT_WITHOUT_BACKGROUND_FIELD = "INLAY_TEXT_WITHOUT_BACKGROUND";
    private static final TextAttributes DEFAULT_INLAY_TEXT_THEME = new TextAttributes();
    private static final int TOOLTIPS_OFFSET = 16;
    private static final String SHORTCUT_TOOLTIPS = "Tips: Partial accept";//多语言//CosyBundle.message("completion.apply.partial.tooltips", new Object[0]);
    private final @NotNull List<String> lines;
    private final List<String> tooltipTexts;
    private final @NotNull String content;
    private final @NotNull TextAttributes textAttributes;
    private @Nullable Inlay<CodeInlayRenderer> inlay;
    private int cachedWidth;
    private int cachedHeight;
    private int lineMaxLength;
    private int totalLineCount;
    private CodeEditorInlayItem item;
    private long lastUpdateDisplayTimeLength;

    public CodeDefaultInlayRenderer(@NotNull CodeEditorInlayItem item, @NotNull Editor editor,
                                    @NotNull InlayCompletionRequest request, @NotNull List<String> lines, int lineStartIndex,
                                    int totalLineCount, int lineMaxLength) {
        super();
        if (item == null) {
            //$$$reportNull$$$0(0);
        }

        if (editor == null) {
            //$$$reportNull$$$0(1);
        }

        if (request == null) {
            //$$$reportNull$$$0(2);
        }

        if (lines == null) {
            //$$$reportNull$$$0(3);
        }

        this.cachedWidth = -1;
        this.cachedHeight = -1;
        this.lastUpdateDisplayTimeLength = 0L;
        this.item = item;
        this.lines = this.processLines(request, lines);
        this.lineMaxLength = lineMaxLength;
        this.totalLineCount = totalLineCount;
        this.content = StringUtils.join(this.lines, "\n");
        this.textAttributes = getEdtorTextAttributes(editor);
        this.tooltipTexts = this.createTooltipText(lineStartIndex);
    }

    @Override
    public @NotNull List<String> getContentLines() {

        return this.lines;
    }

    public @Nullable Inlay<CodeInlayRenderer> getInlay() {
        return this.inlay;
    }

    public void setInlay(@NotNull Inlay<CodeInlayRenderer> inlay) {
        this.inlay = inlay;
    }

    public @NotNull TextAttributes getTextAttributes() {
        TextAttributes var10000 = this.textAttributes;
        if (var10000 == null) {
            //$$$reportNull$$$0(16);
        }

        return var10000;
    }

    @Override
    public int calcWidthInPixels(@NotNull Inlay inlay) {
        if (inlay == null) {
            //$$$reportNull$$$0(10);
        }

        if (this.cachedWidth < 0) {
            if (this.totalLineCount == 1) {
                this.cachedWidth = FontUtil.calculateWidth(inlay.getEditor(), this.content, this.lines);
            } else {
                String shortcutText = KeyboardUtil.getShortcutText("SelectCodePartInlayAction", "");
                String tips = String.format("%s %s", shortcutText, SHORTCUT_TOOLTIPS);
                String maxLine = null;
                var var5 = this.tooltipTexts.iterator();

                while (true) {
                    String tip;
                    do {
                        if (!var5.hasNext()) {
                            this.cachedWidth = this.lineMaxLength;
                            FontMetrics metrics = FontUtil.fontMetrics(inlay.getEditor(), FontUtil.getFont(inlay.getEditor(), tips));
                            int tipWidth = maxLine == null ? 0 : metrics.stringWidth(maxLine) + 16;
                            this.cachedWidth += tipWidth;
                            return this.cachedWidth;
                        }

                        tip = var5.next();
                    } while (maxLine != null && tip.length() <= maxLine.length());

                    maxLine = tip;
                }
            }
        }

        return this.cachedWidth;
    }

    private List<String> processLines(InlayCompletionRequest request, List<String> lines) {
        lines = com.tooneCode.util.StringUtils.replaceHeadTabs(lines, false, request.getTabWidth());
        lines = lines.stream().map((line) -> {
            return line.replace("<|cursor|>", "");
        }).collect(Collectors.toList());
        return lines;
    }

    private List<String> createTooltipText(int lineStartIndex) {
        List<String> tooltips = new ArrayList();
//        CosySetting setting = CosyPersistentSetting.getInstance().getState();
//        if (setting != null && this.totalLineCount > 1 && SystemInfo.isMac) {
//            String shortcutText = KeyboardUtil.getShortcutText("SelectCosyPartInlayAction", "");
//            if (StringUtils.isBlank(shortcutText)) {
//                return tooltips;
//            } else {
//                String shortcutIcon = shortcutText.substring(0, shortcutText.length() - 1);
//
//                for (int i = 0; i < this.lines.size(); ++i) {
//                    if (lineStartIndex + i < 9) {
//                        if (lineStartIndex + i == 0 && setting.isShowInlinePartialAcceptTips()) {
//                            tooltips.add(String.format("%s%d %s", shortcutIcon, lineStartIndex + i + 1, SHORTCUT_TOOLTIPS));
//                        } else {
//                            tooltips.add(String.format("%s%d", shortcutIcon, lineStartIndex + i + 1));
//                        }
//                    }
//                }
//
//                return tooltips;
//            }
//        } else {
//            return tooltips;
//        }
        return tooltips;
    }

    private static TextAttributes getEdtorTextAttributes(@NotNull Editor editor) {
        if (editor == null) {
            //$$$reportNull$$$0(11);
        }

        EditorColorsScheme scheme = editor.getColorsScheme();
        TextAttributes themeAttributes = null;

        TextAttributesKey userColor;
        try {
            userColor = (TextAttributesKey) ReflectUtil.getStaticField(DefaultLanguageHighlighterColors.class, "INLAY_TEXT_WITHOUT_BACKGROUND");
            if (userColor != null) {
                themeAttributes = scheme.getAttributes(userColor);
            } else {
                themeAttributes = DEFAULT_INLAY_TEXT_THEME;
            }
        } catch (Exception var5) {
            themeAttributes = DEFAULT_INLAY_TEXT_THEME;
        }

        userColor = null;
        if (userColor == null && themeAttributes != null && themeAttributes.getForegroundColor() != null) {
            return themeAttributes;
        } else {
            TextAttributes customAttributes = themeAttributes != null ? themeAttributes.clone() : new TextAttributes();
            if (userColor != null) {
                //customAttributes.setForegroundColor(userColor);
            }

            if (customAttributes.getForegroundColor() == null) {
                customAttributes.setForegroundColor(JBColor.GRAY);
            }

            return customAttributes;
        }
    }

    public @NotNull List<String> getLines() {
        return this.lines;
    }

    public @NotNull String getContent() {
        return this.content;
    }

    public List<String> getTooltipTexts() {
        return this.tooltipTexts;
    }

    public int getCachedWidth() {
        return this.cachedWidth;
    }

    public int getCachedHeight() {
        return this.cachedHeight;
    }

    public int getLineMaxLength() {
        return this.lineMaxLength;
    }

    public int getTotalLineCount() {
        return this.totalLineCount;
    }

    public CodeEditorInlayItem getItem() {
        return this.item;
    }

    public long getLastUpdateDisplayTimeLength() {
        return this.lastUpdateDisplayTimeLength;
    }

    public void setCachedWidth(int cachedWidth) {
        this.cachedWidth = cachedWidth;
    }

    public void setCachedHeight(int cachedHeight) {
        this.cachedHeight = cachedHeight;
    }

    public void setLineMaxLength(int lineMaxLength) {
        this.lineMaxLength = lineMaxLength;
    }

    public void setTotalLineCount(int totalLineCount) {
        this.totalLineCount = totalLineCount;
    }

    public void setItem(CodeEditorInlayItem item) {
        this.item = item;
    }


    static {
        DEFAULT_INLAY_TEXT_THEME.setForegroundColor(new JBColor(new Color(8026746), new Color(8026746)));
    }
}
