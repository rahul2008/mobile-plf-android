/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
package com.philips.cdp.di.ecs.store;

import android.util.Log;

import com.philips.cdp.di.ecs.util.ECSConfig;


public class ECSURLBuilder extends AbstractStore {

    private static final String SUFFIX_CONFIGURATION = "inAppConfig";

    public static final String WEBROOT = "pilcommercewebservices";
    public static final String V2 = "v2";
    public static final String SEPERATOR = "/";
    private static final String USER = "users";
    private static final String FIELDS_FULL_LANG = "?fields=FULL&lang=";
    private static final String SUFFIX_LANG_QUESTION = "?lang=";

    //Region API
    private static final String METAINFO = "metainfo";
    private static final String REGIONS = "regions";

    //Oauth
    private static final String SUFFIX_OAUTH ="oauth/token";


    private static final String SUFFIX_REFRESH_OAUTH = "oauth/token";

    //Requests
    private static final String SUFFIX_CARTS = "carts";
    private static final String SUFFIX_CURRENT = "current";
    private static final String SUFFIX_ENTRIES = "entries";

    /*ToDO : using lang=en instead of locale as backend not support*/
    private String SUFFIX_PRODUCT_CATALOG = "products/search?query=::category:Tuscany_Campaign&lang=";

    private static final String SUFFIX_PRODUCTS = "products";
    private static final String SUFFIX_CURRENT_PAGE = "&currentPage=%s";
    private static final String SUFFIX_STRING_PARAM = "%s";

    private static final String SUFFIX_ADDRESSES = "addresses";
    private static final String SUFFIX_DELIVERY_ADDRESS = "delivery";

    private static final String SUFFIX_DELIVERY_MODE = "deliverymode";
    private static final String SUFFIX_DELIVERY_MODES = "deliverymodes";

    private static final String SUFFIX_PAYMENT_DETAILS = "/paymentdetails";
    private static final String SUFFIX_ORDERS = "/orders";
    private static final String SUFFIX_PAY = "/pay";
    private static final String SUFFIX_CONTACT_PHONE_URL = "%s" + ".querytype.(fallback)";

    private static final String SUFFIX_VOUCHERS = "vouchers";
    private static final String SUFFIX_LANGUAGE = "&lang=";

    private boolean mIsNewUser;



    private String mOauthUrl;
    private String mOauthRefreshUrl;

    protected String mBaseURl,mBaseUrlCart;
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

    private String mApplyVoucherUrl;


