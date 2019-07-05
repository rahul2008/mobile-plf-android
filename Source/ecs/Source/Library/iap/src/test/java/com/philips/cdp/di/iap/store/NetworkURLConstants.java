/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
package com.philips.cdp.di.iap.store;

public class NetworkURLConstants {
    public static final String HOST_PORT = "acc.occ.shop.philips.com";
    public static final String SITE = "US_TUSCANY";
    public static final String LOCALE = "en_US";
    public static final String JANRAIN_EMAIL = "a@b.com";
    public static final String JANRAIN_ID = "sometoken";

    public static final String DUMMY_PRODUCT_ID = "H1212";
    public static final String DUMMY_ORDER_ID = "H1212";
    public static final String DUMMY_PAGE_NUMBER = "0";
    public static final String DUMMY_QUANTITY = "2";
    public static final String DUMMY_PRODUCT_NUMBER = "1212";
    public static final String ADDRESS_ID = "8799470125079";

    //OAuth
    public static final String OAUTH_URL = String.format("https://" + HOST_PORT +
            "/pilcommercewebservices/oauth/token");
    public static final String OAUTH_REFRESH_URL = "https://" + HOST_PORT +
            "/pilcommercewebservices/oauth/token";
    public static final String SAMPLE_PRODUCT_CATEGORY = "ENERGYLIGHT_SU";

    public static final String BASE_URL = "https://" + HOST_PORT + "/pilcommercewebservices/" + "v2/" + SITE +
            "/users/" +"/current";


    public static final String BASE_URL_CART = "https://" + HOST_PORT + "/pilcommercewebservices/" + "v2/" + SITE +
            "/users" ;

    //Carts
    public static final String GET_CARTS_URL = BASE_URL + "/carts/current?fields=FULL&lang=en_US";
    public static final String DELETE_CART_URL = BASE_URL + "/carts/current?lang=en_US";
    public static final String CREATE_CART_URL = BASE_URL_CART + "/current/carts?fields=FULL&lang=en_US";
    public static final String ADD_TO_CART_URL = BASE_URL + "/carts/current/entries?fields=FULL&lang=en_US";

    //Product
    public static final String PRODUCT_CATALOG_URL = "https://acc.occ.shop.philips" +
            ".com/pilcommercewebservices/v2/US_TUSCANY/products/search?query=::category:Tuscany_Campaign&lang=en_US&currentPage=0&pageSize=1";
    public static final String PRODUCT_CATALOG_BASE_URL = "https://" + HOST_PORT + "/pilcommercewebservices/" + "v2/" + SITE;
    public static final String CART_MODIFY_PRODUCT_URL = BASE_URL + "/carts/current/entries/" + DUMMY_PRODUCT_NUMBER + "?fields=FULL&lang=en_US";
    public static final String PRODUCT_DETAIL_URL = PRODUCT_CATALOG_BASE_URL + "/products/" + DUMMY_PRODUCT_ID;
    public static final String GET_RETAILERS_URL = "https://www.philips.com/api/wtb/v1/B2C/en_US/online-retailers?product=code&lang=en";

    //Address
    public static final String GET_REGIONS_URL = "https://" + HOST_PORT + "/pilcommercewebservices/" + "v2/metainfo/regions/US?fields=FULL&lang=en_US";
    public static final String GET_ADDRESSES_URL = BASE_URL + "/addresses?fields=FULL&lang=en_US";
    public static final String EDIT_ADDRESS_URL = BASE_URL + "/addresses/" + DUMMY_PRODUCT_ID + "?fields=FULL&lang=en_US";
    public static final String GET_UPDATE_ADDRESS_URL = BASE_URL + "/addresses/" + ADDRESS_ID + "?fields=FULL&lang=en_US";
    public static final String SET_DELIVERY_ADDRESS_URL = BASE_URL + "/carts/current/addresses/delivery?fields=FULL&lang=en_US";

    //Delivery mode
    public static final String SET_DELIVERY_MODE_URL = BASE_URL + "/carts/current/deliverymode?fields=FULL&lang=en_US";
    public static final String GET_DELIVERY_MODES_URL = BASE_URL + "/carts/current/deliverymodes?lang=en_US";


    //Payment
    public static final String SET_PAYMENT_DETAIL_URL = BASE_URL + "/carts/current/paymentdetails?fields=FULL&lang=en_US";
    public static final String GET_PAYMENT_DETAILS_URL = BASE_URL + "/paymentdetails?fields=FULL&lang=en_US";
    public static final String MAKE_PAYMENT_URL = BASE_URL + "/orders/" + DUMMY_PRODUCT_ID + "/pay?fields=FULL&lang=en_US";
    public static final String PLACE_ORDER_URL = BASE_URL + "/orders?fields=FULL&lang=en_US";

    //Orders
    public static final String ORDER_DETAIL_URL = BASE_URL + "/orders/" + DUMMY_ORDER_ID + "?fields=FULL&lang=en_US";
    public static final String ORDER_HISTORY_URL = BASE_URL + "/orders?fields=FULL&lang=en_US&currentPage=" + DUMMY_PAGE_NUMBER;
    public static final String PHONE_CONTACT_URL = "https://www.philips.com/prx/cdls/B2C/en_US/CARE/" + SAMPLE_PRODUCT_CATEGORY + ".querytype.(fallback)";

    //Vouchers
    public static final String APPLY_URL = BASE_URL + "/carts/current/vouchers?lang=en_US";

}
