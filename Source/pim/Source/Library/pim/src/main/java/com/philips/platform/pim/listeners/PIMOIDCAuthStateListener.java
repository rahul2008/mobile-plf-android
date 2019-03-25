package com.philips.platform.pim.listeners;

import net.openid.appauth.AuthState;

public interface PIMOIDCAuthStateListener {
    void onSuccess(AuthState state);
    void onError();
}
