package com.philips.cdp.di.ecs.ProductCatalog;

import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.philips.cdp.di.ecs.TestUtil;
import com.philips.cdp.di.ecs.integration.ECSCallback;
import com.philips.cdp.di.ecs.model.products.Products;
import com.philips.cdp.di.ecs.request.GetProductRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;

public class MockGetProductListRequest extends GetProductRequest {

String jsonFile;

    public MockGetProductListRequest(String jsonFile, int currentPage, int pageSize, ECSCallback<Products, Exception> ecsCallback) {
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
            VolleyError volleyError = new VolleyError("");
            onErrorResponse(volleyError);
        }
        onResponse(result);

    }
}
