package com.philips.cdp.registration.coppa.ui.controllers;

import android.content.Context;

import com.philips.cdp.registration.coppa.RegistrationApiInstrumentationBase;
import com.philips.cdp.registration.coppa.base.CoppaExtension;
import com.philips.cdp.registration.coppa.ui.fragment.ParentalConsentFragment;

import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static junit.framework.Assert.assertNotNull;


public class ConfirmationHandlerTest extends RegistrationApiInstrumentationBase {

    Context mContext;
    ConfirmationHandler mConfirmationHandler;
    ParentalConsentFragment mParentalConsentFragment;
    @Override
    public void setUp() throws Exception {

        super.setUp();

        mContext = getInstrumentation().getTargetContext();
        CoppaExtension coppaExtension = new CoppaExtension(mContext);
        mConfirmationHandler = new ConfirmationHandler(coppaExtension,mContext);
        assertNotNull(mConfirmationHandler);
        mParentalConsentFragment= new ParentalConsentFragment();
        assertNotNull(mParentalConsentFragment);
    }

}