   /* void generateStoreUrls() {
        createOauthUrl();
        createOAuthRefreshUrl();
        createBaseUrl();
        createBaseUrlForCreateCart();
        createBaseUrlForProductCatalog();
        generateGenericUrls();
    }*/

//    private void createOauthUrl() {
//        StringBuilder builder = new StringBuilder(mStoreConfig.getHostPort());
//        builder.append(WEBROOT).append(SEPERATOR).append(SUFFIX_OAUTH);
//        mOauthUrl = String.format(builder.toString(), mIAPUser.getJanRainID());
//    }
//
//    private void createOAuthRefreshUrl() {
//        StringBuilder builder = new StringBuilder(mStoreConfig.getHostPort());
//        builder.append(WEBROOT).append(SEPERATOR).append(SUFFIX_REFRESH_OAUTH);
//        mOauthRefreshUrl = builder.toString();
//    }
//
//    private void createBaseUrl() {
//        StringBuilder builder = new StringBuilder(mStoreConfig.getHostPort());
//        builder.append(WEBROOT).append(SEPERATOR).append(V2).append(SEPERATOR);
//        builder.append(mStoreConfig.getSite()).append(SEPERATOR);
//        builder.append(USER).append(SUFFIX_CURRENT);
//        //builder.append(USER)/*.append(SEPERATOR).append(mIAPUser.getJanRainEmail())*/;
//        mBaseURl = builder.toString();
//    }
//
//    private void createBaseUrlForCreateCart() {
//        StringBuilder builder = new StringBuilder(mStoreConfig.getHostPort());
//        builder.append(WEBROOT).append(SEPERATOR).append(V2).append(SEPERATOR);
//        builder.append(mStoreConfig.getSite()).append(SEPERATOR);
//        builder.append(USER);
//        mBaseUrlCart = builder.toString();
//    }
//    private void createBaseUrlForProductCatalog() {
//        StringBuilder builder = new StringBuilder(mStoreConfig.getHostPort());
//        builder.append(WEBROOT).append(SEPERATOR).append(V2).append(SEPERATOR);
//        builder.append(mStoreConfig.getSite()).append(SEPERATOR);
//        mBaseURlForProductCatalog = builder.toString();
//    }
//
//    private String createRegionsUrl() {
//        StringBuilder builder = new StringBuilder(mStoreConfig.getHostPort());
//        builder.append(WEBROOT).append(SEPERATOR).append(V2).append(SEPERATOR);
//        builder.append(METAINFO).append(SEPERATOR);
//        builder.append(REGIONS).append(SEPERATOR);
//        builder.append(getCountry()).append(FIELDS_FULL_LANG);
//        return builder.toString();
//    }
//
//    protected void generateGenericUrls() {
//
//        SUFFIX_PRODUCT_CATALOG = "products/search?query=::category:"+mStoreConfig.getCampaign()+"&lang=";
//
//        //Carts
//        String baseCartUrl = mBaseURl.concat(SUFFIX_CARTS);
//        mGetCartsUrl = baseCartUrl.concat(FIELDS_FULL_LANG) + mStoreConfig.getLocale();
//        mGetCurrentCartUrl = baseCartUrl.concat(SUFFIX_CURRENT).concat(FIELDS_FULL_LANG) + mStoreConfig.getLocale();
//       // mCreateCartUrl = baseCartUrl.concat(FIELDS_FULL_LANG) + mStoreConfig.getLocale();
//        mCreateCartUrl = mBaseUrlCart.concat(SUFFIX_CURRENT).concat(SUFFIX_CARTS).concat(FIELDS_FULL_LANG) + mStoreConfig.getLocale();
//        mDeleteCartUrl = baseCartUrl.concat(SUFFIX_CURRENT).concat(LANG) + mStoreConfig.getLocale();
//        mAddToCartUrl = baseCartUrl.concat(SUFFIX_CURRENT).concat(SUFFIX_ENTRIES).concat(FIELDS_FULL_LANG) + mStoreConfig.getLocale();
//
//        //Product
//        mGetProductCatalogUrl = mBaseURlForProductCatalog.concat(SUFFIX_PRODUCT_CATALOG).concat(mStoreConfig.getLocale()).concat("&currentPage=%s&pageSize=%s");
//        mSearchProductUrl = mBaseURlForProductCatalog.concat(SUFFIX_PRODUCTS).concat(SUFFIX_STRING_PARAM);
//        mUpdateProductUrl = baseCartUrl.concat(SUFFIX_CURRENT).
//                concat(SUFFIX_ENTRIES).concat(SUFFIX_STRING_PARAM).concat(FIELDS_FULL_LANG) + mStoreConfig.getLocale();
//
//        //Address
//        mRegionsUrl = createRegionsUrl().concat(mStoreConfig.getLocale());
//        mGetUserUrl = mBaseURl.concat(FIELDS_FULL_LANG) + mStoreConfig.getLocale();
//        mAddressDetailsUrl = mBaseURl.concat(SUFFIX_ADDRESSES).concat(FIELDS_FULL_LANG) + mStoreConfig.getLocale();
//        mEditAddressUrl = mBaseURl.concat(SUFFIX_ADDRESSES).concat(SUFFIX_STRING_PARAM).concat(FIELDS_FULL_LANG) + mStoreConfig.getLocale();
//        mDeliveryAddressUrl = baseCartUrl.concat(SUFFIX_CURRENT).
//                concat(SUFFIX_ADDRESSES).concat(SUFFIX_DELIVERY_ADDRESS).concat(FIELDS_FULL_LANG) + mStoreConfig.getLocale();
//
//        //Delivery mode
//        mDeliveryModeUrl = baseCartUrl.concat(SUFFIX_CURRENT).concat(SUFFIX_DELIVERY_MODE).concat(FIELDS_FULL_LANG) + mStoreConfig.getLocale();
//        mGetDeliveryModesUrl = baseCartUrl.concat(SUFFIX_CURRENT).concat(SUFFIX_DELIVERY_MODES).concat(LANG) + mStoreConfig.getLocale();
//
//        //Payment
//        mGetPaymentDetailsUrl = mBaseURl.concat(SUFFIX_PAYMENT_DETAILS).concat(FIELDS_FULL_LANG) + mStoreConfig.getLocale();
//        mSetPaymentDetailsUrl = baseCartUrl.concat(SUFFIX_CURRENT).concat(SUFFIX_PAYMENT_DETAILS).concat(FIELDS_FULL_LANG) + mStoreConfig.getLocale();
//        mMakePaymentUrl = mBaseURl.concat(SUFFIX_ORDERS).
//                concat(SUFFIX_STRING_PARAM).concat(SUFFIX_PAY).concat(FIELDS_FULL_LANG) + mStoreConfig.getLocale();
//        mPlaceOrderUrl = mBaseURl.concat(SUFFIX_ORDERS).concat(FIELDS_FULL_LANG) + mStoreConfig.getLocale();
//
//        //Orders
//        mOrderHistoryUrl = mPlaceOrderUrl.concat(SUFFIX_CURRENT_PAGE);
//        mOrderDetailUrl = mBaseURl.concat(SUFFIX_ORDERS).concat(SUFFIX_STRING_PARAM).concat(FIELDS_FULL_LANG) + mStoreConfig.getLocale();
//        mGetPhoneContactUrl = "https://www.philips.com/prx/cdls/B2C/" +
//                mStoreConfig.getLocale() + "/CARE/".concat(SUFFIX_CONTACT_PHONE_URL);
//
//        //Vouchers
//        mApplyVoucherUrl = mBaseURl.concat(SUFFIX_CARTS).concat(SUFFIX_CURRENT).concat(SUFFIX_VOUCHERS).concat(LANG)+ mStoreConfig.getLocale();
//    }

