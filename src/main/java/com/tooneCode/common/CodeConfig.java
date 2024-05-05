package com.tooneCode.common;

import com.intellij.openapi.diagnostic.Logger;

import com.intellij.openapi.application.ApplicationInfo;
import com.intellij.openapi.util.SystemInfo;

import java.io.File;
import java.io.InputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;

import com.intellij.util.system.CpuArch;
import com.tooneCode.ui.config.CodePersistentSetting;
import com.tooneCode.util.ProcessUtils;
import org.apache.commons.lang3.StringUtils;

public class CodeConfig {
    private static final Logger log = Logger.getInstance(CodeConfig.class);
    public static final String PLUGIN_NAME;
    public static final String SYSTEM_ARCH;
    public static final String PLATFORM_NAME;
    public static final String COSY_EXECUTABLE_NAME;
    public static final String CONFIG_FILE_NAME = "config.json";
    public static final String ENV_FILE_NAME = "env.json";
    public static final String CONFIG_BINARY_VERSION = "cosy.core.version";
    public static final int MAX_COMPLETION_COUNT = 4;
    public static final String COSY_BINARY_RESOURCE = "/binaries/lingma.zip";
    public static final String IDE_VERSION;
    public static final String IDE_NAME;
    private static final Properties FEATURES_CONFIG;

    public CodeConfig() {
    }

    static {
        PLUGIN_NAME = "同望AI编码助手";//I18NConstant.COSY_PLUGIN_NAME;
        SYSTEM_ARCH = getSystemArch();
        PLATFORM_NAME = getPlatformName();
        COSY_EXECUTABLE_NAME = getExecutableName();
        IDE_VERSION = getIdeVersion();
        IDE_NAME = getIdeName();
        FEATURES_CONFIG = loadFeatureConfig();
    }

    private static String getSystemArch() {
        String arch = CpuArch.is32Bit() ? "i686" : "x86_64";
        if ("aarch64".equals(System.getProperty("os.arch"))) {
            arch = "aarch64";
        }

        log.info(String.format("SYSTEM ARCH: arch=%s, os.arch=%s, is32Bit=%s", arch, System.getProperty("os.arch"), CpuArch.is32Bit()));
        return arch;
    }

    private static String getPlatformName() {
        String platform = null;
        if (SystemInfo.isWindows) {
            platform = "windows";
        } else if (SystemInfo.isMac) {
            platform = "darwin";
        } else {
            if (!SystemInfo.isLinux) {
                throw new RuntimeException("Cosy only supports platform Windows, macOS, Linux");
            }

            platform = "linux";
        }

        return platform;
    }

    public static Path getHomeDirectory() {
        CodeSetting setting = CodePersistentSetting.getInstance().getState();
        if (setting != null && StringUtils.isNotBlank(setting.getLocalStoragePath())) {
            File file = new File(setting.getLocalStoragePath());
            boolean validPath = true;
            if (!file.exists()) {
                validPath = file.mkdirs();
            }

            if (validPath) {
                log.debug("use custom local storage path:" + setting.getLocalStoragePath());
                return Paths.get(setting.getLocalStoragePath());
            }

            log.warn("invalid local storage path:" + setting.getLocalStoragePath());
        }

        return Paths.get(getUserHome(), ".tooneCode");
    }

    private static String getUserHome() {
        String userHome = null;
        String osName = System.getProperty("os.name");
        if (StringUtils.isNotBlank(osName)) {
            osName = osName.toLowerCase();
            if (ProcessUtils.isWindowsPlatform()) {
                userHome = System.getenv("USERPROFILE");
            } else if (osName.contains("mac")) {
                userHome = System.getenv("HOME");
            } else if (osName.contains("nix") || osName.contains("nux")) {
                userHome = System.getenv("HOME");
            }
        }

        return StringUtils.isBlank(userHome) ? System.getProperty("user.home") : userHome;
    }

    private static String getExecutableName() {
        return SystemInfo.isWindows ? "Lingma.exe" : "Lingma";
    }

    private static String getIdeVersion() {
        return ApplicationInfo.getInstance().getFullVersion();
    }

    private static String getIdeName() {
        return ApplicationInfo.getInstance().getVersionName();
    }

    private static Properties loadFeatureConfig() {
        Properties properties = new Properties();

        try {
            InputStream stream = CodeConfig.class.getResourceAsStream("/features.properties");

            try {
                if (stream != null) {
                    properties.load(stream);
                }
            } catch (Throwable var5) {
                if (stream != null) {
                    try {
                        stream.close();
                    } catch (Throwable var4) {
                        var5.addSuppressed(var4);
                    }
                }

                throw var5;
            }

            if (stream != null) {
                stream.close();
            }
        } catch (Exception var6) {
            Exception e = var6;
            log.warn("load feature config failed", e);
        }

        return properties;
    }

    public static String getFeature(String key, String defaultValue) {
        return StringUtils.isBlank(key) ? defaultValue : FEATURES_CONFIG.getProperty(key, defaultValue);
    }

    public static boolean getFeature(String key, boolean defaultValue) {
        if (StringUtils.isBlank(key)) {
            return defaultValue;
        } else {
            String feature = FEATURES_CONFIG.getProperty(key, String.valueOf(defaultValue));
            if (StringUtils.isBlank(feature)) {
                return defaultValue;
            } else {
                try {
                    return Boolean.parseBoolean(feature);
                } catch (Exception var4) {
                    Exception e = var4;
                    log.warn("parse feature config failed", e);
                    return defaultValue;
                }
            }
        }
    }
}
