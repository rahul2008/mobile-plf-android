package com.philips.cdp.registration.configuration;

import com.philips.cdp.registration.app.infra.AppInfraWrapper;
import com.philips.cdp.registration.injection.RegistrationComponent;
import com.philips.cdp.registration.ui.utils.URInterface;
import com.philips.platform.appinfra.appconfiguration.AppConfigurationInterface;
import com.philips.platform.appinfra.appconfiguration.AppConfigurationInterface.AppConfigurationError;

import junit.framework.TestCase;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;

import static com.philips.cdp.registration.configuration.URConfigurationConstants.HSDP_CONFIGURATION_APPLICATION_NAME;
import static com.philips.cdp.registration.configuration.URConfigurationConstants.UR;

@RunWith(MockitoJUnitRunner.class)
public class HSDPConfigurationTest extends TestCase{

    @Mock
    AppConfigurationInterface appConfigurationInterface;

    @InjectMocks
    AppInfraWrapper appInfraWrapperMock;

    @Mock
    RegistrationComponent component;

    final AppConfigurationError configError = new AppConfigurationError();

    HSDPConfiguration hsdpConfiguration;

    @Before
    public void setUp() throws Exception {
        URInterface.setComponent(component);
        MockitoAnnotations.initMocks(this);
        hsdpConfiguration = new HSDPConfiguration();
        hsdpConfiguration.setAppInfraWrapper(appInfraWrapperMock);

    }

    @After
    public void tearDown() throws Exception {
        appInfraWrapperMock = null;
        appConfigurationInterface = null;
    }

    @Test
    public void getHsdpAppName() throws Exception {
        appConfigurationInterface.setPropertyForKey(
                HSDP_CONFIGURATION_APPLICATION_NAME,
                UR,
                "uGrow",
                configError);

        String name = hsdpConfiguration.getHsdpAppName();
        assertEquals("uGrow", name);
    }

    @Test
    public void getHsdpSharedId() throws Exception {

    }

    @Test
    public void getHsdpSecretId() throws Exception {

    }

    @Test
    public void getHsdpBaseUrl() throws Exception {

    }

}