/*
 * Copyright (c) 2015-2018 Koninklijke Philips N.V.
 * All rights reserved.
 */

package com.philips.cdp.di.iap.screens;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.philips.cdp.di.iap.R;
import com.philips.cdp.di.iap.TestUtils;
import com.philips.cdp.di.iap.utils.NetworkUtility;
import com.philips.platform.uid.view.widget.DotNavigationIndicator;
import com.philips.platform.uid.view.widget.ProgressBarButton;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.shadows.support.v4.SupportFragmentTestUtil;

import static androidx.test.platform.app.InstrumentationRegistry.getInstrumentation;
import static org.mockito.Mockito.mock;
import static org.mockito.MockitoAnnotations.initMocks;

@RunWith(RobolectricTestRunner.class)
public class ProductDetailFragmentTest {

    @Mock
    private View viewMock;

    @Mock
    private ProgressBarButton progressBarAddToCartButtonMock;

    @Mock
    private ProgressBarButton progressBarBuyFromRetailerButtonMock;

    @Mock
    private LinearLayout linearLayout1Mock;

    @Mock
    private LinearLayout linearLayout2Mock;

    @Mock
    private Button button;

    @Mock
    private ViewPager viewPagerMock;

    @Mock
    private DotNavigationIndicator dotNavigationIndicatorMock;

    @Mock
    private ScrollView scrollViewMock;

    @Mock
    private TextView productDescriptionMock;

    @Mock
    private TextView ctnMock;

    @Mock
    private TextView priceMock;

    @Mock
    private TextView textView;

    @Mock
    private TextView textView1;

    @Mock
    private TextView textView2;

    @Mock
    private TextView textView3;

    private Context mContext;

    private ProductDetailFragment productDetailFragment;

    @Before
    public void setUp() {
        initMocks(this);
        TestUtils.getStubbedStore();
        TestUtils.getStubbedHybrisDelegate();
        isNetworkAvailable();

        mContext = getInstrumentation().getContext();
        productDetailFragment = ProductDetailFragment.createInstance(new Bundle(), InAppBaseFragment.AnimationType.NONE);
    }

    @Test(expected = RuntimeException.class)
    public void shouldDisplayAddressSelectionFragment() {

        SupportFragmentTestUtil.startFragment(productDetailFragment);
    }

    @Test
    public void shouldInitializeViews() throws Exception {
        initializaViews();
        productDetailFragment.initializeViews(viewMock);
    }

    @Test(expected = NullPointerException.class)
    public void shouldCallOnResume() throws Exception {
        initializaViews();
        Bundle bundle = new Bundle();
        productDetailFragment.setArguments(bundle);
        productDetailFragment.initializeViews(viewMock);
        productDetailFragment.onResume();

    }

    private void initializaViews() {
        Mockito.when(viewMock.findViewById(R.id.scrollView)).thenReturn(scrollViewMock);
        Mockito.when(viewMock.findViewById(R.id.iap_productDetailScreen_productDescription_lebel)).thenReturn(productDescriptionMock);
        Mockito.when(viewMock.findViewById(R.id.iap_productDetailsScreen_ctn_lebel)).thenReturn(ctnMock);
        Mockito.when(viewMock.findViewById(R.id.iap_productDetailsScreen_individualPrice_lebel)).thenReturn(priceMock);
        Mockito.when(viewMock.findViewById(R.id.iap_productDetailsScreen_addToCart_button)).thenReturn(progressBarAddToCartButtonMock);
        Mockito.when(viewMock.findViewById(R.id.iap_productDetailsScreen_buyFromRetailor_button)).thenReturn(progressBarBuyFromRetailerButtonMock);
        Mockito.when(viewMock.findViewById(R.id.iap_productDetailsScreen_btn_ll)).thenReturn(linearLayout1Mock);
        Mockito.when(viewMock.findViewById(R.id.iap_productDetailsScreen_quantity_delete_btn_ll)).thenReturn(linearLayout2Mock);
        Mockito.when(viewMock.findViewById(R.id.iap_productCatalogItem_discountedPrice_lebel)).thenReturn(textView);
        Mockito.when(viewMock.findViewById(R.id.iap_productDetailsScreen_outOfStock_label)).thenReturn(textView1);
        Mockito.when(viewMock.findViewById(R.id.delete_btn)).thenReturn(button);
        Mockito.when(viewMock.findViewById(R.id.quantity_val)).thenReturn(textView2);
        Mockito.when(viewMock.findViewById(R.id.iap_productDetailsScreen_productOverview)).thenReturn(textView3);
        Mockito.when(viewMock.findViewById(R.id.pager)).thenReturn(viewPagerMock);
        Mockito.when(viewMock.findViewById(R.id.indicator)).thenReturn(dotNavigationIndicatorMock);

        productDetailFragment.onAttach(mContext);
    }

    private void isNetworkAvailable() {
        final ConnectivityManager connectivityManager = mock(ConnectivityManager.class);
        final NetworkInfo networkInfo = mock(NetworkInfo.class);
        Mockito.when(connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI)).thenReturn(networkInfo);
        Mockito.when(networkInfo.isAvailable()).thenReturn(true);
        Mockito.when(networkInfo.isConnected()).thenReturn(true);
        NetworkUtility.getInstance().isNetworkAvailable(connectivityManager);
    }
}