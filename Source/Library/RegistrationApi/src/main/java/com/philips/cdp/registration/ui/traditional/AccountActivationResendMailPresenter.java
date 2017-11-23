package com.philips.cdp.registration.ui.traditional;

import com.philips.cdp.registration.*;
import com.philips.cdp.registration.dao.*;
import com.philips.cdp.registration.events.*;
import com.philips.cdp.registration.handlers.*;
import com.philips.cdp.registration.settings.*;
import com.philips.cdp.registration.ui.utils.*;

public class AccountActivationResendMailPresenter implements NetworkStateListener,
        ResendVerificationEmailHandler {

    private final AccountActivationResendMailContract accountActivationContract;


    private final User user;

    private final RegistrationHelper registrationHelper;


    public AccountActivationResendMailPresenter(
            AccountActivationResendMailContract accountActivationContract, User user, RegistrationHelper registrationHelper) {
        this.user = user;
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
    public void onResendVerificationEmailSuccess() {
        accountActivationContract.handleResendVerificationEmailSuccess();
    }

    @Override
    public void onResendVerificationEmailFailedWithError(
            final UserRegistrationFailureInfo userRegistrationFailureInfo) {
        accountActivationContract.handleResendVerificationEmailFailedWithError
                (userRegistrationFailureInfo);
    }

    void resendMail(User user, String email) {
        user.resendVerificationMail(email, this);
    }

}
