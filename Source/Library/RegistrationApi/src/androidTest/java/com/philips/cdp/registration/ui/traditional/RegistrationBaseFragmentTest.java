package com.philips.cdp.registration.ui.traditional;

import android.content.res.Configuration;
import android.support.multidex.MultiDex;
import android.view.View;

import com.philips.cdp.registration.RegistrationApiInstrumentationBase;

import org.junit.Before;
import org.junit.Test;

import static junit.framework.Assert.assertNotNull;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public class RegistrationBaseFragmentTest extends RegistrationApiInstrumentationBase {

    RegistrationBaseFragment registrationBaseFragment;
    @Before
    public void setUp() throws Exception {
       super.setUp();
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