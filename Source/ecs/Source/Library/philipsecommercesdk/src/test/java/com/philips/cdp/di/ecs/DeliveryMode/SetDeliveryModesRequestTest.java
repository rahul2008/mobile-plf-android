package com.philips.cdp.di.ecs.DeliveryMode;

import android.content.Context;

import com.philips.cdp.di.ecs.ECSServices;
import com.philips.cdp.di.ecs.MockECSServices;
import com.philips.cdp.di.ecs.StaticBlock;
import com.philips.cdp.di.ecs.integration.ECSCallback;
import com.philips.platform.appinfra.AppInfra;
import com.philips.platform.appinfra.rest.RestInterface;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.robolectric.RobolectricTestRunner;

import static androidx.test.platform.app.InstrumentationRegistry.getInstrumentation;
import static org.junit.Assert.*;

@RunWith(RobolectricTestRunner.class)
public class SetDeliveryModesRequestTest {

    private Context mContext;


    MockECSServices mockECSServices;
    ECSServices ecsServices;


    private AppInfra appInfra;


    @Mock
    RestInterface mockRestInterface;

    MockSetDeliveryModesRequest mockSetDeliveryModesRequest;

    @Before
    public void setUp() throws Exception {

        mContext = getInstrumentation().getContext();
        appInfra = new AppInfra.Builder().setRestInterface(mockRestInterface).build(mContext);
        appInfra.getServiceDiscovery().setHomeCountry("DE");

        mockECSServices = new MockECSServices("", appInfra);
        ecsServices = new ECSServices("",appInfra);

        StaticBlock.initialize();

        mockSetDeliveryModesRequest = new MockSetDeliveryModesRequest("1234", new ECSCallback<Boolean, Exception>() {
            @Override
            public void onResponse(Boolean result) {

            }

            @Override
            public void onFailure(Exception error, int errorCode) {

            }
        },"SetDeliveryModeFailure.json");
    }

    @Test
    public void setDeliveryModeSuccess() {

        mockECSServices.setJsonFileName("EmptyString.json");
        mockECSServices.setDeliveryMode("UPS_PARCEL", new ECSCallback<Boolean, Exception>() {
            @Override
            public void onResponse(Boolean result) {
                assertTrue(result);
                //test case passed
            }

            @Override
            public void onFailure(Exception error, int errorCode) {
                assertTrue(false);
                //test case failed
            }
        });
    }

    @Test
    public void setDeliveryModeFailure() {

        mockECSServices.setJsonFileName("SetDeliveryModeFailure.json");
        mockECSServices.setDeliveryMode("UPS_PARCEL", new ECSCallback<Boolean, Exception>() {
            @Override
            public void onResponse(Boolean result) {
                assertTrue(false);
                //test case failed
            }

            @Override
            public void onFailure(Exception error, int errorCode) {
                assertTrue(true);
                //test case passed

            }
        });
    }

    @Test
    public void isValidURL() {
        String excepted = StaticBlock.getBaseURL()+"pilcommercewebservices"+"/v2/"+StaticBlock.getSiteID()+"/users/current/carts/current/deliverymode?fields=FULL&lang="+StaticBlock.getLocale();
        Assert.assertEquals(excepted,mockSetDeliveryModesRequest.getURL());
    }
}