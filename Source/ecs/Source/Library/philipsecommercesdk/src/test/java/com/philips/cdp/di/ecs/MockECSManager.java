package com.philips.cdp.di.ecs;

import com.philips.cdp.di.ecs.Address.MockCreateAddressRequest;
import com.philips.cdp.di.ecs.Address.MockDeleteAddressRequest;
import com.philips.cdp.di.ecs.Address.MockGetAddressRequest;
import com.philips.cdp.di.ecs.Address.MockSetDeliveryAddressRequest;
import com.philips.cdp.di.ecs.Address.MockUpdateAddressRequest;
import com.philips.cdp.di.ecs.Cart.MockAddProductToECSShoppingCartRequest;
import com.philips.cdp.di.ecs.Cart.MockCreateECSShoppingCartRequest;
import com.philips.cdp.di.ecs.Cart.MockGetECSShoppingCartsRequest;
import com.philips.cdp.di.ecs.Cart.MockUpdateECSShoppingCartQuantityRequest;
import com.philips.cdp.di.ecs.Config.MockGetConfigurationRequest;
import com.philips.cdp.di.ecs.Oath.MockOAuthRequest;
import com.philips.cdp.di.ecs.Payment.MockGetPaymentsRequest;
import com.philips.cdp.di.ecs.Payment.MockMakePaymentRequest;
import com.philips.cdp.di.ecs.Payment.MockPlaceOrderRequest;
import com.philips.cdp.di.ecs.Payment.MockSetPaymentMethodRequest;
import com.philips.cdp.di.ecs.ProductCatalog.MockGetProductListRequest;
import com.philips.cdp.di.ecs.ProductCatalog.MockGetProductSummaryListRequest;
import com.philips.cdp.di.ecs.ProductDetail.MockGetProductAssetRequest;
import com.philips.cdp.di.ecs.ProductDetail.MockGetProductDisclaimerRequest;
import com.philips.cdp.di.ecs.ProductForCTN.MockGetProductForRequest;
import com.philips.cdp.di.ecs.Region.MockGetRegionsRequest;
import com.philips.cdp.di.ecs.Voucher.MockGetVouchersRequest;
import com.philips.cdp.di.ecs.Voucher.MockRemoveVoucherRequest;
import com.philips.cdp.di.ecs.Voucher.MockSetVoucherRequest;
import com.philips.cdp.di.ecs.DeliveryMode.MockDeliveryModesRequest;
import com.philips.cdp.di.ecs.DeliveryMode.MockSetDeliveryModesRequest;
import com.philips.cdp.di.ecs.integration.ECSCallback;
import com.philips.cdp.di.ecs.integration.OAuthInput;
import com.philips.cdp.di.ecs.model.address.Addresses;
import com.philips.cdp.di.ecs.model.address.GetDeliveryModes;
import com.philips.cdp.di.ecs.model.address.GetShippingAddressData;
import com.philips.cdp.di.ecs.model.asset.Assets;
import com.philips.cdp.di.ecs.model.cart.ECSShoppingCart;
import com.philips.cdp.di.ecs.model.cart.EntriesEntity;
import com.philips.cdp.di.ecs.model.disclaimer.Disclaimers;
import com.philips.cdp.di.ecs.model.order.OrdersData;
import com.philips.cdp.di.ecs.model.orders.OrderDetail;
import com.philips.cdp.di.ecs.model.payment.MakePaymentData;
import com.philips.cdp.di.ecs.model.payment.PaymentMethods;
import com.philips.cdp.di.ecs.model.products.Product;
import com.philips.cdp.di.ecs.model.products.Products;
import com.philips.cdp.di.ecs.model.region.RegionsList;
import com.philips.cdp.di.ecs.model.config.HybrisConfigResponse;
import com.philips.cdp.di.ecs.model.oauth.OAuthResponse;
import com.philips.cdp.di.ecs.model.retailers.WebResults;
import com.philips.cdp.di.ecs.model.summary.ECSProductSummary;
import com.philips.cdp.di.ecs.model.user.UserProfile;
import com.philips.cdp.di.ecs.model.voucher.GetAppliedValue;
import com.philips.cdp.di.ecs.orderHistory.MockGetOrderDetailRequest;
import com.philips.cdp.di.ecs.orderHistory.MockGetOrderHistoryRequest;
import com.philips.cdp.di.ecs.request.AddProductToECSShoppingCartRequest;
import com.philips.cdp.di.ecs.request.CreateAddressRequest;
import com.philips.cdp.di.ecs.request.CreateECSShoppingCartRequest;
import com.philips.cdp.di.ecs.request.GetConfigurationRequest;
import com.philips.cdp.di.ecs.request.GetECSShoppingCartsRequest;
import com.philips.cdp.di.ecs.request.GetVouchersRequest;
import com.philips.cdp.di.ecs.request.SetVoucherRequest;
import com.philips.cdp.di.ecs.request.UpdateECSShoppingCartQuantityRequest;
import com.philips.cdp.di.ecs.retailer.MockGetRetailersInfoRequest;
import com.philips.cdp.di.ecs.userProfile.MockGetUserProfileRequest;
import com.philips.cdp.di.ecs.util.ECSConfig;

