package com.philips.cdp.di.ecs.ProductDetail;

import android.content.Context;

import com.philips.cdp.di.ecs.ECSServices;
import com.philips.cdp.di.ecs.MockECSServices;
import com.philips.cdp.di.ecs.integration.ECSCallback;
import com.philips.cdp.di.ecs.model.products.Product;
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
public class ProductDetailTest {

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


    // This will fetch Product detail:  Asset and Disclaimer
    @Test
    public void getProductDetailSuccess() {
        Product product = new Product();
        product.setCode("HX2345/01");
        mockECSServices.setJsonFileName("PRXProductAssets.json");
        mockECSServices.getProductDetail(product, new ECSCallback<Product, Exception>() {
            @Override
            public void onResponse(Product product) {
                assertNotNull(product);
                assertNotNull(product.getAssets());
                assertNotNull(product.getDisclaimers());
                // test case passed
            }

            @Override
            public void onFailure(Exception error, int errorCode) {
                assertFalse(true);
                // test case failed
            }
        });
    }


    @Test
    public void getProductDetailAssetFailure() {
        Product product = new Product();
        product.setCode("HX2345/01");
        mockECSServices.setJsonFileName("Empty.json");
        mockECSServices.getProductDetail(product, new ECSCallback<Product, Exception>() {
            @Override
            public void onResponse(Product product) {
                assertTrue(true);
                // test case failed
            }

            @Override
            public void onFailure(Exception error, int errorCode) {
                assertEquals(5999, errorCode); // error code for Product List
                // test case passed
            }
        });
    }
}
