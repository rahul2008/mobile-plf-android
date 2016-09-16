package com.philips.cdp.registration.ui.social;

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
        mergeSocialToSocialAccountFragment= new MergeSocialToSocialAccountFragment();
    }
    @Test
    public void testAssert(){
        assertNotNull(mergeSocialToSocialAccountFragment);
    }
}