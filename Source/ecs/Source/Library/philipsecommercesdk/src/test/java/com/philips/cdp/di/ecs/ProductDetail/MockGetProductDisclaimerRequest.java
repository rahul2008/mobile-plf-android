package com.philips.cdp.di.ecs.ProductDetail;

import com.android.volley.VolleyError;
import com.philips.cdp.di.ecs.TestUtil;
import com.philips.cdp.di.ecs.error.ECSErrorEnum;
import com.philips.cdp.di.ecs.integration.ECSCallback;
import com.philips.cdp.di.ecs.model.disclaimer.Disclaimers;
import com.philips.cdp.di.ecs.request.GetProductDisclaimerRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;

public class MockGetProductDisclaimerRequest extends GetProductDisclaimerRequest {
    String jsonFileName;
    public MockGetProductDisclaimerRequest(String jsonFileName, String assetUrl, ECSCallback<Disclaimers, Exception> ecsCallback) {
        super(assetUrl, ecsCallback);
        this.jsonFileName=jsonFileName;
    }

    @Override
    public void executeRequest() {

        JSONObject result = null;
        InputStream in = getClass().getClassLoader().getResourceAsStream(jsonFileName);//"PRXDisclaimers.json"
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
