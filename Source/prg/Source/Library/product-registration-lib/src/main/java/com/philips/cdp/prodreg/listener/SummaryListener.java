/* Copyright (c) Koninklijke Philips N.V., 2016
* All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
*/
package com.philips.cdp.prodreg.listener;

import com.philips.cdp.prodreg.model.summary.ProductSummaryResponse;

/**
 * It is used to handle product summary response
 * @since 1.0.0
 */
public interface SummaryListener {

    /**
     * API to give a callback to proposition with ProductSummaryResponse
     * @param productSummaryResponse - get instance of ProductSummaryResponse
     * @since 1.0.0
     */
    void onSummaryResponse(ProductSummaryResponse productSummaryResponse);

    /**
     * API to give a callback to proposition with ProductSummaryResponse Error
     * @param errorMessage - get error message
     * @param responseCode - get error response code
     * @since 1.0.0
     */
    void onErrorResponse(String errorMessage, int responseCode);
}
