/* Copyright (c) Koninklijke Philips N.V., 2016
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */

package com.philips.platform.ths.intake;

public interface THSConditionsCallBack<THSCondition, PTHSDKError> {
    void onResponse(THSCondition var1, PTHSDKError var2);

    void onFailure(Throwable throwable);
}