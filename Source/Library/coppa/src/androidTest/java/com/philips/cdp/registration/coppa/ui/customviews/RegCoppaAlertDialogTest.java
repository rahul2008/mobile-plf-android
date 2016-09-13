package com.philips.cdp.registration.coppa.ui.customviews;

import android.app.AlertDialog;
import android.test.InstrumentationTestCase;

import org.junit.Before;
import org.junit.Test;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public class RegCoppaAlertDialogTest extends InstrumentationTestCase{
    RegCoppaAlertDialog regCoppaAlertDialog;
    AlertDialog dialogBuilder;
    @Before
    public void setUp() throws Exception {

        regCoppaAlertDialog =  new RegCoppaAlertDialog();

    }
    @Test
    public void testRegCoppa(){
        assertNotNull(regCoppaAlertDialog);
    }
    @Test
    public void testDismissDialog(){
        regCoppaAlertDialog.dismissDialog();

    }
}