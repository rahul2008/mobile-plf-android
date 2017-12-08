package com.philips.cdp.registration.coppa.ui.customviews;

import android.app.AlertDialog;

import com.philips.cdp.registration.coppa.RegistrationApiInstrumentationBase;

import org.junit.Before;
import org.junit.Test;

import static junit.framework.Assert.assertNotNull;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public class RegCoppaAlertDialogTest extends RegistrationApiInstrumentationBase {
    RegCoppaAlertDialog regCoppaAlertDialog;
    AlertDialog dialogBuilder;
    @Before
    public void setUp() throws Exception {
     super.setUp();

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