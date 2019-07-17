package com.philips.cdp.di.ecs;

import android.support.annotation.NonNull;

import com.philips.cdp.di.ecs.integration.OAuthInput;
import com.philips.cdp.di.ecs.integration.ECSCallback;
import com.philips.cdp.di.ecs.integration.ECSInput;
import com.philips.cdp.di.ecs.integration.ECSServiceProvider;
import com.philips.cdp.di.ecs.model.asset.Assets;
import com.philips.cdp.di.ecs.model.disclaimer.Disclaimers;
import com.philips.cdp.di.ecs.model.products.Products;
import com.philips.cdp.di.ecs.model.products.Product;
import com.philips.cdp.di.ecs.model.response.HybrisConfigResponse;
import com.philips.cdp.di.ecs.model.response.OAuthResponse;
import com.philips.cdp.di.ecs.util.ECSConfig;
import com.philips.platform.appinfra.AppInfra;


import static com.philips.cdp.di.ecs.util.ECSErrorReason.ECS_INITIALIZATION_FAILURE;

public class ECSServices implements ECSServiceProvider {

   // private static ECSServices mECSServices;

    private ECSManager mECSManager;

    public ECSServices(ECSInput ecsInput, @NonNull AppInfra appInfra, ECSCallback<ECSServices, Exception> iapsdkCallback) {
        ECSConfig.INSTANCE.setAppInfra(appInfra);
        ECSConfig.INSTANCE.setEcsInput(ecsInput);
        mECSManager = getECSManager();
    }

       ECSManager getECSManager(){
        return new ECSManager();
    }


    private static boolean isValidInput(ECSInput ecsInput, AppInfra appInfra) {
        return ecsInput.getLocale()!=null && appInfra!=null;
    }


    public void hybrisOathAuthentication(OAuthInput OAuthInput, ECSCallback<OAuthResponse,Exception> ecsListener){
        mECSManager.getOAuth(OAuthInput,ecsListener);
    }



    public void getECSConfig(ECSCallback<HybrisConfigResponse, Exception> ecsCallback) {
        mECSManager.getHybrisConfigResponse(ecsCallback);

    }

    @Override
    public void getProductList(int currentPage, int pageSize, ECSCallback<Products, Exception> eCSCallback) {

        mECSManager.getProductList(currentPage,pageSize,eCSCallback);
    }


    @Override
    public void getProductDetail(Product product, ECSCallback<Product, Exception> ecsCallback) {
        mECSManager.getProductDetail(product,ecsCallback);
    }

    @Override
    public void InvalidateECS(ECSCallback<Boolean,Exception> ecsCallback) {
       // mECSServices=null;
        ecsCallback.onResponse(true);

    }
}