    //OAuth
    @Override
    public String getOauthUrl(String janRainID) {
        StringBuilder builder = new StringBuilder(ECSConfig.INSTANCE.getBaseURL());
        builder.append(WEBROOT).append(SEPERATOR).append(SUFFIX_OAUTH)
        .append("?janrain=").append(janRainID)
        .append("&grant_type=janrain&client_id=mobile_android&client_secret=secret");
        return  builder.toString();
    }

    @Override
    public String getOauthRefreshUrl() {
        return mOauthRefreshUrl;
    }

    //Product
    @Override
    public String getProductCatalogUrl(int currentPage, int pageSize) {

        StringBuilder builder = new StringBuilder(ECSConfig.INSTANCE.getBaseURL());
        builder.append(WEBROOT).append(SEPERATOR).append(V2).append(SEPERATOR);
        builder.append(ECSConfig.INSTANCE.getSiteId()).append(SEPERATOR);
        String baseURlForProductCatalog = builder.toString();

        String SUFFIX_PRODUCT_CATALOG = "products/search?query=::category:"+ECSConfig.INSTANCE.getRootCategory()+"&lang=";
        mGetProductCatalogUrl = baseURlForProductCatalog.concat(SUFFIX_PRODUCT_CATALOG).concat(ECSConfig.INSTANCE.getLocale()).concat("&currentPage=%s&pageSize=%s");

        if (mGetProductCatalogUrl != null)
            return String.format(mGetProductCatalogUrl, currentPage, pageSize);
        return null;
    }


