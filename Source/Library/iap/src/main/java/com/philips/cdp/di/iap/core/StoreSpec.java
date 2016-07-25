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

    void initStoreConfig(String language, String countryCode, RequestListener listener);

    String getCountry();

    String getLocale();

    String getOauthUrl();

    String getOauthRefreshUrl();

    String getJanRainEmail();

    IAPUser getUser();

    //Request Urls
    String getCurrentCartDetailsUrl();

    String getProductCatalogUrl(int currentPage, int pageSize);

    String getCreateCartUrl();

    String getAddToCartUrl();

    String getModifyProductUrl(String productID);

    String getPaymentDetailsUrl();

    String getAddressDetailsUrl();

    String getRegionsUrl();

    String getAddressAlterUrl(String addressID);

    String getRetailersAlterUrl(String CTN);

    String getUpdateDeliveryModeUrl();

    String getUpdateDeliveryAddressUrl();

    String getSetPaymentUrl(String id);

    String getPlaceOrderUrl();

    String getSetPaymentDetailsUrl();

    void refreshLoginSession();

    void setUserLogout(boolean userLoggedout);

    boolean isUserLoggedOut();

    boolean isStoreInitialized();

    String getOrderDetailUrl(String orderID);

    String getSearchProductUrl(String ctnNumber);

    String getOrderHistoryUrl(String pageNumber);

    String getDeliveryModesUrl();

    String getUserUrl();
}
