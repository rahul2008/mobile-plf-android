package com.philips.cdp.registration.ui.utils.test;

import android.app.Instrumentation;
import android.test.InstrumentationTestCase;

import com.philips.cdp.registration.ui.utils.FieldsValidator;

import org.junit.Before;

/**
 * Created by 310190722 on 10/17/2016.
 */
public class MobileNumberVerifyTest extends InstrumentationTestCase {

    @Before
    protected void setUp() throws Exception {
        super.setUp();
    }

    public void testMobileNumberNotNull(){
        String verifiedMobileStr = FieldsValidator.getVerifiedMobileNumber("j6j2mrs","f47ac10b-58cc-4372-a567-0e02b2c3d479");
        assertNotNull(verifiedMobileStr);
    }

    public void testMobileNumberisValid(){
        String verifiedMobileStr = FieldsValidator.getVerifiedMobileNumber("f47ac10b-58cc-4372-a567-0e02b2c3d479","j6j2mrs");
        assertEquals("j6j2mrsf47acb58cc4372a567e2b2c3d",verifiedMobileStr);
    }
}
