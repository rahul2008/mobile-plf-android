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
public class XPasswordHintRowTest extends RegistrationApiInstrumentationBase {
    XPasswordHintRow xPasswordHintRow;
    @Before
    public void setUp() throws Exception {
       super.setUp();
        xPasswordHintRow= new XPasswordHintRow(getInstrumentation().getContext());
    }
    @Test
    public void testAssert(){
        assertNotNull(xPasswordHintRow);
    }
}