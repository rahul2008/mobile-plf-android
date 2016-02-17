package com.philips.cdp.di.iap.session;

import java.util.Locale;

public class NetworkConstants {

    public static final String HOST_URL = "https://tst.admin.shop.philips.com/";
    public static final String WEB_ROOT = "pilcommercewebservices/";
    public static final String V2 = "v2/";
    public static final String APP_CONFIG = "US_Tuscany/";
    public static final String USER = "users/inapp@3/";
    public static final String BASE_URL = HOST_URL + WEB_ROOT + V2 + APP_CONFIG + USER;
    public static final String CURRENT_CART = "carts/current";

   /* public static final String LOCALE = "en_US";
    public static final String PROPOSITION_ID = "Tuscany2016";
    public static final String CLIENT_ID = "mobile_android";
    public static final String CLIENT_SECRET = "secret";*/

    public final static String GET_CURRENT_CART_URL = BASE_URL + "carts";
    public static final String CREATE_CART_URL = BASE_URL + "carts";
    public static final String ADD_TO_CART_URL = BASE_URL + CURRENT_CART + "/entries";
    public static final String DELETE_PRODUCT_URL = BASE_URL + CURRENT_CART + "/entries/%s";
    public static final String UPDATE_QUANTITY_URL = BASE_URL + CURRENT_CART + "/entries/%s";

    public static String PRX_SECTOR_CODE = "B2C";
    public static String PRX_LOCALE = "en_US";
    public static String PRX_CATALOG_CODE = "CONSUMER";
    public static String EXTRA_ANIMATIONTYPE = "EXTRA_ANIMATIONTYPE";
    public static String IS_ONLINE = "IS_ONLINE";
    public static Locale STORE_LOCALE = Locale.US;
    public final static String EMPTY_RESPONSE = "";
}

