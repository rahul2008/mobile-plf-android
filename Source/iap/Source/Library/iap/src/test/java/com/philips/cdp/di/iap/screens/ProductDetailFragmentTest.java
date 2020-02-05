/*
 * Copyright (c) 2015-2018 Koninklijke Philips N.V.
 * All rights reserved.
 */

package com.philips.cdp.di.iap.screens;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import androidx.viewpager.widget.ViewPager;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.philips.cdp.di.iap.R;
import com.philips.cdp.di.iap.TestUtils;
import com.philips.cdp.di.iap.container.CartModelContainer;
import com.philips.cdp.di.iap.utils.NetworkUtility;
import com.philips.platform.appinfra.AppInfraInterface;
import com.philips.platform.appinfra.logging.LoggingInterface;
import com.philips.platform.uid.view.widget.DotNavigationIndicator;
import com.philips.platform.uid.view.widget.ProgressBarButton;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.shadows.support.v4.SupportFragmentController;

import static androidx.test.platform.app.InstrumentationRegistry.getInstrumentation;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
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

    @Mock
    private AppInfraInterface appInfraMock;

    @Mock
    private LoggingInterface loggingMock;

    private Context mContext;

    private ProductDetailFragment productDetailFragment;

    @Before
    public void setUp() {
        initMocks(this);
        TestUtils.getStubbedStore();
        TestUtils.getStubbedHybrisDelegate();
        isNetworkAvailable();

        when(loggingMock.createInstanceForComponent(any(), any())).thenReturn(loggingMock);
        when(appInfraMock.getLogging()).thenReturn(loggingMock);
        CartModelContainer.getInstance().setAppInfraInstance(appInfraMock);

        mContext = getInstrumentation().getContext();

        productDetailFragment = ProductDetailFragment.createInstance(new Bundle(), InAppBaseFragment.AnimationType.NONE);
//        SupportFragmentController.of(productDetailFragment).create().start().resume();
    }

    @Test
    public void shouldInitializeViews() throws Exception {
        initializeViews();
        productDetailFragment.initializeViews(viewMock);
    }

    @Test()
    public void shouldCallOnResume() throws Exception {
        initializeViews();
        Bundle bundle = new Bundle();
        productDetailFragment.setArguments(bundle);
        productDetailFragment.initializeViews(viewMock);
        productDetailFragment.onResume();
    }

    private void initializeViews() {
        when(viewMock.findViewById(R.id.scrollView)).thenReturn(scrollViewMock);
        when(viewMock.findViewById(R.id.iap_productDetailScreen_productDescription_lebel)).thenReturn(productDescriptionMock);
        when(viewMock.findViewById(R.id.iap_productDetailsScreen_ctn_lebel)).thenReturn(ctnMock);
        when(viewMock.findViewById(R.id.iap_productDetailsScreen_individualPrice_lebel)).thenReturn(priceMock);
        when(viewMock.findViewById(R.id.iap_productDetailsScreen_addToCart_button)).thenReturn(progressBarAddToCartButtonMock);
        when(viewMock.findViewById(R.id.iap_productDetailsScreen_buyFromRetailor_button)).thenReturn(progressBarBuyFromRetailerButtonMock);
        when(viewMock.findViewById(R.id.iap_productDetailsScreen_btn_ll)).thenReturn(linearLayout1Mock);
        when(viewMock.findViewById(R.id.iap_productDetailsScreen_quantity_delete_btn_ll)).thenReturn(linearLayout2Mock);
        when(viewMock.findViewById(R.id.iap_productCatalogItem_discountedPrice_lebel)).thenReturn(textView);
        when(viewMock.findViewById(R.id.iap_productDetailsScreen_outOfStock_label)).thenReturn(textView1);
        when(viewMock.findViewById(R.id.delete_btn)).thenReturn(button);
        when(viewMock.findViewById(R.id.quantity_val)).thenReturn(textView2);
        when(viewMock.findViewById(R.id.iap_productDetailsScreen_productOverview)).thenReturn(textView3);
        when(viewMock.findViewById(R.id.pager)).thenReturn(viewPagerMock);
        when(viewMock.findViewById(R.id.indicator)).thenReturn(dotNavigationIndicatorMock);

        productDetailFragment.onAttach(mContext);
    }

    private void isNetworkAvailable() {
        final ConnectivityManager connectivityManager = mock(ConnectivityManager.class);
        final NetworkInfo networkInfo = mock(NetworkInfo.class);
        when(connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI)).thenReturn(networkInfo);
        when(networkInfo.isAvailable()).thenReturn(true);
        when(networkInfo.isConnected()).thenReturn(true);
        NetworkUtility.getInstance().isNetworkAvailable(connectivityManager);
    }
}