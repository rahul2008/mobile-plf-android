package com.philips.cdp.di.ecs.Payment;

import com.philips.cdp.di.ecs.TestUtil;
import com.philips.cdp.di.ecs.integration.ECSCallback;
import com.philips.cdp.di.ecs.model.address.Addresses;
import com.philips.cdp.di.ecs.model.orders.OrderDetail;
import com.philips.cdp.di.ecs.model.payment.MakePaymentData;
import com.philips.cdp.di.ecs.request.MakePaymentRequest;

import org.json.JSONObject;

import java.io.InputStream;

public class MockMakePaymentRequest extends MakePaymentRequest {

    String jsonfileName;

    public MockMakePaymentRequest(String jsonFileName, OrderDetail orderDetail, Addresses ecsBillingAddressRequest, ECSCallback<MakePaymentData, Exception> ecsCallback) {
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
