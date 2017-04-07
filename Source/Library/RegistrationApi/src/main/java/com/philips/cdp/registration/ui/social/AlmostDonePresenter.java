package com.philips.cdp.registration.ui.social;


import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

import com.philips.cdp.registration.R;
import com.philips.cdp.registration.User;
import com.philips.cdp.registration.app.tagging.AppTagging;
import com.philips.cdp.registration.app.tagging.AppTagingConstants;
import com.philips.cdp.registration.configuration.RegistrationConfiguration;
import com.philips.cdp.registration.dao.UserRegistrationFailureInfo;
import com.philips.cdp.registration.events.EventHelper;
import com.philips.cdp.registration.events.EventListener;
import com.philips.cdp.registration.events.NetworStateListener;
import com.philips.cdp.registration.handlers.SocialProviderLoginHandler;
import com.philips.cdp.registration.handlers.UpdateUserDetailsHandler;
import com.philips.cdp.registration.settings.RegistrationHelper;
import com.philips.cdp.registration.ui.utils.FieldsValidator;
import com.philips.cdp.registration.ui.utils.NetworkUtility;
import com.philips.cdp.registration.ui.utils.RLog;
import com.philips.cdp.registration.ui.utils.RegConstants;
import com.philips.cdp.registration.ui.utils.RegUtility;
import com.philips.cdp.registration.ui.utils.UIFlow;
import com.philips.cdp.registration.ui.utils.URInterface;

import org.json.JSONException;
import org.json.JSONObject;

import javax.inject.Inject;

import static com.philips.cdp.registration.ui.traditional.LogoutFragment.BAD_RESPONSE_ERROR_CODE;

public class AlmostDonePresenter implements NetworStateListener,EventListener,SocialProviderLoginHandler,UpdateUserDetailsHandler {


    @Inject
    User mUser;

    @Inject
    NetworkUtility networkUtility;

    private final AlmostDoneContract almostDoneContract;

    private final Handler mHandler = new Handler(Looper.getMainLooper());

    private Thread mUiThread = Looper.getMainLooper().getThread();

    private final static int EMAIL_ADDRESS_ALREADY_USE_CODE = 390;

    private String mGivenName;

    private String mDisplayName;

    private String mFamilyName;

    private String mEmail;

    private String mProvider;

    private boolean isEmailExist;

    private String mRegistrationToken;

    private Bundle mBundle;

    private boolean isOnline;

    public AlmostDonePresenter(AlmostDoneContract almostDoneContract) {
        URInterface.getComponent().inject(this);
        this.almostDoneContract = almostDoneContract;
        RegistrationHelper.getInstance().registerNetworkStateListener(this);
        EventHelper.getInstance().registerEventNotification(RegConstants.JANRAIN_INIT_SUCCESS, this);
    }


   public void cleanUp(){
       RegistrationHelper.getInstance().unRegisterNetworkListener(this);
       EventHelper.getInstance().unregisterEventNotification(RegConstants.JANRAIN_INIT_SUCCESS,
               this);
   }

    @Override
    public void onNetWorkStateReceived(boolean isOnline) {
        this.isOnline = isOnline;
        updateUIControls();

    }

    public void updateUIControls() {
        if (isEmailExist) {
            if (isOnline) {
                almostDoneContract.enableContinueBtn();
            } else {
                almostDoneContract.handleOfflineMode();
            }
        } else {
            if (isOnline) {
                almostDoneContract.validateEmailFieldUI();
            } else {
                almostDoneContract.handleOfflineMode();
            }
        }
    }

    @Override
    public void onEventReceived(String event) {
        if (RegConstants.JANRAIN_INIT_SUCCESS.equals(event)) {
            updateUIControls();
        }
    }

    public void handleAcceptTermsAndReceiveMarketingOpt(){
        if (RegistrationConfiguration.getInstance().isEmailVerificationRequired()) {
            if (isEmailExist && almostDoneContract.getPreferenceStoredState((mEmail))){
                almostDoneContract.hideAcceptTermsView();
            }else if(mBundle !=null && mBundle.getString(RegConstants.SOCIAL_TWO_STEP_ERROR)!=null){
                almostDoneContract.updateABTestingUIFlow();
            }
        }else{
            almostDoneContract.hideAcceptTermsView();
        }
        updateTermsAndReceiveMarketingOpt();
    }

