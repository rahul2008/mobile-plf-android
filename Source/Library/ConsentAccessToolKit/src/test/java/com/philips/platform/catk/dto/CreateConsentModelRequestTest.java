/*
 * Copyright (c) 2017 Koninklijke Philips N.V.
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */

package com.philips.platform.catk.dto;

import com.android.volley.Request;
import com.google.gson.JsonArray;
import com.philips.cdp.registration.User;
import com.philips.platform.catk.ConsentAccessToolKitManipulator;
import com.philips.platform.catk.injection.CatkComponent;
import com.philips.platform.catk.network.NetworkAbstractModel;
import com.philips.platform.catk.network.NetworkController;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.configuration.MockitoConfiguration;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.when;

public class CreateConsentModelRequestTest extends MockitoConfiguration {

    private CreateConsentModelRequest consentModelRequest;

    @Mock
    User mockUser;

    @Mock
    NetworkAbstractModel.DataLoadListener mockDataLoadListener;

    @Mock
    private CatkComponent mockCatkComponent;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        ConsentAccessToolKitManipulator.setCatkComponent(mockCatkComponent);
        when(mockCatkComponent.getUser()).thenReturn(mockUser);
        when(mockUser.getHsdpAccessToken()).thenReturn("x73ywf56h46h5p25");
        when(mockUser.getHsdpUUID()).thenReturn(SUBJECT);
        consentModelRequest = new CreateConsentModelRequest(URL, APPLICATION_NAME, "active",
                PROPOSITION_NAME, "af-ZA", mockDataLoadListener);
    }

    @Test
    public void parseResponse() throws Exception {
        JsonArray jsonArray = new JsonArray();
        jsonArray.add("success");
        assertNull(consentModelRequest.parseResponse(jsonArray));
    }

    @Test
    public void testGetMethod() throws Exception {
        assertEquals(Request.Method.POST, consentModelRequest.getMethod());
    }

    @Test
    public void testRequestHeader() throws Exception {
        assertNotNull(NetworkController.requestHeader());
        assertEquals("1", NetworkController.requestHeader().get("api-version"));
        assertEquals("application/json", NetworkController.requestHeader().get("content-type"));
        assertEquals("bearer x73ywf56h46h5p25", NetworkController.requestHeader().get("authorization"));
        assertEquals(SUBJECT, NetworkController.requestHeader().get("performerid"));
        assertEquals("no-cache", NetworkController.requestHeader().get("cache-control"));
    }

    @Test
    public void testRequestBody() throws Exception {
        assertEquals(EXPECTED_BODY, consentModelRequest.requestBody());
    }

    @Test
    public void testGetUrl() throws Exception {
        assertEquals(URL, consentModelRequest.getUrl());
    }

    private static final String EXPECTED_BODY = "{\"resourceType\":\"Consent\",\"language\":\"af-ZA\",\"status\":\"active\",\"subject\":\"17f7ce85-403c-4824-a17f-3b551f325ce0\",\"policyRule\":\"urn:com.philips.consent:moment/null/0/OneBackendProp/OneBackend\"}";
    private static final String APPLICATION_NAME = "OneBackend";
    private static final String PROPOSITION_NAME = "OneBackendProp";
    private final String URL = "https://hdc-css-mst.cloud.pcftest.com/consent";
    private final String SUBJECT = "17f7ce85-403c-4824-a17f-3b551f325ce0";
}