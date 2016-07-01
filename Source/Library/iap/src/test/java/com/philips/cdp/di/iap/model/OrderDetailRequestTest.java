package com.philips.cdp.di.iap.model;

import android.content.Context;

import com.android.volley.Request;
import com.philips.cdp.di.iap.TestUtils;
import com.philips.cdp.di.iap.core.StoreSpec;
import com.philips.cdp.di.iap.response.orders.OrderDetail;
import com.philips.cdp.di.iap.response.orders.OrdersData;
import com.philips.cdp.di.iap.store.IAPUser;
import com.philips.cdp.di.iap.store.MockStore;
import com.philips.cdp.di.iap.store.NetworkURLConstants;
import com.philips.cdp.di.iap.utils.ModelConstants;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.robolectric.RobolectricTestRunner;

import java.util.HashMap;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertNull;
import static org.junit.Assert.*;

//@RunWith(RobolectricTestRunner.class)
public class OrderDetailRequestTest {

    @Mock
    Context mContext;
    @Mock
    IAPUser mUser;
    private StoreSpec mStore;
    private AbstractModel mModel;

    @Before
    public void setUp() {
        mStore = (new MockStore(mContext, mUser)).getStore();
        mStore.initStoreConfig("en","US", null);
        mModel = new OrderDetailRequest(mStore, null, null);
    }


    @Test
    public void testRequestMethodIsGET() {
        assertEquals(Request.Method.GET, mModel.getMethod());
    }

    @Test
    public void testQueryParamsIsNull() {
        assertNull(mModel.requestBody());
    }

    @Test
    public void testStoreIsNotNull() {
        assertNotNull(mModel.getStore());
    }

    @Test
    public void testOrderDetailURL() {
        HashMap<String, String> query = new HashMap<>();
        query.put(ModelConstants.ORDER_NUMBER, NetworkURLConstants.DUMMY_ORDER_ID);
        OrderDetailRequest request = new OrderDetailRequest(mStore, query, null);
        assertEquals(NetworkURLConstants.ORDER_DETAIL_URL, request.getUrl());
    }

    @Test
    public void testOrderDetailURLWhenParamsIsNull() {
        assertEquals(NetworkURLConstants.ORDER_DETAIL_URL, mModel.getUrl());
    }

    @Test
    public void parseResponseShouldBeOfGetShippingAddressDataType() {
        String oneAddress = TestUtils.readFile(OrderDetailRequestTest.class, "order_detail.txt");
        HashMap<String, String> query = new HashMap<>();
        query.put(ModelConstants.ORDER_NUMBER, NetworkURLConstants.DUMMY_ORDER_ID);
        OrderDetailRequest request = new OrderDetailRequest(mStore, query, null);
        Object response = request.parseResponse(oneAddress);
        assertEquals(response.getClass(), OrderDetail.class);
    }

}