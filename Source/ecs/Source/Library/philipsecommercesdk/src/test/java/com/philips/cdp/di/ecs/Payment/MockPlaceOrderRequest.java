package com.philips.cdp.di.ecs.Payment;

import com.philips.cdp.di.ecs.TestUtil;
import com.philips.cdp.di.ecs.integration.ECSCallback;
import com.philips.cdp.di.ecs.model.orders.OrderDetail;
import com.philips.cdp.di.ecs.request.SubmitOrderRequest;

import org.json.JSONObject;

import java.io.InputStream;

public class MockPlaceOrderRequest extends SubmitOrderRequest {

    String jsonfileName;

    public MockPlaceOrderRequest(String jsonFileName, String cvv, ECSCallback<OrderDetail, Exception> exceptionECSCallback) {
        super(cvv, exceptionECSCallback);
        this.jsonfileName=jsonFileName;
    }

    @Override
    public void executeRequest() {

        JSONObject result = null;
        InputStream in = getClass().getClassLoader().getResourceAsStream(jsonfileName);
        String jsonString = TestUtil.loadJSONFromFile(in);
        onResponse(jsonString);

    }
}
