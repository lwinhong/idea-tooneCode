package com.tooneCode.ui.notifications;

import com.intellij.util.messages.Topic;
import com.tooneCode.core.model.model.AuthStatus;

public interface AuthLoginNotifier {
    Topic<AuthLoginNotifier> AUTH_LOGIN_NOTIFICATION = Topic.create("Auth Login Notifier", AuthLoginNotifier.class);

    void notifyLoginAuth(AuthStatus var1);
}
