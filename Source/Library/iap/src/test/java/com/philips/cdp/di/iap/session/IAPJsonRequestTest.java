package com.philips.cdp.di.iap.session;

import com.android.volley.NetworkResponse;
import com.philips.cdp.di.iap.TestUtils;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

@RunWith(RobolectricTestRunner.class)
public class IAPJsonRequestTest {
    private IAPJsonRequest mIAIapJsonRequest;

    @Before
    public void setUp() throws Exception {
        mIAIapJsonRequest = new IAPJsonRequest(1,null,null,null,null);
    }

    @Test(expected = JSONException.class)
    public void testParseNetworkResponse() throws Exception{
        JSONObject obj = new JSONObject(TestUtils.readFile(IAPJsonRequestTest.class, "json_exception.txt"));
        NetworkResponse networkResponse = new NetworkResponse(obj.toString().getBytes("utf-8"));
        mIAIapJsonRequest.parseNetworkResponse(networkResponse);
    }

}