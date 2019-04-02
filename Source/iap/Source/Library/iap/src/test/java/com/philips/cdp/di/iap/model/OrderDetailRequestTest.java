package com.philips.cdp.di.iap.model;

import android.content.Context;

import com.android.volley.Request;
import com.philips.cdp.di.iap.TestUtils;
import com.philips.cdp.di.iap.integration.MockIAPDependencies;
import com.philips.cdp.di.iap.integration.MockIAPSetting;
import com.philips.cdp.di.iap.response.orders.OrderDetail;
import com.philips.cdp.di.iap.store.IAPUser;
import com.philips.cdp.di.iap.store.MockStore;
import com.philips.cdp.di.iap.store.NetworkURLConstants;
import com.philips.cdp.di.iap.store.StoreListener;
import com.philips.cdp.di.iap.utils.ModelConstants;
import com.philips.platform.appinfra.AppInfra;
import com.philips.platform.pif.DataInterface.USR.UserDataInterface;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.robolectric.RobolectricTestRunner;

import java.util.HashMap;

import static androidx.test.platform.app.InstrumentationRegistry.getInstrumentation;
import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertNull;
import static org.junit.Assert.assertNotEquals;
import static org.mockito.Mockito.mock;

@RunWith(RobolectricTestRunner.class)
public class OrderDetailRequestTest {

    private Context mContext;

    @Mock
    private IAPUser mUser;

    private StoreListener mStore;
    private AbstractModel mModel;

    @Before
    public void setUp() {
        mContext = getInstrumentation().getContext();
        mStore = (new MockStore(mContext, mUser)).getStore(new MockIAPSetting(mContext),new MockIAPDependencies(mock(AppInfra.class),mock(UserDataInterface.class)));
        mStore.initStoreConfig(/*"en", "US",*/ null);
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
    }

    @Test(expected = RuntimeException.class)
    public void testOrderDetailURLWhenParamsIsNull() throws Exception {
        assertNotEquals(NetworkURLConstants.ORDER_DETAIL_URL, mModel.getUrl());
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