/* Copyright (c) Koninklijke Philips N.V., 2016
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */

package com.philips.platform.ths.practice;

import com.americanwell.sdk.entity.SDKError;

public interface THSPracticesListCallback {
    //TODO: Review Comment - Spoorti - wrap SDKError to PTHError
    void onPracticesListReceived(THSPracticeList practices, SDKError sdkError);
    void onPracticesListFetchError(Throwable throwable);
}
