/* Copyright (c) Koninklijke Philips N.V. 2016
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */
package com.philips.platform.appinfra.rest;

import com.philips.platform.appinfra.rest.request.RequestQueue;


public interface RestInterface {

    /**
     * Gets request queue.
     *
     * @return the REST request queue
     */
    public RequestQueue getRequestQueue();


/**
 * Get the network  connection and sets the wifiConnected and mobileConnected
 * @return String Wifi/Mobile
 */
    public String getNetworkReachabilityStatus();

    /**
     * Method to check the network connectivity availability .
     * @return boolean true/false.
     */
    public boolean isInternetReachable() ;

}
