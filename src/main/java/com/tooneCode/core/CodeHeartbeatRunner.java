package com.tooneCode.core;

import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.project.Project;

import java.nio.ByteBuffer;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import javax.websocket.Session;

import com.tooneCode.core.lsp.LanguageServer;

public class CodeHeartbeatRunner implements Runnable {
    private Session session;
    private LanguageServer server;
    private Project project;

    public CodeHeartbeatRunner(Project project, Session session, LanguageServer server) {
        this.session = session;
        this.server = server;
        this.project = project;
    }

    @Override
    public void run() {

    }
}
