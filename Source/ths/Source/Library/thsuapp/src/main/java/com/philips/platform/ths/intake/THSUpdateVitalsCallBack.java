/* Copyright (c) Koninklijke Philips N.V., 2016
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */

package com.philips.platform.ths.intake;

import android.support.annotation.NonNull;

import com.americanwell.sdk.entity.SDKError;

import java.util.Map;

public interface THSUpdateVitalsCallBack {

    void onUpdateVitalsValidationFailure(@NonNull Map<String, String> map);

    void onUpdateVitalsResponse(SDKError sdkError);

    void onUpdateVitalsFailure(Throwable throwable);
}