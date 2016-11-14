/* Copyright (c) Koninklijke Philips N.V., 2016
* All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
*/

package com.philips.platform.appframework.utility;

public class Constants {
    public static final String isEmailMarketingEnabled = "FragmentLifecycle";

    /**
     * Debug Test fragment constants
     * Configuration environment
     */
    public static final String STAGING = "Staging";
    public static final String EVALUATION = "Evaluation";
    public static final String TESTING = "Testing";
    public static final String DEVELOPMENT = "Development";
    public static final String PRODUCTION = "Production";

    public static final String PRODUCT_REGISTRATION_PREFERENCES = "prod_demo";
    public static final String REGISTRATION_ENV_PREFERENCES = "prod_demo";

    /**
     * Home Activity string constants
     */
    public static final String HOME_FRAGMENT_PRESSED = "Home_Fragment_Pressed";
    public static final String HAMBURGER_ICON_TAG = "HamburgerIcon";
    public static final String BACK_BUTTON_TAG = "BackButton";
    public static final String IAP_PHILIPS_SHOP_FRAGMENT_TAG = "ProductCatalogFragment";
    //public static final String IAP_PURCHASE_HISTORY_FRAGMENT_TAG = "PurchaseHistoryFragment";
   // public static final String IAP_SHOPPING_CART_FRAGMENT_TAG = "ShoppingCartFragment";

    /**
     * WelcomeActivity constants
     */
    public static final int BACK_BUTTON_CLICK_CONSTANT = 100000;
    public static final int LOGOUT_BUTTON_CLICK_CONSTANT = 100001;
    public static final int UI_SHOPPING_CART_BUTTON_CLICK = 100002;
    public static final int IAP_PURCHASE_HISTORY = 100003;
    public static final String DONE_PRESSED = "donePressed";
    public static final String PAGE_INDEX = "pageIndex";

    public static final int ADD_HOME_FRAGMENT = 200000;
    public static final int CLEAR_TILL_HOME = 200001;
    public static final int ADD_FROM_HAMBURGER = 200002;
    public static final int ADD_FROM_CHILD_FRAGMENT = 200003;
}
