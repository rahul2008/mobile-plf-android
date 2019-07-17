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

   // private static ECSServices mECSServices;

    private ECSManager mECSManager;

    public ECSServices(ECSInput ecsInput, @NonNull AppInfra appInfra, ECSCallback<ECSServices, Exception> iapsdkCallback) {
        ECSConfig.INSTANCE.setAppInfra(appInfra);
        ECSConfig.INSTANCE.setEcsInput(ecsInput);
        mECSManager = getECSManager();
    }

<<<<<<< HEAD
       ECSManager getECSManager(){
        return new ECSManager();
    }


    private static boolean isValidInput(ECSInput ecsInput, AppInfra appInfra) {
        return ecsInput.getLocale()!=null && appInfra!=null;
    }
=======
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
>>>>>>> a70bf6e11eb5dc9be23ff91e74c5937fc2c986e2

        ArrayList<String> listOfServiceId = new ArrayList<>();
        listOfServiceId.add(ECSConstant.SERVICE_ID);

        ServiceDiscoveryInterface.OnGetServiceUrlMapListener onGetServiceUrlMapListener = new ServiceDiscoveryInterface.OnGetServiceUrlMapListener() {
            @Override
            public void onError(ERRORVALUES errorvalues, String s) {

                ecsCallback.onFailure(new Exception(errorvalues.name()), 9000);
            }

            @Override
            public void onSuccess(Map<String, ServiceDiscoveryService> map) {

                Collection<ServiceDiscoveryService> values = map.values();

                ArrayList<ServiceDiscoveryService> serviceDiscoveryServiceArrayList = new ArrayList<>(values);

                ServiceDiscoveryService serviceDiscoveryService = serviceDiscoveryServiceArrayList.get(0);

                String locale = serviceDiscoveryService.getLocale();
                ECSConfig.INSTANCE.setLocale(locale);
                String configUrls = serviceDiscoveryService.getConfigUrls();

<<<<<<< HEAD
    public void getECSConfig(ECSCallback<HybrisConfigResponse, Exception> ecsCallback) {
        mECSManager.getHybrisConfigResponse(ecsCallback);
=======
                if (configUrls != null) {
                    mECSManager.getHybrisConfigResponse(ecsCallback);
                }
            }
        };
>>>>>>> a70bf6e11eb5dc9be23ff91e74c5937fc2c986e2

        ECSConfig.INSTANCE.getAppInfra().getServiceDiscovery().getServicesWithCountryPreference(listOfServiceId, onGetServiceUrlMapListener,null);
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
<<<<<<< HEAD
    public void InvalidateECS(ECSCallback<Boolean,Exception> ecsCallback) {
       // mECSServices=null;
        ecsCallback.onResponse(true);
=======
    public void InvalidateECS(ECSCallback<Boolean, Exception> ecsCallback) {
        mECSServices=null;
    }
>>>>>>> a70bf6e11eb5dc9be23ff91e74c5937fc2c986e2

    public void  getECSConfig(ECSCallback<HybrisConfigResponse, Exception> ecsCallback){
        new GetConfigurationRequest(ecsCallback).executeRequest();
    }
}
