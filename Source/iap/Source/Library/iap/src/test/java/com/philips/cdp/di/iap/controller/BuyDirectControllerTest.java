/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
package com.philips.cdp.di.iap.controller;

import android.content.Context;
import android.os.Message;

import com.android.volley.VolleyError;
import com.philips.cdp.di.iap.TestUtils;
import com.philips.cdp.di.iap.response.State.RegionsList;
import com.philips.cdp.di.iap.response.addresses.GetDeliveryModes;
import com.philips.cdp.di.iap.response.addresses.GetUser;
import com.philips.cdp.di.iap.response.payment.PaymentMethods;
import com.philips.cdp.di.iap.session.HybrisDelegate;
import com.philips.cdp.di.iap.session.IAPNetworkError;
import com.philips.cdp.di.iap.session.MockNetworkController;
import com.philips.cdp.di.iap.session.NetworkConstants;
import com.philips.cdp.di.iap.session.RequestCode;
import com.philips.cdp.di.iap.utils.IAPConstant;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.robolectric.RobolectricTestRunner;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotSame;
import static junit.framework.Assert.assertTrue;

@RunWith(RobolectricTestRunner.class)
public class BuyDirectControllerTest {
    private MockNetworkController mNetworkController;
    private HybrisDelegate mHybrisDelegate;

