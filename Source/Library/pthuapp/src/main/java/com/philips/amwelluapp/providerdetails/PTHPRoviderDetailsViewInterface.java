package com.philips.amwelluapp.providerdetails;


import android.content.Context;

import com.americanwell.sdk.entity.consumer.Consumer;
import com.americanwell.sdk.entity.provider.ProviderInfo;

public interface PTHPRoviderDetailsViewInterface {

    Context getContext();
    ProviderInfo getProviderInfo();
    Consumer getConsumerInfo();
}
