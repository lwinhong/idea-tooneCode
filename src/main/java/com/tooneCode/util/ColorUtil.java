package com.tooneCode.util;

import com.intellij.openapi.editor.colors.EditorColors;
import com.intellij.openapi.editor.colors.EditorColorsManager;
import com.intellij.openapi.editor.colors.EditorColorsScheme;
import com.intellij.ui.Gray;
import com.intellij.ui.JBColor;
import com.intellij.util.ui.UIUtil;

import java.awt.Color;
import javax.swing.UIManager;

import org.jetbrains.annotations.NotNull;

public class ColorUtil {
    public ColorUtil() {
    }

    public static Color getEditorBackgroundColor() {
        return new JBColor(() -> {
            EditorColorsManager colorsManager = EditorColorsManager.getInstance();
            EditorColorsScheme colorsScheme = colorsManager.getGlobalScheme();
            return colorsScheme.getDefaultBackground();
        });
    }

    public static Color getChatCardBackgroundColor() {
        Color newNotificationColor = UIManager.getColor("NotificationsToolwindow.newNotification.background");
        return newNotificationColor != null ? JBColor.namedColor("NotificationsToolwindow.newNotification.background", newNotificationColor) : new JBColor(() -> {
            Color color = getToolWindowBackgroundColor();
            float[] hsl = new float[3];
            Color.RGBtoHSB(color.getRed(), color.getGreen(), color.getBlue(), hsl);
            Color schemaColor = EditorColorsManager.getInstance().getGlobalScheme().getColor(EditorColors.READONLY_FRAGMENT_BACKGROUND_COLOR);
            float newLight;
            if (schemaColor == null) {
                newLight = (float) ((int) (hsl[2] * 100.0F) - 3) / 100.0F;
                if (newLight < 0.0F) {
                    newLight = (float) ((int) (hsl[2] * 100.0F) + 3) / 100.0F;
                }

                hsl[2] = newLight;
            } else {
                newLight = (float) ((int) (hsl[2] * 100.0F) + 3) / 100.0F;
                if (newLight > 1.0F) {
                    newLight = (float) ((int) (hsl[2] * 100.0F) - 3) / 100.0F;
                }

                hsl[2] = newLight;
            }

            return Color.getHSBColor(hsl[0], hsl[1], hsl[2]);
        });
    }

    public static Color getTabbedUnderlineColor() {
        return JBColor.namedColor("TabbedPane.underlineColor", new JBColor(new Color(255, 0, 0), new Color(238, 0, 0)));
    }

    public static Color getTabbedHoverBgColor() {
        return JBColor.namedColor("TabbedPane.hoverColor", new JBColor(new Color(255, 0, 0), new Color(238, 0, 0)));
    }

    public static Color getTabbedBgColor() {
        return JBColor.namedColor("TabbedPane.background", new JBColor(new Color(255, 0, 0), new Color(238, 0, 0)));
    }

    public static Color getTabbedSelectForegroundColor() {
        return JBColor.namedColor("TabbedPane.selectedTabTitleNormalColor", new JBColor(new Color(255, 0, 0), new Color(238, 0, 0)));
    }

    public static Color getChatCardChildBackgroundColor() {
        return getChatCardBackgroundColor();
    }

    public static Color getSelectedListItemColor() {
        return UIUtil.getListSelectionForeground(true);
    }

    public static Color getToolWindowBackgroundColor() {
        return JBColor.namedColor("ToolWindow.background", UIUtil.getPanelBackground());
    }

    public static Color getButtonHoverBackgroundColor() {
        return JBColor.namedColor("ActionButton.hoverBackground", new JBColor(new Color(255, 0, 0), new Color(238, 0, 0)));
    }

    public static Color getDisabledTextFieldBackgroundColor() {
        return JBColor.namedColor("TextField.disabledBackground", new JBColor(Gray._242, Gray._242));
    }

    public static Color getEnabledTextFieldBackgroundColor() {
        return JBColor.namedColor("TextField.background", new JBColor(new Color(69, 73, 74), new Color(69, 73, 74)));
    }

    public static Color getTextForegroundColor() {
        return new JBColor(UIUtil::getTextAreaForeground);
    }

    public static Color getTextPaneForegroundColor() {
        return JBColor.namedColor("TextPane.foreground", getTextForegroundColor());
    }

    public static Color getLabelForegroundColor() {
        return JBColor.namedColor("Label.foreground", getTextPaneForegroundColor());
    }

    public static Color getAuxiliaryForegroundColor() {
        return JBColor.namedColor("Label.infoForeground", JBColor.namedColor("ToolTip.infoForeground", new JBColor(new Color(255, 0, 0), new Color(238, 0, 0))));
    }

    public static Color getInactiveForegroundColor() {
        return JBColor.namedColor("TextArea.inactiveForeground", new JBColor(new Color(255, 0, 0), new Color(238, 0, 0)));
    }

    public static Color getLinkForegroundColor() {
        return JBColor.namedColor("Hyperlink.linkColor", new JBColor(new Color(255, 0, 0), new Color(238, 0, 0)));
    }

    public static Color getItemSelectionBackground() {
        return new JBColor(() -> {
            return UIUtil.getListSelectionBackground(true);
        });
    }

    public static Color getListBackground() {
        return new JBColor(UIUtil::getListBackground);
    }

    public static Color getListForeground() {
        return new JBColor(UIUtil::getListForeground);
    }

    public static Color getPopupMenuSelectionBackground() {
        return JBColor.namedColor("PopupMenu.selectionBackground", getItemSelectionBackground());
    }

    public static @NotNull Color getListHoverBackground() {
        JBColor var10000 = JBColor.namedColor("List.hoverBackground", new JBColor(15595004, 4606541));

        return var10000;
    }
}
