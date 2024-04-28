package com.tooneCode.util;

import com.alibaba.fastjson.JSON;
import com.intellij.lang.Language;
import com.intellij.lang.LanguageUtil;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.fileEditor.FileEditor;
import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.fileTypes.FileType;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.testFramework.LightVirtualFile;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.Nullable;

public class FileUtil {
    private static final Logger log = Logger.getInstance(FileUtil.class);
    public static final long MAX_FILE_SIZE = 1048576L;
    public static final List<String> VALID_FILE_LANGUAGES = Arrays.asList("java", "python");
    public static final Pattern JAVA_CLASS_PATTERN = Pattern.compile(".*(class|interface|enum)\\s+([\\w$_]+)\\s+.*");

    public FileUtil() {
    }

    public static boolean isLargeFile(@Nullable VirtualFile file) {
        return file == null || file.getLength() > 1048576L;
    }

    public static boolean checkAllFilesExist(List<String> filePaths, String root) {
        try {
            Iterator var5 = filePaths.iterator();

            while (var5.hasNext()) {
                String filePath = (String) var5.next();
                if (!Paths.get(root, filePath).toFile().exists()) {
                    log.info(String.format("File [%s] exists in zip but not in local file system", filePath));
                    return Boolean.FALSE;
                }
            }
        } catch (Exception var4) {
            Exception e = var4;
            log.warn("Check file exist failed" + e.getMessage());
            return Boolean.FALSE;
        }

        return Boolean.TRUE;
    }

    public static void deleteRecursive(File path) {
        File[] files = path.listFiles();
        log.info("Cleaning out folder:" + path);
        if (files != null) {
            File[] var2 = files;
            int var3 = files.length;

            for (int var4 = 0; var4 < var3; ++var4) {
                File file = var2[var4];
                if (file.isDirectory()) {
                    log.info("Deleting file:" + file);
                    deleteRecursive(file);
                    file.delete();
                } else {
                    file.delete();
                }
            }
        }

        path.delete();
    }

    public static final String getPathExt(String filePath) {
        String ext = null;
        int index = filePath.lastIndexOf(46);
        if (index > 0) {
            ext = filePath.substring(index + 1);
        }

        return ext;
    }

    public static String getLanguageFileType(Project project) {
        FileEditorManager fileEditorManager = FileEditorManager.getInstance(project);
        FileEditor selectEditor = fileEditorManager.getSelectedEditor();
        if (selectEditor == null) {
            return "";
        } else {
            VirtualFile file = selectEditor.getFile();
            if (file == null) {
                return "";
            } else {
                Language language = LanguageUtil.getFileLanguage(file);
                if (language == null) {
                    return "";
                } else {
                    log.info("Current code language: " + language.getDisplayName());
                    return language.getDisplayName();
                }
            }
        }
    }

    public static boolean createPrivacyPolicyFile(File targetFile) {
        Map<String, Object> map = new HashMap();
        map.put("timestamp", System.currentTimeMillis());

        try {
            FileUtils.write(targetFile, JSON.toJSONString(map), "UTF-8");
            return true;
        } catch (IOException var3) {
            IOException e = var3;
            log.warn("fail to create privacy policy file:" + targetFile + " cause by " + e.getMessage(), e);
            return false;
        }
    }

    public static String getJavaFileNameFromContent(String content) {
        Matcher matcher = JAVA_CLASS_PATTERN.matcher(content);
        return matcher.find() ? matcher.group(2) : null;
    }

    public static String generateFileNameWhenNewFile(String ext, String content, String fullPath, String fileName, String separator, boolean isTestcase) {
        String newFileName;
        if (StringUtils.isBlank(ext)) {
            newFileName = getJavaFileNameFromContent(content);
            if (newFileName != null) {
                fileName = newFileName;
            }
        } else if ("java".equals(ext)) {
            newFileName = getJavaFileNameFromContent(content);
            String originalFileName = fullPath.substring(fullPath.lastIndexOf(separator) + 1);
            if (newFileName != null) {
                fileName = newFileName;
            } else {
                fileName = originalFileName.substring(0, originalFileName.lastIndexOf("."));
                if (isTestcase) {
                    fileName = fileName + "Test";
                } else {
                    fileName = fileName + "_" + System.currentTimeMillis();
                }
            }
        }

        return fileName;
    }

    public static String generatePathWhenNewFile(String ext, String path, boolean isTestcase) {
        if ("java".equals(ext) && isTestcase) {
            String separator = getFileSeparator(path);
            return path.replace(String.format("src%smain%sjava", separator, separator), String.format("src%stest%sjava", separator, separator));
        } else {
            return path;
        }
    }

    public static VirtualFile buildVirtualFile(String fileName, FileType fileType, String content, final boolean writable) {
        return new LightVirtualFile(fileName, fileType, content, StandardCharsets.UTF_8, 0L) {
            public boolean isWritable() {
                return writable;
            }
        };
    }

    public static String getFileSeparator(String pathExample) {
        return "\\".equals(File.separator) && !pathExample.contains(File.separator) && pathExample.contains("/") ? "/" : File.separator;
    }
}

