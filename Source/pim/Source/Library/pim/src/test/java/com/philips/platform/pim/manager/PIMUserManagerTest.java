package com.philips.platform.pim.manager;

import android.content.Context;
import android.content.SharedPreferences;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.philips.platform.appinfra.AppInfraInterface;
import com.philips.platform.appinfra.appconfiguration.AppConfigurationInterface;
import com.philips.platform.appinfra.logging.LoggingInterface;
import com.philips.platform.appinfra.rest.RestInterface;
import com.philips.platform.appinfra.rest.request.RequestQueue;
import com.philips.platform.appinfra.securestorage.SecureStorageInterface;
import com.philips.platform.pif.DataInterface.USR.enums.Error;
import com.philips.platform.pif.DataInterface.USR.listeners.LogoutSessionListener;
import com.philips.platform.pif.DataInterface.USR.listeners.RefreshSessionListener;
import com.philips.platform.pim.configration.PIMOIDCConfigration;
import com.philips.platform.pim.rest.LogoutRequest;
import com.philips.platform.pim.rest.PIMRestClient;

import junit.framework.TestCase;

import net.openid.appauth.AuthState;
import net.openid.appauth.AuthorizationRequest;
import net.openid.appauth.AuthorizationResponse;
import net.openid.appauth.AuthorizationServiceConfiguration;
import net.openid.appauth.AuthorizationServiceDiscovery;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.reflect.Whitebox;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.powermock.api.mockito.PowerMockito.mockStatic;
import static org.powermock.api.mockito.PowerMockito.whenNew;

//TODO: Shashi, Add test cases
@PrepareForTest({PIMSettingManager.class, PIMOIDCConfigration.class, PIMUserManager.class})
@RunWith(PowerMockRunner.class)
public class PIMUserManagerTest extends TestCase {

    private PIMUserManager pimUserManager;
    @Mock
    Context mockContext;
    @Mock
    AppInfraInterface mockAppInfraInterface;
    @Mock
    PIMSettingManager mockPimSettingManager;
    @Mock
    LoggingInterface mockLoggingInterface;
    @Mock
    SharedPreferences mockSharedPreferences;
    @Mock
    AuthState mockAuthState;
    @Mock
    PIMRestClient mockPimRestClient;

    @Captor
    ArgumentCaptor<Response.Listener> responseArgumentCaptor;
    @Captor
    ArgumentCaptor<Response.ErrorListener> errorArgumentCaptor;


    private PIMUserManager userManager;


    public void setUp() throws Exception {
        super.setUp();
        MockitoAnnotations.initMocks(this);

        mockStatic(PIMSettingManager.class);
        RestInterface mockRestInterface = mock(RestInterface.class);

        when(PIMSettingManager.getInstance()).thenReturn(mockPimSettingManager);
        when(mockPimSettingManager.getAppInfraInterface()).thenReturn(mockAppInfraInterface);
        when(mockPimSettingManager.getLoggingInterface()).thenReturn(mockLoggingInterface);
        when(mockContext.getSharedPreferences("PIM_PREF", Context.MODE_PRIVATE)).thenReturn(mockSharedPreferences);
        PowerMockito.when(mockPimSettingManager.getRestClient()).thenReturn(mockRestInterface);
        PowerMockito.when(mockRestInterface.getRequestQueue()).thenReturn(mock(RequestQueue.class));

        whenNew(PIMRestClient.class).withArguments(mockRestInterface).thenReturn(mockPimRestClient);

        pimUserManager = new PIMUserManager();
        pimUserManager.init(mockContext, mockAppInfraInterface);
    }

    @Test
    public void testInit() {
        pimUserManager.init(mockContext, mockAppInfraInterface);
    }

    @Test
    public void refreshSession(){
        RefreshSessionListener mockRefreshSessionListener = mock(RefreshSessionListener.class);
        pimUserManager.refreshSession(mockRefreshSessionListener);
    }

