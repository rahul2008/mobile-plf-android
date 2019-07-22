package com.philips.cdp.di.ecs;

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
import com.philips.cdp.di.ecs.request.GetProductListRequest;
import com.philips.cdp.di.ecs.request.GetProductForRequest;
import com.philips.cdp.di.ecs.request.GetProductSummaryListRequest;
import com.philips.cdp.di.ecs.request.OAuthRequest;
import com.philips.cdp.di.ecs.util.ECSConfig;
import com.philips.cdp.di.ecs.util.ECSErrorReason;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.philips.cdp.di.ecs.util.ECSErrorReason.ECS_NO_PRODUCT_DETAIL_FOUND;

public class ECSManager {

    void getHybrisConfig(ECSCallback<Boolean, Exception> ecsCallback) {
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

    void getHybrisConfigResponse(ECSCallback<HybrisConfigResponse, Exception> ecsCallback) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                new GetConfigurationRequest(new ECSCallback<HybrisConfigResponse, Exception>() {
                    @Override
                    public void onResponse(HybrisConfigResponse result) {

                        ECSConfig.INSTANCE.setSiteId(result.getSiteId());
                        ECSConfig.INSTANCE.setRootCategory(result.getRootCategory());

                        ecsCallback.onResponse(result);
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
                GetProductListRequest getProductListRequest = new GetProductListRequest(currentPage, pageSize, new ECSCallback<Products, Exception>() {
                    @Override
                    public void onResponse(Products result) {
                        prepareProductSummaryURL(result, finalEcsCallback);
                    }

                    @Override
                    public void onFailure(Exception error, int errorCode) {
                        finalEcsCallback.onFailure(error, errorCode);

                    }
                });
                getProductListRequest.executeRequest();

            }
        }).start();
    }

    public void getProductFor(String ctn, ECSCallback<Product, Exception> eCSCallback) {

        if (null != ECSConfig.INSTANCE.getSiteId()) { // hybris flow
            new Thread(new Runnable() {
                @Override
                public void run() {
                    new GetProductForRequest(ctn, new ECSCallback<Product, Exception>() {
                        @Override
                        public void onResponse(Product result) {
                            getSummaryForCTN(ctn, result, eCSCallback);
                        }

                        @Override
                        public void onFailure(Exception error, int errorCode) {
                            eCSCallback.onFailure(new Exception(ECSErrorReason.ECS_GIVEN_PRODUCT_NOT_FOUND), 28999);
                        }
                    }).executeRequest();
                }
            }).start();
        } else { // Retailer flow
            getSummaryForCTN(ctn, null, eCSCallback);
        }

    }

    private void getSummaryForCTN(String ctn, Product product, ECSCallback<Product, Exception> eCSCallback) {
        Products products = new Products();
        List<Product> productList = new ArrayList<Product>();
        ArrayList<String> ctns = new ArrayList<>();
        if (null == product) {
            product = new Product();
        }

        products.setProducts(productList);
        products.getProducts().add(product);
        ctns.add(ctn);
        getProductSummary(products, new ECSCallback<Products, Exception>() {
            @Override
            public void onResponse(Products result) {
                eCSCallback.onResponse(result.getProducts().get(0)); // one and only product
            }

            @Override
            public void onFailure(Exception error, int errorCode) {
                eCSCallback.onFailure(new Exception(ECSErrorReason.ECS_GIVEN_PRODUCT_NOT_FOUND), 28999);
            }
        }, ctns);
    }

    void prepareProductSummaryURL(Products result, final ECSCallback<Products, Exception> ecsCallback) {
        List<Product> productsEntities = result.getProducts();
        ArrayList<String> ctns = new ArrayList<>();

        for (Product product : productsEntities) {
            ctns.add(product.getCode());
        }
        getProductSummary(result, ecsCallback, ctns);
    }

    private void getProductSummary(Products result, ECSCallback<Products, Exception> ecsCallback, ArrayList<String> ctns) {
        //Call PRX here
        ProductSummaryListServiceDiscoveryRequest productSummaryListServiceDiscoveryRequest = prepareProductSummaryListRequest(ctns);
        productSummaryListServiceDiscoveryRequest.getRequestUrlFromAppInfra(new ServiceDiscoveryRequest.OnUrlReceived() {
            @Override
            public void onSuccess(String url) {
                getProductSummary(url, new ECSCallback<ECSProductSummary, Exception>() {
                    @Override
                    public void onResponse(ECSProductSummary ecsProductSummary) {
                        updateProductsWithSummary(result, ecsProductSummary);
                        ecsCallback.onResponse(result);

                    }

                    @Override
                    public void onFailure(Exception error, int errorCode) {
                        ecsCallback.onFailure(error, errorCode);
                    }
                });

            }

            @Override
            public void onError(ERRORVALUES errorvalues, String s) {

            }
        });
    }

