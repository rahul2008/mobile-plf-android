/*----------------------------------------------------------------------------
Copyright(c) Philips Electronics India Ltd
All rights reserved. Reproduction in whole or in part is prohibited without
the written consent of the copyright holder.

Project           : SaecoAvanti
----------------------------------------------------------------------------*/

package com.philips.cdp.di.iap.utils;

import android.util.Log;

public class IAPLog {
    public final static String LOG = "InAppPurchase";
    public static final String FRAGMENT_LIFECYCLE = "FragmentLifecycle";
    public static final String ANIMATION = "Animation";
    public static final String DEMOAPPACTIVITY = "DemoAppActivity";
    public static final String SHOPPING_BASE_FRAGMENT = "ShoppingBaseFragment";
    public static final String SHOPPINGCART_PRESENTER = "ShoppingCartPresenter";
    public static final String BASE_FRAGMENT_ACTIVITY = "BaseFragmentActivity";
    public static final String SHIPPING_ADDRESS_FRAGMENT = "ShippingAddressFragment";

    private static boolean isLoggingEnabled = true;

    public static String APP_SOURCE_VALUE = "Develop";
    public static String SHOPPING_CART_FRAGMENT = "SHOPPING_CART_FRAGMENT";
    public static String IAPHANDLER = "InAppHandler";
    public static String ORDER_SUMMARY_FRAGMENT = "OrderSummaryFragment";

    public static void enableLogging() {
        isLoggingEnabled = true;
        APP_SOURCE_VALUE = "Develop";
    }

    public static void disableLogging() {
        isLoggingEnabled = false;
        APP_SOURCE_VALUE = "PlayStore";
    }

    public static boolean isLoggingEnabled() {
        return isLoggingEnabled;
    }

    public static void d(String tag, String message) {
        if (isLoggingEnabled) {
            Log.d(tag, message);
        }
    }

    public static void e(String tag, String message) {
        if (isLoggingEnabled) {
            Log.e(tag, message);
        }
    }

    public static void i(String tag, String message) {
        if (isLoggingEnabled) {
            Log.i(tag, message);
        }
    }

    public static void v(String tag, String message) {
        if (isLoggingEnabled) {
            Log.v(tag, message);
        }
    }
}
