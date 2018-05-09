package com.philips.cdp.registration.ui.traditional;

import com.philips.cdp.registration.BuildConfig;
import com.philips.cdp.registration.CustomRobolectricRunner;
import com.philips.cdp.registration.User;
import com.philips.cdp.registration.configuration.RegistrationConfiguration;
import com.philips.cdp.registration.dao.UserRegistrationFailureInfo;
import com.philips.cdp.registration.injection.RegistrationComponent;
import com.philips.cdp.registration.settings.RegistrationHelper;
import com.philips.cdp.registration.ui.utils.URInterface;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.robolectric.annotation.Config;

import static org.junit.Assert.*;

/**
 * Created by philips on 11/23/17.
 */

@RunWith(CustomRobolectricRunner.class)
@Config(constants = BuildConfig.class, sdk = 21)
public class AccountActivationResendMailPresenterTest {

    @Mock
    private RegistrationComponent mockRegistrationComponent;

    @Mock
    AccountActivationResendMailContract accountActivationResendMailContractMock;

    @Mock
    User userMock;

    @Mock
    RegistrationHelper registrationHelperMock;

    AccountActivationResendMailPresenter accountActivationResendMailPresenter;


    @Before
    public void setUp() throws Exception {

        MockitoAnnotations.initMocks(this);
        RegistrationConfiguration.getInstance().setComponent(mockRegistrationComponent);

        accountActivationResendMailPresenter = new AccountActivationResendMailPresenter(accountActivationResendMailContractMock, userMock, registrationHelperMock);

    }

    @Test
    public void onNetWorkStateReceived() throws Exception {

        accountActivationResendMailPresenter.onNetWorkStateReceived(true);
        Mockito.verify(accountActivationResendMailContractMock).handleUiState(true);
    }

    @Test
    public void registerListener() throws Exception {
        accountActivationResendMailPresenter.registerListener();
        Mockito.verify(registrationHelperMock).registerNetworkStateListener(accountActivationResendMailPresenter);
    }

    @Test
    public void unRegisterListener() throws Exception {

        accountActivationResendMailPresenter.unRegisterListener();
        Mockito.verify(registrationHelperMock).unRegisterNetworkListener(accountActivationResendMailPresenter);
    }

    @Test
    public void onResendVerificationEmailSuccess() throws Exception {

        accountActivationResendMailPresenter.onResendVerificationEmailSuccess();
        Mockito.verify(accountActivationResendMailContractMock).handleResendVerificationEmailSuccess();
    }

    UserRegistrationFailureInfo userRegistrationFailureInfoMock;

    @Test
    public void onResendVerificationEmailFailedWithError() throws Exception {
        accountActivationResendMailPresenter.onResendVerificationEmailFailedWithError(userRegistrationFailureInfoMock);

        Mockito.verify(accountActivationResendMailContractMock).handleResendVerificationEmailFailedWithError
                (userRegistrationFailureInfoMock);
    }

}