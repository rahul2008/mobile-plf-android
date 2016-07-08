/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
package com.philips.cdp.di.iap.store;

public class NetworkURLConstants {
    public static final String LCOALE = "en_US";
    public static final String DUMMY_PRODUCT_ID = "H1212";
    public static final String DUMMY_ORDER_ID = "H1212";
    public static final String DUMMY_PAGE_NUMBER = "0";
    public static final String DUMMY_QUANTITY = "2";
    public static final String DUMMY_PRODUCT_NUBMBER = "1212";
    public static final String HOST_PORT = "acc.occ.shop.philips.com";
    public static final String SITE = "US_TUSCANY";
    public static final String JANRAIN_EMAIL = "a@b.com";
    public static final String JANRAIN_ID = "sometoken";
    public static final String ADDRESS_ID = "8799470125079";
    public static final String OAUTH_URL = String.format("https://" + HOST_PORT +
            "/pilcommercewebservices/oauth/token?janrain=%s&grant_type=janrain&client_id" +
            "=mobile_android&client_secret=secret", JANRAIN_ID);
    public static final String OAUTH_REFRESH_URL = "https://" + HOST_PORT +
            "/pilcommercewebservices/oauth/token";

    public static final String BASE_URL = "https://" + HOST_PORT + "/pilcommercewebservices/" + "v2/" + SITE +
            "/users/" + JANRAIN_EMAIL;
    public static final String PRODUCT_CATALOG_BASE_URL = "https://" + HOST_PORT + "/pilcommercewebservices/" + "v2/" + SITE;
    public static final String CART_DETAIL_URL = BASE_URL + "/carts/current?fields=FULL&lang=en";
    public static final String CART_CREATE_URL = BASE_URL + "/carts";
    public static final String CART_ADD_TO_URL = BASE_URL + "/carts/current/entries";
    public static final String CART_MODIFY_PRODUCT_URL = CART_ADD_TO_URL + "/" + DUMMY_PRODUCT_NUBMBER;
    public static final String CART_PAYMENT_DETAILS_URL = BASE_URL + "/paymentdetails";
    public static final String ADDRESS_DETAILS_URL = BASE_URL + "/addresses?fields=FULL&lang=en";
    public static final String ADDRESS_ALTER_URL = BASE_URL + "/addresses/" + DUMMY_PRODUCT_ID;
    public static final String UPDATE_DELIVERY_MODE_URL = BASE_URL + "/carts/current/deliverymode";
    public static final String GET_DELIVERY_MODEs_URL = BASE_URL + "/carts/current/deliverymodes?fields=FULL&lang=en";
    public static final String UPDATE_DELIVERY_ADDRESS_URL = BASE_URL + "/carts/current/addresses/delivery";
    public static final String PAYMENT_SET_URL = BASE_URL + "/orders/" + DUMMY_PRODUCT_ID + "/pay";
    public static final String PLACE_ORDER_URL = BASE_URL + "/orders";
    public static final String PAYMENT_DETAILS_URL = BASE_URL + "/carts/current/paymentdetails";
    public static final String PRODUCT_CATALOG_URL = "https://acc.occ.shop.philips" +
            ".com/pilcommercewebservices/v2/US_TUSCANY/products/search?query=::category:Tuscany_Campaign&lang=en&currentPage=0&pageSize=1";
    public static final String REGION_URL = "https://" + HOST_PORT + "/pilcommercewebservices/" + "v2/metainfo/regions/US?fields=FULL&lang=en";
    public static final String RETAILORS_URL = "https://www.philips.com/api/wtb/v1/B2C/en_US/online-retailers?product=code&lang=en";
    public static final String ORDER_DETAIL_URL = BASE_URL + "/orders/" + DUMMY_ORDER_ID + "?fields=FULL&lang=en";
    public static final String ORDER_HISTORY_URL = BASE_URL + "/orders?fields=FULL&lang=en&currentPage=" + DUMMY_PAGE_NUMBER;
    public static final String PRODUCT_DETAIL_URL = PRODUCT_CATALOG_BASE_URL + "/products/" + DUMMY_PRODUCT_ID;
    public static final String GET_UPDATE_ADDRESS_URL = BASE_URL + "/addresses/" + ADDRESS_ID;
}
