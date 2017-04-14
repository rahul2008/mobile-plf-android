package com.philips.cdp.registration.coppa.ui.controllers;

import android.content.Context;
import android.support.multidex.MultiDex;
import android.test.InstrumentationTestCase;

import com.philips.cdp.registration.coppa.base.CoppaExtension;
import com.philips.cdp.registration.coppa.ui.fragment.ParentalConsentFragment;

/**
 * Created by 310243576 on 8/20/2016.
 */
public class ConfirmationHandlerTest extends InstrumentationTestCase {

    Context mContext;
    ConfirmationHandler mConfirmationHandler;
    ParentalConsentFragment mParentalConsentFragment;
    @Override
    protected void setUp() throws Exception {
        MultiDex.install(getInstrumentation().getTargetContext());
        super.setUp();
        System.setProperty("dexmaker.dexcache", getInstrumentation()
                .getTargetContext().getCacheDir().getPath());
        mContext = getInstrumentation().getTargetContext();
        CoppaExtension coppaExtension = new CoppaExtension(mContext);
        mConfirmationHandler = new ConfirmationHandler(coppaExtension,mContext);
        assertNotNull(mConfirmationHandler);
        mParentalConsentFragment= new ParentalConsentFragment();
        assertNotNull(mParentalConsentFragment);
    }

}