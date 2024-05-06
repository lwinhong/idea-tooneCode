package com.tooneCode.ui.notifications;

import com.intellij.util.messages.Topic;
import com.tooneCode.core.model.model.AuthStatus;

public interface AuthLogoutNotifier {
    Topic<AuthLogoutNotifier> AUTH_LOGOUT_NOTIFICATION = Topic.create("Auth Logout Notifier", AuthLogoutNotifier.class);

    void notifyLogout(AuthStatus var1);
}

