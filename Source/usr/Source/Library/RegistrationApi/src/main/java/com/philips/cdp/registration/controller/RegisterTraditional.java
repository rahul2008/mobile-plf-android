
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
import com.philips.cdp.registration.R;
import com.philips.cdp.registration.app.tagging.AppTagingConstants;
import com.philips.cdp.registration.dao.DIUserProfile;
import com.philips.cdp.registration.dao.UserRegistrationFailureInfo;
import com.philips.cdp.registration.events.JumpFlowDownloadStatusListener;
import com.philips.cdp.registration.handlers.TraditionalRegistrationHandler;
import com.philips.cdp.registration.handlers.UpdateUserRecordHandler;
import com.philips.cdp.registration.settings.RegistrationHelper;
import com.philips.cdp.registration.settings.UserRegistrationInitializer;
import com.philips.cdp.registration.ui.utils.FieldsValidator;
import com.philips.cdp.registration.ui.utils.RLog;
import com.philips.cdp.registration.ui.utils.RegConstants;
import com.philips.cdp.registration.ui.utils.RegUtility;
import com.philips.cdp.registration.ui.utils.ThreadUtils;

import org.json.JSONException;
import org.json.JSONObject;

public class RegisterTraditional implements Jump.SignInResultHandler, Jump.SignInCodeHandler, JumpFlowDownloadStatusListener, TraditionalRegistrationHandler {

    private String LOG_TAG = "RegisterTraditional";

    private Context mContext;

    private TraditionalRegistrationHandler mTraditionalRegisterHandler;

    private UpdateUserRecordHandler mUpdateUserRecordHandler;

    private DIUserProfile mProfile;

    public RegisterTraditional(TraditionalRegistrationHandler traditionalRegisterHandler,
                               Context context, UpdateUserRecordHandler updateUserRecordHandler) {
        mTraditionalRegisterHandler = traditionalRegisterHandler;
        mContext = context;
        mUpdateUserRecordHandler = updateUserRecordHandler;
    }

    @Override
    public void onSuccess() {
        Jump.saveToDisk(mContext);
        mUpdateUserRecordHandler.updateUserRecordRegister();
        ThreadUtils.postInMainThread(mContext,()->
        mTraditionalRegisterHandler.onRegisterSuccess());
    }

    @Override
    public void onCode(String code) {

    }

    @Override
    public void onFailure(SignInError error) {
        try {
            UserRegistrationFailureInfo userRegistrationFailureInfo = new UserRegistrationFailureInfo(error.captureApiError);
            if (error.captureApiError.code == -1) {
                userRegistrationFailureInfo.setErrorDescription(mContext.getString(R.string.reg_JanRain_Server_Connection_Failed));
                userRegistrationFailureInfo.setErrorTagging(AppTagingConstants.REG_JAN_RAIN_SERVER_CONNECTION_FAILED);
            }
            userRegistrationFailureInfo.setErrorCode(error.captureApiError.code);
            ThreadUtils.postInMainThread(mContext, () ->
                    mTraditionalRegisterHandler.onRegisterFailedWithFailure(userRegistrationFailureInfo));
        } catch (Exception e) {
        }

    }

    // moved app logic to set user info (traditional login) in diuserprofile to
    // framework.
    public void registerUserInfoForTraditional(String firstName, String lastName, String mUserEmailorMobile,
                                               String password, boolean olderThanAgeLimit, boolean isReceiveMarketingEmail
    ) {

        mProfile = new DIUserProfile();
        mProfile.setGivenName(firstName);
        mProfile.setFamilyName(lastName);
        if (FieldsValidator.isValidEmail(mUserEmailorMobile)){
            mProfile.setEmail(mUserEmailorMobile);
        }else {
            mProfile.setMobile(mUserEmailorMobile);
        }
        mProfile.setPassword(password);
        mProfile.setOlderThanAgeLimit(olderThanAgeLimit);
        mProfile.setReceiveMarketingEmail(isReceiveMarketingEmail);

        if (!UserRegistrationInitializer.getInstance().isJumpInitializated()) {
            UserRegistrationInitializer.getInstance().registerJumpFlowDownloadListener(this);
        } else {
            RLog.d(LOG_TAG, "Jump initialized, registering");
            if (mTraditionalRegisterHandler != null) {
                registerNewUserUsingTraditional();
            }
            return;


        }
        if (!UserRegistrationInitializer.getInstance().isRegInitializationInProgress()) {
            RLog.d(LOG_TAG, "Jump not initialized, initializing");
            RegistrationHelper.getInstance().initializeUserRegistration(mContext);
        }


    }


