package com.philips.cdp.di.ecs;

import com.philips.cdp.di.ecs.error.ECSError;
import com.philips.cdp.di.ecs.error.ECSErrorEnum;
import com.philips.cdp.di.ecs.error.ECSErrorWrapper;
import com.philips.cdp.di.ecs.integration.ECSCallback;
import com.philips.cdp.di.ecs.integration.OAuthInput;
import com.philips.cdp.di.ecs.model.address.Addresses;
import com.philips.cdp.di.ecs.model.address.GetDeliveryModes;
import com.philips.cdp.di.ecs.model.address.GetShippingAddressData;
import com.philips.cdp.di.ecs.model.asset.Assets;
import com.philips.cdp.di.ecs.model.cart.ECSShoppingCart;
import com.philips.cdp.di.ecs.model.cart.EntriesEntity;
import com.philips.cdp.di.ecs.model.disclaimer.Disclaimers;
import com.philips.cdp.di.ecs.model.order.Orders;
import com.philips.cdp.di.ecs.model.order.OrdersData;
import com.philips.cdp.di.ecs.model.orders.Entries;
import com.philips.cdp.di.ecs.model.orders.OrderDetail;
import com.philips.cdp.di.ecs.model.payment.MakePaymentData;
import com.philips.cdp.di.ecs.model.payment.PaymentMethods;
import com.philips.cdp.di.ecs.model.products.Product;
import com.philips.cdp.di.ecs.model.products.Products;
import com.philips.cdp.di.ecs.model.region.RegionsList;
import com.philips.cdp.di.ecs.model.config.HybrisConfigResponse;
import com.philips.cdp.di.ecs.model.oauth.OAuthResponse;
import com.philips.cdp.di.ecs.model.retailers.WebResults;
import com.philips.cdp.di.ecs.model.summary.Data;
import com.philips.cdp.di.ecs.model.summary.ECSProductSummary;
import com.philips.cdp.di.ecs.model.user.UserProfile;
import com.philips.cdp.di.ecs.model.voucher.GetAppliedValue;
import com.philips.cdp.di.ecs.prx.serviceDiscovery.AssetServiceDiscoveryRequest;
import com.philips.cdp.di.ecs.prx.serviceDiscovery.DisclaimerServiceDiscoveryRequest;
import com.philips.cdp.di.ecs.prx.serviceDiscovery.ProductSummaryListServiceDiscoveryRequest;
import com.philips.cdp.di.ecs.prx.serviceDiscovery.ServiceDiscoveryRequest;
import com.philips.cdp.di.ecs.request.CreateAddressRequest;
import com.philips.cdp.di.ecs.request.DeleteAddressRequest;
import com.philips.cdp.di.ecs.request.GetAddressRequest;
import com.philips.cdp.di.ecs.request.GetDeliveryModesRequest;
import com.philips.cdp.di.ecs.request.GetOrderDetailRequest;
import com.philips.cdp.di.ecs.request.GetOrderHistoryRequest;
import com.philips.cdp.di.ecs.request.GetPaymentsRequest;
import com.philips.cdp.di.ecs.request.GetRegionsRequest;
import com.philips.cdp.di.ecs.request.GetRetailersInfoRequest;
import com.philips.cdp.di.ecs.request.GetUserProfileRequest;
import com.philips.cdp.di.ecs.request.GetVouchersRequest;
import com.philips.cdp.di.ecs.request.MakePaymentRequest;
import com.philips.cdp.di.ecs.request.RemoveVoucherRequest;
import com.philips.cdp.di.ecs.request.SetDeliveryAddressRequest;
import com.philips.cdp.di.ecs.request.SetDeliveryModesRequest;
import com.philips.cdp.di.ecs.request.SetPaymentMethodRequest;
import com.philips.cdp.di.ecs.request.SetVoucherRequest;
import com.philips.cdp.di.ecs.request.SubmitOrderRequest;
import com.philips.cdp.di.ecs.request.UpdateAddressRequest;
import com.philips.cdp.di.ecs.request.UpdateECSShoppingCartQuantityRequest;
import com.philips.cdp.di.ecs.request.CreateECSShoppingCartRequest;
import com.philips.cdp.di.ecs.request.AddProductToECSShoppingCartRequest;
import com.philips.cdp.di.ecs.request.GetConfigurationRequest;
import com.philips.cdp.di.ecs.request.GetECSShoppingCartsRequest;
import com.philips.cdp.di.ecs.request.GetProductAssetRequest;
import com.philips.cdp.di.ecs.request.GetProductDisclaimerRequest;
import com.philips.cdp.di.ecs.request.GetProductListRequest;
import com.philips.cdp.di.ecs.request.GetProductForRequest;
import com.philips.cdp.di.ecs.request.GetProductSummaryListRequest;
import com.philips.cdp.di.ecs.request.OAuthRequest;
import com.philips.cdp.di.ecs.util.ECSConfig;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.philips.cdp.di.ecs.error.ECSNetworkError.getErrorLocalizedErrorMessage;


