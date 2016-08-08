/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */

package com.philips.cdp.di.iap.store;

import android.content.Context;

import com.philips.cdp.di.iap.core.AbstractStoreSpec;
import com.philips.cdp.di.iap.session.NetworkConstants;
import com.philips.cdp.di.iap.session.RequestListener;
import com.philips.cdp.di.iap.utils.IAPLog;

public class HybrisStore extends AbstractStoreSpec {

    //Public since required by StoreConfiguration initialization
    public static final String HTTPS = "https://";
    public static final String WEB_ROOT = "pilcommercewebservices";
    public static final String V2 = "v2";
    public static final String SEPERATOR = "/";

    private static final String USER = "users";
    private static final String METAINFO = "metainfo";
    private static final String REGIONS = "regions";
    private static final String LANG = "?fields=FULL&lang=en";
    private static final String LANG_GB = "?fields=FULL&lang=en_GB";

    //Oauth
    private static final String SUFFIX_OAUTH =
            "oauth/token?janrain=%s&grant_type=janrain&client_id=mobile_android&client_secret=secret";

    //Requests
    private static final String SUFFIX_GET_CARTS = "/carts";
    private static final String SUFFIX_CART_CREATE = "/carts";
    private static final String SUFFIX_CART_DELETE = "/current";
    private static final String SUFFIX_CART_ENTRIES = "/current/entries";
    private static final String SUFFIX_PRODUCT_MODIFY = "/current/entries/%s";

    private static final String SUFFIX_PAYMENT_DETAILS = "/paymentdetails";
    private static final String SUFFIX_SET_PAYMENT_DETAILS = "/current/paymentdetails";
    private static final String SUFFIX_SET_PAYMENT_URL = "/orders/%s/pay";

    private static final String SUFFIX_ADDRESSES_FULL = "/addresses";
    private static final String SUFFIX_ADDRESSES_ALTER = "/addresses/%s";

    private static final String SUFFIX_DELIVERY_MODE = "/current/deliverymode";
    //LANG to be added to delivery mode once hybris comes with the fix.
    private static final String SUFFIX_GET_DELIVERY_MODE = "/current/deliverymodes";
    private static final String SUFFIX_DELIVERY_ADDRESS = "/current/addresses/delivery";

    private static final String SUFFIX_PLACE_ORDER = "/orders";
    private static final String SUFFIX_PPRODUCT_CATALOG = "products/search?query=::category:Tuscany_Campaign" + "&lang=en" + "&currentPage=%s&pageSize=%s";
    private static final String SUFFIX_REFRESH_OAUTH = "/oauth/token";

    private static final String PREFIX_RETAILERS = "www.philips.com/api/wtb/v1/";
    private static final String RETAILERS_ALTER = "online-retailers?product=%s&lang=en";

    private static final String SUFFIX_SEARCH_PRODUCT_URL = "products/%s";
    private static final String SUFFIX_ORDER_DETAIL_URL = "/orders/%s?fields=FULL&lang=en";
    private static final String SUFFIX_CURRENT_PAGE = "?fields=FULL&lang=en&currentPage=%s";

    private static final String SUFFIX_CONTACT_PHONE_URL = "%s" + ".querytype.(fallback)";

    private StoreConfiguration mStoreConfig;
    public IAPUser mIAPUser;

    private String mModifyProductUrl;
    private String mPaymentDetailsUrl;
    private String mAddressDetailsUrl;
    private String mRegionsUrl;
    private String mAddressAlterUrl;
    private String mDeliveryModeUrl;
    private String mGetDeliveryModeUrl;
    private String mDeliveryAddressUrl;
    private String mSetPaymentDetails;
    private String mSetPaymentUrl;
    private String mPlaceOrderUrl;
    private String mCreateCartUrl;
    private String mDeleteCartUrl;
    private String mAddToCartUrl;
    protected String mBaseURl;
    protected String mBaseURlForProductCatalog;
    private String mGetRetailersUrl;
    private String mOauthUrl;
    private String mOauthRefreshUrl;
    private String mGetCartsUrl;
    private String mGetProductCatalogUrl;
    private boolean mUserLoggedout;
    private String mRetailersAlter;
    private String mOrderHistoryUrl;
    private String mOrderDetailUrl;
    private String mSearchProductUrl;
    private String mGetUserUrl;
    private String mGetPhoneContactUrl;

    public HybrisStore(Context context) {
        mIAPUser = initIAPUser(context);
        mStoreConfig = getStoreConfig(context);
    }

