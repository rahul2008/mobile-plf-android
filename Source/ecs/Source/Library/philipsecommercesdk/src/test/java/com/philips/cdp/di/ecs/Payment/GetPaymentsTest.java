package com.philips.cdp.di.ecs.Payment;

import android.content.Context;

import com.android.volley.NoConnectionError;
import com.android.volley.VolleyError;
import com.philips.cdp.di.ecs.ECSServices;
import com.philips.cdp.di.ecs.MockECSServices;
import com.philips.cdp.di.ecs.MockInputValidator;
import com.philips.cdp.di.ecs.StaticBlock;
import com.philips.cdp.di.ecs.TestUtil;
import com.philips.cdp.di.ecs.error.ECSError;
import com.philips.cdp.di.ecs.integration.ECSCallback;
import com.philips.cdp.di.ecs.model.order.Orders;
import com.philips.cdp.di.ecs.model.order.OrdersData;
import com.philips.cdp.di.ecs.model.payment.PaymentMethods;
import com.philips.cdp.di.ecs.model.products.ECSProduct;
import com.philips.cdp.di.ecs.model.retailers.WebResults;
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
import java.util.HashMap;
import java.util.Map;

import static androidx.test.platform.app.InstrumentationRegistry.getInstrumentation;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;

@RunWith(RobolectricTestRunner.class)
public class GetPaymentsTest {

    private Context mContext;


    MockECSServices mockECSServices;
    ECSServices ecsServices;


    private AppInfra appInfra;


    @Mock
    RestInterface mockRestInterface;

    MockGetPaymentsRequest mockGetPaymentsRequest;

    ECSCallback<PaymentMethods, Exception> ecsCallback;
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

        ecsCallback = new ECSCallback<PaymentMethods, Exception>() {
            @Override
            public void onResponse(PaymentMethods result) {

            }

            @Override
            public void onFailure(Exception error, ECSError ecsError) {

            }
        };

        mockGetPaymentsRequest = new MockGetPaymentsRequest("GetPaymentsSuccess.json",ecsCallback);

    }



    @Test
    public void getPaymentSuccess() {
        mockInputValidator.setJsonFileName("GetPaymentsSuccess.json");
        mockECSServices.getPayments(new ECSCallback<PaymentMethods, Exception>() {
            @Override
            public void onResponse(PaymentMethods result) {
                assertNotNull(result);
                assertNotNull(result.getPayments().size()>0);
                //test passed

            }

            @Override
            public void onFailure(Exception error, ECSError ecsError) {
                assertTrue(false);
                //test failed
            }
        });
    }

    //doing for kotlin order detail file

    @Test
    public void getOrderHistrorySuccess() {
        mockInputValidator.setJsonFileName("GetOrderHistorySuccess.json");
        mockECSServices.getOrderHistory(1, new ECSCallback<OrdersData, Exception>() {
            @Override
            public void onResponse(OrdersData result) {
                assertNotNull(result);
            }

            @Override
            public void onFailure(Exception error, ECSError ecsError) {
                assertTrue(false);
            }
        });
    }

    @Test
    public void getOrderDetailSuccess() {
        mockInputValidator.setJsonFileName("GetOrderDetailSuccess.json");
        Orders orders = new Orders();
        mockECSServices.getOrderDetail(orders, new ECSCallback<Orders, Exception>() {
            @Override
            public void onResponse(Orders result) {
                assertNotNull(result);
            }

            @Override
            public void onFailure(Exception error, ECSError ecsError) {
                assertTrue(false);
            }
        });
    }

    @Test
    public void getRetailerSuccess() {
        mockInputValidator.setJsonFileName("GetRetailerInfoSuccess.json");
        ECSProduct product = new ECSProduct();
        product.setCode("1234");
        mockECSServices.getRetailers(product, new ECSCallback<WebResults, Exception>() {
            @Override
            public void onResponse(WebResults result) {
                assertNotNull(result);
            }

            @Override
            public void onFailure(Exception error, ECSError ecsError) {
                assertTrue(false);
            }
        });
    }


    @Test
    public void getPaymentFailure() {
        mockInputValidator.setJsonFileName("EmptyJson.json");
        mockECSServices.getPayments(new ECSCallback<PaymentMethods, Exception>() {
            @Override
            public void onResponse(PaymentMethods result) {
                assertTrue(true);
            }

            @Override
            public void onFailure(Exception error, ECSError ecsError) {
                assertEquals(12999, ecsError);
            }
        });
    }

    @Test
    public void isValidURL() {
        System.out.println("print the URL"+mockGetPaymentsRequest.getURL());
        String excepted = StaticBlock.getBaseURL()+"pilcommercewebservices"+"/v2/"+StaticBlock.getSiteID()+"/users/current/paymentdetails?fields=FULL&lang="+StaticBlock.getLocale();
        Assert.assertEquals(excepted,mockGetPaymentsRequest.getURL());
    }


    @Test
    public void isValidGetRequest() {
        Assert.assertEquals(0,mockGetPaymentsRequest.getMethod());
    }

    @Test
    public void isValidHeader() {

        Map<String, String> expectedMap = new HashMap<String, String>();
        expectedMap.put("Authorization", "Bearer " + "acceesstoken");

        Map<String, String> actual = mockGetPaymentsRequest.getHeader();

        assertTrue(expectedMap.equals(actual));
    }

    @Test
    public void isValidParam() {
        assertNull(mockGetPaymentsRequest.getParams());
    }


    @Test
    public void verifyOnResponseError() {
        ECSCallback<PaymentMethods, Exception> spy1 = Mockito.spy(ecsCallback);
        mockGetPaymentsRequest = new MockGetPaymentsRequest("GetPaymentsSuccess.json",spy1);
        VolleyError volleyError = new NoConnectionError();
        mockGetPaymentsRequest.onErrorResponse(volleyError);
        Mockito.verify(spy1).onFailure(any(Exception.class),any(ECSError.class));

    }

    @Test
    public void verifyOnResponseSuccess() {

        ECSCallback<PaymentMethods, Exception> spy1 = Mockito.spy(ecsCallback);
        mockGetPaymentsRequest = new MockGetPaymentsRequest("GetPaymentsSuccess.json",spy1);

        JSONObject jsonObject = getJsonObject("GetPaymentsSuccess.json");

        mockGetPaymentsRequest.onResponse(jsonObject);

        Mockito.verify(spy1).onResponse(any(PaymentMethods.class));

    }


    JSONObject getJsonObject(String jsonfileName){

        JSONObject result = null;
        InputStream in = getClass().getClassLoader().getResourceAsStream(jsonfileName);
        String jsonString = TestUtil.loadJSONFromFile(in);
        try {
            return new JSONObject(jsonString);
        } catch (JSONException e) {
            return null;
        }
    }


    @Test
    public void assertResponseSuccessListenerNotNull() {
        assertNotNull(mockGetPaymentsRequest.getJSONSuccessResponseListener());
    }
}
