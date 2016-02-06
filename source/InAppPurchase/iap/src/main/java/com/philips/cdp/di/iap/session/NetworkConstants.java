package com.philips.cdp.di.iap.session;

public final class NetworkConstants {
    public static String getCurrentCart = "https://tst.admin.shop.philips.com/pilcommercewebservices/v2/US_Tuscany/users/inapp@2/carts/current";
    public static String getCurrentCartUrl = "https://tst.admin.shop.philips.com/pilcommercewebservices/v2/US_Tuscany/users/inapp@2/carts/current";
    public static String createCartUrl = "https://tst.admin.shop.philips.com/pilcommercewebservices/v2/US_Tuscany/users/inapp@2/carts";
    public static String addToCartUrl = "https://tst.admin.shop.philips.com/pilcommercewebservices/v2/US_Tuscany/users/inapp@2/carts/current/entries";
    public static String updateProductCount = "https://tst.admin.shop.philips" +
            ".com/pilcommercewebservices/v2/US_Tuscany/users/inapp@2/carts/current/entries/%s";
    public static String deleteProductEntry = "https://tst.admin.shop.philips.com/pilcommercewebservices/v2/US_Tuscany/users/inapp@2/carts/%s/entries/%s";
}

