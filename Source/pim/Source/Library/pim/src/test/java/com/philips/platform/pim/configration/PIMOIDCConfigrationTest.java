package com.philips.platform.pim.configration;

import com.philips.platform.appinfra.AppInfraInterface;
import com.philips.platform.appinfra.appconfiguration.AppConfigurationInterface;
import com.philips.platform.appinfra.logging.LoggingInterface;
import com.philips.platform.pim.manager.PIMSettingManager;

import junit.framework.TestCase;

import net.openid.appauth.AuthorizationServiceConfiguration;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.HashMap;
import java.util.Map;

import static com.philips.platform.appinfra.logging.LoggingInterface.LogLevel.DEBUG;
import static org.mockito.Mockito.verify;
import static org.powermock.api.mockito.PowerMockito.mock;
import static org.powermock.api.mockito.PowerMockito.mockStatic;
import static org.powermock.api.mockito.PowerMockito.when;
import static org.powermock.api.mockito.PowerMockito.whenNew;

@PrepareForTest({PIMOIDCConfigration.class, PIMSettingManager.class})
@RunWith(PowerMockRunner.class)
public class PIMOIDCConfigrationTest extends TestCase {

    @Mock
    private AppInfraInterface mockAppInfraInterface;
    @Mock
    private AppConfigurationInterface mockAppConfigurationInterface;
    @Mock
    private AppConfigurationInterface.AppConfigurationError mockAppConfigurationError;
    @Mock
    private PIMSettingManager mockPimSettingManager;

    private static String TAG = PIMOIDCConfigration.class.getSimpleName();
    private PIMOIDCConfigration pimoidcConfigration;

    @Before
    public void setUp() throws Exception {
        super.setUp();
        MockitoAnnotations.initMocks(this);
        mockStatic(PIMSettingManager.class);
        when(PIMSettingManager.getInstance()).thenReturn(mockPimSettingManager);
        when(mockPimSettingManager.getAppInfraInterface()).thenReturn(mockAppInfraInterface);
        when(mockAppInfraInterface.getConfigInterface()).thenReturn(mockAppConfigurationInterface);
        whenNew(AppConfigurationInterface.AppConfigurationError.class).withNoArguments().thenReturn(mockAppConfigurationError);
        pimoidcConfigration = new PIMOIDCConfigration();
    }

    @Test
    public void testGetAuthorizationServiceConfiguration() {
        AuthorizationServiceConfiguration mockAuthorizationServiceConfiguration = mock(AuthorizationServiceConfiguration.class);
        PIMOIDCConfigration pimoidcConfigration = new PIMOIDCConfigration(mockAuthorizationServiceConfiguration);
        AuthorizationServiceConfiguration authServiceConfiguration = pimoidcConfigration.getAuthorizationServiceConfiguration();
        assertNotNull(authServiceConfiguration);
        assertSame(mockAuthorizationServiceConfiguration, authServiceConfiguration);
    }

    @Test
    public void testGetRSID() {
        when(mockAppConfigurationInterface.getPropertyForKey("rsid", "PIM", mockAppConfigurationError)).thenReturn("philipspimregistrationdev");
        String rsid = pimoidcConfigration.getrsID();
        assertNotNull(rsid);
        assertEquals("philipspimregistrationdev", rsid);
    }

    @Test
    public void testGetRSIDReturnsNull() {
        String rsid = pimoidcConfigration.getrsID();
        assertNull(rsid);
    }

    @Test
    public void testGetDefaultClientID() {
        when(mockAppConfigurationInterface.getPropertyForKey("PIM.default", "PIM", mockAppConfigurationError)).thenReturn(getDefaultConfigurationResponse());
        String clientId = pimoidcConfigration.getClientId();
        assertNotNull(clientId);
        assertEquals("94e28300-565d-4110-8919-42dc4f817393", clientId);
    }

    @Test
    public void testGetDefaultClientIDReturnsNull() {
        String clientId = pimoidcConfigration.getClientId();
        assertNull(clientId);
    }

