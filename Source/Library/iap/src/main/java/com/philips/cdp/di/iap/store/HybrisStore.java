/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
package com.philips.cdp.di.iap.store;

import android.content.Context;

import com.philips.cdp.di.iap.integration.IAPSettings;
import com.philips.cdp.di.iap.session.RequestListener;

import java.util.Locale;

public class HybrisStore extends AbstractStore {

    public static final String WEBROOT = "pilcommercewebservices";
    public static final String V2 = "v2";
    public static final String SEPERATOR = "/";
    private static final String USER = "users";
    private static final String LANG = "?fields=FULL&lang=";

    //Region API
    private static final String METAINFO = "metainfo";
    private static final String REGIONS = "regions";

    //Oauth
    private static final String SUFFIX_OAUTH =
            "oauth/token?janrain=%s&grant_type=janrain&client_id=mobile_android&client_secret=secret";
    private static final String SUFFIX_REFRESH_OAUTH = "oauth/token";

    //Requests
    private static final String SUFFIX_CARTS = "/carts";
    private static final String SUFFIX_CURRENT = "/current";
    private static final String SUFFIX_ENTRIES = "/entries";

    /*ToDO : using lang=en instead of locale as backend not support*/
    private static final String SUFFIX_PRODUCT_CATALOG = "products/search?query=::category:Tuscany_Campaign"
            + "&lang=en" + "&currentPage=%s&pageSize=%s";
    private static final String SUFFIX_PRODUCTS = "products";
    private static final String SUFFIX_CURRENT_PAGE = "&currentPage=%s";
    private static final String SUFFIX_STRING_PARAM = "/%s";

    private static final String SUFFIX_ADDRESSES = "/addresses";
    private static final String SUFFIX_DELIVERY_ADDRESS = "/delivery";

    private static final String SUFFIX_DELIVERY_MODE = "/deliverymode";
    private static final String SUFFIX_DELIVERY_MODES = "/deliverymodes";

    private static final String SUFFIX_PAYMENT_DETAILS = "/paymentdetails";
    private static final String SUFFIX_ORDERS = "/orders";
    private static final String SUFFIX_PAY = "/pay";
    private static final String SUFFIX_CONTACT_PHONE_URL = "%s" + ".querytype.(fallback)";

    private boolean mIsNewUser;

    private StoreConfiguration mStoreConfig;
    public IAPUser mIAPUser;

    private String mOauthUrl;
    private String mOauthRefreshUrl;

    protected String mBaseURl;
    protected String mBaseURlForProductCatalog;

    private String mGetProductCatalogUrl;
    private String mSearchProductUrl;
    private String mUpdateProductUrl;

    private String mCreateCartUrl;
    private String mGetCartsUrl;
    private String mGetCurrentCartUrl;
    private String mAddToCartUrl;
    private String mDeleteCartUrl;

    private String mOrderHistoryUrl;
    private String mOrderDetailUrl;
    private String mGetPhoneContactUrl;

    private String mRegionsUrl;
    private String mGetUserUrl;
    private String mAddressDetailsUrl;
    private String mEditAddressUrl;
    private String mDeliveryAddressUrl;

    private String mDeliveryModeUrl;
    private String mGetDeliveryModesUrl;

    private String mGetPaymentDetailsUrl;
    private String mSetPaymentDetailsUrl;

    private String mMakePaymentUrl;
    private String mPlaceOrderUrl;

    public HybrisStore(Context context, IAPSettings iapSettings) {
        mIAPUser = createUser(context);
        mStoreConfig = getStoreConfig(context, iapSettings);
    }

    //User
    @Override
    public IAPUser getUser() {
        return mIAPUser;
    }

    @Override
    public void setNewUser(boolean isNewUser) {
        mIsNewUser = isNewUser;
    }

    @Override
    public boolean isNewUser() {
        return mIsNewUser;
    }

    IAPUser createUser(Context context) {
        mIsNewUser = false;
        mIAPUser = new IAPUser(context, this);
        return mIAPUser;
    }

    @Override
    public void createNewUser(Context context) {
        createUser(context);
        generateStoreUrls();
    }

    //Called when janrain token is changed
    void updateJanRainIDBasedUrls() {
        createOauthUrl();
    }

    @Override
    public String getJanRainEmail() {
        return mIAPUser.getJanRainEmail();
    }

    //Locale
    @Override
    public String getCountry() {
        if (mCountry != null) {
            return mCountry;
        }
        return "";
    }

    @Override
    public String getLocale() {
        return mStoreConfig.getLocale();
    }

