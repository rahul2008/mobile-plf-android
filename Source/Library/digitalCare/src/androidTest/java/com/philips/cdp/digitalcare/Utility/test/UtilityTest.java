package com.philips.cdp.digitalcare.Utility.test;

import android.content.Context;
import android.test.InstrumentationTestCase;

import com.philips.cdp.digitalcare.util.Utils;

public final class UtilityTest extends InstrumentationTestCase {

    private Context mContext;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        mContext = getInstrumentation().getContext();
    }

    /**
     * If the connectedDevice is Phone - Test case should pass.
     */
    public void testPhoneCompatibility() {
        assertTrue(!Utils.isTablet(mContext));
    }


    /**
     * If Sim is not available in the connected Device - It should pass.
     */
    public void testSimAvailabity() {
        assertTrue(!Utils.isSimAvailable(mContext));
    }
}
