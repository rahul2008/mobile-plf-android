/*
 * Copyright (c) 2015-2018 Koninklijke Philips N.V.
 * All rights reserved.
 */

package com.philips.cdp.registration.ui.traditional;

import com.philips.cdp.registration.configuration.RegistrationConfiguration;
import com.philips.cdp.registration.injection.RegistrationComponent;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.shadows.support.v4.SupportFragmentTestUtil;

import static org.mockito.MockitoAnnotations.initMocks;

/**
 * Created by philips on 11/21/17.
 */
@RunWith(RobolectricTestRunner.class)
public class ForgotPasswordFragmentTest {

    private ForgotPasswordFragment forgotPasswordFragment;

    @Mock
    private RegistrationComponent mockRegistrationComponent;

    @Before
    public void setUp() throws Exception {
        initMocks(this);

        RegistrationConfiguration.getInstance().setComponent(mockRegistrationComponent);
        forgotPasswordFragment= new ForgotPasswordFragment();
    }

    @Test
    public void testForgotPasswordErrorMessageNull()  {

        forgotPasswordFragment.forgotPasswordErrorMessage(null);
    }

    @Test
    public void testForgotPasswordErrorMessageEmpty()  {

        forgotPasswordFragment.forgotPasswordErrorMessage("");
    }

    @Test
    public void testForgotPasswordErrorMessage()  {

        forgotPasswordFragment.forgotPasswordErrorMessage("sampletext");
    }



}