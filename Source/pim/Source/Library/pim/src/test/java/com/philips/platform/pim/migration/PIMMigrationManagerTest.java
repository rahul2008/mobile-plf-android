package com.philips.platform.pim.migration;

import android.content.Context;
import android.net.Uri;

import com.android.volley.Response;
import com.google.gson.JsonObject;
import com.philips.platform.appinfra.AppInfraInterface;
import com.philips.platform.appinfra.rest.RestInterface;
import com.philips.platform.appinfra.rest.request.RequestQueue;
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

    PIMMigrationManager pimMigrationManager;


    public void setUp() throws Exception {
        super.setUp();

        MockitoAnnotations.initMocks(this);

        mockStatic(PIMSettingManager.class);
        when(PIMSettingManager.getInstance()).thenReturn(mockSettingManager);
        when(mockSettingManager.getAppInfraInterface()).thenReturn(mockAppInfraInterface);
        when(mockSettingManager.getRestClient()).thenReturn(mockRestInterface);
        when(mockRestInterface.getRequestQueue()).thenReturn(mockRequestQueue);
        when(mockSettingManager.getPimOidcConfigration()).thenReturn(mockPimoidcConfigration);
        whenNew(PIMRestClient.class).withArguments(mockRestInterface).thenReturn(mockPimRestClient);
        whenNew(PIMLoginManager.class).withArguments(mockContext, mockPimoidcConfigration).thenReturn(mockPimLoginManager);

        pimMigrationManager = new PIMMigrationManager(mockContext);
    }

    public void tearDown() throws Exception {
    }

    @Test
    public void testMigrateUser() throws Exception {
        String accessToken = "vsu46sctqqpjwkbn";
        whenNew(IDAssertionRequest.class).withArguments("https://stg.api.eu-west-1.philips.com/consumerIdentityService/identityAssertions/", accessToken).thenReturn(mockAssertionRequest);
        pimMigrationManager.migrateUser(accessToken,captorMigrationListener.capture());
        verify(mockPimRestClient).invokeRequest(eq(mockAssertionRequest), captorResponseListener.capture(), captorErrorListener.capture());
    }

    @Test
    public void testPerformAuthorization() throws Exception {
        String id_token_hint = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzUxMiJ9.eyJzdWIiOiI5NmM2ODQ4OC0zZTRjLTRiYjctODc5YS05YTgwNGY0OTRjNWUiLCJpc3MiOiJodHRwczovL3BoaWxpcHMuZXZhbC5qYW5yYWluY2FwdHVyZS5jb20iLCJpYXQiOjE1NjI2NjcyNTksImp0aSI6IjdkOGJhZjdmLTM0YmUtNGYxOS04YWRiLTU3ZDQ0ZjhjYWUyOCIsImV4cCI6MTU2MjY2NzU1OTAwMCwiYXVkIjpbImI5MDZmMDljLTIyYTctNDQ5Yy1hZGNiLTNmMjJhYTFiZDcxYiJdfQ.Y8MlINSfznEL-JUwgwTPtNNPZfWFkUirNyLOvt7N0_BGviNlcn_EFatfFwkfqCujPkzWUpqoxGvUTsbv4-Hqtg";
        AuthorizationRequest mockAuthorizationRequest = mock(AuthorizationRequest.class);
        when(mockPimLoginManager.createAuthRequestUriForMigration(any())).thenReturn(mockAuthorizationRequest);
        when(mockAuthorizationRequest.toUri()).thenReturn(mock(Uri.class));
        PIMMigrationAuthRequest mockMigrationAuthRequest = mock(PIMMigrationAuthRequest.class);
        whenNew(PIMMigrationAuthRequest.class).withArguments(anyString()).thenReturn(mockMigrationAuthRequest);
        pimMigrationManager.performAuthorization(id_token_hint);
        verify(mockPimRestClient).invokeRequest(eq(mockMigrationAuthRequest), captorResponseListener.capture(), captorErrorListener.capture());
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
        String actualIDTokenHint = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzUxMiJ9.eyJzdWIiOiI5NmM2ODQ4OC0zZTRjLTRiYjctODc5YS05YTgwNGY0OTRjNWUiLCJpc3MiOiJodHRwczovL3BoaWxpcHMuZXZhbC5qYW5yYWluY2FwdHVyZS5jb20iLCJpYXQiOjE1NjI2NjcyNTksImp0aSI6IjdkOGJhZjdmLTM0YmUtNGYxOS04YWRiLTU3ZDQ0ZjhjYWUyOCIsImV4cCI6MTU2MjY2NzU1OTAwMCwiYXVkIjpbImI5MDZmMDljLTIyYTctNDQ5Yy1hZGNiLTNmMjJhYTFiZDcxYiJdfQ.Y8MlINSfznEL-JUwgwTPtNNPZfWFkUirNyLOvt7N0_BGviNlcn_EFatfFwkfqCujPkzWUpqoxGvUTsbv4-Hqtg";
        JSONObject bodyJson = new JSONObject();
        JSONObject accessTokenJson = new JSONObject();
        accessTokenJson.put("identityAssertion", actualIDTokenHint);
        bodyJson.put("data", accessTokenJson);
        String id_token_hint = pimMigrationManager.parseIDAssertionFromJSONResponse(bodyJson.toString());
        assertEquals(actualIDTokenHint, id_token_hint);
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