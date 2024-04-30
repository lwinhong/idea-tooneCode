package com.tooneCode.util;

import com.intellij.openapi.diagnostic.Logger;

import java.util.concurrent.Callable;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Debouncer {
    private static final Logger log = Logger.getInstance(Debouncer.class);
    private static final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
    private Future prev;
    private String id;
    private Lock lock = new ReentrantLock();

    public Debouncer() {
    }

    public void debounce(Runnable runnable, long delay, TimeUnit unit) {
        this.lock.lock();

        try {
            if (this.prev != null && !this.prev.isDone()) {
                this.prev.cancel(false);
            }

            this.prev = scheduler.schedule(runnable, delay, unit);
        } finally {
            this.lock.unlock();
        }

    }

    public Future debounce(String id, Callable callable, long delay, TimeUnit unit) {
        this.lock.lock();

        Future var6;
        try {
            if (this.prev != null) {
                this.prev.cancel(false);
            }

            this.prev = scheduler.schedule(callable, delay, unit);
            this.id = id;
            var6 = this.prev;
        } finally {
            this.lock.unlock();
        }

        return var6;
    }

    public void shutdown() {
        this.lock.lock();

        try {
            if (this.prev != null) {
                this.prev.cancel(false);
                this.prev = null;
            }
        } finally {
            this.lock.unlock();
        }

    }
}
