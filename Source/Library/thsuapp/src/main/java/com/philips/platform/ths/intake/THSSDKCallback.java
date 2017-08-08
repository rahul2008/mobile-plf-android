/* Copyright (c) Koninklijke Philips N.V., 2016
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */

package com.philips.platform.ths.intake;

import com.americanwell.sdk.entity.SDKError;


public interface THSSDKCallback<T, E extends SDKError> {
    void onResponse(T var1, E var2);

    void onFailure(Throwable var1);
}