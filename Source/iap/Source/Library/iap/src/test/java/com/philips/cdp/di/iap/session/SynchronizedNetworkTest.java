package com.philips.cdp.di.iap.session;

import com.android.volley.NetworkResponse;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.HurlStack;
import com.philips.cdp.di.iap.TestUtils;

import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.robolectric.RobolectricTestRunner;

import java.util.HashMap;

import static junit.framework.Assert.assertTrue;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(RobolectricTestRunner.class)
public class SynchronizedNetworkTest {
    SynchronizedNetwork mSynchronizedNetwork;
    @Mock
    HurlStack mHurlStack;
    @Mock
    BasicNetwork basicNetwork;
    @Mock
    IAPJsonRequest mIAIapJsonRequest;
    @Mock
    SynchronizedNetworkListener mSynchronizedNetworkCallBack;

    @Mock
    VolleyError volleyErrorMock;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        mSynchronizedNetwork = new SynchronizedNetwork(mHurlStack);
        mSynchronizedNetwork.mBasicNetwork = basicNetwork;
    }

    @Test
    public void performRequestWithVolleyError() throws Exception {
        doThrow(volleyErrorMock).when(basicNetwork).performRequest(mIAIapJsonRequest);
        mSynchronizedNetwork.performRequest(mIAIapJsonRequest, mSynchronizedNetworkCallBack);
        Mockito.verify(mSynchronizedNetworkCallBack, Mockito.atLeast(1)).onSyncRequestError(volleyErrorMock);
    }

    @Test(expected = NullPointerException.class)
    public void performRequestWithVolleyErrorRequiresRedirection() throws Exception {
        when(mIAIapJsonRequest.getUrl()).thenReturn("hh");

        HashMap map = new HashMap();
        map.put("Location","hh");

        NetworkResponse networkResponse = new NetworkResponse(301,null,map,false,12L);
        VolleyError volleyError = new VolleyError(networkResponse);


        doThrow(volleyError).when(basicNetwork).performRequest(mIAIapJsonRequest);
        mSynchronizedNetwork.mBasicNetwork = basicNetwork;
        mSynchronizedNetwork.performRequest(mIAIapJsonRequest, mSynchronizedNetworkCallBack);
        Mockito.verify(basicNetwork, Mockito.atLeast(1)).performRequest(mIAIapJsonRequest);
    }

    @Test
    public void testSuccessResponse() throws Exception {
        mSynchronizedNetwork.successResponse(mIAIapJsonRequest, mSynchronizedNetworkCallBack, null);
        Response<JSONObject> response = mIAIapJsonRequest.parseNetworkResponse(null);
        Mockito.verify(mSynchronizedNetworkCallBack, Mockito.atLeast(1)).onSyncRequestSuccess(response);
    }



}