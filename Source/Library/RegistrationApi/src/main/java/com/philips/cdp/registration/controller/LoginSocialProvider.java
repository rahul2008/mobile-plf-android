
package com.philips.cdp.registration.controller;

import android.content.Context;

import com.janrain.android.Jump;
import com.janrain.android.engage.session.JRProvider;
import com.janrain.android.engage.types.JRDictionary;
import com.philips.cdp.registration.User;
import com.philips.cdp.registration.configuration.RegistrationConfiguration;
import com.philips.cdp.registration.coppa.CoppaConfiguration;
import com.philips.cdp.registration.coppa.CoppaExtension;
import com.philips.cdp.registration.dao.DIUserProfile;
import com.philips.cdp.registration.dao.UserRegistrationFailureInfo;
import com.philips.cdp.registration.handlers.SocialLoginHandler;
import com.philips.cdp.registration.handlers.SocialProviderLoginHandler;
import com.philips.cdp.registration.handlers.UpdateUserRecordHandler;
import com.philips.cdp.registration.hsdp.HsdpUser;
import com.philips.cdp.registration.settings.RegistrationHelper;
import com.philips.cdp.registration.ui.utils.RegConstants;

import org.json.JSONObject;

public class LoginSocialProvider implements Jump.SignInResultHandler, Jump.SignInCodeHandler {

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
        DIUserProfile userProfile = user.getUserInstance(mContext);
        user.buildCoppaConfiguration();

        if (CoppaConfiguration.getCoppaCommunicationSentAt() != null && RegistrationConfiguration.getInstance().isCoppaFlow()) {
            CoppaExtension coppaExtension = new CoppaExtension();
            coppaExtension.triggerSendCoppaMailAfterLogin(user.getUserInstance(mContext).getEmail());
        }

        if (RegistrationConfiguration.getInstance().getHsdpConfiguration().isHsdpFlow() && user.getEmailVerificationStatus(mContext)) {
            HsdpUser hsdpUser = new HsdpUser(mContext);
            hsdpUser.socialLogin(userProfile.getEmail(), user.getAccessToken(), new SocialLoginHandler() {

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
        mUpdateUserRecordHandler.updateUserRecordLogin();
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
}
