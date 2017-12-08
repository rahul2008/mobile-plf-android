package com.philips.cdp.registration.ui.social;

import com.philips.cdp.registration.RegistrationApiInstrumentationBase;

import org.junit.Before;
import org.junit.Test;

import static junit.framework.Assert.assertNotNull;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public class MergeSocialToSocialAccountFragmentTest extends RegistrationApiInstrumentationBase {

    MergeSocialToSocialAccountFragment mergeSocialToSocialAccountFragment;
    @Before
    public void setUp() throws Exception {
       super.setUp();
        mergeSocialToSocialAccountFragment= new MergeSocialToSocialAccountFragment();
    }
    @Test
    public void testAssert(){
        assertNotNull(mergeSocialToSocialAccountFragment);
    }
}