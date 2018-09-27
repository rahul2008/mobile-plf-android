/* Copyright (c) Koninklijke Philips N.V., 2016
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */

package com.philips.platform.ths.appointment;

public interface THSAvailableProvidersBasedOnDateCallback<THSAvailableProviderList, THSSDKError> {
    void onResponse(THSAvailableProviderList availableProviders, THSSDKError sdkError);
    void onFailure(Throwable throwable);
}
