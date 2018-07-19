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
import com.philips.cdp.registration.handlers.TraditionalLoginHandler;
import com.philips.cdp.registration.handlers.UpdateUserRecordHandler;
import com.philips.cdp.registration.settings.RegistrationHelper;
import com.philips.cdp.registration.settings.UserRegistrationInitializer;
import com.philips.cdp.registration.ui.utils.RLog;
import com.philips.cdp.registration.ui.utils.ThreadUtils;

public class LoginTraditional extends BaseHSDPLogin implements Jump.SignInResultHandler, JumpFlowDownloadStatusListener {


    private Context mContext;

    private TraditionalLoginHandler mTraditionalLoginHandler;

    private UpdateUserRecordHandler mUpdateUserRecordHandler;

    private String mEmail;

    private String mPassword;

    private User mUser;

    private final static String TAG = LoginTraditional.class.getSimpleName();

    public LoginTraditional(TraditionalLoginHandler traditionalLoginHandler, Context context,
                            UpdateUserRecordHandler updateUserRecordHandler, String email, String password) {
        super(context);
        mTraditionalLoginHandler = traditionalLoginHandler;
        mContext = context;
        mUpdateUserRecordHandler = updateUserRecordHandler;
        mEmail = email;
        mPassword = password;
        mUser = new User(mContext);
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

        mUpdateUserRecordHandler.updateUserRecordLogin();
        if (RegistrationConfiguration.getInstance().isHsdpFlow() && (mUser.isEmailVerified() || mUser.isMobileVerified())) {
            loginIntoHsdp();
        } else {
            ThreadUtils.postInMainThread(mContext, () -> mTraditionalLoginHandler.onLoginSuccess());
        }
    }


//    @Override
//    public void onCode(String code) {
//        RLog.d(TAG, "onCode : is called");
//    }

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
        String emailorMobile = getUserEmailOrMobile(mUser);
        hsdpLogin(mUser.getAccessToken(), emailorMobile, mTraditionalLoginHandler);

//        RLog.d(TAG, "loginIntoHsdp : is called");
//        final User mUser = new User(mContext);
//        HsdpUser hsdpUser = new HsdpUser(mContext);
//        HsdpUserRecord hsdpUserRecord = hsdpUser.getHsdpUserRecord();
//        if (hsdpUserRecord == null) {
//            String emailOrMobile;
//            if (RegistrationHelper.getInstance().isMobileFlow()) {
//                emailOrMobile = mUser.getMobile();
//            } else {
//                emailOrMobile = mUser.getEmail();
//            }
//            hsdpUser.login(emailOrMobile, mUser.getAccessToken(), Jump.getRefreshSecret(), new SocialLoginHandler() {
//                @Override
//                public void onLoginSuccess() {
//                    ThreadUtils.postInMainThread(mContext, () ->
//                            mTraditionalLoginHandler.onLoginSuccess());
//                    RLog.d(TAG, "loginIntoHsdp : SocialLoginHandler : onLoginSuccess is called");
//                }
//
//                @Override
//                public void onLoginFailedWithError(UserRegistrationFailureInfo userRegistrationFailureInfo) {
//                    AppTaggingErrors.trackActionLoginError(userRegistrationFailureInfo, AppTagingConstants.HSDP);
//                    ThreadUtils.postInMainThread(mContext, () ->
//                            mTraditionalLoginHandler.onLoginFailedWithError(userRegistrationFailureInfo));
//                    RLog.d(TAG, "loginIntoHsdp : SocialLoginHandler : onLoginFailedWithError is called");
//                }
//            });
//        }
    }
}
