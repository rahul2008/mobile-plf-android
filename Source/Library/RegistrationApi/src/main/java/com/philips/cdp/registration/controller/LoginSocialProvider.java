
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
import com.philips.cdp.registration.R;
import com.philips.cdp.registration.User;
import com.philips.cdp.registration.app.tagging.AppTaggingErrors;
import com.philips.cdp.registration.app.tagging.AppTagingConstants;
import com.philips.cdp.registration.configuration.RegistrationConfiguration;
import com.philips.cdp.registration.dao.UserRegistrationFailureInfo;
import com.philips.cdp.registration.events.JumpFlowDownloadStatusListener;
import com.philips.cdp.registration.handlers.SocialLoginHandler;
import com.philips.cdp.registration.handlers.SocialProviderLoginHandler;
import com.philips.cdp.registration.handlers.UpdateUserRecordHandler;
import com.philips.cdp.registration.hsdp.HsdpUser;
import com.philips.cdp.registration.settings.RegistrationHelper;
import com.philips.cdp.registration.settings.UserRegistrationInitializer;
import com.philips.cdp.registration.ui.utils.FieldsValidator;
import com.philips.cdp.registration.ui.utils.RegConstants;
import com.philips.cdp.registration.ui.utils.ThreadUtils;

import org.json.JSONObject;

public class LoginSocialProvider implements Jump.SignInResultHandler, Jump.SignInCodeHandler, JumpFlowDownloadStatusListener {

    private Context mContext;

    private SocialProviderLoginHandler mSocialLoginHandler;

    private String mMergeToken;

    private UpdateUserRecordHandler mUpdateUserRecordHandler;

    public LoginSocialProvider(SocialProviderLoginHandler socialLoginHandler, Context context,
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
                            ThreadUtils.postInMainThread(mContext,()->
                            mSocialLoginHandler.onLoginSuccess());
                        }

                        @Override
                        public void onLoginFailedWithError(UserRegistrationFailureInfo userRegistrationFailureInfo) {
                            AppTaggingErrors.trackActionLoginError(userRegistrationFailureInfo, AppTagingConstants.HSDP);
                            ThreadUtils.postInMainThread(mContext,()->
                            mSocialLoginHandler.onLoginFailedWithError(userRegistrationFailureInfo));
                        }
                    });

        } else {
            ThreadUtils.postInMainThread(mContext,()->
            mSocialLoginHandler.onLoginSuccess());
        }

    }

    @Override
    public void onCode(String code) {

    }

    @Override
    public void onFailure(SignInError error) {
        UserRegistrationFailureInfo userRegistrationFailureInfo = new UserRegistrationFailureInfo();
        if (error.reason == SignInError.FailureReason.CAPTURE_API_ERROR
                && error.captureApiError.isMergeFlowError()) {
            String emailId = null;
            if (null != error.auth_info) {
                JRDictionary profile = error.auth_info.getAsDictionary("profile");
                if (profile != null) {
                    emailId = profile.getAsString("email");
                }
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
            ThreadUtils.postInMainThread(mContext,()->
            mSocialLoginHandler.onLoginFailedWithMergeFlowError(mMergeToken, existingProvider,
                    conflictingIdentityProvider, conflictingIdpNameLocalized,
                    existingIdpNameLocalized, finalEmailId));
            userRegistrationFailureInfo.setErrorDescription(error.captureApiError.error_description);
            userRegistrationFailureInfo.setErrorCode(error.captureApiError.code);
        } else if (error.reason == SignInError.FailureReason.CAPTURE_API_ERROR
                && error.captureApiError.isTwoStepRegFlowError()) {

            JSONObject prefilledRecord = error.captureApiError.getPreregistrationRecord();
            String socialRegistrationToken = error.captureApiError.getSocialRegistrationToken();
            ThreadUtils.postInMainThread(mContext,()->
            mSocialLoginHandler.onLoginFailedWithTwoStepError(prefilledRecord,
                    socialRegistrationToken));
            userRegistrationFailureInfo.setErrorDescription(error.captureApiError.error_description);
            userRegistrationFailureInfo.setErrorCode(error.captureApiError.code);
        } else {
            userRegistrationFailureInfo.setErrorCode(RegConstants.DI_PROFILE_NULL_ERROR_CODE);
            ThreadUtils.postInMainThread(mContext,()->
            mSocialLoginHandler.onLoginFailedWithError(userRegistrationFailureInfo));
        }
        AppTaggingErrors.trackActionLoginError(userRegistrationFailureInfo, AppTagingConstants.JANRAIN);
    }

    private Activity mActivity;
    private String mProviderName;

    public void loginSocial(final Activity activity, final String providerName, final String mergeToken) {
        mActivity = activity;
        mProviderName = providerName;
        mMergeToken = mergeToken;
        if (!UserRegistrationInitializer.getInstance().isJumpInitializated()) {
            UserRegistrationInitializer.getInstance().registerJumpFlowDownloadListener(this);
        } else {
            Jump.showSignInDialog(activity, providerName, this, mergeToken);
            return;
        }
        if (!UserRegistrationInitializer.getInstance().isRegInitializationInProgress()) {
            RegistrationHelper.getInstance().initializeUserRegistration(mContext);
        }
    }

    @Override
    public void onFlowDownloadSuccess() {
        Jump.showSignInDialog(mActivity, mProviderName, this, mMergeToken);
        UserRegistrationInitializer.getInstance().unregisterJumpFlowDownloadListener();
    }

    @Override
    public void onFlowDownloadFailure() {
        if (mSocialLoginHandler != null) {
            UserRegistrationFailureInfo userRegistrationFailureInfo = new UserRegistrationFailureInfo();
            userRegistrationFailureInfo.setErrorDescription(mContext.getString(R.string.reg_JanRain_Server_Connection_Failed));
            userRegistrationFailureInfo.setErrorCode(RegConstants.SOCIAL_LOGIN_FAILED_SERVER_ERROR);
            ThreadUtils.postInMainThread(mContext,()->
                    mSocialLoginHandler.onLoginFailedWithError(userRegistrationFailureInfo));
        }
        UserRegistrationInitializer.getInstance().unregisterJumpFlowDownloadListener();
    }
}
