package com.philips.cdp.di.ecs;

import com.philips.cdp.di.ecs.integration.ECSCallback;
import com.philips.cdp.di.ecs.model.products.Products;
import com.philips.cdp.di.ecs.model.response.HybrisConfigResponse;
import com.philips.cdp.di.ecs.network.GetProductJSONRequest;
import com.philips.cdp.di.ecs.test.FetchConfiguration;

public class ECSManager {



    void getHybrisConfigResponse(ECSCallback<HybrisConfigResponse, Exception> eCSCallback){  new Thread(new Runnable() {
        @Override
        public void run() {
            new FetchConfiguration().fetchConfiguration(eCSCallback);
        }
    }).start();
    }


    public void getProductDetail(int currentPage,int pageSize,ECSCallback<Products, Exception> ecsCallback) {



        new Thread(new Runnable() {
            @Override
            public void run() {
                GetProductJSONRequest getProductJSONRequest = new GetProductJSONRequest(currentPage, pageSize, ecsCallback);
                getProductJSONRequest.executeRequest();
            }
        }).start();
    }
}
