/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
package com.philips.cdp.di.iap.shoppingcart;

import android.content.Context;
import android.os.Message;
import androidx.fragment.app.FragmentManager;

import com.android.volley.NetworkResponse;
import com.android.volley.ServerError;
import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.philips.cdp.di.iap.R;
import com.philips.cdp.di.iap.TestUtils;
import com.philips.cdp.di.iap.cart.IAPCartListener;
import com.philips.cdp.di.iap.cart.ShoppingCartData;
import com.philips.cdp.di.iap.cart.ShoppingCartPresenter;
import com.philips.cdp.di.iap.controller.AddressController;
import com.philips.cdp.di.iap.model.AbstractModel;
import com.philips.cdp.di.iap.prx.MockPRXSummaryListExecutor;
import com.philips.cdp.di.iap.response.addresses.GetDeliveryModes;
import com.philips.cdp.di.iap.response.addresses.GetUser;
import com.philips.cdp.di.iap.response.carts.AppliedOrderPromotionEntity;
import com.philips.cdp.di.iap.response.carts.BasePriceEntity;
import com.philips.cdp.di.iap.response.carts.CartsEntity;
import com.philips.cdp.di.iap.response.carts.DeliveryCostEntity;
import com.philips.cdp.di.iap.response.carts.DeliveryModeEntity;
import com.philips.cdp.di.iap.response.carts.EntriesEntity;
import com.philips.cdp.di.iap.response.carts.ProductEntity;
import com.philips.cdp.di.iap.response.carts.PromotionEntity;
import com.philips.cdp.di.iap.response.carts.TotalPriceEntity;
import com.philips.cdp.di.iap.response.retailers.WebResults;
import com.philips.cdp.di.iap.session.HybrisDelegate;
import com.philips.cdp.di.iap.session.IAPNetworkError;
import com.philips.cdp.di.iap.session.MockNetworkController;
import com.philips.cdp.prxclient.request.ProductSummaryRequest;
import com.philips.cdp.prxclient.request.PrxRequest;
import com.philips.cdp.prxclient.response.ResponseData;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;