    //Store
    StoreConfiguration getStoreConfig(final Context context, IAPSettings iapSettings) {
        return new StoreConfiguration(context, this, iapSettings);
    }

    @Override
    public void initStoreConfig(String language, String countryCode, RequestListener listener) {
        mLanguage = language;
        mCountry = countryCode;
        mStoreConfig.initConfig(language, countryCode, listener);
    }

    void generateStoreUrls() {
        setStoreInitialized(true);
        createOauthUrl();
        createOAuthRefreshUrl();
        createBaseUrl();
        createBaseUrlForProductCatalog();
        generateGenericUrls();
    }

    private void createOauthUrl() {
        StringBuilder builder = new StringBuilder(mStoreConfig.getHostPort());
        builder.append(WEBROOT).append(SEPERATOR).append(SUFFIX_OAUTH);
        mOauthUrl = String.format(builder.toString(), mIAPUser.getJanRainID());
    }

    private void createOAuthRefreshUrl() {
        StringBuilder builder = new StringBuilder(mStoreConfig.getHostPort());
        builder.append(WEBROOT).append(SEPERATOR).append(SUFFIX_REFRESH_OAUTH);
        mOauthRefreshUrl = builder.toString();
    }

    private void createBaseUrl() {
        StringBuilder builder = new StringBuilder(mStoreConfig.getHostPort());
        builder.append(WEBROOT).append(SEPERATOR).append(V2).append(SEPERATOR);
        builder.append(mStoreConfig.getSite()).append(SEPERATOR);
        builder.append(USER).append(SEPERATOR).append(mIAPUser.getJanRainEmail());
        mBaseURl = builder.toString();
    }

    private void createBaseUrlForProductCatalog() {
        StringBuilder builder = new StringBuilder(mStoreConfig.getHostPort());
        builder.append(WEBROOT).append(SEPERATOR).append(V2).append(SEPERATOR);
        builder.append(mStoreConfig.getSite()).append(SEPERATOR);
        mBaseURlForProductCatalog = builder.toString();
    }

    private String createRegionsUrl() {
        StringBuilder builder = new StringBuilder(mStoreConfig.getHostPort());
        builder.append(WEBROOT).append(SEPERATOR).append(V2).append(SEPERATOR);
        builder.append(METAINFO).append(SEPERATOR);
        builder.append(REGIONS).append(SEPERATOR);
        builder.append(getCountry()).append(LANG + Locale.getDefault().getLanguage());
        return builder.toString();
    }

    protected void generateGenericUrls() {

        //Carts
        String baseCartUrl = mBaseURl.concat(SUFFIX_CARTS);
        mGetCartsUrl = baseCartUrl.concat(LANG) + mStoreConfig.getLocale();
        mGetCurrentCartUrl = baseCartUrl.concat(SUFFIX_CURRENT).concat(LANG) + mStoreConfig.getLocale();
        mCreateCartUrl = baseCartUrl.concat(LANG) + mStoreConfig.getLocale();
        mDeleteCartUrl = baseCartUrl.concat(SUFFIX_CURRENT);
        mAddToCartUrl = baseCartUrl.concat(SUFFIX_CURRENT).concat(SUFFIX_ENTRIES).concat(LANG) + mStoreConfig.getLocale();

        //Product
        mGetProductCatalogUrl = mBaseURlForProductCatalog.concat(SUFFIX_PRODUCT_CATALOG);
        mSearchProductUrl = mBaseURlForProductCatalog.concat(SUFFIX_PRODUCTS).concat(SUFFIX_STRING_PARAM);
        mUpdateProductUrl = baseCartUrl.concat(SUFFIX_CURRENT).
                concat(SUFFIX_ENTRIES).concat(SUFFIX_STRING_PARAM).concat(LANG) + mStoreConfig.getLocale();

        //Address
        mRegionsUrl = createRegionsUrl();
        mGetUserUrl = mBaseURl.concat(LANG) + mStoreConfig.getLocale();
        mAddressDetailsUrl = mBaseURl.concat(SUFFIX_ADDRESSES).concat(LANG) + mStoreConfig.getLocale();
        mEditAddressUrl = mBaseURl.concat(SUFFIX_ADDRESSES).concat(SUFFIX_STRING_PARAM).concat(LANG) + mStoreConfig.getLocale();
        mDeliveryAddressUrl = baseCartUrl.concat(SUFFIX_CURRENT).
                concat(SUFFIX_ADDRESSES).concat(SUFFIX_DELIVERY_ADDRESS).concat(LANG) + mStoreConfig.getLocale();

        //Delivery mode
        mDeliveryModeUrl = baseCartUrl.concat(SUFFIX_CURRENT).concat(SUFFIX_DELIVERY_MODE).concat(LANG) + mStoreConfig.getLocale();
        mGetDeliveryModesUrl = baseCartUrl.concat(SUFFIX_CURRENT).concat(SUFFIX_DELIVERY_MODES).concat(LANG) + mStoreConfig.getLocale();

        //Payment
        mGetPaymentDetailsUrl = mBaseURl.concat(SUFFIX_PAYMENT_DETAILS).concat(LANG) + mStoreConfig.getLocale();
        mSetPaymentDetailsUrl = baseCartUrl.concat(SUFFIX_CURRENT).concat(SUFFIX_PAYMENT_DETAILS).concat(LANG) + mStoreConfig.getLocale();
        mMakePaymentUrl = mBaseURl.concat(SUFFIX_ORDERS).
                concat(SUFFIX_STRING_PARAM).concat(SUFFIX_PAY).concat(LANG) + mStoreConfig.getLocale();
        mPlaceOrderUrl = mBaseURl.concat(SUFFIX_ORDERS).concat(LANG) + mStoreConfig.getLocale();

        //Orders
        mOrderHistoryUrl = mPlaceOrderUrl.concat(SUFFIX_CURRENT_PAGE);
        mOrderDetailUrl = mBaseURl.concat(SUFFIX_ORDERS).concat(SUFFIX_STRING_PARAM).concat(LANG) + Locale.getDefault().getLanguage();
        mGetPhoneContactUrl = "http://www.philips.com/prx/cdls/B2C/" +
                mStoreConfig.getLocale() + "/CARE/".concat(SUFFIX_CONTACT_PHONE_URL);
    }

