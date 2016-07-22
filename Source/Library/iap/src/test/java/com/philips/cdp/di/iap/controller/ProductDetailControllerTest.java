package com.philips.cdp.di.iap.controller;

import android.content.Context;
import android.os.Message;

import com.philips.cdp.di.iap.TestUtils;
import com.philips.cdp.di.iap.response.State.RegionsList;
import com.philips.cdp.di.iap.response.products.ProductDetailEntity;
import com.philips.cdp.di.iap.session.HybrisDelegate;
import com.philips.cdp.di.iap.session.MockNetworkController;
import com.philips.cdp.di.iap.session.RequestCode;
import com.philips.cdp.di.iap.store.NetworkURLConstants;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.robolectric.RobolectricTestRunner;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;
import static org.junit.Assert.*;

@RunWith(RobolectricTestRunner.class)
public class ProductDetailControllerTest {

    private MockNetworkController mNetworkController;
    private HybrisDelegate mHybrisDelegate;

    @Mock
    private ProductDetailController mProductDetailController;
    @Mock
    private Context mContext;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        mHybrisDelegate = TestUtils.getStubbedHybrisDelegate();
        mNetworkController = (MockNetworkController) mHybrisDelegate.getNetworkController(mContext);
    }

    @Test
    public void testProductDetailSuccessResponseWithData() throws JSONException {
        mProductDetailController = new ProductDetailController(mContext, new MockProductSearchListener() {
            @Override
            public void onGetProductDetail(Message msg) {
                assertEquals(RequestCode.SEARCH_PRODUCT, msg.what);
                assertTrue(msg.obj instanceof ProductDetailEntity);
            }
        });

        setStoreAndDelegate();
        mProductDetailController.getProductDetail(NetworkURLConstants.DUMMY_PRODUCT_ID);
        JSONObject obj = new JSONObject(TestUtils.readFile(ProductDetailControllerTest.class, "ProductSearch.txt"));
        mNetworkController.sendSuccess(obj);
    }

    public void setStoreAndDelegate() {
        mProductDetailController.setHybrisDelegate(mHybrisDelegate);
        mProductDetailController.setStore(TestUtils.getStubbedStore());
    }

}