package com.philips.cdp.registration.configuration;

import com.philips.cdp.registration.app.infra.AppInfraWrapper;
import com.philips.cdp.registration.injection.RegistrationComponent;
import com.philips.cdp.registration.ui.utils.URInterface;

import junit.framework.TestCase;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.net.URLEncoder;

import static com.philips.cdp.registration.configuration.URConfigurationConstants.HSDP_CONFIGURATION_APPLICATION_NAME;
import static com.philips.cdp.registration.configuration.URConfigurationConstants.HSDP_CONFIGURATION_BASE_URL;
import static com.philips.cdp.registration.configuration.URConfigurationConstants.HSDP_CONFIGURATION_SECRET;
import static com.philips.cdp.registration.configuration.URConfigurationConstants.HSDP_CONFIGURATION_SHARED;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class HSDPConfigurationTest extends TestCase {

    @Mock
    AppInfraWrapper appInfraWrapperMock;

    @Mock
    RegistrationComponent component;

    private HSDPConfiguration hsdpConfiguration;

    @Before
    public void setUp() throws Exception {
        RegistrationConfiguration.getInstance().setComponent(component);
        hsdpConfiguration = new HSDPConfiguration();
        hsdpConfiguration.setAppInfraWrapper(appInfraWrapperMock);
    }

    @After
    public void tearDown() throws Exception {
        hsdpConfiguration = null;
        appInfraWrapperMock = null;
        component = null;
    }

    @Test
    public void testGetHsdpAppName() throws Exception {
        when(appInfraWrapperMock.getURProperty(HSDP_CONFIGURATION_APPLICATION_NAME)).thenReturn("hsdp_app_name");
        String hsdpAppName = hsdpConfiguration.getHsdpAppName();
        assertEquals("hsdp_app_name", hsdpAppName);
    }

    @Test
    public void testGetHsdpSharedId() throws Exception {
        when(appInfraWrapperMock.getURProperty(HSDP_CONFIGURATION_SHARED)).thenReturn("hsdp_shared_id");
        String hsdpSharedId = hsdpConfiguration.getHsdpSharedId();
        assertEquals("hsdp_shared_id", hsdpSharedId);
    }

    @Test
    public void testGetHsdpSecretId() throws Exception {
        when(appInfraWrapperMock.getURProperty(HSDP_CONFIGURATION_SECRET)).thenReturn("hsdp_secret");
        String hsdpSecretId = hsdpConfiguration.getHsdpSecretId();
        assertEquals("hsdp_secret", hsdpSecretId);
    }

    @Test
    public void testGetHsdpBaseUrl_SetInConfiguration() throws Exception {
        String baseUrlEncoded = URLEncoder.encode("http://www.philips.com/configuration", "UTF-8");
        when(appInfraWrapperMock.getURProperty(HSDP_CONFIGURATION_BASE_URL)).thenReturn(baseUrlEncoded);
        String hsdpBaseUrl = hsdpConfiguration.getHsdpBaseUrl();
        assertEquals("http://www.philips.com/configuration", hsdpBaseUrl);
    }

    @Test
    public void testGetHsdpBaseUrl_SetFromServiceDiscovery() throws Exception {
        hsdpConfiguration.setBaseUrlServiceDiscovery("http://www.philips.com/serviceDiscovery");
        when(appInfraWrapperMock.getURProperty(HSDP_CONFIGURATION_BASE_URL)).thenReturn(null);
        String hsdpBaseUrl = hsdpConfiguration.getHsdpBaseUrl();
        assertEquals("http://www.philips.com/serviceDiscovery", hsdpBaseUrl);
    }

    @Test
    public void testGetHsdpBaseUrl_SingleUrl() throws Exception {
        when(appInfraWrapperMock.getURProperty(HSDP_CONFIGURATION_BASE_URL)).thenReturn("http://www.philips.com/configuration");
        String hsdpBaseUrl = hsdpConfiguration.getHsdpBaseUrl();
        assertEquals("http://www.philips.com/configuration", hsdpBaseUrl);
    }

}