package com.philips.cdp.di.ecs.Payment;

import com.philips.cdp.di.ecs.TestUtil;
import com.philips.cdp.di.ecs.integration.ECSCallback;
import com.philips.cdp.di.ecs.model.address.ECSAddress;
import com.philips.cdp.di.ecs.model.orders.ECSOrderDetail;
import com.philips.cdp.di.ecs.model.payment.ECSPaymentProvider;
import com.philips.cdp.di.ecs.request.MakePaymentRequest;

import org.json.JSONObject;

import java.io.InputStream;

public class MockMakePaymentRequest extends MakePaymentRequest {

    String jsonfileName;

    public MockMakePaymentRequest(String jsonFileName, ECSOrderDetail orderDetail, ECSAddress ecsBillingAddressRequest, ECSCallback<ECSPaymentProvider, Exception> ecsCallback) {
        super(orderDetail, ecsBillingAddressRequest, ecsCallback);
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
