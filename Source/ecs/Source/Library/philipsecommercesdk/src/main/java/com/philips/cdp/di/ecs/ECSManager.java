package com.philips.cdp.di.ecs;

import com.google.gson.Gson;
import com.philips.cdp.di.ecs.integration.ECSCallback;
import com.philips.cdp.di.ecs.integration.OAuthInput;
import com.philips.cdp.di.ecs.model.asset.Assets;
import com.philips.cdp.di.ecs.model.disclaimer.Disclaimers;
import com.philips.cdp.di.ecs.model.products.Product;
import com.philips.cdp.di.ecs.model.products.Products;
import com.philips.cdp.di.ecs.model.response.HybrisConfigResponse;
import com.philips.cdp.di.ecs.model.response.OAuthResponse;
import com.philips.cdp.di.ecs.model.summary.Data;
import com.philips.cdp.di.ecs.model.summary.ECSProductSummary;
import com.philips.cdp.di.ecs.prx.serviceDiscovery.AssetServiceDiscoveryRequest;
import com.philips.cdp.di.ecs.prx.serviceDiscovery.DisclaimerServiceDiscoveryRequest;
import com.philips.cdp.di.ecs.prx.serviceDiscovery.ProductSummaryListServiceDiscoveryRequest;
import com.philips.cdp.di.ecs.prx.serviceDiscovery.ServiceDiscoveryRequest;
import com.philips.cdp.di.ecs.request.GetConfigurationRequest;
import com.philips.cdp.di.ecs.request.GetProductAssetRequest;
import com.philips.cdp.di.ecs.request.GetProductDisclaimerRequest;
import com.philips.cdp.di.ecs.request.GetProductRequest;
import com.philips.cdp.di.ecs.request.GetProductSummaryListRequest;
import com.philips.cdp.di.ecs.request.OAuthRequest;
import com.philips.cdp.di.ecs.util.ECSConfig;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.philips.cdp.di.ecs.util.ECSErrorReason.ECS_UNKNOWN_ERROR;

public class ECSManager {

    void getHybrisConfigResponse(ECSCallback<Boolean, Exception> ecsCallback) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                new GetConfigurationRequest(new ECSCallback<HybrisConfigResponse, Exception>() {
                    @Override
                    public void onResponse(HybrisConfigResponse result) {

                        ECSConfig.INSTANCE.setSiteId(result.getSiteId());
                        ECSConfig.INSTANCE.setRootCategory(result.getRootCategory());

                        ecsCallback.onResponse(true);
                    }

                    @Override
                    public void onFailure(Exception error, int errorCode) {
                        ecsCallback.onFailure(error, errorCode);
                    }
                }).executeRequest();
            }
        }).start();
    }


    public void getProductList(int currentPage, int pageSize, final ECSCallback<Products, Exception> finalEcsCallback) {

        new Thread(new Runnable() {
            @Override
            public void run() {
                GetProductRequest getProductRequest = new GetProductRequest(currentPage, pageSize, new ECSCallback<Products, Exception>() {
                    @Override
                    public void onResponse(Products result) {
                         prepareProductSummaryURL(result,finalEcsCallback);
                    }

<<<<<<< HEAD
                    @Override
                    public void onFailure(Exception error, int errorCode) {
                        finalEcsCallback.onFailure(new Exception(ECS_UNKNOWN_ERROR),999);

                    }
                });
                getProductRequest.executeRequest();
=======
                new GetProductRequest(currentPage, pageSize, ecsCallback).executeRequest();

>>>>>>> a70bf6e11eb5dc9be23ff91e74c5937fc2c986e2
            }
        }).start();
    }

     void prepareProductSummaryURL(Products result, final ECSCallback<Products, Exception> ecsCallback) {
            List<Product> productsEntities = result.getProducts();
            ArrayList<String> ctns = new ArrayList<>();

            for (Product product : productsEntities) {
                ctns.add(product.getCode());
            }
            //Call PRX here
            ProductSummaryListServiceDiscoveryRequest productSummaryListServiceDiscoveryRequest = prepareProductSummaryListRequest(ctns);
            productSummaryListServiceDiscoveryRequest.getRequestUrlFromAppInfra(new ServiceDiscoveryRequest.OnUrlReceived() {
                @Override
                public void onSuccess(String url) {
                    getProductSummary(url, new ECSCallback<ECSProductSummary, Exception>() {
                        @Override
                        public void onResponse(ECSProductSummary ecsProductSummary) {
                            updateProductsWithSummary(result,ecsProductSummary);
                            ecsCallback.onResponse(result);

                        }

                        @Override
                        public void onFailure(Exception error, int errorCode) {
                            ecsCallback.onFailure(new Exception(ECS_UNKNOWN_ERROR),999);
                        }
                    });

                }

                @Override
                public void onError(ERRORVALUES errorvalues, String s) {

                }
            });
    }

    void updateProductsWithSummary(Products products,ECSProductSummary ecsProductSummary ){
        HashMap<String, Data> summaryCtnMap = new HashMap<>();

        if (ecsProductSummary.isSuccess()) {
            for (Data data : ecsProductSummary.getData()) {
                summaryCtnMap.put(data.getCtn(), data);
            }
        }

        for (Product product : products.getProducts()) {
            Data productSummaryData = summaryCtnMap.get(product.getCode());
            product.setSummary(productSummaryData);
        }
    }

    void getProductSummary(String url, ECSCallback<ECSProductSummary, Exception> eCSCallback){  new Thread(new Runnable() {
        @Override
        public void run() {
            new GetProductSummaryListRequest(url,eCSCallback).executeRequest();
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

    private void getProductAssetAndDisclaimer(Product product, ECSCallback<Product, Exception> ecsCallback) {

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

                                getProductDisclaimer(product, ecsCallback);
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
        getProductAssetAndDisclaimer(product, ecsCallback);
    }

<<<<<<< HEAD
    private ProductSummaryListServiceDiscoveryRequest prepareProductSummaryListRequest(List<String> ctns) {
        ProductSummaryListServiceDiscoveryRequest productSummaryListServiceDiscoveryRequest = new ProductSummaryListServiceDiscoveryRequest(ctns);
        return productSummaryListServiceDiscoveryRequest;
    }
=======
>>>>>>> a70bf6e11eb5dc9be23ff91e74c5937fc2c986e2
}
