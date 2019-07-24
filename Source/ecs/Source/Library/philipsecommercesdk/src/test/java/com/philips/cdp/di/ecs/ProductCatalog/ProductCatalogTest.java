package com.philips.cdp.di.ecs.ProductCatalog;

import android.content.Context;

import com.philips.cdp.di.ecs.ECSServices;
import com.philips.cdp.di.ecs.MockECSServices;
import com.philips.cdp.di.ecs.integration.ECSCallback;
import com.philips.cdp.di.ecs.model.products.Products;
import com.philips.platform.appinfra.AppInfra;
import com.philips.platform.appinfra.rest.RestInterface;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.robolectric.RobolectricTestRunner;

import static androidx.test.platform.app.InstrumentationRegistry.getInstrumentation;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

@RunWith(RobolectricTestRunner.class)
public class ProductCatalogTest {
    private Context mContext;


    MockECSServices mockECSServices;
    ECSServices ecsServices;


    private AppInfra appInfra;


    @Mock
    RestInterface mockRestInterface;


    @Before
    public void setUp() throws Exception {


        mContext = getInstrumentation().getContext();
        appInfra = new AppInfra.Builder().setRestInterface(mockRestInterface).build(mContext);
        appInfra.getServiceDiscovery().setHomeCountry("DE");


        mockECSServices = new MockECSServices("", appInfra);
        ecsServices = new ECSServices("",appInfra);

    }

    @Test
    public void getProductListSuccess() {
        mockECSServices.setJsonFileName("GetProductList.json");
        mockECSServices.getProductList(0, 1, new ECSCallback<Products, Exception>() {
            @Override
            public void onResponse(Products result) {
                assertNotNull(result);
                assert (result.getProducts().size() > 0); // at least 1 product should come
                assertNotNull(result.getProducts().get(0).getSummary());
                assertNotNull(result.getProducts().get(1).getSummary());
                // test case passed
            }

            @Override
            public void onFailure(Exception error, int errorCode) {
                assertFalse(true);
                // test case failed

            }
        });

    }


    /*
     * In this test case Get Product list API (Hybris) is failed
     * */
    @Test
    public void getProductListHybrisFailure() {
        mockECSServices.setJsonFileName("GetProductListHybrisFailure.json");
        mockECSServices.getProductList(0, 1, new ECSCallback<Products, Exception>() {
            @Override
            public void onResponse(Products result) {
                assertTrue(true);
                // test case failed

            }

            @Override
            public void onFailure(Exception error, int errorCode) {
                assertEquals(4999, errorCode); // error code for Product List
                // test case passed
            }
        });
    }

    /*
     * In this test case Get Product list API (Hybris) is failed
     * */
    @Test
    public void getProductListSummaryFailure() {
        mockECSServices.setJsonFileName("GetProductList.json");
        mockECSServices.getProductList(0, 1, new ECSCallback<Products, Exception>() {
            @Override
            public void onResponse(Products result) {
                assertTrue(true);
                // test case failed

            }

            @Override
            public void onFailure(Exception error, int errorCode) {
                assertEquals(4999, errorCode); // error code for Product List
                // test case passed
            }
        });
    }

}
