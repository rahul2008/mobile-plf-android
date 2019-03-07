package com.philips.cdp.di.iap.model;

import android.content.Context;

import com.android.volley.Request;
import com.philips.cdp.di.iap.integration.MockIAPDependencies;
import com.philips.cdp.di.iap.integration.MockIAPSetting;
import com.philips.cdp.di.iap.store.IAPUser;
import com.philips.cdp.di.iap.store.MockStore;
import com.philips.cdp.di.iap.store.StoreListener;
import com.philips.platform.appinfra.AppInfra;
import com.philips.platform.pif.DataInterface.USR.UserDataInterface;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.robolectric.RobolectricTestRunner;

import static androidx.test.platform.app.InstrumentationRegistry.getInstrumentation;
import static org.mockito.Mockito.mock;

@RunWith(RobolectricTestRunner.class)
public class CartDeleteRequestTest {
    @Mock
    private StoreListener mStore;

    @Before
    public void setUP() {
        Context context = getInstrumentation().getContext();
        mStore = new MockStore(context, mock(IAPUser.class)).getStore(new MockIAPSetting(context),new MockIAPDependencies(mock(AppInfra.class),mock(UserDataInterface.class)));
        mStore.initStoreConfig(/*"en", "us",*/ null);
    }


    @Test
    public void testRequestMethodIsPOST() {
        DeleteCartRequest request = new DeleteCartRequest(mStore, null, null);
        Assert.assertEquals(Request.Method.DELETE, request.getMethod());
    }

    @Test
    public void testQueryParamsIsNull() {
        DeleteCartRequest request = new DeleteCartRequest(mStore, null, null);
        Assert.assertNull(request.requestBody());
    }


    @Test
    public void parseResponseShouldBeOfCartCreateDataType() {
        DeleteCartRequest request = new DeleteCartRequest(mStore, null, null);
        Object response = request.parseResponse(null);
        Assert.assertNull(response);
    }
}