    // this is one of theIAP  entry point where directly product detail is launched
    @Override
    public String getProduct(String ctnNumber) {
        String formattedCtnNumber = ctnNumber.replace('/', '_');
        StringBuilder builder = new StringBuilder(ECSConfig.INSTANCE.getBaseURL());
        builder.append(WEBROOT).append(SEPERATOR).append(V2).append(SEPERATOR);
        builder.append(ECSConfig.INSTANCE.getSiteId()).append(SEPERATOR).append(SUFFIX_PRODUCTS)
                .append(SEPERATOR).append(formattedCtnNumber).append(SUFFIX_LANG_QUESTION).append(ECSConfig.INSTANCE.getLocale());

        return builder.toString();
    }

    @Override
    public String getUpdateProductUrl(String productID) {
        return ECSConfig.INSTANCE.getBaseURL() + ECSURLBuilder.WEBROOT + ECSURLBuilder.SEPERATOR + ECSURLBuilder.V2 + ECSURLBuilder.SEPERATOR
                +ECSConfig.INSTANCE.getSiteId()+ECSURLBuilder.SEPERATOR
                +USER+SEPERATOR
                +SUFFIX_CURRENT+SEPERATOR
                +SUFFIX_CARTS+SEPERATOR
                +SUFFIX_CURRENT+SEPERATOR
                +SUFFIX_ENTRIES+SEPERATOR
                +productID+FIELDS_FULL_LANG
                +ECSConfig.INSTANCE.getLocale();
    }

    @Override
    public String getSearchProductUrl(String ctnNumber) {
        if(mUpdateProductUrl != null && ctnNumber!=null){
            return String.format(mSearchProductUrl, ctnNumber);
        }
        else{
            return null;
        }

    }

    //Carts
    @Override
    public String getCartsUrl() {
        return ECSConfig.INSTANCE.getBaseURL() + ECSURLBuilder.WEBROOT + ECSURLBuilder.SEPERATOR + ECSURLBuilder.V2 + ECSURLBuilder.SEPERATOR
                +ECSConfig.INSTANCE.getSiteId()+ECSURLBuilder.SEPERATOR
                +USER+ECSURLBuilder.SEPERATOR
                +SUFFIX_CURRENT+ECSURLBuilder.SEPERATOR
                +SUFFIX_CARTS+ECSURLBuilder.SEPERATOR
                +SUFFIX_CURRENT+SUFFIX_LANG_QUESTION
                +ECSConfig.INSTANCE.getLocale();

    }

    @Override
    public String getCurrentCartUrl() {
        return mGetCurrentCartUrl;
    }

    @Override
    public String getCreateCartUrl() {
        return ECSConfig.INSTANCE.getBaseURL() + ECSURLBuilder.WEBROOT + ECSURLBuilder.SEPERATOR + ECSURLBuilder.V2 + ECSURLBuilder.SEPERATOR
                +ECSConfig.INSTANCE.getSiteId()+ECSURLBuilder.SEPERATOR
                +USER+ECSURLBuilder.SEPERATOR
                +SUFFIX_CURRENT+ECSURLBuilder.SEPERATOR
                +SUFFIX_CARTS+SUFFIX_LANG_QUESTION
                +ECSConfig.INSTANCE.getLocale();
    }

    @Override
    public String getDeleteCartUrl() {
        return mDeleteCartUrl;
    }

    @Override
    public String getAddToCartUrl() {
        return ECSConfig.INSTANCE.getBaseURL() + ECSURLBuilder.WEBROOT + ECSURLBuilder.SEPERATOR + ECSURLBuilder.V2 + ECSURLBuilder.SEPERATOR
                +ECSConfig.INSTANCE.getSiteId()+ECSURLBuilder.SEPERATOR
                +USER+ECSURLBuilder.SEPERATOR
                +SUFFIX_CURRENT+ECSURLBuilder.SEPERATOR
                +SUFFIX_CARTS+ECSURLBuilder.SEPERATOR
                +SUFFIX_CURRENT+ECSURLBuilder.SEPERATOR
                +SUFFIX_ENTRIES+FIELDS_FULL_LANG
                +ECSConfig.INSTANCE.getLocale();
    }

