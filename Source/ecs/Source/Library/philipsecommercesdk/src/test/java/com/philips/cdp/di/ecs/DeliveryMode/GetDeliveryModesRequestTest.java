package com.philips.cdp.di.ecs.DeliveryMode;

import android.content.Context;

import com.android.volley.NoConnectionError;
import com.android.volley.VolleyError;
import com.philips.cdp.di.ecs.ECSServices;
import com.philips.cdp.di.ecs.MockECSServices;
import com.philips.cdp.di.ecs.StaticBlock;
import com.philips.cdp.di.ecs.error.ECSError;
import com.philips.cdp.di.ecs.integration.ECSCallback;
import com.philips.cdp.di.ecs.model.address.GetDeliveryModes;
import com.philips.platform.appinfra.AppInfra;
import com.philips.platform.appinfra.rest.RestInterface;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.robolectric.RobolectricTestRunner;

import static androidx.test.platform.app.InstrumentationRegistry.getInstrumentation;
import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;

@RunWith(RobolectricTestRunner.class)
public class GetDeliveryModesRequestTest {

    private Context mContext;


    MockECSServices mockECSServices;
    ECSServices ecsServices;


    private AppInfra appInfra;


    @Mock
    RestInterface mockRestInterface;

    MockDeliveryModesRequest mockDeliveryModesRequest;

    ECSCallback<GetDeliveryModes, Exception> ecsCallback;

    @Before
    public void setUp() throws Exception {

        mContext = getInstrumentation().getContext();
        appInfra = new AppInfra.Builder().setRestInterface(mockRestInterface).build(mContext);
        appInfra.getServiceDiscovery().setHomeCountry("DE");

        mockECSServices = new MockECSServices("", appInfra);
        ecsServices = new ECSServices("",appInfra);

        StaticBlock.initialize();

        ecsCallback = new ECSCallback<GetDeliveryModes, Exception>() {
            @Override
            public void onResponse(GetDeliveryModes result) {

            }

            @Override
            public void onFailure(Exception error, ECSError ecsError) {

            }
        };

        mockDeliveryModesRequest = new MockDeliveryModesRequest(ecsCallback,"deliverymodes.json");
    }

    @Test
    public void getDeliveryModesSuccess() {
        mockECSServices.setJsonFileName("deliverymodes.json");
        mockECSServices.getDeliveryModes(new ECSCallback<GetDeliveryModes, Exception>() {
            @Override
            public void onResponse(GetDeliveryModes result) {
                assertNotNull(result);
                assertNotNull(result.getDeliveryModes());
                assertNotEquals(0,result.getDeliveryModes().size());
                //  test case passed
            }

            @Override
            public void onFailure(Exception error, ECSError ecsError) {
                assertTrue(false);
                //  test case failed
            }
        });
    }

    @Test
    public void getDeliveryModesFailureEmpty() {
        mockECSServices.setJsonFileName("EmptyJson.json");
        mockECSServices.getDeliveryModes(new ECSCallback<GetDeliveryModes, Exception>() {
            @Override
            public void onResponse(GetDeliveryModes result) {
                assertNull(result.getDeliveryModes());
                //  test case failed
            }

            @Override
            public void onFailure(Exception error, ECSError ecsError) {
                assertTrue(false);
                //  test case passed
            }
        });
    }

    @Test
    public void isValidURL() {
        String excepted = StaticBlock.getBaseURL()+"pilcommercewebservices"+"/v2/"+StaticBlock.getSiteID()+"/users/current/carts/current/deliverymodes?fields=FULL&lang="+StaticBlock.getLocale();
        Assert.assertEquals(excepted,mockDeliveryModesRequest.getURL());
    }

    @Test
    public void isValidGetRequest() {
        Assert.assertEquals(0,mockDeliveryModesRequest.getMethod());
    }


    @Test
    public void verifyOnResponseError() {
        ECSCallback<GetDeliveryModes, Exception> spy1 = Mockito.spy(ecsCallback);
        mockDeliveryModesRequest = new MockDeliveryModesRequest(spy1,"deliverymodes.json");
        VolleyError volleyError = new NoConnectionError();
        mockDeliveryModesRequest.onErrorResponse(volleyError);
        Mockito.verify(spy1).onFailure(any(Exception.class),anyInt());

    }


    @Test
    public void assertResponseSuccessListenerNotNull() {
        assertNotNull(mockDeliveryModesRequest.getJSONSuccessResponseListener());
    }
}