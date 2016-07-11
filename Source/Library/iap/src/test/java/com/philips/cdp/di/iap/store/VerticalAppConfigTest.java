package com.philips.cdp.di.iap.store;

import android.content.Context;

import junit.framework.TestCase;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.robolectric.RobolectricTestRunner;

import java.io.IOException;
import java.io.InputStream;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
@RunWith(RobolectricTestRunner.class)
public class VerticalAppConfigTest extends TestCase {
    @Mock
    Context mContext;

    @Before
    public void setUP() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testPropositionIDIsTuscany2016() {
        MockVerticalAppConfig mockConfig = new MockVerticalAppConfig(mContext);
        assertEquals("Tuscany2016", mockConfig.getProposition());
    }

    @Test
    public void testIOExceptionForWrongFileInput() {
        VerticalAppConfig mockConfig = new VerticalAppConfig(mContext) {
            @Override
            public InputStream readJsonInputStream(final Context context) throws IOException {
                throw new IOException();
            }
        };

        assertNull(mockConfig.getProposition());
    }

    @Test(expected = NullPointerException.class)
    public void testNullPointerExceptionWhenMockedReadJSONInputStream() throws IOException {
        VerticalAppConfig config = new VerticalAppConfig(mContext);
        config.readJsonInputStream(mContext);
    }
}
