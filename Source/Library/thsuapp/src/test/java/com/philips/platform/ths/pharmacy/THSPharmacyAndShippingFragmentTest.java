package com.philips.platform.ths.pharmacy;

import com.americanwell.sdk.entity.Address;
import com.americanwell.sdk.entity.pharmacy.Pharmacy;
import com.philips.platform.ths.CustomRobolectricRunnerAmwel;
import com.philips.platform.ths.registration.THSConsumer;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.robolectric.shadows.support.v4.SupportFragmentTestUtil;

@RunWith(CustomRobolectricRunnerAmwel.class)
public class THSPharmacyAndShippingFragmentTest {

    THSPharmacyAndShippingFragment thsPharmacyAndShippingFragment;

    @Mock
    Address address;

    @Mock
    Pharmacy pharmacy;

    @Mock
    THSConsumer consumer;

    @Before
    public void setUp() throws  Exception{
        thsPharmacyAndShippingFragment = new THSPharmacyAndShippingFragment();
        thsPharmacyAndShippingFragment.setPharmacyAndAddress(address,pharmacy);
        thsPharmacyAndShippingFragment.setConsumer(consumer);
    }

    @Test
    public void launchPharmacyShippingFragment(){
        SupportFragmentTestUtil.startFragment(thsPharmacyAndShippingFragment);
    }
}
