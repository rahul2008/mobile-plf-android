package com.philips.cdp.di.iap.model;

import android.content.Context;

import com.android.volley.Request;
import com.philips.cdp.di.iap.TestUtils;
import com.philips.cdp.di.iap.integration.MockIAPDependencies;
import com.philips.cdp.di.iap.integration.MockIAPSetting;
import com.philips.cdp.di.iap.response.carts.CreateCartData;
import com.philips.cdp.di.iap.store.IAPUser;
import com.philips.cdp.di.iap.store.MockStore;
import com.philips.cdp.di.iap.store.NetworkURLConstants;
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
public class CartCreateRequestTest {
    @Mock
    private StoreListener mStore;

    @Before
    public void setUp() {
        Context context = getInstrumentation().getContext();
        mStore = new MockStore(context, mock(IAPUser.class)).getStore(new MockIAPSetting(context),new MockIAPDependencies(mock(AppInfra.class),mock(UserDataInterface.class)));
        mStore.initStoreConfig(/*"en", "us", */null);
    }

    @Test
    public void matchCartCreateRequestURL() {
        CartCreateRequest request = new CartCreateRequest(mStore, null, null);
        Assert.assertEquals(NetworkURLConstants.CREATE_CART_URL, request.getUrl());
    }

    @Test
    public void testRequestMethodIsPOST() {
        CartCreateRequest request = new CartCreateRequest(mStore, null, null);
        Assert.assertEquals(Request.Method.POST, request.getMethod());
    }

    @Test
    public void testQueryParamsIsNull() {
        CartCreateRequest request = new CartCreateRequest(mStore, null, null);
        Assert.assertNull(request.requestBody());
    }


    @Test
    public void parseResponseShouldBeOfCartCreateDataType() {
        CartCreateRequest request = new CartCreateRequest(mStore, null, null);
        String oneAddress = TestUtils.readFile(CartCreateRequestTest.class, "create_cart.txt");
        Object response = request.parseResponse(oneAddress);
        Assert.assertEquals(response.getClass(), CreateCartData.class);
    }
}