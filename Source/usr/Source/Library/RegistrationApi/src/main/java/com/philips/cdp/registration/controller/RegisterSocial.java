
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
import com.janrain.android.capture.CaptureRecord;
import com.philips.cdp.registration.User;
import com.philips.cdp.registration.app.tagging.AppTaggingErrors;
import com.philips.cdp.registration.app.tagging.AppTagingConstants;
import com.philips.cdp.registration.configuration.RegistrationConfiguration;
import com.philips.cdp.registration.dao.DIUserProfile;
import com.philips.cdp.registration.dao.UserRegistrationFailureInfo;
import com.philips.cdp.registration.errors.ErrorCodes;
import com.philips.cdp.registration.errors.ErrorType;
import com.philips.cdp.registration.errors.URError;
import com.philips.cdp.registration.events.JumpFlowDownloadStatusListener;
import com.philips.cdp.registration.handlers.SocialLoginProviderHandler;
import com.philips.cdp.registration.handlers.UpdateUserRecordHandler;
import com.philips.cdp.registration.settings.RegistrationHelper;
import com.philips.cdp.registration.settings.UserRegistrationInitializer;
import com.philips.cdp.registration.ui.utils.FieldsValidator;
import com.philips.cdp.registration.ui.utils.RLog;
import com.philips.cdp.registration.ui.utils.ThreadUtils;

import org.json.JSONException;
import org.json.JSONObject;

