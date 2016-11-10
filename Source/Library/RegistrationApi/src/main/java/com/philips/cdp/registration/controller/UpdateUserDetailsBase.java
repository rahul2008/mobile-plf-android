/*
 *  Copyright (c) Koninklijke Philips N.V., 2016
 *  All rights are reserved. Reproduction or dissemination
 *  * in whole or in part is prohibited without the prior written
 *  * consent of the copyright holder.
 * /
 */

package com.philips.cdp.registration.controller;

import android.content.Context;

import com.janrain.android.capture.CaptureRecord;
import com.philips.cdp.registration.User;
import com.philips.cdp.registration.handlers.RefreshLoginSessionHandler;
import com.philips.cdp.registration.handlers.UpdateUserDetailsHandler;
import com.philips.cdp.registration.settings.JanrainInitializer;
import com.philips.cdp.registration.ui.utils.RLog;
import com.philips.cdp.registration.update.UpdateUser;

public class UpdateUserDetailsBase implements
        JanrainInitializer.JanrainInitializeListener,
        UpdateUser.UpdateUserListener, RefreshLoginSessionHandler {


    protected UpdateUserDetailsHandler mUpdateUserDetails;

    protected Context mContext;

    protected CaptureRecord mUpdatedUserdata;


    protected JanrainInitializer mJanrainInitializer;


    protected void performActualUpdate() {
    }

    protected void performLocalUpdate() {
        if (null != mUpdatedUserdata)
            mUpdatedUserdata.saveToDisk(mContext);
    }

    @Override
    public void onJanrainInitializeSuccess() {
        performActualUpdate();
    }

    @Override
    public void onJanrainInitializeFailed() {
        if (null != mUpdateUserDetails)
            mUpdateUserDetails
                    .onUpdateFailedWithError(-1);

    }

    @Override
    public boolean isJanrainInitializeRequired() {
        return mJanrainInitializer.isJanrainInitializeRequired();
    }

    @Override
    public void onUserUpdateSuccess() {
        performLocalUpdate();
        if (null != mUpdateUserDetails)
            mUpdateUserDetails.onUpdateSuccess();
    }

    @Override
    public void onUserUpdateFailed(int error) {
        RLog.d("Error", "Error" + error);
        if (error == -1) {
            if (null != mUpdateUserDetails) {
                mUpdateUserDetails
                        .onUpdateFailedWithError(-1);
            }
            return;
        }
        //Session Expired and refresh during this case
        if (error == 414) {
            User user = new User(mContext);
            user.refreshLoginSession(this);
            return;
        }
        mUpdateUserDetails
                .onUpdateFailedWithError(error);

    }

    @Override
    public void onRefreshLoginSessionSuccess() {
        performActualUpdate();
    }

    @Override
    public void onRefreshLoginSessionFailedWithError(int error) {
        if (null != mUpdateUserDetails)
            mUpdateUserDetails
                    .onUpdateFailedWithError(error);
    }

    @Override
    public void onRefreshLoginSessionInProgress(String message) {
        //PerformNothing
    }
}
