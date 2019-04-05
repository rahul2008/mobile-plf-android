package com.philips.platform.pim.manager;

import com.philips.platform.appinfra.logging.LoggingInterface;
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
import org.mockito.MockitoAnnotations;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.powermock.api.mockito.PowerMockito.mockStatic;
import static org.powermock.api.mockito.PowerMockito.when;

@PrepareForTest({PIMSettingManager.class})
@RunWith(PowerMockRunner.class)
public class PIMOidcDiscoveryManagerTest extends TestCase {

    private PIMOidcDiscoveryManager pimOidcDiscoveryManager;

    @Mock
    private LoggingInterface mockLoggingInterface;
    @Mock
    private PIMAuthManager mockPimAuthManager;
    @Mock
    private PIMAuthorizationServiceConfigurationListener mockServiceConfigurationListener;
    @Mock
    private PIMSettingManager mockPimSettingManager;
    @Captor
    ArgumentCaptor<PIMAuthorizationServiceConfigurationListener> captorListener;
    String baseurl = "https://stg.api.accounts.philips.com/c2a48310-9715-3beb-895e-000000000000/login";

    @Before
    public void setUp() throws Exception {
        super.setUp();
        MockitoAnnotations.initMocks(this);

        mockStatic(PIMSettingManager.class);
        when(PIMSettingManager.getInstance()).thenReturn(mockPimSettingManager);
        when(mockPimSettingManager.getLoggingInterface()).thenReturn(mockLoggingInterface);

        pimOidcDiscoveryManager = new PIMOidcDiscoveryManager(mockPimAuthManager);
    }

    @Test
    public void downloadOidUrlTest_onSuccess(){
        pimOidcDiscoveryManager.downloadOidcUrls(baseurl);
        verify(mockPimAuthManager).fetchAuthWellKnownConfiguration(any(String.class),captorListener.capture());
        mockServiceConfigurationListener = captorListener.getValue();
        AuthorizationServiceDiscovery mockAuthServiceDiscovery = mock(AuthorizationServiceDiscovery.class);

        mockServiceConfigurationListener.onSuccess(mockAuthServiceDiscovery);
    }

    @Test
    public void downloadOidUrlTest_onError(){
        pimOidcDiscoveryManager.downloadOidcUrls(baseurl);
        verify(mockPimAuthManager).fetchAuthWellKnownConfiguration(any(String.class),captorListener.capture());

        mockServiceConfigurationListener = captorListener.getValue();

         mockServiceConfigurationListener.onError();
    }

    @After
    public void tearDown() throws Exception {
        pimOidcDiscoveryManager = null;
        mockServiceConfigurationListener = null;
        mockPimAuthManager = null;
        captorListener = null;
    }
}