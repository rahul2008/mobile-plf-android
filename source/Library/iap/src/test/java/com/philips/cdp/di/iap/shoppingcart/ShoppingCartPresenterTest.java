package com.philips.cdp.di.iap.shoppingcart;

import android.content.Context;
import android.support.v4.app.FragmentManager;

import com.android.volley.NetworkResponse;
import com.android.volley.ServerError;
import com.android.volley.VolleyError;
import com.philips.cdp.di.iap.container.CartModelContainer;
import com.philips.cdp.di.iap.prx.MockPRXDataBuilder;
import com.philips.cdp.di.iap.ShoppingCart.ShoppingCartData;
import com.philips.cdp.di.iap.ShoppingCart.ShoppingCartPresenter;
import com.philips.cdp.di.iap.TestUtils;
import com.philips.cdp.di.iap.response.carts.DeliveryModeEntity;
import com.philips.cdp.di.iap.response.carts.EntriesEntity;
import com.philips.cdp.di.iap.session.HybrisDelegate;
import com.philips.cdp.di.iap.session.IAPNetworkError;
import com.philips.cdp.di.iap.session.MockNetworkController;
import com.philips.cdp.di.iap.store.VerticalAppConfig;
import com.philips.cdp.prxclient.datamodels.summary.SummaryModel;
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

import java.lang.reflect.Field;
import java.util.ArrayList;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;
import static org.mockito.Mockito.mock;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
@RunWith(RobolectricTestRunner.class)
public class ShoppingCartPresenterTest implements ShoppingCartPresenter.LoadListener<ShoppingCartData>{
    private MockNetworkController mNetworkController;
    private HybrisDelegate mHybrisDelegate;
    private ShoppingCartPresenter mShoppingCartPresenter;
    private MockPRXDataBuilder mMockPRXDataBuilder;
    @Mock
    private FragmentManager mFragmentManager;

    @Mock
    private Context mContext;

    ArrayList<String> ctns = new ArrayList<>();


    @Before
    public void setUP() {
        MockitoAnnotations.initMocks(this);
        mNetworkController = new MockNetworkController(mContext);
        mHybrisDelegate = TestUtils.getStubbedHybrisDelegate();
        mNetworkController = (MockNetworkController) mHybrisDelegate.getNetworkController(null);
        ctns.add("HX9033/64");
        ctns.add("HX9023/64");
        ctns.add("HX9003/64");
    }

    @Test
    public void getCurrentCartDetailsVerifySuccess() throws JSONException {
        mShoppingCartPresenter =new ShoppingCartPresenter(mContext, this, mFragmentManager);
        mMockPRXDataBuilder = new MockPRXDataBuilder(mContext, ctns, mShoppingCartPresenter);
        mShoppingCartPresenter.setHybrisDelegate(mHybrisDelegate);
        mShoppingCartPresenter.getCurrentCartDetails();

        JSONObject obj = new JSONObject(TestUtils.readFile(ShoppingCartPresenterTest
                .class, "get_cart_api_success_response.txt"));
        mNetworkController.sendSuccess(obj);

        makePRXData();
    }

    @Test
    public void getCurrentCartDetailsVerifyHybrisFail() throws JSONException {
        mShoppingCartPresenter =new ShoppingCartPresenter(mContext, this, mFragmentManager);
        mShoppingCartPresenter.setHybrisDelegate(mHybrisDelegate);
        mShoppingCartPresenter.getCurrentCartDetails();

        JSONObject obj = new JSONObject(TestUtils.readFile(ShoppingCartPresenterTest
                .class, "get_cart_api_error_response.txt"));
        NetworkResponse networkResponse = new NetworkResponse(500, obj.toString().getBytes(),null,true,1222L);

        VolleyError error = new ServerError(networkResponse);

        mNetworkController.sendFailure(error);
    }

    private void makePRXData() throws JSONException {
        PrxRequest mProductSummaryBuilder = new ProductSummaryRequest("125", null);

        JSONObject obj = new JSONObject(TestUtils.readFile(MockPRXDataBuilder
                .class, "get_prx_success_response_HX9033_64.txt"));
        ResponseData responseData = mProductSummaryBuilder.getResponseData(obj);
        CartModelContainer.getInstance().addProductDataToList("HX9033/64", (SummaryModel) responseData);
        mMockPRXDataBuilder.sendSuccess(responseData);

        obj = new JSONObject(TestUtils.readFile(MockPRXDataBuilder
                .class, "get_prx_success_response_HX9023_64.txt"));
        responseData = mProductSummaryBuilder.getResponseData(obj);
        CartModelContainer.getInstance().addProductDataToList("HX9023/64", (SummaryModel) responseData);
        mMockPRXDataBuilder.sendSuccess(responseData);

        obj = new JSONObject(TestUtils.readFile(MockPRXDataBuilder
                .class, "get_prx_success_response_HX9003_64.txt"));
        responseData = mProductSummaryBuilder.getResponseData(obj);
        CartModelContainer.getInstance().addProductDataToList("HX9003/64", (SummaryModel) responseData);
        mMockPRXDataBuilder.sendSuccess(responseData);
    }

    @Test
    public void DeleteCartVerifyHybrisSuccess() throws JSONException, NoSuchFieldException, IllegalAccessException {
        mShoppingCartPresenter =new ShoppingCartPresenter(mContext, this, mFragmentManager);
        mMockPRXDataBuilder = new MockPRXDataBuilder(mContext, ctns, mShoppingCartPresenter);
        mShoppingCartPresenter.setHybrisDelegate(mHybrisDelegate);
        EntriesEntity entriesEntity = new EntriesEntity();
        Field entry = entriesEntity.getClass().getDeclaredField("entryNumber");
        entry.setAccessible(true);
        entry.setInt(entriesEntity,0);
        ShoppingCartData data = new ShoppingCartData(entriesEntity, null);
        mShoppingCartPresenter.deleteProduct(data);
    }

    @Override
    public void onLoadFinished(final ArrayList<ShoppingCartData> data) {
        boolean isShoppingCartDataReturned = data.get(0) instanceof ShoppingCartData;
        assert(isShoppingCartDataReturned);
        assertEquals(ctns.size(),data.size());
    }

    @Override
    public void onLoadListenerError(final IAPNetworkError error) {
        boolean isHybrisError = error instanceof IAPNetworkError;
        assert(isHybrisError);
        assertEquals(error.getStatusCode(),error.getIAPErrorCode());
        assertEquals("Hybris", error.getServerError().getErrors().get(0).getType());
        assertEquals("Hybris Server Down", error.getServerError().getErrors().get(0).getReason());
        assertEquals("Hybris Error", error.getServerError().getErrors().get(0).getSubject());
    }

    @Override
    public void onRetailerError(final IAPNetworkError errorMsg) {

    }
}
