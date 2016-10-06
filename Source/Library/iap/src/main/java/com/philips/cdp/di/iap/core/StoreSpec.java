/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
package com.philips.cdp.di.iap.core;

import android.content.Context;

import com.philips.cdp.di.iap.session.RequestListener;
import com.philips.cdp.di.iap.store.IAPUser;

/**
 * Contains generic URL required to query from Server.
 * It should be refactored again to minimize the apis, as these apis are server dependant and
 * it's not scalable to add all the apis in single config interface.
 */
public interface StoreSpec {
    void setNewUser(Context context);

    void setLangAndCountry(String language, String countryCode);

    void initStoreConfig(String language, String countryCode, RequestListener listener);//is language and country code is required to be passed?

    String getCountry();

    String getLocale();

    String getJanRainEmail();

    IAPUser getUser();

    void refreshLoginSession();

    void setUserLogout(boolean userLoggedout);

    boolean isUserLoggedOut();

    boolean isStoreInitialized();

    //OAuth
    String getOauthUrl();

    String getOauthRefreshUrl();

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

}
