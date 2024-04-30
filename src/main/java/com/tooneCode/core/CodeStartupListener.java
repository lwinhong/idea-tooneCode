package com.tooneCode.core;

public interface CodeStartupListener {
    void onStartup();

    void onTimeout();

    void onFailed();

    void onCancelled();
}
