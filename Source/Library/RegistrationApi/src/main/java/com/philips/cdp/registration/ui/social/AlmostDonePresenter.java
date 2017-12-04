package com.philips.cdp.registration.ui.social;


import android.os.Bundle;

import com.janrain.android.Jump;
import com.philips.cdp.registration.User;
import com.philips.cdp.registration.app.tagging.AppTagging;
import com.philips.cdp.registration.app.tagging.AppTagingConstants;
import com.philips.cdp.registration.configuration.RegistrationConfiguration;
import com.philips.cdp.registration.dao.UserRegistrationFailureInfo;
import com.philips.cdp.registration.events.NetworkStateListener;
import com.philips.cdp.registration.handlers.SocialProviderLoginHandler;
import com.philips.cdp.registration.handlers.UpdateUserDetailsHandler;
import com.philips.cdp.registration.settings.RegistrationHelper;
import com.philips.cdp.registration.ui.utils.FieldsValidator;
import com.philips.cdp.registration.ui.utils.LoginFailureNotification;
import com.philips.cdp.registration.ui.utils.RLog;
import com.philips.cdp.registration.ui.utils.RegConstants;
import com.philips.cdp.registration.ui.utils.URInterface;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import javax.inject.Inject;

import static com.philips.cdp.registration.ui.utils.RegConstants.EMAIL_ADDRESS_ALREADY_USE_CODE;

public class AlmostDonePresenter implements NetworkStateListener, SocialProviderLoginHandler, UpdateUserDetailsHandler {


    @Inject
    User mUser;

    private final AlmostDoneContract almostDoneContract;

    private String mGivenName;

    private String mDisplayName;

    private String mFamilyName;

    private String mEmail;

    private boolean isEmailExist;

    private String mProvider;

    private String mRegistrationToken;

    private Bundle mBundle;

    private boolean isOnline = true;

    public AlmostDonePresenter(AlmostDoneContract almostDoneContract, User user) {
        URInterface.getComponent().inject(this);
        this.mUser = user;
        this.almostDoneContract = almostDoneContract;
        RegistrationHelper.getInstance().registerNetworkStateListener(this);
    }

    public void cleanUp() {
        RegistrationHelper.getInstance().unRegisterNetworkListener(this);
    }

    @Override
    public void onNetWorkStateReceived(boolean isOnline) {
        setOnline(isOnline);
        updateUIControls();
    }

    public void updateUIControls() {
        if (isEmailExist) {
            if (isOnline()) {
                almostDoneContract.enableContinueBtn();
            } else {
                almostDoneContract.handleOfflineMode();
            }
        } else {
            if (isOnline()) {
                almostDoneContract.validateEmailFieldUI();
            } else {
                almostDoneContract.handleOfflineMode();
            }
        }
    }

    public void handleAcceptTermsAndReceiveMarketingOpt() {
        if (RegistrationConfiguration.getInstance().isEmailVerificationRequired()) {
            if (isEmailExist && almostDoneContract.getPreferenceStoredState((mEmail))) {
                almostDoneContract.hideAcceptTermsView();
            } else if (mBundle != null && mBundle.getString(RegConstants.SOCIAL_TWO_STEP_ERROR) != null) {
                almostDoneContract.updateABTestingUIFlow();
            }
        } else {
            almostDoneContract.hideAcceptTermsView();
        }
        updateTermsAndReceiveMarketingOpt();
    }

    public void updateTermsAndReceiveMarketingOpt() {
        if (mUser.isTermsAndConditionAccepted()) {
            almostDoneContract.updateTermsAndConditionView();
        }

        if (!mUser.getReceiveMarketingEmail()) {
            almostDoneContract.showMarketingOptCheck();
        } else {
            almostDoneContract.hideMarketingOptCheck();
        }
    }

    @Override
    public void onLoginSuccess() {
        AppTagging.trackAction(AppTagingConstants.SEND_DATA, AppTagingConstants.SPECIAL_EVENTS,
                AppTagingConstants.SUCCESS_LOGIN);
    }

    @Override
    public void onLoginFailedWithError(UserRegistrationFailureInfo userRegistrationFailureInfo) {
        handleLoginFailed(userRegistrationFailureInfo);
    }

    private void handleLoginFailed(UserRegistrationFailureInfo userRegistrationFailureInfo) {
        almostDoneContract.hideMarketingOptSpinner();
        EventBus.getDefault().post(new LoginFailureNotification());
        if (userRegistrationFailureInfo.getErrorCode() == RegConstants.EMAIL_ADDRESS_ALREADY_USE_CODE) {
            if (RegistrationHelper.getInstance().isMobileFlow()) {
                almostDoneContract.phoneNumberAlreadyInuseError();
            } else {
                almostDoneContract.emailAlreadyInuseError();
            }
            almostDoneContract.showLoginFailedError();
        }

        if(userRegistrationFailureInfo.getErrorCode() == RegConstants.HSDP_ADMINISTRATION_ERROR){
            almostDoneContract.showTryAgainError();
            return;
        }

        almostDoneContract.showAnyOtherErrors(userRegistrationFailureInfo.getErrorDescription());

    }

