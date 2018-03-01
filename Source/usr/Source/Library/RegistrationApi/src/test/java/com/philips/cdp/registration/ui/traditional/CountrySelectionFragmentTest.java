package com.philips.cdp.registration.ui.traditional;

import android.app.Application;
import android.content.Context;

import com.philips.cdp.registration.BuildConfig;
import com.philips.cdp.registration.CustomRobolectricRunner;
import com.philips.cdp.registration.configuration.RegistrationConfiguration;
import com.philips.cdp.registration.dao.Country;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.support.v4.SupportFragmentTestUtil;

import static org.junit.Assert.*;
import static org.mockito.MockitoAnnotations.initMocks;

/**
 * Created by philips on 2/22/18.
 */
@RunWith(CustomRobolectricRunner.class)
@Config(constants = BuildConfig.class, sdk = 21)
public class CountrySelectionFragmentTest {

    private Context mContext;

    CountrySelectionFragment countrySelectionFragment;


    @Mock
    private com.philips.cdp.registration.injection.RegistrationComponent componentMock;

    @Before
    public void setUp() throws Exception {

        initMocks(this);
        mContext = RuntimeEnvironment.application;
        RegistrationConfiguration.getInstance().setComponent(componentMock);
    }

    @Mock
    Country countryMock;
    @Test(expected = NullPointerException.class)
    public void shouldNotifyCountryChange() throws Exception {
        countrySelectionFragment = new CountrySelectionFragment();
        SupportFragmentTestUtil.startFragment(countrySelectionFragment);
        countrySelectionFragment.notifyCountryChange(countryMock);
    }
}