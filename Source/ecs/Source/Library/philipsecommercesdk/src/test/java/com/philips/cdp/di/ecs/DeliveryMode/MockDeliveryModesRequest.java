package com.philips.cdp.di.ecs.DeliveryMode;

import com.android.volley.VolleyError;
import com.philips.cdp.di.ecs.TestUtil;
import com.philips.cdp.di.ecs.integration.ECSCallback;
import com.philips.cdp.di.ecs.model.address.GetDeliveryModes;
import com.philips.cdp.di.ecs.request.GetDeliveryModesRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;

public class MockDeliveryModesRequest extends GetDeliveryModesRequest {

    private final  String jsonFile;

    public MockDeliveryModesRequest(ECSCallback<GetDeliveryModes, Exception> ecsCallback, String jsonFile) {
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
