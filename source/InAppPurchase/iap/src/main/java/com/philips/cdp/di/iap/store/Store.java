/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */

package com.philips.cdp.di.iap.store;

import android.content.Context;

public class Store {

    private static final String HTTPS = "https://";
    private static final String WEB_ROOT = "pilcommercewebservices";
    private static final String V2 = "v2";
    public static final String USER = "users";
    private static final String SEPERATOR = "/";

    //Oauth
    private static final String SUFFIX_OAUTH =
            "oauth/token?janrain=%s&grant_type=janrain&client_id=mobile_android&client_secret=secret";
    //Requests
    private static final String SUFFIX_CURRENT_CART = "/carts/current";
    private static final String SUFFIX_GET_CART = "/carts?fields=FULL";
    private static final String SUFFIX_CART_CREATE = "/carts";
    private static final String SUFFIX_CART_ENTRIES = "/entries";
    private static final String SUFFIX_PRODUCT_MODIFY = "/entries/%s";

    private static final String SUFFIX_PAYMENT_DETAILS = "/paymentdetails";
    private static final String SUFFIX_SET_PAYMENT_DETAILS = "/paymentdetails";
    private static final String SUFFIX_SET_PAYMENT_URL = "/orders/%s/pay";

    private static final String SUFFIX_ADDRESSES_FULL = "/addresses?fields=FULL";
    private static final String SUFFIX_ADDRESSES_ALTER = "/addresses/%s";

    private static final String SUFFIX_DELIVERY_MODE = "/deliverymode";
    private static final String SUFFIX_DELIVERY_ADDRESS = "/addresses/delivery";

    private static final String SUFFIX_PLACE_ORDER = "/orders";

    private StoreConfiguration mStoreConfig;
    private IAPUser mIAPUser;

    private String mModifyProductUrl;
    private String mPaymentDetailsUrl;
    private String mAddressDetailsUrl;
    private String mAddressAlterUrl;
    private String mDeliveryModeUrl;
    private String mDeliveryAddressUrl;
    private String mSetPaymentDetails;
    private String mSetPaymentUrl;
    private String mPlaceOrderUrl;
    private String mCreateCartUrl;
    private String mAddToCartUrl;
    protected String mBaseURl;
    private String mCurrentCartUrl;

    private String mOauthUrl;
    private String mGetCartUrl;

    public Store(Context context, IAPUser iapUser) {
        mIAPUser = iapUser;
        mStoreConfig = setStoreConfig(context);
        createBaseUrl();
        createOauthUrl();
        generateGenericUrls();
    }

    protected StoreConfiguration setStoreConfig(final Context context) {
        return new StoreConfiguration(context);
    }

    private void createBaseUrl() {
        StringBuilder builder = new StringBuilder(HTTPS);
        builder.append(mStoreConfig.getHostPort()).append(SEPERATOR);
        builder.append(WEB_ROOT).append(SEPERATOR);
        builder.append(V2).append(SEPERATOR);
        builder.append(mStoreConfig.getSite()).append(SEPERATOR);
        builder.append(USER).append(SEPERATOR).append(mIAPUser.getJanRainEmail());

        mBaseURl = builder.toString();
    }

    private void createOauthUrl() {
        StringBuilder builder = new StringBuilder(HTTPS);
        builder.append(mStoreConfig.getHostPort()).append(SEPERATOR);
        builder.append(WEB_ROOT).append(SEPERATOR);
        builder.append(SUFFIX_OAUTH);

        mOauthUrl = String.format(builder.toString(), mIAPUser.getJanRainID());
    }

    protected void generateGenericUrls() {
        mCurrentCartUrl = mBaseURl.concat(SUFFIX_CURRENT_CART);
        mGetCartUrl = mBaseURl.concat(SUFFIX_GET_CART);
        mCreateCartUrl = mBaseURl.concat(SUFFIX_CART_CREATE);
        mAddToCartUrl = mCurrentCartUrl.concat(SUFFIX_CART_ENTRIES);
        mPaymentDetailsUrl = mBaseURl.concat(SUFFIX_PAYMENT_DETAILS);
        mAddressDetailsUrl = mBaseURl.concat(SUFFIX_ADDRESSES_FULL);
        mAddressAlterUrl = mBaseURl.concat(SUFFIX_ADDRESSES_ALTER);
        mSetPaymentUrl = mBaseURl.concat(SUFFIX_SET_PAYMENT_URL);
        mPlaceOrderUrl = mBaseURl.concat(SUFFIX_PLACE_ORDER);

        mModifyProductUrl = mCurrentCartUrl.concat(SUFFIX_PRODUCT_MODIFY);
        mDeliveryModeUrl = mCurrentCartUrl.concat(SUFFIX_DELIVERY_MODE);
        mDeliveryAddressUrl = mCurrentCartUrl.concat(SUFFIX_DELIVERY_ADDRESS);
        mSetPaymentDetails = mCurrentCartUrl.concat(SUFFIX_SET_PAYMENT_DETAILS);
    }

    public String getOauthUrl() {
        return mOauthUrl;
    }

    public String getJanRainEmail() {
        return mIAPUser.getJanRainEmail();
    }

    //Request Urls
    public String getCurrentCartDetailsUrl() {
        return mGetCartUrl;
    }

    public String getCreateCartUrl() {
        return mCreateCartUrl;
    }

    public String getAddToCartUrl() {
        return mAddToCartUrl;
    }

    public String getModifyProductUrl(String productID) {
        return String.format(mModifyProductUrl, productID);
    }

    public String getPaymentDetailsUrl() {
        return mPaymentDetailsUrl;
    }

    public String getAddressDetailsUrl() {
        return mAddressDetailsUrl;
    }

    public String getAddressAlterUrl(String addressID) {
        return String.format(mAddressAlterUrl, addressID);
    }

    public String getUpdateDeliveryModeUrl() {
        return mDeliveryModeUrl;
    }

    public String getUpdateDeliveryAddressUrl() {
        return mDeliveryAddressUrl;
    }

    public String getSetPaymentUrl(String id) {
        return String.format(mSetPaymentUrl, id);
    }

    public String getPlaceOrderUrl() {
        return mPlaceOrderUrl;
    }

    public String getSetPaymentDetailsUrl(){
        return mSetPaymentDetails;
    }
}