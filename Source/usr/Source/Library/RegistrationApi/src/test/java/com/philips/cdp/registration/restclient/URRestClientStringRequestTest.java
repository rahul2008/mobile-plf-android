package com.philips.cdp.registration.restclient;

import com.android.volley.NetworkResponse;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.philips.cdp.registration.BuildConfig;
import com.philips.cdp.registration.CustomRobolectricRunner;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.robolectric.annotation.Config;

import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by philips on 06/02/18.
 */

@RunWith(CustomRobolectricRunner.class)
@Config(constants = BuildConfig.class, sdk = 21)
public class URRestClientStringRequestTest {

    private URRestClientStringRequest urRestClientStringRequest;

    @Mock
    private  Response.Listener<String> mResponseListener;

    @Mock
    private Response.ErrorListener mErrorListener;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        urRestClientStringRequest = new URRestClientStringRequest("", "", null, mResponseListener, mErrorListener, isHeaderRequired);
    }

    @Test
    public void testGetBodyContentType() throws Exception {
        final String bodyContentType = urRestClientStringRequest.getBodyContentType();
        Assert.assertSame(bodyContentType, "application/x-www-form-urlencoded; charset=UTF-8");
    }

    @Test
    public void testGetHedder() throws Exception {
        final Map<String, String> headers = urRestClientStringRequest.getHeaders();
        Map<String, String> params = new HashMap<String, String>();
        params.put("cache-control", "no-cache");
        params.put("content-type", "application/x-www-form-urlencoded");
        Assert.assertNotNull(params);
    }

    @Test
    public void testGetBody() throws Exception {
        final byte[] body = urRestClientStringRequest.getBody();
        Assert.assertNotNull(body);
    }

    @Test
    public void testGetBodyIfBodyNull() throws Exception {
        urRestClientStringRequest = new URRestClientStringRequest("", null, null, null, null, isHeaderRequired);
        final byte[] body = urRestClientStringRequest.getBody();
        Assert.assertNotNull(body);
    }


    @Test
    public void testParseNetworkResponse() throws Exception {
        final String s = new String("This".getBytes(Charset.forName("UTF-8")));
        Map<String ,String> header =  new HashMap<>();
        header.put("cache-control", "no-cache");
        header.put("Content-type", "application/x-www-form-urlencoded");
        NetworkResponse networkResponse = new NetworkResponse(200,s.getBytes(),header,false,0);
        final Response<String> response = urRestClientStringRequest.parseNetworkResponse(networkResponse);
        Assert.assertNotNull(response);
    }



    @Test
    public void testParseNetworkErrorWithNetworkResponseNull() throws Exception {
        VolleyError error = new VolleyError();
        VolleyError volleyError = urRestClientStringRequest.parseNetworkError(error);
        Assert.assertNotNull(volleyError);
    }

    @Test
    public void testParseNetworkErrorWithNetworkResponse() throws Exception {
        NetworkResponse networkResponse = new NetworkResponse(200,new byte[1],null,false,0);
        VolleyError error = new VolleyError(networkResponse);
        VolleyError volleyError = urRestClientStringRequest.parseNetworkError(error);
        Assert.assertNotNull(volleyError);
    }

    @Test
    public void testDeliverResponse() throws Exception {
        final String s = new String("This".getBytes(Charset.forName("UTF-8")));
        urRestClientStringRequest.deliverResponse(s);
    }

    @Test
    public void testDeliverError() throws Exception {
        NetworkResponse networkResponse = new NetworkResponse(200,new byte[1],null,false,0);
        VolleyError error = new VolleyError(networkResponse);
        urRestClientStringRequest.deliverError(error);
    }
}