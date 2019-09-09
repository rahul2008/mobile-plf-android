package com.philips.cdp.di.ecs.integration;


import android.support.annotation.NonNull;

import com.philips.cdp.di.ecs.model.address.Addresses;
import com.philips.cdp.di.ecs.model.address.DeliveryModes;
import com.philips.cdp.di.ecs.model.address.GetDeliveryModes;
import com.philips.cdp.di.ecs.model.address.GetShippingAddressData;
import com.philips.cdp.di.ecs.model.cart.ECSShoppingCart;
import com.philips.cdp.di.ecs.model.cart.EntriesEntity;
import com.philips.cdp.di.ecs.model.oauth.ECSOAuthData;
import com.philips.cdp.di.ecs.model.order.Orders;
import com.philips.cdp.di.ecs.model.order.OrdersData;
import com.philips.cdp.di.ecs.model.orders.OrderDetail;
import com.philips.cdp.di.ecs.model.payment.MakePaymentData;
import com.philips.cdp.di.ecs.model.payment.PaymentMethods;
import com.philips.cdp.di.ecs.model.products.ECSProducts;
import com.philips.cdp.di.ecs.model.products.ECSProduct;
import com.philips.cdp.di.ecs.model.region.RegionsList;
import com.philips.cdp.di.ecs.model.config.ECSConfig;
import com.philips.cdp.di.ecs.model.retailers.WebResults;
import com.philips.cdp.di.ecs.model.user.UserProfile;
import com.philips.cdp.di.ecs.model.voucher.GetAppliedValue;

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

    void updateQuantity(int quantity, EntriesEntity entriesEntity, ECSCallback<ECSShoppingCart, Exception> ecsCallback) ;

    //voucher
    void setVoucher(String voucherCode, ECSCallback<GetAppliedValue,Exception> ecsCallback);

    void getVoucher(ECSCallback<GetAppliedValue,Exception> ecsCallback);

    void removeVoucher(String voucherCode, ECSCallback<GetAppliedValue,Exception> ecsCallback);

    void getDeliveryModes(ECSCallback<GetDeliveryModes,Exception> ecsCallback);

    void setDeliveryMode(DeliveryModes deliveryModes, ECSCallback<Boolean, Exception> ecsCallback);

    void getRegions(ECSCallback<RegionsList, Exception> ecsCallback);

    void getListSavedAddress(ECSCallback<GetShippingAddressData, Exception> ecsCallback);

    void createNewAddress(Addresses address, ECSCallback<GetShippingAddressData, Exception> ecsCallback);

    void createNewAddress(Addresses address, ECSCallback<Addresses, Exception> ecsCallback,boolean singleAddress);

    void setDeliveryAddress(Addresses address,ECSCallback<Boolean, Exception> ecsCallback);

    void updateAddress(Addresses address,ECSCallback<Boolean, Exception> ecsCallback);

    void setDefaultAddress(Addresses address,ECSCallback<Boolean, Exception> ecsCallback);

    void deleteAddress(Addresses address,ECSCallback<GetShippingAddressData, Exception> ecsCallback);

    void getRetailers(String productID, ECSCallback<WebResults,Exception> ecsCallback);

    void getRetailers(ECSProduct product, ECSCallback<WebResults,Exception> ecsCallback);

    void getPayments(ECSCallback<PaymentMethods,Exception> ecsCallback);

    void setPaymentMethod(String paymentDetailsId, ECSCallback<Boolean, Exception> ecsCallback);

    void submitOrder(String cvv, ECSCallback<OrderDetail, Exception> ecsCallback);

    void makePayment(OrderDetail orderDetail, Addresses billingAddress, ECSCallback<MakePaymentData, Exception> ecsCallback);

    void getOrderHistory(int pageNumber, ECSCallback<OrdersData,Exception> ecsCallback);

    void getOrderDetail(String orderId, ECSCallback<OrderDetail,Exception> ecsCallback);

    void getOrderDetail(OrderDetail orderDetail, ECSCallback<OrderDetail,Exception> ecsCallback);

    void getOrderDetail(Orders orders, ECSCallback<Orders, Exception> ecsCallback);

    void getUserProfile(ECSCallback<UserProfile,Exception> ecsCallback);

    void refreshAuth(ECSOAuthProvider oAuthInput, ECSCallback<ECSOAuthData, Exception> ecsListener);

    void setPropositionID(@NonNull String propositionID);

}
