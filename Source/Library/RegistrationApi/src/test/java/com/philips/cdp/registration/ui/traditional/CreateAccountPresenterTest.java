package com.philips.cdp.registration.ui.traditional;

import com.philips.cdp.registration.*;
import com.philips.cdp.registration.configuration.*;
import com.philips.cdp.registration.events.*;
import com.philips.cdp.registration.injection.*;
import com.philips.cdp.registration.settings.*;
import com.philips.cdp.registration.ui.utils.*;
import com.philips.cdp.registration.update.*;
import com.philips.platform.appinfra.logging.*;

import org.junit.*;
import org.junit.runner.*;
import org.mockito.*;
import org.robolectric.*;
import org.robolectric.annotation.*;

import static org.mockito.Mockito.*;

@RunWith(CustomRobolectricRunner.class)
@Config(constants = BuildConfig.class, sdk = 21)
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
        URInterface.setComponent(registrationComponentMock);
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