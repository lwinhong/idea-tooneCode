package com.tooneCode.topics;

import com.intellij.util.messages.Topic;

public interface CefPageLoadedTopic {
    Topic<CefPageLoadedTopic> ANY_GENERATE_NOTIFICATION = Topic.create("ANY Generate Notifier", CefPageLoadedTopic.class);

    void anyGenerate();
}
