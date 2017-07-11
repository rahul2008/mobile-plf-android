package com.philips.platform.ths.appointment;

import android.support.annotation.NonNull;

import com.americanwell.sdk.entity.provider.AvailableProvider;
import com.americanwell.sdk.entity.provider.ProviderInfo;

import java.util.Date;
import java.util.List;

public class THSAvailableProviders {

    AvailableProvider availableProvider;

    public AvailableProvider getAvailableProvider() {
        return availableProvider;
    }

    public void setAvailableProvider(AvailableProvider availableProvider) {
        this.availableProvider = availableProvider;
    }

    ProviderInfo getProviderInfo(){
        return availableProvider.getProviderInfo();
    }

    List<Date> getAvailableAppointmentTimeSlots(){
        return availableProvider.getAvailableAppointmentTimeSlots();
    }
}
