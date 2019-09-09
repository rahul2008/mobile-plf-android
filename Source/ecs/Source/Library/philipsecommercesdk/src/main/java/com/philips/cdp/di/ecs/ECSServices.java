package com.philips.cdp.di.ecs;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.philips.cdp.di.ecs.error.ECSErrorEnum;
import com.philips.cdp.di.ecs.error.ECSErrorWrapper;
import com.philips.cdp.di.ecs.integration.ECSOAuthProvider;
import com.philips.cdp.di.ecs.integration.ECSCallback;
import com.philips.cdp.di.ecs.integration.ECSServiceProvider;
import com.philips.cdp.di.ecs.model.address.Addresses;
import com.philips.cdp.di.ecs.model.address.ECSDeliveryMode;
import com.philips.cdp.di.ecs.model.address.GetShippingAddressData;
import com.philips.cdp.di.ecs.model.cart.ECSShoppingCart;
import com.philips.cdp.di.ecs.model.cart.ECSEntries;
import com.philips.cdp.di.ecs.model.order.Orders;
import com.philips.cdp.di.ecs.model.order.OrdersData;
import com.philips.cdp.di.ecs.model.orders.OrderDetail;
import com.philips.cdp.di.ecs.model.payment.MakePaymentData;
import com.philips.cdp.di.ecs.model.payment.PaymentMethods;
import com.philips.cdp.di.ecs.model.products.ECSProducts;
import com.philips.cdp.di.ecs.model.products.ECSProduct;
import com.philips.cdp.di.ecs.model.region.RegionsList;
import com.philips.cdp.di.ecs.model.config.ECSConfig;
import com.philips.cdp.di.ecs.model.oauth.ECSOAuthData;
import com.philips.cdp.di.ecs.model.retailers.WebResults;
import com.philips.cdp.di.ecs.model.user.UserProfile;
import com.philips.cdp.di.ecs.model.voucher.ECSVoucher;
import com.philips.cdp.di.ecs.util.ECSConfiguration;
import com.philips.platform.appinfra.AppInfra;
import com.philips.platform.appinfra.servicediscovery.ServiceDiscoveryInterface;
import com.philips.platform.appinfra.servicediscovery.model.ServiceDiscoveryService;


import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import static com.philips.cdp.di.ecs.error.ECSNetworkError.getErrorLocalizedErrorMessage;

public class ECSServices implements ECSServiceProvider {

   // private ECSManager mECSManager;

    public static final String SERVICE_ID = "iap.baseurl";
    public static final String ECS_NOTATION = "ecs";

    private ECSCallValidator ecsCallValidator;

    public ECSServices(@Nullable String propositionID, @NonNull AppInfra appInfra) {
        ECSConfiguration.INSTANCE.setAppInfra(appInfra);
        ECSConfiguration.INSTANCE.setPropositionID(propositionID);
        ECSConfiguration.INSTANCE.setEcsLogging(appInfra.getLogging().createInstanceForComponent(ECS_NOTATION, BuildConfig.VERSION_NAME));
        ecsCallValidator = new ECSCallValidator();
    }


    public void configureECS(ECSCallback<Boolean,Exception> ecsCallback){

        ArrayList<String> listOfServiceId = new ArrayList<>();
        listOfServiceId.add(SERVICE_ID);

        ServiceDiscoveryInterface.OnGetServiceUrlMapListener onGetServiceUrlMapListener = new ServiceDiscoveryInterface.OnGetServiceUrlMapListener() {
            @Override
            public void onError(ERRORVALUES errorvalues, String s) {

                ECSErrorWrapper ecsErrorWrapper = getErrorLocalizedErrorMessage(ECSErrorEnum.ECSsomethingWentWrong,null,s+"\n"+errorvalues.name());
                ecsCallback.onFailure(ecsErrorWrapper.getException(), ecsErrorWrapper.getEcsError());
            }

            @Override
            public void onSuccess(Map<String, ServiceDiscoveryService> map) {

                Collection<ServiceDiscoveryService> values = map.values();

                ArrayList<ServiceDiscoveryService> serviceDiscoveryServiceArrayList = new ArrayList<>(values);

                ServiceDiscoveryService serviceDiscoveryService = serviceDiscoveryServiceArrayList.get(0);

                String locale = serviceDiscoveryService.getLocale();
                ECSConfiguration.INSTANCE.setLocale(locale);
                String configUrls = serviceDiscoveryService.getConfigUrls();

                if(configUrls!=null){
                    ECSConfiguration.INSTANCE.setBaseURL(configUrls+"/");
                    ecsCallValidator.getHybrisConfig(ecsCallback);
                }else {
                    ecsCallback.onResponse(false);
                }
            }

    };

        ECSConfiguration.INSTANCE.getAppInfra().getServiceDiscovery().getServicesWithCountryPreference(listOfServiceId, onGetServiceUrlMapListener,null);
    }

