package com.philips.cdp.di.iap.model;

import android.content.Context;

import com.android.volley.Request;
import com.philips.cdp.di.iap.TestUtils;
import com.philips.cdp.di.iap.core.StoreSpec;
import com.philips.cdp.di.iap.response.addresses.Addresses;
import com.philips.cdp.di.iap.store.IAPUser;
import com.philips.cdp.di.iap.store.MockStore;
import com.philips.cdp.di.iap.store.NetworkURLConstants;
import com.philips.cdp.di.iap.utils.ModelConstants;

import junit.framework.TestCase;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.util.HashMap;

import static org.mockito.Mockito.mock;

public class CreateAddressRequestTest extends TestCase {
    @Mock
    private StoreSpec mStore;

    @Before
    public void setUP() {
        mStore = new MockStore(mock(Context.class), mock(IAPUser.class)).getStore();
        mStore.initStoreConfig("en", "us", null);
    }

    @Test
    public void matchCreateAddressRequestURL() {
        HashMap<String, String> query = new HashMap<>();
        query.put(ModelConstants.PRODUCT_ENTRYCODE, NetworkURLConstants.DUMMY_PRODUCT_NUBMBER);
        query.put(ModelConstants.PRODUCT_CODE, NetworkURLConstants.DUMMY_PRODUCT_ID);
        query.put(ModelConstants.PRODUCT_QUANTITY, "2");
        CreateAddressRequest request = new CreateAddressRequest(mStore, query, null);
        assertEquals(NetworkURLConstants.ADDRESS_DETAILS_URL, request.getUrl());
    }


    @Test
    public void testRequestMethodIsPOST() {
        CreateAddressRequest request = new CreateAddressRequest(mStore, null, null);
        assertEquals(Request.Method.POST, request.getMethod());
    }

    @Test
    public void testQueryParamsIsNull() {
        CreateAddressRequest mockCreateAddressRequest = Mockito.mock(CreateAddressRequest.class);
        assertNotNull(mockCreateAddressRequest.requestBody());
    }

    @Test
    public void parseResponseShouldBeOfCreateAddressRequestDataType() {
        CreateAddressRequest request = new CreateAddressRequest(mStore, null, null);
        String oneAddress = TestUtils.readFile(CreateAddressRequestTest.class, "create_address.txt");
        Object response = request.parseResponse(oneAddress);
        assertEquals(response.getClass(), Addresses.class);
    }


}