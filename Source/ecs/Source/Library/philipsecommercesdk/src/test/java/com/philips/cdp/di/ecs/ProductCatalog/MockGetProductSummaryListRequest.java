package com.philips.cdp.di.ecs.ProductCatalog;

import com.android.volley.VolleyError;
import com.philips.cdp.di.ecs.TestUtil;
import com.philips.cdp.di.ecs.integration.ECSCallback;
import com.philips.cdp.di.ecs.model.summary.ECSProductSummary;
import com.philips.cdp.di.ecs.request.GetProductSummaryListRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;

public class MockGetProductSummaryListRequest extends GetProductSummaryListRequest {



    String jsonFileName;

    public MockGetProductSummaryListRequest(String jsonFile, String prxSummaryListURL, ECSCallback<ECSProductSummary, Exception> ecsCallback) {
        super(prxSummaryListURL, ecsCallback);
        this.jsonFileName=jsonFile;
    }

    @Override
    public void executeRequest() {
        JSONObject result=null;
        InputStream in = getClass().getClassLoader().getResourceAsStream(jsonFileName);//"PRXSummaryResponse.json"
        String jsonString= TestUtil.loadJSONFromFile(in);
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
