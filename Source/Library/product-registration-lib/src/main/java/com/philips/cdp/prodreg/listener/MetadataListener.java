package com.philips.cdp.prodreg.listener;

import com.philips.cdp.prodreg.model.metadata.ProductMetadataResponse;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public interface MetadataListener {

    void onMetadataResponse(ProductMetadataResponse productMetadataResponse);

    void onErrorResponse(String errorMessage, int responseCode);
}
