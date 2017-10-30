package com.philips.platform.catk.network;


import com.android.volley.Response;
import com.google.gson.JsonArray;
import com.philips.cdp.registration.User;
import com.philips.platform.appinfra.rest.RestInterface;
import com.philips.platform.appinfra.rest.request.RequestQueue;
import com.philips.platform.catk.CatkConstants;
import com.philips.platform.catk.model.GetConsentsModelRequest;
import com.philips.platform.catk.request.ConsentRequest;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Map;

import static org.mockito.Mockito.when;

public class NetworkControllerTest {

    private NetworkControllerCustom networkController;

    @Mock
    RestInterface mockRestInterface;

    GetConsentsModelRequest consentsModelRequest;

    @Mock
    Response.Listener listener;

    @Mock
    Response.ErrorListener errorListener;

    @Mock
    ConsentRequest mockConsentRequest;

    @Mock
    NetworkAbstractModel.DataLoadListener mockDataLoadListener;

    @Mock
    User mockUser;

    @Mock
    RequestQueue mockRequestQueue;

    @Mock
    NetworkController mockNetworkController;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        networkController = new NetworkControllerCustom();
        when(mockUser.getHsdpAccessToken()).thenReturn("x73ywf56h46h5p25");
        when(mockUser.getHsdpUUID()).thenReturn("17f7ce85-403c-4824-a17f-3b551f325ce0");
        consentsModelRequest = new GetConsentsModelRequest(CatkConstants.APPLICATION_NAME,CatkConstants.PROPOSITION_NAME,mockDataLoadListener);
        when(mockConsentRequest.getMethod()).thenReturn(0);
        final String url = consentsModelRequest.getUrl();
        when(mockConsentRequest.getUrl()).thenReturn(url);
        final Map<String, String> headers = consentsModelRequest.requestHeader();
        when(mockConsentRequest.getHeaders()).thenReturn(headers);
        when(mockRestInterface.getRequestQueue()).thenReturn(mockRequestQueue);
    }

    @After
    public void tearDown(){
        networkController = null;
    }

    @Test
    public void testSendRequest(){
        networkController.sendConsentRequest(0, consentsModelRequest, consentsModelRequest);
    }

    class NetworkControllerCustom extends NetworkController{

        protected void init(){
            restInterface = mockRestInterface;
        }

        protected ConsentRequest getConsentJsonRequest(final NetworkAbstractModel model, final Response.ErrorListener error, final Response.Listener<JsonArray> response) {
            return mockConsentRequest;
        }

    }
}