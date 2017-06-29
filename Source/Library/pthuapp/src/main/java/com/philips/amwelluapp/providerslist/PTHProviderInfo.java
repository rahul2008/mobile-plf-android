package com.philips.amwelluapp.providerslist;

import android.support.annotation.Nullable;

import com.americanwell.sdk.entity.consumer.Gender;
import com.americanwell.sdk.entity.practice.PracticeInfo;
import com.americanwell.sdk.entity.provider.ProviderInfo;
import com.americanwell.sdk.entity.provider.ProviderType;
import com.americanwell.sdk.entity.provider.ProviderVisibility;

import java.io.Serializable;

public class PTHProviderInfo implements Serializable{
    ProviderInfo providerInfo;

    public ProviderInfo getProviderInfo() {
        return providerInfo;
    }

    public void setProviderInfo(ProviderInfo providerInfo) {
        this.providerInfo = providerInfo;
    }

    ProviderVisibility getVisibility(){
        return providerInfo.getVisibility();
    }

    @Nullable
    PracticeInfo getPracticeInfo(){
        return providerInfo.getPracticeInfo();
    }

    ProviderType getSpecialty(){
        return providerInfo.getSpecialty();
    }

    int getRating(){
        return providerInfo.getRating();
    }

    Integer getWaitingRoomCount(){
        return providerInfo.getWaitingRoomCount();
    }

    Gender getGender(){
        return providerInfo.getGender();
    }

    String getSourceId(){
        return providerInfo.getSourceId();
    }

    boolean hasImage(){
        return providerInfo.hasImage();
    }

}
