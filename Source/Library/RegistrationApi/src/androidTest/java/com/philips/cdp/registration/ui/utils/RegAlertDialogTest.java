package com.philips.cdp.registration.ui.utils;

import android.test.InstrumentationTestCase;

import org.junit.Before;
import org.junit.Test;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public class RegAlertDialogTest extends InstrumentationTestCase{
RegAlertDialog regAlertDialog;
    @Before
    public void setUp() throws Exception {
        regAlertDialog= new RegAlertDialog();
    }
    @Test
    public void testassert(){
        assertNotNull(regAlertDialog);
    }
}