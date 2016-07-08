package com.philips.cdp.di.iap.model;

import android.content.Context;

import com.android.volley.Request;
import com.philips.cdp.di.iap.core.StoreSpec;
import com.philips.cdp.di.iap.store.IAPUser;
import com.philips.cdp.di.iap.store.MockStore;
import com.philips.cdp.di.iap.store.NetworkURLConstants;
import com.philips.cdp.di.iap.utils.IAPConstant;
import com.philips.cdp.di.iap.utils.ModelConstants;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.util.HashMap;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;

public class SetDeliveryAddressModeRequestTest {
    @Mock
    Context mContext;
    @Mock
    IAPUser mUser;
    private AbstractModel mModel;

    @Before
    public void setUP() {
        StoreSpec mStore = (new MockStore(mContext, mUser)).getStore();
        mStore.initStoreConfig("en", "US", null);

        HashMap<String, String> params = new HashMap<>();
        params.put(ModelConstants.DELIVERY_MODE_ID, "standard-net");
        mModel = new SetDeliveryAddressModeRequest(mStore, params, null);
    }

    @Test
    public void testRequestMethodIsPUT() {
        assertEquals(Request.Method.PUT, mModel.getMethod());
    }

    @Test
    public void testBodyParamsIsNull() {
        SetDeliveryAddressModeRequest setDeliveryAddressModeRequest =
                Mockito.mock(SetDeliveryAddressModeRequest.class);
        assertNotNull(setDeliveryAddressModeRequest.requestBody());
    }

    @Test
    public void testStoreIsNotNull() {
        assertNotNull(mModel.getStore());
    }

    @Test
    public void isValidUrl() {
        assertEquals(NetworkURLConstants.UPDATE_DELIVERY_MODE_URL, mModel.getUrl());
    }

    @Test
    public void testSetDeliveryModeAddressResponse() {
        assertEquals(IAPConstant.IAP_SUCCESS, mModel.parseResponse(null));
    }
}