    // For Traditional Registration
    private void registerNewUserUsingTraditional() {

        if (mProfile != null) {

            JSONObject newUser = new JSONObject();
            try {
                newUser.put("email", mProfile.getEmail())
                        .put("mobileNumber", mProfile.getMobile())
                        .put("givenName", mProfile.getGivenName())
                        .put("password", mProfile.getPassword())
                        .put("olderThanAgeLimit", mProfile.getOlderThanAgeLimit())
                        .put("receiveMarketingEmail", mProfile.getReceiveMarketingEmail())
                        .put("familyName",mProfile.getFamilyName());
                new RussianConsent().addRussianConsent(newUser);
            } catch (JSONException e) {
            }

            Jump.registerNewUser(newUser, null, this);
        } else {
            UserRegistrationFailureInfo userRegistrationFailureInfo = new UserRegistrationFailureInfo();
            userRegistrationFailureInfo.setErrorCode(RegConstants.DI_PROFILE_NULL_ERROR_CODE);
            userRegistrationFailureInfo.setErrorDescription(mContext.getString(R.string.reg_JanRain_Server_Connection_Failed));
            userRegistrationFailureInfo.setErrorTagging(AppTagingConstants.REG_JAN_RAIN_SERVER_CONNECTION_FAILED);
            ThreadUtils.postInMainThread(mContext,()->
            mTraditionalRegisterHandler.onRegisterFailedWithFailure(userRegistrationFailureInfo));
        }
    }


    @Override
    public void onFlowDownloadSuccess() {
        if (mTraditionalRegisterHandler != null) {
            RLog.d(LOG_TAG, "Jump  initialized now after coming to this screen,  was in progress earlier, registering user");
            registerNewUserUsingTraditional();
        }
        UserRegistrationInitializer.getInstance().unregisterJumpFlowDownloadListener();
    }

    @Override
    public void onFlowDownloadFailure() {
        RLog.d(LOG_TAG, "Jump not initialized, was initialized but failed");
        if (mTraditionalRegisterHandler != null) {
            UserRegistrationFailureInfo userRegistrationFailureInfo = new UserRegistrationFailureInfo();
            userRegistrationFailureInfo.setErrorDescription(mContext.getString(R.string.reg_JanRain_Server_Connection_Failed));
            userRegistrationFailureInfo.setErrorTagging(AppTagingConstants.REG_JAN_RAIN_SERVER_CONNECTION_FAILED);
            userRegistrationFailureInfo.setErrorCode(RegConstants.REGISTER_TRADITIONAL_FAILED_SERVER_ERROR);
            ThreadUtils.postInMainThread(mContext,()->
            mTraditionalRegisterHandler.onRegisterFailedWithFailure(userRegistrationFailureInfo));
        }

    }


    @Override
    public void onRegisterSuccess() {
        ThreadUtils.postInMainThread(mContext,()->
        mTraditionalRegisterHandler.onRegisterSuccess());
    }

    @Override
    public void onRegisterFailedWithFailure(UserRegistrationFailureInfo userRegistrationFailureInfo) {
        ThreadUtils.postInMainThread(mContext,()->
        mTraditionalRegisterHandler.onRegisterFailedWithFailure(userRegistrationFailureInfo));
    }
}
