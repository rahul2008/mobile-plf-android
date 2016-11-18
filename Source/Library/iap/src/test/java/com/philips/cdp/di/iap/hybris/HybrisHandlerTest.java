/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
package com.philips.cdp.di.iap.hybris;

import android.content.Context;
import android.os.Message;

import com.philips.cdp.di.iap.TestUtils;
import com.philips.cdp.di.iap.iapHandler.HybrisHandler;
import com.philips.cdp.di.iap.integration.IAPListener;
import com.philips.cdp.di.iap.session.HybrisDelegate;
import com.philips.cdp.di.iap.utils.IAPConstant;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;

import static junit.framework.Assert.*;

public class HybrisHandlerTest {
    @Mock
    Context mContext;

    @Mock
    Message mMessage;

    private HybrisHandler mHybrisHandler;
    private IAPListener mIAPListener;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);

        mHybrisHandler = new HybrisHandler(mContext);
        mIAPListener = new IAPListener() {
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
            public void onFailure(int errorCode) {

            }
        };
    }


    @Test(expected = RuntimeException.class)
    public void testGetCompleteProductList() throws Exception {
        mHybrisHandler.getCompleteProductList(mIAPListener);
    }

    @Test(expected = NullPointerException.class)
    public void testGetProductCount() throws Exception {
        mHybrisHandler.getProductCartCount(mIAPListener);
    }

    @Test
    public void testIAPErrorCodeForUnknownError() throws Exception {
        assertEquals(IAPConstant.IAP_ERROR_UNKNOWN, mHybrisHandler.getIAPErrorCode(new Message()));
    }

    @Test(expected = NullPointerException.class)
    public void testStoreInitialization(){
        assertFalse(HybrisDelegate.getInstance().getStore().isStoreInitialized());
    }

    @Test
    public void testStoreInitializationWithHybrisInitialization(){
        TestUtils.getStubbedHybrisDelegate();
        assertTrue(HybrisDelegate.getInstance().getStore().isStoreInitialized());
    }
}