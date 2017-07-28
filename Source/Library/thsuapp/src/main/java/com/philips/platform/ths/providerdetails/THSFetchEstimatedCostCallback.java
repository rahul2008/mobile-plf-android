package com.philips.platform.ths.providerdetails;

import com.americanwell.sdk.entity.SDKError;
import com.americanwell.sdk.entity.provider.EstimatedVisitCost;

public interface THSFetchEstimatedCostCallback {

    void onEstimatedCostFetchSuccess(EstimatedVisitCost estimatedVisitCost, SDKError sdkError);
    void onError(Throwable throwable);
}
