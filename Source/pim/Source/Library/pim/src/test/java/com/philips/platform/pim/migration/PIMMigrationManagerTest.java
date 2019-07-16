package com.philips.platform.pim.migration;

import android.content.Context;
import android.net.Uri;

import com.android.volley.Response;
import com.google.gson.JsonObject;
import com.philips.platform.appinfra.AppInfraInterface;
import com.philips.platform.appinfra.logging.LoggingInterface;
import com.philips.platform.appinfra.rest.RestInterface;
import com.philips.platform.appinfra.rest.request.RequestQueue;
import com.philips.platform.pif.DataInterface.USR.enums.Error;
import com.philips.platform.pim.configration.PIMOIDCConfigration;
import com.philips.platform.pim.listeners.PIMUserMigrationListener;
import com.philips.platform.pim.manager.PIMLoginManager;
import com.philips.platform.pim.manager.PIMSettingManager;
import com.philips.platform.pim.rest.IDAssertionRequest;
import com.philips.platform.pim.rest.PIMMigrationAuthRequest;
import com.philips.platform.pim.rest.PIMRestClient;
import com.philips.platform.pim.utilities.UserCustomClaims;

import junit.framework.TestCase;

import net.openid.appauth.AuthorizationRequest;

import org.json.JSONObject;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.Map;

import static com.philips.platform.appinfra.logging.LoggingInterface.LogLevel.DEBUG;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.powermock.api.mockito.PowerMockito.mockStatic;
import static org.powermock.api.mockito.PowerMockito.when;
import static org.powermock.api.mockito.PowerMockito.whenNew;

@PrepareForTest({PIMSettingManager.class, IDAssertionRequest.class, PIMMigrationManager.class, PIMLoginManager.class})
@RunWith(PowerMockRunner.class)
public class PIMMigrationManagerTest extends TestCase {

    @Mock
    Context mockContext;
    @Mock
    PIMSettingManager mockSettingManager;
    @Mock
    AppInfraInterface mockAppInfraInterface;
    @Mock
    RestInterface mockRestInterface;
    @Mock
    IDAssertionRequest mockAssertionRequest;
    @Mock
    PIMRestClient mockPimRestClient;
    @Mock
    RequestQueue mockRequestQueue;
    @Mock
    PIMOIDCConfigration mockPimoidcConfigration;
    @Mock
    PIMLoginManager mockPimLoginManager;
    @Captor
    ArgumentCaptor<Response.Listener<String>> captorResponseListener;
    @Captor
    ArgumentCaptor<Response.ErrorListener> captorErrorListener;
    @Captor
    ArgumentCaptor<PIMUserMigrationListener> captorMigrationListener;
    @Mock
    private LoggingInterface mockLoggingInterface;
    @Mock
    private PIMUserMigrationListener mockMigrationListener;
    @Mock
    private Error mockError;

    private PIMMigrationManager pimMigrationManager;
    private final String TAG = PIMMigrationManager.class.getSimpleName();
    private String ID_TOKEN_HINT = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzUxMiJ9.eyJzdWIiOiI5NmM2ODQ4OC0zZTRjLTRiYjctODc5YS05YTgwNGY0OTRjNWUiLCJpc3MiOiJodHRwczovL3BoaWxpcHMuZXZhbC5qYW5yYWluY2FwdHVyZS5jb20iLCJpYXQiOjE1NjI2NjcyNTksImp0aSI6IjdkOGJhZjdmLTM0YmUtNGYxOS04YWRiLTU3ZDQ0ZjhjYWUyOCIsImV4cCI6MTU2MjY2NzU1OTAwMCwiYXVkIjpbImI5MDZmMDljLTIyYTctNDQ5Yy1hZGNiLTNmMjJhYTFiZDcxYiJdfQ.Y8MlINSfznEL-JUwgwTPtNNPZfWFkUirNyLOvt7N0_BGviNlcn_EFatfFwkfqCujPkzWUpqoxGvUTsbv4-Hqtg";