@Config(sdk=23)
@RunWith(RobolectricTestRunner.class)
public class ShoppingCartPresenterTest implements ShoppingCartPresenter.ShoppingCartListener<ShoppingCartData>, AddressController.AddressListener {
    private MockNetworkController mNetworkController;
    private HybrisDelegate mHybrisDelegate;
    private ShoppingCartPresenter mShoppingCartPresenter;
    AddressController mAddressController;
    private MockPRXSummaryListExecutor mMockPRXDataBuilder;
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
        mAddressController = new AddressController(mContext, this);
    }

    @Test(expected = NullPointerException.class)
    public void testNullDelegate() throws JSONException {
        mShoppingCartPresenter = new ShoppingCartPresenter(mContext, this);
        mMockPRXDataBuilder = new MockPRXSummaryListExecutor(mContext, mCTNS, mShoppingCartPresenter);
        mShoppingCartPresenter.setHybrisDelegate(null);
        mShoppingCartPresenter.getCurrentCartDetails();

        JSONObject obj = new JSONObject(TestUtils.readFile(ShoppingCartPresenterTest
                .class, "get_current_cart_response.txt"));
        mNetworkController.sendSuccess(obj);
        makePRXData();
    }

    @Test(expected = NullPointerException.class)
    public void testGetCurrentCartDetailsSuccessResponse() throws JSONException {
        mShoppingCartPresenter = new ShoppingCartPresenter(mContext, this);
        mMockPRXDataBuilder = new MockPRXSummaryListExecutor(mContext, mCTNS, mShoppingCartPresenter);
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

        JSONObject obj = new JSONObject(TestUtils.readFile(MockPRXSummaryListExecutor
                .class, "get_prx_success_response_HX9033_64.txt"));
        ResponseData responseData = mProductSummaryBuilder.getResponseData(obj);
        mMockPRXDataBuilder.sendSuccess(responseData);

    }

    @Test
    public void testDeleteProductSuccessResponse() throws
            JSONException, NoSuchFieldException, IllegalAccessException {
        mShoppingCartPresenter = new ShoppingCartPresenter(mContext, this);
        mShoppingCartPresenter.setHybrisDelegate(mHybrisDelegate);

        EntriesEntity entriesEntity = new EntriesEntity();
        Field entry = entriesEntity.getClass().getDeclaredField("entryNumber");
        entry.setAccessible(true);
        entry.setInt(entriesEntity, 0);

        ShoppingCartData data = new ShoppingCartData(entriesEntity, null);
        mShoppingCartPresenter.deleteProduct(data);

        JSONObject obj = new JSONObject(TestUtils.readFile(ShoppingCartPresenterTest
                .class, "EmptyResponse.txt"));
        mNetworkController.sendSuccess(obj);
    }

    @Test
    public void testDeleteProductErrorResponse() throws
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
        ctn.set(data, "HX8331/11");
        mShoppingCartPresenter.updateProductQuantity(data, 5, getQuantityStatus(5, 1));

        JSONObject obj = new JSONObject(TestUtils.readFile(ShoppingCartPresenterTest
                .class, "update_cart_response.txt"));
        mNetworkController.sendSuccess(obj);

    }

    @Test
    public void testUpdateCartSuccessResponseOnProductRemove() throws
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
        ctn.set(data, "HX8331/11");
        mShoppingCartPresenter.updateProductQuantity(data, 5, getQuantityStatus(1, 2));

        JSONObject obj = new JSONObject(TestUtils.readFile(ShoppingCartPresenterTest
                .class, "update_cart_response.txt"));
        mNetworkController.sendSuccess(obj);

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
    public void testGetCartCountWithTwoCartsSuccessResponse() throws JSONException {
        IAPCartListener mProductCountListener = new IAPCartListener() {
            @Override
            public void onSuccess(final int count) {
                assertEquals(5, count);
            }

            @Override
            public void onFailure(final Message msg) {
            }
        };

        mShoppingCartPresenter = new ShoppingCartPresenter();
        mShoppingCartPresenter.setHybrisDelegate(mHybrisDelegate);
        mShoppingCartPresenter.getProductCartCount(mContext, mProductCountListener);

        JSONObject obj = new JSONObject(TestUtils.readFile(ShoppingCartPresenterTest
                .class, "get_carts_response.txt"));
        mNetworkController.sendSuccess(obj);
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
    public void testDeleteCartSuccessResponse() throws JSONException {
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
        mShoppingCartPresenter.deleteCart(mContext, mProductCountListener);
        mNetworkController.sendSuccess(null);
    }

    @Test
    public void testDeleteCartFailureResponse() throws JSONException {
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
        mShoppingCartPresenter.deleteCart(mContext, mProductCountListener);
        mNetworkController.sendFailure(null);
    }

    @Test
    public void testCreateCartSuccessResponseIfBuy() throws JSONException {
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
        mAddressController = new AddressController(mContext, mShoppingCartPresenter);
        mShoppingCartPresenter.setHybrisDelegate(mHybrisDelegate);
        mShoppingCartPresenter.createCart(mContext, mProductCountListener, "HX8832/11", true);
        JSONObject obj = new JSONObject(TestUtils.readFile(ShoppingCartPresenterTest
                .class, "create_cart_response.txt"));
        mNetworkController.sendSuccess(obj);
    }

    @Test
    public void testCreateCartSuccessResponseIfNotBuy() throws JSONException {
        IAPCartListener mProductCountListener = new IAPCartListener() {
            @Override
            public void onSuccess(final int count) {
                assertEquals(0, count);
            }

            @Override
            public void onFailure(final Message msg) {
            }
        };

        mShoppingCartPresenter = new ShoppingCartPresenter();
        mAddressController = new AddressController(mContext, mShoppingCartPresenter);
        mShoppingCartPresenter.setHybrisDelegate(mHybrisDelegate);
        mShoppingCartPresenter.createCart(mContext, mProductCountListener, "HX8832/11", false);
        JSONObject obj = new JSONObject(TestUtils.readFile(ShoppingCartPresenterTest
                .class, "create_cart_response.txt"));
        mNetworkController.sendSuccess(obj);
    }

    @Test
    public void testCreateCartFailureResponse() throws JSONException {
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
        mShoppingCartPresenter.createCart(mContext, mProductCountListener, "HX8832/11", true);
        JSONObject obj = new JSONObject(TestUtils.readFile(ShoppingCartPresenterTest
                .class, "create_cart_response.txt"));
        mNetworkController.sendFailure(null);
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
    public void testAddToCartSuccessResponseWithIsBuyFalse() throws JSONException {
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
        mShoppingCartPresenter.addProductToCart(mContext, "Hx8331/11", mProductCountListener, false);

        JSONObject obj = new JSONObject(TestUtils.readFile(ShoppingCartPresenterTest
                .class, "add_product_to_cart_response.txt"));
        mNetworkController.sendSuccess(obj);
    }


    @Test
    public void testAddToCartSuccessResponseWithIsBuyTrue() throws JSONException {
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
        mShoppingCartPresenter.addProductToCart(mContext, "Hx8331/11", mProductCountListener, true);

        JSONObject obj = new JSONObject(TestUtils.readFile(ShoppingCartPresenterTest
                .class, "add_product_to_cart_response.txt"));
        mNetworkController.sendSuccess(obj);
    }


    @Test
    public void testAddToCartErrorResponse() throws JSONException {
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
        mShoppingCartPresenter.addProductToCart(mContext, "Hx8331/11", mProductCountListener, false);

        mNetworkController.sendFailure(new VolleyError());
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
        mShoppingCartPresenter.buyProduct(mContext, "HX8331/11", mProductCountListener);
        JSONObject obj = new JSONObject(TestUtils.readFile(ShoppingCartPresenterTest
                .class, "get_carts_response.txt"));
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
    public void testBuyProductWhenEmptyResponse() throws JSONException {
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
                .class, "EmptyResponse.txt"));
        mNetworkController.sendSuccess(obj);
    }

    @Test
    public void testBuyProductErrorResponse() throws JSONException {
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
        mNetworkController.sendFailure(new VolleyError());
    }


    @Test(expected = NullPointerException.class)
    public void testBuyProductWhenTotalItemnullOrZero() throws JSONException {

        mShoppingCartPresenter = new ShoppingCartPresenter();
        mShoppingCartPresenter.setHybrisDelegate(mHybrisDelegate);
        mShoppingCartPresenter.buyProduct(mContext, "HX9003/63", Mockito.mock(IAPCartListener.class));
        JSONObject obj = new JSONObject(TestUtils.readFile(ShoppingCartPresenterTest
                .class, "get_carts_response_entries_zero.txt"));
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
        mNetworkController.sendFailure(new VolleyError());
    }

    @Test
    public void testGetUserResponseWithIAPNetworkError() {
        mShoppingCartPresenter = new ShoppingCartPresenter(mContext, this);
        mShoppingCartPresenter.setHybrisDelegate(mHybrisDelegate);
        Message msg = new Message();
        msg.obj = new IAPNetworkError(null, 1, null);
        mShoppingCartPresenter.onGetUser(msg);
    }

    @Test
    public void testGetUserResponseSuccess() throws Exception {
        mShoppingCartPresenter = new ShoppingCartPresenter(mContext, this);
        mShoppingCartPresenter.setHybrisDelegate(mHybrisDelegate);
        JSONObject obj = new JSONObject(TestUtils.readFile(ShoppingCartPresenterTest
                .class, "GetUser.txt"));
        Message msg = Message.obtain();
        msg.obj = new Gson().fromJson(obj.toString(), GetUser.class);
        mShoppingCartPresenter.onGetUser(msg);
    }

    @Test
    public void testGetDeliveryModesResponseWithIAPNetworkError() {
        mShoppingCartPresenter = new ShoppingCartPresenter(mContext, this);
        mShoppingCartPresenter.setHybrisDelegate(mHybrisDelegate);
        Message msg = new Message();
        msg.obj = new IAPNetworkError(null, 1, null);
        mShoppingCartPresenter.onGetDeliveryModes(msg);
    }

    @Test
    public void testGetDeliveryModesResponse() throws Exception {
        mShoppingCartPresenter = new ShoppingCartPresenter(mContext, this);
        mShoppingCartPresenter.setHybrisDelegate(mHybrisDelegate);
        JSONObject obj = new JSONObject(TestUtils.readFile(ShoppingCartPresenterTest
                .class, "DeliveryModes.txt"));
        Message msg = Message.obtain();
        msg.obj = new Gson().fromJson(obj.toString(), GetDeliveryModes.class);
        mShoppingCartPresenter.onGetDeliveryModes(msg);
    }

    @Test
    public void testGetDeliveryModesResponseWithDeliveryModes() throws Exception {
        mShoppingCartPresenter = new ShoppingCartPresenter(mContext, this);
        mShoppingCartPresenter.setHybrisDelegate(mHybrisDelegate);
        JSONObject obj = new JSONObject(TestUtils.readFile(ShoppingCartPresenterTest
                .class, "DeliveryModesWithModes.txt"));
        Message msg = Message.obtain();
        msg.obj = new Gson().fromJson(obj.toString(), GetDeliveryModes.class);
        mShoppingCartPresenter.onGetDeliveryModes(msg);

    }

    @Test
    public void testSetDeliveryModesResponseWithIAPNetworkError() {
        mShoppingCartPresenter = new ShoppingCartPresenter(mContext, this);
        mShoppingCartPresenter.setHybrisDelegate(mHybrisDelegate);
        Message msg = new Message();
        msg.obj = new IAPNetworkError(null, 1, null);
        mShoppingCartPresenter.onSetDeliveryMode(msg);
    }

    @Test
    public void testSetDeliveryAddressResponseWithIAPNetworkError() {
        mShoppingCartPresenter = new ShoppingCartPresenter(mContext, this);
        mShoppingCartPresenter.setHybrisDelegate(mHybrisDelegate);
        Message msg = new Message();
        msg.obj = new IAPNetworkError(null, 1, null);
        mShoppingCartPresenter.onSetDeliveryAddress(msg);
    }

    @Test
    public void verifyNullHybrisDelegate() {
        mShoppingCartPresenter = new ShoppingCartPresenter(mContext, this);
        mShoppingCartPresenter.setHybrisDelegate(null);
        mShoppingCartPresenter.getHybrisDelegate();
    }


    @Test(expected = NullPointerException.class)
    public void verfyMerge() throws Exception {
        mShoppingCartPresenter = new ShoppingCartPresenter(mContext, this);
        CartsEntity cartsEntity = Mockito.mock(CartsEntity.class);
        final ArrayList<EntriesEntity> value = new ArrayList<>();
        EntriesEntity entity = Mockito.mock(EntriesEntity.class);
        Mockito.when(entity.getBasePrice()).thenReturn(new BasePriceEntity());
        Mockito.when(entity.getEntryNumber()).thenReturn(1);
        Mockito.when(entity.getProduct()).thenReturn(new ProductEntity());
        Mockito.when(entity.getQuantity()).thenReturn(2);
        Mockito.when(entity.getTotalPrice()).thenReturn(new TotalPriceEntity());
        value.add(entity);
        Mockito.when(cartsEntity.getEntries()).thenReturn(value);
        Message msg = new Message();
        msg.obj = new HashMap<>();
        mShoppingCartPresenter.onModelDataLoadFinished(msg);
    }

    /*@Test
    public void verfyGetShoppingCartDatas() throws Exception {
        mShoppingCartPresenter = new ShoppingCartPresenter(mContext, this);
        CartsEntity cartsEntity = Mockito.mock(CartsEntity.class);
        final ArrayList<EntriesEntity> value = new ArrayList<>();
        EntriesEntity entity = Mockito.mock(EntriesEntity.class);
        Mockito.when(entity.getBasePrice()).thenReturn(new BasePriceEntity());
        Mockito.when(entity.getEntryNumber()).thenReturn(1);

        final ProductEntity productEntity = Mockito.mock(ProductEntity.class);
        Mockito.when(productEntity.getCode()).thenReturn("HX8332/11");
        final ArrayList<CategoriesEntity> categoriesEntities = new ArrayList<>();
        CategoriesEntity categoriesEntity = Mockito.mock(CategoriesEntity.class);
        Mockito.when(categoriesEntity.getCode()).thenReturn("HX8332/11");
        categoriesEntities.add(categoriesEntity);
        Mockito.when(productEntity.getCategories()).thenReturn(categoriesEntities);

        Mockito.when(entity.getProduct()).thenReturn(productEntity);
        Mockito.when(entity.getQuantity()).thenReturn(2);
        Mockito.when(entity.getTotalPrice()).thenReturn(new TotalPriceEntity());
        value.add(entity);


        Mockito.when(cartsEntity.getEntries()).thenReturn(value);
        final TotalPriceWithTaxEntity totalPriceWithTaxEntity = Mockito.mock(TotalPriceWithTaxEntity.class);
        Mockito.when(totalPriceWithTaxEntity.getFormattedValue()).thenReturn("12");
        Mockito.when(cartsEntity.getTotalPriceWithTax()).thenReturn(totalPriceWithTaxEntity);
        Mockito.when(cartsEntity.getTotalItems()).thenReturn(2);
        Mockito.when(cartsEntity.getDeliveryAddress()).thenReturn(new DeliveryAddressEntity());
        final TotalTaxEntity totalTaxEntity = Mockito.mock(TotalTaxEntity.class);
        Mockito.when(totalTaxEntity.getFormattedValue()).thenReturn("3");
        Mockito.when(cartsEntity.getTotalTax()).thenReturn(totalTaxEntity);


        CartModelContainer.getInstance().addProductAsset("HX8332/11", new ArrayList<String>());
        final SummaryModel summaryModel = Mockito.mock(SummaryModel.class);
        final Data data = new Data();
        data.setMarketingTextHeader("Something");
        data.setProductTitle("product title");
        data.setImageURL("http://imageUrl/");
        Mockito.when(summaryModel.getData()).thenReturn(data);
//        Mockito.when(summaryModel.getData().getImageURL()).thenReturn(data.g);
//        Mockito.when(summaryModel.getData().getProductTitle()).thenReturn("Brush");
//        Mockito.when(summaryModel.getData().getMarketingTextHeader()).thenReturn("http://image/");
        CartModelContainer.getInstance().addProductSummary("HX8332/11", summaryModel);
     //   mShoppingCartPresenter.getShoppingCartDatas(cartsEntity, value);
    }*/

    @Override
    public void onLoadFinished(ArrayList<?> data) {
        if (data.size() > 0 && data.get(0) != null) {
            assertEquals(mCTNS.size(), data.size());
        } else {
            assertFalse(false);
        }
    }

    @Override
    public void onLoadError(final Message msg) {
        assertFalse(msg.obj instanceof Message);
    }

    @Override
    public void onRetailerError(final IAPNetworkError errorMsg) {
        assertEquals(errorMsg.getMessage(), mContext.getString(R.string.iap_no_retailer_message));
    }

    @Override
    public void onGetRegions(Message msg) {

    }

    @Override
    public void onGetUser(Message msg) {

    }

    @Override
    public void onCreateAddress(Message msg) {

    }

    @Override
    public void onGetAddress(Message msg) {

    }

    @Override
    public void onSetDeliveryAddress(Message msg) {

    }

    @Override
    public void onGetDeliveryModes(Message msg) {

    }

    @Override
    public void onSetDeliveryMode(Message msg) {

    }

    @Mock
    CartsEntity cartsEntityMock;

    @Mock
    List<AppliedOrderPromotionEntity> appliedOrderPromotionEntityListMock;

    @Mock
    AppliedOrderPromotionEntity appliedOrderPromotionEntityMock;

    @Mock
    DeliveryModeEntity deliveryModeMock;

    @Mock
    PromotionEntity promotionEntityMock;

    @Mock
    DeliveryCostEntity deliveryCostEntityMock;

    @Test
    public void setFreeDeliVeryIfPromotionCodeIsUSFreeShipping() throws Exception {
        Mockito.when(promotionEntityMock.getCode()).thenReturn("US-freeshipping");
        Mockito.when(appliedOrderPromotionEntityMock.getPromotion()).thenReturn(promotionEntityMock);
        Mockito.when(appliedOrderPromotionEntityListMock.size()).thenReturn(1);
        Mockito.when(appliedOrderPromotionEntityListMock.get(0)).thenReturn(appliedOrderPromotionEntityMock);
        Mockito.when(cartsEntityMock.getAppliedOrderPromotions()).thenReturn(appliedOrderPromotionEntityListMock);
        //cartsEntity.getDeliveryMode().getDeliveryCost().setFormattedValue(newDeliveryCost);
        Mockito.when(deliveryCostEntityMock.getFormattedValue()).thenReturn("$ 4.4");
        Mockito.when(deliveryModeMock.getDeliveryCost()).thenReturn(deliveryCostEntityMock);
        Mockito.when(cartsEntityMock.getDeliveryMode()).thenReturn(deliveryModeMock);
        mShoppingCartPresenter.applyPromotion(cartsEntityMock);

       // Mockito.verify(deliveryCostEntityMock).setFormattedValue("$ 0.0");
    }

    @Test
    public void setFreeDeliVeryIfPromotionCodeIsfreeshippingUS() throws Exception {
        Mockito.when(promotionEntityMock.getCode()).thenReturn("freeshipping_us");
        Mockito.when(appliedOrderPromotionEntityMock.getPromotion()).thenReturn(promotionEntityMock);
        Mockito.when(appliedOrderPromotionEntityListMock.size()).thenReturn(1);
        Mockito.when(appliedOrderPromotionEntityListMock.get(0)).thenReturn(appliedOrderPromotionEntityMock);
        Mockito.when(cartsEntityMock.getAppliedOrderPromotions()).thenReturn(appliedOrderPromotionEntityListMock);
        //cartsEntity.getDeliveryMode().getDeliveryCost().setFormattedValue(newDeliveryCost);
        Mockito.when(deliveryCostEntityMock.getFormattedValue()).thenReturn("$ 4.4");
        Mockito.when(deliveryModeMock.getDeliveryCost()).thenReturn(deliveryCostEntityMock);
        Mockito.when(cartsEntityMock.getDeliveryMode()).thenReturn(deliveryModeMock);
        mShoppingCartPresenter.applyPromotion(cartsEntityMock);

       // Mockito.verify(deliveryCostEntityMock).setFormattedValue("$ 0.0");
    }


    //    @Test
//    public void testGetRetailersInformationWithWebResultNull() throws JSONException {
//        mShoppingCartPresenter = new ShoppingCartPresenter(mContext, this);
//        mShoppingCartPresenter.setHybrisDelegate(mHybrisDelegate);
//        mShoppingCartPresenter.getRetailersInformation("HX8071/10");
//
//    }


    @Test
    public void isValidPromotion() throws Exception {
        String[] expectedCodes = {"US-freeshipping","freeshipping_us","freeShiPping"};
        for(String code:expectedCodes) {
            boolean isValid = mShoppingCartPresenter.isValidPromotion(code);
            Assert.assertEquals(true, isValid);
        }
        String[] invalidCodes = {"xyzabcd","invalid-us-ship-code"};
        for(String code:invalidCodes) {
            boolean isValid = mShoppingCartPresenter.isValidPromotion(code);
            Assert.assertEquals(false, isValid);
        }
    }
}
