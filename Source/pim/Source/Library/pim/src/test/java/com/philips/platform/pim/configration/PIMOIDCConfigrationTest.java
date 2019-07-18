package com.philips.platform.pim.configration;

import com.philips.platform.appinfra.AppInfraInterface;
import com.philips.platform.appinfra.appconfiguration.AppConfigurationInterface;
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

import static org.powermock.api.mockito.PowerMockito.mockStatic;
import static org.powermock.api.mockito.PowerMockito.when;
import static org.powermock.api.mockito.PowerMockito.whenNew;

@PrepareForTest({PIMOIDCConfigration.class, PIMSettingManager.class})
@RunWith(PowerMockRunner.class)
public class PIMOIDCConfigrationTest extends TestCase {

    @Mock
    private AuthorizationServiceConfiguration mockAuthServiceConfiguration;
    @Mock
    private AppInfraInterface mockAppInfraInterface;
    @Mock
    private AppConfigurationInterface mockAppConfigurationInterface;
    private static final String CLIENT_ID = "clientId";
    private static final String RSID = "rsid";
    @Mock
    private AppConfigurationInterface.AppConfigurationError mockAppConfigurationError;
    @Mock
    private PIMSettingManager mockPimSettingManager;

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

        pimoidcConfigration = new PIMOIDCConfigration(mockAuthServiceConfiguration);
    }

    @Test
    public void testGetAuthorizationServiceConfiguration(){
        AuthorizationServiceConfiguration authServiceConfiguration =  pimoidcConfigration.getAuthorizationServiceConfiguration();
        assertSame(mockAuthServiceConfiguration,authServiceConfiguration);
        assertEquals(mockAuthServiceConfiguration,authServiceConfiguration);
    }


    @Test
    public void shouldNotNull_AuthorizationServiceConfiguration() {
        PIMOIDCConfigration pimoidcConfigration = new PIMOIDCConfigration(mockAuthServiceConfiguration);
        AuthorizationServiceConfiguration authorizationServiceConfiguration = pimoidcConfigration.getAuthorizationServiceConfiguration();
        assertNotNull(authorizationServiceConfiguration);
    }

    @Test
    public void shouldNotNullClientId() {
        PIMOIDCConfigration pimoidcConfigration = new PIMOIDCConfigration();
        when(mockAppConfigurationInterface.getPropertyForKey(CLIENT_ID, "PIM", mockAppConfigurationError)).thenReturn("clientId");
        String clientID = pimoidcConfigration.getClientId();
        assertEquals(CLIENT_ID, clientID);

    }

    @Test
    public void shouldGetClientID_Correct_ClientID_Wrong_GroupName() {
        PIMOIDCConfigration pimoidcConfigration = new PIMOIDCConfigration();
        when(mockAppConfigurationInterface.getPropertyForKey(CLIENT_ID, "Dummy_Group", mockAppConfigurationError)).thenReturn("clientId");
        String clientID = pimoidcConfigration.getClientId();
        assertNull(clientID);
    }

    @Test
    public void shouldGetClientID_Wrong_ClientID_Correct_GroupName() {
        PIMOIDCConfigration pimoidcConfigration = new PIMOIDCConfigration();
        when(mockAppConfigurationInterface.getPropertyForKey("Dummy_ClientID", "PIM", mockAppConfigurationError)).thenReturn("clientId");
        String clientID = pimoidcConfigration.getClientId();
        assertNull(clientID);
    }

    @Test
    public void shouldGetClientID_Wromg_ClientID_Wrong_GroupName() {
        PIMOIDCConfigration pimoidcConfigration = new PIMOIDCConfigration();
        when(mockAppConfigurationInterface.getPropertyForKey("Dummy_ClientID", "Dummy_Group", mockAppConfigurationError)).thenReturn("clientId");
        String clientID = pimoidcConfigration.getClientId();
        assertNull(clientID);
    }


    @Test
    public void shouldNotNullRsiId() {
        PIMOIDCConfigration pimoidcConfigration = new PIMOIDCConfigration();
        when(mockAppConfigurationInterface.getPropertyForKey(RSID, "PIM", mockAppConfigurationError)).thenReturn("rsid");
        String rsid = pimoidcConfigration.getrsID();
        assertEquals(RSID, rsid);

    }

    @Test
    public void shouldGetGetRsiId_Correct_ClientID_Wrong_GroupName() {
        PIMOIDCConfigration pimoidcConfigration = new PIMOIDCConfigration();
        when(mockAppConfigurationInterface.getPropertyForKey(RSID, "Dummy_Group", mockAppConfigurationError)).thenReturn("rsid");
        String rsid = pimoidcConfigration.getrsID();
        assertNull(rsid);
    }

    @Test
    public void shouldGetRsiId_Wrong_ClientID_Correct_GroupName() {
        PIMOIDCConfigration pimoidcConfigration = new PIMOIDCConfigration();
        when(mockAppConfigurationInterface.getPropertyForKey("Dummy_RssId", "PIM", mockAppConfigurationError)).thenReturn("rsid");
        String rsid = pimoidcConfigration.getrsID();
        assertNull(rsid);
    }

    @Test
    public void shouldGetRsiId_Wromg_ClientID_Wrong_GroupName() {
        PIMOIDCConfigration pimoidcConfigration = new PIMOIDCConfigration();
        when(mockAppConfigurationInterface.getPropertyForKey("Dummy_RssID", "Dummy_Group", mockAppConfigurationError)).thenReturn("rsid");
        String rsid = pimoidcConfigration.getrsID();
        assertNull(rsid);
    }


    @After
    public void tearDown() throws Exception {
        mockAuthServiceConfiguration = null;
        mockAppInfraInterface = null;
        mockAppConfigurationInterface = null;
    }
}