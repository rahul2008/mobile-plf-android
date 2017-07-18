package com.philips.platform.ths.appointment;

import android.os.Parcel;
import android.support.annotation.NonNull;

import com.americanwell.sdk.entity.provider.AvailableProvider;
import com.americanwell.sdk.entity.provider.ProviderInfo;
import com.philips.platform.ths.providerdetails.THSProviderEntity;

import java.util.Date;
import java.util.List;

public class THSAvailableProvider extends THSProviderEntity{

    AvailableProvider availableProvider;

    public THSAvailableProvider(){

    }

    protected THSAvailableProvider(Parcel in) {
        super(in);
    }

    public AvailableProvider getAvailableProvider() {
        return availableProvider;
    }

    public void setAvailableProvider(AvailableProvider availableProvider) {
        this.availableProvider = availableProvider;
    }

    public ProviderInfo getProviderInfo(){
        return availableProvider.getProviderInfo();
    }

    public List<Date> getAvailableAppointmentTimeSlots(){
        return availableProvider.getAvailableAppointmentTimeSlots();
    }
}
