package com.philips.cdp.di.iap.screens;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.philips.cdp.di.iap.BuildConfig;
import com.philips.cdp.di.iap.CustomRobolectricRunner;
import com.philips.cdp.di.iap.TestUtils;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.support.v4.SupportFragmentTestUtil;
import org.robolectric.util.FragmentTestUtil;

import static org.junit.Assert.*;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.robolectric.shadows.support.v4.SupportFragmentTestUtil.startFragment;

/**
 * Created by philips on 10/6/17.
 */
@RunWith(CustomRobolectricRunner.class)
@Config(constants = BuildConfig.class, sdk = 21)
public class DeliveryMethodFragmentTest {

    @Mock
    Context mContext;

    DeliveryMethodFragment deliveryMethodFragment;

    @Before
    public void setUp() {
        initMocks(this);

        mContext = RuntimeEnvironment.application;
        deliveryMethodFragment = DeliveryMethodFragment.createInstance(new Bundle(), InAppBaseFragment.AnimationType.NONE);
    }

    @Test(expected = NullPointerException.class)
    public void shouldStartFragment() throws Exception {
        SupportFragmentTestUtil.startFragment(deliveryMethodFragment);
    }

    @Test
    public void onAttach() throws Exception {
        deliveryMethodFragment.onAttach(mContext);
    }

    @Test
    public void onResume() throws Exception {
        onAttach();
        deliveryMethodFragment.onResume();
    }

    @Mock
    Bundle bundleMock;

    @Mock
    LayoutInflater layoutInflaterMock;

    @Mock
    ViewGroup viewGroupMock;

    @Test
    public void onCreateView() throws Exception {
    }

    @Test
    public void onActivityCreated() throws Exception {

    }

    @Test
    public void onItemClick() throws Exception {

    }

    @Test
    public void onGetRegions() throws Exception {

    }

    @Test
    public void onGetUser() throws Exception {

    }

    @Test
    public void onCreateAddress() throws Exception {

    }

    @Test
    public void onGetAddress() throws Exception {

    }

    @Test
    public void onSetDeliveryAddress() throws Exception {

    }

    @Test
    public void onGetDeliveryModes() throws Exception {

    }

    @Test
    public void onSetDeliveryMode() throws Exception {

    }

}