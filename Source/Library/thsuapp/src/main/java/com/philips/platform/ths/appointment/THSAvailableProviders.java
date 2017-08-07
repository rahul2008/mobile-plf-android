/* Copyright (c) Koninklijke Philips N.V., 2016
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */

package com.philips.platform.ths.appointment;

import com.americanwell.sdk.entity.provider.AvailableProvider;
import com.americanwell.sdk.entity.provider.ProviderInfo;

import java.util.Date;
import java.util.List;

public class THSAvailableProviders {

    private AvailableProvider availableProvider;

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
