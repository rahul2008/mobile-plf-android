
/*
 * Copyright (c) 2015-2018 Koninklijke Philips N.V.
 * All rights reserved.
 */

package com.philips.dhpclient;

import android.support.multidex.MultiDex;

import com.philips.dhpclient.response.DhpResponse;

import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import static androidx.test.platform.app.InstrumentationRegistry.getInstrumentation;
import static org.junit.Assert.assertNotNull;

/**
 * Created by 310243576 on 8/21/2016.
 */
public class DhpAuthenticationManagementClientTest {

    private DhpAuthenticationManagementClient mDhpAuthenticationManagementClient;

    @Before
    public void setUp() throws Exception {
        MultiDex.install(getInstrumentation().getTargetContext());
        System.setProperty("dexmaker.dexcache", getInstrumentation().getTargetContext().getCacheDir().getPath());

        DhpApiClientConfiguration dhpApiClientConfiguration = new DhpApiClientConfiguration("apiBaseUrl", "dhpApplicationName", "signingKey", "signingSecret");
        mDhpAuthenticationManagementClient = new DhpAuthenticationManagementClient(dhpApiClientConfiguration);

    }

    @Test
    public void testDhpAuthenticateManagementClient() throws Exception {
        mDhpAuthenticationManagementClient.authenticate("username", "password", "secret");
        mDhpAuthenticationManagementClient.createRefreshSignature("refresh_Secret", "date", "accessToken");
        mDhpAuthenticationManagementClient.createRefreshSignature("refresh_Secret", "", "");
        mDhpAuthenticationManagementClient.validateToken("userId", "accessToken");
        mDhpAuthenticationManagementClient.validateToken(null, null);
        mDhpAuthenticationManagementClient.validateToken("", "");
        mDhpAuthenticationManagementClient.loginSocialProviders("email", "socialaccesstoken", "asjdbwdbwdbejkwfbjkewbwejkdw");
        mDhpAuthenticationManagementClient.logout("sample", "sample");
        mDhpAuthenticationManagementClient.logout(null, null);
        mDhpAuthenticationManagementClient.logout("", "");

        assertNotNull(mDhpAuthenticationManagementClient);
    }

    @Test
    public void testSign() throws Exception {
        Method method = null;
        String s = "sample";
        Map<String, String> headers = new HashMap<>();
        Map<String, Object> rawResponse = new HashMap<>();
        DhpResponse dhpResponse = new DhpResponse(rawResponse);

        method = DhpAuthenticationManagementClient.class.getDeclaredMethod("getDhpAuthenticationResponse", DhpResponse.class);
        method.setAccessible(true);
        method.invoke(mDhpAuthenticationManagementClient, dhpResponse);
        dhpResponse = null;
        method.invoke(mDhpAuthenticationManagementClient, dhpResponse);
    }

    @Test
    public void testGetUTCdatetimeAsString() throws Exception {
        Method method = null;
        method = DhpAuthenticationManagementClient.class.getDeclaredMethod("getUTCdatetimeAsString");
        method.setAccessible(true);
        method.invoke(mDhpAuthenticationManagementClient);
    }
}
