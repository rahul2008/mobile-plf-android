package com.philips.cdp.registration.ui.customviews;

import com.philips.cdp.registration.RegistrationApiInstrumentationBase;

import org.junit.Before;
import org.junit.Test;

import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static junit.framework.Assert.assertNotNull;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public class XRegErrorTest extends RegistrationApiInstrumentationBase {
    XRegError xRegError;
    String mSigninErrMsg;
    @Before
    public void setUp() throws Exception {
        super.setUp();
        xRegError = new XRegError(getInstrumentation().getContext());
    }
    @Test
    public void testAssert(){
        assertNotNull(xRegError);
    }
    @Test
    public void testSetError(){
        String errorMsg="error";
        xRegError.setError(errorMsg);
        assertNotNull(xRegError);
        mSigninErrMsg=errorMsg;
        xRegError.getContext();
    }
}