/*
 *  Copyright (c) Koninklijke Philips N.V., 2016
 *  All rights are reserved. Reproduction or dissemination
 *  * in whole or in part is prohibited without the prior written
 *  * consent of the copyright holder.
 * /
 */

package com.philips.cdp.registration.controller;

import android.content.Context;
import android.support.annotation.NonNull;

import com.janrain.android.Jump;
import com.philips.cdp.registration.handlers.UpdateUserDetailsHandler;
import com.philips.cdp.registration.settings.JanrainInitializer;
import com.philips.cdp.registration.ui.utils.ThreadUtils;
import com.philips.cdp.registration.update.UpdateUser;
import com.philips.ntputils.ServerTime;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class UpdateDateOfBirth extends UpdateUserDetailsBase {

    public final static String USER_DATE_OF_BIRTH = "birthday";

    public static final String DATE_FORMAT_FOR_DOB = "yyyy-MM-dd";

    private String mBirthDate;

    public UpdateDateOfBirth(Context context) {
        mJanrainInitializer = new JanrainInitializer();
        mContext = context;
    }

    public void updateDateOfBirth(@NonNull final UpdateUserDetailsHandler
                                          updateUserDetailsHandler,
                                  @NonNull final Date date) {
        mUpdateUserDetails = updateUserDetailsHandler;
        final SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT_FOR_DOB, Locale.ROOT);
        mBirthDate = sdf.format(date);
        try {
            Date firstDate = sdf.parse(mBirthDate);
            Date secondDate = sdf.parse(ServerTime.getCurrentTime());
            if (firstDate.compareTo(secondDate) > 0) {
                ThreadUtils.postInMainThread(mContext,()->
                mUpdateUserDetails.onUpdateFailedWithError(-1));
                return;
            }
            if (isJanrainInitializeRequired()) {
                mJanrainInitializer.initializeJanrain(mContext, this);
                return;
            }
            performActualUpdate();
        } catch (ParseException e) {
            ThreadUtils.postInMainThread(mContext,()->
            mUpdateUserDetails.onUpdateFailedWithError(-1));
        }
    }


    protected void performActualUpdate() {
        JSONObject userData = getCurrentUserAsJsonObject();
        mUpdatedUserdata = Jump.getSignedInUser();
        try {
            if (null != mUpdatedUserdata) {
                mUpdatedUserdata.put(USER_DATE_OF_BIRTH, mBirthDate);
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
                mUpdatedUserdata.put(USER_DATE_OF_BIRTH, mBirthDate);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        mUpdatedUserdata.saveToDisk(mContext);
    }
}