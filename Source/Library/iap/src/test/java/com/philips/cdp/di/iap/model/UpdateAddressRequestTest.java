package com.philips.cdp.di.iap.model;

import android.content.Context;

import com.philips.cdp.di.iap.core.StoreSpec;
import com.philips.cdp.di.iap.store.IAPUser;
import com.philips.cdp.di.iap.store.MockStore;
import com.philips.cdp.di.iap.store.NetworkURLConstants;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import static org.mockito.Mockito.mock;

public class UpdateAddressRequestTest {
    @Mock
    private StoreSpec mStore;

    @Before
    public void setUP() {
        mStore = new MockStore(mock(Context.class), mock(IAPUser.class)).getStore();
        mStore.initStoreConfig("en", "us", null);
    }

    @Test(expected = RuntimeException.class)
    public void testgetURLThrowsWhenParamEqualToNull() {
        UpdateAddressRequest request = new UpdateAddressRequest(mStore, null, null);
        Assert.assertEquals(request.getUrl(), NetworkURLConstants.UPDATE_DELIVERY_ADDRESS_URL);
    }

}