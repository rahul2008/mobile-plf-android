/* Copyright (c) Koninklijke Philips N.V., 2016
* All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
*/
package com.philips.cdp.prodreg.listener;

import com.philips.cdp.prodreg.model.metadata.ProductMetadataResponse;

/**
 * It is used to give callback to proposition with product metadata .
 */
public interface MetadataListener {

    /**
     * API used to give back ProductMetadataResponse to proposition
     * @param productMetadataResponse - ProductMetadataResponse productMetadataResponse
     *                                @since 1.0.0
     */
    void onMetadataResponse(ProductMetadataResponse productMetadataResponse);

    /**
     * API used to give back error while fetching metadata to proposition
     * @param errorMessage - String errorMessage
     * @param responseCode - int responseCode
     *                     @since 1.0.0
     */
    void onErrorResponse(String errorMessage, int responseCode);
}
