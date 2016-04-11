package com.philips.cdp.di.iap.store;

import android.content.Context;

import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class WebStoreConfigTest {

    @Mock
    Context mContext;

    /*@Test
    public void testGetCountryLocaleDefaultIsNull() {
        WebStoreConfig config = new WebStoreConfig(mContext);
        assertNull(config.getLocale());
    }

    @Test
    public void testGetCountryLocaleDefaultAfterInitConfig() {
        WebStoreConfig config = new WebStoreConfig(mContext) {
            @Override
            void initLocaleMatcher() {
                mPILLocale = mock(PILLocale.class);
                when(mPILLocale.getLocaleCode()).thenReturn("en_US");
            }
        };
        config.initConfig("en_US", listener);
        assertNotNull(config.getLocale());
    }
    @Test
    public void localeIsNullIfLocaleMatcherFailed() {
        WebStoreConfig config = new WebStoreConfig(mContext) {
            @Override
            void initLocaleMatcher() {
                mPILLocale = null;
            }
        };
        config.initConfig("en_US", listener);
        assertNull(config.getLocale());
    }*/
}