/*
 *  Copyright (c) Koninklijke Philips N.V., 2016
 *  All rights are reserved. Reproduction or dissemination
 *  * in whole or in part is prohibited without the prior written
 *  * consent of the copyright holder.
 * /
 */

package com.philips.cdp.registration.controller;

import android.content.Context;
import androidx.annotation.VisibleForTesting;

import com.janrain.android.Jump;
import com.philips.cdp.registration.User;
import com.philips.cdp.registration.app.tagging.AppTaggingErrors;
import com.philips.cdp.registration.app.tagging.AppTagingConstants;
import com.philips.cdp.registration.configuration.RegistrationConfiguration;
import com.philips.cdp.registration.dao.UserRegistrationFailureInfo;
import com.philips.cdp.registration.errors.ErrorCodes;
import com.philips.cdp.registration.errors.ErrorType;
import com.philips.cdp.registration.errors.URError;
import com.philips.cdp.registration.events.JumpFlowDownloadStatusListener;
import com.philips.cdp.registration.handlers.LoginHandler;
import com.philips.cdp.registration.settings.RegistrationHelper;
import com.philips.cdp.registration.settings.UserRegistrationInitializer;
import com.philips.cdp.registration.ui.utils.RLog;
import com.philips.cdp.registration.ui.utils.ThreadUtils;

public class LoginTraditional implements Jump.SignInResultHandler, JumpFlowDownloadStatusListener {

    private Context mContext;

    private LoginHandler mLoginHandler;

    private String mEmail;

    private String mPassword;

    private User mUser;

    private HSDPLoginService mHsdpLoginService;

    private final static String TAG = "LoginTraditional";

    public LoginTraditional(LoginHandler loginHandler, Context context,
                            String email, String password) {
        mLoginHandler = loginHandler;
        mContext = context;
        mEmail = email;
        mPassword = password;
        //mUser = new User(mContext);
        getUser();
        mHsdpLoginService = new HSDPLoginService(mContext);
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

        final RegistrationConfiguration registrationConfiguration = RegistrationConfiguration.getInstance();
        RLog.d(TAG, "onSuccess : isHSDPSkipLoginConfigurationAvailable : " + registrationConfiguration.isHSDPSkipLoginConfigurationAvailable());
        RLog.d(TAG, "onSuccess : isHsdpFlow : " + registrationConfiguration.isHsdpFlow());
        if (!registrationConfiguration.isHSDPSkipLoginConfigurationAvailable() && registrationConfiguration.isHsdpFlow() && (getUser().isEmailVerified() || getUser().isMobileVerified())) {
            loginIntoHsdp();
        } else {
            ThreadUtils.postInMainThread(mContext, () -> mLoginHandler.onLoginSuccess());
        }
    }

    @Override
    public void onFailure(SignInError error) {
        try {
            RLog.e(TAG, "onFailure : is called error: "+error.captureApiError.raw_response);
            UserRegistrationFailureInfo userRegistrationFailureInfo = new UserRegistrationFailureInfo(error.captureApiError, mContext);
            userRegistrationFailureInfo.setErrorDescription(error.captureApiError.error_description);
            userRegistrationFailureInfo.setErrorCode(error.captureApiError.code);
            AppTaggingErrors.trackActionLoginError(userRegistrationFailureInfo, AppTagingConstants.JANRAIN);
            ThreadUtils.postInMainThread(mContext, () ->
                    mLoginHandler.onLoginFailedWithError(userRegistrationFailureInfo));
        } catch (Exception e) {
            RLog.e(TAG, "onFailure: exception :" + e.getMessage());
            UserRegistrationFailureInfo userRegistrationFailureInfo = new UserRegistrationFailureInfo(mContext);
            userRegistrationFailureInfo.setErrorCode(ErrorCodes.UNKNOWN_ERROR);
            ThreadUtils.postInMainThread(mContext, () ->
                    mLoginHandler.onLoginFailedWithError(userRegistrationFailureInfo));
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
        if (mLoginHandler != null) {
            UserRegistrationFailureInfo userRegistrationFailureInfo = new UserRegistrationFailureInfo(mContext);
            userRegistrationFailureInfo.setErrorDescription(new URError(mContext).getLocalizedError(ErrorType.JANRAIN, ErrorCodes.TRADITIONAL_LOGIN_FAILED_SERVER_ERROR));
            userRegistrationFailureInfo.setErrorTagging(AppTagingConstants.REG_JAN_RAIN_SERVER_CONNECTION_FAILED);
            userRegistrationFailureInfo.setErrorCode(ErrorCodes.TRADITIONAL_LOGIN_FAILED_SERVER_ERROR);
            ThreadUtils.postInMainThread(mContext, () ->
                    mLoginHandler.onLoginFailedWithError(userRegistrationFailureInfo));
        }
        UserRegistrationInitializer.getInstance().unregisterJumpFlowDownloadListener();
    }

    public void loginIntoHsdp() {
        RLog.d(TAG, "loginIntoHsdp : is called");
        String emailOrMobile = mHsdpLoginService.getUserEmailOrMobile(getUser());
        mHsdpLoginService.hsdpLogin(getUser().getAccessToken(), emailOrMobile, mLoginHandler);
    }

    @VisibleForTesting
    User getUser() {
        return new User(mContext);
    }

}
