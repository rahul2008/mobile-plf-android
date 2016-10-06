package com.philips.cdp.di.iap.hybris;

import android.content.Context;

import com.philips.cdp.di.iap.integration.MockIAPDependencies;
import com.philips.cdp.di.iap.networkEssential.HybrisNetworkEssentials;
import com.philips.cdp.di.iap.session.OAuthListener;
import com.philips.cdp.di.iap.session.RequestListener;
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
public class HybrisNetworkEssentialsTest {
    HybrisNetworkEssentials mHybrisNetworkEssentials;
    MockIAPDependencies mockIAPDependencies;
    OAuthListener oAuthHandler;
    @Mock
    AppInfra mAppInfra;
    @Mock
    Context mContext;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        mHybrisNetworkEssentials = new HybrisNetworkEssentials();
        mockIAPDependencies = new MockIAPDependencies(mAppInfra);
        oAuthHandler = new OAuthListener() {
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

    @Test(expected = NullPointerException.class)
    public void getHurlStackShouldNotNull() throws Exception {
        mHybrisNetworkEssentials.getHurlStack(mContext, oAuthHandler);
    }
}