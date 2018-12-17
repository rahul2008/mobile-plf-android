package com.philips.cdp.registration.settings;

import com.philips.cdp.registration.configuration.RegistrationConfiguration;
import com.philips.cdp.registration.injection.RegistrationComponent;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;

/**
 * Created by philips on 12/3/17.
 */

@RunWith(MockitoJUnitRunner.class)
public class RegistrationSettingsURLTest {

    RegistrationSettingsURL registrationSettingsURL;

    @Mock
    private RegistrationComponent registartionComponentMock;

    @Before
    public void setUp() throws Exception {

        MockitoAnnotations.initMocks(this);

        RegistrationConfiguration.getInstance().setComponent(registartionComponentMock);

        registrationSettingsURL=new RegistrationSettingsURL();
    }

    @Test(expected = NullPointerException.class)
    public void initialiseConfigParameters() throws Exception {

        registrationSettingsURL.initialiseConfigParameters("en-US");
    }

    @Test
    public void isMobileFlow() throws Exception {

        registrationSettingsURL.isMobileFlow();
    }

    @Test
    public void setMobileFlow() throws Exception {
        registrationSettingsURL.setMobileFlow(true);
    }

}