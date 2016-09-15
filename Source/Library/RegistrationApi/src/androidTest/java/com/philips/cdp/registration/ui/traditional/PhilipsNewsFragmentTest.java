package com.philips.cdp.registration.ui.traditional;

import android.test.InstrumentationTestCase;

import org.junit.Before;
import org.junit.Test;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public class PhilipsNewsFragmentTest extends InstrumentationTestCase{

    PhilipsNewsFragment philipsNewsFragment;
    @Before
    public void setUp() throws Exception {
        philipsNewsFragment=new PhilipsNewsFragment();
    }
    @Test
    public void testAsser(){
        assertNotNull(philipsNewsFragment);
    }
}