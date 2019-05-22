/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
package com.philips.cdp.di.iap.store;

import android.content.Context;

import com.philips.cdp.di.iap.integration.IAPDependencies;

public abstract class AbstractStore implements StoreListener {

    protected boolean mStoreInitialized;
    protected String mCountry;
    protected String mLanguage;

    @Override
    public void createNewUser(final Context context, final IAPDependencies iapDependencies) {
    }

    @Override
    public void setLangAndCountry(final String language, final String countryCode) {
        checkAndUpdateStoreChange(language, countryCode);
        mLanguage = language;
        mCountry = countryCode;
    }

    protected void checkAndUpdateStoreChange(String language, String countryCode) {
        if (language == null || countryCode == null || mLanguage == null || mCountry == null
                || !mCountry.equals(countryCode) || !mLanguage.equals(language)) {
            setStoreInitialized(false);
        }
    }

    @Override
    public String getCountry() {
        return mCountry;
    }

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
    public IAPUser getUser() {
        return null;
    }

    @Override
    public String getCartsUrl() {
        return null;
    }

    @Override
    public String getCurrentCartUrl() {
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
    public String getUpdateProductUrl(final String productID) {
        return null;
    }

    @Override
    public String getPaymentDetailsUrl() {
        return null;
    }

    @Override
    public String getAddressesUrl() {
        return null;
    }

    @Override
    public String getRegionsUrl() {
        return null;
    }

    @Override
    public String getEditAddressUrl(final String addressID) {
        return null;
    }

    @Override
    public String getSetDeliveryModeUrl() {
        return null;
    }

    @Override
    public String getSetDeliveryAddressUrl() {
        return null;
    }

    @Override
    public String getMakePaymentUrl(final String id) {
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
    public void setNewUser(final boolean userLoggedout) {
    }

    @Override
    public boolean isNewUser() {
        return false;
    }

    protected void setStoreInitialized(boolean isStoreAvailable) {
        mStoreInitialized = isStoreAvailable;
    }

    @Override
    public boolean isStoreInitialized() {
        return mStoreInitialized;
    }

    @Override
    public String getJanRainEmail() {
        return null;
    }

    @Override
    public String getDisplayName() {
        return null;
    }

    @Override
    public String getGivenName() {
        return null;
    }

    @Override
    public String getFamilyName() {
        return null;
    }

    @Override
    public String getOrderDetailUrl(String orderID) {
        return null;
    }

    @Override
    public String getSearchProductUrl(String ctnNumber) {
        return null;
    }

    @Override
    public String getOrderHistoryUrl(String pageNumber) {
        return null;
    }

    @Override
    public String getDeliveryModesUrl() {
        return null;
    }

    @Override
    public String getUserUrl() {
        return null;
    }

    @Override
    public String getDeleteCartUrl() {
        return null;
    }

    @Override
    public String getPhoneContactUrl(final String category) {
        return null;
    }

    @Override
    public String getApplyVoucherUrl() {
        return null;
    }

    @Override
    public String getDeleteVoucherUrl(String voucherId) {
        return null;
    }

    @Override
    public String getAppliedVoucherUrl() {
        return null;
    }
}
