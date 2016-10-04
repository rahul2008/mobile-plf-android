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
import com.janrain.android.capture.CaptureApiError;
import com.philips.cdp.registration.R;
import com.philips.cdp.registration.User;
import com.philips.cdp.registration.configuration.RegistrationConfiguration;
import com.philips.cdp.registration.dao.UserRegistrationFailureInfo;
import com.philips.cdp.registration.events.JumpFlowDownloadStatusListener;
import com.philips.cdp.registration.handlers.SocialLoginHandler;
import com.philips.cdp.registration.handlers.TraditionalLoginHandler;
import com.philips.cdp.registration.handlers.UpdateUserRecordHandler;
import com.philips.cdp.registration.hsdp.HsdpUser;
import com.philips.cdp.registration.hsdp.HsdpUserRecord;
import com.philips.cdp.registration.settings.RegistrationHelper;
import com.philips.cdp.registration.settings.UserRegistrationInitializer;
import com.philips.cdp.registration.ui.utils.RLog;
import com.philips.cdp.registration.ui.utils.RegConstants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.security.SecureRandom;

public class LoginTraditional implements Jump.SignInResultHandler, Jump.SignInCodeHandler, JumpFlowDownloadStatusListener {


    private Context mContext;

    private TraditionalLoginHandler mTraditionalLoginHandler;

    private UpdateUserRecordHandler mUpdateUserRecordHandler;

    private String mEmail;

    private String mPassword;

    public LoginTraditional(TraditionalLoginHandler traditionalLoginHandler, Context context,
                            UpdateUserRecordHandler updateUserRecordHandler, String email, String password) {
        mTraditionalLoginHandler = traditionalLoginHandler;
        mContext = context;
        mUpdateUserRecordHandler = updateUserRecordHandler;
        mEmail = email;
        mPassword = password;
    }



    public void loginTraditionally(final String email, final String password) {
        if(!UserRegistrationInitializer.getInstance().isJumpInitializated()) {
            UserRegistrationInitializer.getInstance().registerJumpFlowDownloadListener(this);
        }else{
            Jump.performTraditionalSignIn(email, password, this, null);
            return;
        }
        if (!UserRegistrationInitializer.getInstance().isRegInitializationInProgress()) {
            RegistrationHelper.getInstance().initializeUserRegistration(mContext);
        }
    }

    public void mergeTraditionally(final String email, final String password, final String token) {
        if (!UserRegistrationInitializer.getInstance().isJumpInitializated()) {
            UserRegistrationInitializer.getInstance().registerJumpFlowDownloadListener(this);
        }
        else {
            Jump.performTraditionalSignIn(email, password, this, token);
            return;
        }
        if (!UserRegistrationInitializer.getInstance().isRegInitializationInProgress()) {
            RegistrationHelper.getInstance().initializeUserRegistration(mContext);
        }
    }

    @Override
    public void onSuccess() {
        Jump.saveToDisk(mContext);
        final User user = new User(mContext);
        mUpdateUserRecordHandler.updateUserRecordLogin();
        if (RegistrationConfiguration.getInstance().isHsdpFlow() && user.getEmailVerificationStatus()) {

            HsdpUser hsdpUser = new HsdpUser(mContext);
            hsdpUser.socialLogin(user.getEmail(), user.getAccessToken(), new SocialLoginHandler() {

                @Override
                public void onLoginSuccess() {
                    mTraditionalLoginHandler.onLoginSuccess();
                }

                @Override
                public void onLoginFailedWithError(UserRegistrationFailureInfo userRegistrationFailureInfo) {
                    mTraditionalLoginHandler.onLoginFailedWithError(userRegistrationFailureInfo);
                }
            });

          
        } else {
            mTraditionalLoginHandler.onLoginSuccess();
        }
    }

    @Override
    public void onCode(String code) {

    }

    @Override
    public void onFailure(SignInError error) {
        try{
            UserRegistrationFailureInfo userRegistrationFailureInfo = new UserRegistrationFailureInfo();
            userRegistrationFailureInfo.setError(error.captureApiError);
            handleInvalidInputs(error.captureApiError, userRegistrationFailureInfo);
            handleInvalidCredentials(error.captureApiError, userRegistrationFailureInfo);
            userRegistrationFailureInfo.setErrorCode(error.captureApiError.code);
            mTraditionalLoginHandler.onLoginFailedWithError(userRegistrationFailureInfo);
        }catch (Exception e){
            RLog.e("Login failed :","exception :"+e.getMessage());
        }
    }

