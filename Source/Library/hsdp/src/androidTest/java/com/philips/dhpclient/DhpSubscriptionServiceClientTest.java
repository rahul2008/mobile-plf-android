
package com.philips.dhpclient;

import android.support.multidex.MultiDex;
import android.test.InstrumentationTestCase;

import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by 310243576 on 8/21/2016.
 */
public class DhpSubscriptionServiceClientTest extends InstrumentationTestCase{

    DhpSubscriptionServiceClient mDhpSubscriptionServiceClient;
    DhpSubscriptionServiceClient.DhpTermsAndConditionsResponse mDhpTermsAndConditionsResponse;
    DhpSubscriptionServiceClient.DhpTermsAndConditionsResponse mDhpTermsAndConditionsResponse1;

    @Before
    public void setUp() throws Exception {
        MultiDex.install(getInstrumentation().getTargetContext());
//        MockitoAnnotations.initMocks(this);
        super.setUp();
        System.setProperty("dexmaker.dexcache", getInstrumentation().getTargetContext().getCacheDir().getPath());

        DhpApiClientConfiguration dhpApiClientConfiguration = new DhpApiClientConfiguration("apiBaseUrl","dhpApplicationName","signingKey","signingSecret");
    mDhpSubscriptionServiceClient = new DhpSubscriptionServiceClient(dhpApiClientConfiguration);
        mDhpTermsAndConditionsResponse = new DhpSubscriptionServiceClient.DhpTermsAndConditionsResponse("responseCode","acceptedTermsVersion");
        mDhpTermsAndConditionsResponse1 = new DhpSubscriptionServiceClient.DhpTermsAndConditionsResponse("responseCode","acceptedTermsVersion");
    }

    @Test
    public void testDhpSubscriptionServiceClient(){
        mDhpSubscriptionServiceClient.subscribe("dhpUserId","accessToken");
        mDhpSubscriptionServiceClient.closeAccount("dhpUserId",true,"accessToken");
        assertNotNull(mDhpSubscriptionServiceClient);

    }
    @Test
    public void testDhpTermsAndConditonsResponse(){
        assertNotNull(mDhpTermsAndConditionsResponse);
        assertTrue(mDhpTermsAndConditionsResponse.equals(mDhpTermsAndConditionsResponse));
        assertTrue(mDhpTermsAndConditionsResponse.equals(mDhpTermsAndConditionsResponse1));
        assertFalse(mDhpTermsAndConditionsResponse.equals(mDhpSubscriptionServiceClient));
        assertFalse(mDhpTermsAndConditionsResponse.equals(null));
        assertNotNull(mDhpTermsAndConditionsResponse.hashCode());
        assertNotNull(mDhpTermsAndConditionsResponse.toString());
    }
    public void testGetLastAcceptedTermsAndConditions(){
        Method method = null;
                Map<String, Object> responseMap = new HashMap<String, Object>();
            responseMap.put("200","\\.sample");
        try {
            method = DhpSubscriptionServiceClient.class.getDeclaredMethod("getLastAcceptedTermsAndConditions", Map.class);
            method.setAccessible(true);
            method.invoke(mDhpSubscriptionServiceClient,responseMap);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }
}
