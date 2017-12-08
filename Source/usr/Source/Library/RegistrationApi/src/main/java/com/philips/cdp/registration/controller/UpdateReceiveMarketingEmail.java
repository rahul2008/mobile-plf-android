/*
 *  Copyright (c) Koninklijke Philips N.V., 2016
 *  All rights are reserved. Reproduction or dissemination
 *  * in whole or in part is prohibited without the prior written
 *  * consent of the copyright holder.
 * /
 */

package com.philips.cdp.registration.controller;

import android.content.Context;

import com.janrain.android.Jump;
import com.philips.cdp.registration.handlers.UpdateUserDetailsHandler;
import com.philips.cdp.registration.settings.JanrainInitializer;
import com.philips.cdp.registration.ui.utils.ThreadUtils;
import com.philips.cdp.registration.update.UpdateUser;

import org.json.JSONException;
import org.json.JSONObject;


public class UpdateReceiveMarketingEmail extends UpdateUserDetailsBase {

    private final static String USER_RECEIVE_MARKETING_EMAIL = "receiveMarketingEmail";

    private boolean mReceiveMarketingEmail;

    public UpdateReceiveMarketingEmail(Context context) {
        mJanrainInitializer = new JanrainInitializer();
        mContext = context;
    }

    public void updateMarketingEmailStatus(final UpdateUserDetailsHandler
                                                   updateUserDetailsHandler,
                                           final boolean receiveMarketingEmail) {
        mUpdateUserDetails = updateUserDetailsHandler;
        mReceiveMarketingEmail = receiveMarketingEmail;
        if (isJanrainInitializeRequired()) {
            mJanrainInitializer.initializeJanrain(mContext, this);
            return;
        }
        performActualUpdate();
    }

    protected void performActualUpdate() {
        JSONObject userData = getCurrentUserAsJsonObject();
        mUpdatedUserdata = Jump.getSignedInUser();
        try {
            if (null != mUpdatedUserdata) {
                mUpdatedUserdata.put(USER_RECEIVE_MARKETING_EMAIL, mReceiveMarketingEmail);
                UpdateUser updateUser = new UpdateUser();
                updateUser.update(mUpdatedUserdata, userData, this);
            }
        } catch (JSONException e) {
            e.printStackTrace();
            if (null != mUpdateUserDetails)
                ThreadUtils.postInMainThread(mContext,()->
                mUpdateUserDetails.
                        onUpdateFailedWithError(-1));
        }
    }

    protected void performLocalUpdate() {
        if (null != mUpdatedUserdata)
            try {
                mUpdatedUserdata.put(USER_RECEIVE_MARKETING_EMAIL, mReceiveMarketingEmail);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        mUpdatedUserdata.saveToDisk(mContext);
    }
}