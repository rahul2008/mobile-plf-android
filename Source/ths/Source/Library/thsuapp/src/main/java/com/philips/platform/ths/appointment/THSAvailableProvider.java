/* Copyright (c) Koninklijke Philips N.V., 2016
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */
package com.philips.platform.ths.appointment;

import android.os.Parcel;

import com.americanwell.sdk.entity.provider.AvailableProvider;
import com.americanwell.sdk.entity.provider.ProviderInfo;
import com.philips.platform.ths.providerdetails.THSProviderEntity;

import java.util.Date;
import java.util.List;

public class THSAvailableProvider extends THSProviderEntity{

    private AvailableProvider availableProvider;

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
