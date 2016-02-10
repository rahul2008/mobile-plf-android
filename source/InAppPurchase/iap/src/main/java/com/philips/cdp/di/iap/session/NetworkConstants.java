package com.philips.cdp.di.iap.session;

public final class NetworkConstants {
    public static String getCurrentCartUrl = "https://tst.admin.shop.philips.com/pilcommercewebservices/v2/US_Tuscany/users/inapp@2/carts/current";
    public static String createCartUrl = "https://tst.admin.shop.philips.com/pilcommercewebservices/v2/US_Tuscany/users/inapp@2/carts";
    public static String addToCartUrl = "https://tst.admin.shop.philips.com/pilcommercewebservices/v2/US_Tuscany/users/inapp@2/carts/current/entries";
    public static String deleteProductUrl = "https://tst.admin.shop.philips.com/pilcommercewebservices/v2/US_Tuscany/users/inapp@2/carts/%s/entries/%s";

    public static String updateQuantityUrl = "https://tst.admin.shop.philips" +
            ".com/pilcommercewebservices/v2/US_Tuscany/users/inapp@2/carts/current/entries/%s";

    public static String protocol = "https://";
    public static String hostport = "tst.pl.shop.philips.com/";
    public static String webroot = "pilcommercewebservices/";
    public static String v2 = "v2/";
    public static String appConfig = "US_Tuscany/";

    public static String locale = "en_US";
    public static String propositionId = "Tuscany2016";
    public static String client_id = "mobile_android";
    public static String client_secret = "secret";
}

