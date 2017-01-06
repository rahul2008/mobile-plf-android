package com.philips.cdp.registration.ui.utils;

import android.support.multidex.MultiDex;
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
        MultiDex.install(getInstrumentation().getTargetContext());
        regAlertDialog= new RegAlertDialog();
    }
    @Test
    public void testassert(){
        assertNotNull(regAlertDialog);
    }
}