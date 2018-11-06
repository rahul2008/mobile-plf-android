package com.philips.platform.ths.cost;
/* Copyright (c) Koninklijke Philips N.V., 2016
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */

public interface ApplyCouponCallback<Void, THSSDKError> {
    void onApplyCouponResponse(Void aVoid, THSSDKError thssdkError);
    void onApplyCouponFailure(Throwable throwable);
}
