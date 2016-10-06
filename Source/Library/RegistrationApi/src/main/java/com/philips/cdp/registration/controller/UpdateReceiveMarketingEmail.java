/*
 *  Copyright (c) Koninklijke Philips N.V., 2016
 *  All rights are reserved. Reproduction or dissemination
 *  * in whole or in part is prohibited without the prior written
 *  * consent of the copyright holder.
 * /
 */

package com.philips.cdp.registration.controller;

import android.content.Context;
import android.util.Log;

import com.janrain.android.capture.Capture;
import com.janrain.android.capture.Capture.CaptureApiRequestCallback;
import com.janrain.android.capture.CaptureApiError;
import com.janrain.android.capture.CaptureRecord;
import com.philips.cdp.registration.User;
import com.philips.cdp.registration.events.JumpFlowDownloadStatusListener;
import com.philips.cdp.registration.handlers.RefreshLoginSessionHandler;
import com.philips.cdp.registration.handlers.UpdateReceiveMarketingEmailHandler;
import com.philips.cdp.registration.settings.RegistrationHelper;
import com.philips.cdp.registration.settings.UserRegistrationInitializer;
import com.philips.cdp.registration.ui.utils.RLog;

import org.json.JSONException;
import org.json.JSONObject;

public class UpdateReceiveMarketingEmail implements CaptureApiRequestCallback ,
        JumpFlowDownloadStatusListener {

    public UpdateReceiveMarketingEmailHandler mUpdateReceiveMarketingEmailHandler;

    private Context mContext;

    private boolean mReceiveMarketingEmail;

    private CaptureRecord mCapturedData;

    private String USER_RECEIVE_MARKETING_EMAIL = "receiveMarketingEmail";

    public UpdateReceiveMarketingEmail(
            UpdateReceiveMarketingEmailHandler updateReceiveMarketingEmailHandler, Context context,
            boolean receiveMarketingEmail) {
        mUpdateReceiveMarketingEmailHandler = updateReceiveMarketingEmailHandler;
        mContext = context;
        mReceiveMarketingEmail = receiveMarketingEmail;
    }

    public void onSuccess() {
        CaptureRecord capture = CaptureRecord.loadFromDisk(mContext);

        try {
            capture.put(USER_RECEIVE_MARKETING_EMAIL, mReceiveMarketingEmail);
            capture.saveToDisk(mContext);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        mUpdateReceiveMarketingEmailHandler.onUpdateReceiveMarketingEmailSuccess();

    }

    public void onFailure(CaptureApiError error) {
        RLog.d("Error", "Error" + error.code + error.error_description);
        //Session Expired and refresh during this case
        if (null != error && error.code == 414) {
            User user = new User(mContext);
            user.refreshLoginSession(new RefreshLoginSessionHandler() {
                @Override
                public void onRefreshLoginSessionSuccess() {
                    updateMarketingEmailStatus();
                }

                @Override
                public void onRefreshLoginSessionFailedWithError(int error) {
                    mUpdateReceiveMarketingEmailHandler
                            .onUpdateReceiveMarketingEmailFailedWithError(error);
                }

                @Override
                public void onRefreshLoginSessionInProgress(String message) {

                }
            });
            return;
        }
        mUpdateReceiveMarketingEmailHandler
                .onUpdateReceiveMarketingEmailFailedWithError(error.code);

    }

    public void updateMarketingEmailStatus() {
        if(!UserRegistrationInitializer.getInstance().isJumpInitializated()) {
            UserRegistrationInitializer.getInstance().registerJumpFlowDownloadListener(this);
        }else{
            performActualUpdate();
            return;
        }
        if (!UserRegistrationInitializer.getInstance().isRegInitializationInProgress()) {
            RegistrationHelper.getInstance().initializeUserRegistration(mContext);
        }
    }

    private void performActualUpdate() {
        mCapturedData = CaptureRecord.loadFromDisk(mContext);
        JSONObject originalUserInfo = CaptureRecord.loadFromDisk(mContext);
        if (mCapturedData != null) {
            try {
                mCapturedData.put(USER_RECEIVE_MARKETING_EMAIL, mReceiveMarketingEmail);
                try {
                    mCapturedData.synchronize(this, originalUserInfo);
                } catch (Capture.InvalidApidChangeException e) {
                    Log.e("User Registration",
                            "On updateReceiveMarketingEmail,Caught InvalidApidChange Exception");
                }
            } catch (JSONException e) {
                Log.e("User Registration", "On updateReceiveMarketingEmail,Caught JSON Exception");
            }
        }
    }

    @Override
    public void onFlowDownloadSuccess() {
        performActualUpdate();
        UserRegistrationInitializer.getInstance().unregisterJumpFlowDownloadListener();
    }

    @Override
    public void onFlowDownloadFailure() {
        mUpdateReceiveMarketingEmailHandler
                .onUpdateReceiveMarketingEmailFailedWithError(-1);
        UserRegistrationInitializer.getInstance().unregisterJumpFlowDownloadListener();
    }
}
