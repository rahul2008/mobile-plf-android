/*
 * Copyright (c) 2015-2018 Koninklijke Philips N.V.
 * All rights reserved.
 */

package com.philips.cdp.di.iap.screens;

import android.content.Context;
import android.os.Message;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.shadows.support.v4.SupportFragmentTestUtil;

import static androidx.test.platform.app.InstrumentationRegistry.getInstrumentation;
import static org.mockito.MockitoAnnotations.initMocks;

/**
 * Created by philips on 10/6/17.
 */
@RunWith(RobolectricTestRunner.class)
public class DeliveryMethodFragmentTest {

    private Context mContext;

    @Mock
    private Message messageMock;

    private DeliveryMethodFragmentMock deliveryMethodFragment;

    @Before
    public void setUp() {
        initMocks(this);

        mContext = getInstrumentation().getContext();
        deliveryMethodFragment = new DeliveryMethodFragmentMock();
    }

    @Test
    public void shouldStartFragment() throws Exception {
        SupportFragmentTestUtil.startFragment(deliveryMethodFragment);
    }

    @Test
    public void onAttach() throws Exception {
        deliveryMethodFragment.onAttach(mContext);
    }

    @Test
    public void onResume() throws Exception {
        SupportFragmentTestUtil.startFragment(deliveryMethodFragment);
        onAttach();
        deliveryMethodFragment.onResume();
    }

    @Test(expected = NullPointerException.class)
    public void onItemClick() throws Exception {
        deliveryMethodFragment.onItemClick(0);
    }
    @Test
    public void onGetRegions() throws Exception {
        deliveryMethodFragment.onGetRegions(messageMock);
    }

    @Test
    public void onGetUser() throws Exception {
        deliveryMethodFragment.onGetUser(messageMock);
    }

    @Test
    public void onCreateAddress() throws Exception {
        deliveryMethodFragment.onCreateAddress(messageMock);
    }

    @Test
    public void onGetAddress() throws Exception {
        deliveryMethodFragment.onGetAddress(messageMock);
    }

    @Test
    public void onSetDeliveryAddress() throws Exception {
        deliveryMethodFragment.onSetDeliveryAddress(messageMock);
    }

    @Test
    public void onGetDeliveryModes() throws Exception {
        deliveryMethodFragment.onGetDeliveryModes(messageMock);
    }

    @Test(expected = NullPointerException.class)
    public void onSetDeliveryMode() throws Exception {
        deliveryMethodFragment.onSetDeliveryMode(messageMock);
    }
}