    @Override
    public void configureECSToGetConfiguration(@NonNull ECSCallback<ECSConfig, Exception> ecsCallback) {

        ArrayList<String> listOfServiceId = new ArrayList<>();
        listOfServiceId.add(SERVICE_ID);

        ServiceDiscoveryInterface.OnGetServiceUrlMapListener onGetServiceUrlMapListener = new ServiceDiscoveryInterface.OnGetServiceUrlMapListener() {
            @Override
            public void onError(ERRORVALUES errorvalues, String s) {

                ECSErrorWrapper ecsErrorWrapper = getErrorLocalizedErrorMessage(ECSErrorEnum.ECSsomethingWentWrong,null,s+"\n"+errorvalues.name());
                ecsCallback.onFailure(ecsErrorWrapper.getException(), ecsErrorWrapper.getEcsError());
            }

            @Override
            public void onSuccess(Map<String, ServiceDiscoveryService> map) {

                Collection<ServiceDiscoveryService> values = map.values();

                ArrayList<ServiceDiscoveryService> serviceDiscoveryServiceArrayList = new ArrayList<>(values);

                ServiceDiscoveryService serviceDiscoveryService = serviceDiscoveryServiceArrayList.get(0);

                String locale = serviceDiscoveryService.getLocale();
                ECSConfiguration.INSTANCE.setLocale(locale);
                String configUrls = serviceDiscoveryService.getConfigUrls();

                if(configUrls!=null){
                    ECSConfiguration.INSTANCE.setBaseURL(configUrls+"/");
                    ecsCallValidator.getECSConfig(ecsCallback);
                }else {
                    ECSConfig hybrisConfigResponse = new ECSConfig();
                    hybrisConfigResponse.setLocale(locale);
                    ecsCallback.onResponse(hybrisConfigResponse);
                }
            }

        };

        ECSConfiguration.INSTANCE.getAppInfra().getServiceDiscovery().getServicesWithCountryPreference(listOfServiceId, onGetServiceUrlMapListener,null);
    }

    public void hybrisOAthAuthentication(@NonNull ECSOAuthProvider ecsoAuthProvider, @NonNull ECSCallback<ECSOAuthData,Exception> ecsListener){
        ecsCallValidator.getOAuth(ecsoAuthProvider,ecsListener);
    }

    @Override
    public void hybrisRefreshOAuth(@NonNull ECSOAuthProvider ecsoAuthProvider, @NonNull ECSCallback<ECSOAuthData, Exception> ecsListener) {
        ecsCallValidator.refreshAuth(ecsoAuthProvider,ecsListener);
    }

    @Override
    public void fetchProducts(int currentPage, int pageSize, @NonNull ECSCallback<ECSProducts, Exception> eCSCallback) {
        ecsCallValidator.getProductList(currentPage,pageSize,eCSCallback);
    }


    @Override
    public void fetchProductSummaries(@NonNull List<String> ctns, @NonNull ECSCallback<List<ECSProduct>, Exception> ecsCallback) {
        ecsCallValidator.getProductSummary(ctns,ecsCallback);
    }

    @Override
    public void fetchProduct(@NonNull String ctn, @NonNull ECSCallback<ECSProduct, Exception> eCSCallback) {
        ecsCallValidator.getProductFor(ctn,eCSCallback);

    }

    @Override
    public void fetchProductDetails(@NonNull ECSProduct product, @NonNull ECSCallback<ECSProduct, Exception> ecsCallback) {
        ecsCallValidator.getProductDetail(product,ecsCallback);
    }

    @Override
    public void fetchShoppingCart(@NonNull ECSCallback<ECSShoppingCart, Exception> ecsCallback) {
        ecsCallValidator.getECSShoppingCart(ecsCallback);
    }

