package com.philips.cdp.di.ecs;

import android.support.annotation.NonNull;

import com.philips.cdp.di.ecs.integration.OAuthInput;
import com.philips.cdp.di.ecs.integration.ECSCallback;
import com.philips.cdp.di.ecs.integration.ECSServiceProvider;
import com.philips.cdp.di.ecs.model.cart.ECSShoppingCart;
import com.philips.cdp.di.ecs.model.products.Products;
import com.philips.cdp.di.ecs.model.products.Product;
import com.philips.cdp.di.ecs.model.response.HybrisConfigResponse;
import com.philips.cdp.di.ecs.model.response.OAuthResponse;
import com.philips.cdp.di.ecs.util.ECSConfig;
import com.philips.cdp.di.ecs.util.ECSConstant;
import com.philips.platform.appinfra.AppInfra;
import com.philips.platform.appinfra.servicediscovery.ServiceDiscoveryInterface;
import com.philips.platform.appinfra.servicediscovery.model.ServiceDiscoveryService;


import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public class ECSServices implements ECSServiceProvider {

   // private static ECSServices mECSServices;

    private ECSManager mECSManager;

    public ECSServices(String propositionID, @NonNull AppInfra appInfra) {
        ECSConfig.INSTANCE.setAppInfra(appInfra);
        ECSConfig.INSTANCE.setPropositionID(propositionID);
        mECSManager = getECSManager();
    }

       ECSManager getECSManager(){
        return new ECSManager();
    }


    public void configureECS(ECSCallback<Boolean,Exception> ecsCallback){

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
                ECSConfig.INSTANCE.setBaseURL(configUrls);

                if(configUrls!=null){
                    mECSManager.getHybrisConfig(ecsCallback);
                }
            }

    };

        ECSConfig.INSTANCE.getAppInfra().getServiceDiscovery().getServicesWithCountryPreference(listOfServiceId, onGetServiceUrlMapListener,null);
    }

    @Override
    public void getECSConfig(ECSCallback<HybrisConfigResponse, Exception> ecsCallback) {
        mECSManager.getHybrisConfigResponse(ecsCallback);
    }

    @Override
    public void getProductSummary(List<String> ctns, ECSCallback<List<Product>, Exception> ecsCallback) {
        mECSManager.getSummary(ctns,ecsCallback);
    }

    @Override
    public void getShoppingCart(ECSCallback<ECSShoppingCart, Exception> ecsCallback) {
        mECSManager.getECSShoppingCart(ecsCallback);
    }

    @Override
    public void createShoppingCart(ECSCallback<ECSShoppingCart, Exception> ecsCallback) {

    }

    public void hybrisOathAuthentication(OAuthInput OAuthInput, ECSCallback<OAuthResponse,Exception> ecsListener){
        mECSManager.getOAuth(OAuthInput,ecsListener);
    }

    @Override
    public void getProductList(int currentPage, int pageSize, ECSCallback<Products, Exception> eCSCallback) {
        mECSManager.getProductList(currentPage,pageSize,eCSCallback);
    }

    @Override
    public void getProductFor(String ctn, ECSCallback<Product, Exception> eCSCallback) {
        mECSManager.getProductFor(ctn,eCSCallback);

    }

    @Override
    public void InvalidateECS(ECSCallback<Boolean, Exception> eCSCallback) {

    }


    @Override
    public void getProductDetail(Product product, ECSCallback<Product, Exception> ecsCallback) {
        mECSManager.getProductDetail(product,ecsCallback);
    }


}
