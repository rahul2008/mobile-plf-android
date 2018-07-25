package com.philips.platform.catk;


import android.util.Log;

import com.philips.cdp.registration.User;
import com.philips.cdp.registration.handlers.RefreshLoginSessionHandler;
import com.philips.platform.catk.listener.RefreshTokenListener;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

class RefreshTokenHandler {
    private static final int STATE_NONE = 0;
    private static final int STATE_TOKEN_REFRESHING = 1;
    private static final int STATE_TOKEN_REFRESH_SUCCESSFUL = 2;
    private static final int STATE_TOKEN_REFRESH_FAILED = 3;
    private User user;
    private int errorCode;

    private AtomicInteger refreshState = new AtomicInteger(STATE_NONE);
    private List<RefreshTokenListener> refreshTokenListeners = new ArrayList<>();

    RefreshTokenHandler(User user) {
        this.user = user;
    }

    void refreshToken(RefreshTokenListener tokenListener) {
        if (refreshState.get() == STATE_NONE) {
            refreshState.set(STATE_TOKEN_REFRESHING);
            refreshTokenListeners.add(tokenListener);
            refreshToken();
        } else if (refreshState.get() == STATE_TOKEN_REFRESHING) {
            refreshTokenListeners.add(tokenListener);
        } else {
            handleRefreshStates(tokenListener);
        }
    }

    private void handleRefreshStates(RefreshTokenListener tokenListener) {
        if (refreshState.get() == STATE_TOKEN_REFRESH_SUCCESSFUL) {
            tokenListener.onRefreshSuccess();
        } else {
            tokenListener.onRefreshFailed(errorCode);
        }
    }

    private void refreshToken() {
        user.refreshLoginSession(new RefreshLoginSessionHandler() {
            @Override
            public void onRefreshLoginSessionSuccess() {
                refreshState.set(STATE_TOKEN_REFRESH_SUCCESSFUL);
                for (RefreshTokenListener tokenListener : refreshTokenListeners) {
                    tokenListener.onRefreshSuccess();
                }
                refreshTokenListeners.clear();
                refreshState.set(STATE_NONE);
            }

            @Override
            public void onRefreshLoginSessionFailedWithError(int error) {
                refreshState.set(STATE_TOKEN_REFRESH_FAILED);
                errorCode = error;
                for (RefreshTokenListener tokenListener : refreshTokenListeners) {
                    tokenListener.onRefreshFailed(error);
                }
                refreshTokenListeners.clear();
                refreshState.set(STATE_NONE);
            }

            @Override
            public void onRefreshLoginSessionInProgress(String message) {
            }
        });
    }
}
