/* Copyright (c) Koninklijke Philips N.V., 2016
* All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
*/
package com.philips.cdp.prodreg.listener;

import com.philips.cdp.prodreg.model.summary.ProductSummaryResponse;

public interface SummaryListener {

    void onSummaryResponse(ProductSummaryResponse productSummaryResponse);

    void onErrorResponse(String errorMessage, int responseCode);
}
