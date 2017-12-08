/*
 *  Copyright (c) Koninklijke Philips N.V., 2016
 *  All rights are reserved. Reproduction or dissemination
 *  * in whole or in part is prohibited without the prior written
 *  * consent of the copyright holder.
 * /
 */

package com.philips.dhpclient;

public class DhpApiClientConfiguration {
    private final String apiBaseUrl;
    private final String dhpApplicationName;
    private final String signingKey;
    private final String signingSecret;

    public DhpApiClientConfiguration(String apiBaseUrl, String dhpApplicationName, String signingKey, String signingSecret) {
        this.apiBaseUrl = apiBaseUrl;
        this.dhpApplicationName = dhpApplicationName;
        this.signingKey = signingKey;
        this.signingSecret = signingSecret;
    }

    public String getApiBaseUrl() {
        return apiBaseUrl;
    }

    public String getDhpApplicationName() {
        return dhpApplicationName;
    }

    public String getSigningKey() {
        return signingKey;
    }

    public String getSigningSecret() {
        return signingSecret;
    }
}
