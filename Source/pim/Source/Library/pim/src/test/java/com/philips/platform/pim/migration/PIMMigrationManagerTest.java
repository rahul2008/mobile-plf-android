package com.philips.platform.pim.migration;

import android.content.Context;
import android.net.Uri;

import com.android.volley.Header;
import com.android.volley.NetworkResponse;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.philips.platform.appinfra.AppInfraInterface;
import com.philips.platform.appinfra.logging.LoggingInterface;
import com.philips.platform.appinfra.rest.RestInterface;
import com.philips.platform.appinfra.rest.request.RequestQueue;
import com.philips.platform.appinfra.servicediscovery.ServiceDiscoveryInterface;
import com.philips.platform.appinfra.servicediscovery.model.ServiceDiscoveryService;
import com.philips.platform.pif.DataInterface.USR.enums.Error;
import com.philips.platform.pim.PIMParameterToLaunchEnum;
import com.philips.platform.pim.configration.PIMOIDCConfigration;
import com.philips.platform.pim.errors.PIMErrorEnums;
import com.philips.platform.pim.listeners.PIMUserMigrationListener;
import com.philips.platform.pim.manager.PIMLoginManager;
import com.philips.platform.pim.manager.PIMSettingManager;
import com.philips.platform.pim.rest.IDAssertionRequest;
import com.philips.platform.pim.rest.PIMMigrationAuthRequest;
import com.philips.platform.pim.rest.PIMRestClient;

import junit.framework.TestCase;

import net.openid.appauth.AuthorizationRequest;

import org.json.JSONException;
import org.json.JSONObject;
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
import org.powermock.reflect.Whitebox;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.philips.platform.appinfra.logging.LoggingInterface.LogLevel.DEBUG;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.powermock.api.mockito.PowerMockito.mockStatic;
import static org.powermock.api.mockito.PowerMockito.spy;
import static org.powermock.api.mockito.PowerMockito.verifyPrivate;
import static org.powermock.api.mockito.PowerMockito.when;
import static org.powermock.api.mockito.PowerMockito.whenNew;

@PrepareForTest({PIMSettingManager.class, IDAssertionRequest.class, PIMMigrationManager.class, PIMLoginManager.class, PIMErrorEnums.class, PIMParameterToLaunchEnum.class})
@RunWith(PowerMockRunner.class)
public class PIMMigrationManagerTest extends TestCase {

    @Mock
    private PIMSettingManager mockSettingManager;
    @Mock
    private IDAssertionRequest mockAssertionRequest;
    @Mock
    private PIMRestClient mockPimRestClient;
    @Mock
    private PIMOIDCConfigration mockPimoidcConfigration;
    @Mock
    private PIMLoginManager mockPimLoginManager;
    @Captor
    private ArgumentCaptor<Response.Listener<String>> captorResponseListener;
    @Captor
    private ArgumentCaptor<Response.ErrorListener> captorErrorListener;
    @Captor
    private ArgumentCaptor<PIMUserMigrationListener> captorMigrationListener;
    @Captor
    private ArgumentCaptor<ServiceDiscoveryInterface.OnGetServiceUrlMapListener> catorServiceMapListener;
    @Mock
    private LoggingInterface mockLoggingInterface;
    @Mock
    private PIMUserMigrationListener mockMigrationListener;

    private PIMMigrationManager spyMigrationManager;
    private final String TAG = PIMMigrationManager.class.getSimpleName();
    private String accessToken = "vsu46sctqqpjwkbn";
    private String id_assertion_endpoint = "https://stg.api.eu-west-1.philips.com/consumerIdentityService/identityAssertions/";

