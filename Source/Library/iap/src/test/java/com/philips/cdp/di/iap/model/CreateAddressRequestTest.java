package com.philips.cdp.di.iap.model;

import android.content.Context;

import com.android.volley.Request;
import com.philips.cdp.di.iap.TestUtils;
import com.philips.cdp.di.iap.integration.MockIAPSetting;
import com.philips.cdp.di.iap.store.StoreListener;
import com.philips.cdp.di.iap.response.addresses.Addresses;
import com.philips.cdp.di.iap.store.IAPUser;
import com.philips.cdp.di.iap.store.MockStore;
import com.philips.cdp.di.iap.store.NetworkURLConstants;
import com.philips.cdp.di.iap.utils.ModelConstants;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.util.HashMap;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;

public class CreateAddressRequestTest {
    @Mock
    Context mContext;
    @Mock
    IAPUser mUser;
    private AbstractModel mModel;

    @Before
    public void setUP() {
        StoreListener mStore = (new MockStore(mContext, mUser)).getStore(new MockIAPSetting(mContext));
        mStore.initStoreConfig(/*"en", "US",*/ null);

        HashMap<String, String> query = new HashMap<>();
        query.put(ModelConstants.PRODUCT_ENTRYCODE, NetworkURLConstants.DUMMY_PRODUCT_NUMBER);
        query.put(ModelConstants.PRODUCT_CODE, NetworkURLConstants.DUMMY_PRODUCT_ID);
        query.put(ModelConstants.PRODUCT_QUANTITY, "2");
        mModel = new CreateAddressRequest(mStore, query, null);
    }

    @Test
    public void isValidUrl() {
        assertEquals(NetworkURLConstants.GET_ADDRESSES_URL, mModel.getUrl());
    }


    @Test
    public void testRequestMethodIsPOST() {
        assertEquals(Request.Method.POST, mModel.getMethod());
    }

    @Test
    public void testQueryParamsIsNull() {
        CreateAddressRequest mockCreateAddressRequest = Mockito.mock(CreateAddressRequest.class);
        assertNotNull(mockCreateAddressRequest.requestBody());
    }

    @Test
    public void testStoreIsNotNull() {
        assertNotNull(mModel.getStore());
    }

    @Test
    public void isValidResponse() {
        String isCreated = TestUtils.readFile(CreateAddressRequestTest.class, "CreateAddress.txt");
        Object response = mModel.parseResponse(isCreated);
        assertEquals(response.getClass(), Addresses.class);
    }
}