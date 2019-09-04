package com.philips.cdp.di.ecs;

import android.support.annotation.NonNull;

import com.philips.cdp.di.ecs.integration.OAuthInput;
import com.philips.cdp.di.ecs.integration.ECSCallback;
import com.philips.cdp.di.ecs.integration.ECSServiceProvider;
import com.philips.cdp.di.ecs.model.address.Addresses;
import com.philips.cdp.di.ecs.model.address.GetDeliveryModes;
import com.philips.cdp.di.ecs.model.address.GetShippingAddressData;
import com.philips.cdp.di.ecs.model.cart.ECSShoppingCart;
import com.philips.cdp.di.ecs.model.cart.EntriesEntity;
import com.philips.cdp.di.ecs.model.order.Orders;
import com.philips.cdp.di.ecs.model.order.OrdersData;
import com.philips.cdp.di.ecs.model.orders.OrderDetail;
import com.philips.cdp.di.ecs.model.payment.MakePaymentData;
import com.philips.cdp.di.ecs.model.payment.PaymentMethods;
import com.philips.cdp.di.ecs.model.products.Products;
import com.philips.cdp.di.ecs.model.products.Product;
import com.philips.cdp.di.ecs.model.region.RegionsList;
import com.philips.cdp.di.ecs.model.config.HybrisConfigResponse;
import com.philips.cdp.di.ecs.model.oauth.OAuthResponse;
import com.philips.cdp.di.ecs.model.retailers.WebResults;
import com.philips.cdp.di.ecs.model.user.UserProfile;
import com.philips.cdp.di.ecs.model.voucher.GetAppliedValue;
import com.philips.cdp.di.ecs.util.ECSConfig;
import com.philips.platform.appinfra.AppInfra;
import com.philips.platform.appinfra.servicediscovery.ServiceDiscoveryInterface;
import com.philips.platform.appinfra.servicediscovery.model.ServiceDiscoveryService;


