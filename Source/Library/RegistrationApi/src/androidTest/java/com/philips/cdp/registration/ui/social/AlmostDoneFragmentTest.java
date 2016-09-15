package com.philips.cdp.registration.ui.social;

import android.test.InstrumentationTestCase;

import org.junit.Before;
import org.junit.Test;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public class AlmostDoneFragmentTest extends InstrumentationTestCase{

    AlmostDoneFragment almostDoneFragment;
    @Before
    public void setUp() throws Exception {
        almostDoneFragment= new AlmostDoneFragment();
    }
    @Test
    public void testALmostDoneFragment(){
        assertNotNull(almostDoneFragment);

    }
}