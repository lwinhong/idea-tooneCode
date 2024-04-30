package com.tooneCode.util;

import com.intellij.ide.BrowserUtil;
import com.intellij.openapi.diagnostic.Logger;
import com.tooneCode.constants.LingmaUrls;

public class CodeBrowserUtil {
    private static final Logger LOG = Logger.getInstance(CodeBrowserUtil.class);

    public CodeBrowserUtil() {
    }

    public static boolean browse(LingmaUrls url) {
//        if (url != null) {
//            String realUrl = url.getRealUrl();
//            if (StringUtils.isBlank(realUrl) || "#".equals(realUrl.trim())) {
//                return false;
//            }
//
//            try {
//                BrowserUtil.browse(realUrl.trim());
//                return true;
//            } catch (Exception var3) {
//                Exception e = var3;
//                LOG.warn("browse url error", e);
//            }
//        }

        return false;
    }
}
