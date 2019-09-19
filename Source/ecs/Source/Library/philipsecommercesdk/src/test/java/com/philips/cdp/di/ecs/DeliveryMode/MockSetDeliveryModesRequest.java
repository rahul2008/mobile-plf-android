package com.philips.cdp.di.ecs.DeliveryMode;

import com.philips.cdp.di.ecs.TestUtil;
import com.philips.cdp.di.ecs.integration.ECSCallback;
import com.philips.cdp.di.ecs.request.SetDeliveryModesRequest;


import java.io.InputStream;

public class MockSetDeliveryModesRequest extends SetDeliveryModesRequest {

    final String jsonFileName;

    public MockSetDeliveryModesRequest(String deliveryModeID, ECSCallback<Boolean, Exception> ecsCallback, String jsonFileName) {
        super(deliveryModeID, ecsCallback);
        this.jsonFileName = jsonFileName;
    }

    @Override
    public void executeRequest() {
        InputStream in = getClass().getClassLoader().getResourceAsStream(jsonFileName);
        String jsonString = TestUtil.loadJSONFromFile(in);
        onResponse(jsonString);
    }
}
