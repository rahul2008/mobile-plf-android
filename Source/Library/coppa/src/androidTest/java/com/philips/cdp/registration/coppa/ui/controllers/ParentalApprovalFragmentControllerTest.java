package com.philips.cdp.registration.coppa.ui.controllers;

import android.content.Context;
import android.test.InstrumentationTestCase;

import com.philips.cdp.registration.coppa.base.CoppaExtension;
import com.philips.cdp.registration.coppa.ui.fragment.ParentalApprovalFragment;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by 310243576 on 8/20/2016.
 */
public class ParentalApprovalFragmentControllerTest extends InstrumentationTestCase {

    Context mContext;
    ParentalApprovalFragmentController mParentalApprovalFragmentController;
    @Override
    protected void setUp() throws Exception {
        super.setUp();
        System.setProperty("dexmaker.dexcache", getInstrumentation()
                .getTargetContext().getCacheDir().getPath());
        mContext = getInstrumentation().getTargetContext();

    }

    @Test
    public void testParentalApprovalFragmentController(){
        ParentalApprovalFragment parentalApprovalFragment = new ParentalApprovalFragment();
        mParentalApprovalFragmentController = new ParentalApprovalFragmentController(parentalApprovalFragment);
        assertNotNull(mParentalApprovalFragmentController);    }
}