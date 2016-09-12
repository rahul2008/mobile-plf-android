package com.philips.cdp.registration.coppa.base;

import android.content.Context;
import android.test.InstrumentationTestCase;

import com.janrain.android.Jump;
import com.philips.cdp.registration.coppa.interfaces.CoppaConsentUpdateCallback;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

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
    public void testUpdateCoppaConsentStatus(){

        assertNotNull(mContext);
        assertNull(mCoppaConsentUpdater.getCoppaEmailConsentStatus());

        try{
            mCoppaConsentUpdater.buildConfiguration();

        }
        catch(Exception e){

        }



    }
}