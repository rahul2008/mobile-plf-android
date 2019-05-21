package com.philips.cdp.di.iap.model;

import android.content.Context;
import android.os.Message;

import com.android.volley.Request;
import com.philips.cdp.di.iap.TestUtils;
import com.philips.cdp.di.iap.integration.MockIAPDependencies;
import com.philips.cdp.di.iap.integration.MockIAPSetting;
import com.philips.cdp.di.iap.response.products.Products;
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
import org.mockito.Mockito;
import org.robolectric.RobolectricTestRunner;

import java.util.HashMap;

import static androidx.test.platform.app.InstrumentationRegistry.getInstrumentation;
import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNull;
import static org.junit.Assert.assertNotEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@RunWith(RobolectricTestRunner.class)
public class GetProductCatalogRequestTest {
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
        GetProductCatalogRequest request = new GetProductCatalogRequest(mStore, null, null);
        assertEquals(Request.Method.GET, request.getMethod());
    }

    @Test
    public void testQueryParamsIsNull() {
        GetProductCatalogRequest request = new GetProductCatalogRequest(mStore, null, null);
        assertNull(request.requestBody());
    }

    @Test
    public void matchProductCatalogURL() {
        HashMap<String, String> query = new HashMap<>();
        query.put(ModelConstants.CURRENT_PAGE, String.valueOf(0));
        query.put(ModelConstants.PAGE_SIZE, String.valueOf(1));
        GetProductCatalogRequest request = new GetProductCatalogRequest(mStore, query, null);
        assertEquals(NetworkURLConstants.PRODUCT_CATALOG_URL, request.getUrl());
    }

    @Test(expected = RuntimeException.class)
    public void testProductDetailURLWhenParamsIsNull() throws Exception {
        GetProductCatalogRequest request = new GetProductCatalogRequest(mStore, null, null);
        assertNotEquals(NetworkURLConstants.PRODUCT_CATALOG_URL, request.getUrl());
    }

    @Test
    public void parseResponseShouldBeOfProductsDataType() {
        GetProductCatalogRequest request = new GetProductCatalogRequest(mStore, null, null);
        String paymentResponse = TestUtils.readFile(GetRegionsRequestTest.class, "get_catalog.txt");
        Object response = request.parseResponse(paymentResponse);
        assertEquals(response.getClass(), Products.class);
    }

    @Test
    public void testonPostSuccess() {
        AbstractModel.DataLoadListener listener = Mockito.mock(AbstractModel.DataLoadListener.class);
        Message msg = Mockito.mock(Message.class);
        GetProductCatalogRequest request = new GetProductCatalogRequest(mStore, null, listener);
        GetProductCatalogRequest mockrequest = Mockito.spy(request);
        mockrequest.onPostSuccess(msg);
        verify(listener, Mockito.atLeast(1)).onModelDataLoadFinished(msg);
    }

}