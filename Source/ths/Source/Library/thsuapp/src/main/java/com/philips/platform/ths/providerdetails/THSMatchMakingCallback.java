package com.philips.platform.ths.providerdetails;
/* Copyright (c) Koninklijke Philips N.V., 2016
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */

import com.americanwell.sdk.entity.SDKError;
import com.americanwell.sdk.entity.provider.Provider;

public interface THSMatchMakingCallback {

    public void onMatchMakingProviderFound(Provider provider);
    public void onMatchMakingProviderListExhausted();
    public void onMatchMakingRequestGone();
    public void onMatchMakingResponse(Void aVoid, SDKError sdkError);
    public void onMatchMakingFailure(Throwable throwable);
}
