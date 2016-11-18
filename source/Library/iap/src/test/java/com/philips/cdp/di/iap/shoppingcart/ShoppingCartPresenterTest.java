/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
package com.philips.cdp.di.iap.shoppingcart;

import android.content.Context;
import android.os.Message;
import android.support.v4.app.FragmentManager;

import com.android.volley.NetworkResponse;
import com.android.volley.ServerError;
import com.android.volley.VolleyError;
import com.philips.cdp.di.iap.R;
import com.philips.cdp.di.iap.TestUtils;
import com.philips.cdp.di.iap.cart.IAPCartListener;
import com.philips.cdp.di.iap.cart.ShoppingCartData;
import com.philips.cdp.di.iap.cart.ShoppingCartPresenter;
import com.philips.cdp.di.iap.container.CartModelContainer;
import com.philips.cdp.di.iap.model.AbstractModel;
import com.philips.cdp.di.iap.prx.MockPRXSummaryExecutor;
import com.philips.cdp.di.iap.response.carts.EntriesEntity;
import com.philips.cdp.di.iap.response.retailers.WebResults;
import com.philips.cdp.di.iap.session.HybrisDelegate;
import com.philips.cdp.di.iap.session.IAPNetworkError;
import com.philips.cdp.di.iap.session.MockNetworkController;
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
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;

@RunWith(RobolectricTestRunner.class)
public class ShoppingCartPresenterTest implements ShoppingCartPresenter.ShoppingCartListener<ShoppingCartData> {
    private MockNetworkController mNetworkController;
    private HybrisDelegate mHybrisDelegate;
    private ShoppingCartPresenter mShoppingCartPresenter;
    private MockPRXSummaryExecutor mMockPRXDataBuilder;
    private ArrayList<String> mCTNS = new ArrayList<>();

    @Mock
    private FragmentManager mFragmentManager;
    @Mock
    private Context mContext;

    @Before
    public void setUP() {
        MockitoAnnotations.initMocks(this);
        mHybrisDelegate = TestUtils.getStubbedHybrisDelegate();
        mNetworkController = (MockNetworkController) mHybrisDelegate.getNetworkController(mContext);

        mCTNS.add("HX9033/64");
        mShoppingCartPresenter = new ShoppingCartPresenter(mContext, this);
    }

    @Test
    public void testNullDelegate() throws JSONException {
        mShoppingCartPresenter = new ShoppingCartPresenter(mContext, this);
        mMockPRXDataBuilder = new MockPRXSummaryExecutor(mContext, mCTNS, mShoppingCartPresenter);
        mShoppingCartPresenter.setHybrisDelegate(null);
        mShoppingCartPresenter.getCurrentCartDetails();

        JSONObject obj = new JSONObject(TestUtils.readFile(ShoppingCartPresenterTest
                .class, "get_current_cart_response.txt"));
        mNetworkController.sendSuccess(obj);
        makePRXData();
    }

    @Test
    public void testGetCurrentCartDetailsSuccessResponse() throws JSONException {
        mShoppingCartPresenter = new ShoppingCartPresenter(mContext, this);
        mMockPRXDataBuilder = new MockPRXSummaryExecutor(mContext, mCTNS, mShoppingCartPresenter);
        mShoppingCartPresenter.setHybrisDelegate(mHybrisDelegate);
        mShoppingCartPresenter.getCurrentCartDetails();

        JSONObject obj = new JSONObject(TestUtils.readFile(ShoppingCartPresenterTest
                .class, "get_current_cart_response.txt"));
        mNetworkController.sendSuccess(obj);
        makePRXData();
    }

    @Test
    public void testGetCurrentCartDetailsErrorResponse() throws JSONException {
        mShoppingCartPresenter = new ShoppingCartPresenter(mContext, this);
        mShoppingCartPresenter.setHybrisDelegate(mHybrisDelegate);
        mShoppingCartPresenter.getCurrentCartDetails();

        JSONObject obj = new JSONObject(TestUtils.readFile(ShoppingCartPresenterTest
                .class, "get_cart_api_error_response.txt"));
        NetworkResponse networkResponse = new NetworkResponse(500, obj.toString().getBytes(),
                null, true, 1222L);

        VolleyError error = new ServerError(networkResponse);
        mNetworkController.sendFailure(error);
    }

    private void makePRXData() throws JSONException {
        PrxRequest mProductSummaryBuilder = new ProductSummaryRequest("125", null);

        JSONObject obj = new JSONObject(TestUtils.readFile(MockPRXSummaryExecutor
                .class, "get_prx_success_response_HX9033_64.txt"));
        ResponseData responseData = mProductSummaryBuilder.getResponseData(obj);
        CartModelContainer.getInstance().addProductSummary("HX9033/64", (SummaryModel) responseData);
        mMockPRXDataBuilder.sendSuccess(responseData);

        obj = new JSONObject(TestUtils.readFile(MockPRXSummaryExecutor
                .class, "get_prx_success_response_HX9023_64.txt"));
        responseData = mProductSummaryBuilder.getResponseData(obj);
        CartModelContainer.getInstance().addProductSummary("HX9023/64", (SummaryModel) responseData);
        mMockPRXDataBuilder.sendSuccess(responseData);

        obj = new JSONObject(TestUtils.readFile(MockPRXSummaryExecutor
                .class, "get_prx_success_response_HX9003_64.txt"));
        responseData = mProductSummaryBuilder.getResponseData(obj);
        CartModelContainer.getInstance().addProductSummary("HX9003/64", (SummaryModel) responseData);
        mMockPRXDataBuilder.sendSuccess(responseData);
    }

