package com.tooneCode.util;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class ThreadUtil {
    private static final String WORKER_PREFIX = "worker";
    private static final AtomicInteger THREAD_COUNT = new AtomicInteger(1);
    private static final ThreadPoolExecutor EXECUTOR;

    public ThreadUtil() {
    }

    public static void execute(Runnable r) {
        EXECUTOR.execute(r);
    }

    public static void sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException var3) {
        }

    }

    static {
        EXECUTOR = new ThreadPoolExecutor(Runtime.getRuntime().availableProcessors(), Runtime.getRuntime().availableProcessors() * 8, 60L, TimeUnit.SECONDS, new ArrayBlockingQueue(128), new ThreadFactory() {
            public Thread newThread(Runnable r) {
                return new Thread(r, "worker" + ThreadUtil.THREAD_COUNT.getAndIncrement());
            }
        }, new ThreadPoolExecutor.CallerRunsPolicy());
    }
}
