/*
 * Copyright (c) 2017 Koninklijke Philips N.V.
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */

package com.philips.platform.catk.network;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.HashMap;
import java.util.Map;

import org.junit.*;
import org.mockito.*;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.google.gson.JsonArray;
import com.philips.cdp.registration.User;
import com.philips.platform.appinfra.rest.RestInterface;
import com.philips.platform.appinfra.rest.request.RequestQueue;
import com.philips.platform.catk.injection.CatkComponent;
import com.philips.platform.catk.model.GetConsentsModelRequest;
import com.philips.platform.catk.request.ConsentRequest;

public class NetworkControllerTest {

    private NetworkControllerCustom networkController;

    @Mock
    RestInterface mockRestInterface;

    GetConsentsModelRequest consentsModelRequest;

    @Mock
    ConsentRequest mockConsentRequest;

    @Mock
    NetworkAbstractModel.DataLoadListener mockDataLoadListener;

    @Mock
    User mockUser;

    @Mock
    RequestQueue mockRequestQueue;

    @Captor
    ArgumentCaptor<ConsentRequest> captorConsentRequest;

    @Mock
    private CatkComponent mockCatkComponent;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        networkController = new NetworkControllerCustom();
        when(mockUser.getHsdpAccessToken()).thenReturn("x73ywf56h46h5p25");
        when(mockUser.getHsdpUUID()).thenReturn("17f7ce85-403c-4824-a17f-3b551f325ce0");
        when(mockConsentRequest.getMethod()).thenReturn(com.android.volley.Request.Method.GET);
        when(mockConsentRequest.getUrl()).thenReturn("https://philips.com");
        when(mockConsentRequest.getHeaders()).thenReturn(getRequestHeader());
        when(mockRestInterface.getRequestQueue()).thenReturn(mockRequestQueue);
        consentsModelRequest = new GetConsentsModelRequest(URL, APPLICATION_NAME, PROPOSITION_NAME, mockDataLoadListener);
    }

    @After
    public void tearDown() {
        networkController = null;
    }

    @Test
    public void testSendRequest() throws AuthFailureError {
        networkController.sendConsentRequest(consentsModelRequest);
        verify(mockRequestQueue).add(captorConsentRequest.capture());

        assertEquals(getRequestHeader(), captorConsentRequest.getValue().getHeaders());
        assertEquals("https://philips.com", captorConsentRequest.getValue().getUrl());
        assertEquals(com.android.volley.Request.Method.GET, captorConsentRequest.getValue().getMethod());
    }

    private Map<String, String> getRequestHeader() {
        Map<String, String> params = new HashMap<String, String>();
        params.put("api-version", "1");
        params.put("content-type", "application/json");
        params.put("authorization", "bearer x73ywf56h46h5p25");
        params.put("performerid", "17f7ce85-403c-4824-a17f-3b551f325ce0");
        params.put("cache-control", "no-cache");
        return params;
    }

    class NetworkControllerCustom extends NetworkController {

        protected void init() {
            restInterface = mockRestInterface;
        }

        protected ConsentRequest getConsentJsonRequest(final NetworkAbstractModel model, final Response.ErrorListener error, final Response.Listener<JsonArray> response) {
            return mockConsentRequest;
        }

    }

    private static final String APPLICATION_NAME = "OneBackend";
    private static final String PROPOSITION_NAME = "OneBackendProp";
    private final String URL = "https://hdc-css-mst.cloud.pcftest.com/consent";
}