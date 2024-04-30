package com.tooneCode.util;

import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.progress.ProgressIndicator;
import com.intellij.openapi.project.Project;
import com.sun.jna.Pointer;
import com.sun.jna.platform.win32.Kernel32;
import com.sun.jna.platform.win32.WinNT;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import com.tooneCode.core.TooneCoder;
import org.jetbrains.annotations.NotNull;

public class ProcessUtils {
    private static final Logger log = Logger.getInstance(ProcessUtils.class);
    public static final String WINDOWS_OS = "win";
    public static final String LINUX_OS = "nux";
    public static final String LINIX_OS = "nix";
    public static final String MAC_OS = "mac";
    public static final String COSY_PROCESS_NAME = "Lingma";
    public static final String COSY_PROCESS_DESCRIPTION = "Lingma";
    public static final int TASK_KILL_TIMEOUT = 5;

    public ProcessUtils() {
    }

    public static boolean isProcessAlive(long pid) {
        String osName = System.getProperty("os.name").toLowerCase();
        String command;
        if (isWindowsPlatform()) {
            log.info(String.format("Check alive Windows mode. Pid: [%d]", pid));
            command = "C:\\Windows\\System32\\cmd.exe /c C:\\Windows\\System32\\tasklist.exe /FI \"PID eq " + pid + "\"";
        } else {
            if (!osName.contains("nix") && !osName.contains("nux") && !osName.contains("mac")) {
                log.info(String.format("Unsupported OS: Check alive for Pid: [%d] return false", pid));
                return false;
            }

            log.info(String.format("Check alive Linux/Unix mode. Pid: [%d]", pid));
            command = "ps -p " + pid;
        }

        return isProcessIdRunning(pid, command);
    }

    public static boolean isWindowsPlatform() {
        String osName = System.getProperty("os.name").toLowerCase();
        return osName.indexOf("win") >= 0;
    }

    private static boolean isProcessIdRunning(long pid, String command) {
        log.info(String.format("Command [%s]", command));

        try {
            Runtime rt = Runtime.getRuntime();
            Process pr = rt.exec(command);
            InputStreamReader isReader = new InputStreamReader(pr.getInputStream());
            BufferedReader bReader = new BufferedReader(isReader);

            String strLine;
            do {
                if ((strLine = bReader.readLine()) == null) {
                    return false;
                }
            } while(!strLine.contains("" + pid + " "));

            return true;
        } catch (Exception var8) {
            Exception ex = var8;
            log.warn(String.format("Got exception using system command [%s].", command), ex);
            return true;
        }
    }

    public static void killProcess(long pid) {
        log.info("Kill cosy process: " + pid);
        Runtime rt = Runtime.getRuntime();
        String osName = System.getProperty("os.name").toLowerCase();

        try {
            Process process = null;
            if (isWindowsPlatform()) {
                log.info(String.format("Kill process in Windows mode. Pid: [%d]", pid));
                process = rt.exec("C:\\Windows\\System32\\taskkill.exe /F /T /PID " + pid);
            } else if (!osName.contains("nix") && !osName.contains("nux") && !osName.contains("mac")) {
                log.info(String.format("Unsupported OS: Check alive for Pid: [%d] return false", pid));
            } else {
                log.info(String.format("Kill process in Linux/Unix mode. Pid: [%d]", pid));
                process = rt.exec("kill " + pid);
            }

            if (process != null) {
                ProcessReader reader = new ProcessReader(process);
                reader.start();
                log.info("Wait for process killing");
                process.waitFor(5L, TimeUnit.SECONDS);
                log.info("Process killing output:" + reader.getOutput());
                reader.interrupt();
            }
        } catch (Exception var6) {
            log.warn("Kill process encountered exception");
        }

    }

    public static List<Long> findCosyPidList() {
        return getPidListFromName("Lingma");
    }

