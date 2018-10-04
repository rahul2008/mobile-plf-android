package com.philips.cdp.registration.app.infra;

import com.philips.cdp.registration.configuration.RegistrationConfiguration;
import com.philips.cdp.registration.injection.RegistrationComponent;
import com.philips.platform.appinfra.AppInfraInterface;
import com.philips.platform.appinfra.appconfiguration.AppConfigurationInterface;
import com.philips.platform.appinfra.appidentity.AppIdentityInterface;

import junit.framework.TestCase;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

/**
 * Created by philips on 11/27/17.
 */
@RunWith(MockitoJUnitRunner.class)
public class AppInfraWrapperTest extends TestCase{


    @Mock
    private RegistrationComponent mockRegistrationComponent;

    @Mock
    private AppInfraInterface appInfraInterfaceMock;
    @Mock
    private AppIdentityInterface appIdentityInterfaceMock;

    private String SD_COUNTRYMAPPING_ID_KEY = "servicediscovery.countryMapping";


    private AppInfraWrapper appInfraWrapper;

    @Before
    public void setUp() throws Exception {
        super.setUp();

        MockitoAnnotations.initMocks(this);
        RegistrationConfiguration.getInstance().setComponent(mockRegistrationComponent);

        appInfraWrapper = new AppInfraWrapper(appInfraInterfaceMock);

       // Mockito.when(appInfraInterfaceMock.getConfigInterface()).thenReturn(appConfigurationInterfaceMock);
    }


    @Test
    public void getAppInfraProperty() {

        Assert.assertNull(appInfraWrapper.getAppInfraProperty(SD_COUNTRYMAPPING_ID_KEY));
    }

    @Test
    public void getURProperty() {
        Assert.assertNull(appInfraWrapper.getURProperty(SD_COUNTRYMAPPING_ID_KEY));
    }




    @Test
    public void getAppState() {

        Mockito.when(appInfraInterfaceMock.getAppIdentity()).thenReturn(appIdentityInterfaceMock);
        Assert.assertNull(appInfraWrapper.getAppState());
    }

    @Test
    public void getAppIdentity() {

        Assert.assertNull(appInfraWrapper.getAppIdentity());
    }

}