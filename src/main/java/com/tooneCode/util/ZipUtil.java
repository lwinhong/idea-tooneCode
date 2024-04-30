package com.tooneCode.util;

import com.intellij.openapi.diagnostic.Logger;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

public class ZipUtil {
    private static final Logger log = Logger.getInstance(ZipUtil.class);
    private static final int BUFFER_SIZE = 8192;

    public ZipUtil() {
    }

    public static String readZipFileToString(File srcZipFile, String targetFileName) {
        try {
            ZipFile zipFile = new ZipFile(srcZipFile);

            String var4;
            try {
                InputStream zipInputStream = zipFile.getInputStream(zipFile.getEntry(targetFileName));

                try {
                    var4 = IOUtils.toString(zipInputStream, StandardCharsets.UTF_8);
                } catch (Throwable var8) {
                    if (zipInputStream != null) {
                        try {
                            zipInputStream.close();
                        } catch (Throwable var7) {
                            var8.addSuppressed(var7);
                        }
                    }

                    throw var8;
                }

                if (zipInputStream != null) {
                    zipInputStream.close();
                }
            } catch (Throwable var9) {
                try {
                    zipFile.close();
                } catch (Throwable var6) {
                    var9.addSuppressed(var6);
                }

                throw var9;
            }

            zipFile.close();
            return var4;
        } catch (IOException var10) {
            IOException e = var10;
            log.error("fail to read " + targetFileName + " from " + srcZipFile + " Cause " + e.getMessage(), e);
            return null;
        }
    }

    public static File unZip(File srcFile, String destDirPath, List<String> excludeDirs, Set<String> binaryDirs) throws RuntimeException {
        if (!srcFile.exists()) {
            throw new RuntimeException(srcFile.getPath() + " not found");
        } else {
            File finalDir = null;
            ZipFile zipFile = null;

            try {
                zipFile = new ZipFile(srcFile);
                Enumeration<?> entries = zipFile.entries();

                while(entries.hasMoreElements()) {
                    ZipEntry entry = (ZipEntry)entries.nextElement();
                    if (!isExclude(excludeDirs, entry.getName())) {
                        File targetFile;
                        if (entry.isDirectory()) {
                            targetFile = new File(destDirPath, entry.getName());
                            targetFile.mkdirs();
                            if (binaryDirs.contains(targetFile.getName())) {
                                finalDir = targetFile;
                            }
                        } else {
                            targetFile = new File(destDirPath, entry.getName());
                            if (!targetFile.getParentFile().exists()) {
                                targetFile.getParentFile().mkdirs();
                            }

                            if (targetFile.exists()) {
                                targetFile.delete();
                            }

                            targetFile.createNewFile();
                            InputStream is = zipFile.getInputStream(entry);
                            FileUtils.copyToFile(is, targetFile);
                            targetFile.setExecutable(true);
                        }
                    }
                }
            } catch (Exception var13) {
                Exception e = var13;
                throw new RuntimeException("unzip error:" + srcFile, e);
            } finally {
                IOUtils.closeQuietly(zipFile);
            }

            return finalDir;
        }
    }

    private static boolean isExclude(List<String> excludeDirs, String entryName) {
        Iterator var2 = excludeDirs.iterator();

        String excludeDir;
        do {
            if (!var2.hasNext()) {
                return false;
            }

            excludeDir = (String)var2.next();
        } while(!entryName.contains(excludeDir));

        return true;
    }

    public static void toZip(List<File> srcFiles, OutputStream out) throws Exception {
        ZipOutputStream zos = null;
        FileInputStream in = null;

        try {
            zos = new ZipOutputStream(out);
            Iterator var13 = srcFiles.iterator();

            while(var13.hasNext()) {
                File srcFile = (File)var13.next();
                byte[] buf = new byte[8192];
                zos.putNextEntry(new ZipEntry(srcFile.getName()));
                in = new FileInputStream(srcFile);

                int len;
                while((len = in.read(buf)) != -1) {
                    zos.write(buf, 0, len);
                }

                zos.closeEntry();
                in.close();
            }
        } catch (Exception var11) {
            Exception e = var11;
            throw new RuntimeException("zip error:" + srcFiles, e);
        } finally {
            IOUtils.closeQuietly(zos);
            IOUtils.closeQuietly(in);
        }

    }

    public static String getEntryToString(String zipResource, String entryName) throws IOException {
        byte[] bytes = getEntryToBytes(zipResource, entryName);
        return bytes == null ? null : new String(bytes);
    }

    public static byte[] getEntryToBytes(String zipResource, String entryName) throws IOException {
        InputStream ins = ZipUtil.class.getResourceAsStream(zipResource);
        if (ins == null) {
            return null;
        } else {
            try {
                ZipInputStream zin = new ZipInputStream(ins);

                byte[] var8;
                label67: {
                    try {
                        ZipEntry entry = null;

                        while((entry = zin.getNextEntry()) != null) {
                            if (!entry.isDirectory() && entry.getName().equals(entryName)) {
                                byte[] buf = new byte[8192];
                                ByteArrayOutputStream baos = new ByteArrayOutputStream();

                                int len;
                                while((len = zin.read(buf)) != -1) {
                                    baos.write(buf, 0, len);
                                }

                                baos.flush();
                                var8 = baos.toByteArray();
                                break label67;
                            }
                        }
                    } catch (Throwable var10) {
                        try {
                            zin.close();
                        } catch (Throwable var9) {
                            var10.addSuppressed(var9);
                        }

                        throw var10;
                    }

                    zin.close();
                    return null;
                }

                zin.close();
                return var8;
            } catch (Exception var11) {
                Exception e = var11;
                log.error(e.getMessage(), e);
                return null;
            }
        }
    }

    public static List<String> getEntryFilePaths(String zipResource, List<String> excludeDirs) {
        List<String> paths = new ArrayList();
        InputStream ins = ZipUtil.class.getResourceAsStream(zipResource);
        if (ins == null) {
            return null;
        } else {
            try {
                ZipInputStream zin = new ZipInputStream(ins);

                ZipEntry entry;
                try {
                    while((entry = zin.getNextEntry()) != null) {
                        if (!entry.isDirectory() && !isExclude(excludeDirs, entry.getName())) {
                            paths.add(entry.getName());
                        }
                    }
                } catch (Throwable var8) {
                    try {
                        zin.close();
                    } catch (Throwable var7) {
                        var8.addSuppressed(var7);
                    }

                    throw var8;
                }

                zin.close();
            } catch (Exception var9) {
                Exception e = var9;
                log.error(e.getMessage(), e);
            }

            return paths;
        }
    }
}