    public static List<Long> getPidListFromName(String name) {
        String osName = System.getProperty("os.name").toLowerCase();
        String[] command;
        List pids;
        if (isWindowsPlatform()) {
            log.info(String.format("Get pid list Windows mode. Name: [%s]", name));
            command = new String[]{"cmd", "/c", "tasklist /FI \"IMAGENAME eq " + name + ".exe\""};
            pids = getPidListWindows(command);
            if (pids == null || pids.isEmpty()) {
                command = new String[]{"c:\\windows\\system32\\cmd.exe", "/c", "c:\\windows\\system32\\tasklist.exe /FI \"IMAGENAME eq " + name + ".exe\""};
                pids = getPidListWindows(command);
                if (pids == null || pids.isEmpty()) {
                    command = new String[]{"cmd", "/c", "wmic process where \"name='" + name + "'\" get processid"};
                    return getPidListByWmic(command);
                }
            }

            return pids;
        } else if (!osName.contains("nix") && !osName.contains("nux")) {
            if (!osName.contains("mac")) {
                log.info(String.format("Unsupported OS: Get pid list for Name: [%s] return empty list", name));
                return null;
            } else {
                log.info(String.format("Get pid list MacOS mode. Name: [%s]", name));
                command = new String[]{"/bin/sh", "-c", "ps -eo pid,command | grep " + name};
                pids = getPidList(command);
                if (pids == null || pids.isEmpty()) {
                    command = new String[]{"pgrep", name};
                    pids = getPidListByGrep(command);
                }

                return pids;
            }
        } else {
            log.info(String.format("Get pid list Linux/Unix mode. Name: [%s]", name));
            command = new String[]{"/bin/sh", "-c", "ps -eo pid,command | grep " + name};
            return getPidList(command);
        }
    }

    private static List<Long> getPidList(String[] command) {
        log.info(String.format("getPidList Command [%s]", String.join(",", command)));

        try {
            Runtime rt = Runtime.getRuntime();
            Process pr = rt.exec(command);
            InputStreamReader isReader = new InputStreamReader(pr.getInputStream());
            BufferedReader bReader = new BufferedReader(isReader);
            List<Long> pidList = new ArrayList();

            String strLine;
            while((strLine = bReader.readLine()) != null) {
                if (strLine.contains("Lingma") && strLine.contains("start")) {
                    String[] outputs = strLine.trim().split("\\s+");
                    if (outputs.length > 0) {
                        try {
                            pidList.add(Long.parseLong(outputs[0]));
                        } catch (Exception var9) {
                            Exception e = var9;
                            log.warn(String.format("Parse [%s] and add pid list encountered exception: %s", strLine, e.getMessage()));
                        }
                    }
                }
            }

            return pidList;
        } catch (Exception var10) {
            Exception ex = var10;
            log.warn(String.format("Got exception using system command [%s].", String.join(",", command)), ex);
            return null;
        }
    }

    private static List<Long> getPidListWindows(String[] command) {
        log.info(String.format("getPidListWindows Command [%s]", String.join(",", command)));

        try {
            Runtime rt = Runtime.getRuntime();
            Process pr = rt.exec(command);
            InputStreamReader isReader = new InputStreamReader(pr.getInputStream());
            BufferedReader bReader = new BufferedReader(isReader);
            List<Long> pidList = new ArrayList();

            String strLine;
            while((strLine = bReader.readLine()) != null) {
                log.info("windows get pid output:" + strLine);
                if (strLine.contains("Lingma")) {
                    String[] outputs = strLine.trim().split("[\\s\t]+");
                    if (outputs.length > 0) {
                        try {
                            pidList.add(Long.parseLong(outputs[1]));
                        } catch (Exception var9) {
                            Exception e = var9;
                            log.warn(String.format("Parse [%s] and add pid list encountered exception: %s", strLine, e.getMessage()));
                        }
                    }
                }
            }

            return pidList;
        } catch (Exception var10) {
            Exception ex = var10;
            log.warn(String.format("Got exception using system command [%s].", String.join(",", command)), ex);
            return null;
        }
    }

