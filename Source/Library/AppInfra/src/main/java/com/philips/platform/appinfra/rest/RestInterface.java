/* Copyright (c) Koninklijke Philips N.V. 2016
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */
package com.philips.platform.appinfra.rest;

import com.philips.platform.appinfra.rest.request.RequestQueue;

/**
 * The Interface for Request and NetWorkType
 */
public interface RestInterface {

    /**
     * Gets request queue.
     *
     * @return the REST request queue
     */
    RequestQueue getRequestQueue();


    enum NetworkTypes {WIFI,MOBILE_DATA,NO_NETWORK}

    /**
     * Get the network  connection and sets the wifiConnected and mobileConnected
     * @return enum WIFI,MOBILE_DATA,NO_NETWORK
     * @since 1.0.0
 */
    NetworkTypes  getNetworkReachabilityStatus();

    /**
     * Method to check the network connectivity availability .
     * @return boolean true/false.
     * @since 1.1.0
     */
    boolean isInternetReachable() ;

}
