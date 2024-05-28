package com.tooneCode.editor;


import com.intellij.openapi.editor.DefaultLanguageHighlighterColors;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.Inlay;
import com.intellij.openapi.editor.colors.EditorColorsScheme;
import com.intellij.openapi.editor.colors.TextAttributesKey;
import com.intellij.openapi.editor.impl.EditorImpl;
import com.intellij.openapi.editor.markup.TextAttributes;
import com.intellij.ui.JBColor;
import com.intellij.util.ui.GraphicsUtil;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Shape;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

import com.tooneCode.common.CodeBundle;
import com.tooneCode.common.CodeSetting;
import com.tooneCode.editor.model.CodeEditorInlayItem;
import com.tooneCode.editor.model.InlayCompletionRequest;
import com.tooneCode.ui.config.CodePersistentSetting;
import com.tooneCode.util.FontUtil;
import com.tooneCode.util.KeyboardUtil;
import com.tooneCode.util.ReflectUtil;
import com.tooneCode.util.RenderUtil;
import lombok.Generated;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class CodeDefaultInlayRenderer implements CodeInlayRenderer {
    private static final String INLAY_TEXT_WITHOUT_BACKGROUND_FIELD = "INLAY_TEXT_WITHOUT_BACKGROUND";
    private static final TextAttributes DEFAULT_INLAY_TEXT_THEME = new TextAttributes();
    private static final int TOOLTIPS_OFFSET = 16;
    private static final String ACCEPT_SBY_LINE_TOOLTIPS = CodeBundle.message("completion.apply.partial.tooltips");
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

    public CodeDefaultInlayRenderer(@NotNull CodeEditorInlayItem item, @NotNull Editor editor, @NotNull InlayCompletionRequest request, @NotNull List<String> lines, int lineStartIndex, int totalLineCount, int lineMaxLength) {
        super();
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

    public int calcHeightInPixels(@NotNull Inlay inlay) {
        return this.cachedHeight < 0 ? (this.cachedHeight = inlay.getEditor().getLineHeight() * this.lines.size()) : this.cachedHeight;
    }

    public void paint(@NotNull Inlay inlay, @NotNull Graphics g, @NotNull Rectangle targetRegion, @NotNull TextAttributes textAttributes) {

        Editor editor = inlay.getEditor();
        if (!editor.isDisposed()) {
            Rectangle region = targetRegion;
            if (!this.content.trim().isEmpty() && !this.lines.isEmpty()) {
                Rectangle clipBounds = g.getClipBounds();
                Graphics2D g2 = (Graphics2D) g.create();
                GraphicsUtil.setupAAPainting(g2);
                String var10001 = this.content;
                Font font = FontUtil.getFont(editor, var10001 + StringUtils.join(new List[]{this.tooltipTexts}));
                g2.setFont(font);
                FontMetrics metrics = FontUtil.fontMetrics(editor, font);
                double lineHeight = (double) editor.getLineHeight();
                double fontBaseline = Math.ceil(font.createGlyphVector(metrics.getFontRenderContext(), "lAba").getVisualBounds().getHeight());
                double linePadding = (lineHeight - fontBaseline) / 2.0;
                double offsetX = region.getX();
                double offsetY = region.getY() + fontBaseline + linePadding;
                int lineOffset = 0;
                g2.setClip((Shape) (clipBounds != null && !clipBounds.equals(region) ? region.createIntersection(clipBounds) : region));

                for (int i = 0; i < this.lines.size(); ++i) {
                    String line = (String) this.lines.get(i);
                    RenderUtil.renderBackground(g2, this.textAttributes, offsetX, region.getY() + (double) lineOffset, region.getWidth(), lineHeight);
                    g2.setColor(this.textAttributes.getForegroundColor());
                    g2.drawString(line, (float) offsetX, (float) (offsetY + (double) lineOffset));
                    if (i < this.tooltipTexts.size()) {
                        String tooltipText = (String) this.tooltipTexts.get(i);
                        if (tooltipText != null) {
                            //绘制Ctrl+向下箭头，逐行补全提示。 还未完全实现，这个提示先屏蔽
                            //g2.drawString(tooltipText, (float) (this.lineMaxLength + 16), (float) (offsetY + (double) lineOffset));
                        }
                    }

                    if (editor instanceof EditorImpl) {
                        RenderUtil.renderEffects(g2, offsetX, offsetY + (double) lineOffset, (double) metrics.stringWidth(line), ((EditorImpl) editor).getCharHeight(), ((EditorImpl) editor).getDescent(), this.textAttributes, font);
                    }

                    lineOffset = (int) ((double) lineOffset + lineHeight);
                }

                g2.dispose();
                this.item.setRendered(true);
                if (this.item.getFirstDisplayTimeMs() == 0L) {
                    this.item.setFirstDisplayTimeMs(System.currentTimeMillis());
                }

                if (!this.item.isAccepted() && (this.lastUpdateDisplayTimeLength == 0L || this.lastUpdateDisplayTimeLength < (long) this.item.getContent().length())) {
                    this.lastUpdateDisplayTimeLength = (long) this.item.getContent().length();
                    this.item.setDisplayTimeMs(System.currentTimeMillis());
                }

            }
        }
    }

    public @Nullable @NonNls String getContextMenuGroupId(@NotNull Inlay inlay) {
        return "code.inlayContextMenu";
    }

    public int calcWidthInPixels(@NotNull Inlay inlay) {

        if (this.cachedWidth < 0) {
            if (this.totalLineCount <= 1) {
                this.cachedWidth = FontUtil.calculateWidth(inlay.getEditor(), this.content, this.lines);
            } else {
                String shortcutText = KeyboardUtil.getShortcutText("ApplyCodeInlayByLineCompletion", "");
                String tips = String.format("%s %s", shortcutText, ACCEPT_SBY_LINE_TOOLTIPS);
                String maxLine = null;
                Iterator var5 = this.tooltipTexts.iterator();

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

                        tip = (String) var5.next();
                    } while (maxLine != null && tip.length() <= maxLine.length());

                    maxLine = tip;
                }
            }
        }

        return this.cachedWidth;
    }

    private static TextAttributes getEdtorTextAttributes(@NotNull Editor editor) {
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
//            if (userColor != null) {
//                customAttributes.setForegroundColor(userColor);
//            }

            if (customAttributes.getForegroundColor() == null) {
                customAttributes.setForegroundColor(JBColor.GRAY);
            }

            return customAttributes;
        }
    }

    public @NotNull List<String> getContentLines() {
        return this.lines;
    }

    public @Nullable Inlay<CodeInlayRenderer> getInlay() {
        return this.inlay;
    }

    public void setInlay(@NotNull Inlay<CodeInlayRenderer> inlay) {
        this.inlay = inlay;
    }

    private List<String> createTooltipText(int lineStartIndex) {
        List<String> tooltips = new ArrayList<>();
        CodeSetting setting = CodePersistentSetting.getInstance().getState();
        if (setting != null && this.totalLineCount > 1) {
            String shortcutText = KeyboardUtil.getShortcutText("ApplyCodeInlayByLineCompletion", "");
            if (StringUtils.isBlank(shortcutText)) {
                return tooltips;
            } else {
                for (int i = 0; i < this.lines.size(); ++i) {
                    if (lineStartIndex + i == 0) {
                        tooltips.add(String.format("%s %s", shortcutText, ACCEPT_SBY_LINE_TOOLTIPS));
                        break;
                    }
                }

                return tooltips;
            }
        } else {
            return tooltips;
        }
    }

    private List<String> processLines(InlayCompletionRequest request, List<String> lines) {
        lines = com.tooneCode.util.StringUtils.replaceHeadTabs(lines, false, request.getTabWidth());
        lines = lines.stream().map((line) -> {
            return line.replace("<|cursor|>", "");
        }).collect(Collectors.toList());
        return lines;
    }

    @Generated
    public @NotNull List<String> getLines() {
        return this.lines;
    }

    @Generated
    public List<String> getTooltipTexts() {
        return this.tooltipTexts;
    }

    @Generated
    public @NotNull String getContent() {
        return this.content;
    }

    @Generated
    public @NotNull TextAttributes getTextAttributes() {
        return this.textAttributes;
    }

    @Generated
    public int getCachedWidth() {
        return this.cachedWidth;
    }

    @Generated
    public int getCachedHeight() {
        return this.cachedHeight;
    }

    @Generated
    public int getLineMaxLength() {
        return this.lineMaxLength;
    }

    @Generated
    public int getTotalLineCount() {
        return this.totalLineCount;
    }

    @Generated
    public CodeEditorInlayItem getItem() {
        return this.item;
    }

    @Generated
    public long getLastUpdateDisplayTimeLength() {
        return this.lastUpdateDisplayTimeLength;
    }

    @Generated
    public void setCachedWidth(int cachedWidth) {
        this.cachedWidth = cachedWidth;
    }

    @Generated
    public void setCachedHeight(int cachedHeight) {
        this.cachedHeight = cachedHeight;
    }

    @Generated
    public void setLineMaxLength(int lineMaxLength) {
        this.lineMaxLength = lineMaxLength;
    }

    @Generated
    public void setTotalLineCount(int totalLineCount) {
        this.totalLineCount = totalLineCount;
    }

    @Generated
    public void setItem(CodeEditorInlayItem item) {
        this.item = item;
    }

    @Generated
    public void setLastUpdateDisplayTimeLength(long lastUpdateDisplayTimeLength) {
        this.lastUpdateDisplayTimeLength = lastUpdateDisplayTimeLength;
    }

    @Generated
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        } else if (!(o instanceof CodeDefaultInlayRenderer)) {
            return false;
        } else {
            CodeDefaultInlayRenderer other = (CodeDefaultInlayRenderer) o;
            if (!other.canEqual(this)) {
                return false;
            } else {
                label103:
                {
                    Object this$lines = this.getLines();
                    Object other$lines = other.getLines();
                    if (this$lines == null) {
                        if (other$lines == null) {
                            break label103;
                        }
                    } else if (this$lines.equals(other$lines)) {
                        break label103;
                    }

                    return false;
                }

                Object this$tooltipTexts = this.getTooltipTexts();
                Object other$tooltipTexts = other.getTooltipTexts();
                if (this$tooltipTexts == null) {
                    if (other$tooltipTexts != null) {
                        return false;
                    }
                } else if (!this$tooltipTexts.equals(other$tooltipTexts)) {
                    return false;
                }

                label89:
                {
                    Object this$content = this.getContent();
                    Object other$content = other.getContent();
                    if (this$content == null) {
                        if (other$content == null) {
                            break label89;
                        }
                    } else if (this$content.equals(other$content)) {
                        break label89;
                    }

                    return false;
                }

                Object this$textAttributes = this.getTextAttributes();
                Object other$textAttributes = other.getTextAttributes();
                if (this$textAttributes == null) {
                    if (other$textAttributes != null) {
                        return false;
                    }
                } else if (!this$textAttributes.equals(other$textAttributes)) {
                    return false;
                }

                label75:
                {
                    Object this$inlay = this.getInlay();
                    Object other$inlay = other.getInlay();
                    if (this$inlay == null) {
                        if (other$inlay == null) {
                            break label75;
                        }
                    } else if (this$inlay.equals(other$inlay)) {
                        break label75;
                    }

                    return false;
                }

                if (this.getCachedWidth() != other.getCachedWidth()) {
                    return false;
                } else if (this.getCachedHeight() != other.getCachedHeight()) {
                    return false;
                } else if (this.getLineMaxLength() != other.getLineMaxLength()) {
                    return false;
                } else if (this.getTotalLineCount() != other.getTotalLineCount()) {
                    return false;
                } else {
                    label62:
                    {
                        Object this$item = this.getItem();
                        Object other$item = other.getItem();
                        if (this$item == null) {
                            if (other$item == null) {
                                break label62;
                            }
                        } else if (this$item.equals(other$item)) {
                            break label62;
                        }

                        return false;
                    }

                    if (this.getLastUpdateDisplayTimeLength() != other.getLastUpdateDisplayTimeLength()) {
                        return false;
                    } else {
                        return true;
                    }
                }
            }
        }
    }

    @Generated
    protected boolean canEqual(Object other) {
        return other instanceof CodeDefaultInlayRenderer;
    }

    @Generated
    public int hashCode() {
        int result = 1;
        Object $lines = this.getLines();
        result = result * 59 + ($lines == null ? 43 : $lines.hashCode());
        Object $tooltipTexts = this.getTooltipTexts();
        result = result * 59 + ($tooltipTexts == null ? 43 : $tooltipTexts.hashCode());
        Object $content = this.getContent();
        result = result * 59 + ($content == null ? 43 : $content.hashCode());
        Object $textAttributes = this.getTextAttributes();
        result = result * 59 + ($textAttributes == null ? 43 : $textAttributes.hashCode());
        Object $inlay = this.getInlay();
        result = result * 59 + ($inlay == null ? 43 : $inlay.hashCode());
        result = result * 59 + this.getCachedWidth();
        result = result * 59 + this.getCachedHeight();
        result = result * 59 + this.getLineMaxLength();
        result = result * 59 + this.getTotalLineCount();
        Object $item = this.getItem();
        result = result * 59 + ($item == null ? 43 : $item.hashCode());
        long $lastUpdateDisplayTimeLength = this.getLastUpdateDisplayTimeLength();
        result = result * 59 + (int) ($lastUpdateDisplayTimeLength >>> 32 ^ $lastUpdateDisplayTimeLength);
        return result;
    }

    @Generated
    public String toString() {
        List var10000 = this.getLines();
        return "CosyDefaultInlayRenderer(lines=" + var10000 + ", tooltipTexts=" + this.getTooltipTexts() + ", content=" + this.getContent() + ", textAttributes=" + this.getTextAttributes() + ", inlay=" + this.getInlay() + ", cachedWidth=" + this.getCachedWidth() + ", cachedHeight=" + this.getCachedHeight() + ", lineMaxLength=" + this.getLineMaxLength() + ", totalLineCount=" + this.getTotalLineCount() + ", item=" + this.getItem() + ", lastUpdateDisplayTimeLength=" + this.getLastUpdateDisplayTimeLength() + ")";
    }

    static {
        DEFAULT_INLAY_TEXT_THEME.setForegroundColor(new JBColor(new Color(8026746), new Color(8026746)));
    }

}
