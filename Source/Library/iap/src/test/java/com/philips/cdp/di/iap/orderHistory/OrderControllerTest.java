/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
package com.philips.cdp.di.iap.orderHistory;

import android.content.Context;
import android.os.Message;

import com.android.volley.VolleyError;
import com.philips.cdp.di.iap.TestUtils;
import com.philips.cdp.di.iap.controller.OrderController;
import com.philips.cdp.di.iap.response.orders.OrderDetail;
import com.philips.cdp.di.iap.response.orders.OrdersData;
import com.philips.cdp.di.iap.session.HybrisDelegate;
import com.philips.cdp.di.iap.session.IAPNetworkError;
import com.philips.cdp.di.iap.session.MockNetworkController;
import com.philips.cdp.di.iap.session.NetworkConstants;
import com.philips.cdp.di.iap.session.RequestCode;

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

@RunWith(RobolectricTestRunner.class)
public class OrderControllerTest {

    private MockNetworkController mNetworkController;
    private HybrisDelegate mHybrisDelegate;

    @Mock
    private OrderController mController;
    @Mock
    private Context mContext;

    @Before
    public void setUP() {
        MockitoAnnotations.initMocks(this);
        mHybrisDelegate = TestUtils.getStubbedHybrisDelegate();
        mNetworkController = (MockNetworkController) mHybrisDelegate.getNetworkController(null);
    }

    @Test
    public void orderHistorySuccessResponseWithData() throws JSONException {
        mController = new OrderController(mContext, new MockOrderListener() {
            @Override
            public void onGetOrderDetail(final Message msg) {
                assertEquals(RequestCode.GET_ORDERS, msg.what);
                assertTrue(msg.obj instanceof OrdersData);
            }
        });

        setStoreAndDelegate();
        mController.getOrderList(0);
        JSONObject obj = new JSONObject(TestUtils.readFile(OrderControllerTest
                .class, "orders.txt"));
        mNetworkController.sendSuccess(obj);
    }

    @Test
    public void orderDetailSuccessResponseWithData() throws JSONException {
        mController = new OrderController(mContext, new MockOrderListener() {
            @Override
            public void onGetOrderDetail(final Message msg) {
                assertEquals(RequestCode.GET_ORDER_DETAIL, msg.what);
                assertTrue(msg.obj instanceof OrderDetail);
            }
        });

        setStoreAndDelegate();
        mController.getOrderDetails("");
        JSONObject obj = new JSONObject(TestUtils.readFile(OrderControllerTest
                .class, "order_detail.txt"));
        mNetworkController.sendSuccess(obj);
    }

    @Test
    public void orderHistoryErrorResponse() {
        mController = new OrderController(mContext, new MockOrderListener() {
            @Override
            public void onGetOrderList(final Message msg) {
                assertEquals(RequestCode.GET_ORDERS, msg.what);
                assertTrue(msg.obj instanceof IAPNetworkError);
            }
        });

        setStoreAndDelegate();
        mController.getOrderList(0);
        mNetworkController.sendFailure(new VolleyError());
    }

    @Test
    public void orderDetailErrorResponse() {
        mController = new OrderController(mContext, new MockOrderListener() {
            @Override
            public void onGetOrderDetail(final Message msg) {
                assertEquals(RequestCode.GET_ORDER_DETAIL, msg.what);
                assertTrue(msg.obj instanceof IAPNetworkError);
            }
        });

        setStoreAndDelegate();
        mController.getOrderDetails("");
        mNetworkController.sendFailure(new VolleyError());
    }

    @Test
    public void orderListEmptySuccessResponse() throws JSONException {
        mController = new OrderController(mContext, new MockOrderListener() {
            @Override
            public void onGetOrderList(final Message msg) {
                assertEquals(RequestCode.GET_ORDERS, msg.what);
                assertEquals(NetworkConstants.EMPTY_RESPONSE, msg.obj);
            }
        });

        setStoreAndDelegate();
        mController.getOrderList(0);
        JSONObject obj = new JSONObject(TestUtils.readFile(OrderControllerTest
                .class, "EmptyResponse.txt"));
        mNetworkController.sendSuccess(obj);
    }

    public void setStoreAndDelegate() {
        mController.setHybrisDelegate(mHybrisDelegate);
        mController.setStore(TestUtils.getStubbedStore());
    }
}