package com.philips.cdp.di.iap.screens;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;

import com.philips.cdp.di.iap.BuildConfig;
import com.philips.cdp.di.iap.CustomRobolectricRunner;
import com.philips.cdp.di.iap.TestUtils;
import com.philips.cdp.di.iap.integration.IAPListener;
import com.philips.cdp.di.iap.products.ProductCatalogPresenter;
import com.philips.cdp.di.iap.utils.IAPConstant;
import com.philips.cdp.di.iap.utils.Utility;
import com.philips.platform.uappframework.listener.ActionBarListener;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import java.util.ArrayList;

import static org.mockito.MockitoAnnotations.initMocks;
import static org.robolectric.shadows.support.v4.SupportFragmentTestUtil.startFragment;

@RunWith(CustomRobolectricRunner.class)
@Config(constants = BuildConfig.class, sdk = 25)
public class ProductCatalogFragmentTest {
    // private Activity activity;
    private ProductCatalogFragment productCatalogFragment;
    @Mock
    private ProductCatalogPresenter productCatalogPresenter;
    @Mock
    IAPListener mockIAPListener;
    private Context mContext;

    @Before
    public void setUp() {
        initMocks(this);

        mContext = RuntimeEnvironment.application;
        TestUtils.getStubbedStore();
        TestUtils.getStubbedHybrisDelegate();
    }

    @Test(expected = NullPointerException.class)
    public void shouldDisplayCategorizedProductlist() {
        Bundle bundle = new Bundle();
        ArrayList<String> list = new ArrayList<>();
        list.add("HX8332/11");
        bundle.putStringArrayList(IAPConstant.CATEGORISED_PRODUCT_CTNS, list);
        productCatalogFragment = ProductCatalogFragment.createInstance(bundle, InAppBaseFragment.AnimationType.NONE);
        productCatalogFragment.setActionBarListener(Mockito.mock(ActionBarListener.class), mockIAPListener);
        startFragment(productCatalogFragment);
        Assert.assertNotNull(productCatalogFragment);
    }

    @Test(expected = NullPointerException.class)
    public void shouldNotDisplayCategorizedProductlist() {
        Utility.addCountryInPreference(PreferenceManager.getDefaultSharedPreferences(mContext), IAPConstant.IAP_COUNTRY_KEY, "US");
        productCatalogFragment = ProductCatalogFragment.createInstance(new Bundle(), InAppBaseFragment.AnimationType.NONE);
        productCatalogFragment.setActionBarListener(Mockito.mock(ActionBarListener.class), mockIAPListener);
        startFragment(productCatalogFragment);
    }
}