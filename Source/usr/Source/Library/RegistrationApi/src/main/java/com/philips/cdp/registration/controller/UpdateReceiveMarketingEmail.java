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
import com.philips.cdp.registration.settings.RegistrationHelper;
import com.philips.cdp.registration.ui.utils.ThreadUtils;
import com.philips.cdp.registration.update.UpdateUser;
import com.philips.ntputils.ServerTime;
import com.philips.platform.appinfra.AppInfraInterface;

import org.json.JSONException;
import org.json.JSONObject;

import javax.inject.Inject;


public class UpdateReceiveMarketingEmail extends UpdateUserDetailsBase {

    private final static String USER_RECEIVE_MARKETING_EMAIL = "receiveMarketingEmail";
    private final static String LOCALE = "LOCALE";
    private final static String TIMESTAMP = "TIMESTAMP";
    private String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";

    @Inject
    AppInfraInterface appInfraInterface;

    private boolean mReceiveMarketingEmail;

    public UpdateReceiveMarketingEmail(Context context) {
        super(context);
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

    @Override
    protected void performActualUpdate() {
        JSONObject userData = getCurrentUserAsJsonObject();
        mUpdatedUserdata = Jump.getSignedInUser();
        try {
            if (null != mUpdatedUserdata) {
                mUpdatedUserdata.put(USER_RECEIVE_MARKETING_EMAIL, mReceiveMarketingEmail);
                mUpdatedUserdata.put(LOCALE, RegistrationHelper.getInstance().getLocale(mContext));
                mUpdatedUserdata.put(TIMESTAMP, ServerTime.getCurrentUTCTimeWithFormat(DATE_FORMAT));
                UpdateUser updateUser = new UpdateUser();
                updateUser.update(mUpdatedUserdata, userData, this);
            }
        } catch (JSONException e) {
            e.printStackTrace();
            if (null != mUpdateUserDetails)
                ThreadUtils.postInMainThread(mContext, () ->
                        mUpdateUserDetails.
                                onUpdateFailedWithError(-1));
        }
    }

    @Override
    protected void performLocalUpdate() {
        if (null != mUpdatedUserdata) {
            try {
                mUpdatedUserdata.put(USER_RECEIVE_MARKETING_EMAIL, mReceiveMarketingEmail);
                mUpdatedUserdata.put(LOCALE, RegistrationHelper.getInstance().getLocale(mContext));
                mUpdatedUserdata.put(TIMESTAMP, ServerTime.getCurrentUTCTimeWithFormat(DATE_FORMAT));
                mUpdatedUserdata.saveToDisk(mContext);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
