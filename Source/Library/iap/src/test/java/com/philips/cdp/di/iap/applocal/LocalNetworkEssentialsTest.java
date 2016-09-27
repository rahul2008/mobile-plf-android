package com.philips.cdp.di.iap.applocal;

import android.content.Context;

import com.philips.cdp.di.iap.integration.MockIAPDependencies;
import com.philips.cdp.di.iap.session.OAuthHandler;
import com.philips.cdp.di.iap.session.RequestListener;
import com.philips.platform.appinfra.AppInfra;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.robolectric.RobolectricTestRunner;

import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertNull;

/**
 * Created by indrajitkumar on 27/09/16.
 */
@RunWith(RobolectricTestRunner.class)
public class LocalNetworkEssentialsTest {
    LocalNetworkEssentials mLocalNetworkEssentials;
    @Mock
    Context mContext;
    @Mock
    AppInfra mAppInfra;
    MockIAPDependencies mockIAPDependencies;
    @Mock
    OAuthHandler oAuthHandler;

    @Before
    public void setUP() {
        MockitoAnnotations.initMocks(this);
        mockIAPDependencies = new MockIAPDependencies(mAppInfra);
        mLocalNetworkEssentials = new LocalNetworkEssentials();
        oAuthHandler = new OAuthHandler() {
            @Override
            public String getAccessToken() {
                return null;
            }

            @Override
            public void refreshToken(RequestListener listener) {

            }

            @Override
            public void resetAccessToken() {

            }
        };
    }

    @Test
    public void getStoreNotNull() throws Exception {
        assertNotNull(mLocalNetworkEssentials.getStore(mContext, mockIAPDependencies));
    }

    @Test
    public void hurlStackNotNull() throws Exception {
        assertNotNull(mLocalNetworkEssentials.getHurlStack(mContext, oAuthHandler));
    }

    @Test
    public void getOAuthHandlerNull() throws Exception {
        assertNull(mLocalNetworkEssentials.getOAuthHandler());
    }
}