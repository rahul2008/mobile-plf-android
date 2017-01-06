package com.philips.cdp.registration.ui.social;

import android.support.multidex.MultiDex;
import android.test.InstrumentationTestCase;

import org.junit.Before;
import org.junit.Test;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public class MergeAccountFragmentTest extends InstrumentationTestCase {

    MergeAccountFragment mergeAccountFragment;

    @Before
    public void setUp() throws Exception {
        MultiDex.install(getInstrumentation().getTargetContext());
        mergeAccountFragment = new MergeAccountFragment();
    }
    @Test
    public void testAssert(){
        assertNotNull(mergeAccountFragment);
    }
}