    @Test
    public void testDeleteCartSuccessResponse() throws
            JSONException, NoSuchFieldException, IllegalAccessException {
        mShoppingCartPresenter = new ShoppingCartPresenter(mContext, this);
        mShoppingCartPresenter.setHybrisDelegate(mHybrisDelegate);

        EntriesEntity entriesEntity = new EntriesEntity();
        Field entry = entriesEntity.getClass().getDeclaredField("entryNumber");
        entry.setAccessible(true);
        entry.setInt(entriesEntity, 0);

        ShoppingCartData data = new ShoppingCartData(entriesEntity, null);
        mShoppingCartPresenter.deleteProduct(data);
    }

    @Test
    public void testDeleteCartErrorResponse() throws
            JSONException, NoSuchFieldException, IllegalAccessException {
        mShoppingCartPresenter = new ShoppingCartPresenter(mContext, this);
        mShoppingCartPresenter.setHybrisDelegate(mHybrisDelegate);

        EntriesEntity entriesEntity = new EntriesEntity();
        Field entry = entriesEntity.getClass().getDeclaredField("entryNumber");
        entry.setAccessible(true);
        entry.setInt(entriesEntity, 0);

        ShoppingCartData data = new ShoppingCartData(entriesEntity, null);
        mShoppingCartPresenter.deleteProduct(data);
        mNetworkController.sendFailure(new VolleyError());
    }

    @Test
    public void testUpdateCartSuccessResponse() throws
            JSONException, NoSuchFieldException, IllegalAccessException {
        mShoppingCartPresenter = new ShoppingCartPresenter(mContext, this);
        mShoppingCartPresenter.setHybrisDelegate(mHybrisDelegate);

        EntriesEntity entriesEntity = new EntriesEntity();
        Field entry = entriesEntity.getClass().getDeclaredField("entryNumber");
        entry.setAccessible(true);
        entry.setInt(entriesEntity, 0);

        ShoppingCartData data = new ShoppingCartData(entriesEntity, null);
        Field ctn = data.getClass().getDeclaredField("mCtnNumber");
        ctn.setAccessible(true);
        ctn.set(data, "HX9003/64");
        mShoppingCartPresenter.updateProductQuantity(data, 4, getQuantityStatus(4, 1));
    }

    @Test
    public void testUpdateCartErrorResponse() throws
            JSONException, NoSuchFieldException, IllegalAccessException {
        mShoppingCartPresenter = new ShoppingCartPresenter(mContext, this);
        mShoppingCartPresenter.setHybrisDelegate(mHybrisDelegate);

        EntriesEntity entriesEntity = new EntriesEntity();
        Field entry = entriesEntity.getClass().getDeclaredField("entryNumber");
        entry.setAccessible(true);
        entry.setInt(entriesEntity, 0);

        ShoppingCartData data = new ShoppingCartData(entriesEntity, null);
        Field ctn = data.getClass().getDeclaredField("mCtnNumber");
        ctn.setAccessible(true);
        ctn.set(data, "HX9003/64");
        mShoppingCartPresenter.updateProductQuantity(data, 4, getQuantityStatus(4, 1));
        mNetworkController.sendFailure(new VolleyError());
    }

    private int getQuantityStatus(int newCount, int oldCount) {
        if (newCount > oldCount)
            return 1;
        else if (newCount < oldCount)
            return 0;
        else
            return -1;
    }

    @Test
    public void testGetCartCountSuccessResponse() throws JSONException {
        IAPCartListener mProductCountListener = new IAPCartListener() {
            @Override
            public void onSuccess(final int count) {
                assertEquals(1, count);
            }

            @Override
            public void onFailure(final Message msg) {
            }
        };

        mShoppingCartPresenter = new ShoppingCartPresenter();
        mShoppingCartPresenter.setHybrisDelegate(mHybrisDelegate);
        mShoppingCartPresenter.getProductCartCount(mContext, mProductCountListener);

        JSONObject obj = new JSONObject(TestUtils.readFile(ShoppingCartPresenterTest
                .class, "get_cart_api_success_response.txt"));
        mNetworkController.sendSuccess(obj);
    }

    @Test
    public void testGetCartCountEmptySuccessResponse() throws JSONException {
        IAPCartListener mProductCountListener = new IAPCartListener() {
            @Override
            public void onSuccess(final int count) {
                assertEquals(3, count);
            }

            @Override
            public void onFailure(final Message msg) {
            }
        };

        mShoppingCartPresenter = new ShoppingCartPresenter();
        mShoppingCartPresenter.setHybrisDelegate(mHybrisDelegate);
        mShoppingCartPresenter.getProductCartCount(mContext, mProductCountListener);
        JSONObject obj = new JSONObject(TestUtils.readFile(ShoppingCartPresenterTest
                .class, "get_cart_api_empty_response.txt"));
        mNetworkController.sendSuccess(obj);
    }

