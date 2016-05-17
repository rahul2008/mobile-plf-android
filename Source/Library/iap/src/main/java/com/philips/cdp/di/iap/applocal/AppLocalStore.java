/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
package com.philips.cdp.di.iap.applocal;

import android.content.Context;

import com.philips.cdp.di.iap.core.StoreSpec;
import com.philips.cdp.di.iap.session.RequestListener;
import com.philips.cdp.di.iap.store.IAPUser;

/**
 * Handles the scenario where the CTNs are provided from the vertical app.
 * All other urls, we are forced to override though it makes no sense.
 */
public class AppLocalStore implements StoreSpec {
    @Override
    public void setNewUser(final Context context) {

    }

    @Override
    public void setLangAndCountry(final String language, final String countryCode) {

    }

    @Override
    public void initStoreConfig(final String language, final String countryCode, final RequestListener listener) {

    }

    @Override
    public String getCountry() {
        return null;
    }

    @Override
    public String getLocale() {
        return null;
    }

    @Override
    public String getOauthUrl() {
        return null;
    }

    @Override
    public String getOauthRefreshUrl() {
        return null;
    }

    @Override
    public String getJanRainEmail() {
        return null;
    }

    @Override
    public IAPUser getUser() {
        return null;
    }

    @Override
    public String getCurrentCartDetailsUrl() {
        return null;
    }

    @Override
    public String getProductCatalogUrl() {
        return null;
    }

    @Override
    public String getCreateCartUrl() {
        return null;
    }

    @Override
    public String getAddToCartUrl() {
        return null;
    }

    @Override
    public String getModifyProductUrl(final String productID) {
        return null;
    }

    @Override
    public String getPaymentDetailsUrl() {
        return null;
    }

    @Override
    public String getAddressDetailsUrl() {
        return null;
    }

    @Override
    public String getRegionsUrl() {
        return null;
    }

    @Override
    public String getAddressAlterUrl(final String addressID) {
        return null;
    }

    @Override
    public String getRetailersAlterUrl(final String CTN) {
        return null;
    }

    @Override
    public String getUpdateDeliveryModeUrl() {
        return null;
    }

    @Override
    public String getUpdateDeliveryAddressUrl() {
        return null;
    }

    @Override
    public String getSetPaymentUrl(final String id) {
        return null;
    }

    @Override
    public String getPlaceOrderUrl() {
        return null;
    }

    @Override
    public String getSetPaymentDetailsUrl() {
        return null;
    }

    @Override
    public void refreshLoginSession() {

    }

    @Override
    public void setUserLogout(final boolean userLoggedout) {

    }

    @Override
    public boolean isUserLoggedOut() {
        return false;
    }

    @Override
    public boolean isStoreInitialized() {
        return false;
    }
}
