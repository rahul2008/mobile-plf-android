
/*
 * Copyright (c) 2015-2018 Koninklijke Philips N.V.
 * All rights reserved.
 */

package com.philips.dhpclient;

import android.support.multidex.MultiDex;

import com.philips.dhpclient.response.DhpResponseVerifier;

import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.Method;
import java.net.HttpURLConnection;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;

import static androidx.test.platform.app.InstrumentationRegistry.getInstrumentation;
import static org.junit.Assert.assertNotNull;

/**
 * Created by 310243576 on 8/25/2016.
 */
public class DhpApiClientTest {
    private DhpApiClient mDhpApiClient;

    @Before
    public void setUp() throws Exception {
        MultiDex.install(getInstrumentation().getTargetContext());
        System.setProperty("dexmaker.dexcache", getInstrumentation().getTargetContext().getCacheDir().getPath());

        DhpApiClientConfiguration dhpApiClientConfiguration = new DhpApiClientConfiguration("apiBaseUrl", "dhpApplicationName", "signingKey", "signingSecret");
        mDhpApiClient = new DhpApiClient(dhpApiClientConfiguration);
    }

    @Test
    public void testDhpApiClient() {
        assertNotNull(mDhpApiClient);

        DhpResponseVerifier dhpResponseVerifier = ignored -> {
        };

        mDhpApiClient.setResponseVerifier(dhpResponseVerifier);
        assertNotNull(mDhpApiClient);
    }

    @Test
    public void testSendSignedRequest() throws Exception {
        Method method = null;
        String s = "sample";
        Object d = new Object();
        Map<String, String> headers = new HashMap<>();

        method = DhpApiClient.class.getDeclaredMethod("sendSignedRequest", String.class, String.class, String.class, HashMap.class, Object.class);
        method.setAccessible(true);
        method.invoke(mDhpApiClient, s, s, s, headers, d);
    }

    @Test
    public void testSendSignedRequestForSocialLogin() throws Exception {
        Method method = null;
        String s = "sample";
        Object d = new Object();
        Map<String, String> headers = new HashMap<>();

        method = DhpApiClient.class.getDeclaredMethod("sendSignedRequestForSocialLogin", String.class, String.class, String.class, HashMap.class, Object.class);
        method.setAccessible(true);
        method.invoke(mDhpApiClient, s, s, s, headers, d);
    }

    @Test
    public void testAddSignedDateHeader() throws Exception {
        Method method = null;
        String s = "sample";
        Object d = new Object();
        Map<String, String> headers = new HashMap<>();

        method = DhpApiClient.class.getDeclaredMethod("addSignedDateHeader", Map.class);
        method.setAccessible(true);
        method.invoke(mDhpApiClient, headers);
    }

    @Test
    public void testSendRestRequest1() throws Exception {
        Method method = null;
        String s = "sample";
        Object d = new Object();
        Map<String, String> headers = new HashMap<>();

        method = DhpApiClient.class.getDeclaredMethod("sendRestRequest", String.class, String.class, String.class, Map.class, Object.class);
        method.setAccessible(true);
        method.invoke(mDhpApiClient, s, s, s, headers, d);
    }

    @Test
    public void testSendRestRequest() throws Exception {
        Method method = null;
        String s = "sample";
        Object d = new Object();
        Map<String, String> headers = new HashMap<>();
        URI uri = URI.create(s + s + "?" + s);

        method = DhpApiClient.class.getDeclaredMethod("sendRestRequest", String.class, URI.class, Map.class, Object.class);
        method.setAccessible(true);
        method.invoke(mDhpApiClient, s, uri, headers, d);
    }

    @Test
    public void testEstablishConnection() throws Exception {
        Method method = null;
        String s = "sample";
        Object d = new Object();
        Map<String, String> headers = new HashMap<>();
        URI uri = URI.create(s + s + "?" + s);

        method = DhpApiClient.class.getDeclaredMethod("establishConnection", URI.class, String.class, Map.class, Object.class);
        method.setAccessible(true);
        method.invoke(mDhpApiClient, uri, s, headers, d);
    }

    @Test
    public void testAddRequestBody() throws Exception {
        Method method = null;
        String s = "sample";
        Object d = new Object();
        URI uri = URI.create(s + s + "?" + s);
        HttpURLConnection urlConnection = (HttpURLConnection) uri.toURL().openConnection();

        method = DhpApiClient.class.getDeclaredMethod("addRequestBody", String.class, HttpURLConnection.class);
        method.setAccessible(true);
        method.invoke(mDhpApiClient, s, urlConnection);
    }

    @Test
    public void testOpenHttpURLConnection() throws Exception {
        Method method = null;
        String s = "sample";
        URI uri = URI.create(s + s + "?" + s);

        method = DhpApiClient.class.getDeclaredMethod("openHttpURLConnection", URI.class);
        method.setAccessible(true);
        method.invoke(mDhpApiClient, uri);
    }

    @Test
    public void testAddRequestHeaders() throws Exception {
        Method method = null;
        String s = "sample";
        Map<String, String> headers = new HashMap<>();
        URI uri = URI.create(s + s + "?" + s);
        HttpURLConnection urlConnection = null;
        urlConnection = (HttpURLConnection) uri.toURL().openConnection();

        method = DhpApiClient.class.getDeclaredMethod("addRequestHeaders", Map.class, HttpURLConnection.class);
        method.setAccessible(true);
        method.invoke(mDhpApiClient, headers, urlConnection);
    }

    @Test
    public void testSign() throws Exception {
        Method method = null;
        String s = "sample";
        Map<String, String> headers = new HashMap<>();

        method = DhpApiClient.class.getDeclaredMethod("sign", Map.class, String.class, String.class, String.class, String.class);
        method.setAccessible(true);
        method.invoke(mDhpApiClient, headers, s, s, s, s);
    }

}
