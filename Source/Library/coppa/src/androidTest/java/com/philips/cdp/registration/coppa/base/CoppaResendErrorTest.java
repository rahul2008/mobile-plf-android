package com.philips.cdp.registration.coppa.base;

import com.philips.cdp.registration.coppa.RegistrationApiInstrumentationBase;

import org.junit.Test;

import static junit.framework.Assert.assertEquals;


public class CoppaResendErrorTest extends RegistrationApiInstrumentationBase {
    CoppaResendError mCoppaResendError ;

    @Override
    public void setUp() throws Exception {

        super.setUp();

    mCoppaResendError = new CoppaResendError();
    }

    @Test
    public void testGetErrorCode() throws Exception {
        mCoppaResendError.setErrorCode(1);
        assertEquals(1,mCoppaResendError.getErrorCode());

    }

    @Test
    public void testGetErrorDesc() throws Exception {
     mCoppaResendError.setErrorDesc("TestError");
        assertEquals("TestError",mCoppaResendError.getErrorDesc());

    }
}