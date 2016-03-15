/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */

package com.philips.cdp.di.iap.store;

import android.content.Context;

import com.philips.cdp.di.iap.session.OAuthHandler;

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
    private String mJanRainID;
    private String userName;
    private String mBaseURl;
    private String mCurrentCartUrl;

    private OAuthHandler oAuthHandler;
    private Context context;
    private String mOauthUrl;

    public Store(Context context, final String hostPort, final String webRoot, final String userID,
                 final String janRainID) {
        this.context = context;
        this.mJanRainID = janRainID;
        this.userName = userID;
        mStoreConfig = new StoreConfiguration(context);
        mJanRainID = janRainID;
        createBaseUrl(userID);
        generateGenericUrls();
    }

    public Store(Context context, IAPUser iapUser) {
        mStoreConfig = new StoreConfiguration(context);
        createBaseUrl(iapUser.getJanRainEmail());
        createOauthUrl(iapUser.getJanRainID());
        generateGenericUrls();
    }

    protected void createBaseUrl(final String userID) {
        StringBuilder builder = new StringBuilder(HTTPS);
        builder.append(mStoreConfig.getHostPort()).append(SEPERATOR);
        builder.append(WEB_ROOT).append(SEPERATOR);
        builder.append(V2).append(SEPERATOR);
        builder.append(mStoreConfig.getSite()).append(SEPERATOR);
        builder.append(USER).append(SEPERATOR).append(userID);

        mBaseURl = builder.toString();
    }

    private void createOauthUrl(final String janRainID) {
        StringBuilder builder = new StringBuilder(HTTPS);
        builder.append(mStoreConfig.getHostPort()).append(SEPERATOR);
        builder.append(WEB_ROOT).append(SEPERATOR);
        builder.append(SUFFIX_OAUTH);

        mOauthUrl = String.format(builder.toString(), janRainID);
    }

    protected void generateGenericUrls() {
        mCurrentCartUrl = mBaseURl.concat(SUFFIX_CURRENT_CART);
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

    //Request Urls
    public String getCurrentCartUrl() {
        return mCurrentCartUrl;
    }

    public String getCreateCartUrl() {
        return mBaseURl.concat(SUFFIX_CART_CREATE);
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

    public String getDeliveryModeUrl() {
        return mDeliveryModeUrl;
    }

    public String getDeliveryAddressUrl() {
        return mDeliveryAddressUrl;
    }

    public String getSetPaymentUrl(String id) {
        return String.format(mSetPaymentUrl, id);
    }

    public String getPlaceOrderUrl() {
        return mPlaceOrderUrl;
    }
}