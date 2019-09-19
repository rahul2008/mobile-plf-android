package com.philips.cdp.di.ecs.Payment;

import com.android.volley.VolleyError;
import com.philips.cdp.di.ecs.TestUtil;
import com.philips.cdp.di.ecs.integration.ECSCallback;
import com.philips.cdp.di.ecs.model.payment.ECSPayment;
import com.philips.cdp.di.ecs.model.payment.PaymentMethods;
import com.philips.cdp.di.ecs.request.GetPaymentsRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.util.List;

public class MockGetPaymentsRequest extends GetPaymentsRequest {

    String jsonFile;

    public MockGetPaymentsRequest(String jsonFile, ECSCallback<List<ECSPayment>, Exception> ecsCallback) {
        super(ecsCallback);
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
