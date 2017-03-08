/*
 *  Copyright (c) Koninklijke Philips N.V., 2016
 *  All rights are reserved. Reproduction or dissemination
 *  * in whole or in part is prohibited without the prior written
 *  * consent of the copyright holder.
 * /
 */

package com.philips.cdp.registration.configuration;

public class HSDPInfo {


    private String sharedId;
    private String secretId;
    private String baseURL;
    private String applicationName;

    public HSDPInfo(String sharedId, String secretId, String baseURL, String applicationName) {
        this.sharedId = sharedId;
        this.secretId = secretId;
        this.baseURL = baseURL;
        this.applicationName = applicationName;
    }

    /**
     * Get Shared Id
     *
     * @return String shared id
     */
    public String getSharedId() {
        return sharedId;
    }

    /**
     * Get secrete id
     *
     * @return String
     */
    public String getSecreteId() {
        return secretId;
    }

    /**
     * Get base URL
     *
     * @return String
     */
    public String getBaseURL() {
        return baseURL;
    }

    /**
     * Get Application name
     *
     * @return String Application
     */
    public String getApplicationName() {
        return applicationName;
    }
}