    @Test
    public void testLogoutSession() throws Exception {
        AppConfigurationInterface mockConfigurationInterface = mock(AppConfigurationInterface.class);
        AppConfigurationInterface.AppConfigurationError mockConfigurationError = mock(AppConfigurationInterface.AppConfigurationError.class);
        PIMOIDCConfigration mockPimoidcConfigration = mock(PIMOIDCConfigration.class);
        LogoutSessionListener mockLogoutListener = mock(LogoutSessionListener.class);
        LogoutRequest mockLogoutRequest = mock(LogoutRequest.class);
        SecureStorageInterface mockStorageInterface = mock(SecureStorageInterface.class);

        whenNew(AppConfigurationInterface.AppConfigurationError.class).withNoArguments().thenReturn(mockConfigurationError);
        when(mockAppInfraInterface.getConfigInterface()).thenReturn(mockConfigurationInterface);
        when(mockConfigurationInterface.getPropertyForKey("PIM.default", "PIM", mockConfigurationError)).thenReturn(new Object());
        when(mockPimoidcConfigration.getClientId()).thenReturn("94e28300-565d-4110-8919-42dc4f817393");
        whenNew(PIMOIDCConfigration.class).withNoArguments().thenReturn(mockPimoidcConfigration);
        when(mockSharedPreferences.getString("LOGIN_FLOW", PIMUserManager.LOGIN_FLOW.DEFAULT.toString())).thenReturn(PIMUserManager.LOGIN_FLOW.DEFAULT.toString());
        when(mockAppInfraInterface.getSecureStorage()).thenReturn(mockStorageInterface);

        AuthorizationResponse mockAuthorizationResponse = mock(AuthorizationResponse.class);
        AuthorizationRequest mockAuthorizationRequest = mock(AuthorizationRequest.class);
        AuthorizationServiceConfiguration mockAuthorizationServiceConfiguration = mock(AuthorizationServiceConfiguration.class);
        AuthorizationServiceDiscovery mockAuthorizationServiceDiscovery = mock(AuthorizationServiceDiscovery.class);

        when(mockAuthState.getLastAuthorizationResponse()).thenReturn(mockAuthorizationResponse);
        SharedPreferences.Editor editor = mock(SharedPreferences.Editor.class);
        when(mockSharedPreferences.edit()).thenReturn(editor);

        Whitebox.setInternalState(pimUserManager, "authState", mockAuthState);
        Whitebox.setInternalState(mockAuthorizationResponse, "request", mockAuthorizationRequest);
        Whitebox.setInternalState(mockAuthorizationRequest, "configuration", mockAuthorizationServiceConfiguration);
        Whitebox.setInternalState(mockAuthorizationServiceConfiguration, "discoveryDoc", mockAuthorizationServiceDiscovery);
        when(mockAuthorizationServiceDiscovery.getIssuer()).thenReturn("IssuerUrl"); //TODO:Shashi, need to update url later

        whenNew(LogoutRequest.class).withArguments(mockAuthState, "94e28300-565d-4110-8919-42dc4f817393").thenReturn(mockLogoutRequest);
        pimUserManager.logoutSession(mockLogoutListener);
        verify(mockPimRestClient).invokeRequest(eq(mockLogoutRequest), responseArgumentCaptor.capture(), errorArgumentCaptor.capture());

        Response.Listener reponselistener = responseArgumentCaptor.getValue();
        reponselistener.onResponse("ResponseString");
        verify(mockLogoutListener).logoutSessionSuccess();

        Response.ErrorListener errorListener = errorArgumentCaptor.getValue();
        errorListener.onErrorResponse(any(VolleyError.class));
        verify(mockLogoutListener).logoutSessionFailed(any(Error.class));
    }

