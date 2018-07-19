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
import com.philips.cdp.registration.User;
import com.philips.cdp.registration.app.tagging.AppTaggingErrors;
import com.philips.cdp.registration.app.tagging.AppTagingConstants;
import com.philips.cdp.registration.configuration.RegistrationConfiguration;
import com.philips.cdp.registration.dao.UserRegistrationFailureInfo;
import com.philips.cdp.registration.errors.ErrorCodes;
import com.philips.cdp.registration.events.JumpFlowDownloadStatusListener;
import com.philips.cdp.registration.handlers.SocialLoginHandler;
import com.philips.cdp.registration.handlers.TraditionalLoginHandler;
import com.philips.cdp.registration.handlers.UpdateUserRecordHandler;
import com.philips.cdp.registration.hsdp.HsdpUser;
import com.philips.cdp.registration.hsdp.HsdpUserRecordV2;
import com.philips.cdp.registration.settings.RegistrationHelper;
import com.philips.cdp.registration.settings.UserRegistrationInitializer;
import com.philips.cdp.registration.ui.utils.FieldsValidator;
import com.philips.cdp.registration.ui.utils.RLog;
import com.philips.cdp.registration.ui.utils.ThreadUtils;

public class LoginTraditional implements Jump.SignInResultHandler, Jump.SignInCodeHandler, JumpFlowDownloadStatusListener {


    private Context mContext;

    private TraditionalLoginHandler mTraditionalLoginHandler;

    private UpdateUserRecordHandler mUpdateUserRecordHandler;

    private String mEmail;

    private String mPassword;

    private final static String TAG = LoginTraditional.class.getSimpleName();

    public LoginTraditional(TraditionalLoginHandler traditionalLoginHandler, Context context,
                            UpdateUserRecordHandler updateUserRecordHandler, String email, String password) {
        mTraditionalLoginHandler = traditionalLoginHandler;
        mContext = context;
        mUpdateUserRecordHandler = updateUserRecordHandler;
        mEmail = email;
        mPassword = password;
    }


    public void loginTraditionally(final String email, final String password) {
        RLog.d(TAG, "loginTraditionally : is called");
        if (!UserRegistrationInitializer.getInstance().isJumpInitializated()) {
            RLog.d(TAG, "loginTraditionally : not isJumpInitializated");
            UserRegistrationInitializer.getInstance().registerJumpFlowDownloadListener(this);
        } else {
            Jump.performTraditionalSignIn(email, password, this, null);
            return;
        }
        if (!UserRegistrationInitializer.getInstance().isRegInitializationInProgress()) {
            RegistrationHelper.getInstance().initializeUserRegistration(mContext);
        }
    }

    public void mergeTraditionally(final String email, final String password, final String token) {
        RLog.d(TAG, "mergeTraditionally : is called");
        if (!UserRegistrationInitializer.getInstance().isJumpInitializated()) {
            UserRegistrationInitializer.getInstance().registerJumpFlowDownloadListener(this);
        } else {
            Jump.performTraditionalSignIn(email, password, this, token);
            return;
        }
        if (!UserRegistrationInitializer.getInstance().isRegInitializationInProgress()) {
            RegistrationHelper.getInstance().initializeUserRegistration(mContext);
        }
    }

    @Override
    public void onSuccess() {
        RLog.d(TAG, "onSuccess : is called");
        Jump.saveToDisk(mContext);
        final User user = new User(mContext);
        mUpdateUserRecordHandler.updateUserRecordLogin();
        if (RegistrationConfiguration.getInstance().isHsdpFlow() && (user.isEmailVerified() || user.isMobileVerified())) {
            String emailorMobile = getUserEmailOrMobile(user);
            hsdpLogin(user, emailorMobile);
        } else {
            ThreadUtils.postInMainThread(mContext, () -> mTraditionalLoginHandler.onLoginSuccess());
        }
    }

    private String getUserEmailOrMobile(User user) {
        String emailorMobile;
        if (FieldsValidator.isValidEmail(user.getEmail())) {
            emailorMobile = user.getEmail();
        } else {
            emailorMobile = user.getMobile();
        }
        return emailorMobile;
    }

