package com.philips.cdp.registration.restclient;

import com.philips.cdp.registration.BuildConfig;
import com.philips.cdp.registration.CustomRobolectricRunner;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.annotation.Config;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by philips on 06/02/18.
 */

@RunWith(CustomRobolectricRunner.class)
@Config(constants = BuildConfig.class, sdk = 21)
public class URRestClientStringRequestTest {

    private URRestClientStringRequest urRestClientStringRequest;

    @Before
    public void setUp() throws Exception {
        urRestClientStringRequest = new URRestClientStringRequest(1, "", "", null, null, null, null, null);
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
}