    //OAuth
    @Override
    public String getOauthUrl() {
        return mOauthUrl;
    }

    @Override
    public String getOauthRefreshUrl() {
        return mOauthRefreshUrl;
    }

    @Override
    public void refreshLoginSession() {
        mIAPUser.refreshLoginSession();
    }

    //Product
    @Override
    public String getProductCatalogUrl(int currentPage, int pageSize) {
        if (mGetProductCatalogUrl != null)
            return String.format(mGetProductCatalogUrl, currentPage, pageSize);
        return null;
    }

    @Override
    public String getUpdateProductUrl(String productID) {
        if (mUpdateProductUrl != null && productID != null)
            return String.format(mUpdateProductUrl, productID);
        else
            return null;
    }

    @Override
    public String getSearchProductUrl(String ctnNumber) {
        return String.format(mSearchProductUrl, ctnNumber);
    }

    //Carts
    @Override
    public String getCartsUrl() {
        return mGetCartsUrl;
    }

    @Override
    public String getCurrentCartUrl() {
        return mGetCurrentCartUrl;
    }

    @Override
    public String getCreateCartUrl() {
        return mCreateCartUrl;
    }

    @Override
    public String getDeleteCartUrl() {
        return mDeleteCartUrl;
    }

    @Override
    public String getAddToCartUrl() {
        return mAddToCartUrl;
    }

    //Address
    @Override
    public String getRegionsUrl() {
        return mRegionsUrl;
    }

    @Override
    public String getUserUrl() {
        return mGetUserUrl;
    }

    @Override
    public String getAddressesUrl() {
        return mAddressDetailsUrl;
    }

    @Override
    public String getEditAddressUrl(String addressID) {
        return String.format(mEditAddressUrl, addressID);
    }

    @Override
    public String getSetDeliveryAddressUrl() {
        return mDeliveryAddressUrl;
    }

    //Delivery mode
    @Override
    public String getDeliveryModesUrl() {
        return mGetDeliveryModesUrl;
    }

    @Override
    public String getSetDeliveryModeUrl() {
        return mDeliveryModeUrl;
    }

    //Payment
    @Override
    public String getPaymentDetailsUrl() {
        return mGetPaymentDetailsUrl;
    }

    @Override
    public String getSetPaymentDetailsUrl() {
        return mSetPaymentDetailsUrl;
    }

    @Override
    public String getMakePaymentUrl(String id) {
        return String.format(mMakePaymentUrl, id);
    }

    @Override
    public String getPlaceOrderUrl() {
        return mPlaceOrderUrl;
    }

    //Orders
    @Override
    public String getOrderHistoryUrl(String pageNumber) {
        return String.format(mOrderHistoryUrl, pageNumber);
    }

    @Override
    public String getOrderDetailUrl(String orderId) {
        return String.format(mOrderDetailUrl, orderId);
    }

    @Override
    public String getPhoneContactUrl(String category) {
        return String.format(mGetPhoneContactUrl, category);
    }
}