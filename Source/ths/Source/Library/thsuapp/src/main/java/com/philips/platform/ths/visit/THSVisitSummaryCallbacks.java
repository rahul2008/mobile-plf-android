package com.philips.platform.ths.visit;
/* Copyright (c) Koninklijke Philips N.V., 2016
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */


import com.philips.platform.ths.sdkerrors.THSSDKError;

public interface THSVisitSummaryCallbacks {

    interface THSVisitSummaryCallback<THSVisitSummary, THSSDKError>{

        void onResponse(THSVisitSummary tHSVisitSummary, THSSDKError tHSSDKError);

        void onFailure(Throwable throwable);
    }
}
