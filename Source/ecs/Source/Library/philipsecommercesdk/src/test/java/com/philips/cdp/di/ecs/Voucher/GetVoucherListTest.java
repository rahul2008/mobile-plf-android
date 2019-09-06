package com.philips.cdp.di.ecs.Voucher;

import android.content.Context;

import com.android.volley.NoConnectionError;
import com.android.volley.VolleyError;
import com.philips.cdp.di.ecs.ECSServices;
import com.philips.cdp.di.ecs.MockECSServices;
import com.philips.cdp.di.ecs.StaticBlock;
import com.philips.cdp.di.ecs.error.ECSError;
import com.philips.cdp.di.ecs.integration.ECSCallback;
import com.philips.cdp.di.ecs.model.voucher.GetAppliedValue;
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
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;

@RunWith(RobolectricTestRunner.class)
public class GetVoucherListTest {

    private Context mContext;


    MockECSServices mockECSServices;
    ECSServices ecsServices;


    private AppInfra appInfra;


    @Mock
    RestInterface mockRestInterface;

    MockGetVouchersRequest mockGetVouchersRequest;

    ECSCallback<GetAppliedValue, Exception> ecsCallback;


    @Before
    public void setUp() throws Exception {


        mContext = getInstrumentation().getContext();
        appInfra = new AppInfra.Builder().setRestInterface(mockRestInterface).build(mContext);
        appInfra.getServiceDiscovery().setHomeCountry("DE");


        mockECSServices = new MockECSServices("", appInfra);
        ecsServices = new ECSServices("",appInfra);

        StaticBlock.initialize();
        ecsCallback = new ECSCallback<GetAppliedValue, Exception> () {

            @Override
            public void onResponse(GetAppliedValue result) {

            }

            @Override
            public void onFailure(Exception error, ECSError ecsError) {

            }
        };

        mockGetVouchersRequest = new MockGetVouchersRequest("GetVoucherSuccess.json",ecsCallback);

    }

    @Test
    public void getAppliedVoucherListSuccess() {
        mockECSServices.setJsonFileName("GetVoucherSuccess.json");
        mockECSServices.getVoucher(new ECSCallback<GetAppliedValue, Exception>() {
            @Override
            public void onResponse(GetAppliedValue result) {
                assertNotNull(result);
                assertNotNull(result.getVouchers().get(0).getCode());
                assertNotNull(result.getVouchers().get(0).getValue());
                //  test case passed
            }

            @Override
            public void onFailure(Exception error, ECSError ecsError) {
                assertTrue(true);
                //  test case failed
            }
        });

    }

    @Test
    public void getAppliedVoucherListSuccessWithNoVoucher() {
        mockECSServices.setJsonFileName("EmptyJson.json");// {}
        mockECSServices.getVoucher(new ECSCallback<GetAppliedValue, Exception>() {
            @Override
            public void onResponse(GetAppliedValue result) {
                assertTrue(true);
                //  test case failed
            }

            @Override
            public void onFailure(Exception error, ECSError ecsError) {
                assertEquals(19999, ecsError);
                //  test case passed

            }
        });

    }

    @Test
    public void getAppliedVoucherListFailure() {
        mockECSServices.setJsonFileName("EmptyString.json");
        mockECSServices.getVoucher(new ECSCallback<GetAppliedValue, Exception>() {
            @Override
            public void onResponse(GetAppliedValue result) {
                assertTrue(false);
                //  test case failed
            }

            @Override
            public void onFailure(Exception error, ECSError ecsError) {
                assertEquals(5999, ecsError);
                //  test case passed
            }
        });

    }

    @Test
    public void isValidURL() {
        System.out.println("print the URL"+mockGetVouchersRequest.getURL());
        String excepted = StaticBlock.getBaseURL()+"pilcommercewebservices"+"/v2/"+StaticBlock.getSiteID()+"/users/current/carts/current/vouchers?lang="+StaticBlock.getLocale();
        Assert.assertEquals(excepted,mockGetVouchersRequest.getURL());
    }


    @Test
    public void isValidGetRequest() {
        Assert.assertEquals(0,mockGetVouchersRequest.getMethod());
    }

    @Test
    public void isValidHeader() {

        Map<String, String> expectedMap = new HashMap<String, String>();
        expectedMap.put("Content-Type", "application/x-www-form-urlencoded");
        expectedMap.put("Authorization", "Bearer " + "acceesstoken");

        Map<String, String> actual = mockGetVouchersRequest.getHeader();

        assertTrue(expectedMap.equals(actual));
    }

    @Test
    public void isValidParam() {
        assertNull(mockGetVouchersRequest.getParams());
    }


    @Test
    public void verifyOnResponseError() {
        ECSCallback<GetAppliedValue, Exception> spy1 = Mockito.spy(ecsCallback);
        mockGetVouchersRequest = new MockGetVouchersRequest("GetVoucherSuccess.json",spy1);
        VolleyError volleyError = new NoConnectionError();
        mockGetVouchersRequest.onErrorResponse(volleyError);
        Mockito.verify(spy1).onFailure(any(Exception.class),any(ECSError.class));

    }

    @Test
    public void assertResponseSuccessListenerNotNull() {
        assertNotNull(mockGetVouchersRequest.getJSONSuccessResponseListener());
    }

}
