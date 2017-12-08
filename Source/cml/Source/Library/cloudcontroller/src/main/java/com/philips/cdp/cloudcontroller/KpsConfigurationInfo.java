
/*
 * Copyright (c) 2015-2017 Koninklijke Philips N.V.
 * All rights reserved.
 */

package com.philips.cdp.cloudcontroller;

/**
 * Configuration object for signing in to Philips DeviceCloud.
 * Values to return need to be agreed with DeviceCloud operations/development team.
 * Invalid values will lead to sign-in failures.
 * @publicApi
 */
public abstract class KpsConfigurationInfo {

    private static final int ICPCLIENT_PRIORITY = 20;
    private static final int ICPCLIENT_STACKSIZE = 131072;
    private static final int HTTP_TIMEOUT = 30;
    private static final String FILTERSTRING_NOFILTER = "";
    private static final int MAX_NUMBER_RETRIES = 2;

    /**
     * Returns the priority for ICPClient
     * @return int The priority.
     */
    public int getICPClientPriority() {
        return ICPCLIENT_PRIORITY;
    }

    /**
     * Returns the stack size of ICPClient
     * @return int The size of the stack
     */
    public int getICPClientStackSize() {
        return ICPCLIENT_STACKSIZE;
    }

    /**
     * Returns a timeout
     * @return int The timeout
     */
    public int getHttpTimeout() {
        return HTTP_TIMEOUT;
    }

    /**
     * Returns a String to be used in filtering
     * @return String The filter String
     */
    public String getFilterString() {
        return FILTERSTRING_NOFILTER;
    }

    /**
     * Returns the maximum number of retries
     * @return int The maximum number of retries
     */
    public int getMaxNumberOfRetries() {
        return MAX_NUMBER_RETRIES;
    }

    /**
     * Returns Bootstrap ID for the mobile app to use when communicating
     * @return String
     */
    public abstract String getBootStrapId();

    /**
     * Returns a key to help sign in to DeviceCloud
     * @return String
     */
    public abstract String getBootStrapKey();

    /**
     * Returns the ID of the product as configured in DeviceCloud
     * @return String
     */
    public abstract String getProductId();

    /**
     * Returns the version of the product
     * @return String
     */
    public abstract int getProductVersion();

    /**
     * Returns the name of this component in the system/product.
     * @return String
     */
    public abstract String getComponentId();

    /**
     * Returns the total component count in the product
     * @return int
     */
    public abstract int getComponentCount();

    /**
     * Returns the package name of the app signing in to DeviceCloud.
     * @return String
     */
    public abstract String getAppId();

    /**
     * Returns the version of the app signing in to DeviceCloud.
     * @return int
     */
    public abstract int getAppVersion();

    /**
     * Returns the type of app signing in to DeviceCloud.
     * @return String
     */
    public abstract String getAppType();

    /**
     * Returns the country code for the user signing in.
     * @return String
     */
    public abstract String getCountryCode();

    /**
     * Returns the language code for the user signing in.
     * @return String
     */
    public abstract String getLanguageCode();

    /**
     * Returns the gateway URL to the DeviceCloud.
     * @return String
     */
    public abstract String getDevicePortUrl();

}
