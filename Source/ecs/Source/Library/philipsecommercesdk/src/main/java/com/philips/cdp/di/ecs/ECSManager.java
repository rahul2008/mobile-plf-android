package com.philips.cdp.di.ecs;

import com.philips.cdp.di.ecs.integration.ECSCallback;
import com.philips.cdp.di.ecs.integration.OAuthInput;
import com.philips.cdp.di.ecs.model.asset.Assets;
import com.philips.cdp.di.ecs.model.disclaimer.Disclaimers;
import com.philips.cdp.di.ecs.model.products.Products;
import com.philips.cdp.di.ecs.model.products.Product;
import com.philips.cdp.di.ecs.model.response.HybrisConfigResponse;
import com.philips.cdp.di.ecs.model.response.OAuthResponse;
import com.philips.cdp.di.ecs.prx.serviceDiscovery.AssetServiceDiscoveryRequest;
import com.philips.cdp.di.ecs.prx.serviceDiscovery.DisclaimerServiceDiscoveryRequest;
import com.philips.cdp.di.ecs.prx.serviceDiscovery.ServiceDiscoveryRequest;
import com.philips.cdp.di.ecs.request.GetConfigurationRequest;
import com.philips.cdp.di.ecs.request.GetProductAssetRequest;
import com.philips.cdp.di.ecs.request.GetProductDisclaimerRequest;
import com.philips.cdp.di.ecs.request.GetProductRequest;
import com.philips.cdp.di.ecs.request.OAuthRequest;
import com.philips.cdp.di.ecs.util.ECSConfig;

public class ECSManager {

    void getHybrisConfigResponse(ECSCallback<HybrisConfigResponse, Exception> eCSCallback){  new Thread(new Runnable() {
        @Override
        public void run() {
            new GetConfigurationRequest(eCSCallback).executeRequest();
        }
    }).start();
    }


    public void getProductList(int currentPage, int pageSize, ECSCallback<Products, Exception> ecsCallback) {

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

    private void getProductAssetAndDisclaimer(Product product,ECSCallback<Product, Exception> ecsCallback) {

        new Thread(new Runnable() {
            @Override
            public void run() {

                new AssetServiceDiscoveryRequest(product.getCode()).getRequestUrlFromAppInfra(new ServiceDiscoveryRequest.OnUrlReceived() {
                    @Override
                    public void onSuccess(String url) {

                        new GetProductAssetRequest(url, new ECSCallback<Assets, Exception>() {
                            @Override
                            public void onResponse(Assets result) {
                                product.setAssets(result);

                                getProductDisclaimer(product,ecsCallback);
                            }

                            @Override
                            public void onFailure(Exception error, int errorCode) {

                            }
                        });

                    }

                    @Override
                    public void onError(ERRORVALUES errorvalues, String s) {

                    }
                });

            }
        }).start();
    }

    private void getProductDisclaimer(Product product, ECSCallback<Product, Exception> ecsCallback) {

        new Thread(new Runnable() {
            @Override
            public void run() {

                new DisclaimerServiceDiscoveryRequest(product.getCode()).getRequestUrlFromAppInfra(new ServiceDiscoveryRequest.OnUrlReceived() {
                    @Override
                    public void onError(ERRORVALUES errorvalues, String s) {

                    }

                    @Override
                    public void onSuccess(String url) {

                        new GetProductDisclaimerRequest(url, new ECSCallback<Disclaimers, Exception>() {
                            @Override
                            public void onResponse(Disclaimers result) {

                                product.setDisclaimers(result);
                                ecsCallback.onResponse(product);

                            }

                            @Override
                            public void onFailure(Exception error, int errorCode) {

                            }
                        }).executeRequest();
                    }
                });

            }
        }).start();
    }

    public void getProductDetail(Product product, ECSCallback<Product, Exception> ecsCallback) {
        getProductAssetAndDisclaimer(product,ecsCallback);
    }
}
