package com.philips.cdp.registration.ui.utils;

import com.philips.cdp.registration.RegistrationApiInstrumentationBase;

import org.junit.Before;
import org.junit.Test;

import static junit.framework.Assert.assertNotNull;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public class RegAlertDialogTest extends RegistrationApiInstrumentationBase {
RegAlertDialog regAlertDialog;
    @Before
    public void setUp() throws Exception {
        super.setUp();
        regAlertDialog= new RegAlertDialog();
    }
    @Test
    public void testassert(){
        assertNotNull(regAlertDialog);
    }
}