package com.philips.cdp.registration.ui.traditional;

import com.philips.cdp.registration.configuration.RegistrationConfiguration;
import com.philips.cdp.registration.dao.UserRegistrationFailureInfo;
import com.philips.cdp.registration.events.NetworkStateListener;
import com.philips.cdp.registration.handlers.LoginHandler;
import com.philips.cdp.registration.settings.RegistrationHelper;
import com.philips.cdp.registration.ui.utils.LoginFailureNotification;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONObject;

/**
 * Created by philips on 22/06/17.
 */

public class AccountActivationPresenter implements NetworkStateListener, LoginHandler {

    private final AccountActivationContract accountActivationContract;

    private final RegistrationHelper registrationHelper;


    public AccountActivationPresenter(AccountActivationContract accountActivationContract, RegistrationHelper registrationHelper) {
        this.registrationHelper = registrationHelper;
        RegistrationConfiguration.getInstance().getComponent().inject(this);
        this.accountActivationContract = accountActivationContract;
    }
    @Override
    public void onNetWorkStateReceived(boolean isOnline) {
        accountActivationContract.handleUiState(isOnline);
    }

    public void registerListener() {
        registrationHelper.registerNetworkStateListener(this);
    }

    public void unRegisterListener() {
        registrationHelper.unRegisterNetworkListener(this);
    }

    @Override
    public void onLoginSuccess() {
        accountActivationContract.updateActivationUIState();
    }

    @Override
    public void onLoginFailedWithError(final UserRegistrationFailureInfo userRegistrationFailureInfo) {
        EventBus.getDefault().post(new LoginFailureNotification());
        accountActivationContract.verificationError(userRegistrationFailureInfo.getErrorDescription());
        accountActivationContract.hideActivateSpinner();
        accountActivationContract.activateButtonEnable(true);
    }

    @Override
    public void onLoginFailedWithTwoStepError(JSONObject prefilledRecord, String socialRegistrationToken) {
        //nope
    }

    @Override
    public void onLoginFailedWithMergeFlowError(String mergeToken, String existingProvider, String conflictingIdentityProvider, String conflictingIdpNameLocalized, String existingIdpNameLocalized, String emailId) {
        //nope
    }

    @Override
    public void onContinueSocialProviderLoginSuccess() {
        //nope
    }

    @Override
    public void onContinueSocialProviderLoginFailure(UserRegistrationFailureInfo userRegistrationFailureInfo) {
        //nope
    }

}
