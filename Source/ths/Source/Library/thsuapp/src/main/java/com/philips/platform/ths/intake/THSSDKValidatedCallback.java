/* Copyright (c) Koninklijke Philips N.V., 2016
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */

package com.philips.platform.ths.intake;

import android.support.annotation.NonNull;

import com.americanwell.sdk.entity.SDKError;
import com.americanwell.sdk.manager.SDKCallback;

import java.util.Map;

public interface THSSDKValidatedCallback<T, E extends SDKError> extends SDKCallback<T, E> {

    void onValidationFailure(@NonNull Map<String, String> map);
}
