package com.philips.platform.pim.manager;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import com.philips.platform.appinfra.logging.LoggingInterface;
import com.philips.platform.pim.configration.PIMOIDCConfigration;
import com.philips.platform.pim.fragment.PIMFragment;
import com.philips.platform.pim.listeners.PIMAuthorizationServiceConfigurationListener;

import junit.framework.TestCase;

import net.openid.appauth.AuthorizationException;
import net.openid.appauth.AuthorizationRequest;
import net.openid.appauth.AuthorizationResponse;
import net.openid.appauth.AuthorizationService;
import net.openid.appauth.AuthorizationServiceConfiguration;
import net.openid.appauth.AuthorizationServiceDiscovery;
import net.openid.appauth.TokenRequest;
import net.openid.appauth.TokenResponse;

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

import static com.philips.platform.appinfra.logging.LoggingInterface.LogLevel.DEBUG;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.powermock.api.mockito.PowerMockito.mockStatic;
import static org.powermock.api.mockito.PowerMockito.when;
import static org.powermock.api.mockito.PowerMockito.whenNew;

@PrepareForTest({Uri.class,AuthorizationServiceConfiguration.class,PIMSettingManager.class,PIMAuthManager.class,AuthorizationResponse.class})
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

    @Mock
    private AuthorizationService.TokenResponseCallback mockTokenResponseCallback;
    @Captor
    private ArgumentCaptor<AuthorizationService.TokenResponseCallback> captorTokenResponse;
    @Mock
    private AuthorizationService mockAuthorizationService;
    @Mock
    private AuthorizationResponse mockAuthorizationResponse;
    @Mock
    private Context mockContext;
    @Mock
    private TokenRequest mockTokenRequest;
    @Mock
    private PIMOIDCConfigration mockPimoidcConfigration;


    private String baseurl = "https://stg.api.accounts.philips.com/c2a48310-9715-3beb-895e-000000000000/login";

    public void setUp() throws Exception {
        super.setUp();
        MockitoAnnotations.initMocks(this);

        mockStatic(PIMSettingManager.class);
        when(PIMSettingManager.getInstance()).thenReturn(mockPimSettingManager);
        when(mockPimSettingManager.getLoggingInterface()).thenReturn(mockLoggingInterface);
        when(mockPimSettingManager.getPimOidcConfigration()).thenReturn(mockPimoidcConfigration);

        mockStatic(AuthorizationServiceConfiguration.class);
        mockAuthorizationServiceConfiguration = mock(AuthorizationServiceConfiguration.class);
        when(mockPimoidcConfigration.getAuthorizationServiceConfiguration()).thenReturn(mockAuthorizationServiceConfiguration);
        mockStatic(Uri.class);
        Uri uri = mock(Uri.class);
        when(Uri.class,"parse", ArgumentMatchers.anyString()).thenReturn(uri);

        whenNew(AuthorizationService.class).withArguments(mockContext).thenReturn(mockAuthorizationService);

        pimAuthManager = new PIMAuthManager();
    }

    @Test
    public void shouldFetchFromUrl_Verify_OnSuccess() throws AuthorizationServiceDiscovery.MissingArgumentException, JSONException {
        pimAuthManager.fetchAuthWellKnownConfiguration(baseurl,mockConfigurationListener);

        PowerMockito.verifyStatic(AuthorizationServiceConfiguration.class);
        AuthorizationServiceConfiguration.fetchFromUrl(ArgumentMatchers.any(Uri.class),captorRetrieveConfigCallback.capture());

        mockConfigurationCallback = captorRetrieveConfigCallback.getValue();
        mockConfigurationCallback.onFetchConfigurationCompleted(mockAuthorizationServiceConfiguration,null);

        verify(mockConfigurationListener).onSuccess(mockAuthorizationServiceConfiguration);
        verify(mockLoggingInterface).log(DEBUG,PIMAuthManager.class.getSimpleName(),"fetchAuthWellKnownConfiguration : Configuration retrieved for  proceeding : "+mockAuthorizationServiceConfiguration);
    }


    @Test
    public void shouldFetchFromUrl_Verify_OnError()throws AuthorizationServiceDiscovery.MissingArgumentException, JSONException {
        pimAuthManager.fetchAuthWellKnownConfiguration(baseurl,mockConfigurationListener);

        PowerMockito.verifyStatic(AuthorizationServiceConfiguration.class);
        AuthorizationServiceConfiguration.fetchFromUrl(ArgumentMatchers.any(Uri.class),captorRetrieveConfigCallback.capture());

        mockConfigurationCallback = captorRetrieveConfigCallback.getValue();
        AuthorizationException ex = new AuthorizationException(0, 0, null, null, null, null);
        mockConfigurationCallback.onFetchConfigurationCompleted(mockAuthorizationServiceConfiguration, ex);
        verify(mockConfigurationListener).onError(ex.getMessage());
        verify(mockLoggingInterface).log(DEBUG,PIMAuthManager.class.getSimpleName(),"fetchAuthWellKnownConfiguration : Failed to retrieve configuration for : "+ex.getMessage());
    }

    @Test
    public void shouldPerformAuthRequest(){
        PIMFragment mockPimFragment = mock(PIMFragment.class);
        when(mockPimFragment.getContext()).thenReturn(mockContext);

        Intent mockIntent = mock(Intent.class);
        AuthorizationRequest mockAuthorizationRequest = mock(AuthorizationRequest.class);
        when(mockAuthorizationService.getAuthorizationRequestIntent(mockAuthorizationRequest)).thenReturn(mockIntent);
        pimAuthManager.makeAuthRequest(mockContext, mockPimoidcConfigration, mBundle);

        verify(mockPimFragment).startActivityForResult(any(Intent.class),any(Integer.class));
    }

    @Test
    public void shouldPerformTokenRequest() {
        when(mockAuthorizationResponse.createTokenExchangeRequest()).thenReturn(mockTokenRequest);
        pimAuthManager.performTokenRequest(mockContext,mockAuthorizationResponse,mockTokenResponseCallback);
        verify(mockAuthorizationService).performTokenRequest(any(TokenRequest.class),captorTokenResponse.capture());
        mockTokenResponseCallback = captorTokenResponse.getValue();
        mockTokenResponseCallback.onTokenRequestCompleted(any(TokenResponse.class),any(AuthorizationException.class));
    }

    public void tearDown() throws Exception {
        pimAuthManager = null;
        mockConfigurationListener = null;
        mockConfigurationCallback = null;
    }
}