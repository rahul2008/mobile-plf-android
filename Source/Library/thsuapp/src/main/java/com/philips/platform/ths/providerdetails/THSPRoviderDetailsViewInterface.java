package com.philips.platform.ths.providerdetails;


import android.content.Context;

import com.americanwell.sdk.entity.consumer.Consumer;
import com.americanwell.sdk.entity.practice.Practice;
import com.americanwell.sdk.entity.provider.Provider;
import com.americanwell.sdk.entity.provider.ProviderInfo;

import java.util.Date;
import java.util.List;

public interface THSPRoviderDetailsViewInterface {

    Context getContext();
    ProviderInfo getProviderInfo();
    Practice getPracticeInfo();
    Consumer getConsumerInfo();
    void updateView(Provider provider);
    void dismissRefreshLayout();
    List<Date> getAppointmentTimeSlots();
}