    @Override
    public void onLoginFailedWithTwoStepError(JSONObject prefilledRecord,
                                              String socialRegistrationToken) {
        almostDoneContract.hideMarketingOptSpinner();
    }

    @Override
    public void onLoginFailedWithMergeFlowError(String mergeToken, String existingProvider,
                                                String conflictingIdentityProvider, String conflictingIdpNameLocalized,
                                                String existingIdpNameLocalized, String emailId) {
        almostDoneContract.hideMarketingOptSpinner();
        almostDoneContract.addMergeAccountFragment();
    }


    @Override
    public void onContinueSocialProviderLoginFailure(final UserRegistrationFailureInfo userRegistrationFailureInfo) {
        handleContinueSocialProviderFailed(userRegistrationFailureInfo);
    }

    private void handleContinueSocialProviderFailed(UserRegistrationFailureInfo userRegistrationFailureInfo) {
        almostDoneContract.hideMarketingOptSpinner();
        if (null != userRegistrationFailureInfo.getErrorDescription()) {
            almostDoneContract.emailErrorMessage(userRegistrationFailureInfo);
        } else {
            emailAlreadyInUse(userRegistrationFailureInfo);
        }
    }

    private void emailAlreadyInUse(UserRegistrationFailureInfo userRegistrationFailureInfo) {
        if (userRegistrationFailureInfo.getErrorCode() == EMAIL_ADDRESS_ALREADY_USE_CODE) {
            if (RegistrationHelper.getInstance().isMobileFlow()) {
                almostDoneContract.phoneNumberAlreadyInuseError();
            } else {
                almostDoneContract.emailAlreadyInuseError();
            }
        }
    }

    @Override
    public void onContinueSocialProviderLoginSuccess() {
        almostDoneContract.storePreference(mEmail);
        almostDoneContract.handleContinueSocialProvider();
    }

    public void parseRegistrationInfo(Bundle bundle) {
        mBundle = bundle;
        if (null != bundle) {
            handleSocialTwoStepError(bundle);
        }
        if (null != mProvider) {
            mProvider = Character.toUpperCase(mProvider.charAt(0)) + mProvider.substring(1);
        }
        if (isEmailExist) {
            almostDoneContract.emailFieldHide();
        } else {
            if (bundle == null) {
                almostDoneContract.enableContinueBtn();
            } else {
                almostDoneContract.showEmailField();
            }
        }
    }

    private void handleSocialTwoStepError(Bundle bundle) {
        try {
            if (bundle.getString(RegConstants.SOCIAL_TWO_STEP_ERROR) != null) {
                JSONObject mPreRegJson = new JSONObject(bundle.getString(RegConstants.SOCIAL_TWO_STEP_ERROR));
                performSocialTwoStepError(mPreRegJson, bundle);
            }
            if (null == mGivenName) {
                mGivenName = mDisplayName;
            }

        } catch (JSONException e) {
            RLog.e(RLog.EXCEPTION, "AlmostDoneFragment Exception : " + e.getMessage());
        }
    }

    private void performSocialTwoStepError(JSONObject mPreRegJson, Bundle mBundle) {
        try {
            if (null != mPreRegJson) {
                mProvider = mBundle.getString(RegConstants.SOCIAL_PROVIDER);
                mRegistrationToken = mBundle.getString(RegConstants.SOCIAL_REGISTRATION_TOKEN);

                if (!mPreRegJson.isNull(RegConstants.REGISTER_GIVEN_NAME)
                        && !RegConstants.SOCIAL_BLANK_CHARACTER.equals(mPreRegJson
                        .getString(RegConstants.REGISTER_GIVEN_NAME))) {
                    setGivenName(mPreRegJson.getString(RegConstants.REGISTER_GIVEN_NAME));
                }
                if (!mPreRegJson.isNull(RegConstants.REGISTER_DISPLAY_NAME)
                        && !RegConstants.SOCIAL_BLANK_CHARACTER.equals(mPreRegJson
                        .getString(RegConstants.REGISTER_DISPLAY_NAME))) {
                    setDisplayName(mPreRegJson.getString(RegConstants.REGISTER_DISPLAY_NAME));
                }
                if (!mPreRegJson.isNull(RegConstants.REGISTER_FAMILY_NAME)
                        && !RegConstants.SOCIAL_BLANK_CHARACTER.equals(mPreRegJson
                        .getString(RegConstants.REGISTER_FAMILY_NAME))) {
                    setFamilyName(mPreRegJson.getString(RegConstants.REGISTER_FAMILY_NAME));
                }
                if (!mPreRegJson.isNull(RegConstants.REGISTER_EMAIL)
                        && !RegConstants.SOCIAL_BLANK_CHARACTER.equals(mPreRegJson
                        .getString(RegConstants.REGISTER_EMAIL))) {
                    setEmail(mPreRegJson.getString(RegConstants.REGISTER_EMAIL));
                    setEmailExist(true);
                } else {
                    setEmailExist(false);
                }
            }
        } catch (JSONException e) {
            RLog.e(RLog.EXCEPTION, "AlmostDoneFragment Exception : " + e.getMessage());
        }
    }