    private void hsdpLogin(User user, String emailorMobile) {
        HsdpUser hsdpUser = new HsdpUser(mContext);
        hsdpUser.login(emailorMobile, user.getAccessToken(), Jump.getRefreshSecret(), new SocialLoginHandler() {

            @Override
            public void onLoginSuccess() {
                ThreadUtils.postInMainThread(mContext, () ->
                        mTraditionalLoginHandler.onLoginSuccess());
            }

            @Override
            public void onLoginFailedWithError(UserRegistrationFailureInfo userRegistrationFailureInfo) {
                AppTaggingErrors.trackActionLoginError(userRegistrationFailureInfo, AppTagingConstants.HSDP);
                ThreadUtils.postInMainThread(mContext, () -> mTraditionalLoginHandler.onLoginFailedWithError(userRegistrationFailureInfo));
            }
        });
    }

    @Override
    public void onCode(String code) {
        RLog.d(TAG, "onCode : is called");
    }

    @Override
    public void onFailure(SignInError error) {
        RLog.d(TAG, "onFailure : is called");
        try {
            UserRegistrationFailureInfo userRegistrationFailureInfo = new UserRegistrationFailureInfo(error.captureApiError, mContext);
            userRegistrationFailureInfo.setErrorDescription(error.captureApiError.error_description);
            userRegistrationFailureInfo.setErrorCode(error.captureApiError.code);
            AppTaggingErrors.trackActionLoginError(userRegistrationFailureInfo, AppTagingConstants.JANRAIN);
            ThreadUtils.postInMainThread(mContext, () ->
                    mTraditionalLoginHandler.onLoginFailedWithError(userRegistrationFailureInfo));
        } catch (Exception e) {
            RLog.e("Login failed :", "exception :" + e.getMessage());
            RLog.d(TAG, "onFailure : is called" + e.getMessage());
        }
    }

    @Override
    public void onFlowDownloadSuccess() {
        RLog.d(TAG, "onFlowDownloadSuccess : is called");
        Jump.performTraditionalSignIn(mEmail, mPassword, this, null);
        UserRegistrationInitializer.getInstance().unregisterJumpFlowDownloadListener();
    }

    @Override
    public void onFlowDownloadFailure() {
        RLog.d(TAG, "onFlowDownloadFailure : is called");
        if (mTraditionalLoginHandler != null) {
            UserRegistrationFailureInfo userRegistrationFailureInfo = new UserRegistrationFailureInfo(mContext);
            userRegistrationFailureInfo.setErrorDescription(mContext.getString(R.string.USR_Janrain_HSDP_ServerErrorMsg));
            userRegistrationFailureInfo.setErrorTagging(AppTagingConstants.REG_JAN_RAIN_SERVER_CONNECTION_FAILED);
            userRegistrationFailureInfo.setErrorCode(ErrorCodes.TRADITIONAL_LOGIN_FAILED_SERVER_ERROR);
            ThreadUtils.postInMainThread(mContext, () ->
                    mTraditionalLoginHandler.onLoginFailedWithError(userRegistrationFailureInfo));
        }
        UserRegistrationInitializer.getInstance().unregisterJumpFlowDownloadListener();
    }

    public void loginIntoHsdp() {
        RLog.d(TAG, "loginIntoHsdp : is called");
        final User user = new User(mContext);
        HsdpUser hsdpUser = new HsdpUser(mContext);
        HsdpUserRecordV2 hsdpUserRecordV2 = hsdpUser.getHsdpUserRecord();
        if (hsdpUserRecordV2 == null) {
            String emailOrMobile;
            if (RegistrationHelper.getInstance().isMobileFlow()) {
                emailOrMobile = user.getMobile();
            } else {
                emailOrMobile = user.getEmail();
            }
            hsdpUser.login(emailOrMobile, user.getAccessToken(), Jump.getRefreshSecret(), new SocialLoginHandler() {
                @Override
                public void onLoginSuccess() {
                    ThreadUtils.postInMainThread(mContext, () ->
                            mTraditionalLoginHandler.onLoginSuccess());
                    RLog.d(TAG, "loginIntoHsdp : SocialLoginHandler : onLoginSuccess is called");
                }

                @Override
                public void onLoginFailedWithError(UserRegistrationFailureInfo userRegistrationFailureInfo) {
                    AppTaggingErrors.trackActionLoginError(userRegistrationFailureInfo, AppTagingConstants.HSDP);
                    ThreadUtils.postInMainThread(mContext, () ->
                            mTraditionalLoginHandler.onLoginFailedWithError(userRegistrationFailureInfo));
                    RLog.d(TAG, "loginIntoHsdp : SocialLoginHandler : onLoginFailedWithError is called");
                }
            });
        }
    }
}