    public void updateTermsAndReceiveMarketingOpt() {
        if(isTermsAndConditionAccepted()){
            if(!isReceiveMarketingEmailOpt()){
                almostDoneContract.showMarketingOptCheck();
            }else{
                almostDoneContract.hideMarketingOptCheck();
            }
            almostDoneContract.updateTermsAndConditionView();
        }else if(mUser.getReceiveMarketingEmail()){
            almostDoneContract.updateReceiveMarktingView();
        }
    }

    @Override
    public void onLoginSuccess() {
        handleOnUIThread(new Runnable() {
            @Override
            public void run() {
                AppTagging.trackAction(AppTagingConstants.SEND_DATA, AppTagingConstants.SPECIAL_EVENTS,
                        AppTagingConstants.SUCCESS_LOGIN);
                almostDoneContract.hideMarketingOptSpinner();
            }
        });
    }

    @Override
    public void onLoginFailedWithError(final UserRegistrationFailureInfo userRegistrationFailureInfo) {

        handleOnUIThread(new Runnable() {
            @Override
            public void run() {
                handleLoginFailed(userRegistrationFailureInfo);
            }
        });
    }

    private void handleLoginFailed(UserRegistrationFailureInfo userRegistrationFailureInfo) {
        almostDoneContract.hideMarketingOptSpinner();
        if (userRegistrationFailureInfo.getErrorCode() == EMAIL_ADDRESS_ALREADY_USE_CODE) {
            if (RegistrationHelper.getInstance().isChinaFlow()){
                almostDoneContract.phoneNumberAlreadyInuseError();
            }else {
                almostDoneContract.emailAlreadyInuseError();
            }
           almostDoneContract.showLoginFailedError();
        }
    }

    @Override
    public void onLoginFailedWithTwoStepError(JSONObject prefilledRecord,
                                              String socialRegistrationToken) {
        handleOnUIThread(new Runnable() {
            @Override
            public void run() {
                almostDoneContract.hideMarketingOptSpinner();
            }
        });
    }

    @Override
    public void onLoginFailedWithMergeFlowError(String mergeToken, String existingProvider,
                                                String conflictingIdentityProvider, String conflictingIdpNameLocalized,
                                                String existingIdpNameLocalized, String emailId) {
        handleOnUIThread(new Runnable() {
            @Override
            public void run() {
                almostDoneContract.hideMarketingOptSpinner();
                almostDoneContract.addMergeAccountFragment();
            }
        });
    }


    @Override
    public void onContinueSocialProviderLoginFailure(final UserRegistrationFailureInfo userRegistrationFailureInfo) {

        handleOnUIThread(new Runnable() {
            @Override
            public void run() {
                handleContinueSocialProviderFailed(userRegistrationFailureInfo);
            }
        });

    }

    private void handleContinueSocialProviderFailed(UserRegistrationFailureInfo userRegistrationFailureInfo) {

        almostDoneContract.hideMarketingOptSpinner();
        if (null != userRegistrationFailureInfo.getDisplayNameErrorMessage()) {
            almostDoneContract.displayNameErrorMessage(userRegistrationFailureInfo,mDisplayName);
            return;
        }
        if (null != userRegistrationFailureInfo.getEmailErrorMessage()) {
            almostDoneContract.emailErrorMessage(userRegistrationFailureInfo);
        } else {
            emailAlreadyInUse(userRegistrationFailureInfo);
        }
    }

    private void emailAlreadyInUse(UserRegistrationFailureInfo userRegistrationFailureInfo) {
        if (userRegistrationFailureInfo.getErrorCode() == EMAIL_ADDRESS_ALREADY_USE_CODE) {
            if (RegistrationHelper.getInstance().isChinaFlow()){
                almostDoneContract.phoneNumberAlreadyInuseError();
            }else {
                almostDoneContract.emailAlreadyInuseError();
            }
        }
    }

