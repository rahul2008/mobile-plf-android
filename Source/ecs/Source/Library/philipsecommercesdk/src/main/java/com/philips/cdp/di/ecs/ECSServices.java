package com.philips.cdp.di.ecs;


import com.philips.cdp.di.ecs.error.ECSErrorEnum;
import com.philips.cdp.di.ecs.error.ECSErrorWrapper;
import com.philips.cdp.di.ecs.integration.ECSOAuthProvider;
import com.philips.cdp.di.ecs.integration.ECSCallback;
import com.philips.cdp.di.ecs.integration.ECSServiceProvider;
import com.philips.cdp.di.ecs.model.address.ECSAddress;
import com.philips.cdp.di.ecs.model.address.ECSDeliveryMode;
import com.philips.cdp.di.ecs.model.cart.ECSShoppingCart;
import com.philips.cdp.di.ecs.model.cart.ECSEntries;
import com.philips.cdp.di.ecs.model.order.ECSOrders;
import com.philips.cdp.di.ecs.model.order.ECSOrderHistory;
import com.philips.cdp.di.ecs.model.orders.ECSOrderDetail;
import com.philips.cdp.di.ecs.model.payment.ECSPayment;
import com.philips.cdp.di.ecs.model.payment.ECSPaymentProvider;
import com.philips.cdp.di.ecs.model.products.ECSProducts;
import com.philips.cdp.di.ecs.model.products.ECSProduct;
import com.philips.cdp.di.ecs.model.region.ECSRegion;
import com.philips.cdp.di.ecs.model.config.ECSConfig;
import com.philips.cdp.di.ecs.model.oauth.ECSOAuthData;
import com.philips.cdp.di.ecs.model.retailers.ECSRetailerList;
import com.philips.cdp.di.ecs.model.user.ECSUserProfile;
import com.philips.cdp.di.ecs.model.voucher.ECSVoucher;
import com.philips.cdp.di.ecs.util.ECSConfiguration;
import com.philips.platform.appinfra.AppInfra;
import com.philips.platform.appinfra.servicediscovery.ServiceDiscoveryInterface;
import com.philips.platform.appinfra.servicediscovery.model.ServiceDiscoveryService;


