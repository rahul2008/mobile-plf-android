package com.philips.cdp.registration.controller;


import android.content.Context;

import com.janrain.android.capture.CaptureRecord;
import com.philips.cdp.registration.com.philips.cdp.registartion.utils.Profile;
import com.philips.cdp.registration.configuration.RegistrationConfiguration;
import com.philips.cdp.registration.events.JumpFlowDownloadStatusListener;
import com.philips.cdp.registration.handlers.RefreshLoginSessionHandler;
import com.philips.cdp.registration.hsdp.HsdpUser;
import com.philips.cdp.registration.settings.RegistrationHelper;
import com.philips.cdp.registration.settings.UserRegistrationInitializer;
import com.philips.cdp.registration.ui.utils.RLog;
import com.philips.cdp.registration.ui.utils.RegConstants;

public class RefreshUserSession implements RefreshLoginSessionHandler, JumpFlowDownloadStatusListener {

    private RefreshLoginSessionHandler mRefreshLoginSessionHandler;
    private Context mContext;
    private String LOG_TAG = "RefreshUserSession";

    public RefreshUserSession(RefreshLoginSessionHandler refreshLoginSessionHandler, Context context) {
        mRefreshLoginSessionHandler = refreshLoginSessionHandler;
        mContext = context;
    }
    

    private void refreshSession() {
        CaptureRecord captureRecord = CaptureRecord.loadFromDisk(mContext);
        if (captureRecord == null) {
            return;
        }
        captureRecord.refreshAccessToken(new RefreshLoginSession(this), mContext);
    }

    private void refreshHsdpAccessToken() {
        final HsdpUser hsdpUser = new HsdpUser(mContext);
        hsdpUser.refreshToken(new RefreshLoginSessionHandler() {
            @Override
            public void onRefreshLoginSessionSuccess() {
                mRefreshLoginSessionHandler.onRefreshLoginSessionSuccess();
            }

            @Override
            public void onRefreshLoginSessionFailedWithError(int error) {
                if (error == Integer.parseInt(RegConstants.INVALID_ACCESS_TOKEN_CODE)
                        || error == Integer.parseInt(RegConstants.INVALID_REFRESH_TOKEN_CODE)) {
                    Profile profile = new Profile(mContext);
                    profile.clearData();
                    RegistrationHelper.getInstance().getUserRegistrationListener().notifyOnLogoutSuccessWithInvalidAccessToken();
                }
                mRefreshLoginSessionHandler.onRefreshLoginSessionFailedWithError(error);
            }
        });
    }

    public void refreshUserSession() {

        if (!UserRegistrationInitializer.getInstance().isJumpInitializated()) {
            UserRegistrationInitializer.getInstance().registerJumpFlowDownloadListener(this);
        }

        if (UserRegistrationInitializer.getInstance().isJumpInitializated()) {
            RLog.i(LOG_TAG, "Jump initialized, refreshUserSession");

            refreshSession();
            return;

        } else if (!UserRegistrationInitializer.getInstance().isRegInitializationInProgress()) {
            RLog.i(LOG_TAG, "Jump not initialized, initializing");
            RegistrationHelper.getInstance().initializeUserRegistration(mContext, RegistrationHelper.getInstance().getLocale());
        }
    }

    @Override
    public void onFlowDownloadSuccess() {
        RLog.i(LOG_TAG, "Jump  initialized now after coming to this screen,  was in progress earlier, now performing forgot password");
        refreshSession();
        UserRegistrationInitializer.getInstance().unregisterJumpFlowDownloadListener();
    }

    @Override
    public void onFlowDownloadFailure() {
        RLog.i(LOG_TAG, "Jump not initialized, was initialized but failed");
        mRefreshLoginSessionHandler.onRefreshLoginSessionFailedWithError(-1);
        UserRegistrationInitializer.getInstance().unregisterJumpFlowDownloadListener();
    }

    @Override
    public void onRefreshLoginSessionSuccess() {
        if (RegistrationConfiguration.getInstance().getHsdpConfiguration().isHsdpFlow()) {
            // refreshLoginSessionHandler.onRefreshLoginSessionSuccess();
            refreshHsdpAccessToken();
            return;
        }
        mRefreshLoginSessionHandler.onRefreshLoginSessionSuccess();

    }

    @Override
    public void onRefreshLoginSessionFailedWithError(int error) {
        mRefreshLoginSessionHandler.onRefreshLoginSessionFailedWithError(error);

    }
}
