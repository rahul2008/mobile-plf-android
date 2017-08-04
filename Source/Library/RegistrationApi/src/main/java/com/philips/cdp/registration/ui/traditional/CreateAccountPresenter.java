package com.philips.cdp.registration.ui.traditional;

import com.philips.cdp.registration.*;
import com.philips.cdp.registration.app.tagging.*;
import com.philips.cdp.registration.configuration.RegistrationConfiguration;
import com.philips.cdp.registration.dao.UserRegistrationFailureInfo;
import com.philips.cdp.registration.events.*;
import com.philips.cdp.registration.handlers.TraditionalRegistrationHandler;
import com.philips.cdp.registration.settings.RegistrationHelper;
import com.philips.cdp.registration.ui.utils.*;

import javax.inject.Inject;

/**
 * Created by philips on 22/06/17.
 */

public class CreateAccountPresenter implements NetworStateListener,EventListener,TraditionalRegistrationHandler {

    private static final int FAILURE_TO_CONNECT = -1;

    private final static int EMAIL_ADDRESS_ALREADY_USE_CODE = 390;

    private final CreateAccountContract createAccountContract;

    @Inject
    User user;

    @Inject
    RegistrationHelper registrationHelper;

    @Inject
    EventHelper eventHelper;

    public CreateAccountPresenter(CreateAccountContract createAccountContract) {
        URInterface.getComponent().inject(this);
        this.createAccountContract = createAccountContract;
    }
    @Override
    public void onNetWorkStateReceived(boolean isOnline) {
        RLog.i(RLog.NETWORK_STATE, "CreateAccoutFragment :onNetWorkStateReceived : " + isOnline);
        createAccountContract.handleUiState();
        createAccountContract.updateUiStatus();
    }

    public void registerListener() {
        registrationHelper.registerNetworkStateListener(this);
        eventHelper.registerEventNotification(RegConstants.JANRAIN_INIT_SUCCESS, this);
    }

    public void unRegisterListener() {
        registrationHelper.unRegisterNetworkListener(this);
        eventHelper.unregisterEventNotification(RegConstants.JANRAIN_INIT_SUCCESS, this);
        eventHelper.unregisterEventNotification(RegConstants.JANRAIN_INIT_FAILURE, this);
    }

    public void registerUserInfo(User user, String firstName, String lastName, String email, String password, boolean olderThanAgeLimit, boolean isReceiveMarketingEmail){
        user.registerUserInfoForTraditional(firstName,lastName, email
                , password.toString(), olderThanAgeLimit, isReceiveMarketingEmail,this);
    }

    @Override
    public void onEventReceived(String event) {
        RLog.i(RLog.EVENT_LISTENERS, "CreateAccoutFragment :onCounterEventReceived : " + event);
        if (RegConstants.JANRAIN_INIT_SUCCESS.equals(event)) {
            createAccountContract.updateUiStatus();
        }
    }

    @Override
    public void onRegisterSuccess() {

        handleRegistrationSuccess();

    }

    @Override
    public void onRegisterFailedWithFailure(UserRegistrationFailureInfo userRegistrationFailureInfo) {
        handleRegisterFailedWithFailure(userRegistrationFailureInfo);
    }

    private void handleRegistrationSuccess() {
        RLog.i(RLog.CALLBACK, "CreateAccountFragment : onRegisterSuccess");
        if (RegistrationConfiguration.getInstance().isTermsAndConditionsAcceptanceRequired()) {
            createAccountContract.storeEMail();
        }
        createAccountContract.hideSpinner();
        createAccountContract.trackCheckMarketing();
        selectABTestingFlow();
        accountCreationTime();
    }

    public void accountCreationTime() {
        if(createAccountContract.getTrackCreateAccountTime() == 0 && RegUtility.getCreateAccountStartTime() > 0){
            createAccountContract.setTrackCreateAccountTime((System.currentTimeMillis() - RegUtility.getCreateAccountStartTime())/1000);
        }else{
            createAccountContract.setTrackCreateAccountTime((System.currentTimeMillis() - createAccountContract.getTrackCreateAccountTime())/1000);
        }
        createAccountContract.tractCreateActionStatus(AppTagingConstants.SEND_DATA, AppTagingConstants.TOTAL_TIME_CREATE_ACCOUNT, String.valueOf(createAccountContract.getTrackCreateAccountTime()));

        createAccountContract.setTrackCreateAccountTime(0);
    }

    private void selectABTestingFlow() {
        final UIFlow abTestingUIFlow= RegUtility.getUiFlow();
        createAccountContract.tractCreateActionStatus(AppTagingConstants.SEND_DATA, AppTagingConstants.SPECIAL_EVENTS,
                AppTagingConstants.SUCCESS_USER_CREATION);
        switch (abTestingUIFlow){
            case FLOW_A:
                RLog.d(RLog.AB_TESTING, "UI Flow Type A ");
                setABTestingFlow();
                break;
            case FLOW_B:
                RLog.d(RLog.AB_TESTING, "UI Flow Type B");
                createAccountContract.launchMarketingAccountFragment();
                break;
            case FLOW_C:
                RLog.d(RLog.AB_TESTING, "UI Flow Type  C");
                setABTestingFlow();
                break;
        }
    }

    private void setABTestingFlow() {
        if (RegistrationConfiguration.getInstance().isEmailVerificationRequired()) {
            if (FieldsValidator.isValidEmail(createAccountContract.getEmail())) {
                createAccountContract.launchAccountActivateFragment();
            } else {
                createAccountContract.launchMobileVerifyCodeFragment();
            }
        } else {
            createAccountContract.launchWelcomeFragment();
        }
    }


    private void handleRegisterFailedWithFailure(UserRegistrationFailureInfo userRegistrationFailureInfo) {
        RLog.i(RLog.CALLBACK, "CreateAccountFragment : onRegisterFailedWithFailure"+userRegistrationFailureInfo.getErrorCode());
        if (userRegistrationFailureInfo.getErrorCode() == EMAIL_ADDRESS_ALREADY_USE_CODE) {
            if (RegistrationHelper.getInstance().isChinaFlow()) {
                createAccountContract.emailError(R.string.reg_CreateAccount_Using_Phone_Alreadytxt);
            } else {
                createAccountContract.emailError(R.string.reg_EmailAlreadyUsed_TxtFieldErrorAlertMsg);
            }
            createAccountContract.scrollViewAutomaticallyToEmail();
            createAccountContract.emailAlreadyUsed();
        } else if (userRegistrationFailureInfo.getErrorCode() == FAILURE_TO_CONNECT) {
            createAccountContract.emailError(R.string.reg_JanRain_Server_Connection_Failed);
        } else {
            createAccountContract.emailError(userRegistrationFailureInfo.getErrorDescription());
            createAccountContract.scrollViewAutomaticallyToError();
        }
        AppTaggingErrors.trackActionRegisterError(userRegistrationFailureInfo,AppTagingConstants.JANRAIN);
        createAccountContract.registrtionFail();
    }

}
