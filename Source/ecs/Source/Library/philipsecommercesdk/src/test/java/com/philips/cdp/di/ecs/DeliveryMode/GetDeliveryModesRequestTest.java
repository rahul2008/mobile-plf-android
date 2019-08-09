package com.philips.cdp.di.ecs.DeliveryMode;

import android.content.Context;

import com.philips.cdp.di.ecs.ECSServices;
import com.philips.cdp.di.ecs.MockECSServices;
import com.philips.cdp.di.ecs.integration.ECSCallback;
import com.philips.cdp.di.ecs.model.address.GetDeliveryModes;
import com.philips.platform.appinfra.AppInfra;
import com.philips.platform.appinfra.rest.RestInterface;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.robolectric.RobolectricTestRunner;

import static androidx.test.platform.app.InstrumentationRegistry.getInstrumentation;
import static org.junit.Assert.*;

@RunWith(RobolectricTestRunner.class)
public class GetDeliveryModesRequestTest {

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
            public void onFailure(Exception error, String detailErrorMessage, int errorCode) {
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
                assertTrue(false);
                //  test case failed
            }

            @Override
            public void onFailure(Exception error, String detailErrorMessage, int errorCode) {
                assertTrue(true);
                //  test case passed
            }
        });
    }

}