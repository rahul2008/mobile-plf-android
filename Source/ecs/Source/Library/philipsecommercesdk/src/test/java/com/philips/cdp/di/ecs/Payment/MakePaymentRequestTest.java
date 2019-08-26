package com.philips.cdp.di.ecs.Payment;


import android.content.Context;

import com.philips.cdp.di.ecs.ECSServices;
import com.philips.cdp.di.ecs.MockECSServices;
import com.philips.cdp.di.ecs.integration.ECSCallback;
import com.philips.cdp.di.ecs.model.address.Addresses;
import com.philips.cdp.di.ecs.model.orders.OrderDetail;
import com.philips.cdp.di.ecs.model.payment.MakePaymentData;
import com.philips.platform.appinfra.AppInfra;
import com.philips.platform.appinfra.rest.RestInterface;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.robolectric.RobolectricTestRunner;

import static androidx.test.platform.app.InstrumentationRegistry.getInstrumentation;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

@RunWith(RobolectricTestRunner.class)

public class MakePaymentRequestTest {

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
            public void onFailure(Exception error, int errorCode) {
                assertTrue(false);
                //test failed
            }
        });
    }

}
