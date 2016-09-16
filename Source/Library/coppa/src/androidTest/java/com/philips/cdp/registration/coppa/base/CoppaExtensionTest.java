package com.philips.cdp.registration.coppa.base;

import android.content.Context;
import android.test.InstrumentationTestCase;

import org.junit.Test;

/**
 * Created by 310243576 on 8/24/2016.
 */
public class CoppaExtensionTest extends InstrumentationTestCase {

    CoppaExtension mCoppaConsentUpdater;
    Context mContext;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        System.setProperty("dexmaker.dexcache", getInstrumentation()
                .getTargetContext().getCacheDir().getPath());

        mCoppaConsentUpdater = new CoppaExtension(mContext);
        assertNotNull(mCoppaConsentUpdater);
        mContext = getInstrumentation().getTargetContext();
    }

    @Test
    public void testUpdateCoppaConsentStatus() {

        assertNotNull(mContext);
        assertNull(mCoppaConsentUpdater.getCoppaEmailConsentStatus());

        try {
            mCoppaConsentUpdater.buildConfiguration();
        } catch (Exception e) {

        }
    }
    @Test
    public void testResetConfiguration(){
        mCoppaConsentUpdater.resetConfiguration();
        CoppaConfiguration.clearConfiguration();
        assertNotNull(mCoppaConsentUpdater);
    }
}