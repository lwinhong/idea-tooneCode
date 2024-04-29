package com.tooneCode.util;

import java.util.List;
import java.util.stream.Collectors;

import com.intellij.openapi.util.text.StringUtil;
import org.jetbrains.annotations.NotNull;

public class StringUtils {
    //private static Logger logger = Logger.getInstance(StringUtils.class);

    public StringUtils() {
    }

    public static List<String> replaceHeadTabs(@NotNull List<String> lines, boolean useTabs, int tabWidth) {
        if (lines == null) {
            //$$$reportNull$$$0(5);
        }

        return lines.stream().map((line) -> {
            return replaceHeadTabs(line, useTabs, tabWidth);
        }).collect(Collectors.toList());
    }

    public static String replaceHeadTabs(String line, boolean useTabs, int tabWidth) {
        int tabCount = StringUtil.countChars(line, '\t', 0, true);
        if (tabCount > 0 && !useTabs) {
            String tabSpaces = StringUtil.repeatSymbol(' ', tabCount * tabWidth);
            return tabSpaces + line.substring(tabCount);
        } else {
            return line;
        }
    }

    public static int countHeadWhitespaceLength(@NotNull String text) {
        if (text == null) {
            //$$$reportNull$$$0(4);
        }

        int length = text.length();

        int offset;
        for (offset = 0; offset < length; ++offset) {
            char ch = text.charAt(offset);
            if (ch == '\n' || !Character.isWhitespace(ch)) {
                break;
            }
        }

        return offset;
    }

}
