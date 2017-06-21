package com.philips.cdp.registration.ui.utils.test;

import com.philips.cdp.registration.RegistrationApiInstrumentationBase;
import com.philips.cdp.registration.ui.utils.FieldsValidator;

import org.junit.Before;
import org.junit.Test;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;


public class MobileNumberVerifyTest extends RegistrationApiInstrumentationBase {

    @Before
    public void setUp() throws Exception {

        super.setUp();
    }
    @Test
    public void testMobileNumberNotNull(){
        String verifiedMobileStr = FieldsValidator.getVerifiedMobileNumber("j6j2mrs","f47ac10b-58cc-4372-a567-0e02b2c3d479");
        assertNotNull(verifiedMobileStr);
    }

    @Test
    public void testMobileNumberisValid(){
        String verifiedMobileStr = FieldsValidator.getVerifiedMobileNumber("f47ac10b-58cc-4372-a567-0e02b2c3d479","j6j2mrs");
        assertEquals("j6j2mrsf47acb58cc4372a567e2b2c3d",verifiedMobileStr);
    }
}
