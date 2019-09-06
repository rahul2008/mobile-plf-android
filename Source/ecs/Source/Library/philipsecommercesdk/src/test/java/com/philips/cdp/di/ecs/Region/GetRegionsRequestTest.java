package com.philips.cdp.di.ecs.Region;


import android.content.Context;

import com.android.volley.NoConnectionError;
import com.android.volley.VolleyError;
import com.philips.cdp.di.ecs.ECSServices;
import com.philips.cdp.di.ecs.MockECSServices;
import com.philips.cdp.di.ecs.MockInputValidator;
import com.philips.cdp.di.ecs.StaticBlock;
import com.philips.cdp.di.ecs.TestUtil;
import com.philips.cdp.di.ecs.error.ECSError;
import com.philips.cdp.di.ecs.integration.ECSCallback;
import com.philips.cdp.di.ecs.model.region.RegionsList;
import com.philips.platform.appinfra.AppInfra;
import com.philips.platform.appinfra.rest.RestInterface;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.robolectric.RobolectricTestRunner;

import java.io.InputStream;

import static androidx.test.platform.app.InstrumentationRegistry.getInstrumentation;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;

@RunWith(RobolectricTestRunner.class)
public class GetRegionsRequestTest {

    private Context mContext;


    MockECSServices mockECSServices;
    ECSServices ecsServices;


    private AppInfra appInfra;


    @Mock
    RestInterface mockRestInterface;

    MockGetRegionsRequest mockGetRegionsRequest;

    ECSCallback<RegionsList, Exception> ecsCallback;
    private MockInputValidator mockInputValidator;


    @Before
    public void setUp() throws Exception {

        mContext = getInstrumentation().getContext();
        appInfra = new AppInfra.Builder().setRestInterface(mockRestInterface).build(mContext);
        appInfra.getServiceDiscovery().setHomeCountry("DE");

        mockECSServices = new MockECSServices("", appInfra);
        ecsServices = new ECSServices("",appInfra);

        mockInputValidator = new MockInputValidator();

        StaticBlock.initialize();

        ecsCallback = new ECSCallback<RegionsList, Exception>() {
            @Override
            public void onResponse(RegionsList result) {

            }

            @Override
            public void onFailure(Exception error, ECSError ecsError) {

            }
        };

        mockGetRegionsRequest = new MockGetRegionsRequest("GetRegionsSuccess.json",ecsCallback);
    }

    @Test
    public void getRegionRequestSuccess() {
        mockInputValidator.setJsonFileName("GetRegionsSuccess.json");
        mockECSServices.getRegions(new ECSCallback<RegionsList, Exception>() {
            @Override
            public void onResponse(RegionsList result) {
                assertNotNull(result);
                assertNotNull(result.getRegions().size()>0);

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
    public void getRegionRequestFailure() {
        mockInputValidator.setJsonFileName("EmptyJson.json");
        mockECSServices.getRegions(new ECSCallback<RegionsList, Exception>() {
            @Override
            public void onResponse(RegionsList result) {
                assertTrue(true);

                //test case failed
            }

            @Override
            public void onFailure(Exception error, ECSError ecsError) {
                assertTrue(false);
                //test case passed
            }
        });
    }


    @Test
    public void isValidURL() {
        System.out.println("print the URL"+mockGetRegionsRequest.getURL());

        //acc.us.pil.shop.philips.com/pilcommercewebservices/v2/metainfo/regions/null?fields=FULL&lang=en_US

        String excepted = StaticBlock.getBaseURL()+"pilcommercewebservices"+"/v2/"+"metainfo/regions/"+StaticBlock.getCountry()+"?fields=FULL&lang="+StaticBlock.getLocale();
        Assert.assertEquals(excepted,mockGetRegionsRequest.getURL());
    }

    @Test
    public void isValidGetRequest() {
        Assert.assertEquals(0,mockGetRegionsRequest.getMethod());
    }

    @Test
    public void isValidParam() {
        assertNull(mockGetRegionsRequest.getParams());
    }

    @Test
    public void isValidJSONRequest() {
        assertNull(mockGetRegionsRequest.getJSONRequest());
    }


    @Test
    public void verifyOnResponseSuccess() {

        ECSCallback<RegionsList, Exception> spy1 = Mockito.spy(ecsCallback);
        mockGetRegionsRequest = new MockGetRegionsRequest("GetRegionsSuccess.json",spy1);

        JSONObject jsonObject = getJsonObject("GetProductForCTN.json");

        mockGetRegionsRequest.onResponse(jsonObject);

        Mockito.verify(spy1).onResponse(any(RegionsList.class));

    }


    @Test
    public void verifyOnResponseError() {
        ECSCallback<RegionsList, Exception> spy1 = Mockito.spy(ecsCallback);
        mockGetRegionsRequest = new MockGetRegionsRequest("GetRegionsSuccess.json",spy1);
        VolleyError volleyError = new NoConnectionError();
        mockGetRegionsRequest.onErrorResponse(volleyError);
        Mockito.verify(spy1).onFailure(any(Exception.class),any(ECSError.class));

    }


    @Test
    public void assertResponseSuccessListenerNotNull() {
        assertNotNull(mockGetRegionsRequest.getJSONSuccessResponseListener());
    }

    JSONObject getJsonObject(String jsonfileName){

        JSONObject result = null;
        InputStream in = getClass().getClassLoader().getResourceAsStream(jsonfileName);//"PRXProductAssets.json"
        String jsonString = TestUtil.loadJSONFromFile(in);
        try {
            return new JSONObject(jsonString);
        } catch (JSONException e) {
            return null;
        }
    }
}
