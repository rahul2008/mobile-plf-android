
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

import com.janrain.android.Jump;
import com.janrain.android.capture.CaptureApiError;
import com.philips.cdp.registration.R;
import com.philips.cdp.registration.User;
import com.philips.cdp.registration.dao.DIUserProfile;
import com.philips.cdp.registration.dao.UserRegistrationFailureInfo;
import com.philips.cdp.registration.events.JumpFlowDownloadStatusListener;
import com.philips.cdp.registration.handlers.TraditionalRegistrationHandler;
import com.philips.cdp.registration.handlers.UpdateUserRecordHandler;
import com.philips.cdp.registration.settings.RegistrationHelper;
import com.philips.cdp.registration.settings.UserRegistrationInitializer;
import com.philips.cdp.registration.ui.utils.RLog;
import com.philips.cdp.registration.ui.utils.RegConstants;
import com.philips.cdp.security.SecurityHelper;
import com.philips.platform.appinfra.AppInfra;
import com.philips.platform.appinfra.securestorage.SecureStorageInterface;

import org.json.JSONArray;
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
        User user = new User(mContext);
        saveDIUserProfileToDisk(mProfile);
        mUpdateUserRecordHandler.updateUserRecordRegister();
        mTraditionalRegisterHandler.onRegisterSuccess();
    }

    @Override
    public void onCode(String code) {

    }

    @Override
    public void onFailure(SignInError error) {
        UserRegistrationFailureInfo userRegistrationFailureInfo = new UserRegistrationFailureInfo();
        userRegistrationFailureInfo.setError(error.captureApiError);
        if (error.captureApiError.code == -1) {
            userRegistrationFailureInfo.setErrorDescription(mContext.getString(R.string.JanRain_Server_Connection_Failed));
        }
        handleInvalidInputs(error.captureApiError, userRegistrationFailureInfo);
        userRegistrationFailureInfo.setErrorCode(error.captureApiError.code);
        mTraditionalRegisterHandler.onRegisterFailedWithFailure(userRegistrationFailureInfo);
    }

    private void handleInvalidInputs(CaptureApiError error,
                                     UserRegistrationFailureInfo userRegistrationFailureInfo) {
        if (null != error && null != error.error
                && error.error.equals(RegConstants.INVALID_FORM_FIELDS)) {
            try {
                JSONObject object = error.raw_response;
                JSONObject jsonObject = (JSONObject) object.get(RegConstants.INVALID_FIELDS);
                if (jsonObject != null) {

                    if (!jsonObject.isNull(RegConstants.TRADITIONAL_REGISTRATION_FIRST_NAME)) {
                        userRegistrationFailureInfo
                                .setFirstNameErrorMessage(getErrorMessage(jsonObject
                                        .getJSONArray(RegConstants.TRADITIONAL_REGISTRATION_FIRST_NAME)));
                    }

                    if (!jsonObject.isNull(RegConstants.TRADITIONAL_REGISTRATION_EMAIL_ADDRESS)) {
                        userRegistrationFailureInfo
                                .setEmailErrorMessage(getErrorMessage(jsonObject
                                        .getJSONArray(RegConstants.TRADITIONAL_REGISTRATION_EMAIL_ADDRESS)));
                    }

                    if (!jsonObject.isNull(RegConstants.TRADITIONAL_REGISTRATION_PASSWORD)) {
                        userRegistrationFailureInfo
                                .setPasswordErrorMessage(getErrorMessage(jsonObject
                                        .getJSONArray(RegConstants.TRADITIONAL_REGISTRATION_PASSWORD)));
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }

    private String getErrorMessage(JSONArray jsonArray)
            throws JSONException {
        if (null == jsonArray) {
            return null;
        }
        return (String) jsonArray.get(0);
    }


    // moved app logic to set user info (traditional login) in diuserprofile to
    // framework.
    public void registerUserInfoForTraditional(String mGivenName, String mUserEmail,
                                               String password, boolean olderThanAgeLimit, boolean isReceiveMarketingEmail
    ) {

        mProfile = new DIUserProfile();
        mProfile.setGivenName(mGivenName);
        mProfile.setEmail(mUserEmail);
        mProfile.setPassword(password);
        mProfile.setOlderThanAgeLimit(olderThanAgeLimit);
        mProfile.setReceiveMarketingEmail(isReceiveMarketingEmail);

        if (!UserRegistrationInitializer.getInstance().isJumpInitializated()) {
            UserRegistrationInitializer.getInstance().registerJumpFlowDownloadListener(this);
        } else {
            RLog.i(LOG_TAG, "Jump initialized, registering");

            if (mTraditionalRegisterHandler != null) {
                registerNewUserUsingTraditional();
            }
            return;


        }
        if (!UserRegistrationInitializer.getInstance().isRegInitializationInProgress()) {
            RLog.i(LOG_TAG, "Jump not initialized, initializing");
            RegistrationHelper.getInstance().initializeUserRegistration(mContext);
        }


    }


    // For Traditional Registration
    private void registerNewUserUsingTraditional() {

        if (mProfile != null) {

            JSONObject newUser = new JSONObject();
            try {
                newUser.put("email", mProfile.getEmail()).
                        put("givenName", mProfile.getGivenName())
                        .put("password", mProfile.getPassword())
                        .put("olderThanAgeLimit", mProfile.getOlderThanAgeLimit())
                        .put("receiveMarketingEmail", mProfile.getReceiveMarketingEmail());
            } catch (JSONException e) {
                Log.e(LOG_TAG, "On registerNewUserUsingTraditional,Caught JSON Exception");
            }

            Jump.registerNewUser(newUser, null, this);
        } else {
            UserRegistrationFailureInfo userRegistrationFailureInfo = new UserRegistrationFailureInfo();
            userRegistrationFailureInfo.setErrorCode(RegConstants.DI_PROFILE_NULL_ERROR_CODE);
            userRegistrationFailureInfo.setErrorDescription(mContext.getString(R.string.JanRain_Server_Connection_Failed));
            mTraditionalRegisterHandler.onRegisterFailedWithFailure(userRegistrationFailureInfo);
        }
    }


    @Override
    public void onFlowDownloadSuccess() {
        if (mTraditionalRegisterHandler != null) {
            RLog.i(LOG_TAG, "Jump  initialized now after coming to this screen,  was in progress earlier, registering user");
            registerNewUserUsingTraditional();
        }
        UserRegistrationInitializer.getInstance().unregisterJumpFlowDownloadListener();
    }

    @Override
    public void onFlowDownloadFailure() {

        RLog.i(LOG_TAG, "Jump not initialized, was initialized but failed");
        if (mTraditionalRegisterHandler != null) {
            UserRegistrationFailureInfo userRegistrationFailureInfo = new UserRegistrationFailureInfo();
            userRegistrationFailureInfo.setErrorDescription(mContext.getString(R.string.JanRain_Server_Connection_Failed));
            userRegistrationFailureInfo.setErrorCode(RegConstants.REGISTER_TRADITIONAL_FAILED_SERVER_ERROR);
            mTraditionalRegisterHandler.onRegisterFailedWithFailure(userRegistrationFailureInfo);
        }

    }


    @Override
    public void onRegisterSuccess() {
        saveDIUserProfileToDisk(mProfile);
        mTraditionalRegisterHandler.onRegisterSuccess();
    }

    @Override
    public void onRegisterFailedWithFailure(UserRegistrationFailureInfo userRegistrationFailureInfo) {
        mTraditionalRegisterHandler.onRegisterFailedWithFailure(userRegistrationFailureInfo);
    }


    //Added to avoid direct access
    private void saveDIUserProfileToDisk(DIUserProfile diUserProfile) {
            diUserProfile.setPassword(null);
            SecureStorageInterface secureStorageInterface = new AppInfra.Builder().build(mContext).getSecureStorage();
            secureStorageInterface.storeValueForKey(RegConstants.DI_PROFILE_FILE, SecurityHelper.objectToString(diUserProfile));
    }

}
