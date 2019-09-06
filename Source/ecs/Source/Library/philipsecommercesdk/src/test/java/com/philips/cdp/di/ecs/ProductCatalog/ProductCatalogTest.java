package com.philips.cdp.di.ecs.ProductCatalog;

import android.content.Context;

import com.android.volley.NoConnectionError;
import com.android.volley.VolleyError;
import com.philips.cdp.di.ecs.ECSServices;
import com.philips.cdp.di.ecs.MockECSServices;
import com.philips.cdp.di.ecs.MockInputValidator;
import com.philips.cdp.di.ecs.StaticBlock;
import com.philips.cdp.di.ecs.constants.ModelConstants;
import com.philips.cdp.di.ecs.error.ECSError;
import com.philips.cdp.di.ecs.integration.ECSCallback;
import com.philips.cdp.di.ecs.model.products.Products;
import com.philips.platform.appinfra.AppInfra;
import com.philips.platform.appinfra.rest.RestInterface;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.robolectric.RobolectricTestRunner;

import java.util.HashMap;
import java.util.Map;

import static androidx.test.platform.app.InstrumentationRegistry.getInstrumentation;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;

@RunWith(RobolectricTestRunner.class)
public class ProductCatalogTest {
    private Context mContext;


    MockECSServices mockECSServices;
    ECSServices ecsServices;


    private AppInfra appInfra;


    @Mock
    RestInterface mockRestInterface;

    MockGetProductListRequest  mockGetProductListRequest;

    ECSCallback<Products, Exception> ecsCallback;

    int currentPage = 0;
    int pageSize = 20;
    private MockInputValidator mockInputValidator;

    @Before
    public void setUp() throws Exception {


        mContext = getInstrumentation().getContext();
        appInfra = new AppInfra.Builder().setRestInterface(mockRestInterface).build(mContext);
        appInfra.getServiceDiscovery().setHomeCountry("DE");


        mockECSServices = new MockECSServices("", appInfra);
        ecsServices = new ECSServices("",appInfra);

        StaticBlock.initialize();

        mockInputValidator = new MockInputValidator();

        ecsCallback = new ECSCallback<Products, Exception>() {
            @Override
            public void onResponse(Products result) {

            }

            @Override
            public void onFailure(Exception error, ECSError ecsError) {

            }
        };

        mockGetProductListRequest = new MockGetProductListRequest("GetProductList.json",0,20,ecsCallback);
    }

    @Test
    public void getProductListSuccess() {
        mockInputValidator.setJsonFileName("GetProductList.json");
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
            public void onFailure(Exception error, ECSError ecsError) {
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
        mockInputValidator.setJsonFileName("GetProductListHybrisFailure.json");
        mockECSServices.getProductList(0, 1, new ECSCallback<Products, Exception>() {
            @Override
            public void onResponse(Products result) {
                assertTrue(true);
                // test case failed

            }

            @Override
            public void onFailure(Exception error, ECSError ecsError) {
                assertEquals(5999, ecsError); // error code for Product List
                // test case passed
            }
        });
    }

    /*
     * In this test case Get Product list API (Hybris) is failed
     * */
    @Test
    public void getProductListSummaryFailure() {
        mockInputValidator.setJsonFileName("GetProductList.json");
        mockECSServices.getProductList(0, 1, new ECSCallback<Products, Exception>() {
            @Override
            public void onResponse(Products result) {
                assertTrue(true);
                // test case failed

            }

            @Override
            public void onFailure(Exception error, ECSError ecsError) {
                assertEquals(4999, ecsError); // error code for Product List
                // test case passed
            }
        });
    }


    @Test
    public void isValidURL() {
        System.out.println("print the URL"+mockGetProductListRequest.getURL());
        //acc.us.pil.shop.philips.com/pilcommercewebservices/v2/US_Tuscany/products/search?query=::category:null&lang=en_US&currentPage=0&pageSize=20
        String excepted = StaticBlock.getBaseURL()+"pilcommercewebservices"+"/v2/"+StaticBlock.getSiteID()+"/products/search?query=::category:"+StaticBlock.getRootCategory()+"&lang="+StaticBlock.getLocale()+"&currentPage="+currentPage+"&pageSize="+pageSize;
        Assert.assertEquals(excepted,mockGetProductListRequest.getURL());
    }

    @Test
    public void isValidGetRequest() {
        Assert.assertEquals(0,mockGetProductListRequest.getMethod());
    }

    @Test
    public void isValidParam() {

        Map<String, String> expectedMap = new HashMap<>();
        expectedMap.put(ModelConstants.CURRENT_PAGE, String.valueOf(currentPage));
        expectedMap.put(ModelConstants.PAGE_SIZE, String.valueOf(pageSize));


        assertNotNull(mockGetProductListRequest.getParams());
        assertTrue(expectedMap.equals(mockGetProductListRequest.getParams()));
    }




    @Test
    public void verifyOnResponseError() {
        ECSCallback<Products, Exception> spy1 = Mockito.spy(ecsCallback);
        mockGetProductListRequest = new MockGetProductListRequest("GetProductList.json",0,20,spy1);
        VolleyError volleyError = new NoConnectionError();
        mockGetProductListRequest.onErrorResponse(volleyError);
        Mockito.verify(spy1).onFailure(any(Exception.class),any(ECSError.class));

    }


    @Test
    public void assertResponseSuccessListenerNotNull() {
        assertNotNull(mockGetProductListRequest.getJSONSuccessResponseListener());
    }

    @Test
    public void headerShouldNull() {
        assertNull(mockGetProductListRequest.getHeader());
    }

    @Test
    public void isValidToken() {
        assertNull(mockGetProductListRequest.getToken());
    }

}
