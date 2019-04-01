package com.philips.platform.pim.manager;

import com.google.common.base.CharMatcher;
import com.philips.platform.pim.configration.PIMOIDCConfigration;
import com.philips.platform.pim.listeners.PIMAuthorizationServiceConfigurationListener;

import junit.framework.TestCase;

import net.openid.appauth.AuthorizationServiceDiscovery;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
public class PIMOidcDiscoveryManagerTest extends TestCase {

    private PIMOidcDiscoveryManager pimOidcDiscoveryManager;

    @Mock
    private PIMAuthManager mockPimAuthManager;
    @Mock
    private PIMSettingManager mockPIMSettingManager;
    @Mock
    private PIMAuthorizationServiceConfigurationListener mockServiceConfigurationListener;
    @Captor
    ArgumentCaptor<PIMAuthorizationServiceConfigurationListener> captorListener;
    @Mock
    PIMOIDCConfigration mockOidcConfigration;

    @Before
    public void setUp() throws Exception {
        super.setUp();
        MockitoAnnotations.initMocks(this);
        pimOidcDiscoveryManager = new PIMOidcDiscoveryManager(mockPimAuthManager);
    }

    @Test
    public void downloadOidUrlTest(){
        String baseurl = "https://stg.api.accounts.philips.com/c2a48310-9715-3beb-895e-000000000000/login";
        pimOidcDiscoveryManager.downloadOidcUrls(baseurl);
        verify(mockPimAuthManager).fetchAuthWellKnownConfiguration(any(String.class),captorListener.capture());
        mockServiceConfigurationListener = captorListener.getValue();
        AuthorizationServiceDiscovery mockAuthServiceDiscovery = mock(AuthorizationServiceDiscovery.class);
        mockServiceConfigurationListener.onSuccess(mockAuthServiceDiscovery);
        //verify(mockPIMSettingManager).setPimOidcConfigration(mockOidcConfigration);
    }

    @After
    public void tearDown() throws Exception {
        pimOidcDiscoveryManager = null;
        mockServiceConfigurationListener = null;
        mockPimAuthManager = null;
        mockPIMSettingManager = null;
        captorListener = null;
    }
}