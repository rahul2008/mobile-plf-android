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
     * Gets product detail containing assets and disclaimer details
     *
     * @param currentPage the current page
     * @param pageSize    the page size
     * @param eCSCallback the iapsdk callback success block containing AssetModel and DisclaimerModel
     *                    <p>
     *                    Gets product list along with product summary detail.
     * @param ECSCallback the iapsdk callback success block containing Products object (a list of ProductsEntity and other fields)
     */
/**
     * Gets product list along with product summary detail.
     *
     * @param ECSCallback the iapsdk callback success block containing Products object (a list of ProductsEntity and other fields)
     *//*
    public void fetchProducts(ECSCallback ECSCallback);


    */

    void fetchProducts(int currentPage, int pageSize, ECSCallback<ECSProducts,Exception> eCSCallback);

    /**
     * Fetch product.
     *
     * @param ctn         the ctn
     * @param eCSCallback the e cs callback
     */
    void fetchProduct(String ctn, ECSCallback<ECSProduct,Exception> eCSCallback );


    /**
     * Fetch product details.
     *
     * @param product     the product
     * @param ecsCallback the ecs callback
     */
    void fetchProductDetails(ECSProduct product, ECSCallback<ECSProduct,Exception> ecsCallback);

    /**
     * Configure ecs.
     *
     * @param ecsCallback the ecs callback
     */
    void configureECS(ECSCallback<Boolean,Exception> ecsCallback);

    /**
     * Configure ecs to get configuration.
     *
     * @param ecsCallback the ecs callback
     */
    void configureECSToGetConfiguration(ECSCallback<ECSConfig, Exception> ecsCallback);

    /**
     * Fetch product summaries.
     *
     * @param ctns        the ctns
     * @param ecsCallback the ecs callback
     */
    void fetchProductSummaries(List<String> ctns, ECSCallback<List<ECSProduct>,Exception> ecsCallback);

    /**
     * Fetch shopping cart.
     *
     * @param ecsCallback the ecs callback
     */
    void fetchShoppingCart(ECSCallback<ECSShoppingCart,Exception> ecsCallback);

    /**
     * Create shopping cart.
     *
     * @param ecsCallback the ecs callback
     */
    void createShoppingCart(ECSCallback<ECSShoppingCart,Exception> ecsCallback);

    /**
     * Add product to shopping cart.
     *
     * @param product     the product
     * @param ecsCallback the ecs callback
     */
    void addProductToShoppingCart(ECSProduct product, ECSCallback<ECSShoppingCart, Exception> ecsCallback);

    /**
     * Update shopping cart.
     *
     * @param quantity      the quantity
     * @param entriesEntity the entries entity
     * @param ecsCallback   the ecs callback
     */
    void updateShoppingCart(int quantity, ECSEntries entriesEntity, ECSCallback<ECSShoppingCart, Exception> ecsCallback) ;

    /**
     * Apply voucher.
     *
     * @param voucherCode the voucher code
     * @param ecsCallback the ecs callback
     */
