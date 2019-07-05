/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
package com.philips.cdp.di.iap.applocal;

import android.content.Context;

import com.philips.cdp.di.iap.integration.MockIAPDependencies;
import com.philips.cdp.di.iap.integration.MockIAPSetting;
import com.philips.cdp.di.iap.networkEssential.LocalNetworkEssentials;
import com.philips.cdp.di.iap.session.OAuthListener;
import com.philips.cdp.di.iap.session.RequestListener;
import com.philips.platform.appinfra.AppInfra;
import com.philips.platform.pif.DataInterface.USR.UserDataInterface;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.robolectric.RobolectricTestRunner;

import static androidx.test.platform.app.InstrumentationRegistry.getInstrumentation;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertNull;
import static org.mockito.Mockito.mock;

@RunWith(RobolectricTestRunner.class)
public class LocalNetworkEssentialsTest {
    private Context mContext;

    @Mock
    private OAuthListener oAuthHandler;

    private MockIAPSetting mockIAPSetting;
    private LocalNetworkEssentials mLocalNetworkEssentials;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        mContext = getInstrumentation().getContext();
        mockIAPSetting = new MockIAPSetting(mContext);
        mLocalNetworkEssentials = new LocalNetworkEssentials();

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

    @Test
    public void testGetStore() throws Exception {
        assertNotNull(mLocalNetworkEssentials.getStore(mContext, mockIAPSetting,new MockIAPDependencies(mock(AppInfra.class),mock(UserDataInterface.class))));
    }

    @Test
    public void testHurlStack() throws Exception {
        assertNotNull(mLocalNetworkEssentials.getHurlStack(mContext, oAuthHandler));
    }

    @Test
    public void testGetOAuthHandler() throws Exception {
        assertNull(mLocalNetworkEssentials.getOAuthHandler());
    }
}