public class RegisterSocial implements SocialLoginProviderHandler, Jump.SignInResultHandler,
        JumpFlowDownloadStatusListener {

    private String TAG = "RegisterSocial";
    private SocialLoginProviderHandler mSocialLoginProviderHandler;

    private Context mContext;

    private HSDPLoginService hsdpLoginService;
    private UpdateUserRecordHandler mUpdateUserRecordHandler;

    public RegisterSocial(SocialLoginProviderHandler socialLoginProviderHandler,
                          Context context, UpdateUserRecordHandler updateUserRecordHandler) {
        mSocialLoginProviderHandler = socialLoginProviderHandler;
        mContext = context;
        mUpdateUserRecordHandler = updateUserRecordHandler;
        hsdpLoginService = new HSDPLoginService(mContext);
    }

    public void onSuccess() {
        RLog.d(TAG, "onSuccess is called");
        Jump.saveToDisk(mContext);
        User user = new User(mContext);
        mUpdateUserRecordHandler.updateUserRecordRegister();

        if (!RegistrationConfiguration.getInstance().isHSDPSkipLoginConfigurationAvailable() && RegistrationConfiguration.getInstance().isHsdpFlow() && (user.isEmailVerified() || user.isMobileVerified())) {
            RLog.d(TAG, "onSuccess : if : is called");
            String emailOrMobile = hsdpLoginService.getUserEmailOrMobile(user);
            hsdpLoginService.hsdpLogin(user.getAccessToken(), emailOrMobile, mSocialLoginProviderHandler);
        } else {
            ThreadUtils.postInMainThread(mContext, () ->
                    mSocialLoginProviderHandler.onContinueSocialProviderLoginSuccess());

            RLog.d(TAG, "onSuccess : else : is called");
        }
    }

    public void onFailure(SignInError error) {
        try{
        RLog.d(TAG, "onFailure : is called error: "+ error.captureApiError.raw_response);
        UserRegistrationFailureInfo userRegistrationFailureInfo = new UserRegistrationFailureInfo(error.captureApiError, mContext);
        userRegistrationFailureInfo.setErrorCode(error.captureApiError.code);
        AppTaggingErrors.trackActionRegisterError(userRegistrationFailureInfo, AppTagingConstants.JANRAIN);
        ThreadUtils.postInMainThread(mContext, () ->
                mSocialLoginProviderHandler.onContinueSocialProviderLoginFailure(userRegistrationFailureInfo));
    }catch(Exception e) {
            RLog.d(TAG, "onFailure : is called : Exception : " + e.getMessage());
            UserRegistrationFailureInfo userRegistrationFailureInfo = new UserRegistrationFailureInfo(mContext);
            userRegistrationFailureInfo.setErrorCode(ErrorCodes.UNKNOWN_ERROR);
            ThreadUtils.postInMainThread(mContext, () ->
                    mSocialLoginProviderHandler.onContinueSocialProviderLoginFailure(userRegistrationFailureInfo));
        }
    }

    private JSONObject mUser;
    private String mUserRegistrationToken;

    private void registerNewUser(final JSONObject user, final String userRegistrationToken) {
        RLog.d(TAG, "registerNewUser : is called");
        mUser = user;
        mUserRegistrationToken = userRegistrationToken;
        if (!UserRegistrationInitializer.getInstance().isJumpInitializated()) {
            UserRegistrationInitializer.getInstance().registerJumpFlowDownloadListener(this);
            RLog.d(TAG, "registerNewUser : jump is not initialized");
        } else {
            Jump.registerNewUser(user, userRegistrationToken, this);
            RLog.d(TAG, "registerNewUser : register new User");
            return;

        }
        if (!UserRegistrationInitializer.getInstance().isRegInitializationInProgress()) {
            RegistrationHelper.getInstance().initializeUserRegistration(mContext);
            RLog.d(TAG, "registerNewUser : registartion is not in progress");
        }

    }

    @Override
    public void onFlowDownloadSuccess() {
        RLog.d(TAG, "onFlowDownloadSuccess : is called");
        Jump.registerNewUser(mUser, mUserRegistrationToken, this);
        UserRegistrationInitializer.getInstance().unregisterJumpFlowDownloadListener();

    }

    @Override
    public void onFlowDownloadFailure() {
        RLog.d(TAG, "onFlowDownloadFailure : is called");
        if (mSocialLoginProviderHandler != null) {
            RLog.d(TAG, "onFlowDownloadFailure : mSocialProviderLoginHandler is not null");
            UserRegistrationFailureInfo userRegistrationFailureInfo = new UserRegistrationFailureInfo(mContext);
            userRegistrationFailureInfo.setErrorDescription(new URError(mContext).getLocalizedError(ErrorType.JANRAIN, ErrorCodes.JANRAIN_FLOW_DOWNLOAD_ERROR));
            userRegistrationFailureInfo.setErrorTagging(AppTagingConstants.REG_JAN_RAIN_SERVER_CONNECTION_FAILED);
            userRegistrationFailureInfo.setErrorCode(ErrorCodes.JANRAIN_FLOW_DOWNLOAD_ERROR);
            ThreadUtils.postInMainThread(mContext, () ->
                    mSocialLoginProviderHandler.onLoginFailedWithError(userRegistrationFailureInfo));
        }
        UserRegistrationInitializer.getInstance().unregisterJumpFlowDownloadListener();
    }


    public void registerUserForSocial(final String givenName, final String displayName, final String familyName,
                                      final String userEmail, final boolean olderThanAgeLimit, final boolean isReceiveMarketingEmail,
                                      final String socialRegistrationToken) {
        RLog.d(TAG, "registerUserForSocial : is called");
        DIUserProfile profile = new DIUserProfile();
        profile.setGivenName(givenName);
        profile.setDisplayName(displayName);
        profile.setFamilyName(familyName);
        if (FieldsValidator.isValidEmail(userEmail)) {
            profile.setEmail(userEmail);

        } else {
            // check mobile umber validation
            profile.setMobile(userEmail);

        }
        profile.setOlderThanAgeLimit(olderThanAgeLimit);
        profile.setReceiveMarketingEmail(isReceiveMarketingEmail);
        completeSocialProviderLogin(profile, this, socialRegistrationToken);
    }


    private void completeSocialProviderLogin(DIUserProfile diUserProfile,
                                             SocialLoginProviderHandler socialLoginProviderHandler, String socialRegistrationToken) {
        RLog.d(TAG, "completeSocialProviderLogin : is called");
        String familyName = "";
        if (diUserProfile != null) {
            familyName = diUserProfile.getFamilyName();
            JSONObject newUser = new JSONObject();
            try {
                newUser.put("email", diUserProfile.getEmail()).put("mobileNumber", diUserProfile.getMobile()).put("givenName", diUserProfile.getGivenName())
                        .put("familyName", familyName).put("password", diUserProfile.getPassword())
                        .put("displayName", diUserProfile.getDisplayName())
                        .put("olderThanAgeLimit", diUserProfile.getOlderThanAgeLimit())
                        .put("receiveMarketingEmail", diUserProfile.getReceiveMarketingEmail());

            } catch (JSONException e) {
            }

            registerNewUser(newUser, socialRegistrationToken);
        } else {
            UserRegistrationFailureInfo userRegistrationFailureInfo = new UserRegistrationFailureInfo(mContext);
            userRegistrationFailureInfo.setErrorCode(ErrorCodes.NETWORK_ERROR);
            userRegistrationFailureInfo.setErrorDescription(AppTagingConstants.NETWORK_ERROR);
            userRegistrationFailureInfo.setErrorTagging(AppTagingConstants.NETWORK_ERROR);
            socialLoginProviderHandler.onContinueSocialProviderLoginFailure(userRegistrationFailureInfo);
            AppTaggingErrors.trackActionRegisterError(userRegistrationFailureInfo, AppTagingConstants.JANRAIN);
        }
    }


    @Override
    public void onLoginSuccess() {
        RLog.d(TAG, "onLoginSuccess : is called");
        handleOnLoginSuccess();
    }


    @Override
    public void onLoginFailedWithError(UserRegistrationFailureInfo userRegistrationFailureInfo) {
        RLog.d(TAG, "onLoginFailedWithError : is called");
        ThreadUtils.postInMainThread(mContext, () ->
                mSocialLoginProviderHandler.onLoginFailedWithError(userRegistrationFailureInfo));
    }

    @Override
    public void onLoginFailedWithTwoStepError(JSONObject prefilledRecord, String socialRegistrationToken) {
        RLog.d(TAG, "onLoginFailedWithTwoStepError : is called");
        ThreadUtils.postInMainThread(mContext, () ->
                mSocialLoginProviderHandler.onLoginFailedWithTwoStepError(prefilledRecord, socialRegistrationToken));
    }

    @Override
    public void onLoginFailedWithMergeFlowError(String mergeToken, String existingProvider, String conflictingIdentityProvider, String conflictingIdpNameLocalized, String existingIdpNameLocalized, String emailId) {
        RLog.d(TAG, "onLoginFailedWithMergeFlowError : is called");
        ThreadUtils.postInMainThread(mContext, () ->
                mSocialLoginProviderHandler.onLoginFailedWithMergeFlowError(mergeToken, existingProvider, conflictingIdentityProvider, conflictingIdpNameLocalized, existingIdpNameLocalized, emailId));
    }

    @Override
    public void onContinueSocialProviderLoginSuccess() {
        RLog.d(TAG, "onContinueSocialProviderLoginSuccess : is called");
        ThreadUtils.postInMainThread(mContext, () ->
                mSocialLoginProviderHandler.onContinueSocialProviderLoginSuccess());
    }

    @Override
    public void onContinueSocialProviderLoginFailure(UserRegistrationFailureInfo userRegistrationFailureInfo) {
        RLog.d(TAG, "onContinueSocialProviderLoginFailure : is called");
        AppTaggingErrors.trackActionRegisterError(userRegistrationFailureInfo, AppTagingConstants.JANRAIN);
        ThreadUtils.postInMainThread(mContext, () ->
                mSocialLoginProviderHandler.onContinueSocialProviderLoginFailure(userRegistrationFailureInfo));
    }


    private void handleOnLoginSuccess() {
        RLog.d(TAG, "handleOnLoginSuccess : is called");
        boolean isEmailVerified;
        CaptureRecord captured = Jump.getSignedInUser();
        if (captured == null || (captured != null && captured.isNull("emailVerified"))) {
            isEmailVerified = false;
        } else {
            isEmailVerified = true;
        }
        RLog.d(TAG, "handleOnLoginSuccess : is isEmailVerified" + isEmailVerified);

        if (!RegistrationConfiguration.getInstance().isHSDPSkipLoginConfigurationAvailable() && RegistrationConfiguration.getInstance().isHsdpFlow() && isEmailVerified) {
            RLog.d(TAG, "handleOnLoginSuccess : is hsdpflow  and email verified");
            try {
                hsdpLoginService.hsdpLogin(captured.getAccessToken(), captured.getString("email"), mSocialLoginProviderHandler);
            } catch (JSONException e) {
                e.printStackTrace();
            }

        } else {
            RLog.d(TAG, "handleOnLoginSuccess : either of isHsdpflow or isEmailVerified is false");
            ThreadUtils.postInMainThread(mContext, () ->
                    mSocialLoginProviderHandler.onLoginSuccess());
        }
    }
}
