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
import com.philips.cdp.di.iap.response.orders.Consignment;
import com.philips.cdp.di.iap.response.orders.ConsignmentEntries;
import com.philips.cdp.di.iap.prx.MockPRXSummaryListExecutor;
import com.philips.cdp.di.iap.response.orders.ContactsResponse;
import com.philips.cdp.di.iap.response.orders.Cost;
import com.philips.cdp.di.iap.response.orders.Entries;
import com.philips.cdp.di.iap.response.orders.OrderDetail;
import com.philips.cdp.di.iap.response.orders.OrderEntry;
import com.philips.cdp.di.iap.response.orders.OrdersData;
import com.philips.cdp.di.iap.response.orders.Product;
import com.philips.cdp.di.iap.response.orders.ProductData;
import com.philips.cdp.di.iap.session.HybrisDelegate;
import com.philips.cdp.di.iap.session.IAPNetworkError;
import com.philips.cdp.di.iap.session.MockNetworkController;
import com.philips.cdp.di.iap.session.NetworkConstants;
import com.philips.cdp.di.iap.session.RequestCode;
import com.philips.cdp.di.iap.store.NetworkURLConstants;
import com.philips.cdp.di.iap.utils.IAPLog;
import com.philips.cdp.prxclient.datamodels.summary.Data;
import com.philips.cdp.prxclient.datamodels.summary.SummaryModel;
import com.philips.cdp.prxclient.request.ProductSummaryListRequest;
import com.philips.cdp.prxclient.request.ProductSummaryRequest;
import com.philips.cdp.prxclient.request.PrxRequest;
import com.philips.cdp.prxclient.response.ResponseData;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.robolectric.RobolectricTestRunner;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertTrue;

@RunWith(RobolectricTestRunner.class)
public class OrderControllerTest {

    private MockNetworkController mNetworkController;
    private HybrisDelegate mHybrisDelegate;
    private MockPRXSummaryListExecutor mMockPRXDataBuilder;
    @Mock
    ProductData productData;
    @Mock
    Entries entries;
    @Mock
    OrderDetail orderDetail;
    @Mock
    private OrderController mOrderController;
    @Mock
    private Context mContext;
    ArrayList<String> productCatalogNumber = new ArrayList<>();

    @Before
    public void setUP() {
        MockitoAnnotations.initMocks(this);
        mHybrisDelegate = TestUtils.getStubbedHybrisDelegate();
        mNetworkController = (MockNetworkController) mHybrisDelegate.getNetworkController(null);
        productCatalogNumber.add("HX9033/64");
        productCatalogNumber.add("HX9023/64");
        productCatalogNumber.add("HX9003/64");
        mMockPRXDataBuilder = new MockPRXSummaryListExecutor(mContext, productCatalogNumber, mOrderController);
    }

    @Test
    public void orderHistorySuccessResponseWithData() throws JSONException {
        mOrderController = new OrderController(mContext, new MockOrderListener() {
            @Override
            public void onGetOrderDetail(final Message msg) {
                assertEquals(RequestCode.GET_ORDERS, msg.what);
                assertTrue(msg.obj instanceof OrdersData);
            }
        });

        setStoreAndDelegate();
        mOrderController.getOrderList(0);
        JSONObject obj = new JSONObject(TestUtils.readFile(OrderControllerTest
                .class, "orders.txt"));
        mNetworkController.sendSuccess(obj);
    }