    public void setUp() throws Exception {
        super.setUp();

        MockitoAnnotations.initMocks(this);

        mockStatic(PIMSettingManager.class);
        when(PIMSettingManager.getInstance()).thenReturn(mockSettingManager);
        when(mockSettingManager.getAppInfraInterface()).thenReturn(mockAppInfraInterface);
        when(mockSettingManager.getRestClient()).thenReturn(mockRestInterface);
        when(mockSettingManager.getLoggingInterface()).thenReturn(mockLoggingInterface);
        when(mockRestInterface.getRequestQueue()).thenReturn(mockRequestQueue);
        when(mockSettingManager.getPimOidcConfigration()).thenReturn(mockPimoidcConfigration);
        whenNew(PIMRestClient.class).withArguments(mockRestInterface).thenReturn(mockPimRestClient);
        whenNew(PIMLoginManager.class).withArguments(mockContext, mockPimoidcConfigration).thenReturn(mockPimLoginManager);

        pimMigrationManager = new PIMMigrationManager(mockContext, mockMigrationListener);
    }

    public void tearDown() throws Exception {
    }

    @Test
    public void testMigrateUser() throws Exception {
        String accessToken = "vsu46sctqqpjwkbn";
        whenNew(IDAssertionRequest.class).withArguments("https://stg.api.eu-west-1.philips.com/consumerIdentityService/identityAssertions/", accessToken).thenReturn(mockAssertionRequest);
        pimMigrationManager.migrateUser(accessToken);
        verify(mockPimRestClient).invokeRequest(eq(mockAssertionRequest), captorResponseListener.capture(), captorErrorListener.capture());
        Response.Listener<String> response = captorResponseListener.getValue();
    }

    @Test
    public void testPerformAuthorization() throws Exception {
        AuthorizationRequest mockAuthorizationRequest = mock(AuthorizationRequest.class);
        when(mockPimLoginManager.createAuthRequestUriForMigration(any())).thenReturn(mockAuthorizationRequest);
        when(mockAuthorizationRequest.toUri()).thenReturn(mock(Uri.class));
        PIMMigrationAuthRequest mockMigrationAuthRequest = mock(PIMMigrationAuthRequest.class);
        whenNew(PIMMigrationAuthRequest.class).withArguments(anyString()).thenReturn(mockMigrationAuthRequest);
        pimMigrationManager.performAuthorization(ID_TOKEN_HINT);
        verify(mockPimRestClient).invokeRequest(eq(mockMigrationAuthRequest), captorResponseListener.capture(), captorErrorListener.capture());
    }

    @Test
    public void testPerformAuthorization_AuthorizationRequest_Null() throws Exception {
        when(mockPimLoginManager.createAuthRequestUriForMigration(any())).thenReturn(null);
        pimMigrationManager.performAuthorization(ID_TOKEN_HINT);
        verify(mockLoggingInterface).log(DEBUG, TAG, "performAuthorization failed. Cause : authorizationRequest is null.");
        verify(mockMigrationListener).onUserMigrationFailed(any(Error.class));
    }


    @Test
    public void testCreateAdditionalParameterForMigration() throws Exception {
        String id_token_hint = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzUxMiJ9.eyJzdWIiOiI5NmM2ODQ4OC0zZTRjLTRiYjctODc5YS05YTgwNGY0OTRjNWUiLCJpc3MiOiJodHRwczovL3BoaWxpcHMuZXZhbC5qYW5yYWluY2FwdHVyZS5jb20iLCJpYXQiOjE1NjI2NjcyNTksImp0aSI6IjdkOGJhZjdmLTM0YmUtNGYxOS04YWRiLTU3ZDQ0ZjhjYWUyOCIsImV4cCI6MTU2MjY2NzU1OTAwMCwiYXVkIjpbImI5MDZmMDljLTIyYTctNDQ5Yy1hZGNiLTNmMjJhYTFiZDcxYiJdfQ.Y8MlINSfznEL-JUwgwTPtNNPZfWFkUirNyLOvt7N0_BGviNlcn_EFatfFwkfqCujPkzWUpqoxGvUTsbv4-Hqtg";
        String customClaim = getCustomClaims();
        when(mockSettingManager.getPimOidcConfigration()).thenReturn(mockPimoidcConfigration);
        when(mockPimoidcConfigration.getCustomClaims()).thenReturn(customClaim);
        Map<String, String> additionalParameterForMigration = pimMigrationManager.createAdditionalParameterForMigration(id_token_hint);
        assertEquals(id_token_hint, additionalParameterForMigration.get("id_token_hint"));
        assertEquals(customClaim, additionalParameterForMigration.get("claims"));
    }

