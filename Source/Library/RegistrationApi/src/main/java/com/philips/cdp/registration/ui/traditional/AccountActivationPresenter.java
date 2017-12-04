package com.philips.cdp.registration.ui.traditional;

import com.philips.cdp.registration.dao.UserRegistrationFailureInfo;
import com.philips.cdp.registration.events.NetworkStateListener;
import com.philips.cdp.registration.handlers.TraditionalLoginHandler;
import com.philips.cdp.registration.settings.RegistrationHelper;
import com.philips.cdp.registration.ui.utils.LoginFailureNotification;
import com.philips.cdp.registration.ui.utils.URInterface;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by philips on 22/06/17.
 */

public class AccountActivationPresenter implements NetworkStateListener, TraditionalLoginHandler  {

    private final AccountActivationContract accountActivationContract;

    private final RegistrationHelper registrationHelper;


    public AccountActivationPresenter(AccountActivationContract accountActivationContract, RegistrationHelper registrationHelper) {
        this.registrationHelper = registrationHelper;
        URInterface.getComponent().inject(this);
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

}
