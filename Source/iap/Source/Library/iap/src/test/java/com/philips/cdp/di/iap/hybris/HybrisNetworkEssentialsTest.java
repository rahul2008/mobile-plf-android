package com.philips.cdp.di.iap.hybris;

import android.content.Context;
import com.philips.cdp.di.iap.networkEssential.HybrisNetworkEssentials;
import com.philips.cdp.di.iap.session.OAuthListener;
import com.philips.cdp.di.iap.session.RequestListener;
import com.philips.platform.pif.DataInterface.USR.UserDataInterface;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import static androidx.test.platform.app.InstrumentationRegistry.getInstrumentation;
import static org.mockito.Mockito.mock;

@RunWith(RobolectricTestRunner.class)
public class HybrisNetworkEssentialsTest {
    private HybrisNetworkEssentials mHybrisNetworkEssentials;
    private OAuthListener oAuthHandler;
    private Context mContext;

    @Before
    public void setUp() {
        mContext = getInstrumentation().getContext();
        mHybrisNetworkEssentials = new HybrisNetworkEssentials(mock(UserDataInterface.class));
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
    public void getHurlStackShouldNotNull() throws Exception {
        mHybrisNetworkEssentials.getHurlStack(mContext, oAuthHandler);
    }
}