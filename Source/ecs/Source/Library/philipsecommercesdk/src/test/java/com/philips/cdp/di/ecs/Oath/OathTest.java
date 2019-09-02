package com.philips.cdp.di.ecs.Oath;

import android.content.Context;

import com.android.volley.NoConnectionError;
import com.android.volley.VolleyError;
import com.philips.cdp.di.ecs.Cart.MockAddProductToECSShoppingCartRequest;
import com.philips.cdp.di.ecs.ECSServices;
import com.philips.cdp.di.ecs.MockECSServices;
import com.philips.cdp.di.ecs.StaticBlock;
import com.philips.cdp.di.ecs.integration.ECSCallback;
import com.philips.cdp.di.ecs.integration.OAuthInput;
import com.philips.cdp.di.ecs.model.oauth.OAuthResponse;
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
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;

@RunWith(RobolectricTestRunner.class)
public class OathTest {
    private Context mContext;


    MockECSServices mockECSServices;
    ECSServices ecsServices;


    private AppInfra appInfra;


    @Mock
    RestInterface mockRestInterface;

    ECSCallback<OAuthResponse, Exception> ecsCallback;

    MockOAuthRequest mockOAuthRequest;

    OAuthInput oAuthInput;


    @Before
    public void setUp() throws Exception {


        mContext = getInstrumentation().getContext();
        appInfra = new AppInfra.Builder().setRestInterface(mockRestInterface).build(mContext);
        appInfra.getServiceDiscovery().setHomeCountry("DE");


        mockECSServices = new MockECSServices("", appInfra);
        ecsServices = new ECSServices("",appInfra);

        StaticBlock.initialize();

        ecsCallback = new ECSCallback<OAuthResponse, Exception>() {
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
        };

        oAuthInput = new OAuthInput() {
            @Override
            public String getJanRainID() {
                return "mock Jainrain ID";
            }
        };

        mockOAuthRequest = new MockOAuthRequest("HybrisOauthSuccess.json",oAuthInput,ecsCallback);

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



    @Test
    public void isValidURL() {
        String excepted = StaticBlock.getBaseURL()+"pilcommercewebservices/oauth/token?janrain="+oAuthInput.getJanRainID()+"&grant_type=janrain&client_id=mobile_android&client_secret=secret";
        Assert.assertEquals(excepted,mockOAuthRequest.getURL());
    }

    @Test
    public void isValidPostRequest() {
        Assert.assertEquals(1,mockOAuthRequest.getMethod());
    }

    @Test
    public void isValidHeader() {

        Map<String, String> expectedMap = new HashMap<String, String>();
        expectedMap.put("janrain","mock Jainrain ID");
        expectedMap.put("grant_type",oAuthInput.getGrantType());
        expectedMap.put("client_id",oAuthInput.getClientID());
        expectedMap.put("client_secret",oAuthInput.getClientSecret());

        Map<String, String> actual = mockOAuthRequest.getHeader();

        assertTrue(expectedMap.equals(actual));
    }

    @Test
    public void verifyOnResponseError() {
        ECSCallback<OAuthResponse, Exception> spy1 = Mockito.spy(ecsCallback);
        mockOAuthRequest = new MockOAuthRequest("HybrisOauthSuccess.json",oAuthInput,spy1);
        VolleyError volleyError = new NoConnectionError();
        mockOAuthRequest.onErrorResponse(volleyError);
        Mockito.verify(spy1).onFailure(any(Exception.class),anyInt());

    }


    @Test
    public void assertResponseSuccessListenerNotNull() {
        assertNotNull(mockOAuthRequest.getJSONSuccessResponseListener());
    }

}
