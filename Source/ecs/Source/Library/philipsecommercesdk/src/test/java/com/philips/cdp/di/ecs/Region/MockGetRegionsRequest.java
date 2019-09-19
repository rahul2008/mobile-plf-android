package com.philips.cdp.di.ecs.Region;

import com.android.volley.VolleyError;
import com.philips.cdp.di.ecs.TestUtil;
import com.philips.cdp.di.ecs.integration.ECSCallback;
import com.philips.cdp.di.ecs.model.region.ECSRegion;
import com.philips.cdp.di.ecs.model.region.RegionsList;
import com.philips.cdp.di.ecs.request.GetRegionsRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.util.List;

public class MockGetRegionsRequest extends GetRegionsRequest {

    private   String jsonFile;
    public MockGetRegionsRequest(String jsonFile,ECSCallback<List<ECSRegion>, Exception> ecsCallback) {
        super(ecsCallback);
        this.jsonFile = jsonFile;
    }

    @Override
    public void executeRequest() {

        JSONObject result = null;
        InputStream in = getClass().getClassLoader().getResourceAsStream(jsonFile);
        String jsonString = TestUtil.loadJSONFromFile(in);
        try {
            result = new JSONObject(jsonString);
        } catch (JSONException e) {
            e.printStackTrace();
            VolleyError volleyError = new VolleyError(e.getMessage());
            onErrorResponse(volleyError);
        }
        onResponse(result);

    }

}
