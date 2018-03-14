
/*
 *  Copyright (c) Koninklijke Philips N.V., 2016
 *  All rights are reserved. Reproduction or dissemination
 *  * in whole or in part is prohibited without the prior written
 *  * consent of the copyright holder.
 * /
 */

package com.philips.cdp.registration.controller;

import android.app.Activity;
import android.content.Context;
import com.janrain.android.Jump;
import com.janrain.android.engage.session.JRProvider;
import com.janrain.android.engage.types.JRDictionary;
import com.philips.cdp.registration.*;
import com.philips.cdp.registration.app.tagging.AppTagingConstants;
import com.philips.cdp.registration.configuration.RegistrationConfiguration;
import com.philips.cdp.registration.dao.UserRegistrationFailureInfo;
import com.philips.cdp.registration.events.JumpFlowDownloadStatusListener;
import com.philips.cdp.registration.handlers.*;
import com.philips.cdp.registration.hsdp.HsdpUser;
import com.philips.cdp.registration.settings.*;
import com.philips.cdp.registration.ui.utils.*;

import org.json.JSONObject;

public class LoginSocialNativeProvider implements Jump.SignInResultHandler, Jump.SignInCodeHandler,
        JumpFlowDownloadStatusListener {
    private Context mContext;
    private SocialProviderLoginHandler mSocialLoginHandler;
    private UpdateUserRecordHandler mUpdateUserRecordHandler;
    public LoginSocialNativeProvider(SocialProviderLoginHandler socialLoginHandler, Context context,
                                     UpdateUserRecordHandler updateUserRecordHandler) {
        mSocialLoginHandler = socialLoginHandler;
        mContext = context;
        mUpdateUserRecordHandler = updateUserRecordHandler;
    }

    @Override
    public void onSuccess() {
        Jump.saveToDisk(mContext);
        User user = new User(mContext);
        mUpdateUserRecordHandler.updateUserRecordLogin();
        if (RegistrationConfiguration.getInstance().isHsdpFlow() &&
                (user.isEmailVerified() || user.isMobileVerified())) {
            HsdpUser hsdpUser = new HsdpUser(mContext);
            String emailorMobile;
            if (FieldsValidator.isValidEmail(user.getEmail())) {
                emailorMobile = user.getEmail();
            } else {
                emailorMobile = user.getMobile();
            }
            hsdpUser.socialLogin(emailorMobile, user.getAccessToken(), Jump.getRefreshSecret(),
                    new SocialLoginHandler() {

                        @Override
                        public void onLoginSuccess() {
                            ThreadUtils.postInMainThread(mContext, () -> mSocialLoginHandler.onLoginSuccess());
                        }

                        @Override
                        public void onLoginFailedWithError(UserRegistrationFailureInfo userRegistrationFailureInfo) {
                            ThreadUtils.postInMainThread(mContext, () -> mSocialLoginHandler.onLoginFailedWithError(userRegistrationFailureInfo));
                        }
                    });

        } else {
            ThreadUtils.postInMainThread(mContext, () -> mSocialLoginHandler.onLoginSuccess());
        }

    }


    @Override
    public void onCode(String code) {

    }

    @Override
    public void onFailure(SignInError error) {
        if (error.reason == SignInError.FailureReason.CAPTURE_API_ERROR
                && error.captureApiError.isMergeFlowError()) {
            String emailId = null;
            if (null != error.auth_info) {
                JRDictionary profile = error.auth_info.getAsDictionary("profile");
                emailId = profile.getAsString("email");
            }
            mMergeToken = error.captureApiError.getMergeToken();
            final String existingProvider = error.captureApiError
                    .getExistingAccountIdentityProvider();
            String conflictingIdentityProvider = error.captureApiError
                    .getConflictingIdentityProvider();
            String conflictingIdpNameLocalized = JRProvider
                    .getLocalizedName(conflictingIdentityProvider);
            String existingIdpNameLocalized = JRProvider
                    .getLocalizedName(conflictingIdentityProvider);
            String finalEmailId = emailId;
            ThreadUtils.postInMainThread(mContext, () -> mSocialLoginHandler.onLoginFailedWithMergeFlowError(mMergeToken, existingProvider,
                    conflictingIdentityProvider, conflictingIdpNameLocalized,
                    existingIdpNameLocalized, finalEmailId));

        } else if (error.reason == SignInError.FailureReason.CAPTURE_API_ERROR
                && error.captureApiError.isTwoStepRegFlowError()) {

            JSONObject prefilledRecord = error.captureApiError.getPreregistrationRecord();
            String socialRegistrationToken = error.captureApiError.getSocialRegistrationToken();
            ThreadUtils.postInMainThread(mContext, () -> mSocialLoginHandler.onLoginFailedWithTwoStepError(prefilledRecord,
                    socialRegistrationToken));

        } else {

            UserRegistrationFailureInfo userRegistrationFailureInfo = new UserRegistrationFailureInfo();
            userRegistrationFailureInfo.setErrorCode(RegConstants.DI_PROFILE_NULL_ERROR_CODE);
            ThreadUtils.postInMainThread(mContext, () -> mSocialLoginHandler.onLoginFailedWithError(userRegistrationFailureInfo));

        }
    }

    private Activity mActivity;
    private String mProviderName;

    private String mAccessToken;
    private String mTokenSecret;
    private String mMergeToken;

    public void loginSocial(final Activity fromActivity,
                            final String providerName,
                            final String accessToken,
                            final String tokenSecret,
                            final String mergeToken) {
        mActivity = fromActivity;
        mProviderName = providerName;
        mMergeToken = mergeToken;
        mAccessToken = accessToken;
        mTokenSecret = tokenSecret;
        if (!UserRegistrationInitializer.getInstance().isJumpInitializated()) {
            UserRegistrationInitializer.getInstance().registerJumpFlowDownloadListener(this);
        } else {
            Jump.startTokenAuthForNativeProvider(mActivity,
                    mProviderName, mAccessToken, mTokenSecret, this, mMergeToken);
            return;
        }
        if (!UserRegistrationInitializer.getInstance().isRegInitializationInProgress()) {
            RegistrationHelper.getInstance().initializeUserRegistration(mContext);
        }
    }

    @Override
    public void onFlowDownloadSuccess() {
        Jump.startTokenAuthForNativeProvider(mActivity,
                mProviderName, mAccessToken, mTokenSecret, this, mMergeToken);
        UserRegistrationInitializer.getInstance().unregisterJumpFlowDownloadListener();
    }

    @Override
    public void onFlowDownloadFailure() {
        if (mSocialLoginHandler != null) {
            UserRegistrationFailureInfo userRegistrationFailureInfo = new UserRegistrationFailureInfo();
            userRegistrationFailureInfo.setErrorDescription(mContext.getString(R.string.reg_JanRain_Server_Connection_Failed));
            userRegistrationFailureInfo.setErrorTagging(AppTagingConstants.REG_JAN_RAIN_SERVER_CONNECTION_FAILED);
            userRegistrationFailureInfo.setErrorCode(RegConstants.SOCIAL_LOGIN_FAILED_SERVER_ERROR);
            ThreadUtils.postInMainThread(mContext,()->
                    mSocialLoginHandler.onLoginFailedWithError(userRegistrationFailureInfo));
        }
        UserRegistrationInitializer.getInstance().unregisterJumpFlowDownloadListener();

    }
}