    private void handleInvalidInputs(CaptureApiError error,
                                     UserRegistrationFailureInfo userRegistrationFailureInfo) {
        if (null != error && null != error.error
                && error.error.equals(RegConstants.INVALID_FORM_FIELDS)) {
            try {
                JSONObject object = error.raw_response;
                JSONObject jsonObject = (JSONObject) object.get(RegConstants.INVALID_FIELDS);
                if (jsonObject != null) {

                    if (!jsonObject.isNull(RegConstants.TRADITIONAL_SIGN_IN_EMAIL_ADDRESS)) {
                        userRegistrationFailureInfo.setEmailErrorMessage(getErrorMessage(jsonObject
                                .getJSONArray(RegConstants.TRADITIONAL_SIGN_IN_EMAIL_ADDRESS)));
                    }

                    if (!jsonObject.isNull(RegConstants.TRADITIONAL_SIGN_IN_PASSWORD)) {
                        userRegistrationFailureInfo
                                .setPasswordErrorMessage(getErrorMessage(jsonObject
                                        .getJSONArray(RegConstants.TRADITIONAL_SIGN_IN_PASSWORD)));
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private void handleInvalidCredentials(CaptureApiError error, UserRegistrationFailureInfo userRegistrationFailureInfo) {
        if (null != error && null != error.error
                && error.error.equals(RegConstants.INVALID_CREDENTIALS)) {
            try {
                JSONObject object = error.raw_response;
                JSONObject jsonObject = (JSONObject) object.get(RegConstants.INVALID_FIELDS);
                if (jsonObject != null) {
                    if (!jsonObject.isNull(RegConstants.USER_INFORMATION_FORM)) {
                        userRegistrationFailureInfo.setEmailErrorMessage(mContext.getResources().getString(R.string.reg_JanRain_Invalid_Credentials));
                        /*userRegistrationFailureInfo.setEmailErrorMessage(getErrorMessage(jsonObject
                                .getJSONArray(RegConstants.USER_INFORMATION_FORM)));*/
                    }

                    if (!jsonObject.isNull(RegConstants.USER_INFORMATION_FORM)) {
                        userRegistrationFailureInfo.setPasswordErrorMessage(mContext.getResources().getString(R.string.reg_JanRain_Invalid_Credentials));
                        /*userRegistrationFailureInfo
                                .setPasswordErrorMessage(getErrorMessage(jsonObject
                                        .getJSONArray(RegConstants.USER_INFORMATION_FORM)));*/
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

    @Override
    public void onFlowDownloadSuccess() {
        Jump.performTraditionalSignIn(mEmail, mPassword, this, null);
        UserRegistrationInitializer.getInstance().unregisterJumpFlowDownloadListener();
    }

    @Override
    public void onFlowDownloadFailure() {
        if (mTraditionalLoginHandler != null) {
            UserRegistrationFailureInfo userRegistrationFailureInfo = new UserRegistrationFailureInfo();
            userRegistrationFailureInfo.setErrorDescription(mContext.getString(R.string.reg_JanRain_Server_Connection_Failed));
            userRegistrationFailureInfo.setErrorCode(RegConstants.MERGE_TRADITIONAL_FAILED_SERVER_ERROR);
            mTraditionalLoginHandler.onLoginFailedWithError(userRegistrationFailureInfo);
        }
        UserRegistrationInitializer.getInstance().unregisterJumpFlowDownloadListener();

    }


    public void loginIntoHsdp() {
        final User user = new User(mContext);
        HsdpUser hsdpUser = new HsdpUser(mContext);
        HsdpUserRecord hsdpUserRecord = hsdpUser.getHsdpUserRecord();
        if (hsdpUserRecord == null) {
            hsdpUser.socialLogin(mEmail, user.getAccessToken(), new SocialLoginHandler() {

                @Override
                public void onLoginSuccess() {
                    mTraditionalLoginHandler.onLoginSuccess();
                }

                @Override
                public void onLoginFailedWithError(UserRegistrationFailureInfo userRegistrationFailureInfo) {
                    mTraditionalLoginHandler.onLoginFailedWithError(userRegistrationFailureInfo);
                }
            });
        }
    }

//    private static String generateRefreshSecret() {
//        final int SECRET_LENGTH = 40;
//        SecureRandom random = new SecureRandom();
//        StringBuilder buffer = new StringBuilder();
//
//        while (buffer.length() < SECRET_LENGTH) {
//            buffer.append(Integer.toHexString(random.nextInt()));
//        }
//        String refreshSecret = buffer.toString().substring(0, SECRET_LENGTH);
//        return refreshSecret;
//    }
//
//    private static void printRefreshSecretUsage() {
//        System.out.println("Invalid refreshSecret command,Please provide valid command");
//        System.out.println("Example : java -jar RequestAuthenticator.jar getrefreshsecret");
//    }
}
