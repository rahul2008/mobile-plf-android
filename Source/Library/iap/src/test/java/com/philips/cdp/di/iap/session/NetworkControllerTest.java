package com.philips.cdp.di.iap.session;

import android.content.Context;
import android.os.Message;

import com.philips.cdp.di.iap.networkEssential.NetworkEssentials;
import com.philips.cdp.di.iap.integration.MockIAPDependencies;
import com.philips.platform.appinfra.AppInfra;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.robolectric.RobolectricTestRunner;

/**
 * Created by indrajitkumar on 27/09/16.
 */
@RunWith(RobolectricTestRunner.class)
public class NetworkControllerTest {
    @Mock
    AppInfra mAppInfra;
    @Mock
    Context mContext;
    @Mock
    NetworkEssentials mNetworkEssentials;
    NetworkController mNetworkController;
    MockIAPDependencies mockIAPDependencies;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        mockIAPDependencies = new MockIAPDependencies(mAppInfra);
        mNetworkController = new NetworkController(mContext, mNetworkEssentials, mockIAPDependencies);
    }

    @Test
    public void initHurlStackNotNull() throws Exception {
        mNetworkController.initHurlStack(mContext);
    }

    @Test
    public void hybrisVolleyCreateConnectionNotNull() throws Exception {
        mNetworkController.hybrisVolleyCreateConnection(mContext);
    }

    @Test
    public void refreshOAuthTokenNotNull() throws Exception {
        mNetworkController.refreshOAuthToken(new RequestListener() {
            @Override
            public void onSuccess(Message msg) {

            }

            @Override
            public void onError(Message msg) {

            }
        });
    }

    @Test(expected = NullPointerException.class)
    public void sendHybrisRequest() throws Exception {
        mNetworkController.sendHybrisRequest(1, null, null);
    }
    @Test
    public void addToVolleyQueue() throws Exception {
        mNetworkController.addToVolleyQueue(new IAPJsonRequest(1,null,null,null,null));
    }
}