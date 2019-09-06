package com.philips.cdp.di.ecs.Payment;

import android.content.Context;

import com.android.volley.NoConnectionError;
import com.android.volley.VolleyError;
import com.philips.cdp.di.ecs.ECSServices;
import com.philips.cdp.di.ecs.MockECSServices;
import com.philips.cdp.di.ecs.StaticBlock;
import com.philips.cdp.di.ecs.error.ECSError;
import com.philips.cdp.di.ecs.integration.ECSCallback;
import com.philips.cdp.di.ecs.model.orders.OrderDetail;
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
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;

@RunWith(RobolectricTestRunner.class)
public class PlaceOrderTest {

    private Context mContext;


    MockECSServices mockECSServices;
    ECSServices ecsServices;


    private AppInfra appInfra;

    MockPlaceOrderRequest mockPlaceOrderRequest;

    ECSCallback<OrderDetail, Exception> ecsCallback;


    @Mock
    RestInterface mockRestInterface;

    @Before
    public void setUp() throws Exception {


        mContext = getInstrumentation().getContext();
        appInfra = new AppInfra.Builder().setRestInterface(mockRestInterface).build(mContext);
        appInfra.getServiceDiscovery().setHomeCountry("DE");


        mockECSServices = new MockECSServices("", appInfra);
        ecsServices = new ECSServices("",appInfra);

        StaticBlock.initialize();
        ecsCallback = new ECSCallback<OrderDetail, Exception>() {
            @Override
            public void onResponse(OrderDetail result) {

            }

            @Override
            public void onFailure(Exception error, ECSError ecsError) {

            }
        };
        mockPlaceOrderRequest = new MockPlaceOrderRequest("SubmitOrderSuccess.json","", ecsCallback);
    }

    @Test
    public void placeOrderSuccess() {
        mockECSServices.setJsonFileName("SubmitOrderSuccess.json");
        mockECSServices.submitOrder("123",new ECSCallback<OrderDetail, Exception>() {
            @Override
            public void onResponse(OrderDetail result) {
                assertNotNull(result);
                //test passed

            }

            @Override
            public void onFailure(Exception error, ECSError ecsError) {
                assertTrue(false);
                //test failed
            }
        });
    }

    @Test
    public void isValidURL() {
        System.out.println("print the URL"+mockPlaceOrderRequest.getURL());
        String excepted = StaticBlock.getBaseURL()+"pilcommercewebservices"+"/v2/"+StaticBlock.getSiteID()+"/users/current/orders";
        Assert.assertEquals(excepted,mockPlaceOrderRequest.getURL());
    }

    @Test
    public void isValidPostRequest() {
        Assert.assertEquals(1,mockPlaceOrderRequest.getMethod());
    }

    @Test
    public void isValidHeader() {

        Map<String, String> expectedMap = new HashMap<String, String>();
        expectedMap.put("Authorization", "Bearer " + "acceesstoken");

        Map<String, String> actual = mockPlaceOrderRequest.getHeader();

        assertTrue(expectedMap.equals(actual));
    }

    @Test
    public void isValidParam() {
        assertNotNull(mockPlaceOrderRequest.getParams());
        assertNotEquals(0,mockPlaceOrderRequest.getParams().size());
    }


    @Test
    public void verifyOnResponseError() {
        ECSCallback<OrderDetail, Exception> spy1 = Mockito.spy(ecsCallback);
        mockPlaceOrderRequest = new MockPlaceOrderRequest("SubmitOrderSuccess.json","", spy1);
        VolleyError volleyError = new NoConnectionError();
        mockPlaceOrderRequest.onErrorResponse(volleyError);
        Mockito.verify(spy1).onFailure(any(Exception.class),any(ECSError.class));

    }

    @Test
    public void assertResponseSuccessListenerNotNull() {
        assertNotNull(mockPlaceOrderRequest.getStringSuccessResponseListener());
    }
}
