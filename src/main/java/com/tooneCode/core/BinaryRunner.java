package com.tooneCode.core;

import com.intellij.openapi.diagnostic.Logger;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.tooneCode.util.ProcessReader;
import com.tooneCode.util.ProcessUtils;
import org.jetbrains.annotations.NotNull;

public class BinaryRunner {
    private static final Logger log = Logger.getInstance(BinaryRunner.class);
    private final File workDirectory;
    private Process process;
    private final String WHITE_SPACE = " ";

    public BinaryRunner(File workDirectory) {
        this.workDirectory = workDirectory;
    }

    public boolean run(boolean debugMode) throws IOException {
        String binaryPath = BinaryManager.INSTANCE.getBinaryPath(this.workDirectory);
        if (binaryPath == null) {
            return false;
        } else {
            List<String> commands = this.createCommand(binaryPath, debugMode);
            log.info("commands:" + commands);
            ProcessBuilder builder = new ProcessBuilder(commands);
            builder.directory(this.workDirectory);
            this.process = builder.start();

            try {
                if (this.process != null) {
                    ProcessReader reader = new ProcessReader(this.process, true);
                    reader.start();
                }
            } catch (Exception var6) {
                log.warn("Kill process encountered exception");
            }

            return this.process.isAlive();
        }
    }

    public void close() {
        Runtime rt = Runtime.getRuntime();

        try {
            Long pid = ProcessUtils.getPid(this.process);
            if (pid != null) {
                TooneCoder.INSTANCE.killProcessAndDeleteInfoFile(pid);
            } else {
                log.info("Failed to get pid");
            }
        } catch (Exception var3) {
            Exception e = var3;
            e.printStackTrace();
        }

        if (this.process != null && this.process.isAlive()) {
            this.process.destroyForcibly();
        }

    }

    private List<String> createCommand(@NotNull String binaryPath, boolean debugMode) {
        if (binaryPath == null) {
            //$$$reportNull$$$0(0);
        }

        List<String> commands = new ArrayList();
        if (ProcessUtils.isWindowsPlatform()) {
            log.info("Windows platform add 'cmd /c' to detach");
            commands.add("cmd");
            commands.add("/c");
        }

        if (ProcessUtils.isWindowsPlatform()) {
            binaryPath = binaryPath.replace("(", "^(").replace(")", "^)").replace("&", "^&").replace(">", "^>").replace("<", "^<").replace("|", "^|");
            binaryPath = "\"" + binaryPath + "\"";
        }

        commands.add(binaryPath);
        commands.add("start");
        return commands;
    }

    public Process getProcess() {
        return this.process;
    }
}
