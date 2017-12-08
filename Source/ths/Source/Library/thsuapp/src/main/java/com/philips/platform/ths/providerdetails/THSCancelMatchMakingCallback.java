package com.philips.platform.ths.providerdetails;
/* Copyright (c) Koninklijke Philips N.V., 2016
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */

public interface THSCancelMatchMakingCallback<Void, THSSDKError> {
    void onCancelMatchMakingResponse(Void aVoid, THSSDKError thssdkError);
    void onCancelMatchMakingFailure(Throwable throwable);
}