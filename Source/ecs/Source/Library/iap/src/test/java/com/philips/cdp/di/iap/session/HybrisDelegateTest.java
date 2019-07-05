/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
package com.philips.cdp.di.iap.session;

import android.content.Context;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import static androidx.test.platform.app.InstrumentationRegistry.getInstrumentation;

@RunWith(RobolectricTestRunner.class)
public class HybrisDelegateTest {

    private Context mContext;

    @Before
    public void setUp() {
        mContext = getInstrumentation().getContext();
    }

    @Test
    public void testGetInstance() {
        HybrisDelegate.getInstance(mContext);
    }
}