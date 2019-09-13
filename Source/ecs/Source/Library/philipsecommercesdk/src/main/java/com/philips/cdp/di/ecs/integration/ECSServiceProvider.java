package com.philips.cdp.di.ecs.integration;


import android.support.annotation.NonNull;

import com.philips.cdp.di.ecs.model.address.ECSAddress;
import com.philips.cdp.di.ecs.model.address.ECSDeliveryMode;
import com.philips.cdp.di.ecs.model.cart.ECSShoppingCart;
import com.philips.cdp.di.ecs.model.cart.ECSEntries;
import com.philips.cdp.di.ecs.model.oauth.ECSOAuthData;
import com.philips.cdp.di.ecs.model.order.ECSOrders;
import com.philips.cdp.di.ecs.model.order.ECSOrderHistory;
import com.philips.cdp.di.ecs.model.orders.ECSOrderDetail;
import com.philips.cdp.di.ecs.model.payment.ECSPayment;
import com.philips.cdp.di.ecs.model.payment.ECSPaymentProvider;
import com.philips.cdp.di.ecs.model.products.ECSProducts;
import com.philips.cdp.di.ecs.model.products.ECSProduct;
import com.philips.cdp.di.ecs.model.region.ECSRegion;
import com.philips.cdp.di.ecs.model.config.ECSConfig;
import com.philips.cdp.di.ecs.model.retailers.ECSRetailerList;
import com.philips.cdp.di.ecs.model.user.ECSUserProfile;
import com.philips.cdp.di.ecs.model.voucher.ECSVoucher;

import java.util.List;

/**
 * The interface Iap services.
 */
public interface ECSServiceProvider {


   /* *//**
     * Hybris oath authentication, Janrain basic token is used to obtain Hybris oath token and save it within IAPSDKService and return true if success.
     *
     * @param oauthData      the oauth data (Janrain token details)
     * @param ECSCallback the iapsdk callback success block containing boolean
     *//*
    public void  hybrisOAthAuthentication(Map<String, String> oauthData, ECSCallback ECSCallback);
*/

    /**
     * Gets iap config data including catalogId, rootCategory, net , siteId, faqUrl, helpDeskEmail, helpDeskPhone and helpUrl
     *
     * @param eCSCallback the iapsdk callback success block containing IAPConfiguration object
     */
     //public void configureECSToGetConfiguration(ECSCallback<HybrisConfigResponse, Exception> eCSCallback);


    /**
     * Gets product list along with product summary detail.
     *
     * @param ECSCallback the iapsdk callback success block containing Products object (a list of ProductsEntity and other fields)
     *//*
    public void fetchProducts(ECSCallback ECSCallback);


    *//**
     * Gets product detail containing assets and disclaimer details
     *
     * @param eCSCallback the iapsdk callback success block containing AssetModel and DisclaimerModel
     */
    void fetchProducts(int currentPage, int pageSize, ECSCallback<ECSProducts,Exception> eCSCallback);

    void fetchProduct(String ctn, ECSCallback<ECSProduct,Exception> eCSCallback );


    void fetchProductDetails(ECSProduct product, ECSCallback<ECSProduct,Exception> ecsCallback);

    void configureECS(ECSCallback<Boolean,Exception> ecsCallback);

    void configureECSToGetConfiguration(ECSCallback<ECSConfig, Exception> ecsCallback);

    void fetchProductSummaries(List<String> ctns, ECSCallback<List<ECSProduct>,Exception> ecsCallback);

    void fetchShoppingCart(ECSCallback<ECSShoppingCart,Exception> ecsCallback);

    void createShoppingCart(ECSCallback<ECSShoppingCart,Exception> ecsCallback);

    void addProductToShoppingCart(ECSProduct product, ECSCallback<ECSShoppingCart, Exception> ecsCallback);

    void updateShoppingCart(int quantity, ECSEntries entriesEntity, ECSCallback<ECSShoppingCart, Exception> ecsCallback) ;

    //voucher
    void applyVoucher(String voucherCode, ECSCallback<List<ECSVoucher>, Exception> ecsCallback);

    void fetchAppliedVouchers(ECSCallback<List<ECSVoucher>, Exception> ecsCallback);

    void removeVoucher(String voucherCode, ECSCallback<List<ECSVoucher>, Exception> ecsCallback);

    void fetchDeliveryModes(ECSCallback<List<ECSDeliveryMode>, Exception> ecsCallback);

    void setDeliveryMode(ECSDeliveryMode deliveryModes, ECSCallback<Boolean, Exception> ecsCallback);

    void fetchRegions(ECSCallback<List<ECSRegion>, Exception> ecsCallback);

    void fetchSavedAddresses(ECSCallback<List<ECSAddress>, Exception> ecsCallback);

    void createAndFetchAddress(ECSAddress address, ECSCallback<List<ECSAddress>, Exception> ecsCallback);

    void createAddress(ECSAddress address, ECSCallback<ECSAddress, Exception> ecsCallback);


    void setDeliveryAddress(ECSAddress address, ECSCallback<Boolean, Exception> ecsCallback);

    void setAndFetchDeliveryAddress(ECSAddress address, ECSCallback<List<ECSAddress>, Exception> ecsCallback);

    void updateAddress(boolean isDefaultAddress, ECSAddress address, ECSCallback<Boolean, Exception> ecsCallback);

    void updateAndFetchAddress(boolean isDefaultAddress, ECSAddress address, ECSCallback<List<ECSAddress>, Exception> ecsCallback);

    void deleteAddress(ECSAddress address, ECSCallback<Boolean, Exception> ecsCallback);

    void deleteAndFetchAddress(@NonNull ECSAddress address,@NonNull ECSCallback<List<ECSAddress>, Exception> ecsCallback);

    void fetchRetailers(String productID, ECSCallback<ECSRetailerList,Exception> ecsCallback);

    void fetchRetailers(ECSProduct product, ECSCallback<ECSRetailerList,Exception> ecsCallback);

    void fetchPaymentsDetails(ECSCallback<List<ECSPayment>, Exception> ecsCallback);

    void setPaymentDetails(String paymentDetailsId, ECSCallback<Boolean, Exception> ecsCallback);

    void submitOrder(String cvv, ECSCallback<ECSOrderDetail, Exception> ecsCallback);

    void makePayment(ECSOrderDetail orderDetail, ECSAddress billingAddress, ECSCallback<ECSPaymentProvider, Exception> ecsCallback);

    void fetchOrderHistory(int pageNumber, int pageSize, ECSCallback<ECSOrderHistory, Exception> ecsCallback);

    void fetchOrderDetail(String orderId, ECSCallback<ECSOrderDetail,Exception> ecsCallback);

    void fetchOrderDetail(ECSOrderDetail orderDetail, ECSCallback<ECSOrderDetail,Exception> ecsCallback);

    void fetchOrderDetail(ECSOrders orders, ECSCallback<ECSOrders, Exception> ecsCallback);

    void fetchUserProfile(ECSCallback<ECSUserProfile,Exception> ecsCallback);

    void hybrisRefreshOAuth(ECSOAuthProvider oAuthInput, ECSCallback<ECSOAuthData, Exception> ecsListener);

    void setPropositionID(@NonNull String propositionID);

}
