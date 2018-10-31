
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
import com.philips.cdp.registration.app.tagging.AppTagingConstants;
import com.philips.cdp.registration.dao.DIUserProfile;
import com.philips.cdp.registration.dao.UserRegistrationFailureInfo;
import com.philips.cdp.registration.errors.ErrorCodes;
import com.philips.cdp.registration.errors.ErrorType;
import com.philips.cdp.registration.errors.URError;
import com.philips.cdp.registration.events.JumpFlowDownloadStatusListener;
import com.philips.cdp.registration.handlers.TraditionalRegistrationHandler;
import com.philips.cdp.registration.settings.RegistrationHelper;
import com.philips.cdp.registration.settings.UserRegistrationInitializer;
import com.philips.cdp.registration.ui.utils.FieldsValidator;
import com.philips.cdp.registration.ui.utils.RLog;
import com.philips.cdp.registration.ui.utils.ThreadUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Locale;

public class RegisterTraditional implements Jump.SignInResultHandler, Jump.SignInCodeHandler, JumpFlowDownloadStatusListener, TraditionalRegistrationHandler {

    private String TAG = "RegisterTraditional";

    private Context mContext;

    private TraditionalRegistrationHandler mTraditionalRegisterHandler;

    private DIUserProfile mProfile;

    public RegisterTraditional(TraditionalRegistrationHandler traditionalRegisterHandler,
                               Context context) {
        mTraditionalRegisterHandler = traditionalRegisterHandler;
        mContext = context;
    }

    @Override
    public void onSuccess() {
        RLog.d(TAG, "onSuccess : is called");
        Jump.saveToDisk(mContext);
        ThreadUtils.postInMainThread(mContext, () ->
                mTraditionalRegisterHandler.onRegisterSuccess());
    }

    @Override
    public void onCode(String code) {
        RLog.d(TAG, "onCode : is called");
    }

    @Override
    public void onFailure(SignInError error) {
        try {
            RLog.e(TAG, "onFailure : is called error: "  + error.captureApiError.raw_response);
            UserRegistrationFailureInfo userRegistrationFailureInfo = new UserRegistrationFailureInfo(error.captureApiError, mContext);
            if (error.captureApiError.code == ErrorCodes.UNKNOWN_ERROR) {
                userRegistrationFailureInfo.setErrorDescription(new URError(mContext).getLocalizedError(ErrorType.NETWOK, ErrorCodes.NETWORK_ERROR));
                userRegistrationFailureInfo.setErrorTagging(AppTagingConstants.REG_JAN_RAIN_SERVER_CONNECTION_FAILED);
            }
            userRegistrationFailureInfo.setErrorCode(error.captureApiError.code);
            ThreadUtils.postInMainThread(mContext, () ->
                    mTraditionalRegisterHandler.onRegisterFailedWithFailure(userRegistrationFailureInfo));
        } catch (Exception e) {
            RLog.e(TAG, "onFailure: Exception : " + e.getMessage());
            loginFailed();
        }
    }

    private void loginFailed() {
        UserRegistrationFailureInfo userRegistrationFailureInfo = new UserRegistrationFailureInfo( mContext);
        userRegistrationFailureInfo.setErrorCode(ErrorCodes.UNKNOWN_ERROR);
        ThreadUtils.postInMainThread(mContext, () ->
                mTraditionalRegisterHandler.onRegisterFailedWithFailure(userRegistrationFailureInfo));
    }

    // moved app logic to set user info (traditional login) in diuserprofile to
    // framework.
    public void registerUserInfoForTraditional(String firstName, String lastName, String mUserEmailorMobile,
                                               String password, boolean olderThanAgeLimit, boolean isReceiveMarketingEmail
    ) {
        RLog.d(TAG, "registerUserInfoForTraditional : is called");
        mProfile = new DIUserProfile();
        mProfile.setGivenName(firstName);
        mProfile.setFamilyName(lastName);
        if (FieldsValidator.isValidEmail(mUserEmailorMobile)) {
            mProfile.setEmail(mUserEmailorMobile);
        } else {
            mProfile.setMobile(mUserEmailorMobile);
        }
        mProfile.setPassword(password);
        mProfile.setOlderThanAgeLimit(olderThanAgeLimit);
        mProfile.setReceiveMarketingEmail(isReceiveMarketingEmail);

        if (!UserRegistrationInitializer.getInstance().isJumpInitializated()) {
            UserRegistrationInitializer.getInstance().registerJumpFlowDownloadListener(this);
        } else {
            if (mTraditionalRegisterHandler != null) {
                registerNewUserUsingTraditional();
                RLog.d(TAG, "registerUserInfoForTraditional : registerNewUserUsingTraditional");
            }
            return;


        }
        if (!UserRegistrationInitializer.getInstance().isRegInitializationInProgress()) {
            RLog.d(TAG, "registerUserInfoForTraditional : Jump not initialized, initializing");
            RegistrationHelper.getInstance().initializeUserRegistration(mContext);
        }


    }


