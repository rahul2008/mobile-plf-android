
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
import com.philips.cdp.registration.app.tagging.AppTagingConstants;
import com.philips.cdp.registration.configuration.RegistrationConfiguration;
import com.philips.cdp.registration.dao.UserRegistrationFailureInfo;
import com.philips.cdp.registration.errors.ErrorCodes;
import com.philips.cdp.registration.errors.ErrorType;
import com.philips.cdp.registration.errors.URError;
import com.philips.cdp.registration.events.JumpFlowDownloadStatusListener;
import com.philips.cdp.registration.handlers.SocialLoginProviderHandler;
import com.philips.cdp.registration.handlers.UpdateUserRecordHandler;
import com.philips.cdp.registration.settings.RegistrationHelper;
import com.philips.cdp.registration.settings.UserRegistrationInitializer;
import com.philips.cdp.registration.ui.utils.RLog;
import com.philips.cdp.registration.ui.utils.ThreadUtils;

import org.json.JSONObject;

public class LoginSocialNativeProvider extends HSDPLoginService implements Jump.SignInResultHandler,
        JumpFlowDownloadStatusListener {
    private final static String TAG = "LoginSocialNativeProvider";
    private Context mContext;
    private SocialLoginProviderHandler mSocialLoginProviderHandler;
    private UpdateUserRecordHandler mUpdateUserRecordHandler;

    public LoginSocialNativeProvider(SocialLoginProviderHandler socialLoginProviderHandler, Context context,
                                     UpdateUserRecordHandler updateUserRecordHandler) {
        super(context);
        mSocialLoginProviderHandler = socialLoginProviderHandler;
        mContext = context;
        mUpdateUserRecordHandler = updateUserRecordHandler;
    }

    @Override
    public void onSuccess() {
        RLog.d(TAG, "onSuccess : is called");
        Jump.saveToDisk(mContext);
        User user = new User(mContext);
        mUpdateUserRecordHandler.updateUserRecordLogin();
        if (!RegistrationConfiguration.getInstance().isHSDPSkipLoginConfigurationAvailable() && RegistrationConfiguration.getInstance().isHsdpFlow() &&
                (user.isEmailVerified() || user.isMobileVerified())) {
            String emailorMobile = getUserEmailOrMobile(user);
            RLog.d(TAG, "onSuccess : from LoginSocialNativeProvider is called");
            hsdpLogin(user.getAccessToken(), emailorMobile, mSocialLoginProviderHandler);
        } else {
            ThreadUtils.postInMainThread(mContext, () -> mSocialLoginProviderHandler.onLoginSuccess());
        }

    }


    @Override
    public void onFailure(SignInError error) {
        try {
            if (error.reason == SignInError.FailureReason.CAPTURE_API_ERROR
                    && error.captureApiError.isMergeFlowError()) {
                RLog.e(TAG, "onFailure : is called error: " + error.captureApiError.raw_response);

                RLog.d(TAG, "onFailure : isMergeFlowError");
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
                ThreadUtils.postInMainThread(mContext, () -> mSocialLoginProviderHandler.onLoginFailedWithMergeFlowError(mMergeToken, existingProvider,
                        conflictingIdentityProvider, conflictingIdpNameLocalized,
                        existingIdpNameLocalized, finalEmailId));

            } else if (error.reason == SignInError.FailureReason.CAPTURE_API_ERROR
                    && error.captureApiError.isTwoStepRegFlowError()) {
                RLog.e(TAG, "onFailure : isTwoStepRegFlowError"+ error.captureApiError.raw_response);
                JSONObject prefilledRecord = error.captureApiError.getPreregistrationRecord();
                String socialRegistrationToken = error.captureApiError.getSocialRegistrationToken();
                ThreadUtils.postInMainThread(mContext, () -> mSocialLoginProviderHandler.onLoginFailedWithTwoStepError(prefilledRecord,
                        socialRegistrationToken));
            } else if (error.reason == SignInError.FailureReason.AUTHENTICATION_CANCELLED_BY_USER) {
                UserRegistrationFailureInfo userRegistrationFailureInfo = new UserRegistrationFailureInfo(mContext);
                userRegistrationFailureInfo.setErrorCode(ErrorCodes.AUTHENTICATION_CANCELLED_BY_USER);
                ThreadUtils.postInMainThread(mContext, () ->
                        mSocialLoginProviderHandler.onLoginFailedWithError(userRegistrationFailureInfo));
                //   AUTHENTICATION_CANCELLED_BY_USER
                RLog.e(TAG, "onFailure : loginSocial : is cancelled" + error.reason);

            }else {
                RLog.e(TAG, "onFailure : else is called");
                loginFailed();
            }
        } catch (Exception e) {
            RLog.e(TAG, "onFailure : is called exception" + e.getMessage());
            loginFailed();
        }
    }

    private void loginFailed() {
        UserRegistrationFailureInfo userRegistrationFailureInfo = new UserRegistrationFailureInfo(mContext);
        userRegistrationFailureInfo.setErrorCode(ErrorCodes.UNKNOWN_ERROR);
        ThreadUtils.postInMainThread(mContext, () ->
                mSocialLoginProviderHandler.onLoginFailedWithError(userRegistrationFailureInfo));
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
            RLog.d(TAG, "loginSocial : not isJumpInitializated");
            UserRegistrationInitializer.getInstance().registerJumpFlowDownloadListener(this);
        } else {
            Jump.startTokenAuthForNativeProvider(mActivity,
                    mProviderName, mAccessToken, mTokenSecret, this, mMergeToken);
            RLog.d(TAG, "loginSocial : true isJumpInitializated");
            return;
        }
        if (!UserRegistrationInitializer.getInstance().isRegInitializationInProgress()) {
            RegistrationHelper.getInstance().initializeUserRegistration(mContext);
            RLog.d(TAG, "loginSocial : not isRegInitializationInProgress");
        }
    }

    @Override
    public void onFlowDownloadSuccess() {
        RLog.d(TAG, "onFlowDownloadSuccess : is called");
        Jump.startTokenAuthForNativeProvider(mActivity,
                mProviderName, mAccessToken, mTokenSecret, this, mMergeToken);
        UserRegistrationInitializer.getInstance().unregisterJumpFlowDownloadListener();
    }

    @Override
    public void onFlowDownloadFailure() {
        RLog.d(TAG, "onFlowDownloadFailure : is called");
        if (mSocialLoginProviderHandler != null) {
            UserRegistrationFailureInfo userRegistrationFailureInfo = new UserRegistrationFailureInfo(mContext);
            userRegistrationFailureInfo.setErrorDescription(new URError(mContext).getLocalizedError(ErrorType.JANRAIN, ErrorCodes.SOCIAL_LOGIN_FAILED_SERVER_ERROR));
            userRegistrationFailureInfo.setErrorTagging(AppTagingConstants.REG_JAN_RAIN_SERVER_CONNECTION_FAILED);
            userRegistrationFailureInfo.setErrorCode(ErrorCodes.SOCIAL_LOGIN_FAILED_SERVER_ERROR);
            ThreadUtils.postInMainThread(mContext, () ->
                    mSocialLoginProviderHandler.onLoginFailedWithError(userRegistrationFailureInfo));
        }
        UserRegistrationInitializer.getInstance().unregisterJumpFlowDownloadListener();

    }
}
