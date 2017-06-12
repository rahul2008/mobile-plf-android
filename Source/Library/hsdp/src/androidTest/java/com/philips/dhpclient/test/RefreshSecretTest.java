/*
 *  Copyright (c) Koninklijke Philips N.V., 2016
 *  All rights are reserved. Reproduction or dissemination
 *  * in whole or in part is prohibited without the prior written
 *  * consent of the copyright holder.
 * /
 */


package com.philips.dhpclient.test;

import android.support.multidex.MultiDex;
import android.test.InstrumentationTestCase;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.philips.dhpclient.DhpApiClientConfiguration;
import com.philips.dhpclient.DhpAuthenticationManagementClient;
import com.philips.dhpclient.response.DhpAuthenticationResponse;
import com.philips.dhpclient.response.DhpResponse;
import com.philips.dhpclient.util.MapUtils;
import com.philips.ntputils.ServerTime;

import org.json.JSONException;
import org.mockito.Mockito;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


/**
 * Created by 310190722 on 9/8/2015.
 */

public class RefreshSecretTest extends InstrumentationTestCase {

    private DhpAuthenticationManagementClient authenticationManagementClient;

    private final DhpApiClientConfiguration dhpApiClientConfiguration = new DhpApiClientConfiguration(
            "http://ugrow-userregistration15.cloud.pcftest.com",
            "uGrowApp",
            "f129afcc-55f4-11e5-885d-feff819cdc9f",
            "f129b5a8-55f4-11e5-885d-feff819cdc9f");

    @Override
    protected void setUp() throws Exception {
        MultiDex.install(getInstrumentation().getTargetContext());
        super.setUp();
        System.setProperty("dexmaker.dexcache", getInstrumentation()
                .getTargetContext().getCacheDir().getPath());

        authenticationManagementClient = new DhpAuthenticationManagementClient(dhpApiClientConfiguration);
    }

    public void testRefreshSecretCreateSignature() {
        assertEquals("YPCh1N0aEs3r4+2uKoNTqBeT/aw=", authenticationManagementClient.createRefreshSignature("aa6c3f0dd953bcf11053e00e686af2e0d9b1d05b"
                , "2016-03-28 07:20:31", "3kr6baw3tqbuyg58"));
    }

//    public void testRefreshSecretResponseNotNull() {
//        DhpAuthenticationResponse response = getServerMockResponse();
//        assertNotNull(response);
//    }

//    public void testRefreshSecretResponseSize() {
//        DhpAuthenticationResponse response = getServerMockResponse();
//        assertTrue(response.rawResponse.size() > 0);
//    }

//    public void testUserId() {
//        DhpAuthenticationResponse response = getServerMockResponse();
//        assertNotNull(response.userId);
//    }
//
//    public void testResponseMessage() {
//        DhpAuthenticationResponse response = getServerMockResponse();
//        assertNotNull(response.message);
//    }

//    public void testAccessToken() {
//        DhpAuthenticationResponse response = getServerMockResponse();
//        assertNotNull(response.accessToken);
//    }
//
//    public void testExpiresIn() {
//        DhpAuthenticationResponse response = getServerMockResponse();
//        assertNotNull(response.expiresIn);
//    }

//    public void testRefreshToken() {
//        DhpAuthenticationResponse response = getServerMockResponse();
//        assertNotNull(response.refreshToken);
//    }
//
//    public void testResponseCode() {
//        DhpAuthenticationResponse response = getServerMockResponse();
//        assertNotNull(response.responseCode);
//    }

//    public void testRawResponse() {
//        DhpAuthenticationResponse response = getServerMockResponse();
//        assertNotNull(response.rawResponse);
//    }

//    public void testHsdpUserUUID() {
//        DhpAuthenticationResponse dhpAuthenticationResponse = getServerMockResponse();
//        String userUUID = MapUtils.extract(dhpAuthenticationResponse.rawResponse, "exchange.user.userUUID");
//        assertNotNull(userUUID);
//    }

//    public void testRefreshSecretSuccesResponse() {
//        DhpAuthenticationResponse response = getServerMockResponse();
//        assertEquals("200",response.responseCode);
//    }


    private Map<String, Object> readStream(String in) throws IOException, JSONException {
        final ObjectMapper JSON_MAPPER = new ObjectMapper();
        return JSON_MAPPER.readValue(in.toString(), Map.class);
    }

    private DhpAuthenticationResponse getServerMockResponse() {
        final DhpApiClientConfiguration dhpApiClientConfiguration = new DhpApiClientConfiguration(
                "http://ugrow-userregistration15.cloud.pcftest.com",
                "uGrowApp",
                "f129afcc-55f4-11e5-885d-feff819cdc9f",
                "f129b5a8-55f4-11e5-885d-feff819cdc9f");
        DhpAuthenticationManagementClient dhpAuthenticationManagementClient = new DhpAuthenticationManagementClient(dhpApiClientConfiguration);
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
        Mockito.when(dhpAuthenticationManagementClientMock.refreshSecret("maqsoodkk", "3kr6baw3tqbuyg58", "aa6c3f0dd953bcf11053e00e686af2e0d9b1d05b")).thenReturn(dhpAuthenticationResponse);
        return dhpAuthenticationResponse;
    }
}

