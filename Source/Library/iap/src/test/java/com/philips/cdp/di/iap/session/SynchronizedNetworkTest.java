package com.philips.cdp.di.iap.session;

import com.android.volley.Response;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.HurlStack;

import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.robolectric.RobolectricTestRunner;

/**
 * Created by indrajitkumar on 27/09/16.
 */
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

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        mSynchronizedNetwork = new SynchronizedNetwork(mHurlStack);
    }

    @Test(expected = NullPointerException.class)
    public void performRequest() throws Exception {
        mSynchronizedNetwork.performRequest(mIAIapJsonRequest, mSynchronizedNetworkCallBack);
    }

    @Test
    public void successResponse() throws Exception {
        mSynchronizedNetwork.successResponse(mIAIapJsonRequest, mSynchronizedNetworkCallBack, null);
        Response<JSONObject> response = mIAIapJsonRequest.parseNetworkResponse(null);
        Mockito.verify(mSynchronizedNetworkCallBack, Mockito.atLeast(1)).onSyncRequestSuccess(response);
    }
}