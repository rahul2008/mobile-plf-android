/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
package com.philips.cdp.di.iap.utils;

import junit.framework.TestCase;

import org.junit.Test;

public class IAPConstantTest extends TestCase {

    @Test
    public void testConstant() {
        new IAPConstant();
        assertEquals("Value is Same", "EMPTY_CART_FRAGMENT_REPLACED", IAPConstant.EMPTY_CART_FRAGMENT_REPLACED);
        assertFalse(IAPConstant.BUTTON_STATE_CHANGED);
        assertEquals("Value is Same", 0, IAPConstant.IAP_SUCCESS);
        assertEquals("Value is Same", -1, IAPConstant.IAP_ERROR);
        assertEquals("Value is Same", 2, IAPConstant.IAP_ERROR_NO_CONNECTION);
        assertEquals("Value is Same", 3, IAPConstant.IAP_ERROR_CONNECTION_TIME_OUT);
        assertEquals("Value is Same", 4, IAPConstant.IAP_ERROR_AUTHENTICATION_FAILURE);
        assertEquals("Value is Same", 5, IAPConstant.IAP_ERROR_SERVER_ERROR);
        assertEquals("Value is Same", 6, IAPConstant.IAP_ERROR_INSUFFICIENT_STOCK_ERROR);
        assertEquals("Value is Same", 7, IAPConstant.IAP_ERROR_UNKNOWN);
        assertEquals("Value is Same", "IAP_KEY_ACTIVITY_THEME", IAPConstant.IAP_KEY_ACTIVITY_THEME);
        assertEquals("Value is Same", "SHOPPING_CART_PRESENTER", IAPConstant.SHOPPING_CART_PRESENTER);
        assertEquals("Value is Same", "ADD_NEW_ADDRESS", IAPConstant.ADD_NEW_ADDRESS);
        assertEquals("Value is Same", "PRODUCT_DETAIL_FRAGMENT ", IAPConstant.PRODUCT_DETAIL_FRAGMENT);
        assertEquals("Value is Same", "PRODUCT_TITLE", IAPConstant.PRODUCT_TITLE);
        assertEquals("Value is Same", "PRODUCT_CTN", IAPConstant.PRODUCT_CTN );
        assertEquals("Value is Same", "PRODUCT_PRICE", IAPConstant.PRODUCT_PRICE );
        assertEquals("Value is Same", "PRODUCT_VALUE_PRICE", IAPConstant.PRODUCT_VALUE_PRICE );
        assertEquals("Value is Same", "PRODUCT_OVERVIEW", IAPConstant.PRODUCT_OVERVIEW);
        assertEquals("Value is Same",  "UPDATE_SHIPPING_ADDRESS_KEY", IAPConstant.UPDATE_SHIPPING_ADDRESS_KEY );
        assertEquals("Value is Same",  "SINGLE_BUTTON_DIALOG_TEXT", IAPConstant.SINGLE_BUTTON_DIALOG_TEXT);
        assertEquals("Value is Same",  "SINGLE_BUTTON_DIALOG_TITLE", IAPConstant.SINGLE_BUTTON_DIALOG_TITLE);
        assertEquals("Value is Same", "SINGLE_BUTTON_DIALOG_DESCRIPTION", IAPConstant.SINGLE_BUTTON_DIALOG_DESCRIPTION);
        assertEquals("Value is Same",  "IS_SECOND_USER", IAPConstant.IS_SECOND_USER );
        assertEquals("Value is Same",  "PAYMENT_METHOD_LIST", IAPConstant.PAYMENT_METHOD_LIST );
        assertEquals("Value is Same",  "DELIVER_TO_THIS_ADDRESS", IAPConstant.DELIVER_TO_THIS_ADDRESS);
        assertEquals("Value is Same",  "ORDER_TRACK_URL", IAPConstant.ORDER_TRACK_URL );
        assertEquals("Value is Same",  "FROM_PAYMENT_SELECTION", IAPConstant.FROM_PAYMENT_SELECTION );
        assertEquals("Value is Same",  "BILLING_ADDRESS_FIELDS", IAPConstant.BILLING_ADDRESS_FIELDS );
        assertEquals("Value is Same",  "USE_PAYMENT", IAPConstant.USE_PAYMENT );
        assertEquals("Value is Same",  "ADD_NEW_PAYMENT", IAPConstant.ADD_NEW_PAYMENT );
        assertEquals("Value is Same",  "SELECTED_PAYMENT", IAPConstant.SELECTED_PAYMENT );
        assertEquals("Value is Same",  "PRODUCT_DETAIL_FRAGMENT_IMAGE_URL", IAPConstant.PRODUCT_DETAIL_FRAGMENT_IMAGE_URL );
        assertEquals("Value is Same",  "InsufficientStockLevelError", IAPConstant.INSUFFICIENT_STOCK_LEVEL_ERROR );
        assertEquals("Value is Same",  "IAP_LANDING_SCREEN", IAPConstant.IAP_LANDING_SCREEN);
        assertEquals("Value is Same",  "IAP_PRODUCT_CATALOG_NUMBER_FROM_VERTICAL", IAPConstant.IAP_PRODUCT_CATALOG_NUMBER_FROM_VERTICAL);
        assertEquals("Value is Same",  "IS_PRODUCT_CATALOG", IAPConstant.IS_PRODUCT_CATALOG );
        assertEquals("Value is Same",  "IAP_LAUNCH_PRODUCT_DETAIL", IAPConstant.IAP_LAUNCH_PRODUCT_DETAIL);
        assertEquals("Value is Same",  "IAP_LAUNCH_PRODUCT_CATALOG", IAPConstant.IAP_LAUNCH_PRODUCT_CATALOG );
        assertEquals("Value is Same",  "IAP_PRODUCT_DISCOUNTED_PRICE", IAPConstant.IAP_PRODUCT_DISCOUNTED_PRICE );
        assertEquals("Value is Same",  "IAP_FRAGMENT_STATE_KEY", IAPConstant.IAP_FRAGMENT_STATE_KEY );
        assertEquals("Value is Same",  "IAP_LAUNCH_PRODUCT_CATALOG_ON_ERROR", IAPConstant.IAP_LAUNCH_PRODUCT_CATALOG_ON_ERROR );
        assertEquals("Value is Same",  "IAP_BUY_URL", IAPConstant.IAP_BUY_URL );
        assertEquals("Value is Same",  "SWITCH_TO_NO_NETWORK_CONNECTION", IAPConstant.SWITCH_TO_NO_NETWORK_CONNECTION );
        assertEquals("Value is Same",  "PURCHASE_HISTORY_DETAIL", IAPConstant.PURCHASE_HISTORY_DETAIL );
        assertEquals("Value is Same",  "PURCHASE_ID", IAPConstant.PURCHASE_ID );
        assertEquals("Value is Same",  "ORDER_STATUS", IAPConstant.ORDER_STATUS );
        assertEquals("Value is Same",  "TRACKING_ID", IAPConstant.TRACKING_ID );
        assertEquals("Value is Same",  "DELIVERY_NAME", IAPConstant.DELIVERY_NAME);
        assertEquals("Value is Same",  "completed", IAPConstant.ORDER_COMPLETED );
        assertEquals("Value is Same",  "processing", IAPConstant.ORDER_PROCESSING );
        assertEquals("Value is Same",  "ORDER_DETAIL", IAPConstant.ORDER_DETAIL );
        assertEquals("Value is Same",  "IAP_STORE_NAME", IAPConstant.IAP_STORE_NAME );
        assertEquals("Value is Same",  "IAP_RETAILER_INFO", IAPConstant.IAP_RETAILER_INFO );
    }
}