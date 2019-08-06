package com.philips.cdp.di.ecs.Address;


import com.philips.cdp.di.ecs.TestUtil;
import com.philips.cdp.di.ecs.integration.ECSCallback;
import com.philips.cdp.di.ecs.model.address.Addresses;
import com.philips.cdp.di.ecs.request.CreateAddressRequest;

import org.json.JSONObject;

import java.io.InputStream;

public class MockCreateAddressRequest extends CreateAddressRequest {

    String jsonfileName;


    public MockCreateAddressRequest(String jsonFileName,Addresses ecsAddressRequest, ECSCallback<Addresses, Exception> ecsCallback) {
        super(ecsAddressRequest, ecsCallback);
        this.jsonfileName=jsonFileName;
    }

    @Override
    public void executeRequest() {

        JSONObject result = null;
        InputStream in = getClass().getClassLoader().getResourceAsStream(jsonfileName);//"CreateAddressSuccess
        String jsonString = TestUtil.loadJSONFromFile(in);
        onResponse(jsonString);

    }
}
