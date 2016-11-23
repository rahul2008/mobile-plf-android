/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
package com.philips.cdp.di.iap.session;

import android.content.Context;
import android.os.Message;

import com.philips.cdp.di.iap.integration.MockIAPSetting;
import com.philips.cdp.di.iap.networkEssential.NetworkEssentials;
import com.philips.platform.appinfra.AppInfra;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.robolectric.RobolectricTestRunner;

import static junit.framework.Assert.assertEquals;

@RunWith(RobolectricTestRunner.class)
public class NetworkControllerTest {
    @Mock
    AppInfra mAppInfra;
    @Mock
    Context mContext;
    @Mock
    NetworkEssentials mNetworkEssentials;
    NetworkController mNetworkController;
    MockIAPSetting mockIAPSetting;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        mockIAPSetting = new MockIAPSetting(mContext);
        mNetworkController = new NetworkController(mContext, mNetworkEssentials, mockIAPSetting);
    }

    @Test(expected = NullPointerException.class)
    public void testNetworkControllerWithContext() {
        new NetworkController(mContext);
    }

    @Test
    public void testInitHurlStack() throws Exception {
        mNetworkController.initHurlStack(mContext);
    }

    @Test
    public void testVolleyCreateConnection() throws Exception {
        mNetworkController.hybrisVolleyCreateConnection(mContext);
    }

    @Test
    public void testRefreshOAuthToken() throws Exception {
        mNetworkController.refreshOAuthToken(new RequestListener() {
            @Override
            public void onSuccess(Message msg) {

            }

            @Override
            public void onError(Message msg) {

            }
        });
    }

    @Test
    public void testRefreshOAuthTokenWithNullListener() {
        mNetworkController.refreshOAuthToken(null);
    }

    @Test(expected = NullPointerException.class)
    public void testSendHybrisRequest() throws Exception {
        mNetworkController.sendHybrisRequest(1, null, null);
    }

    @Test
    public void testAddToVolleyQueue() throws Exception {
        new VolleyWrapper();
        assertEquals(VolleyWrapper.DEFAULT_CACHE_DIR, "volley");
        assertEquals(VolleyWrapper.DEFAULT_NETWORK_THREAD_POOL_SIZE, 4);
        mNetworkController.addToVolleyQueue(new IAPJsonRequest(1, null, null, null, null));
    }
}