package com.philips.cdp.di.iap.orderHistory;


import android.content.Context;
import android.os.Message;

import com.philips.cdp.di.iap.TestUtils;
import com.philips.cdp.di.iap.controller.OrderController;
import com.philips.cdp.di.iap.response.orders.OrderDetail;
import com.philips.cdp.di.iap.session.HybrisDelegate;
import com.philips.cdp.di.iap.session.MockNetworkController;
import com.philips.cdp.di.iap.session.RequestCode;

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
import static junit.framework.Assert.assertNotNull;
@RunWith(RobolectricTestRunner.class)
public class OrderControllerTest {

    private MockNetworkController mNetworkController;
    private OrderController mController;
    private HybrisDelegate mHybrisDelegate;
    @Mock
    private Context mContext;
    @Mock
    private OrderDetail detail;

    @Before
    public void setUP() {
        MockitoAnnotations.initMocks(this);
        mNetworkController = new MockNetworkController(mContext);
        mHybrisDelegate = TestUtils.getStubbedHybrisDelegate();
        mNetworkController = (MockNetworkController) mHybrisDelegate.getNetworkController(null);
    //    detail = MockOrderDetail(); - Mocking is required.
    //    mNetworkController = (MockNetworkController) mHybrisDelegate.getNetworkController(null);
    }

    @Test
    public void testonGetOrderList() throws JSONException {
        mController = new OrderController(mContext, new MockOrderListener() {
            @Override
            public void onGetOrderList(final Message msg) {
                assertEquals(RequestCode.GET_ORDERS, msg.what);
            }
        });

        setStoreAndDelegate();
        mController.getOrderList(0);
        JSONObject obj = new JSONObject(TestUtils.readFile(OrderControllerTest
                .class, "orders.txt"));
        mNetworkController.sendSuccess(obj);
    }

    @Test
    public void testonGetOrderDetails() throws JSONException {
        mController = new OrderController(mContext, new MockOrderListener() {
            @Override
            public void onGetOrderDetail(final Message msg) {
                assertEquals(RequestCode.GET_ORDER_DETAIL, msg.what);
            }
        });

        setStoreAndDelegate();
        mController.getOrderDetails("");
        JSONObject obj = new JSONObject(TestUtils.readFile(OrderControllerTest
                .class, "order_detail.txt"));
        mNetworkController.sendSuccess(obj);
    }

    //todo - uncomment after it works mController is getting null
   /* @Test
    public void checkProductDataNotNull() {
        ArrayList<OrderDetail> orderDetailList = new ArrayList<>();
        orderDetailList.add(detail);
        assertNotNull(mController.getProductData(orderDetailList));

    }*/

    public void setStoreAndDelegate() {
        mController.setHybrisDelegate(mHybrisDelegate);
        mController.setStore(TestUtils.getStubbedStore());
    }

}