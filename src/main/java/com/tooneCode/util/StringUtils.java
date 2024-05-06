package com.tooneCode.util;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.util.text.StringUtil;
import org.jetbrains.annotations.NotNull;

public class StringUtils {
    private static Logger logger = Logger.getInstance(StringUtils.class);
    public static String FOUR_SPACE_TAB = "    ";
    public static String TAB = "\t";
    public static String TAB_SPACE_PATTERN = "[\t ]*";
    public static final String HTML_TAG_PATTERN = "\\<.*?>";
    public static final String WHITESPACE_PATTERN = "\\s+";
    private static final int INDENT_COUNT = 4;

    public StringUtils() {
    }

    public static String countLeadingLength(String str, String pattern) {
        Pattern p = Pattern.compile("^" + pattern);
        Matcher m = p.matcher(str);
        return m.find() ? m.group() : "";
    }

    public static String tabContent(String leadingContent) {
        return leadingContent.matches("(" + FOUR_SPACE_TAB + ")+") ? FOUR_SPACE_TAB : TAB;
    }

    public static List<String> replaceHeadTabs(@NotNull List<String> lines, boolean useTabs, int tabWidth) {

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

    public static Long getNumberFromString(String line) {
        try {
            return Long.parseLong(line);
        } catch (NumberFormatException var4) {
            logger.warn("Failed to parse number from string: " + line + " try using pattern.");

            try {
                return Long.parseLong(line.replaceAll("\\D+", ""));
            } catch (NumberFormatException var3) {
                logger.warn("Failed to parse number using regex: " + line);
                return null;
            }
        }
    }

    public static long getNotEmptyLineCount(String text) {
        return text == null ? 0L : Arrays.stream(text.split("\n")).filter((line) -> {
            return !line.trim().isEmpty();
        }).count();
    }

    public static boolean isJavaLineEnding(String lineContent) {
        return org.apache.commons.lang3.StringUtils.isBlank(lineContent) || lineContent.endsWith(";") || lineContent.endsWith("{") || lineContent.endsWith("*/") || lineContent.startsWith("//");
    }

    public static String extractClassNameFromFullPath(String fullPath) {
        if (fullPath == null) {
            return null;
        } else {
            return !fullPath.contains(".") ? fullPath : fullPath.substring(fullPath.lastIndexOf(".") + 1);
        }
    }

    public static boolean matchCopyContent(String source, String target) {
        if (source != null && target != null) {
            String sourceTrim = source.trim();
            String targetTrim = target.trim();
            if (sourceTrim.equals(targetTrim)) {
                return true;
            } else {
                String[] sourceLines = sourceTrim.split("\\r?\\n");
                String[] targetLines = targetTrim.split("\\r?\\n");
                if (sourceLines.length != targetLines.length) {
                    return false;
                } else {
                    for (int i = 0; i < sourceLines.length; ++i) {
                        String sourceLine = sourceLines[i];
                        String targetLine = targetLines[i];
                        if (!sourceLine.trim().equals(targetLine.trim())) {
                            return false;
                        }
                    }

                    return true;
                }
            }
        } else {
            return false;
        }
    }

    public static boolean isTabSpace(char c, boolean withNewline) {
        return c == ' ' || c == '\t' || withNewline && c == '\n';
    }

    public static boolean isTabsSpaces(String text, boolean withNewlines) {
        for (int i = 0; i < text.length(); ++i) {
            char c = text.charAt(i);
            if (!isTabSpace(c, withNewlines)) {
                return false;
            }
        }

        return true;
    }

}
