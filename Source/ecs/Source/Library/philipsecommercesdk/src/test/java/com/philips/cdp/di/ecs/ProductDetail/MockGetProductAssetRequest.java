package com.philips.cdp.di.ecs.ProductDetail;

import com.android.volley.VolleyError;
import com.philips.cdp.di.ecs.TestUtil;
import com.philips.cdp.di.ecs.error.ECSErrorEnum;
import com.philips.cdp.di.ecs.integration.ECSCallback;
import com.philips.cdp.di.ecs.model.asset.Assets;
import com.philips.cdp.di.ecs.request.GetProductAssetRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;

public class MockGetProductAssetRequest extends GetProductAssetRequest {
    String jsonfileName;
    public MockGetProductAssetRequest(String jsonFileName, String assetUrl, ECSCallback<Assets, Exception> ecsCallback) {
        super(assetUrl, ecsCallback);
        this.jsonfileName=jsonFileName;
    }


    @Override
    public void executeRequest() {

        JSONObject result = null;
        InputStream in = getClass().getClassLoader().getResourceAsStream(jsonfileName);//"PRXProductAssets.json"
        String jsonString = TestUtil.loadJSONFromFile(in);
        try {
            result = new JSONObject(jsonString);
        } catch (JSONException e) {
            e.printStackTrace();
            VolleyError volleyError = new VolleyError(ECSErrorEnum.somethingWentWrong.toString());
            onErrorResponse(volleyError);
        }
        onResponse(result);

    }
}
