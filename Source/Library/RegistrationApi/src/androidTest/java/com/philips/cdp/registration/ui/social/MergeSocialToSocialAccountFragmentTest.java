package com.philips.cdp.registration.ui.social;

import android.support.multidex.MultiDex;
import android.test.InstrumentationTestCase;

import org.junit.Before;
import org.junit.Test;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public class MergeSocialToSocialAccountFragmentTest extends InstrumentationTestCase{

    MergeSocialToSocialAccountFragment mergeSocialToSocialAccountFragment;
    @Before
    public void setUp() throws Exception {
        MultiDex.install(getInstrumentation().getTargetContext());
        mergeSocialToSocialAccountFragment= new MergeSocialToSocialAccountFragment();
    }
    @Test
    public void testAssert(){
        assertNotNull(mergeSocialToSocialAccountFragment);
    }
}