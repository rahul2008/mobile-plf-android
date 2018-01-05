package com.philips.cdp.registration.ui.utils;

import com.philips.cdp.registration.BuildConfig;
import com.philips.cdp.registration.CustomRobolectricRunner;
import com.philips.cdp.registration.configuration.RegistrationConfiguration;
import com.philips.cdp.registration.configuration.RegistrationLaunchMode;
import com.philips.cdp.registration.injection.RegistrationComponent;
import com.philips.cdp.registration.listener.UserRegistrationUIEventListener;
import com.philips.cdp.registration.settings.RegistrationFunction;
import com.philips.platform.appinfra.logging.LoggingInterface;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.Assert.*;

/**
 * Created by philips on 11/30/17.
 */
@RunWith(CustomRobolectricRunner.class)
@org.robolectric.annotation.Config(constants = BuildConfig.class, sdk = 21)
public class URLaunchInputTest {

    @Mock
    private RegistrationComponent mockRegistrationComponent;
    @Mock
    private LoggingInterface mockLoggingInterface;

    URLaunchInput urLaunchInput;
    /*@Mock
    private RegistrationFunction registrationFunctionMock;*/
    @Mock
    private UserRegistrationUIEventListener userRegListenerMock;
 /*   @Mock
    private RegistrationLaunchMode registrationLunchModeMock;*/
    @Mock
    private RegistrationContentConfiguration regConfigMock;

  /*  @Mock
    private UIFlow uiFlowMock;*/

    @Before
    public void setUp() throws Exception {

        MockitoAnnotations.initMocks(this);
        RegistrationConfiguration.getInstance().setComponent(mockRegistrationComponent);
        RLog.setMockLogger(mockLoggingInterface);

        urLaunchInput=new URLaunchInput();
    }

    @Test
    public void isAddtoBackStack() throws Exception {
        urLaunchInput.isAddtoBackStack();
    }

    @Test
    public void enableAddtoBackStack() throws Exception {
        urLaunchInput.enableAddtoBackStack(true);
    }

    @Test
    public void getRegistrationFunction() throws Exception {
        urLaunchInput.getRegistrationFunction();
    }
/*
    @Test
    public void setRegistrationFunction() throws Exception {
        urLaunchInput.setRegistrationFunction(registrationFunctionMock);
    }*/

    @Test
    public void setUserRegistrationUIEventListener() throws Exception {
        urLaunchInput.setUserRegistrationUIEventListener(userRegListenerMock);
    }

    @Test
    public void getUserRegistrationUIEventListener() throws Exception {
        urLaunchInput.getUserRegistrationUIEventListener();
    }

    @Test
    public void getEndPointScreen() throws Exception {
        urLaunchInput.getEndPointScreen();
    }



  /*  @Test
    public void setEndPointScreen() throws Exception {
        urLaunchInput.setEndPointScreen(registrationLunchModeMock);
    }*/

    @Test
    public void setRegistrationContentConfiguration() throws Exception {
        urLaunchInput.setRegistrationContentConfiguration(regConfigMock);
    }

    @Test
    public void getRegistrationContentConfiguration() throws Exception {
        urLaunchInput.getRegistrationContentConfiguration();
    }
/*
    @Test
    public void setUIFlow() throws Exception {
        urLaunchInput.setUIFlow(uiFlowMock);
    }*/

    @Test
    public void getUIflow() throws Exception {
        urLaunchInput.getUIflow();
    }

}