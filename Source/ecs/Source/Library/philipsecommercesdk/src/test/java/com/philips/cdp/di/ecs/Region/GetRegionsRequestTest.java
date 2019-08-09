package com.philips.cdp.di.ecs.Region;


import android.content.Context;

import com.philips.cdp.di.ecs.ECSServices;
import com.philips.cdp.di.ecs.MockECSServices;
import com.philips.cdp.di.ecs.integration.ECSCallback;
import com.philips.cdp.di.ecs.model.region.RegionsList;
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
public class GetRegionsRequestTest {

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
    public void getRegionRequestSuccess() {
        mockECSServices.setJsonFileName("GetRegionsSuccess.json");
        mockECSServices.getRegions(new ECSCallback<RegionsList, Exception>() {
            @Override
            public void onResponse(RegionsList result) {
                assertNotNull(result);
                assertNotNull(result.getRegions().size()>0);

                //test case passed
            }

            @Override
            public void onFailure(Exception error, String detailErrorMessage, int errorCode) {
                assertTrue(false);
                //test case failed
            }
        });
    }

    @Test
    public void getRegionRequestFailure() {
        mockECSServices.setJsonFileName("EmptyJson.json");
        mockECSServices.getRegions(new ECSCallback<RegionsList, Exception>() {
            @Override
            public void onResponse(RegionsList result) {
                assertTrue(false);

                //test case failed
            }

            @Override
            public void onFailure(Exception error, String detailErrorMessage, int errorCode) {
                assertTrue(true);
                //test case passed
            }
        });
    }
}
