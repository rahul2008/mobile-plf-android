
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
import com.philips.cdp.registration.app.tagging.AppTaggingErrors;
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

public class LoginSocialProvider extends HSDPLoginService implements Jump.SignInResultHandler, JumpFlowDownloadStatusListener {

    private final static String TAG = "LoginSocialProvider";

    private Context mContext;

    private SocialLoginProviderHandler mSocialLoginProviderHandler;

    private String mMergeToken;

    private UpdateUserRecordHandler mUpdateUserRecordHandler;

    private Activity mActivity;

    private String mProviderName;

    public LoginSocialProvider(SocialLoginProviderHandler socialLoginProviderHandler, Context context,
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
            RLog.d(TAG, "onSuccess : from LoginSocialProvider is called");
            hsdpLogin(user.getAccessToken(), emailorMobile, mSocialLoginProviderHandler);
        } else {
            ThreadUtils.postInMainThread(mContext, mSocialLoginProviderHandler::onLoginSuccess);
        }

    }

    @Override
    public void onFailure(SignInError error) {
        UserRegistrationFailureInfo userRegistrationFailureInfo = new UserRegistrationFailureInfo(mContext);
        try {
            if (error.reason == SignInError.FailureReason.CAPTURE_API_ERROR
                    && error.captureApiError.isMergeFlowError()) {
                RLog.e(TAG, "onFailure : is called error: " + error.captureApiError.raw_response);

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
                ThreadUtils.postInMainThread(mContext, () ->
                        mSocialLoginProviderHandler.onLoginFailedWithMergeFlowError(mMergeToken, existingProvider,
                                conflictingIdentityProvider, conflictingIdpNameLocalized,
                                existingIdpNameLocalized, finalEmailId));
                userRegistrationFailureInfo.setErrorDescription(error.captureApiError.error_description);
                userRegistrationFailureInfo.setErrorCode(error.captureApiError.code);
                RLog.e(TAG, "onFailure : userRegistrationFailureInfo.setErrorCode = " + error.captureApiError.code);
            } else if (error.reason == SignInError.FailureReason.CAPTURE_API_ERROR
                    && error.captureApiError.isTwoStepRegFlowError()) {
                RLog.e(TAG, "onFailure : is called error: " + error.captureApiError.raw_response);


                JSONObject prefilledRecord = error.captureApiError.getPreregistrationRecord();
                String socialRegistrationToken = error.captureApiError.getSocialRegistrationToken();
                ThreadUtils.postInMainThread(mContext, () ->
                        mSocialLoginProviderHandler.onLoginFailedWithTwoStepError(prefilledRecord,
                                socialRegistrationToken));
                userRegistrationFailureInfo.setErrorDescription(error.captureApiError.error_description);
                userRegistrationFailureInfo.setErrorCode(error.captureApiError.code);
                RLog.e(TAG, "onFailure : userRegistrationFailureInfo.setErrorCode = " + error.captureApiError.code);
            } else if (error.reason == SignInError.FailureReason.AUTHENTICATION_CANCELLED_BY_USER) {
                userRegistrationFailureInfo.setErrorCode(ErrorCodes.AUTHENTICATION_CANCELLED_BY_USER);
                ThreadUtils.postInMainThread(mContext, () ->
                        mSocialLoginProviderHandler.onLoginFailedWithError(userRegistrationFailureInfo));
                //   AUTHENTICATION_CANCELLED_BY_USER

                RLog.e(TAG, "onFailure : loginSocial : is cancelled" + error.reason);

            } else {
                userRegistrationFailureInfo.setErrorCode(ErrorCodes.UNKNOWN_ERROR);
                ThreadUtils.postInMainThread(mContext, () ->
                        mSocialLoginProviderHandler.onLoginFailedWithError(userRegistrationFailureInfo));

                RLog.e(TAG, "onFailure : loginSocial : is cancelled" + error.reason);
            }
            AppTaggingErrors.trackActionLoginError(userRegistrationFailureInfo, AppTagingConstants.JANRAIN);
        } catch (Exception e) {
            RLog.e(TAG, "onFailure : is called : Exception : " + e.getMessage());
            userRegistrationFailureInfo.setErrorCode(ErrorCodes.UNKNOWN_ERROR);
            ThreadUtils.postInMainThread(mContext, () ->
                    mSocialLoginProviderHandler.onLoginFailedWithError(userRegistrationFailureInfo));
        }
    }

    public void loginSocial(final Activity activity, final String providerName, final String mergeToken) {
        RLog.d(TAG, "loginSocial : is called");
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

    public void startTokenAuthForNativeProvider(final Activity activity, final String providerName, final String mergeToken, final String accessToken) {
        RLog.d(TAG, "startTokenAuthForNativeProvider : is called");
        mActivity = activity;
        mProviderName = providerName;
        mMergeToken = mergeToken;
        if (!UserRegistrationInitializer.getInstance().isJumpInitializated()) {
            UserRegistrationInitializer.getInstance().registerJumpFlowDownloadListener(this);
        } else {
            Jump.startTokenAuthForNativeProvider(activity, providerName, accessToken, null, this, mergeToken);
            return;
        }
        if (!UserRegistrationInitializer.getInstance().isRegInitializationInProgress()) {
            RegistrationHelper.getInstance().initializeUserRegistration(mContext);
        }
    }


    @Override
    public void onFlowDownloadSuccess() {
        RLog.d(TAG, "onFlowDownloadSuccess : is called");
        Jump.showSignInDialog(mActivity, mProviderName, this, mMergeToken);
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
