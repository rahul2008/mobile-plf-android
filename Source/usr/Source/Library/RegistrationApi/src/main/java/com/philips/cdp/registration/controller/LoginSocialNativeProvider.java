
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
import com.philips.cdp.registration.ui.utils.RLog;
import com.philips.cdp.registration.ui.utils.RegConstants;
import com.philips.cdp.registration.ui.utils.ThreadUtils;

import org.json.JSONObject;

public class LoginSocialNativeProvider implements Jump.SignInResultHandler, Jump.SignInCodeHandler,
        JumpFlowDownloadStatusListener {
    private final static String TAG = LoginSocialNativeProvider.class.getSimpleName();
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
        RLog.d(TAG,"onSuccess : is called");
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
            hsdpUser.login(emailorMobile, user.getAccessToken(), Jump.getRefreshSecret(),
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
        RLog.d(TAG,"onCode : is called");
    }

    @Override
    public void onFailure(SignInError error) {
        RLog.d(TAG,"onFailure : is called");
        if (error.reason == SignInError.FailureReason.CAPTURE_API_ERROR
                && error.captureApiError.isMergeFlowError()) {

            RLog.d(TAG,"onFailure : isMergeFlowError");
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
            RLog.d(TAG,"onFailure : isTwoStepRegFlowError");
            JSONObject prefilledRecord = error.captureApiError.getPreregistrationRecord();
            String socialRegistrationToken = error.captureApiError.getSocialRegistrationToken();
            ThreadUtils.postInMainThread(mContext, () -> mSocialLoginHandler.onLoginFailedWithTwoStepError(prefilledRecord,
                    socialRegistrationToken));

        } else {
            RLog.d(TAG,"onFailure : else is called");
//            UserRegistrationFailureInfo userRegistrationFailureInfo = new UserRegistrationFailureInfo(mContext);
//            userRegistrationFailureInfo.setErrorDescription();
//            userRegistrationFailureInfo.setErrorCode(ErrorCodes.NETWORK_ERROR);
//            ThreadUtils.postInMainThread(mContext, () -> mSocialLoginHandler.onLoginFailedWithError(userRegistrationFailureInfo));

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
            RLog.d(TAG,"loginSocial : not isJumpInitializated");
            UserRegistrationInitializer.getInstance().registerJumpFlowDownloadListener(this);
        } else {
            Jump.startTokenAuthForNativeProvider(mActivity,
                    mProviderName, mAccessToken, mTokenSecret, this, mMergeToken);
            RLog.d(TAG,"loginSocial : true isJumpInitializated");
            return;
        }
        if (!UserRegistrationInitializer.getInstance().isRegInitializationInProgress()) {
            RegistrationHelper.getInstance().initializeUserRegistration(mContext);
            RLog.d(TAG,"loginSocial : not isRegInitializationInProgress");
        }
    }

    @Override
    public void onFlowDownloadSuccess() {
        RLog.d(TAG,"onFlowDownloadSuccess : is called");
        Jump.startTokenAuthForNativeProvider(mActivity,
                mProviderName, mAccessToken, mTokenSecret, this, mMergeToken);
        UserRegistrationInitializer.getInstance().unregisterJumpFlowDownloadListener();
    }

    @Override
    public void onFlowDownloadFailure() {
        RLog.d(TAG,"onFlowDownloadFailure : is called");
        if (mSocialLoginHandler != null) {
            UserRegistrationFailureInfo userRegistrationFailureInfo = new UserRegistrationFailureInfo(mContext);
            userRegistrationFailureInfo.setErrorDescription(mContext.getString(R.string.USR_JanRain_Server_ConnectionLost_ErrorMsg));
            userRegistrationFailureInfo.setErrorTagging(AppTagingConstants.REG_JAN_RAIN_SERVER_CONNECTION_FAILED);
            userRegistrationFailureInfo.setErrorCode(RegConstants.SOCIAL_LOGIN_FAILED_SERVER_ERROR);
            ThreadUtils.postInMainThread(mContext,()->
                    mSocialLoginHandler.onLoginFailedWithError(userRegistrationFailureInfo));
        }
        UserRegistrationInitializer.getInstance().unregisterJumpFlowDownloadListener();

    }
}
