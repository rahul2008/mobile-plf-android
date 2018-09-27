/* Copyright (c) Koninklijke Philips N.V., 2016
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */

package com.philips.platform.ths.providerdetails;

import com.americanwell.sdk.entity.SDKError;
import com.americanwell.sdk.entity.provider.EstimatedVisitCost;

public interface THSFetchEstimatedCostCallback {

    void onEstimatedCostFetchSuccess(EstimatedVisitCost estimatedVisitCost, SDKError sdkError);
    void onError(Throwable throwable);
}
