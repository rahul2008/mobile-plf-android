
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
import com.philips.cdp.registration.R;
import com.philips.cdp.registration.User;
import com.philips.cdp.registration.app.tagging.AppTaggingErrors;
import com.philips.cdp.registration.app.tagging.AppTagingConstants;
import com.philips.cdp.registration.configuration.RegistrationConfiguration;
import com.philips.cdp.registration.dao.DIUserProfile;
import com.philips.cdp.registration.dao.UserRegistrationFailureInfo;
import com.philips.cdp.registration.errors.ErrorCodes;
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

import org.json.JSONException;
import org.json.JSONObject;

public class RegisterSocial implements SocialProviderLoginHandler, Jump.SignInResultHandler,
        Jump.SignInCodeHandler, JumpFlowDownloadStatusListener {

    private String TAG = RegisterSocial.class.getSimpleName();
    private SocialProviderLoginHandler mSocialProviderLoginHandler;

    private Context mContext;

    private UpdateUserRecordHandler mUpdateUserRecordHandler;

    public RegisterSocial(SocialProviderLoginHandler socialProviderLoginHandler,
                          Context context, UpdateUserRecordHandler updateUserRecordHandler) {
        mSocialProviderLoginHandler = socialProviderLoginHandler;
        mContext = context;
        mUpdateUserRecordHandler = updateUserRecordHandler;
    }

    public void onSuccess() {
        RLog.d(TAG, "onSuccess is called");
        Jump.saveToDisk(mContext);
        User user = new User(mContext);
        mUpdateUserRecordHandler.updateUserRecordRegister();

        if (RegistrationConfiguration.getInstance().isHsdpFlow() && (user.isEmailVerified() || user.isMobileVerified())) {
            RLog.d(TAG, "onSuccess : if : is called");
            HsdpUser hsdpUser = new HsdpUser(mContext);
            String emailOrMobile;
            if (FieldsValidator.isValidEmail(user.getEmail())) {
                emailOrMobile = user.getEmail();
                RLog.d(TAG, "onSuccess : if : if : is valid email");
            } else {
                emailOrMobile = user.getMobile();
                RLog.d(TAG, "onSuccess : if : else : is not valid email");
            }
            hsdpUser.login(emailOrMobile, user.getAccessToken(), Jump.getRefreshSecret(), new SocialLoginHandler() {

                @Override
                public void onLoginSuccess() {
                    ThreadUtils.postInMainThread(mContext, () ->
                            mSocialProviderLoginHandler.onContinueSocialProviderLoginSuccess());

                    RLog.d(TAG, "onSuccess : if : SocialLoginHandler : onLoginSuccess : is called");
                }

                @Override
                public void onLoginFailedWithError(UserRegistrationFailureInfo userRegistrationFailureInfo) {
                    AppTaggingErrors.trackActionRegisterError(userRegistrationFailureInfo, AppTagingConstants.HSDP);
                    ThreadUtils.postInMainThread(mContext, () ->
                            mSocialProviderLoginHandler.onLoginFailedWithError(userRegistrationFailureInfo));
                    RLog.d(TAG, "onSuccess : if : SocialLoginHandler : onLoginFailedWithError : is called");
                }
            });
        } else {
            ThreadUtils.postInMainThread(mContext, () ->
                    mSocialProviderLoginHandler.onContinueSocialProviderLoginSuccess());

            RLog.d(TAG, "onSuccess : else : is called");
        }
    }

    public void onCode(String code) {
        RLog.d(TAG, "onCode : is called");
    }

    public void onFailure(SignInError error) {
        RLog.d(TAG, "onFailure : is called");
        UserRegistrationFailureInfo userRegistrationFailureInfo = new UserRegistrationFailureInfo(error.captureApiError, mContext);
        userRegistrationFailureInfo.setErrorCode(error.captureApiError.code);
        AppTaggingErrors.trackActionRegisterError(userRegistrationFailureInfo, AppTagingConstants.JANRAIN);
        ThreadUtils.postInMainThread(mContext, () ->
                mSocialProviderLoginHandler.onContinueSocialProviderLoginFailure(userRegistrationFailureInfo));
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
        if (mSocialProviderLoginHandler != null) {
            RLog.d(TAG, "onFlowDownloadFailure : mSocialProviderLoginHandler is not null");
            UserRegistrationFailureInfo userRegistrationFailureInfo = new UserRegistrationFailureInfo(mContext);
            userRegistrationFailureInfo.setErrorDescription(mContext.getString(R.string.USR_Janrain_HSDP_ServerErrorMsg));
            userRegistrationFailureInfo.setErrorTagging(AppTagingConstants.REG_JAN_RAIN_SERVER_CONNECTION_FAILED);
            userRegistrationFailureInfo.setErrorCode(RegConstants.JANRAIN_FLOW_DOWNLOAD_ERROR);
            ThreadUtils.postInMainThread(mContext, () ->
                    mSocialProviderLoginHandler.onLoginFailedWithError(userRegistrationFailureInfo));
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
                                             SocialProviderLoginHandler socialProviderLoginHandler, String socialRegistrationToken) {
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
            socialProviderLoginHandler
                    .onContinueSocialProviderLoginFailure(userRegistrationFailureInfo);
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
                mSocialProviderLoginHandler.onLoginFailedWithError(userRegistrationFailureInfo));
    }

    @Override
    public void onLoginFailedWithTwoStepError(JSONObject prefilledRecord, String socialRegistrationToken) {
        RLog.d(TAG, "onLoginFailedWithTwoStepError : is called");
        ThreadUtils.postInMainThread(mContext, () ->
                mSocialProviderLoginHandler.onLoginFailedWithTwoStepError(prefilledRecord, socialRegistrationToken));
    }

    @Override
    public void onLoginFailedWithMergeFlowError(String mergeToken, String existingProvider, String conflictingIdentityProvider, String conflictingIdpNameLocalized, String existingIdpNameLocalized, String emailId) {
        RLog.d(TAG, "onLoginFailedWithMergeFlowError : is called");
        ThreadUtils.postInMainThread(mContext, () ->
                mSocialProviderLoginHandler.onLoginFailedWithMergeFlowError(mergeToken, existingProvider, conflictingIdentityProvider, conflictingIdpNameLocalized, existingIdpNameLocalized, emailId));
    }

    @Override
    public void onContinueSocialProviderLoginSuccess() {
        RLog.d(TAG, "onContinueSocialProviderLoginSuccess : is called");
        ThreadUtils.postInMainThread(mContext, () ->
                mSocialProviderLoginHandler.onContinueSocialProviderLoginSuccess());
    }

    @Override
    public void onContinueSocialProviderLoginFailure(UserRegistrationFailureInfo userRegistrationFailureInfo) {
        RLog.d(TAG, "onContinueSocialProviderLoginFailure : is called");
        AppTaggingErrors.trackActionRegisterError(userRegistrationFailureInfo, AppTagingConstants.JANRAIN);
        ThreadUtils.postInMainThread(mContext, () ->
                mSocialProviderLoginHandler.onContinueSocialProviderLoginFailure(userRegistrationFailureInfo));
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

        if (RegistrationConfiguration.getInstance().isHsdpFlow() && isEmailVerified) {
            RLog.d(TAG, "handleOnLoginSuccess : is hsdpflow  and email verified");
            HsdpUser hsdpUser = new HsdpUser(mContext);
            try {
                hsdpUser.login(captured.getString("email"), captured.getAccessToken(), Jump.getRefreshSecret(), new SocialLoginHandler() {

                    @Override
                    public void onLoginSuccess() {
                        ThreadUtils.postInMainThread(mContext, () ->
                                mSocialProviderLoginHandler.onLoginSuccess());
                    }

                    @Override
                    public void onLoginFailedWithError(UserRegistrationFailureInfo userRegistrationFailureInfo) {
                        AppTaggingErrors.trackActionRegisterError(userRegistrationFailureInfo, AppTagingConstants.HSDP);
                        ThreadUtils.postInMainThread(mContext, () ->
                                mSocialProviderLoginHandler.onLoginFailedWithError(userRegistrationFailureInfo));
                    }
                });
            } catch (JSONException e) {
                e.printStackTrace();
            }

        } else {
            RLog.d(TAG, "handleOnLoginSuccess : either of isHsdpflow or isEmailVerified is false");
            ThreadUtils.postInMainThread(mContext, () ->
                    mSocialProviderLoginHandler.onLoginSuccess());
        }
    }
}