    @Test
    public void testParseIDAssertionForJson() throws Exception {
        JSONObject jsonResponse = new JSONObject();
        JSONObject accessTokenJson = new JSONObject();
        accessTokenJson.put("identityAssertion", ID_TOKEN_HINT);
        jsonResponse.put("data", accessTokenJson);
        String idTokenHintResponse = pimMigrationManager.parseIDAssertionFromJSONResponse(jsonResponse.toString());
        assertEquals(ID_TOKEN_HINT, idTokenHintResponse);
    }

    @Test
    public void testParseIDAssertionForJson_Exception() throws Exception {
        JSONObject jsonResponse = new JSONObject();
        pimMigrationManager.parseIDAssertionFromJSONResponse(jsonResponse.toString());
        verify(mockMigrationListener).onUserMigrationFailed(any(Error.class));
    }

    @Test
    public void testGetSuccessListener_ResponseNull() {
        IDAssertionRequest mockIDIdAssertionRequest = mock(IDAssertionRequest.class);
        Response.Listener<String> response = pimMigrationManager.getSuccessListener(mockIDIdAssertionRequest);
        assertNotNull(response);
        response.onResponse(null);
    }

    @Test
    public void testGetSuccessListener_IDAssertionRequest() {
        PIMMigrationAuthRequest mockPIMigrationAuthRequest = mock(PIMMigrationAuthRequest.class);
        Response.Listener<String> response = pimMigrationManager.getSuccessListener(mockPIMigrationAuthRequest);
        assertNotNull(response);
    }

    @Test
    public void testGetSuccessListener_PIMMigrationAuthRequest() {
        PIMMigrationAuthRequest mockPIMigrationAuthRequest = mock(PIMMigrationAuthRequest.class);
        Response.Listener<String> response = pimMigrationManager.getSuccessListener(mockPIMigrationAuthRequest);
        assertNotNull(response);
        response.onResponse("");
        verify(mockLoggingInterface).log(DEBUG,TAG,"Token auth request failed.");
    }

    @Test
    public void testGetErrorListener_ResponseNull() {
        IDAssertionRequest mockIDIdAssertionRequest = mock(IDAssertionRequest.class);
        Response.ErrorListener response = pimMigrationManager.getErrorListener(mockIDIdAssertionRequest);
        assertNotNull(response);
        response.onErrorResponse(null);
    }

    @Test
    public void testGetErrorListener_IDAssertionRequest() {
        IDAssertionRequest mockIDIdAssertionRequest = mock(IDAssertionRequest.class);
        Response.ErrorListener response = pimMigrationManager.getErrorListener(mockIDIdAssertionRequest);
        assertNotNull(response);
        response.onErrorResponse(null);
    }

    @Test
    public void testGetErrorListener_PIMMigrationAuthRequest() {
        IDAssertionRequest mockIDIdAssertionRequest = mock(IDAssertionRequest.class);
        Response.ErrorListener response = pimMigrationManager.getErrorListener(mockIDIdAssertionRequest);
        assertNotNull(response);
        response.onErrorResponse(null);
    }

    public String getCustomClaims() {
        JsonObject customClaimObject = new JsonObject();
        customClaimObject.add(UserCustomClaims.RECEIVE_MARKETING_EMAIL_CONSENT, null);
        customClaimObject.add(UserCustomClaims.RECEIVE_MARKETING_EMAIL_TIMESTAMP, null);
        customClaimObject.add(UserCustomClaims.SOCIAL_PROFILES, null);
        customClaimObject.add(UserCustomClaims.UUID, null);

        JsonObject userInfo = new JsonObject();
        userInfo.add("userinfo", customClaimObject);
        return userInfo.toString();
    }
}