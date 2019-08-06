package com.philips.cdp.di.ecs;

import com.philips.cdp.di.ecs.Address.MockCreateAddressRequest;
import com.philips.cdp.di.ecs.Address.MockGetAddressRequest;
import com.philips.cdp.di.ecs.Cart.MockAddProductToECSShoppingCartRequest;
import com.philips.cdp.di.ecs.Cart.MockCreateECSShoppingCartRequest;
import com.philips.cdp.di.ecs.Cart.MockGetECSShoppingCartsRequest;
import com.philips.cdp.di.ecs.Cart.MockUpdateECSShoppingCartQuantityRequest;
import com.philips.cdp.di.ecs.Oath.MockOAuthRequest;
import com.philips.cdp.di.ecs.ProductCatalog.MockGetProductListRequest;
import com.philips.cdp.di.ecs.ProductCatalog.MockGetProductSummaryListRequest;
import com.philips.cdp.di.ecs.ProductDetail.MockGetProductAssetRequest;
import com.philips.cdp.di.ecs.ProductDetail.MockGetProductDisclaimerRequest;
import com.philips.cdp.di.ecs.ProductForCTN.MockGetProductForRequest;
import com.philips.cdp.di.ecs.Voucher.MockGetVouchersRequest;
import com.philips.cdp.di.ecs.Voucher.MockRemoveVoucherRequest;
import com.philips.cdp.di.ecs.Voucher.MockSetVoucherRequest;
import com.philips.cdp.di.ecs.integration.ECSCallback;
import com.philips.cdp.di.ecs.integration.OAuthInput;
import com.philips.cdp.di.ecs.model.address.Addresses;
import com.philips.cdp.di.ecs.model.address.GetShippingAddressData;
import com.philips.cdp.di.ecs.model.asset.Assets;
import com.philips.cdp.di.ecs.model.cart.ECSShoppingCart;
import com.philips.cdp.di.ecs.model.cart.EntriesEntity;
import com.philips.cdp.di.ecs.model.disclaimer.Disclaimers;
import com.philips.cdp.di.ecs.model.products.Product;
import com.philips.cdp.di.ecs.model.products.Products;
import com.philips.cdp.di.ecs.model.response.OAuthResponse;
import com.philips.cdp.di.ecs.model.summary.ECSProductSummary;
import com.philips.cdp.di.ecs.model.voucher.GetAppliedValue;
import com.philips.cdp.di.ecs.request.RemoveVoucherRequest;
import com.philips.cdp.di.ecs.request.SetVoucherRequest;
import com.philips.cdp.di.ecs.util.ECSConfig;
import com.philips.cdp.di.ecs.util.ECSErrorReason;

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

     /*   if (null != ECSConfig.INSTANCE.getSiteId()) { // hybris flow

            new MockGetProductForRequest("",ctn, new ECSCallback<Product, Exception>() {
                @Override
                public void onResponse(Product result) {
                    getSummaryForCTN(ctn, result, eCSCallback);
                }


                @Override
                public void onFailure(Exception error,String detailErrorMessage, int errorCode) {
                    eCSCallback.onFailure(new Exception(ECSErrorReason.ECS_GIVEN_PRODUCT_NOT_FOUND),detailErrorMessage, 5999);
                }
            }).executeRequest();

        } else { // Retailer flow
            getSummaryForCTN(ctn, null, eCSCallback);
        }*/
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
        new MockAddProductToECSShoppingCartRequest(getJsonFileNameMockECSManager(),product.getCode(), new ECSCallback<Boolean, Exception>() {
            @Override
            public void onResponse(Boolean result) {
                setJsonFileNameMockECSManager("ShoppingCartSuccess.json");
                getECSShoppingCart(ecsCallback);
            }

            @Override
            public void onFailure(Exception error, String detailErrorMessage, int errorCode) {
                //getECSShoppingCart(ecsCallback);
                ecsCallback.onFailure(error, detailErrorMessage,errorCode);

            }
        }).executeRequest();
    }

    @Override
    public void updateQuantity(int quantity, EntriesEntity entriesEntity, ECSCallback<ECSShoppingCart, Exception> ecsCallback) {
        new MockUpdateECSShoppingCartQuantityRequest(getJsonFileNameMockECSManager(), new ECSCallback<Boolean, Exception>() {
            @Override
            public void onResponse(Boolean result) {
                setJsonFileNameMockECSManager("UpdateShoppingCart_GetShoppingCartSuccess.json");
                getECSShoppingCart(ecsCallback);
            }

            @Override
            public void onFailure(Exception error, String detailErrorMessage, int errorCode) {
                getECSShoppingCart(ecsCallback);
            }
        }, entriesEntity, quantity).executeRequest();
    }

    @Override
    public void getVoucher(ECSCallback<GetAppliedValue, Exception> ecsCallback) {
        new MockGetVouchersRequest(getJsonFileNameMockECSManager(), ecsCallback).executeRequest();
    }

    @Override
    public void setVoucher(String voucherCode, ECSCallback<GetAppliedValue, Exception> ecsCallback) {
        new MockSetVoucherRequest(getJsonFileNameMockECSManager(),voucherCode, new ECSCallback<Boolean, Exception>() {
            @Override
            public void onResponse(Boolean result) {
                setJsonFileNameMockECSManager("GetVoucherSuccess.json");
                getVoucher(ecsCallback);
            }

            @Override
            public void onFailure(Exception error, String detailErrorMessage, int errorCode) {
                ecsCallback.onFailure(error,detailErrorMessage,errorCode);
            }
        }).executeRequest();
    }

    @Override
    public void removeVoucher(String voucherCode, ECSCallback<GetAppliedValue, Exception> ecsCallback) {
        new MockRemoveVoucherRequest(getJsonFileNameMockECSManager(),voucherCode, new ECSCallback<Boolean, Exception>() {
            @Override
            public void onResponse(Boolean result) {
                setJsonFileNameMockECSManager("EmptyJson.json");
                getVoucher(ecsCallback);
            }

            @Override
            public void onFailure(Exception error, String detailErrorMessage, int errorCode) {
                ecsCallback.onFailure(error,detailErrorMessage,errorCode);
            }
        }).executeRequest();
    }

    @Override
    public void createNewAddress(Addresses address, ECSCallback<Addresses, Exception> ecsCallback, boolean singleAddress) {
        new MockCreateAddressRequest(getJsonFileNameMockECSManager(), address,ecsCallback).executeRequest();
    }

    @Override
    public void createNewAddress(Addresses address, ECSCallback<GetShippingAddressData, Exception> ecsCallback) {
        new MockCreateAddressRequest(getJsonFileNameMockECSManager(), address, new ECSCallback<Addresses, Exception>() {
            @Override
            public void onResponse(Addresses result) {
                setJsonFileNameMockECSManager("ShippingAddressListSuccess.json");
                getListSavedAddress(ecsCallback);

            }

            @Override
            public void onFailure(Exception error, String detailErrorMessage, int errorCode) {
                ecsCallback.onFailure(error,detailErrorMessage,errorCode);
            }
        }).executeRequest();

    }


    @Override
    public void getListSavedAddress(ECSCallback<GetShippingAddressData, Exception> ecsCallback) {
        new MockGetAddressRequest(getJsonFileNameMockECSManager(), new ECSCallback<GetShippingAddressData, Exception>() {
            @Override
            public void onResponse(GetShippingAddressData result) {
                ecsCallback.onResponse(result);
            }

            @Override
            public void onFailure(Exception error, String detailErrorMessage, int errorCode) {
            ecsCallback.onFailure(error,detailErrorMessage,errorCode);
            }
        }).executeRequest();
    }
}