    IAPUser initIAPUser(Context context) {
        mUserLoggedout = false;
        mIAPUser = new IAPUser(context, this);
        IAPLog.i(IAPLog.LOG, "initIAPUser = " + mIAPUser.getJanRainID());
        return mIAPUser;
    }

    @Override
    public void setNewUser(Context context) {
        initIAPUser(context);
        generateStoreUrls();
    }

    StoreConfiguration getStoreConfig(final Context context) {
        return new StoreConfiguration(context, this);
    }

    @Override
    public void initStoreConfig(String language, String countryCode, RequestListener listener) {
        mLanguage = language;
        mCountry = countryCode;
        mStoreConfig.initConfig(language, countryCode, listener);
    }

    void generateStoreUrls() {
        createBaseUrl();
        createBaseUrlForProductCatalog();
        createBaseUrlForRetailers();
        createOauthUrl();
        generateGenericUrls();
    }

    private void createBaseUrlForProductCatalog() {
        StringBuilder builder = new StringBuilder(HTTPS);
        builder.append(mStoreConfig.getHostPort()).append(SEPERATOR);
        builder.append(WEB_ROOT).append(SEPERATOR);
        builder.append(V2).append(SEPERATOR);
        builder.append(mStoreConfig.getSite()).append(SEPERATOR);

        mBaseURlForProductCatalog = builder.toString();
    }

    private void createBaseUrl() {
        setStoreInitialized(true);
        StringBuilder builder = new StringBuilder(HTTPS);
        builder.append(mStoreConfig.getHostPort()).append(SEPERATOR);
        builder.append(WEB_ROOT).append(SEPERATOR);
        builder.append(V2).append(SEPERATOR);
        builder.append(mStoreConfig.getSite()).append(SEPERATOR);
        builder.append(USER).append(SEPERATOR).append(mIAPUser.getJanRainEmail());

        mBaseURl = builder.toString();
    }

    private void createBaseUrlForRetailers() {
        StringBuilder builder = new StringBuilder(HTTPS);
        builder.append(PREFIX_RETAILERS).append(SEPERATOR);
        builder.append(NetworkConstants.PRX_SECTOR_CODE).append(SEPERATOR);
        builder.append(getLocale()).append(SEPERATOR);
        mGetRetailersUrl = builder.toString();
    }

    private void createOauthUrl() {
        StringBuilder builder = new StringBuilder(HTTPS);
        builder.append(mStoreConfig.getHostPort()).append(SEPERATOR);
        builder.append(WEB_ROOT).append(SEPERATOR);
        builder.append(SUFFIX_OAUTH);

        mOauthUrl = String.format(builder.toString(), mIAPUser.getJanRainID());
    }

    private String createRegionsUrl() {
        StringBuilder builder = new StringBuilder(HTTPS);
        builder.append(mStoreConfig.getHostPort()).append(SEPERATOR);
        builder.append(WEB_ROOT).append(SEPERATOR);
        builder.append(V2).append(SEPERATOR);
        builder.append(METAINFO).append(SEPERATOR);
        builder.append(REGIONS).append(SEPERATOR);
        builder.append(getCountry()).append(LANG);//Check whether to pass "UK" / "GB"
        return builder.toString();
    }

    protected void generateGenericUrls() {
        String getCartsUrl = mBaseURl.concat(SUFFIX_GET_CARTS);
        mGetCartsUrl = mBaseURl.concat(SUFFIX_GET_CARTS).concat(LANG);
        mCreateCartUrl = mBaseURl.concat(SUFFIX_CART_CREATE);
        mDeleteCartUrl = mCreateCartUrl.concat(SUFFIX_CART_DELETE);
        mAddToCartUrl = getCartsUrl.concat(SUFFIX_CART_ENTRIES);
        mPaymentDetailsUrl = mBaseURl.concat(SUFFIX_PAYMENT_DETAILS);
        mAddressDetailsUrl = mBaseURl.concat(SUFFIX_ADDRESSES_FULL);
        mRegionsUrl = createRegionsUrl();
        mAddressAlterUrl = mBaseURl.concat(SUFFIX_ADDRESSES_ALTER);
        mSetPaymentUrl = mBaseURl.concat(SUFFIX_SET_PAYMENT_URL);
        mPlaceOrderUrl = mBaseURl.concat(SUFFIX_PLACE_ORDER);
        mGetProductCatalogUrl = mBaseURlForProductCatalog.concat(SUFFIX_PPRODUCT_CATALOG);
        mModifyProductUrl = getCartsUrl.concat(SUFFIX_PRODUCT_MODIFY);
        mDeliveryModeUrl = getCartsUrl.concat(SUFFIX_DELIVERY_MODE);
        mGetDeliveryModeUrl = getCartsUrl.concat(SUFFIX_GET_DELIVERY_MODE);
        mDeliveryAddressUrl = getCartsUrl.concat(SUFFIX_DELIVERY_ADDRESS);
        mSetPaymentDetails = getCartsUrl.concat(SUFFIX_SET_PAYMENT_DETAILS);
        mRetailersAlter = mGetRetailersUrl.concat(RETAILERS_ALTER);
        mOauthRefreshUrl = HTTPS.concat(mStoreConfig.getHostPort()).concat(SEPERATOR)
                .concat(WEB_ROOT).concat(SUFFIX_REFRESH_OAUTH);
        mOrderHistoryUrl = mPlaceOrderUrl.concat(SUFFIX_CURRENT_PAGE);
        mOrderDetailUrl = mBaseURl.concat(SUFFIX_ORDER_DETAIL_URL);
        mSearchProductUrl = mBaseURlForProductCatalog.concat(SUFFIX_SEARCH_PRODUCT_URL);
        mGetUserUrl = mBaseURl.concat(LANG);
        mGetPhoneContactUrl = "http://www.philips.com/prx/cdls/B2C/en_" + getCountry() +"/CARE/".concat(SUFFIX_CONTACT_PHONE_URL);
    }

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

