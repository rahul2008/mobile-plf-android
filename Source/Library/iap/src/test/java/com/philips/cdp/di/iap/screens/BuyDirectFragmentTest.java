package com.philips.cdp.di.iap.screens;

import android.content.Context;
import android.os.Bundle;
import android.os.Message;

import com.philips.cdp.di.iap.BuildConfig;
import com.philips.cdp.di.iap.CustomRobolectricRunner;
import com.philips.cdp.di.iap.TestUtils;

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
public class BuyDirectFragmentTest {
    private Context mContext;
    BuyDirectFragment buyDirectFragment;

    @Mock
    Message message;

    @Before
    public void setUp() {
        initMocks(this);

        mContext = RuntimeEnvironment.application;
        TestUtils.getStubbedStore();
        TestUtils.getStubbedHybrisDelegate();
        buyDirectFragment = BuyDirectFragment.createInstance(new Bundle(), InAppBaseFragment.AnimationType.NONE);
    }

    @Test(expected = NullPointerException.class)
    public void shouldDisplayBuyDirectFragment() {

        SupportFragmentTestUtil.startFragment(buyDirectFragment);
    }

    public void shouldDisplayOnCartCreateWithNull() {
        buyDirectFragment.onCreateCart(null);
        SupportFragmentTestUtil.startFragment(buyDirectFragment);
    }
    public void shouldDisplayOnAddToCartWithNull() {
        buyDirectFragment.onAddToCart(null);
        SupportFragmentTestUtil.startFragment(buyDirectFragment);
    }
    public void shouldDisplayOnGetRegionsWithNull() {
        buyDirectFragment.onGetRegions(null);
        SupportFragmentTestUtil.startFragment(buyDirectFragment);
    }
}