package com.philips.cdp.registration.coppa.ui.controllers;

import android.content.Context;

import com.philips.cdp.registration.coppa.RegistrationApiInstrumentationBase;
import com.philips.cdp.registration.coppa.base.CoppaExtension;

import org.mockito.Mock;

import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static junit.framework.Assert.assertNotNull;


public class ParentalConsentFragmentControllerTest extends RegistrationApiInstrumentationBase {

    Context mContext;
    ParentalConsentFragmentController mParentalConsentFragmentController;
    @Mock
    CoppaExtension mCoppaExtension;

    @Override
    public void setUp() throws Exception {
        super.setUp();
        mContext = getInstrumentation().getTargetContext();

    }


    public void testParentalApprovalFragmentController(){
       /* ParentalConsentFragment parentalApprovalFragment = new ParentalConsentFragment();
        mParentalConsentFragmentController = new ParentalConsentFragmentController(parentalApprovalFragment);
      */  assertNotNull(mContext);    }

}