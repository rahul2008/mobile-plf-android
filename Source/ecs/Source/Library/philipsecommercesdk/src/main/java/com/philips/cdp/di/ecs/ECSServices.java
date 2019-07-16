package com.philips.cdp.di.ecs;

import android.support.annotation.NonNull;

import com.philips.cdp.di.ecs.integration.OAuthInput;
import com.philips.cdp.di.ecs.integration.ECSCallback;
import com.philips.cdp.di.ecs.integration.ECSInput;
import com.philips.cdp.di.ecs.integration.ECSServiceProvider;
import com.philips.cdp.di.ecs.model.products.Products;
import com.philips.cdp.di.ecs.model.products.Product;
import com.philips.cdp.di.ecs.model.response.HybrisConfigResponse;
import com.philips.cdp.di.ecs.model.response.OAuthResponse;
import com.philips.cdp.di.ecs.request.GetConfigurationRequest;
import com.philips.cdp.di.ecs.util.ECSConfig;
import com.philips.cdp.di.ecs.util.ECSConstant;
import com.philips.platform.appinfra.AppInfra;
import com.philips.platform.appinfra.servicediscovery.ServiceDiscoveryInterface;
import com.philips.platform.appinfra.servicediscovery.model.ServiceDiscoveryService;


import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

import static com.philips.cdp.di.ecs.util.ECSErrorReason.ECS_INITIALIZATION_FAILURE;

public class ECSServices implements ECSServiceProvider {

    private static ECSServices mECSServices;

    private ECSManager mECSManager;

    private ECSServices(ECSInput ecsInput, AppInfra appInfra) {
        ECSConfig.INSTANCE.setAppInfra(appInfra);
        ECSConfig.INSTANCE.setEcsInput(ecsInput);
        mECSManager = new ECSManager();
    }

    /**
     * Initialize IAPSDKService. Once initialized IAPSDKService object is created and returned in iapsdkCallback.
     * All IAP service methods can be called only by this IAPSDKService object.
     *  @param ecsInput     the init params componentId, propositionId and locale
     * @param iapsdkCallback the iapsdk callback
     */
    public static void init(ECSInput ecsInput, @NonNull AppInfra appInfra, ECSCallback<ECSServices, Exception> iapsdkCallback) {

        if(isValidInput(appInfra)){  // if locale, propositionID are verified
            mECSServices=new ECSServices(ecsInput,appInfra);
            iapsdkCallback.onResponse(mECSServices);
        }else{
            iapsdkCallback.onFailure(new Exception(ECS_INITIALIZATION_FAILURE),9999);
        }

    }

    public void configureECS(ECSCallback<Boolean,Exception> ecsCallback){

        ArrayList<String> listOfServiceId = new ArrayList<>();
        listOfServiceId.add(ECSConstant.SERVICE_ID);

        new ServiceDiscoveryInterface.OnGetServiceUrlMapListener() {
            @Override
            public void onError(ERRORVALUES errorvalues, String s) {

                ecsCallback.onFailure(new Exception(errorvalues.name()),9000);
            }

            @Override
            public void onSuccess(Map<String, ServiceDiscoveryService> map) {

                Collection<ServiceDiscoveryService> values = map.values();

                ArrayList<ServiceDiscoveryService> serviceDiscoveryServiceArrayList = new ArrayList<>(values);

                ServiceDiscoveryService serviceDiscoveryService = serviceDiscoveryServiceArrayList.get(0);

                String locale = serviceDiscoveryService.getLocale();
                ECSConfig.INSTANCE.setLocale(locale);
                String configUrls = serviceDiscoveryService.getConfigUrls();

                if(configUrls !=null){
                    mECSManager.getHybrisConfigResponse(ecsCallback);
                }
            }
        };
    }

    private static boolean isValidInput(AppInfra appInfra) {
        return appInfra!=null;
    }


    public void hybrisOathAuthentication(OAuthInput OAuthInput, ECSCallback<OAuthResponse,Exception> ecsListener){
        mECSManager.getOAuth(OAuthInput,ecsListener);
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
    public void InvalidateECS(ECSCallback<Boolean, Exception> ecsCallback) {
        mECSServices=null;
    }

    public void  getECSConfig(ECSCallback<HybrisConfigResponse, Exception> ecsCallback){
        new GetConfigurationRequest(ecsCallback).executeRequest();
    }
}
