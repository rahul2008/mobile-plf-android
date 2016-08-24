package com.philips.cdp.di.iap.store;

import android.content.Context;

import com.philips.cdp.di.iap.integration.IAPDependencies;
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

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
@RunWith(RobolectricTestRunner.class)
public class VerticalAppConfigTest extends TestCase {
    MockIAPDependencies mIAPDependencies;
    @Mock
    Context mContext;
    @Mock
    AppInfra mAppInfra;

    @Before
    public void setUP() {
        MockitoAnnotations.initMocks(this);
        mIAPDependencies = new MockIAPDependencies(mAppInfra);
    }

    @Test
    public void testPropositionIDIsTuscany2016() {
        MockVerticalAppConfig mockConfig = new MockVerticalAppConfig(mIAPDependencies);
        assertEquals("Tuscany2016", mockConfig.getProposition());
        assertEquals("acc.occ.shop.philips.com", mockConfig.getHostPort());
    }

    @Test
    public void testIOExceptionForWrongFileInput() {
        VerticalAppConfig mockConfig = new VerticalAppConfig(mIAPDependencies) {
            @Override
            void loadConfigurationFromAsset(IAPDependencies iapDependencies) {
                // super.loadConfigurationFromAsset(iapDependencies);
            }
        };

        assertNull(mockConfig.getProposition());
        assertNull(mockConfig.getHostPort());
    }

    @Test(expected = NullPointerException.class)
    public void testNullPointerExceptionWhenMockedReadJSONInputStream() throws IOException {
        VerticalAppConfig config = new VerticalAppConfig(mIAPDependencies);
        config.loadConfigurationFromAsset(mIAPDependencies);
    }
}
