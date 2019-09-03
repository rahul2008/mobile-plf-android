package com.philips.cdp.di.ecs.ProductDetail;

import android.content.Context;

import com.android.volley.NoConnectionError;
import com.android.volley.VolleyError;
import com.google.gson.JsonObject;
import com.philips.cdp.di.ecs.ECSServices;
import com.philips.cdp.di.ecs.MockECSServices;
import com.philips.cdp.di.ecs.ProductCatalog.MockGetProductListRequest;
import com.philips.cdp.di.ecs.StaticBlock;
import com.philips.cdp.di.ecs.TestUtil;
import com.philips.cdp.di.ecs.integration.ECSCallback;
import com.philips.cdp.di.ecs.model.asset.AssetModel;
import com.philips.cdp.di.ecs.model.asset.Assets;
import com.philips.cdp.di.ecs.model.products.Product;
import com.philips.cdp.di.ecs.model.products.Products;
import com.philips.cdp.di.ecs.util.ECSErrorReason;
import com.philips.platform.appinfra.AppInfra;
import com.philips.platform.appinfra.rest.RestInterface;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.robolectric.RobolectricTestRunner;

import java.io.InputStream;

import static androidx.test.platform.app.InstrumentationRegistry.getInstrumentation;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;

@RunWith(RobolectricTestRunner.class)
public class ProductAssetRequestTest {

    private Context mContext;


    MockECSServices mockECSServices;
    ECSServices ecsServices;


    private AppInfra appInfra;


    @Mock
    RestInterface mockRestInterface;

    MockGetProductAssetRequest mockGetProductAssetRequest;

    String samplePRXAssetUrl = "samplePRXURL";

    ECSCallback<Assets,Exception> ecsCallback;


    @Before
    public void setUp() throws Exception {


        mContext = getInstrumentation().getContext();
        appInfra = new AppInfra.Builder().setRestInterface(mockRestInterface).build(mContext);
        appInfra.getServiceDiscovery().setHomeCountry("DE");


        mockECSServices = new MockECSServices("", appInfra);
        ecsServices = new ECSServices("",appInfra);


        StaticBlock.initialize();
        ecsCallback = new ECSCallback<Assets, Exception>() {
            @Override
            public void onResponse(Assets result) {

            }

            @Override
            public void onFailure(Exception error, int errorCode) {

            }
        };
        mockGetProductAssetRequest = new MockGetProductAssetRequest("EmptyJson.json",samplePRXAssetUrl,ecsCallback);

    }

    @Test
    public void shouldValidateAssetURL() {
        assertEquals(samplePRXAssetUrl,mockGetProductAssetRequest.getURL());
    }

    @Test
    public void shouldValidGetRequest() {
        assertEquals(0,mockGetProductAssetRequest.getMethod());
    }

    @Test
    public void verifyOnResponseError() {
        ECSCallback<Assets, Exception> spy1 = Mockito.spy(ecsCallback);
        mockGetProductAssetRequest = new MockGetProductAssetRequest("EmptyJson.json",samplePRXAssetUrl,spy1);
        VolleyError volleyError = new NoConnectionError();
        mockGetProductAssetRequest.onErrorResponse(volleyError);
        Mockito.verify(spy1).onFailure(any(Exception.class),anyInt());

    }

    @Test
    public void verifyOnResponseSuccess() {

        ECSCallback<Assets, Exception> spy1 = Mockito.spy(ecsCallback);
        mockGetProductAssetRequest = new MockGetProductAssetRequest("PRXProductAssets.json",samplePRXAssetUrl,spy1);

        JSONObject jsonObject = getJsonObject("PRXProductAssets.json");

        mockGetProductAssetRequest.onResponse(jsonObject);

        Mockito.verify(spy1).onResponse(any(Assets.class));

    }

    @Test
    public void shouldValidJSONSuccessResponseListener() {
        assertNotNull(mockGetProductAssetRequest.getJSONSuccessResponseListener());
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
