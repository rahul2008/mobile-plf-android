/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
package com.philips.cdp.di.iap.orderHistory;

import android.content.Context;
import android.os.Message;

import com.android.volley.VolleyError;
import com.philips.cdp.di.iap.TestUtils;
import com.philips.cdp.di.iap.container.CartModelContainer;
import com.philips.cdp.di.iap.controller.OrderController;
import com.philips.cdp.di.iap.model.AbstractModel;
import com.philips.cdp.di.iap.prx.MockPRXDataBuilder;
import com.philips.cdp.di.iap.response.orders.OrderDetail;
import com.philips.cdp.di.iap.response.orders.OrdersData;
import com.philips.cdp.di.iap.session.HybrisDelegate;
import com.philips.cdp.di.iap.session.IAPNetworkError;
import com.philips.cdp.di.iap.session.MockNetworkController;
import com.philips.cdp.di.iap.session.NetworkConstants;
import com.philips.cdp.di.iap.session.RequestCode;
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

import java.util.ArrayList;
import java.util.HashMap;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertTrue;

@RunWith(RobolectricTestRunner.class)
public class OrderControllerTest {

    private MockNetworkController mNetworkController;
    private HybrisDelegate mHybrisDelegate;
    private MockPRXDataBuilder mMockPRXDataBuilder;

    @Mock
    private OrderController mController;
    @Mock
    private Context mContext;
    ArrayList<String> ctns = new ArrayList<>();

    @Before
    public void setUP() {
        MockitoAnnotations.initMocks(this);
        mHybrisDelegate = TestUtils.getStubbedHybrisDelegate();
        mNetworkController = (MockNetworkController) mHybrisDelegate.getNetworkController(null);
        ctns.add("HX9033/64");
        ctns.add("HX9023/64");
        ctns.add("HX9003/64");
        mMockPRXDataBuilder = new MockPRXDataBuilder(mContext, ctns, mController);
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
                final ArrayList<OrderDetail> detail = new ArrayList<>();
                detail.add((OrderDetail)msg.obj);
                mController.makePrxCall(detail, new AbstractModel.DataLoadListener() {
                    @Override
                    public void onModelDataLoadFinished(Message msg) {
                        assertTrue(msg.obj instanceof HashMap);
                    }

                    @Override
                    public void onModelDataError(Message msg) {

                    }
                });
                try {
                    makePRXData();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                assertNotNull(mController.getProductData(detail));
            }
        });

        setStoreAndDelegate();
        mController.getOrderDetails("14000098999");
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

    public void setStoreAndDelegate() {
        mController.setHybrisDelegate(mHybrisDelegate);
        mController.setStore(TestUtils.getStubbedStore());
    }
}