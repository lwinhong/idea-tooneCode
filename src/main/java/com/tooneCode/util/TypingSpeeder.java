package com.tooneCode.util;

import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.LogicalPosition;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;

public class TypingSpeeder {
    private static final int MAX_DEBOUNCE_TIME = 1500;
    private static final int MAX_INTERVAL = 1000;
    private Debouncer typeResetDebouncer = new Debouncer();
    private AtomicLong startTime = new AtomicLong(System.currentTimeMillis());
    private AtomicLong lastTime = new AtomicLong(System.currentTimeMillis());
    private AtomicLong endTime = new AtomicLong(System.currentTimeMillis());
    private AtomicLong charactersTyped = new AtomicLong(0L);
    private AtomicBoolean firstTyped = new AtomicBoolean(true);
    private AtomicReference<String> lastTypedChars = new AtomicReference((Object) null);
    private AtomicReference<String> currentTypedChars = new AtomicReference((Object) null);
    private AtomicInteger lastTypedCharRow = new AtomicInteger(-1);
    private AtomicInteger currentTypedCharRow = new AtomicInteger(-1);

    public TypingSpeeder() {
        this.reset();
    }

    public void reset() {
        long time = System.currentTimeMillis();
        this.startTime.getAndSet(time);
        this.lastTime.getAndSet(time);
        this.endTime.getAndSet(time);
        this.firstTyped.getAndSet(true);
        this.charactersTyped.getAndSet(0L);
        this.currentTypedChars.set(null);
    }

    public void clear() {
        this.reset();
        this.lastTypedChars.set(null);
        this.lastTypedCharRow.set(-1);
        this.currentTypedCharRow.set(-1);
    }

    public double getAvgSpeed() {
        double timeInSeconds = (double) (this.endTime.get() - this.startTime.get());
        if (timeInSeconds != 0.0 && this.charactersTyped.get() != 0L) {
            double avgTime = timeInSeconds / (double) this.charactersTyped.get();
            return avgTime > 1000.0 ? -1.0 : avgTime;
        } else {
            return -1.0;
        }
    }

    public double getLastSpeed() {
        double result = (double) (this.endTime.get() - this.lastTime.get());
        return result != 0.0 && !(result > 1000.0) ? result : -1.0;
    }

    public String getLastTypedChars() {
        return (String) this.lastTypedChars.get();
    }

    public String getCurrentTypedChars() {
        return (String) this.currentTypedChars.get();
    }

    public Integer getTypedCharRowDiff() {
        return this.lastTypedCharRow.get() >= 0 && this.currentTypedCharRow.get() >= 0 ? this.currentTypedCharRow.get() - this.lastTypedCharRow.get() : null;
    }

    public long getTypedLength() {
        return this.charactersTyped.get();
    }

    public synchronized void keyTyped(String typedChars) {
        if (typedChars.length() == 1) {
            if (this.firstTyped.getAndSet(false)) {
                this.startTime.getAndSet(System.currentTimeMillis());
            } else {
                this.charactersTyped.incrementAndGet();
            }

            long last = this.endTime.getAndSet(System.currentTimeMillis());
            this.lastTime.getAndSet(last);
            this.typeResetDebouncer.debounce(this::reset, 1500L, TimeUnit.MILLISECONDS);
        }
    }

    public synchronized void recordTyping(Editor editor, String typedChars) {
        if (typedChars.length() == 1) {
            LogicalPosition position = editor.getCaretModel().getLogicalPosition();
            int line = position.line;
            String lastChars = (String) this.currentTypedChars.getAndSet(typedChars);
            if (lastChars != null) {
                this.lastTypedChars.getAndSet(lastChars);
            }

            Integer lastRow = this.currentTypedCharRow.getAndSet(line);
            if (lastRow != null) {
                this.lastTypedCharRow.getAndSet(lastRow);
            }

        }
    }
}

