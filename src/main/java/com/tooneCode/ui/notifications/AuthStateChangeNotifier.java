package com.tooneCode.ui.notifications;

import com.intellij.util.messages.Topic;
import com.tooneCode.core.model.model.AuthStatus;

public interface AuthStateChangeNotifier {
    Topic<AuthStateChangeNotifier> AUTH_CHANGE_NOTIFICATION = Topic.create("Auth Change Notifier", AuthStateChangeNotifier.class);

    void notifyChangeAuth(AuthStatus var1);
}

