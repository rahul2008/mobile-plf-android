package com.philips.cdp.di.iap.screens;

import android.content.Context;
import android.os.Bundle;

import com.philips.cdp.di.iap.BuildConfig;
import com.philips.cdp.di.iap.CustomRobolectricRunner;
import com.philips.cdp.di.iap.TestUtils;
import com.philips.cdp.di.iap.response.orders.OrderDetail;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.support.v4.SupportFragmentTestUtil;

import static org.mockito.MockitoAnnotations.initMocks;

@RunWith(CustomRobolectricRunner.class)
@Config(constants = BuildConfig.class, sdk = 21)
public class OrderDetailsFragmentTest {
    private Context mContext;
    OrderDetailsFragment orderDetailsFragmentTest;

    Bundle bundle;

    @Before
    public void setUp() {
        initMocks(this);

        mContext = RuntimeEnvironment.application;
        TestUtils.getStubbedStore();
        TestUtils.getStubbedHybrisDelegate();
        bundle=new Bundle();
        orderDetailsFragmentTest = OrderDetailsFragment.createInstance(new Bundle(), InAppBaseFragment.AnimationType.NONE);
        orderDetailsFragmentTest.setArguments(bundle);
    }

    @Test
    public void shouldDisplayAddressSelectionFragment() {

        SupportFragmentTestUtil.startFragment(orderDetailsFragmentTest);
    }

    @Mock
    OrderDetail orderDetailMock;

}