package com.tooneCode.util;

import com.intellij.openapi.diagnostic.Logger;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class LogUtil {
    private static final Logger LOG = Logger.getInstance(LogUtil.class);

    public static Logger getLogger() {
        return LOG;
    }

    public static void info(String msg) {
        LOG.info(msg);
    }

    public static void error(String msg) {
        LOG.error(msg);
    }

    public static void error(@NotNull Throwable t) {
        LOG.error(t);
    }

    public static void error(String message, @Nullable Throwable t) {
        LOG.error(message, t);
    }
}
