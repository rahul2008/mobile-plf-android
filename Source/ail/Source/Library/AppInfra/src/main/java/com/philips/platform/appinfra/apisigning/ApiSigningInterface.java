/*
 * Copyright (c) Koninklijke Philips N.V. 2016
 * All rights are reserved. Reproduction or dissemination in whole or in part
 * is prohibited without the prior written consent of the copyright holder.
 */

package com.philips.platform.appinfra.apisigning;

/**
 * The ApiSigning Interface for WhiteBox API, using HSDP API Signing  .
 */

import java.io.Serializable;
import java.util.Map;

public interface ApiSigningInterface extends Serializable {
    /**
     * Creates an API signature instance according to HSDP specification
     * @param requestMethod Type of method(POST, GET)
     * @param queryString Request URL query.
     * @param headers Request http header fields. Current date is passed as http header value.
     * @param dhpUrl Request URL
     * @param requestbody Request body
     * @return returns API signature key
     * @since 1.1.0
     */
    String createSignature(String requestMethod, String queryString, Map<String, String> headers, String dhpUrl, String requestbody);
}
