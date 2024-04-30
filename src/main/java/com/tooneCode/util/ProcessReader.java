package com.tooneCode.util;

import com.intellij.openapi.diagnostic.Logger;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.io.SequenceInputStream;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import com.tooneCode.common.CodeConfig;
import org.apache.commons.io.FileUtils;

public class ProcessReader extends Thread {
    private static final Logger log = Logger.getInstance(ProcessReader.class);
    Process process;
    Boolean isRealtimePrint;
    StringBuilder buffer = new StringBuilder();
    Lock deleteLock = new ReentrantLock();

    public ProcessReader(Process process) {
        this.process = process;
        this.isRealtimePrint = false;
    }

    public ProcessReader(Process process, boolean isRealtimePrint) {
        this.process = process;
        this.isRealtimePrint = isRealtimePrint;
    }

    public void run() {
        try {
            SequenceInputStream stream = new SequenceInputStream(this.process.getInputStream(), this.process.getErrorStream());

            try {
                BufferedReader reader = new BufferedReader(new InputStreamReader(stream));

                try {
                    while (!this.isInterrupted()) {
                        String line = reader.readLine();
                        if (line == null) {
                            break;
                        }

                        if (this.isRealtimePrint) {
                            log.info(line);
                            if (line.contains("dgraph-io/badger")) {
                                this.clearLingmaStorage();
                            }
                        } else {
                            this.buffer.append(line).append('\n');
                        }
                    }
                } catch (Throwable var7) {
                    try {
                        reader.close();
                    } catch (Throwable var6) {
                        var7.addSuppressed(var6);
                    }

                    throw var7;
                }

                reader.close();
            } catch (Throwable var8) {
                try {
                    stream.close();
                } catch (Throwable var5) {
                    var8.addSuppressed(var5);
                }

                throw var8;
            }

            stream.close();
        } catch (Exception var9) {
            Exception e = var9;
            log.error(e.getMessage(), e);
        }

    }

    private void clearLingmaStorage() {
        if (this.deleteLock.tryLock()) {
            try {
                File homeDir = CodeConfig.getHomeDirectory().toFile();
                if (homeDir.exists()) {
                    File tmpDir = new File(homeDir, "tmp");
                    log.info("clear lingma storage temp dir:" + tmpDir);
                    if (tmpDir.exists()) {
                        boolean success = FileUtils.deleteQuietly(tmpDir);
                        log.info("clear lingma storage success:" + success);
                    }
                }
            } catch (Exception var7) {
                Exception e = var7;
                log.warn("fail to clear lingma storage:" + e.getMessage(), e);
            } finally {
                this.deleteLock.unlock();
            }

        }
    }

    public String getOutput() {
        return this.buffer.toString();
    }
}

