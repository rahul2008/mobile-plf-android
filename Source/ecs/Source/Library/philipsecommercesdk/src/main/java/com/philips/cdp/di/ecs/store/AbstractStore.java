/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
package com.philips.cdp.di.ecs.store;

public abstract class AbstractStore implements StoreListener {


    @Override
    public String getOauthUrl(String janRainID) {
        return null;
    }

    @Override
    public String getOauthRefreshUrl() {
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