import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public class ECSServices implements ECSServiceProvider {

    private ECSManager mECSManager;

    public static final String SERVICE_ID = "iap.baseurl";

    public ECSServices(String propositionID, @NonNull AppInfra appInfra) {
        ECSConfig.INSTANCE.setAppInfra(appInfra);
        ECSConfig.INSTANCE.setPropositionID(propositionID);
        mECSManager = getECSManager();
    }

    ECSManager getECSManager(){
        return new ECSManager();
    }


    public void configureECS(ECSCallback<Boolean,Exception> ecsCallback){

        ArrayList<String> listOfServiceId = new ArrayList<>();
        listOfServiceId.add(SERVICE_ID);

        ServiceDiscoveryInterface.OnGetServiceUrlMapListener onGetServiceUrlMapListener = new ServiceDiscoveryInterface.OnGetServiceUrlMapListener() {
            @Override
            public void onError(ERRORVALUES errorvalues, String s) {

                ecsCallback.onFailure(new Exception(errorvalues.name()), 9000);
            }

            @Override
            public void onSuccess(Map<String, ServiceDiscoveryService> map) {

                Collection<ServiceDiscoveryService> values = map.values();

                ArrayList<ServiceDiscoveryService> serviceDiscoveryServiceArrayList = new ArrayList<>(values);

                ServiceDiscoveryService serviceDiscoveryService = serviceDiscoveryServiceArrayList.get(0);

                String locale = serviceDiscoveryService.getLocale();
                ECSConfig.INSTANCE.setLocale(locale);
                setLangAndCountry(locale);
                String configUrls = serviceDiscoveryService.getConfigUrls();
                ECSConfig.INSTANCE.setBaseURL(configUrls+"/");

                if(configUrls!=null){
                    mECSManager.getHybrisConfig(ecsCallback);
                }else {
                    ecsCallback.onResponse(true);
                }
            }

    };

        ECSConfig.INSTANCE.getAppInfra().getServiceDiscovery().getServicesWithCountryPreference(listOfServiceId, onGetServiceUrlMapListener,null);
    }

    @Override
    public void getECSConfig(ECSCallback<HybrisConfigResponse, Exception> ecsCallback) {
        mECSManager.getHybrisConfigResponse(ecsCallback);
    }

    @Override
    public void getProductSummary(List<String> ctns, ECSCallback<List<Product>, Exception> ecsCallback) {
        mECSManager.getSummary(ctns,ecsCallback);
    }

    @Override
    public void getShoppingCart(ECSCallback<ECSShoppingCart, Exception> ecsCallback) {
        mECSManager.getECSShoppingCart(ecsCallback);
    }

    @Override
    public void createShoppingCart(ECSCallback<ECSShoppingCart, Exception> ecsCallback) {
        mECSManager.createECSShoppingCart(ecsCallback);
    }

    public void hybrisOathAuthentication(OAuthInput OAuthInput, ECSCallback<OAuthResponse,Exception> ecsListener){
        mECSManager.getOAuth(OAuthInput,ecsListener);
    }

    @Override
    public void getProductList(int currentPage, int pageSize, ECSCallback<Products, Exception> eCSCallback) {
        mECSManager.getProductList(currentPage,pageSize,eCSCallback);
    }

    @Override
    public void getProductFor(String ctn, ECSCallback<Product, Exception> eCSCallback) {
        mECSManager.getProductFor(ctn,eCSCallback);

    }

    @Override
    public void InvalidateECS(ECSCallback<Boolean, Exception> eCSCallback) {

    }


    @Override
    public void getProductDetail(Product product, ECSCallback<Product, Exception> ecsCallback) {
        mECSManager.getProductDetail(product,ecsCallback);
    }

    @Override
    public void addProductToShoppingCart(Product product, ECSCallback<ECSShoppingCart, Exception> ecsCallback) {
        mECSManager.addProductToShoppingCart(product,ecsCallback);
    }

    @Override
    public void updateQuantity(int quantity, EntriesEntity entriesEntity, ECSCallback<ECSShoppingCart, Exception> ecsCallback) {
        mECSManager.updateQuantity(quantity, entriesEntity,ecsCallback);
    }

    @Override
    public void setVoucher(String voucherCode, ECSCallback<GetAppliedValue, Exception> ecsCallback) {
        mECSManager.setVoucher(voucherCode,ecsCallback);
    }

    @Override
    public void getVoucher(ECSCallback<GetAppliedValue, Exception> ecsCallback) {
        mECSManager.getVoucher(ecsCallback);
    }

    @Override
    public void removeVoucher(String voucherCode, ECSCallback<GetAppliedValue, Exception> ecsCallback) {
        mECSManager.removeVoucher(voucherCode,ecsCallback);
    }

    @Override
    public void getDeliveryModes(ECSCallback<GetDeliveryModes, Exception> ecsCallback) {
        mECSManager.getDeliveryModes(ecsCallback);
    }

    @Override
    public void setDeliveryMode(String deliveryModeID, ECSCallback<Boolean, Exception> ecsCallback) {
        mECSManager.setDeliveryMode(deliveryModeID,ecsCallback);
    }

    @Override
    public void getRegions(ECSCallback<RegionsList, Exception> ecsCallback) {
        mECSManager.getRegions(ecsCallback);
    }

    @Override
    public void getListSavedAddress(ECSCallback<GetShippingAddressData, Exception> ecsCallback) {
        mECSManager.getListSavedAddress(ecsCallback);
    }

    @Override
    public void createNewAddress(Addresses ecsAddress, ECSCallback<GetShippingAddressData, Exception> ecsCallback) {
        mECSManager.createNewAddress(ecsAddress, ecsCallback);
    }

    @Override
    public void createNewAddress(Addresses address, ECSCallback<Addresses, Exception> ecsCallback, boolean singleAddress) {
        mECSManager.createNewAddress(address, ecsCallback,true);
    }

    @Override
    public void setDeliveryAddress(Addresses address, ECSCallback<Boolean, Exception> ecsCallback) {
        mECSManager.setDeliveryAddress(address,ecsCallback);
    }

    @Override
    public void updateAddress(Addresses address, ECSCallback<Boolean, Exception> ecsCallback) {
        mECSManager.updateAddress(address,ecsCallback);
    }

    @Override
    public void setDefaultAddress(Addresses address, ECSCallback<Boolean, Exception> ecsCallback) {
        address.setDefaultAddress(true);
        mECSManager.updateAddress(address,ecsCallback);
    }

    @Override
    public void deleteAddress(Addresses address, ECSCallback<GetShippingAddressData, Exception> ecsCallback) {
        mECSManager.deleteAddress(address,ecsCallback);
    }

    @Override
    public void getPayments(ECSCallback<PaymentMethods, Exception> ecsCallback) {
        mECSManager.getPayments(ecsCallback);
    }

    @Override
    public void setPaymentMethod(String paymentDetailsId, ECSCallback<Boolean, Exception> ecsCallback) {
        mECSManager.setPaymentMethod(paymentDetailsId,ecsCallback);
    }

    @Override
    public void getRetailers(String productID, ECSCallback<WebResults, Exception> ecsCallback) {
       mECSManager.getRetailers(productID,ecsCallback);
    }

    @Override
    public void getRetailers(Product product, ECSCallback<WebResults, Exception> ecsCallback) {
        mECSManager.getRetailers(product.getCode(),ecsCallback);
    }

    @Override
    public void submitOrder(String cvv, ECSCallback<OrderDetail, Exception> ecsCallback) {
        mECSManager.submitOrder(cvv,ecsCallback);
    }

    @Override
    public void makePayment(OrderDetail orderDetail, Addresses billingAddress, ECSCallback<MakePaymentData, Exception> ecsCallback) {
        mECSManager.makePayment(orderDetail,billingAddress,ecsCallback);
    }

    @Override
    public void getOrderHistory(int pageNumber, ECSCallback<OrdersData, Exception> ecsCallback) {
        mECSManager.getOrderHistory(pageNumber,ecsCallback);
    }

    @Override
    public void getOrderDetail(String orderId, ECSCallback<OrderDetail, Exception> ecsCallback) {
        mECSManager.getOrderDetail(orderId,ecsCallback);
    }

    @Override
    public void getOrderDetail(OrderDetail orderDetail, ECSCallback<OrderDetail, Exception> ecsCallback) {
        mECSManager.getOrderDetail(orderDetail.getCode(),ecsCallback);
    }

    @Override
    public void getOrderDetail(Orders orders, ECSCallback<Orders, Exception> ecsCallback) {
        mECSManager.getOrderDetail(orders,ecsCallback);
    }

    @Override
    public void getUserProfile(ECSCallback<UserProfile, Exception> ecsCallback) {
        mECSManager.getUserProfile(ecsCallback);
    }

    @Override
    public void refreshAuth(OAuthInput oAuthInput,ECSCallback<OAuthResponse, Exception> ecsListener) {
        mECSManager.refreshAuth(oAuthInput,ecsListener);
    }


    private void setLangAndCountry(String locale) {
        String[] localeArray;
        localeArray = locale.split("_");
        ECSConfig.INSTANCE.setCountry(localeArray[1]);
    }

}
