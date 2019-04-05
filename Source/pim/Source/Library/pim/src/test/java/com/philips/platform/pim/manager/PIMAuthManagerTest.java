package com.philips.platform.pim.manager;

import android.net.Uri;

import com.philips.platform.appinfra.logging.LoggingInterface;
import com.philips.platform.pim.listeners.PIMAuthorizationServiceConfigurationListener;

import junit.framework.TestCase;

import net.openid.appauth.AuthorizationServiceConfiguration;
import net.openid.appauth.AuthorizationServiceDiscovery;

import org.json.JSONException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.ArgumentMatchers;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.mockito.Mockito.mock;
import static org.powermock.api.mockito.PowerMockito.mockStatic;
import static org.powermock.api.mockito.PowerMockito.when;

@PrepareForTest({Uri.class,AuthorizationServiceConfiguration.class,PIMSettingManager.class})
@RunWith(PowerMockRunner.class)
public class PIMAuthManagerTest extends TestCase {

    private PIMAuthManager pimAuthManager;


    private AuthorizationServiceConfiguration mockAuthorizationServiceConfiguration;

    @Mock
    private PIMAuthorizationServiceConfigurationListener mockConfigurationListener;
    @Mock
    private AuthorizationServiceDiscovery mockAuthorizationServiceDiscovery;
    @Mock
    private PIMSettingManager mockPimSettingManager;
    @Mock
    private LoggingInterface mockLoggingInterface;

    @Mock
    private AuthorizationServiceConfiguration.RetrieveConfigurationCallback mockConfigurationCallback;
    @Captor
    private ArgumentCaptor<AuthorizationServiceConfiguration.RetrieveConfigurationCallback> captorRetrieveConfigCallback;
    private String baseurl = "https://stg.api.accounts.philips.com/c2a48310-9715-3beb-895e-000000000000/login";

    public void setUp() throws Exception {
        super.setUp();
        MockitoAnnotations.initMocks(this);

        mockStatic(PIMSettingManager.class);
        when(PIMSettingManager.getInstance()).thenReturn(mockPimSettingManager);
        when(mockPimSettingManager.getLoggingInterface()).thenReturn(mockLoggingInterface);

        PowerMockito.mockStatic(AuthorizationServiceConfiguration.class);
        mockAuthorizationServiceConfiguration = mock(AuthorizationServiceConfiguration.class);
        PowerMockito.mockStatic(Uri.class);
        Uri uri = mock(Uri.class);
        when(Uri.class,"parse", ArgumentMatchers.anyString()).thenReturn(uri);

        pimAuthManager = new PIMAuthManager();
    }

    @Test
    public void fetchFromUrl_testOnSuccess_AuthServiceNotNull() throws AuthorizationServiceDiscovery.MissingArgumentException, JSONException {
        pimAuthManager.fetchAuthWellKnownConfiguration(baseurl,mockConfigurationListener);

        PowerMockito.verifyStatic(AuthorizationServiceConfiguration.class);
        AuthorizationServiceConfiguration.fetchFromUrl(ArgumentMatchers.any(Uri.class),captorRetrieveConfigCallback.capture());

        mockConfigurationCallback = captorRetrieveConfigCallback.getValue();

        mockConfigurationListener.onSuccess(mockAuthorizationServiceDiscovery);
        assertNotNull(mockAuthorizationServiceDiscovery);
    }

    @Test
    public void fetchFromUrl_testOnSuccess_AuthServiceNull() throws AuthorizationServiceDiscovery.MissingArgumentException, JSONException {
        pimAuthManager.fetchAuthWellKnownConfiguration(baseurl,mockConfigurationListener);

        PowerMockito.verifyStatic(AuthorizationServiceConfiguration.class);
        AuthorizationServiceConfiguration.fetchFromUrl(ArgumentMatchers.any(Uri.class),captorRetrieveConfigCallback.capture());

        mockConfigurationCallback = captorRetrieveConfigCallback.getValue();

        mockConfigurationListener.onSuccess(mockAuthorizationServiceConfiguration.discoveryDoc);
        assertNull(mockAuthorizationServiceConfiguration.discoveryDoc);
    }

    @Test
    public void fetchFromUrl_testOnError(){
        pimAuthManager.fetchAuthWellKnownConfiguration(baseurl,mockConfigurationListener);

        PowerMockito.verifyStatic(AuthorizationServiceConfiguration.class);
        AuthorizationServiceConfiguration.fetchFromUrl(ArgumentMatchers.any(Uri.class),captorRetrieveConfigCallback.capture());

        mockConfigurationCallback = captorRetrieveConfigCallback.getValue();
        mockConfigurationListener.onError();
    }

    public void tearDown() throws Exception {
        pimAuthManager = null;
        mockConfigurationListener = null;
        mockConfigurationCallback = null;
    }
}