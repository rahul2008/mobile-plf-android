package com.philips.cdp.registration.ui.traditional;

import com.philips.cdp.registration.BuildConfig;
import com.philips.cdp.registration.CustomRobolectricRunner;
import com.philips.cdp.registration.User;
import com.philips.cdp.registration.configuration.RegistrationConfiguration;
import com.philips.cdp.registration.injection.RegistrationComponent;
import com.philips.cdp.registration.settings.RegistrationHelper;
import com.philips.cdp.registration.ui.utils.RLog;
import com.philips.cdp.registration.ui.utils.URInterface;
import com.philips.platform.appinfra.logging.LoggingInterface;

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
public class MarketingAccountPresenterTest {

    @Mock
    MarketingAccountContract marketingAccountContractMock;
    @Mock
    private RegistrationComponent registrationComponentMock;

    @Mock
    private LoggingInterface mockLoggingInterface;

    private MarketingAccountPresenter marketingAccountPresenter;

    @Mock
    private User userMock;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        RegistrationConfiguration.getInstance().setComponent(registrationComponentMock);
        RLog.setMockLogger(mockLoggingInterface);

        marketingAccountPresenter = new MarketingAccountPresenter(marketingAccountContractMock);
    }

    @Test
    public void onNetWorkStateReceived() throws Exception {
        marketingAccountPresenter.onNetWorkStateReceived(true);

        Mockito.verify(marketingAccountContractMock).handleUiState();
    }

    @Test
    public void onUpdateSuccess() throws Exception {
        marketingAccountPresenter.onUpdateSuccess();

        Mockito.verify(marketingAccountContractMock).trackRemarketing();
        RLog.i("MarketingAccountFragment", "onUpdateSuccess ");
        Mockito.verify(marketingAccountContractMock).hideRefreshProgress();
        Mockito.verify(marketingAccountContractMock).handleRegistrationSuccess();
    }

    @Test
    public void onUpdateFailedWithError() throws Exception {

        marketingAccountPresenter.onUpdateFailedWithError(1);
        Mockito.verify(marketingAccountContractMock).hideRefreshProgress();
    }

    @Mock
    RegistrationHelper registrationHelperMock;

    @Test
    public void register() throws Exception {

       // Mockito.when(RegistrationHelper.getInstance()).thenReturn(registrationHelperMock);
        //marketingAccountPresenter.register();
    }

    @Test
    public void unRegister() throws Exception {

       // marketingAccountPresenter.unRegister();
    }

    @Test
    public void updateMarketingEmail() throws Exception {

        marketingAccountPresenter.updateMarketingEmail(userMock,true);

        Mockito.verify(userMock).updateReceiveMarketingEmail(marketingAccountPresenter, true);
    }

}