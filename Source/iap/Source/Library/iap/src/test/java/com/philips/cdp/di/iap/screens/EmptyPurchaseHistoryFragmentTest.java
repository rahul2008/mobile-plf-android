/*
 * Copyright (c) 2015-2018 Koninklijke Philips N.V.
 * All rights reserved.
 */

package com.philips.cdp.di.iap.screens;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import android.view.View;

import com.philips.cdp.di.iap.R;
import com.philips.cdp.di.iap.TestUtils;
import com.philips.cdp.di.iap.activity.IAPActivity;
import com.philips.cdp.di.iap.utils.NetworkUtility;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.shadows.support.v4.SupportFragmentTestUtil;

import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.robolectric.Robolectric.buildActivity;

@RunWith(RobolectricTestRunner.class)
public class EmptyPurchaseHistoryFragmentTest {

    @Mock
    View view;

    @Mock
    private FragmentManager fragmentManager;

    @Mock
    private FragmentTransaction fragmentTransactionMock;

    @Mock
    private Fragment fragment;

    private EmptyPurchaseHistoryFragment emptyPurchaseHistoryFragment;

    @Before
    public void setUp() {
        initMocks(this);

        TestUtils.getStubbedStore();
        TestUtils.getStubbedHybrisDelegate();
        emptyPurchaseHistoryFragment = EmptyPurchaseHistoryFragment.createInstance(new Bundle(), InAppBaseFragment.AnimationType.NONE);
    }

    @Test
    public void shouldDisplayAddressSelectionFragment() {
        SupportFragmentTestUtil.startFragment(emptyPurchaseHistoryFragment);
    }

    @Test(expected = NullPointerException.class)
    public void shouldHandleBackEvent() {
        when(fragmentManager.beginTransaction()).thenReturn(fragmentTransactionMock);
        IAPActivity activity = buildActivity(IAPActivity.class, null).get();
        IAPActivity spyActivity = Mockito.spy(activity);

        Mockito.doReturn(fragmentManager).when(spyActivity).getSupportFragmentManager();
        when(fragmentManager.findFragmentByTag(ProductCatalogFragment.TAG)).thenReturn(emptyPurchaseHistoryFragment);
        when(fragment.getFragmentManager()).thenReturn(fragmentManager);
        emptyPurchaseHistoryFragment.handleBackEvent();
        SupportFragmentTestUtil.startFragment(emptyPurchaseHistoryFragment);
    }

    @Test(expected = NullPointerException.class)
    public void shouldOnClick() {
        networkConnected();
        when(view.getId()).thenReturn(R.id.btn_continue_shopping);
        emptyPurchaseHistoryFragment.onClick(view);
        SupportFragmentTestUtil.startFragment(emptyPurchaseHistoryFragment);
    }

    private void networkConnected() {
        final ConnectivityManager connectivityManager = Mockito.mock(ConnectivityManager.class);
        final NetworkInfo networkInfo = Mockito.mock(NetworkInfo.class);
        when(connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI)).thenReturn(networkInfo);
        when(networkInfo.isAvailable()).thenReturn(true);
        when(networkInfo.isConnected()).thenReturn(true);
        NetworkUtility.getInstance().isNetworkAvailable(connectivityManager);
    }
}