    // For Traditional Registration
    private void registerNewUserUsingTraditional() {

        if (mProfile != null) {

            JSONObject newUser = new JSONObject();
            try {
                // PrimaryAddress
                JSONObject primaryAddressObject = new JSONObject();
                primaryAddressObject.put("country",RegistrationHelper.getInstance().getCountryCode());
                JSONArray primaryAddressArray = new JSONArray();
                primaryAddressArray.put(primaryAddressObject);

                newUser.put("email", mProfile.getEmail())
                        .put("mobileNumber", mProfile.getMobile())
                        .put("givenName", mProfile.getGivenName())
                        .put("password", mProfile.getPassword())
                        .put("olderThanAgeLimit", mProfile.getOlderThanAgeLimit())
                        .put("receiveMarketingEmail", mProfile.getReceiveMarketingEmail())
                        .put("familyName", mProfile.getFamilyName())
                        .put("preferredLanguage",Locale.getDefault().getLanguage())
                        .put("primaryAddress",primaryAddressObject);
                new RussianConsent().addRussianConsent(newUser);
            } catch (JSONException e) {
                RLog.e(TAG, "registerNewUserUsingTraditional : " + e.getMessage());
            }

            Jump.registerNewUser(newUser, null, this);
        } else {
            UserRegistrationFailureInfo userRegistrationFailureInfo = new UserRegistrationFailureInfo(mContext);
            userRegistrationFailureInfo.setErrorCode(ErrorCodes.UNKNOWN_ERROR);
            userRegistrationFailureInfo.setErrorDescription(new URError(mContext).getLocalizedError(ErrorType.JANRAIN, ErrorCodes.UNKNOWN_ERROR));
            userRegistrationFailureInfo.setErrorTagging(AppTagingConstants.REG_JAN_RAIN_SERVER_CONNECTION_FAILED);
            ThreadUtils.postInMainThread(mContext, () ->
                    mTraditionalRegisterHandler.onRegisterFailedWithFailure(userRegistrationFailureInfo));
        }
    }


    @Override
    public void onFlowDownloadSuccess() {
        if (mTraditionalRegisterHandler != null) {
            RLog.d(TAG, "onFlowDownloadSuccess : registerNewUserUsingTraditional");
            registerNewUserUsingTraditional();
        }
        UserRegistrationInitializer.getInstance().unregisterJumpFlowDownloadListener();
    }

    @Override
    public void onFlowDownloadFailure() {
        RLog.d(TAG, "onFlowDownloadFailure : is called");
        if (mTraditionalRegisterHandler != null) {
            UserRegistrationFailureInfo userRegistrationFailureInfo = new UserRegistrationFailureInfo(mContext);
            userRegistrationFailureInfo.setErrorDescription(new URError(mContext).getLocalizedError(ErrorType.JANRAIN, ErrorCodes.UNKNOWN_ERROR));
            userRegistrationFailureInfo.setErrorTagging(AppTagingConstants.REG_JAN_RAIN_SERVER_CONNECTION_FAILED);
            userRegistrationFailureInfo.setErrorCode(ErrorCodes.REGISTER_TRADITIONAL_FAILED_SERVER_ERROR);
            ThreadUtils.postInMainThread(mContext, () ->
                    mTraditionalRegisterHandler.onRegisterFailedWithFailure(userRegistrationFailureInfo));
        }

    }


    @Override
    public void onRegisterSuccess() {
        RLog.d(TAG, "onRegisterSuccess : is called");
        ThreadUtils.postInMainThread(mContext, () ->
                mTraditionalRegisterHandler.onRegisterSuccess());
    }

    @Override
    public void onRegisterFailedWithFailure(UserRegistrationFailureInfo userRegistrationFailureInfo) {
        RLog.d(TAG, "onRegisterFailedWithFailure : is called");
        ThreadUtils.postInMainThread(mContext, () ->
                mTraditionalRegisterHandler.onRegisterFailedWithFailure(userRegistrationFailureInfo));
    }
}
