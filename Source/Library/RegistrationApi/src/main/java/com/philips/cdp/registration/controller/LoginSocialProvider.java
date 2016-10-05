
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
import com.philips.cdp.registration.User;
import com.philips.cdp.registration.configuration.RegistrationConfiguration;
import com.philips.cdp.registration.dao.UserRegistrationFailureInfo;
import com.philips.cdp.registration.events.JumpFlowDownloadStatusListener;
import com.philips.cdp.registration.handlers.SocialLoginHandler;
import com.philips.cdp.registration.handlers.SocialProviderLoginHandler;
import com.philips.cdp.registration.handlers.UpdateUserRecordHandler;
import com.philips.cdp.registration.hsdp.HsdpUser;
import com.philips.cdp.registration.settings.RegistrationHelper;
import com.philips.cdp.registration.settings.UserRegistrationInitializer;
import com.philips.cdp.registration.ui.utils.RegConstants;

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
        if (RegistrationConfiguration.getInstance().isHsdpFlow() && user.getEmailVerificationStatus()) {
            HsdpUser hsdpUser = new HsdpUser(mContext);
            hsdpUser.socialLogin(user.getEmail(), user.getAccessToken(), new SocialLoginHandler() {

                @Override
                public void onLoginSuccess() {
                    mSocialLoginHandler.onLoginSuccess();
                }

                @Override
                public void onLoginFailedWithError(UserRegistrationFailureInfo userRegistrationFailureInfo) {
                    mSocialLoginHandler.onLoginFailedWithError(userRegistrationFailureInfo);
                }
            });

        } else {
            mSocialLoginHandler.onLoginSuccess();
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
            mSocialLoginHandler.onLoginFailedWithMergeFlowError(mMergeToken, existingProvider,
                    conflictingIdentityProvider, conflictingIdpNameLocalized,
                    existingIdpNameLocalized, emailId);
        } else if (error.reason == SignInError.FailureReason.CAPTURE_API_ERROR
                && error.captureApiError.isTwoStepRegFlowError()) {

            JSONObject prefilledRecord = error.captureApiError.getPreregistrationRecord();
            String socialRegistrationToken = error.captureApiError.getSocialRegistrationToken();
            mSocialLoginHandler.onLoginFailedWithTwoStepError(prefilledRecord,
                    socialRegistrationToken);

        } else {

            UserRegistrationFailureInfo userRegistrationFailureInfo = new UserRegistrationFailureInfo();
            userRegistrationFailureInfo.setErrorCode(RegConstants.DI_PROFILE_NULL_ERROR_CODE);
            mSocialLoginHandler.onLoginFailedWithError(userRegistrationFailureInfo);

        }
    }

    private Activity mActivity;
    private String mProviderName;

    public void loginSocial(final Activity activity, final String providerName, final String mergeToken) {
        mActivity = activity;
        mProviderName = providerName;
        mMergeToken = mergeToken;
        if(!UserRegistrationInitializer.getInstance().isJumpInitializated()) {
            UserRegistrationInitializer.getInstance().registerJumpFlowDownloadListener(this);
        }else{
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

    }
}
