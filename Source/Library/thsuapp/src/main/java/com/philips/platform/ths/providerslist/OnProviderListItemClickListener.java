package com.philips.platform.ths.providerslist;

import com.americanwell.sdk.entity.provider.AvailableProvider;


public interface OnProviderListItemClickListener {
    void onItemClick(THSProviderInfo item, AvailableProvider availableProvider);
}
