package com.philips.cdp.registration.coppa.ui.controllers;

import android.content.Context;
import android.test.InstrumentationTestCase;

import com.philips.cdp.registration.coppa.base.CoppaExtension;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by 310243576 on 8/20/2016.
 */
public class ConsentHandlerTest extends InstrumentationTestCase{

    Context mContext;
    ConsentHandler mConfirmationHandler;
    @Override
    protected void setUp() throws Exception {
        super.setUp();
        System.setProperty("dexmaker.dexcache", getInstrumentation()
                .getTargetContext().getCacheDir().getPath());
        mContext = getInstrumentation().getTargetContext();
        CoppaExtension coppaExtension = new CoppaExtension(mContext);
        mConfirmationHandler = new ConsentHandler(coppaExtension,mContext);
        assertNotNull(mConfirmationHandler);
    }

    @Test
    public void testConsentHandler(){
        CoppaExtension coppaExtension = new CoppaExtension(mContext);
        mConfirmationHandler = new ConsentHandler(coppaExtension,mContext);
        assertNotNull(mConfirmationHandler);
    }
}