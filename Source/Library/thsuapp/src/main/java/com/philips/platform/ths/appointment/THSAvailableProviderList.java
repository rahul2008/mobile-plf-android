package com.philips.platform.ths.appointment;

import com.americanwell.sdk.entity.provider.AvailableProvider;
import com.americanwell.sdk.entity.provider.AvailableProviders;

import java.util.Date;
import java.util.List;

public class THSAvailableProviderList {
    AvailableProviders availableProviders;

    public AvailableProviders getAvailableProviders() {
        return availableProviders;
    }

    public void setAvailableProviders(AvailableProviders availableProviders) {
        this.availableProviders = availableProviders;
    }

    List<AvailableProvider> getAvailableProvidersList(){
        return availableProviders.getAvailableProviders();
    }

    Date getDate(){
        return availableProviders.getDate();
    }
}
