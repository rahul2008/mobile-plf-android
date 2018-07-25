package com.philips.platform.catk;


import android.util.Log;

import com.philips.cdp.registration.User;
import com.philips.cdp.registration.handlers.RefreshLoginSessionHandler;
import com.philips.platform.catk.listener.RefreshTokenListener;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

class RefreshTokenHandler implements RefreshLoginSessionHandler {
    private static final int STATE_NONE = 0;
    private static final int STATE_TOKEN_REFRESHING = 1;
    private static final int STATE_TOKEN_REFRESH_SUCCESSFUL = 2;
    private static final int STATE_TOKEN_REFRESH_FAILED = 3;
    private User user;
    private int errorCode;

    AtomicInteger refreshState = new AtomicInteger(STATE_NONE);
    List<RefreshTokenListener> refreshTokenListeners = new ArrayList<>();

    RefreshTokenHandler(User user) {
        this.user = user;
    }

    void refreshToken(RefreshTokenListener tokenListener) {

        switch (refreshState.get()) {

            case STATE_NONE:
                refreshState.set(STATE_TOKEN_REFRESHING);
                refreshTokenListeners.add(tokenListener);
                refreshToken();
                break;
            case STATE_TOKEN_REFRESHING:
                refreshTokenListeners.add(tokenListener);
                break;
            case STATE_TOKEN_REFRESH_SUCCESSFUL:
                tokenListener.onRefreshSuccess();
                break;
            case STATE_TOKEN_REFRESH_FAILED:
                tokenListener.onRefreshFailed(errorCode);
                break;
            default:
                tokenListener.onRefreshFailed(errorCode);
                break;
        }
    }

    private void refreshToken() {
        user.refreshLoginSession(this);
    }

    @Override
    public void onRefreshLoginSessionSuccess() {
        refreshState.set(STATE_TOKEN_REFRESH_SUCCESSFUL);
        for (RefreshTokenListener tokenListener : refreshTokenListeners) {
            tokenListener.onRefreshSuccess();
        }
        clearListAndMakeStateNone();
    }

    @Override
    public void onRefreshLoginSessionFailedWithError(int error) {
        refreshState.set(STATE_TOKEN_REFRESH_FAILED);
        errorCode = error;
        for (RefreshTokenListener tokenListener : refreshTokenListeners) {
            tokenListener.onRefreshFailed(error);
        }
        clearListAndMakeStateNone();
    }

    @Override
    public void onRefreshLoginSessionInProgress(String message) {

    }

    private void clearListAndMakeStateNone() {
        refreshTokenListeners.clear();
        refreshState.set(STATE_NONE);
    }
}
