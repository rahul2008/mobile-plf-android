/* Copyright (c) Koninklijke Philips N.V., 2016
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */

package com.philips.platform.ths.registration;

import com.americanwell.sdk.entity.SDKError;
import com.americanwell.sdk.manager.SDKCallback;
import com.americanwell.sdk.manager.ValidationReason;

import java.util.Map;

public interface THSEditUserDetailsCallBack<T, E>{

    void onEditUserDataValidationFailure(Map<String, ValidationReason> var1);
    void onEditUserDataResponse(T var1, E var2);
    void onEditUserDataFailure(Throwable var1);
}