    @Test
    public void testGetDefaultRedirectUrl() {
        when(mockAppConfigurationInterface.getPropertyForKey("PIM.default", "PIM", mockAppConfigurationError)).thenReturn(getDefaultConfigurationResponse());
        String redirectUrl = pimoidcConfigration.getRedirectUrl();
        assertNotNull(redirectUrl);
        assertEquals("com.philips.apps.94e28300-565d-4110-8919-42dc4f817393://oauthredirect", redirectUrl);
    }

    @Test
    public void testGetRedirectUrlreturnsNull() {
        String redirectUrl = pimoidcConfigration.getRedirectUrl();
        assertNull(redirectUrl);
    }

    @Test
    public void testGetURClientID() {
        when(mockAppConfigurationInterface.getPropertyForKey("JanRainConfiguration.RegistrationClientID", "PIM", mockAppConfigurationError)).thenReturn(getURConfigurationResponse());
        String urClientId = pimoidcConfigration.getURClientId();
        assertNotNull(urClientId);
        assertEquals("f2stykcygm7enbwfw2u9fbg6h6syb8yd", urClientId);
    }

    public void testURClientIDReturnsNull() {
        String urClientId = pimoidcConfigration.getURClientId();
        assertNull(urClientId);
    }

    @Test
    public void testGetMigrationClientID() {
        when(mockAppConfigurationInterface.getPropertyForKey("PIM.migration", "PIM", mockAppConfigurationError)).thenReturn(getMigrationConfigResponse());
        String migrationClientId = pimoidcConfigration.getMigrationClientId();
        assertNotNull(migrationClientId);
        assertEquals("7602c06b-c547-4aae-8f7c-f89e8c887a21", migrationClientId);
    }

    @Test
    public void testGetMigrationClientIdNull() {
        String migrationClientId = pimoidcConfigration.getMigrationClientId();
        assertNull(migrationClientId);
    }


    @Test
    public void testGetMigrationRedirectUrl() {
        when(mockAppConfigurationInterface.getPropertyForKey("PIM.migration", "PIM", mockAppConfigurationError)).thenReturn(getMigrationConfigResponse());
        String redirectUrl = pimoidcConfigration.getMigrationRedirectUrl();
        assertNotNull(redirectUrl);
        assertEquals("com.philips.apps.7602c06b-c547-4aae-8f7c-f89e8c887a21://oauthredirect", redirectUrl);
    }

    @Test
    public void testGetMigrationRedirectUrlReturnsNull() {
        String redirectUrl = pimoidcConfigration.getMigrationRedirectUrl();
        assertNull(redirectUrl);
    }

    @Test
    public void testGetCustomClaims() {
        LoggingInterface mockLoggingInterface = mock(LoggingInterface.class);
        when(mockPimSettingManager.getLoggingInterface()).thenReturn(mockLoggingInterface);
        String jsonString = pimoidcConfigration.getCustomClaims();
        assertNotNull(jsonString);
        verify(mockLoggingInterface).log(DEBUG, TAG, "PIM_KEY_CUSTOM_CLAIMS: " + jsonString);
    }

    private Map getDefaultConfigurationResponse() {
        Map<String, String> defaulrtConfig = new HashMap<>(2);
        defaulrtConfig.put("clientId", "94e28300-565d-4110-8919-42dc4f817393");
        defaulrtConfig.put("redirectURL", "com.philips.apps.94e28300-565d-4110-8919-42dc4f817393://oauthredirect");
        return defaulrtConfig;
    }

    private Map getURConfigurationResponse() {
        Map<String, String> urConfiguration = new HashMap<>(2);
        urConfiguration.put("CN", "4rdpm7afu7bny6xnacw32etmt7htfraa");
        urConfiguration.put("default", "f2stykcygm7enbwfw2u9fbg6h6syb8yd");
        return urConfiguration;
    }

    private Map getMigrationConfigResponse() {
        Map<String, String> migrationClients = new HashMap<>();
        migrationClients.put("clientId", "7602c06b-c547-4aae-8f7c-f89e8c887a21");
        migrationClients.put("redirectURL", "com.philips.apps.7602c06b-c547-4aae-8f7c-f89e8c887a21://oauthredirect");
        return migrationClients;
    }

    @After
    public void tearDown() throws Exception {
        mockAppInfraInterface = null;
        mockAppConfigurationInterface = null;
    }
}