    @Override
    public void addProductToShoppingCart(@NonNull ECSProduct product, @NonNull ECSCallback<ECSShoppingCart, Exception> ecsCallback) {
        ecsCallValidator.addProductToShoppingCart(product,ecsCallback);
    }


    @Override
    public void createShoppingCart(@NonNull ECSCallback<ECSShoppingCart, Exception> ecsCallback) {
        ecsCallValidator.createECSShoppingCart(ecsCallback);
    }

    @Override
    public void updateQuantity(int quantity, @NonNull ECSEntries entriesEntity, @NonNull ECSCallback<ECSShoppingCart, Exception> ecsCallback) {
        ecsCallValidator.updateQuantity(quantity, entriesEntity,ecsCallback);
    }

    //GetAppliedValue = ECSVouchers
    //applyVoucher
    @Override
    public void applyVoucher(@NonNull String voucherCode, @NonNull ECSCallback<List<ECSVoucher>, Exception> ecsCallback) {
        ecsCallValidator.setVoucher(voucherCode,ecsCallback);
    }

    // fetchAppliedVouchers
    @Override
    public void fetchAppliedVouchers(ECSCallback<List<ECSVoucher>, Exception> ecsCallback) {
        ecsCallValidator.getVoucher(ecsCallback);
    }

    @Override
    public void removeVoucher(String voucherCode, ECSCallback<List<ECSVoucher>, Exception> ecsCallback) {
        ecsCallValidator.removeVoucher(voucherCode,ecsCallback);
    }

    //GetDeliveryModes = ECSDeliveryModes
    // DeliveryModes = List<ECSDeliveryModes>
    // GetDeliveryModes = private

    @Override
    public void fetchDeliveryModes(ECSCallback<List<ECSDeliveryMode>, Exception> ecsCallback) {
        ecsCallValidator.getDeliveryModes(ecsCallback);
    }

    //DeliveryModes = ECSDeliveryMode

    @Override
    public void setDeliveryMode(@NonNull ECSDeliveryMode deliveryModes, @NonNull ECSCallback<Boolean, Exception> ecsCallback) {
        ecsCallValidator.setDeliveryMode(deliveryModes.getCode(),ecsCallback);
    }

    //fetch Regions  List<ECSRegion>
    @Override
    public void getRegions(@NonNull ECSCallback<RegionsList, Exception> ecsCallback) {
        ecsCallValidator.getRegions(ecsCallback);
    }

    //fetchSavedAddresses   returns List<ECSAddress>  GetShippingAddressData :- private
    @Override
    public void getListSavedAddress(@NonNull ECSCallback<GetShippingAddressData, Exception> ecsCallback) {
        ecsCallValidator.getListSavedAddress(ecsCallback);
    }

    //Addresses - List<ECSAddress>  returns List<ECSAddress>
    @Override
    public void createNewAddress(@NonNull Addresses ecsAddress, @NonNull ECSCallback<GetShippingAddressData, Exception> ecsCallback) {
        ecsCallValidator.createNewAddress(ecsAddress, ecsCallback);
    }

    // createAddress createNewAddress , ECSAddress  returns ECSAddress
    @Override
    public void createNewAddress(@NonNull Addresses address,@NonNull ECSCallback<Addresses, Exception> ecsCallback, boolean singleAddress) {
        ecsCallValidator.createNewAddress(address, ecsCallback,true);
    }

    // updateAddress  takes ECSAddress ,returns boolean

    // pass 3 parameter = boolean - is Deafalault Address

    @Override
    public void updateAddress(@NonNull Addresses address,@NonNull ECSCallback<Boolean, Exception> ecsCallback) {
        ecsCallValidator.updateAddress(address,ecsCallback);
    }

    // TODO
   /* @Override
    public void updateAddress(@NonNull Addresses address,@NonNull ECSCallback<Boolean, Exception> ecsCallback) {
        ecsCallValidator.updateAddress(address,ecsCallback);
    }
*/
    @Override
    public void setDeliveryAddress(@NonNull Addresses address, @NonNull ECSCallback<Boolean, Exception> ecsCallback) {
        ecsCallValidator.setDeliveryAddress(address,ecsCallback);
    }

    // TODO
   /* @Override
    public void setDeliveryAddress(@NonNull Addresses address, @NonNull ECSCallback<Boolean, Exception> ecsCallback) {
        ecsCallValidator.setDeliveryAddress(address,ecsCallback);
    }*/