public class MockECSManager extends ECSManager {



    String jsonFileNameMockECSManager;

    public String getJsonFileNameMockECSManager() {
        return jsonFileNameMockECSManager;
    }

    public void setJsonFileNameMockECSManager(String jsonFileNameMockECSManager) {
        this.jsonFileNameMockECSManager = jsonFileNameMockECSManager;
    }


    @Override
    GetConfigurationRequest getConfigurationRequestObject(ECSCallback<HybrisConfigResponse, Exception> eCSCallback) {
        return new MockGetConfigurationRequest(getJsonFileNameMockECSManager(),eCSCallback);
    }

    @Override
    public void getRegions(ECSCallback<RegionsList, Exception> ecsCallback) {
        new MockGetRegionsRequest(getJsonFileNameMockECSManager(), new ECSCallback<RegionsList, Exception>() {
            @Override
            public void onResponse(RegionsList result) {
                ecsCallback.onResponse(result);
            }

            @Override
            public void onFailure(Exception error, int errorCode) {
                ecsCallback.onFailure(error, errorCode);
            }
        }).executeRequest();
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
                            public void onFailure(Exception error, int errorCode) {

                            }
                       }).executeRequest();
                    }

                    @Override
                    public void onFailure(Exception error, int errorCode) {
                        ecsCallback.onFailure(error, errorCode);

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
                    public void onFailure(Exception error, int errorCode) {
                        // even if Disclaimer request fails the Product detail call be success as Asset has been already fetched
                        ecsCallback.onResponse(product);
                    }
                }).executeRequest();

            }

            @Override
            public void onFailure(Exception error, int errorCode) {
                // even if Disclaimer request fails the Product detail call be success as Asset has been already fetched
                ecsCallback.onFailure(error, errorCode);
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
    CreateECSShoppingCartRequest createECSShoppingCartRequestObject(ECSCallback<ECSShoppingCart, Exception> ecsCallback) {
        return new MockCreateECSShoppingCartRequest(getJsonFileNameMockECSManager(),ecsCallback);
    }



    @Override
    public GetECSShoppingCartsRequest getShoppingCartsRequestObject(ECSCallback<ECSShoppingCart, Exception> ecsCallback1) {
        return new MockGetECSShoppingCartsRequest(getJsonFileNameMockECSManager(),ecsCallback1 );
    }


    @Override
    AddProductToECSShoppingCartRequest addProductToECSShoppingCartRequestObject(String code, ECSCallback<Boolean, Exception> ecsCallback) {
        return new MockAddProductToECSShoppingCartRequest(getJsonFileNameMockECSManager(),code, ecsCallback);
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
            public void onFailure(Exception error, int errorCode) {
                //getECSShoppingCart(ecsCallback);
                ecsCallback.onFailure(error, errorCode);

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
            public void onFailure(Exception error, int errorCode) {
                getECSShoppingCart(ecsCallback);
            }
        }, entriesEntity, quantity).executeRequest();
    }


    @Override
    GetVouchersRequest getVouchersRequestObject(ECSCallback<GetAppliedValue, Exception> ecsCallback) {
        return new MockGetVouchersRequest(getJsonFileNameMockECSManager(),ecsCallback);
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
            public void onFailure(Exception error, int errorCode) {
                ecsCallback.onFailure(error, errorCode);
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
            public void onFailure(Exception error, int errorCode) {
                ecsCallback.onFailure(error, errorCode);
            }
        }).executeRequest();
    }


    @Override
    CreateAddressRequest createAddressRequestObject(Addresses address, ECSCallback<Addresses, Exception> ecsCallback) {
        return new MockCreateAddressRequest(getJsonFileNameMockECSManager(),address, ecsCallback);
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
            public void onFailure(Exception error, int errorCode) {
                ecsCallback.onFailure(error, errorCode);
            }
        }).executeRequest();

    }


    @Override
    public void getListSavedAddress(ECSCallback<GetShippingAddressData, Exception> ecsCallback) {
        MockGetAddressRequest mockGetAddressRequest=  new MockGetAddressRequest(getJsonFileNameMockECSManager(), new ECSCallback<GetShippingAddressData, Exception>() {
            @Override
            public void onResponse(GetShippingAddressData result) {
                ecsCallback.onResponse(result);
            }

            @Override
            public void onFailure(Exception error, int errorCode) {
            ecsCallback.onFailure(error, errorCode);
            }
        });
        mockGetAddressRequest.getParams();
        mockGetAddressRequest.getHeader();
        mockGetAddressRequest.getMethod();
        mockGetAddressRequest.getStringSuccessResponseListener();
        mockGetAddressRequest.executeRequest();
    }

    @Override
    public void updateAddress(Addresses address, ECSCallback<Boolean, Exception> ecsCallback) {
        MockUpdateAddressRequest mockUpdateAddressRequest=  new MockUpdateAddressRequest(getJsonFileNameMockECSManager(), address, new ECSCallback<Boolean, Exception>() {
            @Override
            public void onResponse(Boolean result) {
                ecsCallback.onResponse(result);
            }

            @Override
            public void onFailure(Exception error, int errorCode) {
                ecsCallback.onFailure(error, errorCode);
            }
        });
        mockUpdateAddressRequest.getParams();
        mockUpdateAddressRequest.getHeader();
        mockUpdateAddressRequest.getMethod();
        mockUpdateAddressRequest.getStringSuccessResponseListener();
        mockUpdateAddressRequest.executeRequest();
    }

    @Override
    public void deleteAddress(Addresses address, ECSCallback<GetShippingAddressData, Exception> ecsCallback) {
        MockDeleteAddressRequest mockDeleteAddressRequest=  new MockDeleteAddressRequest(getJsonFileNameMockECSManager(), address, new ECSCallback<Boolean, Exception>() {
            @Override
            public void onResponse(Boolean result) {
                setJsonFileNameMockECSManager("ShippingAddressListSuccess.json");
                getListSavedAddress(ecsCallback);
            }

            @Override
            public void onFailure(Exception error, int errorCode) {
                ecsCallback.onFailure(error, errorCode);
            }
        });
        mockDeleteAddressRequest.getParams();
        mockDeleteAddressRequest.getHeader();
        mockDeleteAddressRequest.getMethod();
        mockDeleteAddressRequest.getStringSuccessResponseListener();
        mockDeleteAddressRequest.executeRequest();
    }

    @Override
    public void setDeliveryAddress(Addresses address, ECSCallback<Boolean, Exception> ecsCallback) {
        MockSetDeliveryAddressRequest mockSetDeliveryAddressRequest=  new MockSetDeliveryAddressRequest(getJsonFileNameMockECSManager(), address.getId(), new ECSCallback<Boolean, Exception>() {
           @Override
           public void onResponse(Boolean result) {
               ecsCallback.onResponse(result);
           }

           @Override
           public void onFailure(Exception error, int errorCode) {
               ecsCallback.onFailure(error, errorCode);
           }
       });
        mockSetDeliveryAddressRequest.getParams();
        mockSetDeliveryAddressRequest.getHeader();
        mockSetDeliveryAddressRequest.getMethod();
        mockSetDeliveryAddressRequest.getStringSuccessResponseListener();
        mockSetDeliveryAddressRequest.executeRequest();
    }

    @Override
    public void getDeliveryModes(ECSCallback<GetDeliveryModes, Exception> ecsCallback) {
        new MockDeliveryModesRequest(ecsCallback,getJsonFileNameMockECSManager()).executeRequest();
    }

    @Override
    public void setDeliveryMode(String deliveryModeID, ECSCallback<Boolean, Exception> ecsCallback) {
        new MockSetDeliveryModesRequest(deliveryModeID,ecsCallback,getJsonFileNameMockECSManager()).executeRequest();
    }

    @Override
    public void getPayments(ECSCallback<PaymentMethods, Exception> ecsCallback) {
        new MockGetPaymentsRequest(getJsonFileNameMockECSManager(),ecsCallback).executeRequest();
    }

    @Override
    public void setPaymentMethod(String paymentDetailsId, ECSCallback<Boolean, Exception> ecsCallback) {
        new MockSetPaymentMethodRequest(paymentDetailsId,ecsCallback,getJsonFileNameMockECSManager()).executeRequest();
    }

    @Override
    public void getOrderHistory(int pageNumber, ECSCallback<OrdersData, Exception> ecsCallback) {
        new MockGetOrderHistoryRequest(getJsonFileNameMockECSManager(),pageNumber,ecsCallback).executeRequest();
    }

    @Override
    public void getOrderDetail(String orderId, ECSCallback<OrderDetail, Exception> ecsCallback) {
        new MockGetOrderDetailRequest(getJsonFileNameMockECSManager(),orderId,ecsCallback).executeRequest();
    }

    @Override
    public void getRetailers(String productID, ECSCallback<WebResults, Exception> ecsCallback) {
        new MockGetRetailersInfoRequest(getJsonFileNameMockECSManager(),ecsCallback,productID).executeRequest();
    }

    @Override
    public void makePayment(OrderDetail orderDetail, Addresses billingAddress, ECSCallback<MakePaymentData, Exception> ecsCallback) {
        new MockMakePaymentRequest(getJsonFileNameMockECSManager(),orderDetail,billingAddress,ecsCallback).executeRequest();
    }

    @Override
    public void submitOrder(String cvv, ECSCallback<OrderDetail, Exception> ecsCallback) {
        new MockPlaceOrderRequest(getJsonFileNameMockECSManager(),cvv,ecsCallback).executeRequest();
    }

    @Override
    public void getUserProfile(ECSCallback<UserProfile, Exception> ecsCallback) {
        new MockGetUserProfileRequest(getJsonFileNameMockECSManager(),ecsCallback).executeRequest();
    }
}
