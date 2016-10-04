package com.philips.cdp.registration.ui.traditional;

import android.content.res.Configuration;
import android.test.InstrumentationTestCase;
import android.view.View;

import org.junit.Before;
import org.junit.Test;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public class RegistrationBaseFragmentTest extends InstrumentationTestCase{

    RegistrationBaseFragment registrationBaseFragment;
    @Before
    public void setUp() throws Exception {
        registrationBaseFragment= new RegistrationBaseFragment() {
            @Override
            protected void setViewParams(final Configuration config, final int width) {

            }

            @Override
            protected void handleOrientation(final View view) {

            }

            @Override
            public int getTitleResourceId() {
                return 0;
            }
        };
    }
    @Test
    public void testAssert(){
        assertNotNull(registrationBaseFragment);
    }
}