    @Test
    public void testNoCartCreatedResponse() throws JSONException {
        IAPCartListener mProductCountListener = new IAPCartListener() {
            @Override
            public void onSuccess(final int count) {
                assertEquals(3, count);
            }

            @Override
            public void onFailure(final Message msg) {
            }
        };

        mShoppingCartPresenter = new ShoppingCartPresenter(mContext, this);
        mShoppingCartPresenter.setHybrisDelegate(mHybrisDelegate);
        mShoppingCartPresenter.getProductCartCount(mContext, mProductCountListener);
        JSONObject obj = new JSONObject(TestUtils.readFile(ShoppingCartPresenterTest
                .class, "get_product_count_error_no_cart_created.txt"));
        NetworkResponse networkResponse = new NetworkResponse
                (500, obj.toString().getBytes(), null, true, 1222L);
        VolleyError error = new ServerError(networkResponse);
        mNetworkController.sendFailure(error);
    }

    @Test
    public void testBuyProductWhenProductAlreadyInCart() throws JSONException {
        IAPCartListener mProductCountListener = new IAPCartListener() {
            @Override
            public void onSuccess(final int count) {
            }

            @Override
            public void onFailure(final Message msg) {
            }
        };

        mShoppingCartPresenter = new ShoppingCartPresenter();
        mShoppingCartPresenter.setHybrisDelegate(mHybrisDelegate);
        mShoppingCartPresenter.buyProduct(mContext, "HX9003/64", mProductCountListener);
        JSONObject obj = new JSONObject(TestUtils.readFile(ShoppingCartPresenterTest
                .class, "get_cart_api_success_response.txt"));
        mNetworkController.sendSuccess(obj);
    }

    @Test
    public void testBuyProductWhenProductNotInCart() throws JSONException {
        IAPCartListener mProductCountListener = new IAPCartListener() {
            @Override
            public void onSuccess(final int count) {
            }

            @Override
            public void onFailure(final Message msg) {
            }
        };

        mShoppingCartPresenter = new ShoppingCartPresenter();
        mShoppingCartPresenter.setHybrisDelegate(mHybrisDelegate);
        mShoppingCartPresenter.buyProduct(mContext, "HX9003/63", mProductCountListener);
        JSONObject obj = new JSONObject(TestUtils.readFile(ShoppingCartPresenterTest
                .class, "get_cart_api_success_response.txt"));
        mNetworkController.sendSuccess(obj);
    }

    @Test
    public void testGetRetailersInformation() throws JSONException {
        mShoppingCartPresenter = new ShoppingCartPresenter(mContext, this);
        mShoppingCartPresenter.setHybrisDelegate(mHybrisDelegate);
        mShoppingCartPresenter.getRetailersInformation("HX8071/10");

        new AbstractModel.DataLoadListener() {
            @Override
            public void onModelDataLoadFinished(Message msg) {
                assertTrue(msg.obj instanceof WebResults);
            }

            @Override
            public void onModelDataError(Message msg) {

            }
        };

        JSONObject obj = new JSONObject(TestUtils.readFile(ShoppingCartPresenterTest
                .class, "retailers.txt"));
        mNetworkController.sendSuccess(obj);
    }

    @Test
    public void testGetRetailersInformationErrorResponse() throws JSONException {
        mShoppingCartPresenter = new ShoppingCartPresenter(mContext, this);
        mShoppingCartPresenter.setHybrisDelegate(mHybrisDelegate);
        mShoppingCartPresenter.getRetailersInformation("HX8071/10");

        new AbstractModel.DataLoadListener() {
            @Override
            public void onModelDataLoadFinished(Message msg) {
                assertTrue(msg.obj instanceof WebResults);
            }

            @Override
            public void onModelDataError(Message msg) {

            }
        };

        JSONObject obj = new JSONObject(TestUtils.readFile(ShoppingCartPresenterTest
                .class, "retailers.txt"));
        mNetworkController.sendFailure(new VolleyError());
    }

    @Test
    public void verifyNullHybrisDelegate() {
        mShoppingCartPresenter = new ShoppingCartPresenter(mContext, this);
        mShoppingCartPresenter.setHybrisDelegate(null);
        mShoppingCartPresenter.getHybrisDelegate();
    }

    @Override
    public void onLoadFinished(final ArrayList<ShoppingCartData> data) {
        if (data.size() > 0 && data.get(0) != null) {
            assertEquals(mCTNS.size(), data.size());
        } else {
            assertFalse(false);
        }
    }

    @Override
    public void onLoadError(final Message msg) {
        assertTrue(msg.obj instanceof IAPNetworkError);
    }

    @Override
    public void onRetailerError(final IAPNetworkError errorMsg) {
        assertEquals(errorMsg.getMessage(), mContext.getString(R.string.iap_no_retailer_message));
    }
}