    @Override
    public void onContinueSocialProviderLoginSuccess() {

        handleOnUIThread(new Runnable() {
            @Override
            public void run() {
                almostDoneContract.storePreference(mEmail);
                almostDoneContract.handleContinueSocialProvider();
            }
        });
    }

    private final void handleOnUIThread(Runnable runnable) {
        runnable.run();
        if (Thread.currentThread() != mUiThread) {
            mHandler.post(runnable);
        } else {
            runnable.run();
        }
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
            if(bundle.getString(RegConstants.SOCIAL_TWO_STEP_ERROR)!=null){
                trackAbtesting();
            }
            if(bundle.getString(RegConstants.SOCIAL_TWO_STEP_ERROR)!=null) {
                JSONObject mPreRegJson = new JSONObject(bundle.getString(RegConstants.SOCIAL_TWO_STEP_ERROR));
                performSocialTwoStepError(mPreRegJson,bundle);
            }
            if (null == mGivenName) {
                mGivenName = mDisplayName;
            }

        } catch (JSONException e) {
            RLog.e(RLog.EXCEPTION, "AlmostDoneFragment Exception : " + e.getMessage());
        }
    }

    private void performSocialTwoStepError(JSONObject mPreRegJson,Bundle mBundle) {
        try{
            if (null != mPreRegJson) {
                mProvider = mBundle.getString(RegConstants.SOCIAL_PROVIDER);
                mRegistrationToken = mBundle.getString(RegConstants.SOCIAL_REGISTRATION_TOKEN);

                if (!mPreRegJson.isNull(RegConstants.REGISTER_GIVEN_NAME)
                        && !RegConstants.SOCIAL_BLANK_CHARACTER.equals(mPreRegJson
                        .getString(RegConstants.REGISTER_GIVEN_NAME))) {
                    mGivenName = mPreRegJson.getString(RegConstants.REGISTER_GIVEN_NAME);
                }
                if (!mPreRegJson.isNull(RegConstants.REGISTER_DISPLAY_NAME)
                        && !RegConstants.SOCIAL_BLANK_CHARACTER.equals(mPreRegJson
                        .getString(RegConstants.REGISTER_DISPLAY_NAME))) {
                    mDisplayName = mPreRegJson.getString(RegConstants.REGISTER_DISPLAY_NAME);
                }
                if (!mPreRegJson.isNull(RegConstants.REGISTER_FAMILY_NAME)
                        && !RegConstants.SOCIAL_BLANK_CHARACTER.equals(mPreRegJson
                        .getString(RegConstants.REGISTER_FAMILY_NAME))) {
                    mFamilyName = mPreRegJson.getString(RegConstants.REGISTER_FAMILY_NAME);
                }
                if (!mPreRegJson.isNull(RegConstants.REGISTER_EMAIL)
                        && !RegConstants.SOCIAL_BLANK_CHARACTER.equals(mPreRegJson
                        .getString(RegConstants.REGISTER_EMAIL))) {
                    mEmail = mPreRegJson.getString(RegConstants.REGISTER_EMAIL);
                    isEmailExist = true;
                } else {
                    isEmailExist = false;
                }
            }
        }catch (JSONException e){
            RLog.e(RLog.EXCEPTION, "AlmostDoneFragment Exception : " + e.getMessage());
        }
    }

    private void trackAbtesting() {
        final UIFlow abTestingFlow = RegUtility.getUiFlow();

        switch (abTestingFlow){
            case FLOW_A :
                RLog.d(RLog.AB_TESTING, "UI Flow Type A");
                AppTagging.trackAction(AppTagingConstants.SEND_DATA, AppTagingConstants.AB_TEST,
                        AppTagingConstants.REGISTRATION_CONTROL);
                break;

            case FLOW_B:
                RLog.d(RLog.AB_TESTING, "UI Flow Type B");
                AppTagging.trackAction(AppTagingConstants.SEND_DATA, AppTagingConstants.AB_TEST,
                        AppTagingConstants.REGISTRATION_SPLIT_SIGN_UP);
                break;
            case FLOW_C:
                RLog.d(RLog.AB_TESTING, "UI Flow Type C");
                AppTagging.trackAction(AppTagingConstants.SEND_DATA, AppTagingConstants.AB_TEST,
                        AppTagingConstants.REGISTRATION_SOCIAL_PROOF);
                break;
            default:break;
        }
    }

    @Override
    public void onUpdateSuccess() {

        handleOnUIThread(new Runnable() {
            @Override
            public void run() {
                almostDoneContract.hideMarketingOptSpinner();
                almostDoneContract.trackMarketingOpt();
            }
        });

    }

    @Override
    public void onUpdateFailedWithError(final int error) {

        handleOnUIThread(new Runnable() {
            @Override
            public void run() {
                handleUpdateReceiveMarket(error);
            }
        });
    }

    private void handleUpdateReceiveMarket(int error) {
        almostDoneContract.hideMarketingOptSpinner();
        if (error == Integer.parseInt(RegConstants.INVALID_REFRESH_TOKEN_CODE)) {
            almostDoneContract.replaceWithHomeFragment();
            return;
        }
        if (error == RegConstants.FAILURE_TO_CONNECT || error == BAD_RESPONSE_ERROR_CODE) {
            almostDoneContract.failedToConnectToServer();
            return;
        }
        almostDoneContract.updateMarketingOptFailedError();
    }

    public void updateUser(boolean isReMarketingOptCheck) {
        mUser.updateReceiveMarketingEmail(this, isReMarketingOptCheck);
    }

    public void register(boolean isReMarketingOptCheck,String email) {
        if (networkUtility.isNetworkAvailable()) {
            almostDoneContract.hideErrorMessage();
            almostDoneContract.showMarketingOptSpinner();
            if (isEmailExist) {
                mUser.registerUserInfoForSocial(mGivenName, mDisplayName, mFamilyName, mEmail, true,
                        isReMarketingOptCheck, this, mRegistrationToken);
            } else {

                mUser.registerUserInfoForSocial(mGivenName, mDisplayName, mFamilyName,
                        email, true, isReMarketingOptCheck, this, mRegistrationToken);
            }
        }
    }

    public void storeEmailOrMobileInPreference() {
        if (mEmail != null) {
            almostDoneContract.storePreference(mEmail);
            return;
        }
        if(mUser.getMobile()!=null && !mUser.getMobile().equalsIgnoreCase("null")){
            almostDoneContract.storePreference(mUser.getMobile());
        }else if(mUser.getEmail()!=null && !mUser.getEmail().equalsIgnoreCase("null")){
            almostDoneContract.storePreference(mUser.getEmail());
        }
    }

    public boolean isTermsAndConditionAccepted(){
        boolean isTermAccepted = false;
        String mobileNo = mUser.getMobile();
        String email  = mUser.getEmail();
        if(FieldsValidator.isValidMobileNumber(mobileNo)){
            isTermAccepted = almostDoneContract.getPreferenceStoredState(mobileNo);
        }else if(FieldsValidator.isValidEmail(email)){
            isTermAccepted = almostDoneContract.getPreferenceStoredState(email);
        }
        return isTermAccepted;
    }

    public boolean isValidEmail(){
        return FieldsValidator.isValidEmail(mEmail);
    }

    public boolean isEmailVerificationStatus(){
        return mUser.getEmailVerificationStatus();
    }

    public boolean isReceiveMarketingEmailOpt(){
        return mUser.getReceiveMarketingEmail();
    }

    public void handleClearUserData(){
         mUser.logout(null);
    }

    public void handleUpdate() {
        if (networkUtility.isNetworkAvailable()) {
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
            almostDoneContract.launchWelcomeFragment();
        }
    }

    public void handleSocialTermsAndCondition() {

        if (RegistrationConfiguration.getInstance().isTermsAndConditionsAcceptanceRequired() && almostDoneContract.isAcceptTermsContainerVisible()) {
            if (almostDoneContract.isAcceptTermsChecked()) {
                  register(almostDoneContract.isMarketingOptChecked(), almostDoneContract.getMobileNumber());
            } else {
                almostDoneContract.showTermsAndConditionError();
            }
        } else {
              register(almostDoneContract.isMarketingOptChecked(),almostDoneContract.getMobileNumber());
        }
    }
}