    void updateProductsWithSummary(Products products, ECSProductSummary ecsProductSummary) {
        HashMap<String, Data> summaryCtnMap = new HashMap<>();

        ArrayList<Product> productArrayList = new ArrayList<>(); // set back products for which summaries are available

        if (ecsProductSummary.isSuccess()) {
            for (Data data : ecsProductSummary.getData()) {
                summaryCtnMap.put(data.getCtn(), data);
            }
        }

        for (Product product : products.getProducts()) {
            Data productSummaryData = summaryCtnMap.get(product.getCode());

            if (productSummaryData != null) {
                product.setSummary(productSummaryData);
                productArrayList.add(product);
            }
        }

        products.setProducts(productArrayList);
    }

    void getProductSummary(String url, ECSCallback<ECSProductSummary, Exception> eCSCallback) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                new GetProductSummaryListRequest(url, eCSCallback).executeRequest();
            }
        }).start();
    }


    public void getOAuth(OAuthInput oAuthInput, ECSCallback<OAuthResponse, Exception> ecsCallback) {
        new Thread(new Runnable() {
            @Override
            public void run() {

                new OAuthRequest(oAuthInput, ecsCallback).executeRequest();
            }
        }).start();
    }


    void getProductAsset(String url, ECSCallback<Assets, Exception> eCSCallback) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                new GetProductAssetRequest(url, eCSCallback).executeRequest();
            }
        }).start();
    }

    void getProductDisclaimer(String url, ECSCallback<Disclaimers, Exception> eCSCallback) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                new GetProductDisclaimerRequest(url, eCSCallback).executeRequest();
            }
        }).start();
    }


    public void getProductDetail(Product product, ECSCallback<Product, Exception> ecsCallback) {

        new AssetServiceDiscoveryRequest(product.getCode()).getRequestUrlFromAppInfra(new ServiceDiscoveryRequest.OnUrlReceived() {
            @Override
            public void onSuccess(String url) {
                getProductAsset(url, new ECSCallback<Assets, Exception>() {
                    @Override
                    public void onResponse(Assets result) {
                        if (null != result) {
                            product.setAssets(result);
                            getDisclaimer(product, ecsCallback);

                        } else {
                            ecsCallback.onFailure(new Exception(ECS_NO_PRODUCT_DETAIL_FOUND), 5002);
                        }
                    }

                    @Override
                    public void onFailure(Exception error, int errorCode) {
                        ecsCallback.onFailure(error, errorCode);
                    }
                });
            }

            @Override
            public void onError(ERRORVALUES errorvalues, String s) {
                ecsCallback.onFailure(new Exception(ECS_NO_PRODUCT_DETAIL_FOUND), 5002);
            }
        });


    }

    private void getDisclaimer(Product product, ECSCallback<Product, Exception> ecsCallback) {
        new DisclaimerServiceDiscoveryRequest(product.getCode()).getRequestUrlFromAppInfra(new ServiceDiscoveryRequest.OnUrlReceived() {
            @Override
            public void onSuccess(String url) {

                getProductDisclaimer(url, new ECSCallback<Disclaimers, Exception>() {
                    @Override
                    public void onResponse(Disclaimers result) {
                        // here result can come as null if Disclaimer not present for given product
                        // but still Product Detail will be success as asset is already fetched
                        product.setDisclaimers(result);
                        ecsCallback.onResponse(product);
                    }

                    @Override
                    public void onFailure(Exception error, int errorCode) {
                        // even if Disclaimer request fails the Product detail call be success as Asset has been already fetched
                        ecsCallback.onResponse(product);
                    }
                });

            }

            @Override
            public void onError(ERRORVALUES errorvalues, String s) {
                // even if Disclaimer request fails the Product detail call be success as Asset has been already fetched
                ecsCallback.onResponse(product);
            }


        });
    }

    private ProductSummaryListServiceDiscoveryRequest prepareProductSummaryListRequest(List<String> ctns) {
        ProductSummaryListServiceDiscoveryRequest productSummaryListServiceDiscoveryRequest = new ProductSummaryListServiceDiscoveryRequest(ctns);
        return productSummaryListServiceDiscoveryRequest;
    }

    public void getSummary(List<String> ctns, ECSCallback<List<Product>, Exception> ecsCallback) {

        Products products = new Products();

        ArrayList<Product> productArrayList = new ArrayList<>();

        for (String ctn : ctns) {
            Product product = new Product();
            product.setCode(ctn);
            productArrayList.add(product);
        }
        products.setProducts(productArrayList);

        prepareProductSummaryURL(products, new ECSCallback<Products, Exception>() {
            @Override
            public void onResponse(Products result) {
                ecsCallback.onResponse(result.getProducts());
            }

            @Override
            public void onFailure(Exception error, int errorCode) {
                ecsCallback.onFailure(error, errorCode);
            }
        });
    }
}
