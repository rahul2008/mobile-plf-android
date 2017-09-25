/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
package com.philips.cdp.di.iap.hybris;

import android.content.Context;
import android.os.Message;

import com.android.volley.TimeoutError;
import com.philips.cdp.di.iap.TestUtils;
import com.philips.cdp.di.iap.iapHandler.HybrisHandler;
import com.philips.cdp.di.iap.integration.IAPListener;
import com.philips.cdp.di.iap.session.HybrisDelegate;
import com.philips.cdp.di.iap.session.IAPNetworkError;
import com.philips.cdp.di.iap.utils.IAPConstant;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.robolectric.RobolectricTestRunner;

import java.util.ArrayList;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;

@RunWith(RobolectricTestRunner.class)
public class HybrisHandlerTest {
    @Mock
    Context mContext;
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
            public void onSuccess(boolean bool) {

            }

            @Override
            public void onFailure(int errorCode) {

            }

        };
    }

    @Test
    public void testGetCompleteProductList() throws Exception {
        TestUtils.getStubbedHybrisDelegate();
        mHybrisHandler.getCompleteProductList(mIAPListener);
    }

    @Test
    public void testGetCompleteProductListWhenStoreNotInitialized() throws Exception {
        //TestUtils.getStubbedHybrisDelegate();
        mHybrisHandler.getCompleteProductList(mIAPListener);
    }

    @Test
    public void testGetProductCount() throws Exception {
        TestUtils.getStubbedHybrisDelegate();
        mHybrisHandler.getProductCartCount(mIAPListener);
    }
    @Test
    public void testGetProductCountWhenStoreIsNotInitilized() throws Exception {
//        TestUtils.getStubbedHybrisDelegate();
        mHybrisHandler.getProductCartCount(mIAPListener);
    }
    @Test
    public void testIAPErrorCodeForUnknownError() throws Exception {
        assertEquals(IAPConstant.IAP_ERROR_UNKNOWN, mHybrisHandler.getIAPErrorCode(new Message()));
    }

    @Test
    public void testIAPErrorCodeForIAPNetworkError() throws Exception {
        Message msg = new Message();
        msg.obj = new IAPNetworkError(new TimeoutError(), 0, null);
        assertEquals(IAPConstant.IAP_ERROR_CONNECTION_TIME_OUT, mHybrisHandler.getIAPErrorCode(msg));
    }

    @Test
    public void testStoreInitializationWithHybrisInitialization(){
        TestUtils.getStubbedHybrisDelegate();
        assertTrue(HybrisDelegate.getInstance().getStore().isStoreInitialized());
    }
}