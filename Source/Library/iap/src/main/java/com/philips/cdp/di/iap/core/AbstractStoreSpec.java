/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
package com.philips.cdp.di.iap.core;

import android.content.Context;

import com.philips.cdp.di.iap.session.RequestListener;
import com.philips.cdp.di.iap.store.IAPUser;

public abstract class AbstractStoreSpec implements StoreSpec {

    protected String mCountry;
    protected boolean mStoreInitialized;
    protected String mLanguage;

    @Override
    public void setNewUser(final Context context) {

    }

    @Override
    public void setLangAndCountry(final String language, final String countryCode) {
        checkAndUpdateStoreChange(language, countryCode);
        mLanguage = language;
        mCountry = countryCode;
    }

    @Override
    public void initStoreConfig(final String language, final String countryCode, final RequestListener listener) {

    }

    protected void checkAndUpdateStoreChange(String language, String countryCode) {
        if (language == null || countryCode == null || mLanguage == null || mCountry == null
                || !mCountry.equals(countryCode) || !mLanguage.equals(language)) {
            setStoreInitialized(false);
        }
    }

    protected void setStoreInitialized(boolean changed) {
        mStoreInitialized = changed;
    }

    @Override
    public String getCountry() {
        return null;
    }

    /**
     * Return default language combining input of lang and country.
     * But depending on requirements, it can be changed or different as per locale matcher result.
     * @return
     */
    @Override
    public String getLocale() {
        if (mCountry != null && mLanguage != null) {
            return mLanguage + "_" + mCountry;
        }
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
    public String getProductCatalogUrl(int currentPage, int pageSize) {
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
        return mStoreInitialized;
    }

    @Override
    public String getOrderDetailUrl(String orderID) {
        return null;
    }

    @Override
    public String getSearchProductUrl(String ctnNumber) { return null; }

    @Override
    public String getOrderHistoryUrl(String pageNumber) { return null; }
}
