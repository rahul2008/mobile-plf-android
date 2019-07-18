package com.philips.cdp.di.ecs.ProductDetail;

import com.android.volley.VolleyError;
import com.philips.cdp.di.ecs.TestUtil;
import com.philips.cdp.di.ecs.integration.ECSCallback;
import com.philips.cdp.di.ecs.model.disclaimer.Disclaimers;
import com.philips.cdp.di.ecs.request.GetProductDisclaimerRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;

public class MockGetProductDisclaimerRequest extends GetProductDisclaimerRequest {
    public MockGetProductDisclaimerRequest(String assetUrl, ECSCallback<Disclaimers, Exception> ecsCallback) {
        super(assetUrl, ecsCallback);
    }

    @Override
    public void executeRequest() {

        JSONObject result = null;
        InputStream in = getClass().getClassLoader().getResourceAsStream("PRXDisclaimers.json");
        String jsonString = TestUtil.loadJSONFromFile(in);
        try {
            result = new JSONObject(jsonString);
        } catch (JSONException e) {
            e.printStackTrace();
            VolleyError volleyError = new VolleyError("");
            onErrorResponse(volleyError);
        }
        onResponse(result);

    }
}
