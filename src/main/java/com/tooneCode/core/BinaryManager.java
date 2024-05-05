package com.tooneCode.core;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.util.Pair;
import com.intellij.openapi.util.SystemInfo;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.tooneCode.common.CodeConfig;
import com.tooneCode.util.ApplicationUtil;
import com.tooneCode.util.VersionUtil;
import com.tooneCode.util.ZipUtil;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public class BinaryManager {
    private static final Logger log = Logger.getInstance(BinaryManager.class);
    public static final BinaryManager INSTANCE = new BinaryManager();
    public static final List<String> EXCLUDE_DIRECTORIES = new ArrayList(Arrays.asList("aarch64_darwin", "x86_64_darwin", "x86_64_linux", "x86_64_windows", "aarch64_linux"));
    public static final Set<String> BINARY_DIRECTORIES = new HashSet(Arrays.asList("aarch64_darwin", "x86_64_darwin", "x86_64_linux", "x86_64_windows", "aarch64_linux"));

    @Contract(
            pure = true
    )
    private BinaryManager() {
    }

    public synchronized void checkBinary(boolean sameVersionForceUpdate) {
        File homeDir = CodeConfig.getHomeDirectory().toFile();
        if (!homeDir.exists() && !homeDir.mkdirs()) {
            log.error("fail to create directory " + homeDir);
        } else {
            this.checkBinary(homeDir, sameVersionForceUpdate);
        }
    }

    public void checkBinary(@NotNull File workDirectory, boolean sameVersionForceUpdate) {
        if (workDirectory == null) {
            //$$$reportNull$$$0(0);
        }

        log.info("checking cosy binary updating, sameVersionForceUpdate = " + sameVersionForceUpdate);
        File root = this.getBinaryRoot(workDirectory);
        if (root == null) {
            log.error("fail to init binary");
        } else {
            File[] files = root.listFiles(new FilenameFilter() {
                public boolean accept(File dir, String name) {
                    return !name.endsWith(".json");
                }
            });
            File configFile = new File(root, "config.json");
            if (files != null && files.length != 0 && configFile.exists()) {
                if (this.checkBinaryVersion(root, configFile, sameVersionForceUpdate)) {
                    this.initBinary(root);
                    ApplicationUtil.killCosyProcess();
                }
            } else {
                this.initBinary(root);
            }

        }
    }

    private void initBinary(File binDir) {
        try {
            log.info("start to init binary file.");
            InputStream stream = this.getClass().getResourceAsStream("/binaries/lingma.zip");
            if (stream != null) {
                File zipFile = new File(System.getProperty("java.io.tmpdir"), String.format("cosy_%d.zip", System.currentTimeMillis()));
                FileUtils.copyToFile(stream, zipFile);
                if (zipFile.exists()) {
                    log.info("Check cosy process before initBinary");
                    this.findProcessAndKill();
                    File finalDir = ZipUtil.unZip(zipFile, binDir.getAbsolutePath(), EXCLUDE_DIRECTORIES, BINARY_DIRECTORIES);
                    log.info("complete to init binary file." + finalDir);
                    zipFile.delete();
                    this.checkWin7(finalDir);
                } else {
                    log.error("fail to get cosy temp zipfile.");
                }
            } else {
                log.info("fail to get cosy binary resource.");
            }
        } catch (Throwable var5) {
            Throwable e = var5;
            log.error("fail to init binary for " + binDir, e);
        }

    }

    private void checkWin7(File finalDir) {
        log.info("Lingma system environment arch:" + SystemInfo.OS_ARCH + " os.name:" + SystemInfo.OS_NAME + " os.version:" + SystemInfo.OS_VERSION);
        if (SystemInfo.isWindows && !"aarch64".equals(SystemInfo.OS_ARCH) && !"arm64".equals(SystemInfo.OS_ARCH)) {
            if (SystemInfo.isWin10OrNewer) {
                log.info("Lingma system environment is win10 or newer");
            } else if (finalDir != null && finalDir.exists()) {
                File targetBinaryFile = new File(finalDir, "Lingma.exe");
                File backupBinaryFile = new File(finalDir, "LingmaBak.exe");
                File win7BinaryFile = new File(finalDir, "LingmaWin7.exe");
                if (targetBinaryFile.exists()) {
                    targetBinaryFile.renameTo(backupBinaryFile);
                }

                if (win7BinaryFile.exists()) {
                    try {
                        FileUtils.copyFile(win7BinaryFile, targetBinaryFile);
                    } catch (IOException var6) {
                        IOException e = var6;
                        log.error("fail to copy win7 binary file to target binary file", e);
                    }
                }

            } else {
                log.info("Lingma system environment binary directory:" + finalDir);
            }
        } else {
            log.info("Lingma system environment not windows");
        }
    }

    private void findProcessAndKill() {
        Pair<Integer, Long> infoPair = TooneCoder.INSTANCE.readCosyInfoFile(1);
        if (infoPair != null && infoPair.second != null) {
            TooneCoder.INSTANCE.killProcessAndDeleteInfoFile((Long) infoPair.second);
        } else {
            log.info("Pid not exist when trying to kill process, skip process killing");
        }

    }

    private boolean checkBinaryVersion(File binDir, File configFile, boolean sameVersionForceUpdate) {
        try {
            String configContent = ZipUtil.getEntryToString("/binaries/lingma.zip", "config.json");
            String zipVersion = this.getVersionString(configContent);
            if (StringUtils.isEmpty(zipVersion)) {
                log.warn("invalid cosy config zip version.");
                return false;
            } else {
                String fileContent = FileUtils.readFileToString(configFile, "utf-8");
                String localVersion = this.getVersionString(fileContent);
                if (StringUtils.isEmpty(localVersion)) {
                    log.warn("invalid cosy config local version.");
                    return true;
                } else {
                    return this.compareBinary(binDir, zipVersion, localVersion, sameVersionForceUpdate);
                }
            }
        } catch (IOException var8) {
            IOException e = var8;
            log.error("fail to get config file from resource zip", e);
            return false;
        }
    }

    private boolean compareBinary(File binDir, String zipVersion, String localVersion, boolean sameVersionForceUpdate) {
        int flag = VersionUtil.compareVersion(zipVersion, localVersion);
        if (flag < 0) {
            log.warn("local binary version is the latest.");
            return false;
        } else if (flag > 0) {
            log.warn("local binary version is too old, need to upgrade.");
            return true;
        } else if (sameVersionForceUpdate) {
            log.warn("local binary version is the same, sameVersionForceUpdate=true, upgrade it.");
            return true;
        } else {
            log.info("local binary version is equal to plugin internal version, ignore update");
            File localBinaryFile = new File(this.getBinaryVersionPath(binDir, localVersion));
            if (!localBinaryFile.exists()) {
                log.warn("local binary not exist, upgrade it.");
                return true;
            } else {
                return false;
            }
        }
    }

    public String getBinaryPath(@NotNull File workDirectory) throws IOException {
        if (workDirectory == null) {
            //$$$reportNull$$$0(1);
        }

        File root = this.getBinaryRoot(workDirectory);
        if (root == null) {
            log.error("cannot get binary root");
            return null;
        } else {
            File configFile = new File(root, "config.json");
            return configFile.exists() ? this.getBinaryPathFromConfig(root, configFile) : this.getDefaultBinaryPath(root, configFile);
        }
    }

    private File getBinaryRoot(@NotNull File workDirectory) {
        if (workDirectory == null) {
            //$$$reportNull$$$0(2);
        }

        File dir = new File(workDirectory, "bin");
        return !dir.exists() && !dir.mkdirs() ? null : dir;
    }

    private String getDefaultBinaryPath(File root, File configFile) throws IOException {
        File[] dirs = root.listFiles();
        long maxTime = 0L;
        File maxDir = null;
        File[] var7 = dirs;
        int var8 = dirs.length;

        for (int var9 = 0; var9 < var8; ++var9) {
            File dir = var7[var9];
            if (!dir.isFile() && maxTime < dir.lastModified()) {
                maxDir = dir;
            }
        }

        if (maxDir != null) {
            Map<String, String> config = new HashMap();
            config.put("cosy.core.version", maxDir.getName());
            FileUtils.write(configFile, JSON.toJSONString(config), "utf-8");
            this.checkBinaryPermissions(root, maxDir.getName());
            return this.getBinaryVersionPath(root, maxDir.getName());
        } else {
            return null;
        }
    }

    private String getBinaryPathFromConfig(File root, File configFile) throws IOException {
        String fileContent = FileUtils.readFileToString(configFile, "utf-8");
        if (StringUtils.isEmpty(fileContent)) {
            log.error("invalid cosy config file.");
            return null;
        } else {
            String version = this.getVersionString(fileContent);
            if (StringUtils.isEmpty(version)) {
                return null;
            } else {
                this.checkBinaryPermissions(root, version);
                return this.getBinaryVersionPath(root, version);
            }
        }
    }

    private String getBinaryVersionPath(File root, String version) {
        return Paths.get(root.getAbsolutePath(), version, String.format("%s_%s", CodeConfig.SYSTEM_ARCH, CodeConfig.PLATFORM_NAME), CodeConfig.COSY_EXECUTABLE_NAME).toString();
    }

    private void checkBinaryPermissions(File root, String version) {
        File versionDir = Paths.get(root.getAbsolutePath(), version, String.format("%s_%s", CodeConfig.SYSTEM_ARCH, CodeConfig.PLATFORM_NAME)).toFile();
        if (versionDir.exists()) {
            versionDir.setExecutable(true);
        }

        File[] files = versionDir.listFiles();
        if (files != null) {
            File[] var5 = files;
            int var6 = files.length;

            for (int var7 = 0; var7 < var6; ++var7) {
                File file = var5[var7];
                file.setExecutable(true);
            }
        }

    }

    public String getVersionString(String configContent) {
        if (StringUtils.isEmpty(configContent)) {
            log.warn("invalid config content:" + configContent);
            return null;
        } else {
            JSONObject jobj = JSON.parseObject(configContent);
            String version = jobj.getString("cosy.core.version");
            if (StringUtils.isEmpty(version)) {
                log.warn("invalid cosy config version from cotnent:" + configContent);
                return null;
            } else {
                return version;
            }
        }
    }

    static {
        String VALID_DIRECTORY = String.format("%s_%s", CodeConfig.SYSTEM_ARCH, CodeConfig.PLATFORM_NAME);
        EXCLUDE_DIRECTORIES.remove(VALID_DIRECTORY);
    }
}