//voucher
    void applyVoucher(String voucherCode, ECSCallback<List<ECSVoucher>, Exception> ecsCallback);

    /**
     * Fetch applied vouchers.
     *
     * @param ecsCallback the ecs callback
     */
    void fetchAppliedVouchers(ECSCallback<List<ECSVoucher>, Exception> ecsCallback);

    /**
     * Remove voucher.
     *
     * @param voucherCode the voucher code
     * @param ecsCallback the ecs callback
     */
    void removeVoucher(String voucherCode, ECSCallback<List<ECSVoucher>, Exception> ecsCallback);

    /**
     * Fetch delivery modes.
     *
     * @param ecsCallback the ecs callback
     */
    void fetchDeliveryModes(ECSCallback<List<ECSDeliveryMode>, Exception> ecsCallback);

    /**
     * Sets delivery mode.
     *
     * @param deliveryModes the delivery modes
     * @param ecsCallback   the ecs callback
     */
    void setDeliveryMode(ECSDeliveryMode deliveryModes, ECSCallback<Boolean, Exception> ecsCallback);

    /**
     * Fetch regions.
     *
     * @param ecsCallback the ecs callback
     */
    void fetchRegions(ECSCallback<List<ECSRegion>, Exception> ecsCallback);

    /**
     * Fetch saved addresses.
     *
     * @param ecsCallback the ecs callback
     */
    void fetchSavedAddresses(ECSCallback<List<ECSAddress>, Exception> ecsCallback);

    /**
     * Create and fetch address.
     *
     * @param address     the address
     * @param ecsCallback the ecs callback
     */
    void createAndFetchAddress(ECSAddress address, ECSCallback<List<ECSAddress>, Exception> ecsCallback);

    /**
     * Create address.
     *
     * @param address     the address
     * @param ecsCallback the ecs callback
     */
    void createAddress(ECSAddress address, ECSCallback<ECSAddress, Exception> ecsCallback);


    /**
     * Sets delivery address.
     *
     * @param address     the address
     * @param ecsCallback the ecs callback
     */
    void setDeliveryAddress(ECSAddress address, ECSCallback<Boolean, Exception> ecsCallback);

    /**
     * Sets and fetch delivery address.
     *
     * @param address     the address
     * @param ecsCallback the ecs callback
     */
    void setAndFetchDeliveryAddress(ECSAddress address, ECSCallback<List<ECSAddress>, Exception> ecsCallback);

    /**
     * Update address.
     *
     * @param isDefaultAddress the is default address
     * @param address          the address
     * @param ecsCallback      the ecs callback
     */
    void updateAddress(boolean isDefaultAddress, ECSAddress address, ECSCallback<Boolean, Exception> ecsCallback);

    /**
     * Update and fetch address.
     *
     * @param isDefaultAddress the is default address
     * @param address          the address
     * @param ecsCallback      the ecs callback
     */
    void updateAndFetchAddress(boolean isDefaultAddress, ECSAddress address, ECSCallback<List<ECSAddress>, Exception> ecsCallback);

    /**
     * Delete address.
     *
     * @param address     the address
     * @param ecsCallback the ecs callback
     */
    void deleteAddress(ECSAddress address, ECSCallback<Boolean, Exception> ecsCallback);

    /**
     * Delete and fetch address.
     *
     * @param address     the address
     * @param ecsCallback the ecs callback
     */
    void deleteAndFetchAddress(@NonNull ECSAddress address,@NonNull ECSCallback<List<ECSAddress>, Exception> ecsCallback);

    /**
     * Fetch retailers.
     *
     * @param productID   the product id
     * @param ecsCallback the ecs callback
     */
    void fetchRetailers(String productID, ECSCallback<ECSRetailerList,Exception> ecsCallback);

    /**
     * Fetch retailers.
     *
     * @param product     the product
     * @param ecsCallback the ecs callback
     */
    void fetchRetailers(ECSProduct product, ECSCallback<ECSRetailerList,Exception> ecsCallback);

    /**
     * Fetch payments details.
     *
     * @param ecsCallback the ecs callback
     */
    void fetchPaymentsDetails(ECSCallback<List<ECSPayment>, Exception> ecsCallback);

    /**
     * Sets payment details.
     *
     * @param paymentDetailsId the payment details id
     * @param ecsCallback      the ecs callback
     */
    void setPaymentDetails(String paymentDetailsId, ECSCallback<Boolean, Exception> ecsCallback);

    /**
     * Submit order.
     *
     * @param cvv         the cvv
     * @param ecsCallback the ecs callback
     */
    void submitOrder(String cvv, ECSCallback<ECSOrderDetail, Exception> ecsCallback);

    /**
     * Make payment.
     *
     * @param orderDetail    the order detail
     * @param billingAddress the billing address
     * @param ecsCallback    the ecs callback
     */
    void makePayment(ECSOrderDetail orderDetail, ECSAddress billingAddress, ECSCallback<ECSPaymentProvider, Exception> ecsCallback);

    /**
     * Fetch order history.
     *
     * @param pageNumber  the page number
     * @param pageSize    the page size
     * @param ecsCallback the ecs callback
     */
    void fetchOrderHistory(int pageNumber, int pageSize, ECSCallback<ECSOrderHistory, Exception> ecsCallback);

    /**
     * Fetch order detail.
     *
     * @param orderId     the order id
     * @param ecsCallback the ecs callback
     */
    void fetchOrderDetail(String orderId, ECSCallback<ECSOrderDetail,Exception> ecsCallback);

    /**
     * Fetch order detail.
     *
     * @param orderDetail the order detail
     * @param ecsCallback the ecs callback
     */
    void fetchOrderDetail(ECSOrderDetail orderDetail, ECSCallback<ECSOrderDetail,Exception> ecsCallback);

    /**
     * Fetch order detail.
     *
     * @param orders      the orders
     * @param ecsCallback the ecs callback
     */
    void fetchOrderDetail(ECSOrders orders, ECSCallback<ECSOrders, Exception> ecsCallback);

    /**
     * Fetch user profile.
     *
     * @param ecsCallback the ecs callback
     */
    void fetchUserProfile(ECSCallback<ECSUserProfile,Exception> ecsCallback);

    /**
     * Hybris refresh o auth.
     *
     * @param oAuthInput  the o auth input
     * @param ecsListener the ecs listener
     */
    void hybrisRefreshOAuth(ECSOAuthProvider oAuthInput, ECSCallback<ECSOAuthData, Exception> ecsListener);

    /**
     * Sets proposition id.
     *
     * @param propositionID the proposition id
     */
    void setPropositionID(@NonNull String propositionID);

}