import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import io.reactivex.annotations.NonNull;
import io.reactivex.annotations.Nullable;

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
    public void updateShoppingCart(int quantity, @NonNull ECSEntries entriesEntity, @NonNull ECSCallback<ECSShoppingCart, Exception> ecsCallback) {
        ecsCallValidator.updateQuantity(quantity, entriesEntity,ecsCallback);
    }

    @Override
    public void applyVoucher(@NonNull String voucherCode, @NonNull ECSCallback<List<ECSVoucher>, Exception> ecsCallback) {
        ecsCallValidator.setVoucher(voucherCode,ecsCallback);
    }

    @Override
    public void fetchAppliedVouchers(ECSCallback<List<ECSVoucher>, Exception> ecsCallback) {
        ecsCallValidator.getVoucher(ecsCallback);
    }

    @Override
    public void removeVoucher(String voucherCode, ECSCallback<List<ECSVoucher>, Exception> ecsCallback) {
        ecsCallValidator.removeVoucher(voucherCode,ecsCallback);
    }

    @Override
    public void fetchDeliveryModes(ECSCallback<List<ECSDeliveryMode>, Exception> ecsCallback) {
        ecsCallValidator.getDeliveryModes(ecsCallback);
    }

    @Override
    public void setDeliveryMode(@NonNull ECSDeliveryMode deliveryModes, @NonNull ECSCallback<Boolean, Exception> ecsCallback) {
        ecsCallValidator.setDeliveryMode(deliveryModes.getCode(),ecsCallback);
    }

    @Override
    public void fetchRegions(@NonNull ECSCallback<List<ECSRegion>, Exception> ecsCallback) {
        ecsCallValidator.getRegions(ecsCallback);
    }

    // ==== Address starts

    @Override
    public void fetchSavedAddresses(@NonNull ECSCallback<List<ECSAddress>, Exception> ecsCallback) {
        ecsCallValidator.getListSavedAddress(ecsCallback);
    }

    @Override
    public void createAddress(ECSAddress address, ECSCallback<ECSAddress, Exception> ecsCallback){
        ecsCallValidator.createNewAddress(address, ecsCallback,true);
    }

    @Override
    public void createAndFetchAddress(@NonNull ECSAddress ecsAddress, @NonNull ECSCallback<List<ECSAddress>, Exception> ecsCallback) {
        ecsCallValidator.createNewAddress(ecsAddress, ecsCallback);
    }

    @Override
    public void updateAddress(boolean isDefaultAddress, @NonNull ECSAddress address, @NonNull ECSCallback<Boolean, Exception> ecsCallback) {
        address.setDefaultAddress(isDefaultAddress);
        ecsCallValidator.updateAddress(address,ecsCallback);
    }

    @Override
    public void updateAndFetchAddress(boolean isDefaultAddress, @NonNull ECSAddress address, @NonNull ECSCallback<List<ECSAddress>, Exception> ecsCallback) {
        address.setDefaultAddress(isDefaultAddress);
        ecsCallValidator.updateAndFetchAddress(address,ecsCallback);
    }

    @Override
    public void setDeliveryAddress(@NonNull ECSAddress address, @NonNull ECSCallback<Boolean, Exception> ecsCallback) {
        ecsCallValidator.setDeliveryAddress(address,ecsCallback);
    }

    @Override
    public void setAndFetchDeliveryAddress( @NonNull ECSAddress address,@NonNull  ECSCallback<List<ECSAddress>, Exception> ecsCallback) {
        ecsCallValidator.setAndFetchDeliveryAddress(address,ecsCallback);
    }

    @Override
    public void deleteAddress(@NonNull ECSAddress address, @NonNull ECSCallback<Boolean, Exception> ecsCallback) {
        ecsCallValidator.deleteAddress(address,ecsCallback);
    }

    @Override
    public void deleteAndFetchAddress(@NonNull ECSAddress address,@NonNull ECSCallback<List<ECSAddress>, Exception> ecsCallback) {
        ecsCallValidator.deleteAndFetchAddress(address,ecsCallback);
    }

    //Address ends

    @Override
    public void fetchPaymentsDetails(@NonNull ECSCallback<List<ECSPayment>, Exception> ecsCallback) {
        ecsCallValidator.getPayments(ecsCallback);
    }

    @Override
    public void setPaymentDetails(@NonNull String paymentDetailsId, @NonNull ECSCallback<Boolean, Exception> ecsCallback) {
        ecsCallValidator.setPaymentMethod(paymentDetailsId,ecsCallback);
    }

    @Override
    public void makePayment(@NonNull ECSOrderDetail orderDetail, @NonNull ECSAddress billingAddress, ECSCallback<ECSPaymentProvider, Exception> ecsCallback) {
        ecsCallValidator.makePayment(orderDetail,billingAddress,ecsCallback);
    }

    @Override
    public void submitOrder(@Nullable String cvv, ECSCallback<ECSOrderDetail, Exception> ecsCallback) {
        ecsCallValidator.submitOrder(cvv,ecsCallback);
    }

    @Override
    public void fetchRetailers(@NonNull String ctn, @NonNull ECSCallback<ECSRetailerList, Exception> ecsCallback) {
        ecsCallValidator.getRetailers(ctn,ecsCallback);
    }

    @Override
    public void fetchRetailers(@NonNull ECSProduct product, @NonNull ECSCallback<ECSRetailerList, Exception> ecsCallback) {
        ecsCallValidator.getRetailers(product.getCode(),ecsCallback);
    }

    @Override
    public void fetchOrderHistory(int pageNumber, int pageSize, @NonNull ECSCallback<ECSOrderHistory, Exception> ecsCallback) {
        ecsCallValidator.getOrderHistory(pageNumber,ecsCallback);
    }

    @Override
    public void fetchOrderDetail(@NonNull String orderId, @NonNull ECSCallback<ECSOrderDetail, Exception> ecsCallback) {
        ecsCallValidator.getOrderDetail(orderId,ecsCallback);
    }

    @Override
    public void fetchOrderDetail(@NonNull ECSOrderDetail orderDetail, @NonNull ECSCallback<ECSOrderDetail, Exception> ecsCallback) {
        ecsCallValidator.getOrderDetail(orderDetail.getCode(),ecsCallback);
    }

    @Override
    public void fetchOrderDetail(@NonNull ECSOrders orders, @NonNull ECSCallback<ECSOrders, Exception> ecsCallback) {
        ecsCallValidator.getOrderDetail(orders,ecsCallback);
    }

    @Override
    public void fetchUserProfile(@NonNull ECSCallback<ECSUserProfile, Exception> ecsCallback) {
        ecsCallValidator.getUserProfile(ecsCallback);
    }

    @Override
    public void setPropositionID(@NonNull String propositionID) {
        ECSConfiguration.INSTANCE.setPropositionID(propositionID);
    }

}
