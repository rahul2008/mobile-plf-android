package com.philips.cdp.di.ecs;

import android.content.Context;
import android.support.annotation.NonNull;

import com.philips.cdp.di.ecs.integration.ECSCallback;
import com.philips.cdp.di.ecs.integration.OAuthInput;
import com.philips.cdp.di.ecs.model.products.Product;
import com.philips.cdp.di.ecs.model.products.Products;
import com.philips.cdp.di.ecs.model.response.OAuthResponse;
import com.philips.platform.appinfra.AppInfra;
import com.philips.platform.appinfra.rest.RestInterface;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.robolectric.RobolectricTestRunner;

import static androidx.test.platform.app.InstrumentationRegistry.getInstrumentation;
import static org.junit.Assert.*;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;

@RunWith(RobolectricTestRunner.class)

public class ECSServicesTest {
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
    public void init() {
    }


    ////////////////Start of Auth Test cases/////////////

    @Test
    public void hybrisOathAuthenticationSuccess() {
        mockECSServices.setJsonFileName("HybrisOauthSuccess.json");

        OAuthInput oAuthInput = new OAuthInput() {
            @Override
            public String getJanRainID() {
                return "mock Jainrain ID";
            }
        };
        mockECSServices.hybrisOathAuthentication(oAuthInput, new ECSCallback<OAuthResponse, Exception>() {
            @Override
            public void onResponse(OAuthResponse result) {
                assertNotNull(result);
                assertNotNull(result.getAccessToken());
                // test case passed
            }

            @Override
            public void onFailure(Exception error, int errorCode) {
                assertTrue(true);
                // test case failed
            }
        });

    }

    @Test
    public void hybrisOathAuthenticationFailure() {
        mockECSServices.setJsonFileName("HybrisOauthFailure.json");

        OAuthInput oAuthInput = new OAuthInput() {
            @Override
            public String getJanRainID() {
                return "mock Jainrain ID";
            }
        };
        mockECSServices.hybrisOathAuthentication(oAuthInput, new ECSCallback<OAuthResponse, Exception>() {
            @Override
            public void onResponse(OAuthResponse result) {
                assertNotNull(result);
                assertNull(result.getAccessToken());
                // test case passed
            }

            @Override
            public void onFailure(Exception error, int errorCode) {
                assertTrue(true);
                // test case failed
            }
        });

    }

    @Test
    public void hybrisOathAuthenticationInvalidClient() {
        mockECSServices.setJsonFileName("HybrisInvalidClient.json");

        OAuthInput oAuthInput = new OAuthInput() {
            @Override
            public String getJanRainID() {
                return "mock Jainrain ID";
            }
        };
        mockECSServices.hybrisOathAuthentication(oAuthInput, new ECSCallback<OAuthResponse, Exception>() {
            @Override
            public void onResponse(OAuthResponse result) {
                assertNotNull(result);
                assertNull(result.getAccessToken());
                // test case passed
            }

            @Override
            public void onFailure(Exception error, int errorCode) {
                assertTrue(true);
                // test case failed
            }
        });

    }

    @Test
    public void hybrisOathAuthenticationInvalidClientReal() {
        //mockECSServices.setJsonFileName("HybrisInvalidClient.json");

        OAuthInput oAuthInput = new OAuthInput() {
            @Override
            public String getJanRainID() {
                return "mock Jainrain ID";
            }
        };
        ecsServices.hybrisOathAuthentication(oAuthInput, new ECSCallback<OAuthResponse, Exception>() {
            @Override
            public void onResponse(OAuthResponse result) {
                assertNotNull(result);
                assertNull(result.getAccessToken());
                // test case passed
            }

            @Override
            public void onFailure(Exception error, int errorCode) {
                assertTrue(true);
                // test case failed
            }
        });

    }



    ////////////////End of Auth Test cases/////////////

    @Test
    public void getIAPConfig() {
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
        mockECSServices.setJsonFileName("PRXProductAssetsFailure.json");
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


    @Test
    public void getProductAsset() {
    }

    @Test
    public void getProductDisclaimer() {
    }
}