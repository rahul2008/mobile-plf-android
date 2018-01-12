package com.philips.cdp.di.iap.screens;

import android.content.Context;
import android.os.Bundle;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.philips.cdp.di.iap.BuildConfig;
import com.philips.cdp.di.iap.CustomRobolectricRunner;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.support.v4.SupportFragmentTestUtil;

import static org.mockito.MockitoAnnotations.initMocks;

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

    @Test(expected = NullPointerException.class)
    public void onItemClick() throws Exception {
        deliveryMethodFragment.onItemClick(0);
    }

    @Mock
    Message messageMock;
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