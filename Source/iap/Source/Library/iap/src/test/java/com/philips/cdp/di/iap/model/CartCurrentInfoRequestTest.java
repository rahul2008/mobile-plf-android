package com.philips.cdp.di.iap.model;

import android.content.Context;
import android.os.Message;

import com.android.volley.Request;
import com.philips.cdp.di.iap.TestUtils;
import com.philips.cdp.di.iap.integration.MockIAPDependencies;
import com.philips.cdp.di.iap.integration.MockIAPSetting;
import com.philips.cdp.di.iap.response.carts.CartsEntity;
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
import org.mockito.Mockito;
import org.robolectric.RobolectricTestRunner;

import static androidx.test.platform.app.InstrumentationRegistry.getInstrumentation;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@RunWith(RobolectricTestRunner.class)
public class CartCurrentInfoRequestTest {
    @Mock
    private StoreListener mStore;

    @Before
    public void setUP() {
        Context context = getInstrumentation().getContext();
        mStore = new MockStore(context, mock(IAPUser.class)).getStore(new MockIAPSetting(context),new MockIAPDependencies(mock(AppInfra.class),mock(UserDataInterface.class)));
        mStore.initStoreConfig(/*"en", "us",*/ null);
    }


    @Test
    public void testRequestMethodIsGET() {
        GetCartsRequest request = new GetCartsRequest(mStore, null, null);
        Assert.assertEquals(Request.Method.GET, request.getMethod());
    }

    @Test
    public void testQueryParamsIsNull() {
        GetCartsRequest request = new GetCartsRequest(mStore, null, null);
        Assert.assertNull(request.requestBody());
    }

    @Test
    public void parseResponseShouldBeOfCartCurrentInfoDataType() {
        GetCartsRequest request = new GetCartsRequest(mStore, null, null);
        String oneAddress = TestUtils.readFile(CartCurrentInfoRequestTest.class, "create_cart.txt");
        Object response = request.parseResponse(oneAddress);
        Assert.assertEquals(response.getClass(), CartsEntity.class);
    }


    @Test
    public void testonPostSuccess() {
        AbstractModel.DataLoadListener listener = Mockito.mock(AbstractModel.DataLoadListener.class);
        Message msg = Mockito.mock(Message.class);
        GetCartsRequest request = new GetCartsRequest(mStore, null, listener);
        GetCartsRequest mockrequest = Mockito.spy(request);//new GetCartsRequest(mStore, null, listener);
        mockrequest.onPostSuccess(msg);
        verify(listener, Mockito.atLeast(1)).onModelDataLoadFinished(msg);
    }
}