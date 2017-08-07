/* Copyright (c) Koninklijke Philips N.V., 2016
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */

package com.philips.platform.ths.providerslist;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.Nullable;

import com.americanwell.sdk.entity.consumer.Gender;
import com.americanwell.sdk.entity.practice.PracticeInfo;
import com.americanwell.sdk.entity.provider.ProviderInfo;
import com.americanwell.sdk.entity.provider.ProviderType;
import com.americanwell.sdk.entity.provider.ProviderVisibility;
import com.philips.platform.ths.providerdetails.THSProviderEntity;

public class THSProviderInfo extends THSProviderEntity implements Parcelable{
    private ProviderInfo providerInfo;

    public THSProviderInfo(){

    }

    protected THSProviderInfo(Parcel in) {
        providerInfo = in.readParcelable(ProviderInfo.class.getClassLoader());
    }

    public static final Creator<THSProviderInfo> CREATOR = new Creator<THSProviderInfo>() {
        @Override
        public THSProviderInfo createFromParcel(Parcel in) {
            return new THSProviderInfo(in);
        }

        @Override
        public THSProviderInfo[] newArray(int size) {
            return new THSProviderInfo[size];
        }
    };

    public ProviderInfo getProviderInfo() {
        return providerInfo;
    }

    public void setTHSProviderInfo(ProviderInfo providerInfo) {
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeParcelable(providerInfo, i);
    }
}
