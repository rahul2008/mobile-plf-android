package com.philips.cdp.registration.coppa.utils;

import android.support.multidex.MultiDex;
import android.test.InstrumentationTestCase;

import org.junit.Before;
/**
 * Created by 310230979  on 8/31/2016.
 */
public class CoppaConstantsTest extends InstrumentationTestCase{

    CoppaConstants coppaConstants;
    @Override
    protected void setUp() throws Exception {
        MultiDex.install(getInstrumentation().getTargetContext());
        super.setUp();

        System.setProperty("dexmaker.dexcache", getInstrumentation().getTargetContext().getCacheDir().getPath());
        coppaConstants= new CoppaConstants();
    }
    public void testLanuchFragment(){
        assertNotNull(coppaConstants);
        assertEquals("launchParentalFragment",coppaConstants.LAUNCH_PARENTAL_FRAGMENT);
    }
}