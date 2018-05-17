package com.philips.cdp.registration.ui.traditional;

import com.philips.cdp.registration.CustomRobolectricRunner;
import com.philips.cdp.registration.User;
import com.philips.cdp.registration.configuration.AppConfiguration;
import com.philips.cdp.registration.configuration.RegistrationConfiguration;
import com.philips.cdp.registration.events.EventHelper;
import com.philips.cdp.registration.injection.RegistrationComponent;
import com.philips.cdp.registration.settings.RegistrationHelper;
import com.philips.cdp.registration.ui.utils.NetworkUtility;
import com.philips.cdp.registration.ui.utils.RLog;
import com.philips.cdp.registration.update.UpdateUserProfile;
import com.philips.platform.appinfra.logging.LoggingInterface;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(CustomRobolectricRunner.class)
public class CreateAccountPresenterTest {

    @Mock
    RegistrationComponent registrationComponentMock;

    @Mock
    UpdateUserProfile updateUserProfileMock;

    @Mock
    RegistrationConfiguration registrationConfiguration;

    @Mock
    CreateAccountContract contractMock;

    @Mock
    private NetworkUtility mockNetworkUtility;

    @Mock
    RegistrationHelper registrationHelper;

    @Mock
    EventHelper eventHelper;

    @Mock
    User user;

    @Mock
    AppConfiguration appConfiguration;


    private CreateAccountPresenter presenter;

    @Mock
    LoggingInterface mockLoggingInterface;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        RegistrationConfiguration.getInstance().setComponent(registrationComponentMock);
        RLog.setMockLogger(mockLoggingInterface);

        presenter = new CreateAccountPresenter(contractMock);
    }

    @After
    public void tearDown() throws Exception {
        contractMock = null;
        presenter = null;
        updateUserProfileMock = null;
        registrationComponentMock = null;
        registrationConfiguration = null;
        appConfiguration = null;

    }

    //Temp:
   @Test
   public void testAccountCreationTime(){
        presenter.accountCreationTime();

    }

    @Test
    public void networkUtility(){
        when(mockNetworkUtility.isNetworkAvailable()).thenReturn(true);
        presenter.onNetWorkStateReceived(true);
        verify(contractMock).handleUiState();
        verify(contractMock).updateUiStatus();
    }

    @Test
    public void registerUser() {
        presenter.registerUserInfo(user, "firstName", "lastName", "email@kdk.com", "password123", true, true);
    }
    
}