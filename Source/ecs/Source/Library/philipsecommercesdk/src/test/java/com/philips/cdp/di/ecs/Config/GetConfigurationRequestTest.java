package com.philips.cdp.di.ecs.Config;

import android.content.Context;

import com.android.volley.NoConnectionError;
import com.android.volley.VolleyError;
import com.philips.cdp.di.ecs.ECSServices;
import com.philips.cdp.di.ecs.MockECSServices;
import com.philips.cdp.di.ecs.StaticBlock;
import com.philips.cdp.di.ecs.error.ECSError;
import com.philips.cdp.di.ecs.integration.ECSCallback;
import com.philips.cdp.di.ecs.model.config.HybrisConfigResponse;
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
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;

@RunWith(RobolectricTestRunner.class)
public class GetConfigurationRequestTest {
    private Context mContext;


    MockECSServices mockECSServices;
    ECSServices ecsServices;


    private AppInfra appInfra;


    @Mock
    RestInterface mockRestInterface;

    MockGetConfigurationRequest mockGetConfigurationRequest;

    ECSCallback<HybrisConfigResponse, Exception> ecsCallback;

    @Before
    public void setUp() throws Exception {

        mContext = getInstrumentation().getContext();
        appInfra = new AppInfra.Builder().setRestInterface(mockRestInterface).build(mContext);
        appInfra.getServiceDiscovery().setHomeCountry("DE");

        mockECSServices = new MockECSServices("", appInfra);
        ecsServices = new ECSServices("",appInfra);

        StaticBlock.initialize();

        ecsCallback = new ECSCallback<HybrisConfigResponse, Exception>() {
            @Override
            public void onResponse(HybrisConfigResponse result) {

            }

            @Override
            public void onFailure(Exception error, ECSError ecsError) {

            }
        };

        //Creating config request object
        mockGetConfigurationRequest = new MockGetConfigurationRequest("GetConfigSuccess.json", ecsCallback);
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
            public void onFailure(Exception error, ECSError ecsError) {
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
            public void onFailure(Exception error, ECSError ecsError) {
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

    @Test
    public void isValidGetRequest() {
        Assert.assertEquals(0,mockGetConfigurationRequest.getMethod());
    }


    @Test
    public void verifyOnResponseError() {
        ECSCallback<HybrisConfigResponse, Exception> spy1 = Mockito.spy(ecsCallback);
        mockGetConfigurationRequest = new MockGetConfigurationRequest("GetConfigSuccess.json", spy1);
        VolleyError volleyError = new NoConnectionError();
        mockGetConfigurationRequest.onErrorResponse(volleyError);
        Mockito.verify(spy1).onFailure(any(Exception.class),anyInt());

    }


    @Test
    public void assertResponseSuccessListenerNotNull() {
        assertNotNull(mockGetConfigurationRequest.getJSONSuccessResponseListener());
    }
}
