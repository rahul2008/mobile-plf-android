/*
 * Copyright (c) Koninklijke Philips N.V. 2016
 * All rights are reserved. Reproduction or dissemination in whole or in part
 * is prohibited without the prior written consent of the copyright holder.
 */

package com.philips.platform.appinfra.apisigning;

/**
 * The ApiSigning Interface for WhiteBox API .
 */

import java.util.Map;

public interface ApiSigningInterface {
    /**
     * Creates an API signer instance according to HSDP specification
     * @param requestMethod Type of method(POST, GET)
     * @param queryString Request URL query.
     * @param headers Request http header fields. Current date is passed as http header value.
     * @param dhpUrl Request URL
     * @param requestbody Request body
     * @since 1.1.0
     */
    String createSignature(String requestMethod, String queryString, Map<String, String> headers, String dhpUrl, String requestbody);
}
