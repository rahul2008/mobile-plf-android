
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
import com.philips.cdp.registration.errors.ErrorCodes;
import com.philips.cdp.registration.events.JumpFlowDownloadStatusListener;
import com.philips.cdp.registration.handlers.SocialProviderLoginHandler;
import com.philips.cdp.registration.handlers.UpdateUserRecordHandler;
import com.philips.cdp.registration.settings.RegistrationHelper;
import com.philips.cdp.registration.settings.UserRegistrationInitializer;
import com.philips.cdp.registration.ui.utils.RLog;
import com.philips.cdp.registration.ui.utils.RegConstants;
import com.philips.cdp.registration.ui.utils.ThreadUtils;

import org.json.JSONObject;

public class LoginSocialProvider extends BaseHSDPLogin implements Jump.SignInResultHandler, Jump.SignInCodeHandler, JumpFlowDownloadStatusListener {

    private Context mContext;

    private SocialProviderLoginHandler mSocialLoginHandler;

    private String mMergeToken;

    private UpdateUserRecordHandler mUpdateUserRecordHandler;

    private final static String TAG = LoginSocialProvider.class.getSimpleName();

    public LoginSocialProvider(SocialProviderLoginHandler socialLoginHandler, Context context,
                               UpdateUserRecordHandler updateUserRecordHandler) {
        super(context);
        mSocialLoginHandler = socialLoginHandler;
        mContext = context;
        mUpdateUserRecordHandler = updateUserRecordHandler;
    }

    @Override
    public void onSuccess() {
        RLog.d(TAG, "onSuccess : is called");
        Jump.saveToDisk(mContext);
        User user = new User(mContext);
        mUpdateUserRecordHandler.updateUserRecordLogin();
        if (RegistrationConfiguration.getInstance().isHsdpFlow() &&
                (user.isEmailVerified() || user.isMobileVerified())) {

            String emailorMobile = getUserEmailOrMobile(user);
            hsdpLogin(user.getAccessToken(), emailorMobile, mSocialLoginHandler);
//            HsdpUser hsdpUser = new HsdpUser(mContext);
//
//            String emailorMobile;
//            if (FieldsValidator.isValidEmail(user.getEmail())) {
//                emailorMobile = user.getEmail();
//            } else {
//                emailorMobile = user.getMobile();
//            }
//            hsdpUser.login(emailorMobile, user.getAccessToken(), Jump.getRefreshSecret(),
//                    new SocialLoginHandler() {
//
//                        @Override
//                        public void onLoginSuccess() {
//                            ThreadUtils.postInMainThread(mContext, () ->
//                                    mSocialLoginHandler.onLoginSuccess());
//                        }
//
//                        @Override
//                        public void onLoginFailedWithError(UserRegistrationFailureInfo userRegistrationFailureInfo) {
//                            AppTaggingErrors.trackActionLoginError(userRegistrationFailureInfo, AppTagingConstants.HSDP);
//                            ThreadUtils.postInMainThread(mContext, () ->
//                                    mSocialLoginHandler.onLoginFailedWithError(userRegistrationFailureInfo));
//                        }
//                    });

        } else {
            ThreadUtils.postInMainThread(mContext, () ->
                    mSocialLoginHandler.onLoginSuccess());
        }

    }

    @Override
    public void onCode(String code) {
        RLog.d(TAG, "onCode : is called");
    }

    @Override
    public void onFailure(SignInError error) {
        RLog.d(TAG, "onFailure : is called");
        UserRegistrationFailureInfo userRegistrationFailureInfo = new UserRegistrationFailureInfo(mContext);
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
            ThreadUtils.postInMainThread(mContext, () ->
                    mSocialLoginHandler.onLoginFailedWithMergeFlowError(mMergeToken, existingProvider,
                            conflictingIdentityProvider, conflictingIdpNameLocalized,
                            existingIdpNameLocalized, finalEmailId));
            userRegistrationFailureInfo.setErrorDescription(error.captureApiError.error_description);
            userRegistrationFailureInfo.setErrorCode(error.captureApiError.code);
            RLog.e(TAG, "onFailure : userRegistrationFailureInfo.setErrorCode = " + error.captureApiError.code);
        } else if (error.reason == SignInError.FailureReason.CAPTURE_API_ERROR
                && error.captureApiError.isTwoStepRegFlowError()) {

            JSONObject prefilledRecord = error.captureApiError.getPreregistrationRecord();
            String socialRegistrationToken = error.captureApiError.getSocialRegistrationToken();
            ThreadUtils.postInMainThread(mContext, () ->
                    mSocialLoginHandler.onLoginFailedWithTwoStepError(prefilledRecord,
                            socialRegistrationToken));
            userRegistrationFailureInfo.setErrorDescription(error.captureApiError.error_description);
            userRegistrationFailureInfo.setErrorCode(error.captureApiError.code);
            RLog.e(TAG, "onFailure : userRegistrationFailureInfo.setErrorCode = " + error.captureApiError.code);
        } else if (error.reason == SignInError.FailureReason.AUTHENTICATION_CANCELLED_BY_USER) {
            userRegistrationFailureInfo.setErrorCode(ErrorCodes.AUTHENTICATION_CANCELLED_BY_USER);
            ThreadUtils.postInMainThread(mContext, () ->
                    mSocialLoginHandler.onLoginFailedWithError(userRegistrationFailureInfo));
            //   AUTHENTICATION_CANCELLED_BY_USER

            RLog.d(TAG, "onFailure : loginSocial : is cancelled" + error.reason);

        } else {
            userRegistrationFailureInfo.setErrorCode(ErrorCodes.UNKNOWN_ERROR);
            ThreadUtils.postInMainThread(mContext, () ->
                    mSocialLoginHandler.onLoginFailedWithError(userRegistrationFailureInfo));

            RLog.d(TAG, "onFailure : loginSocial : is cancelled" + error.reason);

        }
        AppTaggingErrors.trackActionLoginError(userRegistrationFailureInfo, AppTagingConstants.JANRAIN);
    }

    private Activity mActivity;
    private String mProviderName;

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
        if (mSocialLoginHandler != null) {
            UserRegistrationFailureInfo userRegistrationFailureInfo = new UserRegistrationFailureInfo(mContext);
            userRegistrationFailureInfo.setErrorDescription(mContext.getString(R.string.USR_Janrain_HSDP_ServerErrorMsg));
            userRegistrationFailureInfo.setErrorTagging(AppTagingConstants.REG_JAN_RAIN_SERVER_CONNECTION_FAILED);
            userRegistrationFailureInfo.setErrorCode(RegConstants.SOCIAL_LOGIN_FAILED_SERVER_ERROR);
            ThreadUtils.postInMainThread(mContext, () ->
                    mSocialLoginHandler.onLoginFailedWithError(userRegistrationFailureInfo));
        }
        UserRegistrationInitializer.getInstance().unregisterJumpFlowDownloadListener();
    }
}
