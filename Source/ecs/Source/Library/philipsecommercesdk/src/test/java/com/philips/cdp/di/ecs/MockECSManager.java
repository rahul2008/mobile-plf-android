package com.philips.cdp.di.ecs;

import com.google.gson.Gson;
import com.philips.cdp.di.ecs.ECSManager;
import com.philips.cdp.di.ecs.ProductCatalog.MockGetProductRequest;
import com.philips.cdp.di.ecs.ProductCatalog.MockGetProductSummaryListRequest;
import com.philips.cdp.di.ecs.ProductDetail.MockGetProductAssetRequest;
import com.philips.cdp.di.ecs.ProductDetail.MockGetProductDisclaimerRequest;
import com.philips.cdp.di.ecs.TestUtil;
import com.philips.cdp.di.ecs.integration.ECSCallback;
import com.philips.cdp.di.ecs.model.asset.Assets;
import com.philips.cdp.di.ecs.model.disclaimer.Disclaimers;
import com.philips.cdp.di.ecs.model.products.Product;
import com.philips.cdp.di.ecs.model.products.Products;
import com.philips.cdp.di.ecs.model.summary.ECSProductSummary;
import com.philips.cdp.di.ecs.request.GetProductRequest;
import com.philips.cdp.di.ecs.request.GetProductSummaryListRequest;

import java.io.InputStream;

import static com.philips.cdp.di.ecs.util.ECSErrorReason.ECS_NO_PRODUCT_DETAIL_FOUND;
import static com.philips.cdp.di.ecs.util.ECSErrorReason.ECS_UNKNOWN_ERROR;

public class MockECSManager extends ECSManager {

    @Override
    public void getProductList(int currentPage, int pageSize, ECSCallback<Products, Exception> ecsCallback) {

       // new MockGetProductRequest(currentPage, pageSize, ecsCallback).executeRequest();


                MockGetProductRequest getMockGetProductRequest = new MockGetProductRequest(currentPage, pageSize, new ECSCallback<Products, Exception>() {
                    @Override
                    public void onResponse(Products result) {
                        //prepareProductSummaryURL(result,ecsCallback);
                        new MockGetProductSummaryListRequest("", new ECSCallback<ECSProductSummary, Exception>() {
                            @Override
                            public void onResponse(ECSProductSummary eCSProductSummary) {

                                updateProductsWithSummary(result,eCSProductSummary);
                                ecsCallback.onResponse(result);
                            }

                            @Override
                            public void onFailure(Exception error, int errorCode) {

                            }
                        }).executeRequest();
                    }

                    @Override
                    public void onFailure(Exception error, int errorCode) {
                        ecsCallback.onFailure(new Exception(ECS_UNKNOWN_ERROR),999);

                    }
                });
                getMockGetProductRequest.executeRequest();
            }


    @Override
    void getProductSummary(String url, ECSCallback<ECSProductSummary, Exception> eCSCallback) {
        new MockGetProductSummaryListRequest(url,eCSCallback).executeRequest();
    }

    @Override
    public void getProductDetail(Product product, ECSCallback<Product, Exception> ecsCallback) {
       // super.getProductDetail(product, ecsCallback);
        new MockGetProductAssetRequest("mockURL", new ECSCallback<Assets, Exception>() {
            @Override
            public void onResponse(Assets result) {
                product.setAssets(result);
                new MockGetProductDisclaimerRequest("mockURL", new ECSCallback<Disclaimers, Exception>() {
                    @Override
                    public void onResponse(Disclaimers result) {
                        product.setDisclaimers(result);
                        ecsCallback.onResponse(product);
                    }

                    @Override
                    public void onFailure(Exception error, int errorCode) {
                        // even if Disclaimer request fails the Product detail call be success as Asset has been already fetched
                        ecsCallback.onResponse(product);
                    }
                }).executeRequest();

            }

            @Override
            public void onFailure(Exception error, int errorCode) {
                // even if Disclaimer request fails the Product detail call be success as Asset has been already fetched
                ecsCallback.onFailure(new Exception(ECS_NO_PRODUCT_DETAIL_FOUND),5002);
            }
        }).executeRequest();
    }
}
