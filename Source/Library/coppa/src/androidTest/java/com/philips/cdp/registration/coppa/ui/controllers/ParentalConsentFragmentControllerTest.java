package com.philips.cdp.registration.coppa.ui.controllers;

import android.content.Context;
import android.test.InstrumentationTestCase;

import com.philips.cdp.registration.coppa.base.CoppaExtension;
import com.philips.cdp.registration.coppa.ui.fragment.ParentalConsentFragment;

import org.junit.Test;
import org.mockito.Mock;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public class ParentalConsentFragmentControllerTest extends InstrumentationTestCase {

    Context mContext;
    ParentalConsentFragmentController mParentalConsentFragmentController;
    @Mock
    CoppaExtension mCoppaExtension;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        System.setProperty("dexmaker.dexcache", getInstrumentation()
                .getTargetContext().getCacheDir().getPath());
        mContext = getInstrumentation().getTargetContext();

    }

    @Test
    public void testParentalApprovalFragmentController(){
        ParentalConsentFragment parentalApprovalFragment = new ParentalConsentFragment();
        mParentalConsentFragmentController = new ParentalConsentFragmentController(parentalApprovalFragment);
        assertNotNull(mParentalConsentFragmentController);    }

}