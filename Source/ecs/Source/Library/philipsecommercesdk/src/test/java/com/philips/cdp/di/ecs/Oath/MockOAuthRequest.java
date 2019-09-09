package com.philips.cdp.di.ecs.Oath;

import com.android.volley.VolleyError;
import com.philips.cdp.di.ecs.TestUtil;
import com.philips.cdp.di.ecs.integration.ECSCallback;
import com.philips.cdp.di.ecs.integration.ECSOAuthProvider;
import com.philips.cdp.di.ecs.model.oauth.ECSOAuthData;
import com.philips.cdp.di.ecs.request.OAuthRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;

public class MockOAuthRequest extends OAuthRequest {
    String jsonFile;

    public MockOAuthRequest(String jsonFile, ECSOAuthProvider oAuthInput, ECSCallback<ECSOAuthData, Exception> ecsListener) {
        super(oAuthInput, ecsListener);
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
