package com.philips.cdp.registration.ui.traditional;

import com.philips.cdp.registration.BuildConfig;
import com.philips.cdp.registration.CustomRobolectricRunner;
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

/**
 * Created by philips on 11/23/17.
 */
@RunWith(CustomRobolectricRunner.class)
@Config(constants = BuildConfig.class, sdk = 21)
public class AccountActivationPresenterTest {


    @Mock
    private RegistrationComponent mockRegistrationComponent;

    @Mock
    AccountActivationContract accountActivationContractMock;

    AccountActivationPresenter accountActivationPresenter;

    @Mock
    RegistrationHelper registrationHelperMock;

    @Before
    public void setUp() throws Exception {

        MockitoAnnotations.initMocks(this);
        URInterface.setComponent(mockRegistrationComponent);

        accountActivationPresenter = new AccountActivationPresenter(accountActivationContractMock, registrationHelperMock);

    }

    @Test
    public void onNetWorkStateReceived() throws Exception {

        accountActivationPresenter.onNetWorkStateReceived(true);
        Mockito.verify(accountActivationContractMock).handleUiState(true);
    }

    @Test
    public void registerListener() throws Exception {
        accountActivationPresenter.registerListener();
        Mockito.verify(registrationHelperMock).registerNetworkStateListener(accountActivationPresenter);
    }

    @Test
    public void unRegisterListener() throws Exception {

        accountActivationPresenter.unRegisterListener();
        Mockito.verify(registrationHelperMock).unRegisterNetworkListener(accountActivationPresenter);
    }

    @Test
    public void onLoginSuccess() throws Exception {
        accountActivationPresenter.onLoginSuccess();
        Mockito.verify(accountActivationContractMock).updateActivationUIState();
    }

    @Mock
    UserRegistrationFailureInfo userRegistrationFailureInfoMock;

    @Test
    public void onLoginFailedWithError() throws Exception {

        accountActivationPresenter.onLoginFailedWithError(userRegistrationFailureInfoMock);

        Mockito.verify(accountActivationContractMock).verificationError(userRegistrationFailureInfoMock.getErrorDescription());
        Mockito.verify(accountActivationContractMock).hideActivateSpinner();
        Mockito.verify(accountActivationContractMock).activateButtonEnable(true);
    }

}