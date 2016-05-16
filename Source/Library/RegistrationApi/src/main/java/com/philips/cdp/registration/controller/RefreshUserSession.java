package com.philips.cdp.registration.controller;


import android.content.Context;

import com.janrain.android.capture.CaptureRecord;
import com.janrain.android.engage.session.JRSession;
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
        if(!UserRegistrationInitializer.getInstance().isRefreshUserSessionInProgress()) {
            CaptureRecord captureRecord = CaptureRecord.loadFromDisk(mContext);
            if (captureRecord == null) {
                return;
            }
            UserRegistrationInitializer.getInstance().setRefreshUserSessionInProgress(true);
            captureRecord.refreshAccessToken(new RefreshLoginSession(this), mContext);
        }else{
            mRefreshLoginSessionHandler.onRefreshLoginSessionInProgress("Refresh already scheduled");
        }
    }

    private void refreshHsdpAccessToken() {
        final HsdpUser hsdpUser = new HsdpUser(mContext);
        hsdpUser.refreshToken(new RefreshLoginSessionHandler() {
            @Override
            public void onRefreshLoginSessionSuccess() {
                UserRegistrationInitializer.getInstance().setRefreshUserSessionInProgress(false);
                mRefreshLoginSessionHandler.onRefreshLoginSessionSuccess();
            }

            @Override
            public void onRefreshLoginSessionFailedWithError(int error) {
                UserRegistrationInitializer.getInstance().setRefreshUserSessionInProgress(false);
                if (error == Integer.parseInt(RegConstants.INVALID_ACCESS_TOKEN_CODE)
                        || error == Integer.parseInt(RegConstants.INVALID_REFRESH_TOKEN_CODE)) {

                    clearData();
                    RegistrationHelper.getInstance().getUserRegistrationListener().notifyOnLogoutSuccessWithInvalidAccessToken();
                }
                mRefreshLoginSessionHandler.onRefreshLoginSessionFailedWithError(error);
            }

            @Override
            public void onRefreshLoginSessionInProgress(String message) {
                mRefreshLoginSessionHandler.onRefreshLoginSessionInProgress(message);
            }
        });
    }

    public void refreshUserSession() {

        if (!UserRegistrationInitializer.getInstance().isJumpInitializated()) {
            UserRegistrationInitializer.getInstance().registerJumpFlowDownloadListener(this);
        } else{
            RLog.i(LOG_TAG, "Jump initialized, refreshUserSession");

            refreshSession();
            return;

        }
        if (!UserRegistrationInitializer.getInstance().isRegInitializationInProgress()) {
            RLog.i(LOG_TAG, "Jump not initialized, initializing");
            RegistrationHelper.getInstance().initializeUserRegistration(mContext);
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
            refreshHsdpAccessToken();
            return;
        }
        UserRegistrationInitializer.getInstance().setRefreshUserSessionInProgress(false);
        mRefreshLoginSessionHandler.onRefreshLoginSessionSuccess();

    }

    @Override
    public void onRefreshLoginSessionFailedWithError(int error) {
        UserRegistrationInitializer.getInstance().setRefreshUserSessionInProgress(false);
        mRefreshLoginSessionHandler.onRefreshLoginSessionFailedWithError(error);

    }

    @Override
    public void onRefreshLoginSessionInProgress(String message) {
        mRefreshLoginSessionHandler.onRefreshLoginSessionInProgress(message);
    }

    //Added to avoid public access
    private void clearData() {
        HsdpUser hsdpUser = new HsdpUser(mContext);
        hsdpUser.deleteFromDisk();
        mContext.deleteFile(RegConstants.DI_PROFILE_FILE);
        if (JRSession.getInstance() != null) {
            JRSession.getInstance().signOutAllAuthenticatedUsers();
        }
        CaptureRecord.deleteFromDisk(mContext);
    }



}
