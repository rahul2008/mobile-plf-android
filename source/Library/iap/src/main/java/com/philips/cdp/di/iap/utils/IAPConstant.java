package com.philips.cdp.di.iap.utils;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public class IAPConstant {
    public static final int IAP_SUCCESS = 0;
    public static final int IAP_ERROR = -1;
    public static final int IAP_ERROR_NO_CONNECTION = 2;
    public static final int IAP_ERROR_CONNECTION_TIME_OUT = 3;
    public static final int IAP_ERROR_AUTHENTICATION_FAILURE = 4;
    public static final int IAP_ERROR_SERVER_ERROR = 5;
    public static final int IAP_ERROR_INSUFFICIENT_STOCK_ERROR = 6;
    public static final int IAP_ERROR_UNKNOWN = 7;
    public static final String IAP_KEY_ACTIVITY_THEME = "IAP_KEY_ACTIVITY_THEME";
    public static final Boolean BUTTON_STATE_CHANGED = false;
    public static final String EMPTY_CART_FRAGMENT_REPLACED = "EMPTY_CART_FRAGMENT_REPLACED";
    public static final String SHOPPING_CART_PRESENTER = "SHOPPING_CART_PRESENTER";
    public static final String SHIPPING_ADDRESS_FRAGMENT = "SHIPPING_ADDRESS_FRAGMENT";
    public static final String PRODUCT_DETAIL_FRAGMENT = "PRODUCT_DETAIL_FRAGMENT ";
    public static final String PRODUCT_TITLE = "PRODUCT_TITLE";
    public static final String PRODUCT_CTN = "PRODUCT_CTN";
    public static final String PRODUCT_PRICE = "PRODUCT_PRICE";
    public static final String PRODUCT_VALUE_PRICE = "PRODUCT_VALUE_PRICE";
    public static final String PRODUCT_OVERVIEW = "PRODUCT_OVERVIEW";
    public static final String UPDATE_SHIPPING_ADDRESS_KEY = "UPDATE_SHIPPING_ADDRESS_KEY";
    public static final String MODEL_ALERT_BUTTON_TEXT = "MODEL_ALERT_BUTTON_TEXT";
    public static final String MODEL_ALERT_ERROR_TEXT = "MODEL_ALERT_ERROR_TEXT";
    public static final String MODEL_ALERT_ERROR_DESCRIPTION = "MODEL_ALERT_ERROR_DESCRIPTION";
    public static final String MODEL_ALERT_TRYAGAIN_BUTTON_VISIBLE = "MODEL_ALERT_TRYAGAIN_BUTTON_VISIBLE";
    public static final String IS_SECOND_USER = "IS_SECOND_USER";
    public static final String PAYMENT_METHOD_LIST = "PAYMENT_METHOD_LIST";
    public static final String ADD_DELIVERY_ADDRESS = "ADD_DELIVERY_ADDRESS";
    public static final String FROM_PAYMENT_SELECTION = "FROM_PAYMENT_SELECTION";
    public static final String BILLING_ADDRESS_FIELDS = "BILLING_ADDRESS_FIELDS";
    public static final String USE_PAYMENT = "USE_PAYMENT";
    public static final String ADD_NEW_PAYMENT = "ADD_NEW_PAYMENT";
    public static final String SELECTED_PAYMENT = "SELECTED_PAYMENT";
    public static final String PRODUCT_DETAIL_FRAGMENT_IMAGE_URL = "PRODUCT_DETAIL_FRAGMENT_IMAGE_URL";
    public final static String NEW_LINE_ESCAPE_CHARACTER = "\n";
    public static final String INSUFFICIENT_STOCK_LEVEL_ERROR = "InsufficientStockLevelError";
    public static final String IAP_IS_SHOPPING_CART_VIEW_SELECTED = "IAP_IS_SHOPPING_CART_VIEW_SELECTED";
    public static final String IS_PRODUCT_CATALOG = "IS_PRODUCT_CATALOG";
    public static final String PRODUCT_DETAIL_FRAGMENT_CATALOG = "PRODUCT_DETAIL_FRAGMENT_CATALOG";
    public static final String IAP_LAUNCH_PRODUCT_CATALOG = "IAP_LAUNCH_PRODUCT_CATALOG";
    public static final String IAP_PRODUCT_DISCOUNTED_PRICE = "IAP_PRODUCT_DISCOUNTED_PRICE";
    public static final String IAP_FRAGMENT_STATE_KEY = "IAP_FRAGMENT_STATE_KEY";
    public static final String IAP_LAUNCH_PRODUCT_CATALOG_ON_ERROR = "IAP_LAUNCH_PRODUCT_CATALOG_ON_ERROR";
    public static final String IAP_BUY_URL = "IAP_BUY_URL";
    public static final String IAP_LAUNCH_PRODUCT_CATALOG_FROM_EMPTY_CART = "IAP_LAUNCH_PRODUCT_CATALOG_FROM_EMPTY_CART";
    public static final java.lang.String MODEL_ALERT_CONFIRM_DESCRIPTION = "MODEL_ALERT_CONFIRM_DESCRIPTION";
    public static final String SWITCH_TO_NO_NETWORK_CONNECTION = "SWITCH_TO_NO_NETWORK_CONNECTION";

    public interface IAPLandingViews {
        int IAP_PRODUCT_CATALOG_VIEW = 0;
        int IAP_SHOPPING_CART_VIEW = 1;
    }

    public static final String IAP_STORE_NAME = "IAP_STORE_NAME";
}