    @Test
    public void testLogoutMigratedSession() throws Exception {
        AppConfigurationInterface mockConfigurationInterface = mock(AppConfigurationInterface.class);
        AppConfigurationInterface.AppConfigurationError mockConfigurationError = mock(AppConfigurationInterface.AppConfigurationError.class);
        PIMOIDCConfigration mockPimoidcConfigration = mock(PIMOIDCConfigration.class);
        LogoutSessionListener mockLogoutListener = mock(LogoutSessionListener.class);
        LogoutRequest mockLogoutRequest = mock(LogoutRequest.class);
        SecureStorageInterface mockStorageInterface = mock(SecureStorageInterface.class);

        whenNew(AppConfigurationInterface.AppConfigurationError.class).withNoArguments().thenReturn(mockConfigurationError);
        when(mockAppInfraInterface.getConfigInterface()).thenReturn(mockConfigurationInterface);
        when(mockConfigurationInterface.getPropertyForKey("PIM.default", "PIM", mockConfigurationError)).thenReturn(new Object());
        when(mockPimoidcConfigration.getMigrationClientId()).thenReturn("7602c06b-c547-4aae-8f7c-f89e8c887a21");
        whenNew(PIMOIDCConfigration.class).withNoArguments().thenReturn(mockPimoidcConfigration);
        when(mockSharedPreferences.getString("LOGIN_FLOW", PIMUserManager.LOGIN_FLOW.DEFAULT.toString())).thenReturn(PIMUserManager.LOGIN_FLOW.MIGRATION.toString());
        when(mockAppInfraInterface.getSecureStorage()).thenReturn(mockStorageInterface);

        AuthorizationResponse mockAuthorizationResponse = mock(AuthorizationResponse.class);
        AuthorizationRequest mockAuthorizationRequest = mock(AuthorizationRequest.class);
        AuthorizationServiceConfiguration mockAuthorizationServiceConfiguration = mock(AuthorizationServiceConfiguration.class);
        AuthorizationServiceDiscovery mockAuthorizationServiceDiscovery = mock(AuthorizationServiceDiscovery.class);

        when(mockAuthState.getLastAuthorizationResponse()).thenReturn(mockAuthorizationResponse);
        SharedPreferences.Editor editor = mock(SharedPreferences.Editor.class);
        when(mockSharedPreferences.edit()).thenReturn(editor);

        Whitebox.setInternalState(pimUserManager, "authState", mockAuthState);
        Whitebox.setInternalState(mockAuthorizationResponse, "request", mockAuthorizationRequest);
        Whitebox.setInternalState(mockAuthorizationRequest, "configuration", mockAuthorizationServiceConfiguration);
        Whitebox.setInternalState(mockAuthorizationServiceConfiguration, "discoveryDoc", mockAuthorizationServiceDiscovery);
        when(mockAuthorizationServiceDiscovery.getIssuer()).thenReturn("IssuerUrl"); //TODO:Shashi, need to update url later

        whenNew(LogoutRequest.class).withArguments(mockAuthState, "7602c06b-c547-4aae-8f7c-f89e8c887a21").thenReturn(mockLogoutRequest);
        pimUserManager.logoutSession(mockLogoutListener);
        verify(mockPimRestClient).invokeRequest(eq(mockLogoutRequest), responseArgumentCaptor.capture(), errorArgumentCaptor.capture());

        Response.Listener reponselistener = responseArgumentCaptor.getValue();
        reponselistener.onResponse("ResponseString");
        verify(mockLogoutListener).logoutSessionSuccess();

        Response.ErrorListener errorListener = errorArgumentCaptor.getValue();
        errorListener.onErrorResponse(any(VolleyError.class));
        verify(mockLogoutListener).logoutSessionFailed(any(Error.class));
    }

    @Test
    public void testSaveLoginFlow() {
        SharedPreferences.Editor editor = mock(SharedPreferences.Editor.class);
        when(mockSharedPreferences.edit()).thenReturn(editor);
        pimUserManager.saveLoginFlowType(PIMUserManager.LOGIN_FLOW.DEFAULT);
        verify(editor).apply();
    }

    @Test
    public void testGetLoginFlow() {
        when(mockSharedPreferences.getString("LOGIN_FLOW", PIMUserManager.LOGIN_FLOW.DEFAULT.toString())).thenReturn(PIMUserManager.LOGIN_FLOW.DEFAULT.toString());
        PIMUserManager.LOGIN_FLOW loginFlow = pimUserManager.getLoginFlow();
        assertSame(PIMUserManager.LOGIN_FLOW.DEFAULT, loginFlow);
    }

    public void tearDown() throws Exception {
    }
}