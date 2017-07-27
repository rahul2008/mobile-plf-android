package com.philips.cdp.registration.ui.traditional;

import com.philips.cdp.registration.BuildConfig;
import com.philips.cdp.registration.User;
import com.philips.cdp.registration.configuration.AppConfiguration;
import com.philips.cdp.registration.configuration.RegistrationConfiguration;
import com.philips.cdp.registration.events.EventHelper;
import com.philips.cdp.registration.injection.RegistrationComponent;
import com.philips.cdp.registration.settings.RegistrationHelper;
import com.philips.cdp.registration.ui.utils.NetworkUtility;
import com.philips.cdp.registration.ui.utils.URInterface;
import com.philips.cdp.registration.update.UpdateUserProfile;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class)
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

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        URInterface.setComponent(registrationComponentMock);
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
//        presenter.accountCreationTime();
//
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