    //Package level access
    //Called when janrain token is changed
    void updateJanRainIDBasedUrls() {
        createOauthUrl();
    }

    @Override
    public String getOauthUrl() {
        return mOauthUrl;
    }

    @Override
    public String getOauthRefreshUrl() {
        return mOauthRefreshUrl;
    }

    @Override
    public String getJanRainEmail() {
        return mIAPUser.getJanRainEmail();
    }

    @Override
    public IAPUser getUser() {
        return mIAPUser;
    }

    @Override
    public String getCartsUrl() {
        return mGetCartsUrl;
    }

    @Override
    public String getProductCatalogUrl(int currentPage, int pageSize) {
        if (mGetProductCatalogUrl != null)
            return String.format(mGetProductCatalogUrl, currentPage, pageSize);
        return null;
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

    @Override
    public String getModifyProductUrl(String productID) {
        if (mModifyProductUrl != null && productID != null)
            return String.format(mModifyProductUrl, productID);
        else
            return null;
    }

    @Override
    public String getPaymentDetailsUrl() {
        return mPaymentDetailsUrl;
    }

    @Override
    public String getAddressDetailsUrl() {
        if (getCountry().equalsIgnoreCase("GB")) {
            return mAddressDetailsUrl.concat(LANG);
        } else if (getCountry().equalsIgnoreCase("US")) {
            return mAddressDetailsUrl.concat(LANG);
        }
        return mAddressDetailsUrl;
    }

    @Override
    public String getRegionsUrl() {
        return mRegionsUrl;
    }

    @Override
    public String getAddressAlterUrl(String addressID) {
        return String.format(mAddressAlterUrl, addressID);
    }

    @Override
    public String getRetailersAlterUrl(String CTN) {
        return String.format(mRetailersAlter, CTN);
    }

    @Override
    public String getUpdateDeliveryModeUrl() {
        return mDeliveryModeUrl;
    }

    @Override
    public String getUpdateDeliveryAddressUrl() {
        return mDeliveryAddressUrl;
    }

    @Override
    public String getSetPaymentUrl(String id) {
        return String.format(mSetPaymentUrl, id);
    }

    @Override
    public String getPlaceOrderUrl() {
        return mPlaceOrderUrl;
    }

    @Override
    public String getSetPaymentDetailsUrl() {
        return mSetPaymentDetails;
    }

    @Override
    public void refreshLoginSession() {
        mIAPUser.refreshLoginSession();
    }

    @Override
    public void setUserLogout(boolean userLoggedout) {
        this.mUserLoggedout = userLoggedout;
    }

    @Override
    public boolean isUserLoggedOut() {
        return mUserLoggedout;
    }

    @Override
    public String getOrderDetailUrl(String orderId) {
        return String.format(mOrderDetailUrl, orderId);
    }

    @Override
    public String getSearchProductUrl(String ctnNumber) {
        return String.format(mSearchProductUrl, ctnNumber);
    }

    @Override
    public String getOrderHistoryUrl(String pageNumber) {
        return String.format(mOrderHistoryUrl, pageNumber);
    }

    @Override
    public String getDeliveryModesUrl() {
        return mGetDeliveryModeUrl;
    }

    @Override
    public String getUserUrl() {
        return mGetUserUrl;
    }

    @Override
    public String getPhoneContactUrl(String category) {
        return String.format(mGetPhoneContactUrl, category);
    }
}