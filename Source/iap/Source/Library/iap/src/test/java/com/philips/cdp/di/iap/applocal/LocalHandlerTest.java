/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
package com.philips.cdp.di.iap.applocal;

import android.content.Context;

import com.philips.cdp.di.iap.iapHandler.LocalHandler;
import com.philips.cdp.di.iap.integration.IAPListener;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.robolectric.RobolectricTestRunner;

import java.util.ArrayList;

@RunWith(RobolectricTestRunner.class)
public class LocalHandlerTest {
    @Mock
    Context mContext;
    LocalHandler mAppLocalHandler;

    IAPListener mIapListener = new IAPListener() {
        @Override
        public void onGetCartCount(int count) {
        }

        @Override
        public void onUpdateCartCount() {

        }

        @Override
        public void updateCartIconVisibility(boolean shouldShow) {

        }

        @Override
        public void onGetCompleteProductList(ArrayList<String> productList) {

        }

        @Override
        public void onSuccess() {

        }

        @Override
        public void onSuccess(boolean bool) {

        }

        @Override
        public void onFailure(int errorCode) {

        }

    };

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        mAppLocalHandler = new LocalHandler();
    }

    @Test
    public void testGetProductCartCount() throws Exception {
        mAppLocalHandler.getProductCartCount(mIapListener);
    }

    @Test
    public void testGetCompleteProductList() throws Exception {
        mAppLocalHandler.getCompleteProductList(mIapListener);
    }
}