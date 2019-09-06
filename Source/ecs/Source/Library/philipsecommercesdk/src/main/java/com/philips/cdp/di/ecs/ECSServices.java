package com.philips.cdp.di.ecs;

import android.support.annotation.IntegerRes;
import android.support.annotation.NonNull;

import com.philips.cdp.di.ecs.error.ECSErrorEnum;
import com.philips.cdp.di.ecs.error.ECSErrorWrapper;
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

import static com.philips.cdp.di.ecs.error.ECSNetworkError.getErrorLocalizedErrorMessage;

public class ECSServices implements ECSServiceProvider {

    private ECSManager mECSManager;

    public static final String SERVICE_ID = "iap.baseurl";
    public static final String ECS_NOTATION = "ecs";

    public ECSServices(String propositionID, @NonNull AppInfra appInfra) {
        ECSConfig.INSTANCE.setAppInfra(appInfra);
        ECSConfig.INSTANCE.setPropositionID(propositionID);
        ECSConfig.INSTANCE.setEcsLogging(appInfra.getLogging().createInstanceForComponent(ECS_NOTATION, BuildConfig.VERSION_NAME));
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

                ECSErrorWrapper ecsErrorWrapper = getErrorLocalizedErrorMessage(ECSErrorEnum.ECSsomethingWentWrong,null,s+"\n"+errorvalues.name());
                ecsCallback.onFailure(ecsErrorWrapper.getException(), ecsErrorWrapper.getEcsError());
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

                if(configUrls!=null){
                    ECSConfig.INSTANCE.setBaseURL(configUrls+"/");
                    mECSManager.getHybrisConfig(ecsCallback);
                }else {
                    ecsCallback.onResponse(false);
                }
            }

    };

        ECSConfig.INSTANCE.getAppInfra().getServiceDiscovery().getServicesWithCountryPreference(listOfServiceId, onGetServiceUrlMapListener,null);
    }

    @Override
    public void getECSConfig(@NonNull ECSCallback<HybrisConfigResponse, Exception> ecsCallback) {
        if(new ApiCallValidator().getConfigAPIValidateError()!=null) {
            mECSManager.getHybrisConfigResponse(ecsCallback);
        }else{
           // ecsCallback.onFailure(new ApiCallValidator().getConfigAPIValidateError().getException(),new ApiCallValidator().getConfigAPIValidateError().getErrorcode());
        }
    }

    @Override
    public void getProductSummary(@NonNull List<String> ctns, @NonNull ECSCallback<List<Product>, Exception> ecsCallback) {
        if(new ApiCallValidator().getProductSummaryAPIValidateError(ctns)!=null) {
            mECSManager.getSummary(ctns, ecsCallback);
        }
    }

    @Override
    public void getShoppingCart(@NonNull ECSCallback<ECSShoppingCart, Exception> ecsCallback) {
        new ApiCallValidator().getECSShoppingCartAPIValidateError();
        mECSManager.getECSShoppingCart(ecsCallback);
    }

    @Override
    public void createShoppingCart(@NonNull ECSCallback<ECSShoppingCart, Exception> ecsCallback) {
        new ApiCallValidator().getCreateShoppingCartAPIValidateError();
        mECSManager.createECSShoppingCart(ecsCallback);
    }

    public void hybrisOathAuthentication( @NonNull OAuthInput oAuthInput, @NonNull ECSCallback<OAuthResponse,Exception> ecsListener){
        new ApiCallValidator().getHybrisOathAuthenticationAPIValidateError(oAuthInput);
        mECSManager.getOAuth(oAuthInput,ecsListener);
    }

    @Override
    public void getProductList(int currentPage, int pageSize, @NonNull ECSCallback<Products, Exception> eCSCallback) {
        new ApiCallValidator().getProductListAPIValidateError(pageSize);
        mECSManager.getProductList(currentPage,pageSize,eCSCallback);
    }

    @Override
    public void getProductFor(String ctn, @NonNull ECSCallback<Product, Exception> eCSCallback) {
        new ApiCallValidator().getProductForAPIValidateError(ctn);
        mECSManager.getProductFor(ctn,eCSCallback);

    }

    @Override
    public void InvalidateECS(ECSCallback<Boolean, Exception> eCSCallback) {

    }


    @Override
    public void getProductDetail(@NonNull Product product,@NonNull ECSCallback<Product, Exception> ecsCallback) {
        new ApiCallValidator().getProductDetailAPIValidateError(product);
        mECSManager.getProductDetail(product,ecsCallback);
    }

    @Override
    public void addProductToShoppingCart(@NonNull Product product,@NonNull ECSCallback<ECSShoppingCart, Exception> ecsCallback) {
        new ApiCallValidator().getAddProductToShoppingCartError(product);
        mECSManager.addProductToShoppingCart(product,ecsCallback);
    }

    @Override
    public void updateQuantity(int quantity,@NonNull EntriesEntity entriesEntity,@NonNull ECSCallback<ECSShoppingCart, Exception> ecsCallback) {
        new ApiCallValidator().getUpdateQuantityError(quantity);
        mECSManager.updateQuantity(quantity, entriesEntity,ecsCallback);
    }

    @Override
    public void setVoucher(@NonNull String voucherCode,@NonNull ECSCallback<GetAppliedValue, Exception> ecsCallback) {
        new ApiCallValidator().getSetVoucherError(voucherCode);
        mECSManager.setVoucher(voucherCode,ecsCallback);
    }

    @Override
    public void getVoucher(ECSCallback<GetAppliedValue, Exception> ecsCallback) {
        new ApiCallValidator().getVoucherError();
        mECSManager.getVoucher(ecsCallback);
    }

    @Override
    public void removeVoucher(String voucherCode, ECSCallback<GetAppliedValue, Exception> ecsCallback) {
        new ApiCallValidator().getRemoveVoucherError(voucherCode);
        mECSManager.removeVoucher(voucherCode,ecsCallback);
    }

    @Override
    public void getDeliveryModes(ECSCallback<GetDeliveryModes, Exception> ecsCallback) {
        new ApiCallValidator().getDeliveryModesError();
        mECSManager.getDeliveryModes(ecsCallback);
    }

    @Override
    public void setDeliveryMode(@NonNull String deliveryModeID, @NonNull ECSCallback<Boolean, Exception> ecsCallback) {
        new ApiCallValidator().getSetDeliveryModeError(deliveryModeID);
        mECSManager.setDeliveryMode(deliveryModeID,ecsCallback);
    }

    @Override
    public void getRegions(@NonNull ECSCallback<RegionsList, Exception> ecsCallback) {
        new ApiCallValidator().getRegionsError();
        mECSManager.getRegions(ecsCallback);
    }

    @Override
    public void getListSavedAddress(@NonNull ECSCallback<GetShippingAddressData, Exception> ecsCallback) {
        new ApiCallValidator().getListSavedAddressError();
        mECSManager.getListSavedAddress(ecsCallback);
    }

    @Override
    public void createNewAddress(@NonNull Addresses ecsAddress, @NonNull ECSCallback<GetShippingAddressData, Exception> ecsCallback) {
        new ApiCallValidator().getCreateNewAddressError();
        mECSManager.createNewAddress(ecsAddress, ecsCallback);
    }

    @Override
    public void createNewAddress(@NonNull Addresses address,@NonNull ECSCallback<Addresses, Exception> ecsCallback, boolean singleAddress) {
        new ApiCallValidator().getCreateNewAddressError();
        mECSManager.createNewAddress(address, ecsCallback,true);
    }

    @Override
    public void setDeliveryAddress(@NonNull Addresses address, @NonNull ECSCallback<Boolean, Exception> ecsCallback) {
        new ApiCallValidator().getSetDeliveryAddressError(address);
        mECSManager.setDeliveryAddress(address,ecsCallback);
    }

    @Override
    public void updateAddress(@NonNull Addresses address,@NonNull ECSCallback<Boolean, Exception> ecsCallback) {
        new ApiCallValidator().getUpdateAddressError();
        mECSManager.updateAddress(address,ecsCallback);
    }

    @Override
    public void setDefaultAddress(@NonNull Addresses address,@NonNull ECSCallback<Boolean, Exception> ecsCallback) {
        new ApiCallValidator().getSetDefaultAddressError(address);
        address.setDefaultAddress(true);
        mECSManager.updateAddress(address,ecsCallback);
    }

    @Override
    public void deleteAddress(@NonNull Addresses address,@NonNull ECSCallback<GetShippingAddressData, Exception> ecsCallback) {
        new ApiCallValidator().getDeleteAddressError(address);
        mECSManager.deleteAddress(address,ecsCallback);
    }

    @Override
    public void getPayments(@NonNull ECSCallback<PaymentMethods, Exception> ecsCallback) {
        new ApiCallValidator().getPaymentsError();
        mECSManager.getPayments(ecsCallback);
    }

    @Override
    public void setPaymentMethod(@NonNull String paymentDetailsId,@NonNull ECSCallback<Boolean, Exception> ecsCallback) {
        new ApiCallValidator().getSetPaymentMethodError(paymentDetailsId);
        mECSManager.setPaymentMethod(paymentDetailsId,ecsCallback);
    }

    @Override
    public void getRetailers(@NonNull String productID,@NonNull ECSCallback<WebResults, Exception> ecsCallback) {
        new ApiCallValidator().getRetailersError(productID);
       mECSManager.getRetailers(productID,ecsCallback);
    }

    @Override
    public void getRetailers(@NonNull Product product,@NonNull ECSCallback<WebResults, Exception> ecsCallback) {
        new ApiCallValidator().getRetailersError(product.getCode());
        mECSManager.getRetailers(product.getCode(),ecsCallback);
    }

    @Override
    public void submitOrder(@NonNull String cvv, ECSCallback<OrderDetail, Exception> ecsCallback) {
        new ApiCallValidator().getSubmitOrderError(cvv);
        mECSManager.submitOrder(cvv,ecsCallback);
    }

    @Override
    public void makePayment(@NonNull OrderDetail orderDetail, @NonNull Addresses billingAddress, ECSCallback<MakePaymentData, Exception> ecsCallback) {
        new ApiCallValidator().getMakePaymentError();
        mECSManager.makePayment(orderDetail,billingAddress,ecsCallback);
    }

    @Override
    public void getOrderHistory(int pageNumber, @NonNull ECSCallback<OrdersData, Exception> ecsCallback) {
        new ApiCallValidator().getOrderHistoryError(pageNumber);
        mECSManager.getOrderHistory(pageNumber,ecsCallback);
    }

    @Override
    public void getOrderDetail(@NonNull String orderId, @NonNull ECSCallback<OrderDetail, Exception> ecsCallback) {
        new ApiCallValidator().getOrderDetailError(orderId);
        mECSManager.getOrderDetail(orderId,ecsCallback);
    }

    @Override
    public void getOrderDetail(@NonNull OrderDetail orderDetail, @NonNull ECSCallback<OrderDetail, Exception> ecsCallback) {
        new ApiCallValidator().getOrderDetailError(orderDetail.getCode());
        mECSManager.getOrderDetail(orderDetail.getCode(),ecsCallback);
    }

    @Override
    public void getOrderDetail(@NonNull Orders orders, @NonNull ECSCallback<Orders, Exception> ecsCallback) {
        new ApiCallValidator().getOrderDetailError(orders.getCode());
        mECSManager.getOrderDetail(orders,ecsCallback);
    }

    @Override
    public void getUserProfile(@NonNull ECSCallback<UserProfile, Exception> ecsCallback) {
        new ApiCallValidator().getUserProfileError();
        mECSManager.getUserProfile(ecsCallback);
    }

    @Override
    public void refreshAuth(@NonNull OAuthInput oAuthInput,@NonNull ECSCallback<OAuthResponse, Exception> ecsListener) {
        new ApiCallValidator().getHybrisOathAuthenticationAPIValidateError(oAuthInput);
        mECSManager.refreshAuth(oAuthInput,ecsListener);
    }


    private void setLangAndCountry(String locale) {
        String[] localeArray;
        localeArray = locale.split("_");
        ECSConfig.INSTANCE.setCountry(localeArray[1]);
    }

}
