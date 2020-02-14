/*
 * Copyright (c) 2015-2018 Koninklijke Philips N.V.
 * All rights reserved.
 */

package com.philips.cdp.registration.ui.traditional;

import com.philips.cdp.registration.configuration.RegistrationConfiguration;
import com.philips.cdp.registration.dao.Country;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.shadows.support.v4.SupportFragmentTestUtil;

import static org.mockito.MockitoAnnotations.initMocks;

/**
 * Created by philips on 2/22/18.
 */
@RunWith(RobolectricTestRunner.class)
public class CountrySelectionFragmentTest {

    @Mock
    private com.philips.cdp.registration.injection.RegistrationComponent componentMock;

    @Mock
    private Country countryMock;

    @Before
    public void setUp() throws Exception {
        initMocks(this);

        RegistrationConfiguration.getInstance().setComponent(componentMock);
    }
    @Test(expected = NullPointerException.class)
    public void shouldNotifyCountryChange() throws Exception {
        CountrySelectionFragment countrySelectionFragment = new CountrySelectionFragment();
//        SupportFragmentTestUtil.startFragment(countrySelectionFragment);
        countrySelectionFragment.notifyCountryChange(countryMock);
    }
}