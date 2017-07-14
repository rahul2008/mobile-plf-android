package com.philips.platform.ths.providerslist;

import com.americanwell.sdk.entity.provider.ProviderInfo;

import java.util.List;

public interface THSProviderListViewInterface {
    void updateProviderAdapterList(List<THSProviderInfo> providerInfos);
}
