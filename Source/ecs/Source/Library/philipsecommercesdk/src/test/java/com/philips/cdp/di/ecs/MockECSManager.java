package com.philips.cdp.di.ecs;

import com.philips.cdp.di.ecs.Cart.MockCreateECSShoppingCartRequest;
import com.philips.cdp.di.ecs.Cart.MockGetECSShoppingCartsRequest;
import com.philips.cdp.di.ecs.Oath.MockOAuthRequest;
import com.philips.cdp.di.ecs.ProductCatalog.MockGetProductListRequest;
import com.philips.cdp.di.ecs.ProductCatalog.MockGetProductSummaryListRequest;
import com.philips.cdp.di.ecs.ProductDetail.MockGetProductAssetRequest;
import com.philips.cdp.di.ecs.ProductDetail.MockGetProductDisclaimerRequest;
import com.philips.cdp.di.ecs.ProductForCTN.MockGetProductForRequest;
import com.philips.cdp.di.ecs.integration.ECSCallback;
import com.philips.cdp.di.ecs.integration.OAuthInput;
import com.philips.cdp.di.ecs.model.asset.Assets;
import com.philips.cdp.di.ecs.model.cart.ECSShoppingCart;
import com.philips.cdp.di.ecs.model.disclaimer.Disclaimers;
import com.philips.cdp.di.ecs.model.products.Product;
import com.philips.cdp.di.ecs.model.products.Products;
import com.philips.cdp.di.ecs.model.response.OAuthResponse;
import com.philips.cdp.di.ecs.model.summary.ECSProductSummary;

public class MockECSManager extends ECSManager {



    String jsonFileNameMockECSManager;

    public String getJsonFileNameMockECSManager() {
        return jsonFileNameMockECSManager;
    }

    public void setJsonFileNameMockECSManager(String jsonFileNameMockECSManager) {
        this.jsonFileNameMockECSManager = jsonFileNameMockECSManager;
    }

    @Override
    public void getOAuth(OAuthInput oAuthInput, ECSCallback<OAuthResponse, Exception> ecsCallback) {

        MockOAuthRequest mockOAuthRequest = new MockOAuthRequest(getJsonFileNameMockECSManager(),oAuthInput,ecsCallback);
        mockOAuthRequest.executeRequest();
    }

    @Override
    public void getProductList(int currentPage, int pageSize, ECSCallback<Products, Exception> ecsCallback) {


                MockGetProductListRequest getMockGetProductRequest = new MockGetProductListRequest(getJsonFileNameMockECSManager(),currentPage, pageSize, new ECSCallback<Products, Exception>() {
                    @Override
                    public void onResponse(Products result) {
                        //prepareProductSummaryURL(result,ecsCallback);
                        setJsonFileNameMockECSManager("PRXSummaryResponse.json");
                        new MockGetProductSummaryListRequest(getJsonFileNameMockECSManager(),"", new ECSCallback<ECSProductSummary, Exception>() {
                            @Override
                            public void onResponse(ECSProductSummary eCSProductSummary) {

                                updateProductsWithSummary(result,eCSProductSummary);
                                ecsCallback.onResponse(result);
                            }

                            @Override
                            public void onFailure(Exception error,String errorMessage, int errorCode) {

                            }
                       }).executeRequest();
                    }

                    @Override
                    public void onFailure(Exception error,String errorMessage, int errorCode) {
                        ecsCallback.onFailure(error,errorMessage,errorCode);

                    }
                });
                getMockGetProductRequest.executeRequest();

            }


    @Override
    void getProductSummary(String url, ECSCallback<ECSProductSummary, Exception> eCSCallback) {
        new MockGetProductSummaryListRequest(getJsonFileNameMockECSManager(),url,eCSCallback).executeRequest();
    }

    @Override
    public void getProductDetail(Product product, ECSCallback<Product, Exception> ecsCallback) {
        new MockGetProductAssetRequest(getJsonFileNameMockECSManager(),"mockURL", new ECSCallback<Assets, Exception>() {
            @Override
            public void onResponse(Assets result) {
                product.setAssets(result);
                setJsonFileNameMockECSManager("PRXDisclaimers.json");
                new MockGetProductDisclaimerRequest(getJsonFileNameMockECSManager(),"mockURL", new ECSCallback<Disclaimers, Exception>() {
                    @Override
                    public void onResponse(Disclaimers result) {
                        product.setDisclaimers(result);
                        ecsCallback.onResponse(product);
                    }

                    @Override
                    public void onFailure(Exception error,String errorMessage, int errorCode) {
                        // even if Disclaimer request fails the Product detail call be success as Asset has been already fetched
                        ecsCallback.onResponse(product);
                    }
                }).executeRequest();

            }

            @Override
            public void onFailure(Exception error,String errorMessage, int errorCode) {
                // even if Disclaimer request fails the Product detail call be success as Asset has been already fetched
                ecsCallback.onFailure(error,errorMessage,errorCode);
            }
        }).executeRequest();
    }

    @Override
    public void getProductFor(String ctn, ECSCallback<Product, Exception> eCSCallback) {
        new MockGetProductForRequest(getJsonFileNameMockECSManager(),ctn,eCSCallback).executeRequest();
    }

    @Override
    void createECSShoppingCart(ECSCallback<ECSShoppingCart, Exception> ecsCallback) {
        new MockCreateECSShoppingCartRequest(getJsonFileNameMockECSManager(),ecsCallback ).executeRequest();
    }

    @Override
    void getECSShoppingCart(ECSCallback<ECSShoppingCart, Exception> ecsCallback) {
        new MockGetECSShoppingCartsRequest(getJsonFileNameMockECSManager(),ecsCallback ).executeRequest();
    }

    @Override
    public void addProductToShoppingCart(Product product, ECSCallback<ECSShoppingCart, Exception> ecsCallback) {
        super.addProductToShoppingCart(product, ecsCallback);
    }
}
