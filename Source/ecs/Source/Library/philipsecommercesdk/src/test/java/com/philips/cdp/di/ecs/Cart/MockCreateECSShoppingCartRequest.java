package com.philips.cdp.di.ecs.Cart;

import com.android.volley.VolleyError;
import com.philips.cdp.di.ecs.TestUtil;
import com.philips.cdp.di.ecs.integration.ECSCallback;
import com.philips.cdp.di.ecs.model.cart.ECSShoppingCart;
import com.philips.cdp.di.ecs.request.CreateECSShoppingCartRequest;
import com.philips.cdp.di.ecs.util.ECSErrorReason;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;

public class MockCreateECSShoppingCartRequest extends CreateECSShoppingCartRequest {
    String jsonfileName;
    public MockCreateECSShoppingCartRequest(String jsonFileName, ECSCallback<ECSShoppingCart, Exception> eCSCallback) {
        super(eCSCallback);
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
            VolleyError volleyError = new VolleyError(ECSErrorReason.ECS_UNKNOWN_ERROR);
            onErrorResponse(volleyError);
        }
        onResponse(result);

    }
}
