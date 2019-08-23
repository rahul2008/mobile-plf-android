package com.philips.cdp.di.ecs.Payment;

import com.philips.cdp.di.ecs.TestUtil;
import com.philips.cdp.di.ecs.integration.ECSCallback;
import com.philips.cdp.di.ecs.request.SetPaymentMethodRequest;

import org.json.JSONObject;

import java.io.InputStream;

public class MockSetPaymentMethodRequest extends SetPaymentMethodRequest {

    String jsonFile;
    public MockSetPaymentMethodRequest(String paymentDetailsId, ECSCallback<Boolean, Exception> ecsCallback, String jsonFile) {
        super(paymentDetailsId, ecsCallback);
        this.jsonFile=jsonFile;
    }

    @Override
    public void executeRequest() {

        JSONObject result = null;
        InputStream in = getClass().getClassLoader().getResourceAsStream(jsonFile);
        String jsonString = TestUtil.loadJSONFromFile(in);
        onResponse(jsonString);


    }
}
