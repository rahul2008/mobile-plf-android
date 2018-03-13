package com.philips.platform.ths.cost;
/* Copyright (c) Koninklijke Philips N.V., 2016
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */

import android.support.annotation.NonNull;

import java.util.Map;

public interface CreateVisitCallback<THSVisit, THSSDKError>  {

    void onCreateVisitResponse(THSVisit tHSVisit, THSSDKError tHSSDKError);

    void onCreateVisitFailure(Throwable var1);

    void onCreateVisitValidationFailure(@NonNull Map<String, String> map);
}
