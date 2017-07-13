package com.philips.platform.ths.providerslist;

import com.americanwell.sdk.entity.provider.AvailableProvider;
import com.americanwell.sdk.entity.provider.ProviderInfo;


public interface OnProviderListItemClickListener {
    void onItemClick(ProviderInfo item, AvailableProvider availableProvider);
}
