package com.philips.cdp.di.ecs;

import com.philips.cdp.di.ecs.integration.ECSCallback;
import com.philips.cdp.di.ecs.model.response.HybrisConfigResponse;
import com.philips.cdp.di.ecs.test.FetchConfiguration;

public class ECSManager {



    void getHybrisConfigResponse(ECSCallback<HybrisConfigResponse, Exception> eCSCallback){  new Thread(new Runnable() {
        @Override
        public void run() {
            new FetchConfiguration().fetchConfiguration(eCSCallback);
        }
    }).start();
    }


}
