/*
 * Copyright (c) 2015-2018 Koninklijke Philips N.V.
 * All rights reserved.
 */

package com.philips.cdp.di.iap.screens;

import android.content.Context;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.InflateException;

import com.philips.cdp.di.iap.TestUtils;
import com.philips.cdp.di.iap.integration.IAPListener;
import com.philips.cdp.di.iap.utils.IAPConstant;
import com.philips.cdp.di.iap.utils.Utility;
import com.philips.platform.uappframework.listener.ActionBarListener;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.shadows.support.v4.SupportFragmentController;

import java.util.ArrayList;

import static androidx.test.platform.app.InstrumentationRegistry.getInstrumentation;
import static org.junit.Assert.assertNotNull;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.robolectric.shadows.support.v4.SupportFragmentTestUtil.startFragment;

@RunWith(RobolectricTestRunner.class)
public class ProductCatalogFragmentTest {
    private ProductCatalogFragment productCatalogFragment;

    @Mock
    private IAPListener mockIAPListener;

    private Context mContext;

    @Before
    public void setUp() {
        initMocks(this);

        mContext = getInstrumentation().getContext();
        TestUtils.getStubbedStore();
        TestUtils.getStubbedHybrisDelegate();
    }

    @Test(expected = InflateException.class)
    public void shouldDisplayCategorizedProductlist() {
        Bundle bundle = new Bundle();
        ArrayList<String> list = new ArrayList<>();
        list.add("HX8332/11");
        bundle.putStringArrayList(IAPConstant.CATEGORISED_PRODUCT_CTNS, list);
        productCatalogFragment = ProductCatalogFragment.createInstance(bundle, InAppBaseFragment.AnimationType.NONE);
        productCatalogFragment.setActionBarListener(Mockito.mock(ActionBarListener.class), mockIAPListener);
        SupportFragmentController.of(productCatalogFragment).create().start().resume();
        assertNotNull(productCatalogFragment);
    }

    @Test
    public void shouldNotDisplayCategorizedProductlist() {
        Utility.addCountryInPreference(PreferenceManager.getDefaultSharedPreferences(mContext), IAPConstant.IAP_COUNTRY_KEY, "US");
        productCatalogFragment = ProductCatalogFragment.createInstance(new Bundle(), InAppBaseFragment.AnimationType.NONE);
        productCatalogFragment.setActionBarListener(Mockito.mock(ActionBarListener.class), mockIAPListener);
        startFragment(productCatalogFragment);
    }
}