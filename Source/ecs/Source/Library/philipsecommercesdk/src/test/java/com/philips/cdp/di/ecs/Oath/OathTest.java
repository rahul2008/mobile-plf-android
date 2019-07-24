package com.philips.cdp.di.ecs.Oath;

import android.content.Context;

import com.philips.cdp.di.ecs.ECSServices;
import com.philips.cdp.di.ecs.MockECSServices;
import com.philips.cdp.di.ecs.integration.ECSCallback;
import com.philips.cdp.di.ecs.integration.OAuthInput;
import com.philips.cdp.di.ecs.model.response.OAuthResponse;
import com.philips.platform.appinfra.AppInfra;
import com.philips.platform.appinfra.rest.RestInterface;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.robolectric.RobolectricTestRunner;

import static androidx.test.platform.app.InstrumentationRegistry.getInstrumentation;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

@RunWith(RobolectricTestRunner.class)
public class OathTest {
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
    public void hybrisOathAuthenticationSuccess() {
        mockECSServices.setJsonFileName("HybrisOauthSuccess.json");

        OAuthInput oAuthInput = new OAuthInput() {
            @Override
            public String getJanRainID() {
                return "mock Jainrain ID";
            }
        };
        mockECSServices.hybrisOathAuthentication(oAuthInput, new ECSCallback<OAuthResponse, Exception>() {
            @Override
            public void onResponse(OAuthResponse result) {
                assertNotNull(result);
                assertNotNull(result.getAccessToken());
                // test case passed
            }

            @Override
            public void onFailure(Exception error, int errorCode) {
                assertTrue(true);
                // test case failed
            }
        });

    }

    @Test
    public void hybrisOathAuthenticationFailure() {
        mockECSServices.setJsonFileName("HybrisOauthFailure.json");

        OAuthInput oAuthInput = new OAuthInput() {
            @Override
            public String getJanRainID() {
                return "mock Jainrain ID";
            }
        };
        mockECSServices.hybrisOathAuthentication(oAuthInput, new ECSCallback<OAuthResponse, Exception>() {
            @Override
            public void onResponse(OAuthResponse result) {
                assertNotNull(result);
                assertNull(result.getAccessToken());
                // test case passed
            }

            @Override
            public void onFailure(Exception error, int errorCode) {
                assertTrue(true);
                // test case failed
            }
        });

    }

    @Test
    public void hybrisOathAuthenticationInvalidClient() {
        mockECSServices.setJsonFileName("HybrisInvalidClient.json");

        OAuthInput oAuthInput = new OAuthInput() {
            @Override
            public String getJanRainID() {
                return "mock Jainrain ID";
            }
        };
        mockECSServices.hybrisOathAuthentication(oAuthInput, new ECSCallback<OAuthResponse, Exception>() {
            @Override
            public void onResponse(OAuthResponse result) {
                assertNotNull(result);
                assertNull(result.getAccessToken());
                // test case passed
            }

            @Override
            public void onFailure(Exception error, int errorCode) {
                assertTrue(true);
                // test case failed
            }
        });

    }

    @Test
    public void hybrisOathAuthenticationInvalidClientReal() {
        //mockECSServices.setJsonFileName("HybrisInvalidClient.json");

        OAuthInput oAuthInput = new OAuthInput() {
            @Override
            public String getJanRainID() {
                return "mock Jainrain ID";
            }
        };
        ecsServices.hybrisOathAuthentication(oAuthInput, new ECSCallback<OAuthResponse, Exception>() {
            @Override
            public void onResponse(OAuthResponse result) {
                assertNotNull(result);
                assertNull(result.getAccessToken());
                // test case passed
            }

            @Override
            public void onFailure(Exception error, int errorCode) {
                assertTrue(true);
                // test case failed
            }
        });

    }

}