public class ECSManager {

    static int threadCount = 0;

    void getHybrisConfig(ECSCallback<Boolean, Exception> ecsCallback) {

        new GetConfigurationRequest(new ECSCallback<HybrisConfigResponse, Exception>() {
            @Override
            public void onResponse(HybrisConfigResponse result) {
                ECSConfig.INSTANCE.setSiteId(result.getSiteId());
                ECSConfig.INSTANCE.setRootCategory(result.getRootCategory());
                ecsCallback.onResponse(true);
            }

            @Override
            public void onFailure(Exception error, ECSError ecsError) {
                ecsCallback.onFailure(error, ecsError);
            }
        }).executeRequest();

    }

    void getHybrisConfigResponse(ECSCallback<HybrisConfigResponse, Exception> ecsCallback) {

        ECSCallback ecsCallback1 =  new ECSCallback<HybrisConfigResponse, Exception>() {
            @Override
            public void onResponse(HybrisConfigResponse result) {
                ECSConfig.INSTANCE.setSiteId(result.getSiteId());
                ECSConfig.INSTANCE.setRootCategory(result.getRootCategory());
                ecsCallback.onResponse(result);
            }

            @Override
            public void onFailure(Exception error, ECSError ecsError) {
                ecsCallback.onFailure(error, ecsError);
            }
        };
        GetConfigurationRequest getConfigurationRequest = getConfigurationRequestObject(ecsCallback1);
        getConfigurationRequest.executeRequest();
    }
    GetConfigurationRequest getConfigurationRequestObject(ECSCallback<HybrisConfigResponse, Exception> eCSCallback){
        return new GetConfigurationRequest(eCSCallback);
    }


     void getOAuth(OAuthInput oAuthInput, ECSCallback<OAuthResponse, Exception> ecsCallback) {
        new OAuthRequest(oAuthInput, ecsCallback).executeRequest();
    }

    //========================================= Start of PRX Product List, Product Detail & Product for CTN =======================================

     void getProductList(int currentPage, int pageSize, final ECSCallback<Products, Exception> finalEcsCallback) {

        GetProductListRequest getProductListRequest = new GetProductListRequest(currentPage, pageSize, new ECSCallback<Products, Exception>() {
            @Override
            public void onResponse(Products result) {
                prepareProductSummaryURL(result, finalEcsCallback);
            }

            @Override
            public void onFailure(Exception error, ECSError ecsError) {
                finalEcsCallback.onFailure(error, ecsError);
            }
        });
        getProductListRequest.executeRequest();

    }

