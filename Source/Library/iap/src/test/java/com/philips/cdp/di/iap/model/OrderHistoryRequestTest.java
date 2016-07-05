package com.philips.cdp.di.iap.model;

import android.content.Context;

import com.android.volley.Request;
import com.philips.cdp.di.iap.TestUtils;
import com.philips.cdp.di.iap.core.StoreSpec;
import com.philips.cdp.di.iap.response.orders.OrdersData;
import com.philips.cdp.di.iap.store.IAPUser;
import com.philips.cdp.di.iap.store.MockStore;
import com.philips.cdp.di.iap.store.NetworkURLConstants;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.robolectric.RobolectricTestRunner;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertNull;
import static org.junit.Assert.*;

@RunWith(RobolectricTestRunner.class)
public class OrderHistoryRequestTest {

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
        mModel = new OrderHistoryRequest(mStore, null, null);
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

    @Test(expected = RuntimeException.class)
    public void orderHistoryURL() {
        assertEquals(NetworkURLConstants.PLACE_ORDER_URL, mModel.getUrl());
    }

    @Test
    public void parseResponseShouldBeOfGetShippingAddressDataType() {
        String oneAddress = TestUtils.readFile(OrderHistoryRequestTest.class, "orders.txt");
        Object response = mModel.parseResponse(oneAddress);
        assertEquals(response.getClass(), OrdersData.class);
    }
}