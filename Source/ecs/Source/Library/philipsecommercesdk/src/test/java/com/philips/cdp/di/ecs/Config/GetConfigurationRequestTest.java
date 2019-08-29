package com.philips.cdp.di.ecs.Config;

import android.content.Context;

import com.philips.cdp.di.ecs.ECSServices;
import com.philips.cdp.di.ecs.MockECSServices;
import com.philips.cdp.di.ecs.StaticBlock;
import com.philips.cdp.di.ecs.integration.ECSCallback;
import com.philips.cdp.di.ecs.model.config.HybrisConfigResponse;
import com.philips.platform.appinfra.AppInfra;
import com.philips.platform.appinfra.rest.RestInterface;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.robolectric.RobolectricTestRunner;

import static androidx.test.platform.app.InstrumentationRegistry.getInstrumentation;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

@RunWith(RobolectricTestRunner.class)
public class GetConfigurationRequestTest {
    private Context mContext;


    MockECSServices mockECSServices;
    ECSServices ecsServices;


    private AppInfra appInfra;


    @Mock
    RestInterface mockRestInterface;

    MockGetConfigurationRequest mockGetConfigurationRequest;

    @Before
    public void setUp() throws Exception {

        mContext = getInstrumentation().getContext();
        appInfra = new AppInfra.Builder().setRestInterface(mockRestInterface).build(mContext);
        appInfra.getServiceDiscovery().setHomeCountry("DE");

        mockECSServices = new MockECSServices("", appInfra);
        ecsServices = new ECSServices("",appInfra);

        StaticBlock.initialize();

        //Creating config request object
        mockGetConfigurationRequest = new MockGetConfigurationRequest("GetConfigSuccess.json", new ECSCallback<HybrisConfigResponse, Exception>() {
            @Override
            public void onResponse(HybrisConfigResponse result) {

            }

            @Override
            public void onFailure(Exception error, int errorCode) {

            }
        });
    }


    @Test
    public void getConfigurationRequestSuccess() {
        mockECSServices.setJsonFileName("GetConfigSuccess.json");
        mockECSServices.getECSConfig(new ECSCallback<HybrisConfigResponse, Exception>() {
            @Override
            public void onResponse(HybrisConfigResponse result) {
                assertNotNull(result);
                assertNotNull(result.getSiteId());
                assertNotNull(result.getRootCategory());
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
    public void getConfigurationRequestFailure() {
        mockECSServices.setJsonFileName("GetConfigFailure.json");
        mockECSServices.getECSConfig(new ECSCallback<HybrisConfigResponse, Exception>() {
            @Override
            public void onResponse(HybrisConfigResponse result) {
                assertTrue(false);
                //test case failed

            }

            @Override
            public void onFailure(Exception error, int errorCode) {
                assertTrue(true);
                //test case passed
            }
        });;

    }

    @Test
    public void isValidURL() {
        String excepted = StaticBlock.getBaseURL()+"pilcommercewebservices"+"/v2/inAppConfig/"+StaticBlock.getLocale()+"/"+StaticBlock.getPropositionID();
        Assert.assertEquals(excepted,mockGetConfigurationRequest.getURL());
    }
}
