/*
 * Copyright (c) 2017 Koninklijke Philips N.V.
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */

package com.philips.platform.catk.model;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

import org.junit.*;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.configuration.MockitoConfiguration;

import com.android.volley.Request;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.philips.cdp.registration.User;
import com.philips.platform.catk.ConsentAccessToolKitManipulator;
import com.philips.platform.catk.injection.CatkComponent;
import com.philips.platform.catk.network.NetworkAbstractModel;
import com.philips.platform.catk.response.ConsentStatus;

public class GetConsentsModelRequestTest extends MockitoConfiguration {

    private GetConsentsModelRequest consentModelRequest;

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
        when(mockUser.getHsdpUUID()).thenReturn(subject);
        consentModelRequest = new GetConsentsModelRequest("https://hdc-css-mst.cloud.pcftest.com/consent/", APPLICATION_NAME, PROPOSITION_NAME, mockDataLoadListener);
    }

    @Test
    public void parseResponse() throws Exception {
        JsonArray jsonArray = new JsonArray();
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("dateTime", dateTime);
        jsonObject.addProperty("language", language);
        jsonObject.addProperty("policyRule", policyRule);
        jsonObject.addProperty("resourceType", resourceType);
        jsonObject.addProperty("status", status);
        jsonObject.addProperty("subject", subject);
        jsonArray.add(jsonObject);
        assertArrayEquals(expectedConsentModelRequest, consentModelRequest.parseResponse(jsonArray));
    }

    @Test
    public void testGetMethod() throws Exception {
        assertEquals(Request.Method.GET, consentModelRequest.getMethod());
    }

    @Test
    public void testRequestHeader() throws Exception {
        Assert.assertNotNull(consentModelRequest.requestHeader());
        assertEquals("1", consentModelRequest.requestHeader().get("api-version"));
        assertEquals("application/json", consentModelRequest.requestHeader().get("content-type"));
        assertEquals("bearer x73ywf56h46h5p25", consentModelRequest.requestHeader().get("authorization"));
        assertEquals(subject, consentModelRequest.requestHeader().get("performerid"));
        assertEquals("no-cache", consentModelRequest.requestHeader().get("cache-control"));
    }

    @Test
    public void testRequestBody() throws Exception {
        Assert.assertNull(consentModelRequest.requestBody());
    }

    @Test
    public void testGetUrl() throws Exception {
        assertEquals("https://hdc-css-mst.cloud.pcftest.com/consent/" + subject + "?applicationName=" + APPLICATION_NAME + "&propositionName=" + PROPOSITION_NAME,
                consentModelRequest.getUrl());
    }

    @Test
    public void testGetUrlWhenBaseUrlDoesntTerminateWithSlash() throws Exception {
        givenUrlDoesntTerminateWithSlash();
        assertEquals("https://hdc-css-mst.cloud.pcftest.com/consent/" + subject + "?applicationName=" + APPLICATION_NAME + "&propositionName=" + PROPOSITION_NAME,
                consentModelRequest.getUrl());
    }

    private void givenUrlDoesntTerminateWithSlash() {
        consentModelRequest = new GetConsentsModelRequest("https://hdc-css-mst.cloud.pcftest.com/consent", APPLICATION_NAME, PROPOSITION_NAME, mockDataLoadListener);
    }

    private static final String APPLICATION_NAME = "OneBackend";
    private static final String PROPOSITION_NAME = "OneBackendProp";
    private final String dateTime = "2017-11-01T17:27:16.000Z";
    private final String language = "af-ZA";
    private final String policyRule = "urn:com.philips.consent";
    private final String status = "active";
    private final String subject = "17f7ce85-403c-4824-a17f-3b551f325ce0";
    private final String resourceType = "Consent";
    private GetConsentsModel[] expectedConsentModelRequest = new GetConsentsModel[] { new GetConsentsModel(dateTime, language, policyRule, resourceType,
            ConsentStatus.valueOf(status), subject) };
}