    public void orderDetailSuccessResponseWithData() throws JSONException {
        mOrderController = new OrderController(mContext, new MockOrderListener() {
            @Override
            public void onGetOrderDetail(final Message msg) {
                assertEquals(RequestCode.GET_ORDER_DETAIL, msg.what);
                assertTrue(msg.obj instanceof OrderDetail);
                final ArrayList<OrderDetail> detail = new ArrayList<>();
                detail.add((OrderDetail) msg.obj);
                mOrderController.requestPrxData(detail, new AbstractModel.DataLoadListener() {
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
                    IAPLog.e(IAPLog.LOG, e.getMessage());
                }
                assertNotNull(mOrderController.getProductData(detail));
            }
        });

        setStoreAndDelegate();
        mOrderController.getOrderDetails("14000098999");
        JSONObject obj = new JSONObject(TestUtils.readFile(OrderControllerTest
                .class, "order_detail.txt"));
        mNetworkController.sendSuccess(obj);
    }

    @Test
    public void orderHistoryErrorResponse() {
        mOrderController = new OrderController(mContext, new MockOrderListener() {
            @Override
            public void onGetOrderList(final Message msg) {
                assertEquals(RequestCode.GET_ORDERS, msg.what);
                assertTrue(msg.obj instanceof IAPNetworkError);
            }
        });

        setStoreAndDelegate();
        mOrderController.getOrderList(0);
        mNetworkController.sendFailure(new VolleyError());
    }

    @Test
    public void orderDetailErrorResponse() {
        mOrderController = new OrderController(mContext, new MockOrderListener() {
            @Override
            public void onGetOrderDetail(final Message msg) {
                assertEquals(RequestCode.GET_ORDER_DETAIL, msg.what);
                assertTrue(msg.obj instanceof IAPNetworkError);
            }
        });

        setStoreAndDelegate();
        mOrderController.getOrderDetails("");
        mNetworkController.sendFailure(new VolleyError());
    }

    @Test
    public void orderListEmptySuccessResponse() throws JSONException {
        mOrderController = new OrderController(mContext, new MockOrderListener() {
            @Override
            public void onGetOrderList(final Message msg) {
                assertEquals(RequestCode.GET_ORDERS, msg.what);
                assertEquals(NetworkConstants.EMPTY_RESPONSE, msg.obj);
            }
        });

        setStoreAndDelegate();
        mOrderController.getOrderList(0);
        JSONObject obj = new JSONObject(TestUtils.readFile(OrderControllerTest
                .class, "EmptyResponse.txt"));
        mNetworkController.sendSuccess(obj);
    }

    private void makePRXData() throws JSONException {

        ArrayList<String> ctns = new ArrayList<>();
        ctns.add("HD5061/01");
        ctns.add("HD7870/10");


        PrxRequest mProductSummaryBuilder = new ProductSummaryListRequest(ctns, null,null,null);

        JSONObject obj = new JSONObject(TestUtils.readFile(MockPRXSummaryListExecutor
                .class, "get_prx_success_response_HX9033_64.txt"));
        ResponseData responseData = mProductSummaryBuilder.getResponseData(obj);
        mMockPRXDataBuilder.sendSuccess(responseData);

    }

    @Test
    public void testGetPhoneContactSuccessResponseWithData() throws JSONException {
        mOrderController = new OrderController(mContext, new MockOrderListener() {
            @Override
            public void onGetPhoneContact(Message msg) {
                assertEquals(RequestCode.GET_PHONE_CONTACT, msg.what);
                assertTrue(msg.obj instanceof ContactsResponse);
            }
        });

        setStoreAndDelegate();
        mOrderController.getPhoneContact(NetworkURLConstants.SAMPLE_PRODUCT_CATEGORY);
        JSONObject obj = new JSONObject(TestUtils.readFile(OrderControllerTest.class, "ContactResponse.txt"));
        mNetworkController.sendSuccess(obj);
    }

    @Test
    public void testGetPhoneContactEmptySuccessResponse() throws JSONException {
        mOrderController = new OrderController(mContext, new MockOrderListener() {
            @Override
            public void onGetPhoneContact(Message msg) {
                assertEquals(RequestCode.GET_PHONE_CONTACT, msg.what);
                assertEquals(NetworkConstants.EMPTY_RESPONSE, msg.obj);
            }
        });

        setStoreAndDelegate();
        mOrderController.getPhoneContact(NetworkURLConstants.SAMPLE_PRODUCT_CATEGORY);
        JSONObject obj = new JSONObject(TestUtils.readFile(OrderControllerTest.class, "EmptyResponse.txt"));
        mNetworkController.sendSuccess(obj);
    }

    @Test
    public void testGetPhoneContactErrorResponse() {
        new RequestCode();
        mOrderController = new OrderController(mContext, new MockOrderListener() {
            @Override
            public void onGetPhoneContact(Message msg) {
                assertEquals(RequestCode.GET_PHONE_CONTACT, msg.what);
                assertTrue(msg.obj instanceof IAPNetworkError);
            }
        });

        setStoreAndDelegate();
        mOrderController.getPhoneContact(NetworkURLConstants.SAMPLE_PRODUCT_CATEGORY);
        mNetworkController.sendFailure(new VolleyError());
    }

    public void setStoreAndDelegate() {
        mOrderController.setHybrisDelegate(mHybrisDelegate);
        mOrderController.setStore(TestUtils.getStubbedStore());
    }

    @Test
    public void setProductData() {
        Mockito.when(productData.getImageURL()).thenReturn("www.google.com/images");
        Mockito.when(productData.getProductTitle()).thenReturn("Saver");
        Mockito.when(productData.getSubCategory()).thenReturn("Saver");
        Mockito.when(entries.getQuantity()).thenReturn(20);
        Mockito.when(entries.getTotalPrice()).thenReturn(new Cost());
        Mockito.when(entries.getProduct()).thenReturn(new Product());
        Mockito.when(orderDetail.getCode()).thenReturn("65756");
        OrderController orderController = new OrderController(mContext, null);
        orderController.setProductData(new ArrayList<ProductData>(), orderDetail, entries, productData, new Data());
    }

    @Mock
    List<Consignment> consignmentsMock;

    @Mock
    Consignment consignmentMock;

    @Mock
    List<ConsignmentEntries> entriesMock;

    @Mock
    ConsignmentEntries consignmentEntriesMock;

    @Mock
    OrderEntry orderEntryMock;

    @Mock
    Product productMock;

    @Mock
    Iterator<Consignment> itrConsignment;

    @Mock
    Iterator<ConsignmentEntries> itrConsignmentEntries;


    @Test
    public void shouldReturnConsignmentEntries() throws Exception {


        Mockito.when(itrConsignment.hasNext()).thenReturn(true, false);
        Mockito.when(itrConsignment.next()).thenReturn(consignmentMock);
        Mockito.when(consignmentsMock.iterator()).thenReturn(itrConsignment);


        Mockito.when(itrConsignmentEntries.hasNext()).thenReturn(true, false);
        Mockito.when(itrConsignmentEntries.next()).thenReturn(consignmentEntriesMock);
        Mockito.when(entriesMock.iterator()).thenReturn(itrConsignmentEntries);



        OrderController   mOrderController = new OrderController(mContext, new MockOrderListener() {
            @Override
            public void onGetOrderDetail(final Message msg) {

            }
        });

        Mockito.when(productMock.getCode()).thenReturn("DIS363/03");
        Mockito.when(orderEntryMock.getProduct()).thenReturn(productMock);
        Mockito.when(consignmentEntriesMock.getOrderEntry()).thenReturn(orderEntryMock);
        Mockito.when(entriesMock.get(0)).thenReturn(consignmentEntriesMock);
        Mockito.when(consignmentMock.getEntries()).thenReturn(entriesMock);

        Mockito.when(orderDetail.getConsignments()).thenReturn(consignmentsMock);
        assertEquals(mOrderController.getEntriesFromConsignMent(orderDetail,"DIS363/03"),consignmentEntriesMock);
    }


    Iterator<String> itrStringId;

    @Mock
    List<String> trackTraceIds;


    @Mock
    Iterator<String> itrStringUrl;

    @Mock
    List<String> trackTraceUrls;

    @Test
    public void shouldReturnOrderTrackingURL() throws Exception {

        OrderController   mOrderController = new OrderController(mContext, new MockOrderListener() {
            @Override
            public void onGetOrderDetail(final Message msg) {

            }
        });

        trackTraceIds = new ArrayList<>();
        trackTraceUrls = new ArrayList<>();

        trackTraceIds.add("300068874");
        trackTraceUrls.add("{300068874=http:\\/\\/www.fedex.com\\/Tracking?action=track&cntry_code=us&tracknumber_list=300068874}");

        ConsignmentEntries consignmentEntries = new ConsignmentEntries();
        consignmentEntries.setTrackAndTraceIDs(trackTraceIds);
        consignmentEntries.setTrackAndTraceUrls(trackTraceUrls);

        assertEquals(mOrderController.getOrderTrackUrl(consignmentEntries),"http:\\/\\/www.fedex.com\\/Tracking?action=track&cntry_code=us&tracknumber_list=300068874");

    }
}