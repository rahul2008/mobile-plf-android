package com.philips.cdp.di.iap.shoppingcart;

import android.content.Context;
import android.support.v4.app.FragmentManager;

import com.philips.cdp.di.iap.prx.MockPRXDataBuilder;
import com.philips.cdp.di.iap.ShoppingCart.ShoppingCartData;
import com.philips.cdp.di.iap.ShoppingCart.ShoppingCartPresenter;
import com.philips.cdp.di.iap.TestUtils;
import com.philips.cdp.di.iap.session.HybrisDelegate;
import com.philips.cdp.di.iap.session.IAPNetworkError;
import com.philips.cdp.di.iap.session.MockNetworkController;
import com.philips.cdp.prxclient.request.ProductSummaryRequest;
import com.philips.cdp.prxclient.request.PrxRequest;
import com.philips.cdp.prxclient.response.ResponseData;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.robolectric.RobolectricTestRunner;

import java.util.ArrayList;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;
import static org.mockito.Mockito.mock;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
@RunWith(RobolectricTestRunner.class)
public class ShoppingCartPresenterTestLatest implements ShoppingCartPresenter.LoadListener<ShoppingCartData>{
    private MockNetworkController mNetworkController;
    private HybrisDelegate mHybrisDelegate;
    private ShoppingCartPresenter mShoppingCartPresenter;
    private MockPRXDataBuilder mMockPRXDataBuilder;
    @Mock
    private FragmentManager mFragmentManager;

    @Mock
    private Context mContext;

    @Before
    public void setUP() {
        MockitoAnnotations.initMocks(this);
        mNetworkController = new MockNetworkController(mContext);
        mHybrisDelegate = TestUtils.getStubbedHybrisDelegate();
        mNetworkController = (MockNetworkController) mHybrisDelegate.getNetworkController(null);
    }

    @Test
    public void getCurrentCartDetailsVerifySuccess() throws JSONException {
        mShoppingCartPresenter =new ShoppingCartPresenter(mContext, this, mFragmentManager);
        mMockPRXDataBuilder = new MockPRXDataBuilder(mContext, new ArrayList<String>(), mShoppingCartPresenter);
        mShoppingCartPresenter.setHybrisDelegate(mHybrisDelegate);
        mShoppingCartPresenter.getCurrentCartDetails();

        JSONObject obj = new JSONObject(TestUtils.readFile(ShoppingCartPresenterTest
                .class, "get_cart_api_success_response.txt"));
        mNetworkController.sendSuccess(obj);
        PrxRequest mProductSummaryBuilder = new ProductSummaryRequest("125", null);

        obj = new JSONObject(TestUtils.readFile(MockPRXDataBuilder
                .class, "get_prx_success_response.txt"));
        ResponseData responseData = mProductSummaryBuilder.getResponseData(obj);

        mMockPRXDataBuilder.sendSuccess(responseData);
    }


    @Override
    public void onLoadFinished(final ArrayList<ShoppingCartData> data) {
        boolean isShoppingCartDataReturned = data.get(0) instanceof ShoppingCartData;
        assert(isShoppingCartDataReturned);
    }

    @Override
    public void onLoadListenerError(final IAPNetworkError error) {

    }

    @Override
    public void onRetailerError(final IAPNetworkError errorMsg) {

    }
}
