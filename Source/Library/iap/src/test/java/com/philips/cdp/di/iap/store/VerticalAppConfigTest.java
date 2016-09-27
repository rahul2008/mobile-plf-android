/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
package com.philips.cdp.di.iap.store;

import android.content.Context;

import com.philips.cdp.di.iap.integration.MockIAPDependencies;
import com.philips.platform.appinfra.AppInfra;

import junit.framework.TestCase;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.robolectric.RobolectricTestRunner;

import java.io.IOException;

import static junit.framework.Assert.assertEquals;

@RunWith(RobolectricTestRunner.class)
public class VerticalAppConfigTest {
    MockIAPDependencies mIAPDependencies;
    MockVerticalAppConfig mMockVerticalAppConfig;
    @Mock
    Context mContext;
    @Mock
    AppInfra mAppInfra;

    @Before
    public void setUP() {
        MockitoAnnotations.initMocks(this);
        mIAPDependencies = new MockIAPDependencies(mAppInfra);
        mMockVerticalAppConfig = new MockVerticalAppConfig(mIAPDependencies);
    }

    @Test
    public void testIsValidPropositionId() {
        assertEquals("Tuscany2016", mMockVerticalAppConfig.getProposition());
    }

    @Test
    public void testIsValidHostPort() {
        assertEquals("acc.occ.shop.philips.com", mMockVerticalAppConfig.getHostPort());
    }

    @Test(expected = NullPointerException.class)
    public void testNullPointerExceptionWhenMockedReadJSONInputStream() throws IOException {
        VerticalAppConfig config = new VerticalAppConfig(mIAPDependencies);
        config.loadConfigurationFromAsset(mIAPDependencies);
    }

    @Test
    public void testWithMockedAppConfig() throws IOException {
        MockVerticalAppConfig config = new MockVerticalAppConfig(mIAPDependencies);
        config.loadConfigurationFromAsset(mIAPDependencies);
    }

}
