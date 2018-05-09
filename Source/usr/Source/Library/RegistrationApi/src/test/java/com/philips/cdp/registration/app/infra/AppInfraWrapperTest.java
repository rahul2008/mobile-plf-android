package com.philips.cdp.registration.app.infra;

import com.philips.cdp.registration.BuildConfig;
import com.philips.cdp.registration.CustomRobolectricRunner;
import com.philips.cdp.registration.User;
import com.philips.cdp.registration.configuration.RegistrationConfiguration;
import com.philips.cdp.registration.injection.RegistrationComponent;
import com.philips.cdp.registration.ui.utils.RLog;
import com.philips.cdp.registration.ui.utils.URInterface;
import com.philips.platform.appinfra.AppInfraInterface;
import com.philips.platform.appinfra.appconfiguration.AppConfigurationInterface;
import com.philips.platform.appinfra.appidentity.AppIdentityInterface;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.robolectric.annotation.Config;

import static org.junit.Assert.*;

/**
 * Created by philips on 11/27/17.
 */
@RunWith(CustomRobolectricRunner.class)
@Config(constants = BuildConfig.class, sdk = 21)
public class AppInfraWrapperTest {


    @Mock
    private RegistrationComponent mockRegistrationComponent;

    @Mock
    AppInfraInterface appInfraInterfaceMock;

    @Mock
    AppConfigurationInterface appConfigurationInterfaceMock;

    String SD_COUNTRYMAPPING_ID_KEY = "servicediscovery.countryMapping";


    AppInfraWrapper appInfraWrapper;

    @Before
    public void setUp() throws Exception {

        MockitoAnnotations.initMocks(this);
        RegistrationConfiguration.getInstance().setComponent(mockRegistrationComponent);

        appInfraWrapper = new AppInfraWrapper(appInfraInterfaceMock);

        Mockito.when(appInfraInterfaceMock.getConfigInterface()).thenReturn(appConfigurationInterfaceMock);
    }


    @Test
    public void getAppInfraProperty() throws Exception {

        Assert.assertNull(appInfraWrapper.getAppInfraProperty(SD_COUNTRYMAPPING_ID_KEY));
    }

    @Test
    public void getURProperty() throws Exception {
        Assert.assertNull(appInfraWrapper.getURProperty(SD_COUNTRYMAPPING_ID_KEY));
    }


    @Mock
    AppIdentityInterface appIdentityInterfaceMock;

    @Test
    public void getAppState() throws Exception {

        Mockito.when(appInfraInterfaceMock.getAppIdentity()).thenReturn(appIdentityInterfaceMock);
        Assert.assertNull(appInfraWrapper.getAppState());
    }

    @Test
    public void getAppIdentity() throws Exception {

        Assert.assertNull(appInfraWrapper.getAppIdentity());
    }

}