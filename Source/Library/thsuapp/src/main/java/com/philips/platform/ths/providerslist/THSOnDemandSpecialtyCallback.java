/* Copyright (c) Koninklijke Philips N.V., 2016
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */

package com.philips.platform.ths.providerslist;

public interface THSOnDemandSpecialtyCallback<List,THSSDKError> {
    void onResponse(List onDemandSpecialties, THSSDKError sdkError);
    void onFailure(Throwable throwable);
}