    //Address
    @Override
    public String getRegionsUrl() {
       return ECSConfig.INSTANCE.getBaseURL() + ECSURLBuilder.WEBROOT + ECSURLBuilder.SEPERATOR + ECSURLBuilder.V2 + ECSURLBuilder.SEPERATOR+
                 METAINFO+SEPERATOR+
                 REGIONS+SEPERATOR+
                 ECSConfig.INSTANCE.getCountry()+FIELDS_FULL_LANG +ECSConfig.INSTANCE.getLocale();
    }

    @Override
    public String getUserUrl() {
        return mGetUserUrl;
    }

    @Override
    public String getAddressesUrl() {
       return ECSConfig.INSTANCE.getBaseURL() + ECSURLBuilder.WEBROOT + ECSURLBuilder.SEPERATOR + ECSURLBuilder.V2 + ECSURLBuilder.SEPERATOR+
                ECSConfig.INSTANCE.getSiteId()+ECSURLBuilder.SEPERATOR+
                USER+SEPERATOR +
                SUFFIX_CURRENT+SEPERATOR +
                SUFFIX_ADDRESSES+ FIELDS_FULL_LANG +ECSConfig.INSTANCE.getLocale() ;
    }

    @Override
    public String getEditAddressUrl(String addressID) {

       return ECSConfig.INSTANCE.getBaseURL() + ECSURLBuilder.WEBROOT + ECSURLBuilder.SEPERATOR + ECSURLBuilder.V2 + ECSURLBuilder.SEPERATOR+
                ECSConfig.INSTANCE.getSiteId()+ECSURLBuilder.SEPERATOR+
                USER+SEPERATOR +
                SUFFIX_CURRENT+SEPERATOR +
                SUFFIX_ADDRESSES+SEPERATOR+
                addressID+
                FIELDS_FULL_LANG +ECSConfig.INSTANCE.getLocale() ;
    }

    @Override
    public String getSetDeliveryAddressUrl() {

      return   ECSConfig.INSTANCE.getBaseURL() + ECSURLBuilder.WEBROOT + ECSURLBuilder.SEPERATOR + ECSURLBuilder.V2 + ECSURLBuilder.SEPERATOR+
                ECSConfig.INSTANCE.getSiteId()+ECSURLBuilder.SEPERATOR+
                USER+SEPERATOR +
                SUFFIX_CURRENT+SEPERATOR +
                SUFFIX_CARTS+SEPERATOR+
                SUFFIX_CURRENT+SEPERATOR +
                SUFFIX_ADDRESSES+SEPERATOR+
                SUFFIX_DELIVERY_ADDRESS+
                FIELDS_FULL_LANG +ECSConfig.INSTANCE.getLocale() ;
    }

    //Delivery mode
    @Override
    public String getDeliveryModesUrl() {
        return ECSConfig.INSTANCE.getBaseURL()+ WEBROOT + SEPERATOR + V2 + SEPERATOR+
                ECSConfig.INSTANCE.getSiteId()+SEPERATOR+
                USER+SEPERATOR+
                SUFFIX_CURRENT+SEPERATOR+
                SUFFIX_CARTS+SEPERATOR+
                SUFFIX_CURRENT+SEPERATOR+
                SUFFIX_DELIVERY_MODES+
                FIELDS_FULL_LANG +ECSConfig.INSTANCE.getLocale();
    }

    @Override
    public String getSetDeliveryModeUrl() {

        return ECSConfig.INSTANCE.getBaseURL()+ WEBROOT + SEPERATOR + V2 + SEPERATOR+
                ECSConfig.INSTANCE.getSiteId()+SEPERATOR+
                USER+SEPERATOR+
                SUFFIX_CURRENT+SEPERATOR+
                SUFFIX_CARTS+SEPERATOR+
                SUFFIX_CURRENT+SEPERATOR+
                SUFFIX_DELIVERY_MODE+
                FIELDS_FULL_LANG +ECSConfig.INSTANCE.getLocale();
    }

