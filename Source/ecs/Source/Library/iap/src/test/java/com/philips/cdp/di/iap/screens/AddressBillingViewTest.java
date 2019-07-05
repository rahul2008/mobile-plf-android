/*
 * Copyright (c) 2015-2018 Koninklijke Philips N.V.
 * All rights reserved.
 */

package com.philips.cdp.di.iap.screens;

import android.content.Context;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;

import java.util.HashMap;

import static androidx.test.platform.app.InstrumentationRegistry.getInstrumentation;
import static org.mockito.MockitoAnnotations.initMocks;

@RunWith(RobolectricTestRunner.class)
public class AddressBillingViewTest {
    private Context mContext;
    private AddressBillingView addressBillingView;

    AddressPresenter addressPresenter;
    @Mock
    private AddressContractor addressContractorMock;

    @Before
    public void setUp() {
        initMocks(this);
        mContext = getInstrumentation().getContext();
    }

    @Mock
    HashMap<String, String> addressFiledMapMock;
    @Test(expected = NullPointerException.class)
    public void shouldUpdateFileds() throws Exception {
        addressPresenter = new AddressPresenter(addressContractorMock);
        addressBillingView = new AddressBillingView(addressPresenter);
        addressBillingView.updateFields(addressFiledMapMock);
    }
}