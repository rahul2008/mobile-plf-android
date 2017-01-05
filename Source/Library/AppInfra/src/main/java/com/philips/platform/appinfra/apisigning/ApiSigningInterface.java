/*
 * Copyright (c) Koninklijke Philips N.V. 2016
 * All rights are reserved. Reproduction or dissemination in whole or in part
 * is prohibited without the prior written consent of the copyright holder.
 */

package com.philips.platform.appinfra.apisigning;

/*
 * Created by 310209604 on 2016-10-26.
 */

import java.net.URL;
import java.util.Map;

public interface ApiSigningInterface {
    /**
     * Calculates a signature string for the given REST request
     * @param requestMethod
     * @param queryString
     * @param headers
     * @param dhpUrl
     * @param requestbody
     */
    String createSignature(String requestMethod, String queryString, Map<String, String> headers, String dhpUrl, String requestbody);
}