    @Override
    public void onUpdateSuccess() {
        almostDoneContract.hideMarketingOptSpinner();
        almostDoneContract.trackMarketingOpt();
    }

    @Override
    public void onUpdateFailedWithError(final int error) {
        handleUpdateReceiveMarket(error);
    }

    private void handleUpdateReceiveMarket(int error) {
        almostDoneContract.hideMarketingOptSpinner();
        if (error == Integer.parseInt(RegConstants.INVALID_REFRESH_TOKEN_CODE)) {
            almostDoneContract.replaceWithHomeFragment();
            return;
        }
        if (error == RegConstants.FAILURE_TO_CONNECT || error == RegConstants.BAD_RESPONSE_ERROR_CODE) {
            almostDoneContract.failedToConnectToServer();
            return;
        }
        almostDoneContract.updateMarketingOptFailedError();
    }

    public void updateUser(boolean isReMarketingOptCheck) {
        if (Jump.getSignedInUser() != null) {
            almostDoneContract.showMarketingOptSpinner();
            mUser.updateReceiveMarketingEmail(this, isReMarketingOptCheck);
        }
    }

    public void register(boolean isReMarketingOptCheck, String email) {
        if (isOnline()) {
            almostDoneContract.hideErrorMessage();
            almostDoneContract.showMarketingOptSpinner();
            mUser.registerUserInfoForSocial(mGivenName, mDisplayName, mFamilyName, isEmailExist ? mEmail : email, true,
                    isReMarketingOptCheck, this, mRegistrationToken);
            setEmail(email);
        }
    }

    public void storeEmailOrMobileInPreference() {
        if (mEmail != null) {
            almostDoneContract.storePreference(mEmail);
            return;
        }
        String mobileNo = mUser.getMobile();
        String email = mUser.getEmail();

        if (FieldsValidator.isValidMobileNumber(mobileNo)) {
            almostDoneContract.storePreference(mobileNo);
        }
        if (FieldsValidator.isValidEmail(email)) {
            almostDoneContract.storePreference(email);
        }
    }

    public boolean isValidEmail() {
        return FieldsValidator.isValidEmail(mEmail);
    }

    public boolean isEmailVerificationStatus() {
        return (mUser.isEmailVerified() || mUser.isMobileVerified());
    }

    public void handleClearUserData() {
        mUser.logout(null);
    }

    public void handleUpdateMarketingOpt() {
        if (isOnline()) {
            almostDoneContract.handleUpdateUser();
        } else {
            almostDoneContract.marketingOptCheckDisable();
        }
    }

    public void handleTraditionalTermsAndCondition() {
        if (RegistrationConfiguration.getInstance().isTermsAndConditionsAcceptanceRequired()) {
            if (almostDoneContract.isAcceptTermsChecked()) {
                almostDoneContract.handleAcceptTermsTrue();
            } else {
                almostDoneContract.showTermsAndConditionError();
            }
        } else {
            almostDoneContract.completeRegistration();
        }
    }

    public void handleSocialTermsAndCondition() {
        if (RegistrationConfiguration.getInstance().isTermsAndConditionsAcceptanceRequired() && almostDoneContract.isAcceptTermsContainerVisible()) {
            if (almostDoneContract.isAcceptTermsChecked()) {
                register(almostDoneContract.isMarketingOptChecked(), almostDoneContract.getEmailOrMobileNumber());
            } else {
                almostDoneContract.showTermsAndConditionError();
            }
        } else {
            register(almostDoneContract.isMarketingOptChecked(), almostDoneContract.getEmailOrMobileNumber());
        }
    }

    public String getGivenName() {
        return mGivenName;
    }

    public void setGivenName(String mGivenName) {
        this.mGivenName = mGivenName;
    }

    public String getDisplayName() {
        return mDisplayName;
    }

    public void setDisplayName(String mDisplayName) {
        this.mDisplayName = mDisplayName;
    }

    public String getFamilyName() {
        return mFamilyName;
    }

    public void setFamilyName(String mFamilyName) {
        this.mFamilyName = mFamilyName;
    }

    public String getEmail() {
        return mEmail;
    }

    public void setEmail(String mEmail) {
        this.mEmail = mEmail;
    }

    public boolean isEmailExist() {
        return isEmailExist;
    }

    public void setEmailExist(boolean emailExist) {
        isEmailExist = emailExist;
    }

    public boolean isOnline() {
        return isOnline;
    }

    public void setOnline(boolean online) {
        isOnline = online;
    }
}


