package com.tooneCode.util;

import com.intellij.ide.ui.AntialiasingType;
import com.intellij.openapi.application.ApplicationInfo;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.colors.EditorColorsScheme;
import com.intellij.openapi.editor.colors.EditorFontType;
import com.intellij.openapi.editor.impl.FontInfo;
import com.intellij.openapi.util.SystemInfo;
import com.intellij.util.ui.UIUtil;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.font.FontRenderContext;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.swing.JComponent;
import javax.swing.JTextArea;
import javax.swing.plaf.FontUIResource;
import javax.swing.text.StyleContext;

import com.tooneCode.common.CodeCacheKeys;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class FontUtil {
    private static final @Nullable Method getEditorFontSize2DMethod;
    private static Font editFont;

    public FontUtil() {
    }

    public static FontMetrics fontMetrics(@NotNull Editor editor, @NotNull Font font) {
        if (editor == null) {
            //$$$reportNull$$$0(0);
        }

        if (font == null) {
            //$$$reportNull$$$0(1);
        }

        FontRenderContext editorContext = FontInfo.getFontRenderContext(editor.getContentComponent());
        FontRenderContext context = new FontRenderContext(editorContext.getTransform(), AntialiasingType.getKeyForCurrentScope(false), editorContext.getFractionalMetricsHint());
        Map<Font, FontMetrics> cachedMap = (Map) CodeCacheKeys.KEY_CACHED_FONTMETRICS.get(editor, Collections.emptyMap());
        FontMetrics fontMetrics = (FontMetrics)cachedMap.get(font);
        if (fontMetrics == null || !context.equals(fontMetrics.getFontRenderContext())) {
            fontMetrics = FontInfo.getFontMetrics(font, context);
            CodeCacheKeys.KEY_CACHED_FONTMETRICS.set(editor, CollectionUtil.mergeMaps(new Map[]{cachedMap, Map.of(font, fontMetrics)}));
        }

        return fontMetrics;
    }

    public static FontMetrics fontMetrics(@NotNull JComponent component, @NotNull Font font) {
        if (component == null) {
            //$$$reportNull$$$0(2);
        }

        if (font == null) {
            //$$$reportNull$$$0(3);
        }

        FontRenderContext editorContext = FontInfo.getFontRenderContext(component);
        FontRenderContext context = new FontRenderContext(editorContext.getTransform(), AntialiasingType.getKeyForCurrentScope(false), editorContext.getFractionalMetricsHint());
        return FontInfo.getFontMetrics(font, context);
    }

    public static @NotNull Font getFont(@NotNull Editor editor, @NotNull String text) {
        if (editor == null) {
            //$$$reportNull$$$0(4);
        }

        if (text == null) {
            //$$$reportNull$$$0(5);
        }

        Font font = editor.getColorsScheme().getFont(EditorFontType.PLAIN).deriveFont(2);
        Font fallbackFont = UIUtil.getFontWithFallbackIfNeeded(font, text);
        Font var10000 = fallbackFont.deriveFont(fontSize(editor));
        if (var10000 == null) {
            //$$$reportNull$$$0(6);
        }

        return var10000;
    }

    public static float fontSize(@NotNull Editor editor) {
        if (editor == null) {
            //$$$reportNull$$$0(7);
        }

        EditorColorsScheme scheme = editor.getColorsScheme();
        if (getEditorFontSize2DMethod != null) {
            try {
                return (Float)getEditorFontSize2DMethod.invoke(scheme);
            } catch (InvocationTargetException | IllegalAccessException var3) {
            }
        }

        return (float)scheme.getEditorFontSize();
    }

    public static int calculateWidth(@NotNull Editor editor, @NotNull String text, @NotNull List<String> textLines) {
        if (editor == null) {
            //$$$reportNull$$$0(8);
        }

        if (text == null) {
            //$$$reportNull$$$0(9);
        }

        if (textLines == null) {
            //$$$reportNull$$$0(10);
        }

        FontMetrics metrics = fontMetrics(editor, getFont(editor, text));
        int maxWidth = 0;

        String line;
        for(Iterator var5 = textLines.iterator(); var5.hasNext(); maxWidth = Math.max(maxWidth, metrics.stringWidth(line))) {
            line = (String)var5.next();
        }

        return maxWidth;
    }

    public static @NotNull Font getFontWithFallbackIfNeeded(@NotNull Font font, @NotNull String text) {
        if (font == null) {
            //$$$reportNull$$$0(11);
        }

        if (text == null) {
            //$$$reportNull$$$0(12);
        }

        if (!SystemInfo.isMac && font.canDisplayUpTo(text) != -1) {
            return getFontWithFallback(font);
        } else {
            if (font == null) {
                //$$$reportNull$$$0(13);
            }

            return font;
        }
    }

    public static @NotNull FontUIResource getFontWithFallback(@NotNull Font font) {
        if (font == null) {
            //$$$reportNull$$$0(14);
        }

        return getFontWithFallback(font.getFamily(), font.getStyle(), font.getSize());
    }

    public static @NotNull FontUIResource getFontWithFallback(String familyName, int style, int size) {
        Font fontWithFallback = SystemInfo.isMac ? new Font(familyName, style, size) : (editFont != null ? editFont : (new StyleContext()).getFont(familyName, style, size));
        return fontWithFallback instanceof FontUIResource ? (FontUIResource)fontWithFallback : new FontUIResource(fontWithFallback);
    }

    static {
        Method method = null;
        if (ApplicationInfo.getInstance().getBuild().getBaselineVersion() >= 221) {
            try {
                method = EditorColorsScheme.class.getMethod("getEditorFontSize2D");
            } catch (NoSuchMethodException var2) {
            }
        }

        getEditorFontSize2DMethod = method;
        JTextArea area = new JTextArea();
        editFont = area.getFont();
    }
}

