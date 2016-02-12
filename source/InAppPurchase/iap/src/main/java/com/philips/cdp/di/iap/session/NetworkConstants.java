package com.philips.cdp.di.iap.session;

public class NetworkConstants {

    public final static String HOST_URL = "https://tst.admin.shop.philips.com/";
    public final static String WEB_ROOT = "pilcommercewebservices/";
    public final static String V2 = "v2/";
    public final static String APP_CONFIG = "US_Tuscany/";
    public final static String USER = "users/inapp@3/";
    public final static String BASE_URL = HOST_URL + WEB_ROOT + V2 + APP_CONFIG + USER;
    public final static String CURRENT_CART = "carts/current";

   /* public final static String LOCALE = "en_US";
    public final static String PROPOSITION_ID = "Tuscany2016";
    public final static String CLIENT_ID = "mobile_android";
    public final static String CLIENT_SECRET = "secret";*/

    public final static String GET_CURRENT_CART_URL = BASE_URL + "carts";
    public final static String CREATE_CART_URL = BASE_URL + "carts";
    public final static String ADD_TO_CART_URL = BASE_URL + CURRENT_CART + "/entries";
    public final static String DELETE_PRODUCT_URL = BASE_URL + CURRENT_CART + "/entries/%s";
    public final static String UPDATE_QUANTITY_URL = BASE_URL + CURRENT_CART + "/entries/%s";

    public final static String PRX_SECTOR_CODE = "B2C";
    public final static String PRX_LOCALE = "en_US";
    public final static String PRX_CATALOG_CODE = "CONSUMER";

    public final static String EMPTY_RESPONSE = "";
}

