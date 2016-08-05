/* Copyright (c) Koninklijke Philips N.V., 2016
* All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
*/
package com.philips.cdp.prodreg.tagging;

/**
 * AnalyticsConstants is holding constant members used for TAGGING, Adobe
 * Analytics.
 * <p/>
 * = author: ritesh.jha= philips.com = since: Mar 26, 2015
 */
public class AnalyticsConstants {

    /* PAGE CONSTANTS */
    /* ACTION KEY CONSTANTS */
    /*****************
     * Page Context Data start
     **************/
    public static final String KEY_APP_NAME = "app.name";
    public static final String KEY_VERSION = "app.version";
    public static final String KEY_OS = "app.os";
    public static final String KEY_LANGUAGE = "locale.language";
    public static final String KEY_CURRENCY = "locale.currency";
    public static final String KEY_COUNTRY = "locale.country";
    public static final String KEY_TIME_STAMP = "timestamp";
    public static final String KEY_APP_ID = "appsId";
    public static final String APP_SOURCE = "appSource";
    public static final String APP_SOURCE_VALUE = "PlayStore";

    /***************** Page Context Data End **************/

    /*****************
     * Action Names
     **************/

    /*****************
     * Action Keys
     **************/

    /*****************
     * Action Values
     **************/
    public static final String ACTION_VALUE_APP_NAME = "Product Registration";
    public static final String ACTION_VALUE_ANDROID = "Android ";
    public static String Product_REGISTRATION_START_COUNT = "prod_reg_start_count";
    public static String Product_REGISTRATION_COMPLETED_COUNT = "prod_reg_complete_count";
    public static String Product_REGISTRATION_DATE_COUNT = "prod_reg_date_count";
    public static String Product_REGISTRATION_SCAN_COUNT = "prod_reg_scan_count";
    public static String Product_REGISTRATION_EXTENDED_WARRANTY_COUNT = "prod_reg_ext_warranty_count";
}
