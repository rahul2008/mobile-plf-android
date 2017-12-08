package com.philips.cdp.registration.ui.social;

import com.philips.cdp.registration.RegistrationApiInstrumentationBase;

import org.junit.Before;
import org.junit.Test;

import static junit.framework.Assert.assertNotNull;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public class MergeAccountFragmentTest extends RegistrationApiInstrumentationBase {

    MergeAccountFragment mergeAccountFragment;

    @Before
    public void setUp() throws Exception {
       super.setUp();
        mergeAccountFragment = new MergeAccountFragment();
    }
    @Test
    public void testAssert(){
        assertNotNull(mergeAccountFragment);
    }
}