     void getProductFor(String ctn, ECSCallback<Product, Exception> eCSCallback) {
        if (null != ECSConfig.INSTANCE.getSiteId()) { // hybris flow
            ECSCallback ecsCallback1 =  new ECSCallback<Product, Exception>() {
                @Override
                public void onResponse(Product result) {
                    getSummaryForCTN(ctn, result, eCSCallback);
                }

                @Override
                public void onFailure(Exception error, ECSError ecsError) {
                    eCSCallback.onFailure(error, ecsError);

                }
            };
            GetProductForRequest getProductForRequest = getProductForRequestObject(ctn,ecsCallback1);
        } else { // Retailer flow
            getSummaryForCTN(ctn, null, eCSCallback);
        }


    }
    GetProductForRequest getProductForRequestObject(String ctn, ECSCallback<Product, Exception> ecsCallback){
        return new GetProductForRequest(ctn,  ecsCallback);
    }


    private static void productDetail(Product product, ECSCallback<Product, Exception> ecsCallback) {
        threadCount++;
        if (threadCount == 2) {
            ecsCallback.onResponse(product);
        }
    }

     void getProductDetail(Product product, ECSCallback<Product, Exception> ecsCallback) {
        Thread assets = new Thread() {

            @Override
            public void run() {
                new AssetServiceDiscoveryRequest(product.getCode()).getRequestUrlFromAppInfra(new ServiceDiscoveryRequest.OnUrlReceived() {
                    @Override
                    public void onSuccess(String url) {
                        getProductAsset(url, new ECSCallback<Assets, Exception>() {
                            @Override
                            public void onResponse(Assets result) {
                                if (null != result) {
                                    product.setAssets(result);
                                    productDetail(product, ecsCallback);
                                } else {
                                    ECSErrorWrapper ecsErrorWrapper = getErrorLocalizedErrorMessage(ECSErrorEnum.ECSsomethingWentWrong,null,null);
                                    ecsCallback.onFailure(ecsErrorWrapper.getException(), ecsErrorWrapper.getEcsError());
                                }
                            }

                            @Override
                            public void onFailure(Exception error, ECSError ecsError) {
                                ecsCallback.onFailure(error, ecsError);
                            }
                        });
                    }

                    @Override
                    public void onError(ERRORVALUES errorvalues, String s) {
                          ECSErrorWrapper ecsErrorWrapper = getErrorLocalizedErrorMessage(ECSErrorEnum.ECSsomethingWentWrong,null,s);
                        ecsCallback.onFailure(ecsErrorWrapper.getException(), ecsErrorWrapper.getEcsError());
                    }
                });
            }
        };

        Thread disclaimer = new Thread() {

            @Override
            public void run() {
                new DisclaimerServiceDiscoveryRequest(product.getCode()).getRequestUrlFromAppInfra(new ServiceDiscoveryRequest.OnUrlReceived() {
                    @Override
                    public void onSuccess(String url) {
                        getProductDisclaimer(url, new ECSCallback<Disclaimers, Exception>() {
                            @Override
                            public void onResponse(Disclaimers result) {
                                // here result can come as null if Disclaimer not present for given product
                                // but still Product Detail will be success as asset is already fetched
                                product.setDisclaimers(result);
                                productDetail(product, ecsCallback);
                            }

                            @Override
                            public void onFailure(Exception error, ECSError ecsError) {
                                // even if Disclaimer request fails the Product detail call be success as Asset has been already fetched
                                productDetail(product, ecsCallback);
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
        };
        assets.start();
        disclaimer.start();
    }

    //=========================================End of PRX Product List, Product Detail & Product for CTN =======================================


    //============================================== Start of PRX (Summary, Asset & Disclaimer) ================================================

    void getSummaryForCTN(String ctn, Product product, ECSCallback<Product, Exception> eCSCallback) {
        Products products = new Products();
        ArrayList<String> ctns = new ArrayList<>();
        if (null == product) {
            product = new Product();
            product.setCode(ctn);
        }
        List<Product> productList = new ArrayList<Product>();
        products.setProducts(productList);
        products.getProducts().add(product);
        ctns.add(ctn);
        getProductSummary(products, new ECSCallback<Products, Exception>() {
            @Override
            public void onResponse(Products result) {
                eCSCallback.onResponse(result.getProducts().get(0)); // one and only product
            }

            @Override
            public void onFailure(Exception error, ECSError ecsError) {
                eCSCallback.onFailure(error, ecsError);
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
                    public void onFailure(Exception error, ECSError ecsError) {
                        ecsCallback.onFailure(error, ecsError);
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
        new GetProductSummaryListRequest(url, eCSCallback).executeRequest();
    }

    void getProductAsset(String url, ECSCallback<Assets, Exception> eCSCallback) {
        new GetProductAssetRequest(url, eCSCallback).executeRequest();
    }

    void getProductDisclaimer(String url, ECSCallback<Disclaimers, Exception> eCSCallback) {
        new GetProductDisclaimerRequest(url, eCSCallback).executeRequest();
    }


    private ProductSummaryListServiceDiscoveryRequest prepareProductSummaryListRequest(List<String> ctns) {
        return new ProductSummaryListServiceDiscoveryRequest(ctns);

    }

     void getSummary(List<String> ctns, ECSCallback<List<Product>, Exception> ecsCallback) {

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
            public void onFailure(Exception error, ECSError ecsError) {
                ecsCallback.onFailure(error, ecsError);
            }
        });
    }
    //============================================== End of PRX (Summary, Asset & Disclaimer) ================================================


    //=====================================================Start of Shopping Cart ===========================================================

    void getECSShoppingCart(ECSCallback<ECSShoppingCart, Exception> ecsCallback) {
        ECSCallback ecsCallback1 =  new ECSCallback<ECSShoppingCart, Exception>() {
            @Override
            public void onResponse(ECSShoppingCart ecsShoppingCart) {
                if (null == ecsShoppingCart.getEntries()) {
                    // if no product is added to cart
                    ecsCallback.onResponse(ecsShoppingCart);
                } else {
                    //Preparing products to get summary Data
                    List<Product> productList = new ArrayList<>();

                    for (EntriesEntity entriesEntity : ecsShoppingCart.getEntries()) {
                        productList.add(entriesEntity.getProduct());
                    }
                    Products products = new Products();
                    products.setProducts(productList);
                    //get Summary Data Here
                    ECSCallback<Products, Exception> ecsCallbackProduct = new ECSCallback<Products, Exception>() {
                        @Override
                        public void onResponse(Products result) {
                            ecsCallback.onResponse(ecsShoppingCart);
                        }

                        @Override
                        public void onFailure(Exception error, ECSError ecsError) {
                            ecsCallback.onFailure(error, ecsError);
                        }
                    };
                    prepareProductSummaryURL(products, ecsCallbackProduct);
                }
            }

            @Override
            public void onFailure(Exception error, ECSError ecsError) {
                ecsCallback.onFailure(error, ecsError);
            }
        };

        GetECSShoppingCartsRequest  getECSShoppingCartsRequest = getShoppingCartsRequestObject(ecsCallback1);

        getECSShoppingCartsRequest.executeRequest();

    }

      GetECSShoppingCartsRequest getShoppingCartsRequestObject( ECSCallback<ECSShoppingCart, Exception> ecsCallback1 ){
         return  new GetECSShoppingCartsRequest(ecsCallback1);


    }

    void createECSShoppingCart(ECSCallback<ECSShoppingCart, Exception> ecsCallback) {
        ECSCallback<ECSShoppingCart, Exception> ecsCallback1 = new ECSCallback<ECSShoppingCart, Exception>() {
            @Override
            public void onResponse(ECSShoppingCart ecsShoppingCart) {
                ecsCallback.onResponse(ecsShoppingCart);
            }

            @Override
            public void onFailure(Exception error, ECSError ecsError) {
                ecsCallback.onFailure(error, ecsError);
            }
        };
        createECSShoppingCartRequestObject(ecsCallback1).executeRequest();

    }

    CreateECSShoppingCartRequest createECSShoppingCartRequestObject(ECSCallback<ECSShoppingCart, Exception> ecsCallback){
        return new CreateECSShoppingCartRequest(ecsCallback);
    }
    // AddProduct to Cart
     void addProductToShoppingCart(Product product, ECSCallback<ECSShoppingCart, Exception> ecsCallback) {
         ECSCallback<Boolean, Exception> ecsCallback1= new ECSCallback<Boolean, Exception>() {
            @Override
            public void onResponse(Boolean result) {
                getECSShoppingCart(ecsCallback);
            }

            @Override
            public void onFailure(Exception error, ECSError ecsError) {
                // getECSShoppingCart(ecsCallback);
                ecsCallback.onFailure(error, ecsError);
            }
        };

         addProductToECSShoppingCartRequestObject(product.getCode(),ecsCallback1).executeRequest();
    }
    AddProductToECSShoppingCartRequest addProductToECSShoppingCartRequestObject(String code, ECSCallback<Boolean, Exception> ecsCallback ){
        return new AddProductToECSShoppingCartRequest(code, ecsCallback);
    }

     void updateQuantity(int quantity, EntriesEntity entriesEntity, ECSCallback<ECSShoppingCart, Exception> ecsCallback) {
         ECSCallback<Boolean, Exception> ecsCallback1 = new ECSCallback<Boolean, Exception>() {
            @Override
            public void onResponse(Boolean result) {
                getECSShoppingCart(ecsCallback);
            }

            @Override
            public void onFailure(Exception error, ECSError ecsError) {
                ecsCallback.onFailure(error, ecsError);
            }
        };
         updateECSShoppingCartQuantityRequestObject(ecsCallback1,entriesEntity,quantity).executeRequest();
    }

    UpdateECSShoppingCartQuantityRequest updateECSShoppingCartQuantityRequestObject(ECSCallback<Boolean, Exception> ecsCallback,EntriesEntity entriesEntity,int quantity){
        return new UpdateECSShoppingCartQuantityRequest (ecsCallback,entriesEntity,quantity);
    }


     void setVoucher(String voucherCode, ECSCallback<GetAppliedValue, Exception> ecsCallback) {
         ECSCallback<Boolean, Exception> ecsCallback1= new  ECSCallback<Boolean, Exception>() {
            @Override
            public void onResponse(Boolean result) {
                getVoucher(ecsCallback);
            }

            @Override
            public void onFailure(Exception error, ECSError ecsError) {
                ecsCallback.onFailure(error, ecsError);
            }
        };
         new SetVoucherRequest(voucherCode,ecsCallback1).executeRequest();
    }


     void getVoucher(ECSCallback<GetAppliedValue, Exception> ecsCallback) {
         getVouchersRequestObject(ecsCallback).executeRequest();
    }
    GetVouchersRequest getVouchersRequestObject(ECSCallback<GetAppliedValue, Exception> ecsCallback){
        return new GetVouchersRequest(ecsCallback);
    }

     void removeVoucher(String voucherCode, ECSCallback<GetAppliedValue, Exception> ecsCallback) {

        new RemoveVoucherRequest(voucherCode, new ECSCallback<Boolean, Exception>() {
            @Override
            public void onResponse(Boolean result) {
                getVoucher(ecsCallback);
            }

            @Override
            public void onFailure(Exception error, ECSError ecsError) {
                ecsCallback.onFailure(error, ecsError);
            }
        }).executeRequest();
    }
    //===================================================== End of Shopping Cart ======================================================


    //===================================================== start of Delivery Mode ====================================================
     void getDeliveryModes(ECSCallback<GetDeliveryModes, Exception> ecsCallback) {
        new GetDeliveryModesRequest(ecsCallback).executeRequest();
    }

     void setDeliveryMode(String deliveryModeID, ECSCallback<Boolean, Exception> ecsCallback) {
        new SetDeliveryModesRequest(deliveryModeID, ecsCallback).executeRequest();
    }

     void getRegions(ECSCallback<RegionsList, Exception> ecsCallback) {
        new GetRegionsRequest(ecsCallback).executeRequest();
    }
    //===================================================== End of Delivery Mode ====================================================


    //===================================================== Start of Address ====================================================
     void getListSavedAddress(ECSCallback<GetShippingAddressData, Exception> ecsCallback) {
        new GetAddressRequest(ecsCallback).executeRequest();
    }

     void setDeliveryAddress(Addresses address, ECSCallback<Boolean, Exception> ecsCallback) {
        new SetDeliveryAddressRequest(address.getId(), ecsCallback).executeRequest();
    }
    //===================================================== End of Delivery Mode ====================================================

     void createNewAddress(Addresses address, ECSCallback<Addresses, Exception> ecsCallback, boolean singleAddress) {
         ECSCallback<Addresses, Exception> ecsCallback1= new  ECSCallback<Addresses, Exception>() {
            @Override
            public void onResponse(Addresses result) {
                // Address is created and now Address list needs to be called
                ecsCallback.onResponse(result);

            }

            @Override
            public void onFailure(Exception error, ECSError ecsError) {
                ecsCallback.onFailure(error, ecsError);
            }
        };
         createAddressRequestObject(address,ecsCallback1).executeRequest();
    }

    CreateAddressRequest createAddressRequestObject(Addresses address, ECSCallback<Addresses, Exception> ecsCallback){
        return new CreateAddressRequest(address,ecsCallback);
    }

     void createNewAddress(Addresses address, ECSCallback<GetShippingAddressData, Exception> ecsCallback) {
        new CreateAddressRequest(address, new ECSCallback<Addresses, Exception>() {
            @Override
            public void onResponse(Addresses result) {
                // Address is created and now Address list needs to be called
                getListSavedAddress(ecsCallback);
            }

            @Override
            public void onFailure(Exception error, ECSError ecsError) {
                ecsCallback.onFailure(error, ecsError);
            }
        }).executeRequest();
    }

     void updateAddress(Addresses address, ECSCallback<Boolean, Exception> ecsCallback) {
        new UpdateAddressRequest(address, ecsCallback).executeRequest();
    }

     void deleteAddress(Addresses address, ECSCallback<GetShippingAddressData, Exception> ecsCallback) {
        new DeleteAddressRequest(address, new ECSCallback<Boolean, Exception>() {
            @Override
            public void onResponse(Boolean result) {
                getListSavedAddress(ecsCallback);
            }

            @Override
            public void onFailure(Exception error, ECSError ecsError) {
                ecsCallback.onFailure(error, ecsError);
            }
        }).executeRequest();
    }

     void getRetailers(String productID, ECSCallback<WebResults, Exception> ecsCallback) {
        new GetRetailersInfoRequest(ecsCallback, productID).executeRequest();
    }
    //===================================================== End of Address ====================================================


    //===================================================== Start of Payment ====================================================

     void getPayments(ECSCallback<PaymentMethods, Exception> ecsCallback) {
        new GetPaymentsRequest(ecsCallback).executeRequest();
    }

     void setPaymentMethod(String paymentDetailsId, ECSCallback<Boolean, Exception> ecsCallback) {
        new SetPaymentMethodRequest(paymentDetailsId, new ECSCallback<Boolean, Exception>() {
            @Override
            public void onResponse(Boolean result) {
                ecsCallback.onResponse(result);
            }

            @Override
            public void onFailure(Exception error, ECSError ecsError) {
                ecsCallback.onFailure(error, ecsError);
            }
        }).executeRequest();
    }

     void getOrderHistory(int pageNumber, ECSCallback<OrdersData, Exception> ecsCallback) {
        new GetOrderHistoryRequest(pageNumber, ecsCallback).executeRequest();
    }


     void submitOrder(String cvv, ECSCallback<OrderDetail, Exception> ecsCallback) {
        new SubmitOrderRequest(cvv, ecsCallback).executeRequest();
    }


     void makePayment(OrderDetail orderDetail, Addresses billingAddress, ECSCallback<MakePaymentData, Exception> ecsCallback) {
        new MakePaymentRequest(orderDetail, billingAddress, ecsCallback).executeRequest();
    }


     void getOrderDetail(String orderId, ECSCallback<OrderDetail, Exception> ecsCallback) {

        new GetOrderDetailRequest(orderId, new ECSCallback<OrderDetail, Exception>() {
            @Override
            public void onResponse(OrderDetail orderDetail) {
                if (orderDetail == null || orderDetail.getEntries() == null || orderDetail.getEntries().size() == 0) {
                    ecsCallback.onResponse(orderDetail);
                    return;
                }
                // Get PRX Summary Data
                ArrayList<String> ctns = new ArrayList<>();
                ArrayList<Product> productsFromDirectEntry = new ArrayList<>();
                ArrayList<Product> productsFromDeliveryGroupEntry = new ArrayList<>();
                for (Entries entries : orderDetail.getDeliveryOrderGroups().get(0).getEntries()) {
                    productsFromDeliveryGroupEntry.add(entries.getProduct());
                }
                //Products found in direct Entries
                for (Entries entries : orderDetail.getEntries()) {
                    productsFromDirectEntry.add(entries.getProduct());
                    ctns.add(entries.getProduct().getCode());
                }
                ProductSummaryListServiceDiscoveryRequest productSummaryListServiceDiscoveryRequest = prepareProductSummaryListRequest(ctns);

                //get PRX summary URL
                productSummaryListServiceDiscoveryRequest.getRequestUrlFromAppInfra(new ServiceDiscoveryRequest.OnUrlReceived() {
                    @Override
                    public void onSuccess(String url) {

                        getProductSummary(url, new ECSCallback<ECSProductSummary, Exception>() {
                            @Override
                            public void onResponse(ECSProductSummary ecsProductSummary) {
                                setSummaryToProductsFromDirectEntry(ecsProductSummary, productsFromDirectEntry);
                                setSummaryToProductsDeliveryGroupEntry(ecsProductSummary, productsFromDeliveryGroupEntry);
                                ecsCallback.onResponse(orderDetail);
                            }

                            @Override
                            public void onFailure(Exception error, ECSError ecsError) {
                                ecsCallback.onFailure(error, ecsError);
                            }
                        });
                    }

                    @Override
                    public void onError(ERRORVALUES errorvalues, String s) {
                    }
                });
            }

            @Override
            public void onFailure(Exception error, ECSError ecsError) {

            }
        }).executeRequest();
    }

    private void setSummaryToProductsDeliveryGroupEntry(ECSProductSummary ecsProductSummary, ArrayList<Product> productsFromDeliveryGroupEntry) {
        ArrayList<Data> data = ecsProductSummary.getData();
        for (Data d : data) {
            setProductData(d, productsFromDeliveryGroupEntry);
        }
    }

    private void setProductData(Data data, ArrayList<Product> products) {
        for (Product product : products) {
            if (product.getCode().equalsIgnoreCase(data.getCtn())) {
                product.setSummary(data);
                return;
            }
        }
    }

    private void setSummaryToProductsFromDirectEntry(ECSProductSummary ecsProductSummary, ArrayList<Product> productsFromDirectEntry) {
        ArrayList<Data> data = ecsProductSummary.getData();
        for (Data d : data) {
            setProductData(d, productsFromDirectEntry);
        }
    }

     void getUserProfile(ECSCallback<UserProfile, Exception> ecsCallback) {
        new GetUserProfileRequest(ecsCallback).executeRequest();
    }

     void getOrderDetail(Orders orders, ECSCallback<Orders, Exception> ecsCallback) {
        getOrderDetail(orders.getCode(), new ECSCallback<OrderDetail, Exception>() {
            @Override
            public void onResponse(OrderDetail result) {
                orders.orderDetail = result;
                ecsCallback.onResponse(orders);
            }

            @Override
            public void onFailure(Exception error, ECSError ecsError) {
                ecsCallback.onFailure(error, ecsError);
            }
        });
    }

    public void refreshAuth(OAuthInput oAuthInput, ECSCallback<OAuthResponse, Exception> ecsListener) {
        new OAuthRequest(oAuthInput,ecsListener).executeRequest();
    }
}