   // Remove below
    @Override
    public void setDefaultAddress(@NonNull Addresses address,@NonNull ECSCallback<Boolean, Exception> ecsCallback) {
        address.setDefaultAddress(true);
        ecsCallValidator.updateAddress(address,ecsCallback);
    }

    @Override
    public void deleteAddress(@NonNull Addresses address,@NonNull ECSCallback<GetShippingAddressData, Exception> ecsCallback) {
        ecsCallValidator.deleteAddress(address,ecsCallback);
    }

    // TODO  fetch address
   /* @Override
    public void deleteAddress(@NonNull Addresses address,@NonNull ECSCallback<GetShippingAddressData, Exception> ecsCallback) {
        ecsCallValidator.deleteAddress(address,ecsCallback);
    }*/


   // return list of PaymentMethod - ECSPayment
    // getPayments - FetchPaymentDetails

    @Override
    public void getPayments(@NonNull ECSCallback<PaymentMethods, Exception> ecsCallback) {
        ecsCallValidator.getPayments(ecsCallback);
    }

    // setPaymentMethod  - setPayment
    // setPaymentDetails

    @Override
    public void setPaymentMethod(@NonNull String paymentDetailsId,@NonNull ECSCallback<Boolean, Exception> ecsCallback) {
        ecsCallValidator.setPaymentMethod(paymentDetailsId,ecsCallback);
    }


    //  OrderDetail - ECSOrderDetail - ECSAddress MakePaymentData - ECSPaymentProvider
    @Override
    public void makePayment(@NonNull OrderDetail orderDetail, @NonNull Addresses billingAddress, ECSCallback<MakePaymentData, Exception> ecsCallback) {
        ecsCallValidator.makePayment(orderDetail,billingAddress,ecsCallback);
    }



    // OrderDetail - ECSOrderDetail

    // look for the implementation where cvv is not there
    @Override
    public void submitOrder(@NonNull String cvv, ECSCallback<OrderDetail, Exception> ecsCallback) {
        ecsCallValidator.submitOrder(cvv,ecsCallback);
    }

    //WebResults -
    // StoresEntity - ECSRetailers
    //StoreEntity - ECSRetailer

    // WebResults - ECSRetailerList
    //TODo - write a utility

    @Override
    public void getRetailers(@NonNull String ctn,@NonNull ECSCallback<WebResults, Exception> ecsCallback) {
        ecsCallValidator.getRetailers(ctn,ecsCallback);
    }

    //Product - ECSProduct
    @Override
    public void getRetailers(@NonNull ECSProduct product, @NonNull ECSCallback<WebResults, Exception> ecsCallback) {
        ecsCallValidator.getRetailers(product.getCode(),ecsCallback);
    }



    //getOrderHistory = fetchOrderHistory
    // currentAPge and pageSize
    // OrdersData - ECSOrderHistory

    @Override
    public void getOrderHistory(int pageNumber, @NonNull ECSCallback<OrdersData, Exception> ecsCallback) {
        ecsCallValidator.getOrderHistory(pageNumber,ecsCallback);
    }

    @Override
    public void getOrderDetail(@NonNull String orderId, @NonNull ECSCallback<OrderDetail, Exception> ecsCallback) {
        ecsCallValidator.getOrderDetail(orderId,ecsCallback);
    }

    // OrderDetail - ECSOrderDetail
    @Override
    public void getOrderDetail(@NonNull OrderDetail orderDetail, @NonNull ECSCallback<OrderDetail, Exception> ecsCallback) {
        ecsCallValidator.getOrderDetail(orderDetail.getCode(),ecsCallback);
    }

    // Orders - ECSOrder
    @Override
    public void getOrderDetail(@NonNull Orders orders, @NonNull ECSCallback<Orders, Exception> ecsCallback) {
        ecsCallValidator.getOrderDetail(orders,ecsCallback);
    }

    // getUserProfile -fetchUserProfile , UserProfile - ECSUserProfile
    @Override
    public void getUserProfile(@NonNull ECSCallback<UserProfile, Exception> ecsCallback) {
        ecsCallValidator.getUserProfile(ecsCallback);
    }

    @Override
    public void setPropositionID(@NonNull String propositionID) {
        ECSConfiguration.INSTANCE.setPropositionID(propositionID);
    }

}