    //Payment
    @Override
    public String getPaymentDetailsUrl() {

        return ECSConfig.INSTANCE.getBaseURL()+ WEBROOT + SEPERATOR + V2 + SEPERATOR+
                ECSConfig.INSTANCE.getSiteId()+SEPERATOR+
                USER+SEPERATOR+
                SUFFIX_CURRENT+
                SUFFIX_PAYMENT_DETAILS+
                FIELDS_FULL_LANG +ECSConfig.INSTANCE.getLocale();
    }

    @Override
    public String getSetPaymentDetailsUrl() {
        return ECSConfig.INSTANCE.getBaseURL()+ WEBROOT + SEPERATOR + V2 + SEPERATOR+
                ECSConfig.INSTANCE.getSiteId()+SEPERATOR+
                USER+SEPERATOR+
                SUFFIX_CURRENT+SEPERATOR+
                SUFFIX_CARTS+SEPERATOR+
                SUFFIX_CURRENT+
                SUFFIX_PAYMENT_DETAILS+
                FIELDS_FULL_LANG +ECSConfig.INSTANCE.getLocale();
    }

    @Override
    public String getMakePaymentUrl(String orderId) {
        return ECSConfig.INSTANCE.getBaseURL()+ WEBROOT + SEPERATOR + V2 + SEPERATOR+
                ECSConfig.INSTANCE.getSiteId()+SEPERATOR+
                USER+SEPERATOR+
                SUFFIX_CURRENT+
                SUFFIX_ORDERS+
                SEPERATOR+orderId+
                SUFFIX_PAY;
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

    @Override
    public String getApplyVoucherUrl() {
       return ECSConfig.INSTANCE.getBaseURL()+ WEBROOT + SEPERATOR + V2 + SEPERATOR+
               ECSConfig.INSTANCE.getSiteId()+SEPERATOR+
               USER+SEPERATOR+
               SUFFIX_CURRENT+SEPERATOR+
               SUFFIX_CARTS+SEPERATOR+
               SUFFIX_CURRENT+SEPERATOR+
               SUFFIX_VOUCHERS+SUFFIX_LANG_QUESTION+
               ECSConfig.INSTANCE.getLocale();
    }

    @Override
    public String getDeleteVoucherUrl(String voucherId) {
        return ECSConfig.INSTANCE.getBaseURL()+ WEBROOT + SEPERATOR + V2 + SEPERATOR+
                ECSConfig.INSTANCE.getSiteId()+SEPERATOR+
                USER+SEPERATOR+
                SUFFIX_CURRENT+SEPERATOR+
                SUFFIX_CARTS+SEPERATOR+
                SUFFIX_CURRENT+SEPERATOR+
                SUFFIX_VOUCHERS+SEPERATOR+
                voucherId+
                SUFFIX_LANG_QUESTION+ ECSConfig.INSTANCE.getLocale();

    }

    @Override
    public String getAppliedVoucherUrl() {
        return ECSConfig.INSTANCE.getBaseURL()+ WEBROOT + SEPERATOR + V2 + SEPERATOR+
                ECSConfig.INSTANCE.getSiteId()+SEPERATOR+
                USER+SEPERATOR+
                SUFFIX_CURRENT+SEPERATOR+
                SUFFIX_CARTS+SEPERATOR+
                SUFFIX_CURRENT+SEPERATOR+
                SUFFIX_VOUCHERS+
                SUFFIX_LANG_QUESTION+ ECSConfig.INSTANCE.getLocale();
    }

    public String getRawConfigUrl() {
        return ECSConfig.INSTANCE.getBaseURL() + ECSURLBuilder.WEBROOT + ECSURLBuilder.SEPERATOR + ECSURLBuilder.V2 + ECSURLBuilder.SEPERATOR +
                SUFFIX_CONFIGURATION + ECSURLBuilder.SEPERATOR +
                ECSConfig.INSTANCE.getLocale() + ECSURLBuilder.SEPERATOR +
                ECSConfig.INSTANCE.getPropositionID();
    }

}