   private HashMap<PIMParameterToLaunchEnum, Object> map = new HashMap<>();
    @Before
    public void setUp() throws Exception {
        super.setUp();
        map.put(PIMParameterToLaunchEnum.PIM_AB_TESTING_CONSENT, new Boolean(true));

        MockitoAnnotations.initMocks(this);
        mockStatic(PIMErrorEnums.class);
        mockStatic(PIMSettingManager.class);
        mockStatic(PIMParameterToLaunchEnum.class);
        Context mockContext = mock(Context.class);
        RestInterface mockRestInterface = mock(RestInterface.class);
        AppInfraInterface mockAppInfraInterface = mock(AppInfraInterface.class);

        when(PIMSettingManager.getInstance()).thenReturn(mockSettingManager);
        when(mockSettingManager.getAppInfraInterface()).thenReturn(mockAppInfraInterface);
        when(mockAppInfraInterface.getServiceDiscovery()).thenReturn(mock(ServiceDiscoveryInterface.class));
        when(mockSettingManager.getLoggingInterface()).thenReturn(mockLoggingInterface);
        when(mockSettingManager.getRestClient()).thenReturn(mockRestInterface);
        when(mockRestInterface.getRequestQueue()).thenReturn(mock(RequestQueue.class));
        when(mockSettingManager.getPimOidcConfigration()).thenReturn(mockPimoidcConfigration);
        whenNew(PIMRestClient.class).withArguments(mockRestInterface).thenReturn(mockPimRestClient);
        whenNew(PIMLoginManager.class).withArguments(mockContext, mockPimoidcConfigration, null).thenReturn(mockPimLoginManager);

        PIMMigrationManager pimMigrationManager = new PIMMigrationManager(mockContext, mockMigrationListener);
        spyMigrationManager = spy(pimMigrationManager);
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void testMigrateUser() throws Exception {
        spyMigrationManager.migrateUser(accessToken);
        verifyPrivate(spyMigrationManager).invoke("downloadIDAssertionUrlFromSD", catorServiceMapListener.capture());
        ServiceDiscoveryInterface.OnGetServiceUrlMapListener mapListener = catorServiceMapListener.getValue();

        Map<String, ServiceDiscoveryService> serviceMap = new HashMap<>();
        ServiceDiscoveryService serviceDiscoveryService = new ServiceDiscoveryService();
        serviceDiscoveryService.setConfigUrl(id_assertion_endpoint);
        serviceMap.put("userreg.janrainoidc.migration", serviceDiscoveryService);
        mapListener.onSuccess(serviceMap);
        verifyPrivate(spyMigrationManager).invoke("performIDAssertion", id_assertion_endpoint, accessToken);

        mapListener.onError(ServiceDiscoveryInterface.OnErrorListener.ERRORVALUES.UNKNOWN_ERROR, "ServiceDiscoveryError");
        verify(mockLoggingInterface).log(DEBUG, TAG, "Migration Failed!! " + " Error in downloadIDAssertionUrlFromSD : " + "ServiceDiscoveryError");
    }

    @Test
    public void testIDAssertionResponseNull() throws Exception {
        whenNew(IDAssertionRequest.class).withArguments(id_assertion_endpoint, accessToken).thenReturn(mockAssertionRequest);
        Whitebox.invokeMethod(spyMigrationManager, "performIDAssertion", id_assertion_endpoint, accessToken);
        verify(mockPimRestClient).invokeRequest(eq(mockAssertionRequest), captorResponseListener.capture(), captorErrorListener.capture());
        Response.Listener<String> response = captorResponseListener.getValue();
        response.onResponse(null);
        verify(mockMigrationListener).onUserMigrationFailed(any(Error.class));
    }

    @Test
    public void testIDAssertionResponseSuccess() throws Exception {
        whenNew(IDAssertionRequest.class).withArguments(id_assertion_endpoint, accessToken).thenReturn(mockAssertionRequest);
        Whitebox.invokeMethod(spyMigrationManager, "performIDAssertion", id_assertion_endpoint, accessToken);
        verify(mockPimRestClient).invokeRequest(eq(mockAssertionRequest), captorResponseListener.capture(), captorErrorListener.capture());
        Response.Listener<String> response = captorResponseListener.getValue();
        response.onResponse(readIDAssertionResponseJson());
        verify(mockLoggingInterface).log(DEBUG, TAG, "ID Assertion request success. ID_token_hint : " + getID_TOKEN_HINT());
        verifyPrivate(spyMigrationManager).invoke("performAuthorization", getID_TOKEN_HINT());
    }

    @Test
    public void testIDAssertionErrorResponse() throws Exception {
        whenNew(IDAssertionRequest.class).withArguments(id_assertion_endpoint, accessToken).thenReturn(mockAssertionRequest);
        Whitebox.invokeMethod(spyMigrationManager, "performIDAssertion", id_assertion_endpoint, accessToken);
        verify(mockPimRestClient).invokeRequest(eq(mockAssertionRequest), captorResponseListener.capture(), captorErrorListener.capture());
        Response.ErrorListener errorListener = captorErrorListener.getValue();
        VolleyError volleyError = new VolleyError();
        errorListener.onErrorResponse(volleyError);
        verify(mockLoggingInterface).log(DEBUG, TAG, "Failed in ID Assertion Request. Error : " + volleyError.getMessage());
        verify(mockMigrationListener).onUserMigrationFailed(any(Error.class));
    }

    @Test
    public void testIDAssertionErrorResponseNull() throws Exception {
        whenNew(IDAssertionRequest.class).withArguments(id_assertion_endpoint, accessToken).thenReturn(mockAssertionRequest);
        Whitebox.invokeMethod(spyMigrationManager, "performIDAssertion", id_assertion_endpoint, accessToken);
        verify(mockPimRestClient).invokeRequest(eq(mockAssertionRequest), captorResponseListener.capture(), captorErrorListener.capture());
        Response.ErrorListener errorListener = captorErrorListener.getValue();
        errorListener.onErrorResponse(null);
        verify(mockLoggingInterface).log(DEBUG, TAG, "Error response for" + mockAssertionRequest + "is null.");
    }

    @Test
    public void testPerformAuthReqFailed() throws Exception {
        AuthorizationRequest mockAuthorizationRequest = mock(AuthorizationRequest.class);
        when(mockPimLoginManager.createAuthRequestUriForMigration(any())).thenReturn(mockAuthorizationRequest);
        when(mockAuthorizationRequest.toUri()).thenReturn(mock(Uri.class));
        PIMMigrationAuthRequest mockMigrationAuthRequest = mock(PIMMigrationAuthRequest.class);
        whenNew(PIMMigrationAuthRequest.class).withArguments(anyString()).thenReturn(mockMigrationAuthRequest);
        Whitebox.invokeMethod(spyMigrationManager, "performAuthorization", getID_TOKEN_HINT());
        verify(mockPimRestClient).invokeRequest(eq(mockMigrationAuthRequest), captorResponseListener.capture(), captorErrorListener.capture());
        Response.Listener<String> response = captorResponseListener.getValue();
        response.onResponse(new JSONObject().toString());
        verify(mockMigrationListener).onUserMigrationFailed(any(Error.class));
    }

    @Test
    public void testPerformAuthorizationAuthorizationRequestNull() throws Exception {
        when(mockPimLoginManager.createAuthRequestUriForMigration(any())).thenReturn(null);
        Whitebox.invokeMethod(spyMigrationManager, "performAuthorization", getID_TOKEN_HINT());
        verify(mockLoggingInterface).log(DEBUG, TAG, "performAuthorization failed. Cause : authorizationRequest is null.");
        verify(mockMigrationListener).onUserMigrationFailed(any(Error.class));
    }

    @Test
    public void testPerformAuthorizationResponseNull() throws Exception {
        AuthorizationRequest mockAuthorizationRequest = mock(AuthorizationRequest.class);
        when(mockPimLoginManager.createAuthRequestUriForMigration(any())).thenReturn(mockAuthorizationRequest);
        when(mockAuthorizationRequest.toUri()).thenReturn(mock(Uri.class));
        PIMMigrationAuthRequest mockMigrationAuthRequest = mock(PIMMigrationAuthRequest.class);
        whenNew(PIMMigrationAuthRequest.class).withArguments(anyString()).thenReturn(mockMigrationAuthRequest);
        Whitebox.invokeMethod(spyMigrationManager, "performAuthorization", getID_TOKEN_HINT());
        verify(mockPimRestClient).invokeRequest(eq(mockMigrationAuthRequest), captorResponseListener.capture(), captorErrorListener.capture());
        Response.Listener<String> response = captorResponseListener.getValue();
        response.onResponse(null);
        verify(mockMigrationListener).onUserMigrationFailed(any(Error.class));
    }

    @Test
    public void testPerformAuthorizationResponseSuccess() throws Exception {
        AuthorizationRequest mockAuthorizationRequest = mock(AuthorizationRequest.class);
        when(mockPimLoginManager.createAuthRequestUriForMigration(any())).thenReturn(mockAuthorizationRequest);
        when(mockAuthorizationRequest.toUri()).thenReturn(mock(Uri.class));
        PIMMigrationAuthRequest mockMigrationAuthRequest = mock(PIMMigrationAuthRequest.class);
        whenNew(PIMMigrationAuthRequest.class).withArguments(anyString()).thenReturn(mockMigrationAuthRequest);
        Whitebox.invokeMethod(spyMigrationManager, "performAuthorization", getID_TOKEN_HINT());
        verify(mockPimRestClient).invokeRequest(eq(mockMigrationAuthRequest), captorResponseListener.capture(), captorErrorListener.capture());
        Response.ErrorListener errorListener = captorErrorListener.getValue();
        List<Header> responseHeaders = new ArrayList<>();
        String authResponse = "com.philips.apps.7602c06b-c547-4aae-8f7c-f89e8c887a21://oauthredirect?code=bmEKqXLGOdf8dpQT&state=zSosZoFkZo5pl4CeAL6GnA";
        responseHeaders.add(new Header("Location", authResponse));
        NetworkResponse networkResponse = new NetworkResponse(302, null, false, System.currentTimeMillis(), responseHeaders);
        VolleyError volleyError = new VolleyError(networkResponse);
        errorListener.onErrorResponse(volleyError);
        verify(mockLoggingInterface).log(DEBUG, TAG, "Authorization response success : " + authResponse);
        verify(mockPimLoginManager).exchangeAuthorizationCodeForMigration(eq(mockAuthorizationRequest), eq(authResponse), captorMigrationListener.capture());
    }

    @Test
    public void testPerformAuthorizationNetworkResponseNull() throws Exception {
        AuthorizationRequest mockAuthorizationRequest = mock(AuthorizationRequest.class);
        when(mockPimLoginManager.createAuthRequestUriForMigration(any())).thenReturn(mockAuthorizationRequest);
        when(mockAuthorizationRequest.toUri()).thenReturn(mock(Uri.class));
        PIMMigrationAuthRequest mockMigrationAuthRequest = mock(PIMMigrationAuthRequest.class);
        whenNew(PIMMigrationAuthRequest.class).withArguments(anyString()).thenReturn(mockMigrationAuthRequest);
        Whitebox.invokeMethod(spyMigrationManager, "performAuthorization", getID_TOKEN_HINT());
        verify(mockPimRestClient).invokeRequest(eq(mockMigrationAuthRequest), captorResponseListener.capture(), captorErrorListener.capture());
        Response.ErrorListener errorListener = captorErrorListener.getValue();
        NetworkResponse networkResponse = null;
        VolleyError volleyError = new VolleyError(networkResponse);
        errorListener.onErrorResponse(volleyError);
        verify(mockLoggingInterface).log(DEBUG, TAG, "Token auth request failed.");
        verify(mockMigrationListener).onUserMigrationFailed(any(Error.class));
    }

    @Test
    public void testCreateAdditionalParameterForMigration() throws Exception {
        String id_token_hint = getID_TOKEN_HINT();
        String customClaim = new PIMOIDCConfigration().getCustomClaims();
        when(mockSettingManager.getPimOidcConfigration()).thenReturn(mockPimoidcConfigration);
        when(mockPimoidcConfigration.getCustomClaims()).thenReturn(customClaim);
        Map<String, String> additionalParameterForMigration = Whitebox.invokeMethod(spyMigrationManager, "createAdditionalParameterForMigration", id_token_hint);
        assertEquals(id_token_hint, additionalParameterForMigration.get("id_token_hint"));
        assertEquals(customClaim, additionalParameterForMigration.get("claims"));
    }

    @Test
    public void testParseIDAssertionForJson() throws Exception {
        String idTokenHintResponse = Whitebox.invokeMethod(spyMigrationManager, "parseIDAssertionFromJSONResponse", readIDAssertionResponseJson());
        assertEquals(getID_TOKEN_HINT(), idTokenHintResponse);
    }

    @Test
    public void testParseIDAssertionForJson_Exception() throws Exception {
        JSONObject jsonResponse = new JSONObject();
        Whitebox.invokeMethod(spyMigrationManager, "parseIDAssertionFromJSONResponse", jsonResponse.toString());
        verify(mockMigrationListener).onUserMigrationFailed(any(Error.class));
    }

    private String readIDAssertionResponseJson() {
        String path = "src/test/rs/idassertion_response.json";
        File file = new File(path);
        try {
            JsonParser jsonParser = new JsonParser();
            Object obj = jsonParser.parse(new FileReader(file));
            JsonObject jsonObject = (JsonObject) obj;
            return jsonObject.toString();
        } catch (FileNotFoundException e) {
            return null;
        }
    }

    private String getID_TOKEN_HINT() {
        String id_assertion_response = readIDAssertionResponseJson();
        JSONObject jsonObject;
        try {
            jsonObject = new JSONObject(id_assertion_response);
            return jsonObject.getJSONObject("data").getString("identityAssertion");
        } catch (JSONException e) {
            return null;
        }
    }
}