    private static List<Long> getPidListByGrep(String[] command) {
        log.info(String.format("getPidListByGrep Command [%s]", String.join(",", command)));
        List<Long> pids = new ArrayList();

        try {
            ProcessBuilder pb = new ProcessBuilder(command);
            pb.redirectErrorStream(true);
            Process process = pb.start();
            BufferedReader in = new BufferedReader(new InputStreamReader(process.getInputStream()));

            String line;
            while((line = in.readLine()) != null) {
                try {
                    pids.add(Long.parseLong(line.trim()));
                } catch (Exception var7) {
                    Exception e = var7;
                    log.warn(String.format("Parse [%s] and add pid list encountered exception: %s", line, e.getMessage()));
                }
            }

            in.close();
            process.waitFor();
        } catch (Exception var8) {
            Exception ex = var8;
            log.warn(String.format("Got exception using system command [%s].", String.join(",", command)), ex);
        }

        return pids;
    }

    private static List<Long> getPidListByWmic(String[] command) {
        log.info(String.format("getPidListByWmic Command [%s]", String.join(",", command)));
        List<Long> pids = new ArrayList();

        try {
            ProcessBuilder pb = new ProcessBuilder(command);
            pb.redirectErrorStream(true);
            Process process = pb.start();
            BufferedReader in = new BufferedReader(new InputStreamReader(process.getInputStream()));
            List<String> lines = new ArrayList();

            String line;
            while((line = in.readLine()) != null) {
                lines.add(line);
            }

            in.close();
            process.waitFor();
            if (lines.size() > 1) {
                try {
                    pids.add(Long.parseLong(((String)lines.get(1)).trim()));
                } catch (Exception var8) {
                    Exception e = var8;
                    log.warn(String.format("Parse [%s] and add pid list encountered exception: %s", lines, e.getMessage()));
                }
            }
        } catch (Exception var9) {
            Exception ex = var9;
            log.warn(String.format("Got exception using system command [%s].", String.join(",", command)), ex);
        }

        return pids;
    }

    public static Long getPid(Process process) {
        Long pid;
        if (System.getProperty("os.name").toLowerCase().contains("windows")) {
            pid = windowsProcessId(process);
        } else {
            pid = unixLikeProcessId(process);
        }

        return pid;
    }

    private static Long unixLikeProcessId(Process process) {
        Class<?> clazz = process.getClass();

        try {
            if ("java.lang.UNIXProcess".equals(clazz.getName()) || "java.lang.ProcessImpl".equals(clazz.getName())) {
                Field pidField = clazz.getDeclaredField("pid");
                pidField.setAccessible(true);
                Object value = pidField.get(process);
                if (value instanceof Integer) {
                    log.debug("Detected pid: {}", new Object[]{value});
                    return ((Integer)value).longValue();
                }
            }
        } catch (IllegalAccessException | IllegalArgumentException | NoSuchFieldException | SecurityException var4) {
        }

        return null;
    }

    private static Long windowsProcessId(Process process) {
        if (process.getClass().getName().equals("java.lang.Win32Process") || process.getClass().getName().equals("java.lang.ProcessImpl")) {
            try {
                Field f = process.getClass().getDeclaredField("handle");
                f.setAccessible(true);
                long handl = f.getLong(process);
                Kernel32 kernel = Kernel32.INSTANCE;
                WinNT.HANDLE handle = new WinNT.HANDLE();
                handle.setPointer(Pointer.createConstant(handl));
                int ret = kernel.GetProcessId(handle);
                log.debug("Detected windows pid: {}", new Object[]{ret});
                return (long)ret;
            } catch (Throwable var7) {
                Throwable e = var7;
                e.printStackTrace();
            }
        }

        return null;
    }

    public static boolean checkAndWaitCosyState(@NotNull ProgressIndicator progressIndicator, @NotNull Project project) {

        boolean succeed = false;
        if (!TooneCoder.INSTANCE.checkCosy(project, true)) {
            succeed = TooneCoder.INSTANCE.checkAndWaitCosyState(progressIndicator, project);
        } else {
            log.info("Cosy state is OK, login/logout directly.");
            succeed = true;
        }

        return succeed;
    }
}
