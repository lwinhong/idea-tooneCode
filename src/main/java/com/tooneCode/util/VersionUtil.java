package com.tooneCode.util;

public final class VersionUtil {
    private VersionUtil() {
    }

    public static int compareVersion(String version1, String version2) {
        int i = 0;

        int[] sub2 = new int[0];
        for (int j = 0; i < version1.length() || j < version2.length(); j = sub2[1]) {
            int[] sub1 = getVersionSub(version1, i);
            sub2 = getVersionSub(version2, j);
            if (sub1[0] != sub2[0]) {
                return sub1[0] > sub2[0] ? 1 : -1;
            }

            i = sub1[1];
        }

        return 0;
    }

    private static int[] getVersionSub(String version, int start) {
        if (start >= version.length()) {
            return new int[]{0, start};
        } else {
            int num = 0;

            int i;
            for (i = start; i < version.length() && version.charAt(i) != '.'; ++i) {
                num = num * 10 + version.charAt(i) - 48;
            }

            return new int[]{num, i + 1};
        }
    }
}
