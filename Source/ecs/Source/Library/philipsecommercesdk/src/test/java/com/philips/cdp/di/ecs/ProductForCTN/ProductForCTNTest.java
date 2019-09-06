package com.philips.cdp.di.ecs.ProductForCTN;

import android.content.Context;

import com.android.volley.NoConnectionError;
import com.android.volley.VolleyError;
import com.philips.cdp.di.ecs.ECSServices;
import com.philips.cdp.di.ecs.MockECSServices;
import com.philips.cdp.di.ecs.StaticBlock;
import com.philips.cdp.di.ecs.TestUtil;
import com.philips.cdp.di.ecs.error.ECSError;
import com.philips.cdp.di.ecs.integration.ECSCallback;
import com.philips.cdp.di.ecs.model.products.Product;
import com.philips.platform.appinfra.AppInfra;
import com.philips.platform.appinfra.rest.RestInterface;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.robolectric.RobolectricTestRunner;

import java.io.InputStream;

import static androidx.test.platform.app.InstrumentationRegistry.getInstrumentation;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;

@RunWith(RobolectricTestRunner.class)
public class ProductForCTNTest {

    private Context mContext;


    MockECSServices mockECSServices;
    ECSServices ecsServices;


    private AppInfra appInfra;


    @Mock
    RestInterface mockRestInterface;

    MockGetProductForRequest mockGetProductForRequest;

    ECSCallback<Product, Exception> ecsCallback ;

    String ctn = "MS5030/01";


    @Before
    public void setUp() throws Exception {


        mContext = getInstrumentation().getContext();
        appInfra = new AppInfra.Builder().setRestInterface(mockRestInterface).build(mContext);
       // appInfra.getServiceDiscovery().setHomeCountry("DE");


        mockECSServices = new MockECSServices("", appInfra);

        StaticBlock.initialize();
        ecsServices = new ECSServices("",appInfra);

        ecsCallback = new ECSCallback<Product, Exception>() {
            @Override
            public void onResponse(Product result) {

            }

            @Override
            public void onFailure(Exception error, ECSError ecsError) {

            }
        };

        mockGetProductForRequest = new MockGetProductForRequest("GetProductForCTN.json",ctn,ecsCallback);
    }

    @Test
    public void getProductForCTNHybrissuccess(){
        mockECSServices.setJsonFileName("GetProductForCTN.json");
        mockECSServices.getProductFor(ctn, new ECSCallback<Product, Exception>() {
            @Override
            public void onResponse(Product product) {
                assertNotNull(product);
                //assertNotNull(product.getSummary());
                // test case passed
            }

            @Override
            public void onFailure(Exception error, ECSError ecsError) {
                assertFalse(true);
                // test case passed
            }
        });
    }

    @Test
    public void getProductForCTNHybrisFailure(){
        mockECSServices.setJsonFileName("EmptyJson.json");
        mockECSServices.getProductFor(ctn, new ECSCallback<Product, Exception>() {
            @Override
            public void onResponse(Product product) {
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


    @Test
    public void isValidURL() {
        System.out.println("print the URL"+mockGetProductForRequest.getURL());
        ctn = ctn.replace("/","_");
        //acc.us.pil.shop.philips.com/pilcommercewebservices/v2/US_Tuscany/products/MS5030_01?lang=en_US
        String excepted = StaticBlock.getBaseURL()+"pilcommercewebservices"+"/v2/"+StaticBlock.getSiteID()+"/products/"+ctn+"?lang="+StaticBlock.getLocale();
        Assert.assertEquals(excepted,mockGetProductForRequest.getURL());
    }

    @Test
    public void isValidGetRequest() {
        Assert.assertEquals(0,mockGetProductForRequest.getMethod());
    }

    @Test
    public void isValidParam() {
        assertNull(mockGetProductForRequest.getParams());
    }



    @Test
    public void verifyOnResponseSuccess() {

        ECSCallback<Product, Exception> spy1 = Mockito.spy(ecsCallback);
        mockGetProductForRequest = new MockGetProductForRequest("GetProductForCTN.json","MS5030/01",spy1);

        JSONObject jsonObject = getJsonObject("GetProductForCTN.json");

        mockGetProductForRequest.onResponse(jsonObject);

        Mockito.verify(spy1).onResponse(any(Product.class));

    }


    @Test
    public void verifyOnResponseError() {
        ECSCallback<Product, Exception> spy1 = Mockito.spy(ecsCallback);
        mockGetProductForRequest = new MockGetProductForRequest("GetProductForCTN.json","MS5030/01",spy1);
        VolleyError volleyError = new NoConnectionError();
        mockGetProductForRequest.onErrorResponse(volleyError);
        Mockito.verify(spy1).onFailure(any(Exception.class),anyInt());

    }


    @Test
    public void assertResponseSuccessListenerNotNull() {
        assertNotNull(mockGetProductForRequest.getJSONSuccessResponseListener());
    }

    JSONObject getJsonObject(String jsonfileName){

        JSONObject result = null;
        InputStream in = getClass().getClassLoader().getResourceAsStream(jsonfileName);//"PRXProductAssets.json"
        String jsonString = TestUtil.loadJSONFromFile(in);
        try {
            return new JSONObject(jsonString);
        } catch (JSONException e) {
            return null;
        }
    }

}
