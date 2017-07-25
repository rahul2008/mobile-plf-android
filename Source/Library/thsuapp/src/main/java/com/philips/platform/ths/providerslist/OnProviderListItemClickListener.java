package com.philips.platform.ths.providerslist;

import com.americanwell.sdk.entity.provider.AvailableProvider;
import com.philips.platform.ths.providerdetails.THSProviderEntity;


public interface OnProviderListItemClickListener {
    void onItemClick(THSProviderEntity item);
}
