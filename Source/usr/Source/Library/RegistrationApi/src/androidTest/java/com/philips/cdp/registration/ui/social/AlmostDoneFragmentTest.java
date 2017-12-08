package com.philips.cdp.registration.ui.social;

import android.view.InflateException;

import com.philips.cdp.registration.RegistrationApiInstrumentationBase;

import org.junit.Before;
import org.junit.Test;

import static junit.framework.Assert.assertNotNull;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public class AlmostDoneFragmentTest extends RegistrationApiInstrumentationBase {

    AlmostDoneFragment almostDoneFragment;
    @Before
    public void setUp() throws Exception {
        super.setUp();
        almostDoneFragment= new AlmostDoneFragment();
    }
    @Test
    public void testALmostDoneFragment(){
        assertNotNull(almostDoneFragment);

    }
}