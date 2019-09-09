package com.philips.cdp.di.ecs.Cart;

import com.android.volley.VolleyError;
import com.philips.cdp.di.ecs.TestUtil;
import com.philips.cdp.di.ecs.integration.ECSCallback;
import com.philips.cdp.di.ecs.model.cart.ECSEntries;
import com.philips.cdp.di.ecs.request.UpdateECSShoppingCartQuantityRequest;

import java.io.InputStream;

public class MockUpdateECSShoppingCartQuantityRequest extends UpdateECSShoppingCartQuantityRequest
{  String jsonfileName;
    public MockUpdateECSShoppingCartQuantityRequest(String jsonFileName, ECSCallback<Boolean, Exception> ecsCallback, ECSEntries entriesEntity, int quantity) {
        super(ecsCallback, entriesEntity, quantity);
        this.jsonfileName=jsonFileName;
    }

    @Override
    public void executeRequest() {

        InputStream in = getClass().getClassLoader().getResourceAsStream(jsonfileName);
        String jsonString = TestUtil.loadJSONFromFile(in);
        if(null!=jsonString && !jsonString.isEmpty()){
            onResponse(jsonString);
        }else{
            VolleyError volleyError = new VolleyError("Update Quantity failed");
            onErrorResponse(volleyError);
        }

    }
}
