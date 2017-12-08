/* Copyright (c) Koninklijke Philips N.V., 2016
* All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
*/
package com.philips.cdp.prodreg.listener;

import com.philips.cdp.prodreg.model.metadata.ProductMetadataResponse;

public interface MetadataListener {

    void onMetadataResponse(ProductMetadataResponse productMetadataResponse);

    void onErrorResponse(String errorMessage, int responseCode);
}
