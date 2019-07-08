/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
package com.ecs.demouapp.ui.store;

import android.content.Context;

import com.ecs.demouapp.ui.integration.IAPDependencies;
import com.ecs.demouapp.ui.session.RequestListener;


/**
 * Contains generic URL required to query from Server.
 * It should be refactored again to minimize the apis, as these apis are server dependant and
 * it's not scalable to add all the apis in single config interface.
 */
public interface StoreListener {
    IAPUser getUser();

    void createNewUser(Context context, IAPDependencies iapDependencies);

    void setNewUser(boolean isNewUser);

    boolean isNewUser();

    void setLangAndCountry(String language, String countryCode);

    String getCountry();

    String getLocale();

    void initStoreConfig(RequestListener listener);//is language and country code is required to be passed?

    boolean isStoreInitialized();

    String getJanRainEmail();

    String getDisplayName() ;

    String getGivenName() ;

    String getFamilyName();

    //OAuth
    String getOauthUrl();

    String getOauthRefreshUrl();

    void refreshLoginSession();

    //Product
    String getProductCatalogUrl(int currentPage, int pageSize);

    String getSearchProductUrl(String ctnNumber);

    String getUpdateProductUrl(String productID);

    //Carts
    String getCartsUrl();

    String getCurrentCartUrl();

    String getCreateCartUrl();

    String getDeleteCartUrl();

    String getAddToCartUrl();

    //Address
    String getRegionsUrl();

    String getUserUrl();

    String getAddressesUrl();

    String getEditAddressUrl(String addressID);

    String getSetDeliveryAddressUrl();

    //Delivery mode
    String getDeliveryModesUrl();

    String getSetDeliveryModeUrl();

    //Payment
    String getPaymentDetailsUrl();

    String getSetPaymentDetailsUrl();

    String getMakePaymentUrl(String id);

    String getPlaceOrderUrl();

    //Orders
    String getOrderHistoryUrl(String pageNumber);

    String getOrderDetailUrl(String orderID);

    String getPhoneContactUrl(String category);


    //Vouchers
    String getApplyVoucherUrl();

    String getDeleteVoucherUrl(String voucherId);

    String getAppliedVoucherUrl();

}
