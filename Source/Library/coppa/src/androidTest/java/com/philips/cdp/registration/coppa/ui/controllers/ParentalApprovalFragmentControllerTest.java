package com.philips.cdp.registration.coppa.ui.controllers;

import android.app.ProgressDialog;
import android.content.Context;
import android.test.InstrumentationTestCase;

import com.philips.cdp.registration.coppa.R;
import com.philips.cdp.registration.coppa.base.CoppaExtension;
import com.philips.cdp.registration.coppa.ui.fragment.ParentalApprovalFragment;

import org.junit.Test;

/**
 * Created by 310243576 on 8/20/2016.
 */
public class ParentalApprovalFragmentControllerTest extends InstrumentationTestCase {

    Context mContext;
    ParentalApprovalFragmentController mParentalApprovalFragmentController;
    CoppaExtension coppaExtension;
    ProgressDialog mProgressDialog;
    @Override
    protected void setUp() throws Exception {
        super.setUp();
        System.setProperty("dexmaker.dexcache", getInstrumentation()
                .getTargetContext().getCacheDir().getPath());
        mContext = getInstrumentation().getTargetContext();
        ParentalApprovalFragment parentalApprovalFragment = new ParentalApprovalFragment();
        mParentalApprovalFragmentController = new ParentalApprovalFragmentController(parentalApprovalFragment);
        coppaExtension= new CoppaExtension(mContext);
        mProgressDialog= new ProgressDialog(mContext, R.style.reg_Custom_loaderTheme);



    }

    @Test
    public void testParentalApprovalFragmentController(){
        assertNotNull(mParentalApprovalFragmentController);
        assertNotNull(coppaExtension);

    }
    @Test
    public void tstIsCountryUs(){
        boolean result = mParentalApprovalFragmentController.isCountryUs();
        assertFalse(result);
        assertNotNull(mParentalApprovalFragmentController.getCoppaExtension().getConsent().getLocale());
    }
}