/*
 *  Copyright (c) Koninklijke Philips N.V., 2016
 *  All rights are reserved. Reproduction or dissemination
 *  * in whole or in part is prohibited without the prior written
 *  * consent of the copyright holder.
 * /
 *//*


package com.philips.dhpclient.test;

import android.test.InstrumentationTestCase;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.philips.cdp.servertime.ServerTime;
import com.philips.dhpclient.DhpApiClientConfiguration;
import com.philips.dhpclient.DhpApiSigner;
import com.philips.dhpclient.DhpAuthenticationManagementClient;
import com.philips.dhpclient.response.DhpAuthenticationResponse;
import com.philips.dhpclient.response.DhpResponse;
import com.philips.dhpclient.util.MapUtils;

import org.json.JSONException;
//import org.mockito.Mockito;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

//import org.mockito.Mockito;

*/
/**
 * Created by 310190722 on 9/8/2015.
 *//*

public class DhpApiSignerTest extends InstrumentationTestCase {

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        System.setProperty("dexmaker.dexcache", getInstrumentation()
                .getTargetContext().getCacheDir().getPath());
        ServerTime.init(getInstrumentation()
                .getTargetContext());
    }

    public void testSignedSignature() {
        Map<String, String> headers = new LinkedHashMap<String, String>();
        headers.put("Content-Type", "application/json");
        headers.put("Content-Length", "10");
        headers.put("SignedDate", "2015-07-02T07:52:03.100+0000");

        String result = new DhpApiSigner("sharedKey", "secretKey").buildAuthorizationHeaderValue(
                "POST",
                "foo=bar&bar=foo",
                headers,
                "http://user-registration-service.cloud.pcftest.com",
                "BLAA");

        String expected = "HmacSHA256;Credential:sharedKey;SignedHeaders:Content-Type,Content-Length,SignedDate,;Signature:xymOJ/t5bbgxmxqOf84ifd8U1w2H7WOITQkjB+zyZoY=";
        if (expected.toString().trim().equals(result.toString().trim())) {
            assertTrue(true);
        } else {
            assertTrue(false);
        }
    }

    public void testAuthenticate(){
        DhpAuthenticationResponse response = getServerMockResponse();
        assertNotNull(response);
    }

    public void testUserId() {
        DhpAuthenticationResponse response = getServerMockResponse();
        assertNotNull(response.userId);
    }

    public void testResponseMessage() {
        DhpAuthenticationResponse response = getServerMockResponse();
        assertNotNull(response.message);
    }

    public void testAccessToken() {
        DhpAuthenticationResponse response = getServerMockResponse();
        assertNotNull(response.accessToken);
    }

    public void testExpiresIn() {
        DhpAuthenticationResponse response = getServerMockResponse();
        assertNotNull(response.expiresIn);
    }

    public void testRefreshToken() {
        DhpAuthenticationResponse response = getServerMockResponse();
        assertNotNull(response.refreshToken);
    }

    public void testResponseCode() {
        DhpAuthenticationResponse response = getServerMockResponse();
        assertNotNull(response.responseCode);
    }

    public void testRawResponse() {
        DhpAuthenticationResponse response = getServerMockResponse();
        assertNotNull(response.rawResponse);
    }

    public void testHsdpUserUUID() {
        DhpAuthenticationResponse dhpAuthenticationResponse = getServerMockResponse();
        String userUUID = MapUtils.extract(dhpAuthenticationResponse.rawResponse, "exchange.user.userUUID");
        assertNotNull(userUUID);
    }

    public void testSuccesResponse() {
        DhpAuthenticationResponse response = getServerMockResponse();
        assertEquals("200",response.responseCode);
    }

    public void testRefreshSecretResponseSize() {
        DhpAuthenticationResponse response = getServerMockResponse();
        assertTrue(response.rawResponse.size() > 0);
    }

    private Map<String, Object> readStream(String in) throws IOException, JSONException {
        final ObjectMapper JSON_MAPPER = new ObjectMapper();
        return JSON_MAPPER.readValue(in.toString(), Map.class);
    }

    private DhpAuthenticationResponse getServerMockResponse(){
        final DhpApiClientConfiguration dhpApiClientConfiguration = new DhpApiClientConfiguration(
                "http://ugrow-userregistration15.cloud.pcftest.com",
                "uGrowApp",
                "f129afcc-55f4-11e5-885d-feff819cdc9f",
                "f129b5a8-55f4-11e5-885d-feff819cdc9f");
       */
/* DhpAuthenticationManagementClient dhpAuthenticationManagementClient = new DhpAuthenticationManagementClient(dhpApiClientConfiguration);
        DhpAuthenticationManagementClient dhpAuthenticationManagementClientMock = Mockito.spy(dhpAuthenticationManagementClient);
        Map<String, Object> rawResponse = new HashMap<>();

        String input = "{\"exchange\":{\"meta\":{},\"accessCredential\":{\"accessToken\":\"drm4wnmu32mdhn2x\",\"refreshToken\":\"6nd87nskhnyhy5g8m5js\",\"expiresIn\":\"3600\"},\n" +
                "\"user\":{\"loginId\":\"maqsoodphilips@gmail.com\",\"profile\":{\"givenName\":\"maqsoox\",\"preferredLanguage\":\"en\",\"receiveMarketingEmail\":\"No\",\n" +
                "\"locale\":\"en-US\",\"primaryAddress\":{\"country\":\"GB\"},\"height\":0.0,\"weight\":0.0},\"userIsActive\":1,\"isSocial\":1,\"userUUID\":\"ecde276a-de68-4411-bc01-dcb278f3bb0d\"}},\n" +
                "\"responseCode\":\"200\",\"responseMessage\":\"Success\"}";
        try {
            rawResponse = readStream(input);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        DhpResponse dhpResponse = new DhpResponse(rawResponse);
        DhpAuthenticationResponse dhpAuthenticationResponse = new DhpAuthenticationResponse("6cstbqh7bzwt3z4b", "bxqyqs86kgq7ks3g4f5n", Integer.parseInt("3600"), "nhggh", dhpResponse.rawResponse);
        Mockito.when(dhpAuthenticationManagementClientMock.authenticate("maqsoodphilips@gmail.com", "password","aa6c3f0dd953bcf11053e00e686af2e0d9b1d05b")).thenReturn(dhpAuthenticationResponse);
*//*
        return dhpAuthenticationResponse;
    }
}
*/
