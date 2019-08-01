package com.philips.cdp.di.ecs.Voucher;


import com.philips.cdp.di.ecs.TestUtil;
import com.philips.cdp.di.ecs.integration.ECSCallback;
import com.philips.cdp.di.ecs.request.SetVoucherRequest;


import org.json.JSONObject;

import java.io.InputStream;

public class MockSetVoucherRequest extends SetVoucherRequest
{
    String jsonFile;
    public MockSetVoucherRequest(String jsonFile, String voucherCode, ECSCallback<Boolean, Exception> ecsCallback) {
        super(voucherCode, ecsCallback);
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
