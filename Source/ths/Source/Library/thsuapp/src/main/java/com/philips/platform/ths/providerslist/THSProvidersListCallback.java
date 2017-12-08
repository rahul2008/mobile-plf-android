/* Copyright (c) Koninklijke Philips N.V., 2016
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */

package com.philips.platform.ths.providerslist;

import com.americanwell.sdk.entity.SDKError;

import java.util.List;

//TODO: Review Comment - Spoorti - Use generics for type safety
public interface THSProvidersListCallback {
    void onProvidersListReceived(List<THSProviderInfo> providerInfoList, SDKError sdkError);
    void onProvidersListFetchError(Throwable throwable);
}
