package com.philips.cdp.di.ecs.Payment;


import android.content.Context;

import com.android.volley.NoConnectionError;
import com.android.volley.VolleyError;
import com.philips.cdp.di.ecs.ECSServices;
import com.philips.cdp.di.ecs.MockECSServices;
import com.philips.cdp.di.ecs.StaticBlock;
import com.philips.cdp.di.ecs.error.ECSError;
import com.philips.cdp.di.ecs.integration.ECSCallback;
import com.philips.cdp.di.ecs.model.address.Addresses;
import com.philips.cdp.di.ecs.model.orders.OrderDetail;
import com.philips.cdp.di.ecs.model.payment.MakePaymentData;
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
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;

@RunWith(RobolectricTestRunner.class)

public class MakePaymentRequestTest {

    private Context mContext;


    MockECSServices mockECSServices;
    ECSServices ecsServices;


    private AppInfra appInfra;

    MockMakePaymentRequest mockMakePaymentRequest;

    OrderDetail orderDetail;
    Addresses addresses;

    ECSCallback<MakePaymentData, Exception> ecsCallback;


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
        addresses = StaticBlock.getAddressesObject();
        ecsCallback = new ECSCallback<MakePaymentData, Exception>() {
            @Override
            public void onResponse(MakePaymentData result) {

            }

            @Override
            public void onFailure(Exception error, ECSError ecsError) {

            }
        };
        orderDetail = new OrderDetail();
        orderDetail.setCode("123");
        mockMakePaymentRequest = new MockMakePaymentRequest("MakePaymentSuccess.json",orderDetail,addresses,ecsCallback);

    }

    @Test
    public void makePaymentSuccess() {
        mockECSServices.setJsonFileName("MakePaymentSuccess.json");
        OrderDetail orderDetail = new OrderDetail();
        Addresses addresses = new Addresses();
        mockECSServices.makePayment(orderDetail,addresses,new ECSCallback<MakePaymentData, Exception>() {
            @Override
            public void onResponse(MakePaymentData result) {
                assertNotNull(result);
                assertNotNull(result.getWorldpayUrl());
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
        System.out.println("print the URL"+mockMakePaymentRequest.getURL());
        String excepted = StaticBlock.getBaseURL()+"pilcommercewebservices"+"/v2/"+StaticBlock.getSiteID()+"/users/current/orders/123/pay";
        Assert.assertEquals(excepted,mockMakePaymentRequest.getURL());
    }

    @Test
    public void isValidPostRequest() {
        Assert.assertEquals(1,mockMakePaymentRequest.getMethod());
    }

    @Test
    public void isValidHeader() {

        Map<String, String> expectedMap = new HashMap<String, String>();
        expectedMap.put("Content-Type", "application/x-www-form-urlencoded");
        expectedMap.put("Authorization", "Bearer " + "acceesstoken");

        Map<String, String> actual = mockMakePaymentRequest.getHeader();

        assertTrue(expectedMap.equals(actual));
    }

    @Test
    public void isValidParam() {
        assertNotNull(mockMakePaymentRequest.getParams());
        assertNotEquals(0,mockMakePaymentRequest.getParams().size());
    }


    @Test
    public void verifyOnResponseError() {
        ECSCallback<MakePaymentData, Exception> spy1 = Mockito.spy(ecsCallback);
        mockMakePaymentRequest = new MockMakePaymentRequest("MakePaymentSuccess.json", orderDetail, addresses, spy1);
        VolleyError volleyError = new NoConnectionError();
        mockMakePaymentRequest.onErrorResponse(volleyError);
        Mockito.verify(spy1).onFailure(any(Exception.class),anyInt());

    }

    @Test
    public void assertResponseSuccessListenerNotNull() {
        assertNotNull(mockMakePaymentRequest.getStringSuccessResponseListener());
    }

}
