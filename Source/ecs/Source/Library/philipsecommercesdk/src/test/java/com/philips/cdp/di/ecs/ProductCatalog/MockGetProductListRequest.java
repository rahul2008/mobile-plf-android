package com.philips.cdp.di.ecs.ProductCatalog;

import com.android.volley.VolleyError;
import com.philips.cdp.di.ecs.TestUtil;
import com.philips.cdp.di.ecs.integration.ECSCallback;
import com.philips.cdp.di.ecs.model.products.ECSProducts;
import com.philips.cdp.di.ecs.request.GetProductListRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;

public class MockGetProductListRequest extends GetProductListRequest {

String jsonFile;

    public MockGetProductListRequest(String jsonFile, int currentPage, int pageSize, ECSCallback<ECSProducts, Exception> ecsCallback) {
        super(currentPage, pageSize, ecsCallback);
        this.jsonFile=jsonFile;

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
