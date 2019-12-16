/*
 * Copyright (c) 2015-2018 Koninklijke Philips N.V.
 * All rights reserved.
 */

package com.philips.cdp.registration.ui.social;

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
 * Created by philips on 11/17/17.
 */
@RunWith(RobolectricTestRunner.class)
public class AlmostDoneFragmentTest {

    private AlmostDoneFragment almostDoneFragment;

    @Mock
    private RegistrationComponent mockRegistrationComponent;

    @Before
    public void setUp() throws Exception {
        initMocks(this);

        RegistrationConfiguration.getInstance().setComponent(mockRegistrationComponent);
        almostDoneFragment = new AlmostDoneFragment();
    }

    @Test(expected = NullPointerException.class)
    public void should_startFragment() throws Exception {
//        SupportFragmentTestUtil.startFragment(almostDoneFragment);
    }
}