    @Mock
    private BuyDirectController mBuyDirectController;
    @Mock
    private Context mContext;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        mHybrisDelegate = TestUtils.getStubbedHybrisDelegate();
        mNetworkController = (MockNetworkController) mHybrisDelegate.getNetworkController(mContext);
    }

    @Test
    public void testNullStoreAndDelegate() throws JSONException {
        mBuyDirectController = new BuyDirectController(mContext, new MockBuyDirectListener() {
            @Override
            public void onGetRegions(Message msg) {
                assertEquals(RequestCode.GET_REGIONS, msg.what);
                assertTrue(msg.obj instanceof RegionsList);
            }
        });

        setStoreAndDelegate();
        mBuyDirectController.setHybrisDelegate(null);
        mBuyDirectController.setStore(null);
        mBuyDirectController.getRegions();
        JSONObject obj = new JSONObject(TestUtils.readFile(BuyDirectControllerTest.class, "Region.txt"));
        mNetworkController.sendSuccess(obj);
    }

    @Test
    public void testCreateCartSuccessResponse() throws JSONException {
        mBuyDirectController = new BuyDirectController(mContext, new MockBuyDirectListener() {
            @Override
            public void onCreateCart(Message msg) {
                assertEquals(RequestCode.CREATE_CART, msg.what);
            }
        });

        setStoreAndDelegate();
        mBuyDirectController.createCart();
        JSONObject obj = new JSONObject(TestUtils.readFile(BuyDirectControllerTest.class, "EmptyResponse.txt"));
        mNetworkController.sendSuccess(obj);
    }

    @Test
    public void testCreateCartErrorResponse() throws JSONException {
        mBuyDirectController = new BuyDirectController(mContext, new MockBuyDirectListener() {
            @Override
            public void onCreateCart(Message msg) {
                assertEquals(RequestCode.CREATE_CART, msg.what);
                assertNotSame(RequestCode.GET_CART, msg.what);
                assertNotSame(RequestCode.GET_ADDRESS, msg.what);
                assertNotSame(RequestCode.CREATE_ADDRESS, msg.what);
                assertNotSame(RequestCode.UPDATE_ADDRESS, msg.what);
                assertNotSame(RequestCode.DELETE_ADDRESS, msg.what);
                assertNotSame(RequestCode.DELETE_CART, msg.what);
                assertNotSame(RequestCode.SEARCH_PRODUCT, msg.what);
            }
        });

        setStoreAndDelegate();
        mBuyDirectController.createCart();
        mNetworkController.sendFailure(new VolleyError());
    }

    @Test
    public void testDeleteCartSuccessResponse() throws JSONException {
        mBuyDirectController = new BuyDirectController(mContext, new MockBuyDirectListener() {
            @Override
            public void onDeleteCart(Message msg) {
                assertEquals(RequestCode.DELETE_CART, msg.what);
            }
        });

        setStoreAndDelegate();
        mBuyDirectController.deleteCart();
        JSONObject obj = new JSONObject(TestUtils.readFile(BuyDirectControllerTest.class, "EmptyResponse.txt"));
        mNetworkController.sendSuccess(obj);
    }

    @Test
    public void testDeleteCartErrorResponse() throws JSONException {
        mBuyDirectController = new BuyDirectController(mContext, new MockBuyDirectListener() {
            @Override
            public void onDeleteCart(Message msg) {
                assertEquals(RequestCode.DELETE_CART, msg.what);
            }
        });

        setStoreAndDelegate();
        mBuyDirectController.deleteCart();
        mNetworkController.sendFailure(new VolleyError());
    }

    @Test
    public void testAddToCartSuccessResponse() throws JSONException {
        mBuyDirectController = new BuyDirectController(mContext, new MockBuyDirectListener() {
            @Override
            public void onAddToCart(Message msg) {
                assertEquals(RequestCode.ADD_TO_CART, msg.what);
            }
        });

        setStoreAndDelegate();
        mBuyDirectController.addToCart("HX8071/10");
        JSONObject obj = new JSONObject(TestUtils.readFile(BuyDirectControllerTest.class, "AddToCartResponse.txt"));
        mNetworkController.sendSuccess(obj);
    }

    @Test
    public void testAddToCartErrorResponse() throws JSONException {
        mBuyDirectController = new BuyDirectController(mContext, new MockBuyDirectListener() {
            @Override
            public void onAddToCart(Message msg) {
                assertEquals(RequestCode.ADD_TO_CART, msg.what);
            }
        });

        setStoreAndDelegate();
        mBuyDirectController.addToCart("HX8071/10");
        mNetworkController.sendFailure(new VolleyError());
    }

    @Test
    public void testGetRegionsSuccessResponseWithData() throws JSONException {
        mBuyDirectController = new BuyDirectController(mContext, new MockBuyDirectListener() {
            @Override
            public void onGetRegions(Message msg) {
                assertEquals(RequestCode.GET_REGIONS, msg.what);
                assertTrue(msg.obj instanceof RegionsList);
            }
        });

        setStoreAndDelegate();
        mBuyDirectController.getRegions();
        JSONObject obj = new JSONObject(TestUtils.readFile(BuyDirectControllerTest.class, "Region.txt"));
        mNetworkController.sendSuccess(obj);
    }

    @Test
    public void testGetRegionsEmptySuccessResponse() throws JSONException {
        mBuyDirectController = new BuyDirectController(mContext, new MockBuyDirectListener() {
            @Override
            public void onGetRegions(Message msg) {
                testEmptyResponse(msg, RequestCode.GET_REGIONS);
            }
        });

        setStoreAndDelegate();
        mBuyDirectController.getRegions();
        JSONObject obj = new JSONObject(TestUtils.readFile(BuyDirectControllerTest.class, "EmptyResponse.txt"));
        mNetworkController.sendSuccess(obj);
    }

    @Test
    public void testGetRegionsErrorResponse() {
        mBuyDirectController = new BuyDirectController(mContext, new MockBuyDirectListener() {
            @Override
            public void onGetRegions(Message msg) {
                testErrorResponse(msg, RequestCode.GET_REGIONS);
            }
        });

        setStoreAndDelegate();
        mBuyDirectController.getRegions();
        mNetworkController.sendFailure(new VolleyError());
    }

    @Test
    public void testGetUserSuccessResponseWithData() throws JSONException {
        mBuyDirectController = new BuyDirectController(mContext, new MockBuyDirectListener() {
            @Override
            public void onGetUser(Message msg) {
                assertEquals(RequestCode.GET_USER, msg.what);
                assertTrue(msg.obj instanceof GetUser);
            }
        });

        setStoreAndDelegate();
        mBuyDirectController.getUser();
        JSONObject obj = new JSONObject(TestUtils.readFile(BuyDirectControllerTest.class, "GetUser.txt"));
        mNetworkController.sendSuccess(obj);
    }

    @Test
    public void testGetUserEmptySuccessResponse() throws JSONException {
        mBuyDirectController = new BuyDirectController(mContext, new MockBuyDirectListener() {
            @Override
            public void onGetUser(Message msg) {
                testEmptyResponse(msg, RequestCode.GET_USER);
            }
        });

        setStoreAndDelegate();
        mBuyDirectController.getUser();
        JSONObject obj = new JSONObject(TestUtils.readFile(BuyDirectControllerTest.class, "EmptyResponse.txt"));
        mNetworkController.sendSuccess(obj);
    }

    @Test
    public void testGetUserErrorResponse() {
        mBuyDirectController = new BuyDirectController(mContext, new MockBuyDirectListener() {
            @Override
            public void onGetUser(Message msg) {
                testErrorResponse(msg, RequestCode.GET_USER);
            }
        });

        setStoreAndDelegate();
        mBuyDirectController.getUser();
        mNetworkController.sendFailure(new VolleyError());
    }

    @Test
    public void testSetDeliveryAddressSuccessResponse() throws Exception {
        mBuyDirectController = new BuyDirectController(mContext, new MockBuyDirectListener() {
            @Override
            public void onSetDeliveryAddress(Message msg) {
                testEmptyResponse(msg, RequestCode.SET_DELIVERY_ADDRESS);
            }
        });

        setStoreAndDelegate();
        mBuyDirectController.setDeliveryAddress("8799470125079");
        JSONObject obj = new JSONObject(TestUtils.readFile(BuyDirectControllerTest.class, "EmptyResponse.txt"));
        mNetworkController.sendSuccess(obj);
    }

    @Test
    public void testSetDeliveryAddressWithNull() throws Exception {
        mBuyDirectController = new BuyDirectController(mContext, new MockBuyDirectListener() {
            @Override
            public void onSetDeliveryAddress(Message msg) {
                testErrorResponse(msg, RequestCode.SET_DELIVERY_ADDRESS);
            }
        });

        setStoreAndDelegate();
        mBuyDirectController.setDeliveryAddress(null);
        mNetworkController.sendFailure(new VolleyError());
    }

    @Test
    public void testSetDeliveryAddressErrorResponse() throws Exception {
        mBuyDirectController = new BuyDirectController(mContext, new MockBuyDirectListener() {
            @Override
            public void onSetDeliveryAddress(Message msg) {
                testErrorResponse(msg, RequestCode.SET_DELIVERY_ADDRESS);
            }
        });

        setStoreAndDelegate();
        mBuyDirectController.setDeliveryAddress("8799470125079");
        mNetworkController.sendFailure(new VolleyError());
    }

    @Test
    public void testSetDeliveryModeSuccessResponse() throws Exception {
        mBuyDirectController = new BuyDirectController(mContext, new MockBuyDirectListener() {
            @Override
            public void onSetDeliveryMode(final Message msg) {
                testSuccessResponse(msg, RequestCode.SET_DELIVERY_MODE);
            }
        });

        setStoreAndDelegate();
        mBuyDirectController.setDeliveryMode("");
        mNetworkController.sendSuccess(null);
    }

    @Test
    public void testSetDeliveryModeErrorResponse() throws Exception {
        mBuyDirectController = new BuyDirectController(mContext, new MockBuyDirectListener() {
            @Override
            public void onSetDeliveryMode(Message msg) {
                testErrorResponse(msg, RequestCode.SET_DELIVERY_MODE);
            }
        });

        setStoreAndDelegate();
        mBuyDirectController.setDeliveryMode("");
        mNetworkController.sendFailure(new VolleyError());
    }

    @Test
    public void testGetDeliveryModesSuccessResponse() throws Exception {
        mBuyDirectController = new BuyDirectController(mContext, new MockBuyDirectListener() {
            @Override
            public void onGetDeliveryMode(final Message msg) {
                assertEquals(RequestCode.GET_DELIVERY_MODE, msg.what);
                assertTrue(msg.obj instanceof GetDeliveryModes);
            }
        });

        setStoreAndDelegate();
        mBuyDirectController.getDeliveryModes();
        JSONObject obj = new JSONObject(TestUtils.readFile(BuyDirectControllerTest.class, "DeliveryModes.txt"));
        mNetworkController.sendSuccess(obj);
    }

    @Test
    public void testGetDeliveryModesErrorResponse() throws Exception {
        mBuyDirectController = new BuyDirectController(mContext, new MockBuyDirectListener() {
            @Override
            public void onGetDeliveryMode(final Message msg) {
                testErrorResponse(msg, RequestCode.GET_DELIVERY_MODE);
            }
        });

        setStoreAndDelegate();
        mBuyDirectController.getDeliveryModes();
        mNetworkController.sendFailure(new VolleyError());
    }


    @Test
    public void testGetPaymentDetailsSuccessResponse() throws JSONException {
        mBuyDirectController = new BuyDirectController(mContext, new MockBuyDirectListener() {
            @Override
            public void onGetPaymentMode(Message msg) {
                assertEquals(RequestCode.GET_PAYMENT_DETAILS, msg.what);
                assertTrue(msg.obj instanceof PaymentMethods);
            }
        });

        setStoreAndDelegate();
        mBuyDirectController.getPaymentMode();
        JSONObject obj = new JSONObject(TestUtils.readFile(PaymentControllerTest.class, "Payment.txt"));
        mNetworkController.sendSuccess(obj);
    }

    @Test
    public void testGetPaymentDetailsEmptySuccessResponse() throws JSONException {
        mBuyDirectController = new BuyDirectController(mContext, new MockBuyDirectListener() {
            @Override
            public void onGetPaymentMode(Message msg) {
                testEmptyResponse(msg, RequestCode.GET_PAYMENT_DETAILS);
            }
        });

        setStoreAndDelegate();
        mBuyDirectController.getPaymentMode();
        JSONObject obj = new JSONObject(TestUtils.readFile(PaymentControllerTest.class, "EmptyResponse.txt"));
        mNetworkController.sendSuccess(obj);
    }

    @Test
    public void testGetPaymentDetailsErrorResponse() throws JSONException {
        mBuyDirectController = new BuyDirectController(mContext, new MockBuyDirectListener() {
            @Override
            public void onGetPaymentMode(Message msg) {
                testErrorResponse(msg, RequestCode.GET_PAYMENT_DETAILS);
            }
        });

        setStoreAndDelegate();
        mBuyDirectController.getPaymentMode();
        mNetworkController.sendFailure(new VolleyError());
    }

    @Test
    public void testSetPaymentDetailsSuccessResponse() throws JSONException {
        mBuyDirectController = new BuyDirectController(mContext, new MockBuyDirectListener() {
            @Override
            public void onSetPaymentMode(Message msg) {
                testSuccessResponse(msg, RequestCode.SET_PAYMENT_DETAILS);
            }
        });

        setStoreAndDelegate();
        mBuyDirectController.setPaymentMode("");
        mNetworkController.sendSuccess(null);
    }

    @Test
    public void testSetPaymentDetailsErrorResponse() throws JSONException {
        mBuyDirectController = new BuyDirectController(mContext, new MockBuyDirectListener() {
            @Override
            public void onSetPaymentMode(Message msg) {
                testErrorResponse(msg, RequestCode.SET_PAYMENT_DETAILS);
            }
        });

        setStoreAndDelegate();
        mBuyDirectController.setPaymentMode("");
        mNetworkController.sendFailure(new VolleyError());
    }


    public void testErrorResponse(Message msg, int requestCode) {
        assertEquals(requestCode, msg.what);
        assertTrue(msg.obj instanceof IAPNetworkError);
    }

    public void testEmptyResponse(Message msg, int requestCode) {
        assertEquals(requestCode, msg.what);
        assertEquals(NetworkConstants.EMPTY_RESPONSE, msg.obj);
    }

    public void testSuccessResponse(Message msg, int requestCode) {
        assertEquals(requestCode, msg.what);
        assertEquals(IAPConstant.IAP_SUCCESS, msg.obj);
    }

    public void setStoreAndDelegate() {
        mBuyDirectController.setHybrisDelegate(mHybrisDelegate);
        mBuyDirectController.setStore(TestUtils.getStubbedStore());
    }
}