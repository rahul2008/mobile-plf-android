
/*
 * Copyright (c) 2015-2018 Koninklijke Philips N.V.
 * All rights reserved.
 */

package com.philips.dhpclient;

import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

/**
 * Created by 310243576 on 8/25/2016.
 */
public class DhpApiSignerTest {
    private DhpApiSigner mDhpApiSigner;

    @Before
    public void setUp() throws Exception {
        try {
            mDhpApiSigner = new DhpApiSigner(null, null);
        } catch (IllegalArgumentException ignored) {
        }
        mDhpApiSigner = new DhpApiSigner("sample", "sample");
    }

    @Test
    public void testDhpApiClient() {
        assertNotNull(mDhpApiSigner);
        Map<String, String> headers = new HashMap<String, String>();
        assertNotNull(mDhpApiSigner.buildAuthorizationHeaderValue("requestMethod", "queryString", headers, "dhpUrl", "requestbody"));
    }

    @Test
    public void testJoinHeaders() {
        Method method = null;
        Map<String, String> headers = new HashMap<String, String>();
        headers.put("key", "value");
        try {
            method = DhpApiSigner.class.getDeclaredMethod("joinHeaders", Map.class);
            method.setAccessible(true);
            method.invoke(mDhpApiSigner, headers);
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            fail();
        }
    }

    @Test
    public void testJoinHeaders2() {
        Method method = null;
        List<String> headers = new ArrayList<String>();
        headers.add("value");
        try {
            method = DhpApiSigner.class.getDeclaredMethod("joinHeaders", List.class);
            method.setAccessible(true);
            method.invoke(mDhpApiSigner, headers);
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            fail();
        }
    }

    @Test
    public void testBuildAuthorizationHeaderValue() {
        StringBuilder requestHeader = new StringBuilder();
        requestHeader.append(";");
        requestHeader.append("Credential:");
        requestHeader.append("sharedKey");
        requestHeader.append(";");
        requestHeader.append("SignedHeaders:");
        String header = requestHeader.toString();
        String signature = "signature";
        Method method = null;

        try {
            method = DhpApiSigner.class.getDeclaredMethod("buildAuthorizationHeaderValue", String.class, String.class);
            method.setAccessible(true);
            method.invoke(mDhpApiSigner, header, signature);
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            fail();
        }
    }

    @Test
    public void testHash() {
        Method method = null;
        String data = "data";
        byte[] key = new byte[1];
        key[0] = -1;

        try {
            method = DhpApiSigner.class.getDeclaredMethod("hash", String.class, byte[].class);
            method.setAccessible(true);
            method.invoke(mDhpApiSigner, data, key);
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            fail();
        }
    }
}
