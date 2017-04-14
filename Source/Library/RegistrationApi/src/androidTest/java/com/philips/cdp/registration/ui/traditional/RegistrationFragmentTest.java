package com.philips.cdp.registration.ui.traditional;

import android.support.multidex.MultiDex;
import android.test.InstrumentationTestCase;

import org.junit.Before;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public class RegistrationFragmentTest extends InstrumentationTestCase{
    RegistrationFragment registrationFragment;
    @Before
    public void setUp() throws Exception {
        MultiDex.install(getInstrumentation().getTargetContext());
        registrationFragment= new RegistrationFragment();
    }

}