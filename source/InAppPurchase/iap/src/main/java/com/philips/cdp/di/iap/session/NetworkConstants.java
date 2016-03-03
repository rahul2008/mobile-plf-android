package com.philips.cdp.di.iap.session;

import java.util.Locale;

public class NetworkConstants {

    public static final String OAUTH_TOKEN = "oauth/token?username=" + IAPHandler.getUserName() + "&password=" + IAPHandler.getPassword() + "&grant_type=password&client_id=mobile_android&client_secret=secret";
    public static final String HOST_URL = "https://tst.pl.shop.philips.com/";
    public static final String WEB_ROOT = "pilcommercewebservices/";
    public static final String V2 = "v2/";
    public static final String APP_CONFIG = "US_Tuscany/";
    public static final String USER = "users/" + IAPHandler.getUserName() + "/";

    public static final String BASE_URL = HOST_URL + WEB_ROOT + V2 + APP_CONFIG + USER;
    public static final String CURRENT_CART = "carts/current";

   /* public static final String LOCALE = "en_US";
    public static final String PROPOSITION_ID = "Tuscany2016";
    public static final String CLIENT_ID = "mobile_android";
    public static final String CLIENT_SECRET = "secret";*/

    /**
     * Cart Url
     **/
    public static final String GET_CURRENT_CART_URL = BASE_URL + "carts?fields=FULL";
    public static final String CREATE_CART_URL = BASE_URL + "carts";
    public static final String ADD_TO_CART_URL = BASE_URL + CURRENT_CART + "/entries";
    public static final String DELETE_PRODUCT_URL = BASE_URL + CURRENT_CART + "/entries/%s";
    public static final String UPDATE_QUANTITY_URL = BASE_URL + CURRENT_CART + "/entries/%s";

    public static final String GET_PAYMENT_DETAILS_URL = BASE_URL + "paymentdetails";

    /**
     * Address Url
     **/
    public static final String ADDRESS_URL = BASE_URL + "addresses?fields=FULL";
    /**
     * Update Address Url
     **/
    public static final String UPDATE_OR_DELETE_ADDRESS_URL = BASE_URL + "addresses" + "/%s";
    public static final String SET_DELIVERY_MODE_URL = BASE_URL + CURRENT_CART + "/deliverymode";
    public static final String SET_DELIVERY_ADDRESS_URL = BASE_URL + CURRENT_CART + "/addresses/delivery";
    public static final String SET_PAYMENT_DETAILS_URL = BASE_URL + CURRENT_CART + "/paymentdetails";

    /**
     * Prx data
     **/
    public static final String PRX_SECTOR_CODE = "B2C";
    public static final String PRX_LOCALE = "en_US";
    public static final String PRX_CATALOG_CODE = "CONSUMER";
    public static final String EXTRA_ANIMATIONTYPE = "EXTRA_ANIMATIONTYPE";
    public static final String IS_ONLINE = "IS_ONLINE";

    public static final Locale STORE_LOCALE = Locale.US;
    public static final String EMPTY_RESPONSE = "";
}

