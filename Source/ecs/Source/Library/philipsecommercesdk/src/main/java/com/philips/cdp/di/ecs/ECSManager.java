package com.philips.cdp.di.ecs;

import android.content.Context;

import com.philips.cdp.di.ecs.integration.ECSCallback;
import com.philips.cdp.di.ecs.integration.OAuthInput;
import com.philips.cdp.di.ecs.model.products.Products;
import com.philips.cdp.di.ecs.model.response.HybrisConfigResponse;
import com.philips.cdp.di.ecs.model.response.OAuthResponse;
import com.philips.cdp.di.ecs.request.GetConfigurationRequest;
import com.philips.cdp.di.ecs.request.GetProductRequest;
import com.philips.cdp.di.ecs.request.OAuthRequest;

public class ECSManager {



    void getHybrisConfigResponse(ECSCallback<HybrisConfigResponse, Exception> eCSCallback){  new Thread(new Runnable() {
        @Override
        public void run() {
            new GetConfigurationRequest(eCSCallback).executeRequest();
        }
    }).start();
    }


    public void getProductDetail(int currentPage, int pageSize, ECSCallback<Products, Exception> ecsCallback) {



        new Thread(new Runnable() {
            @Override
            public void run() {
                new GetProductRequest(currentPage, pageSize, ecsCallback).executeRequest();
            }
        }).start();
    }

    public void getOAuth(OAuthInput oAuthInput, ECSCallback<OAuthResponse, Exception> ecsCallback) {
        new Thread(new Runnable() {
            @Override
            public void run() {

               new OAuthRequest(oAuthInput, ecsCallback);
            }
        }).start();
    }
}
