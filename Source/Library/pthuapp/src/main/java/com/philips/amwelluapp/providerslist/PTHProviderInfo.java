package com.philips.amwelluapp.providerslist;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.Nullable;

import com.americanwell.sdk.entity.consumer.Gender;
import com.americanwell.sdk.entity.practice.PracticeInfo;
import com.americanwell.sdk.entity.provider.ProviderInfo;
import com.americanwell.sdk.entity.provider.ProviderType;
import com.americanwell.sdk.entity.provider.ProviderVisibility;

public class PTHProviderInfo implements Parcelable{
    ProviderInfo providerInfo;

    public PTHProviderInfo(){

    }

    protected PTHProviderInfo(Parcel in) {
        providerInfo = in.readParcelable(ProviderInfo.class.getClassLoader());
    }

    public static final Creator<PTHProviderInfo> CREATOR = new Creator<PTHProviderInfo>() {
        @Override
        public PTHProviderInfo createFromParcel(Parcel in) {
            return new PTHProviderInfo(in);
        }

        @Override
        public PTHProviderInfo[] newArray(int size) {
            return new PTHProviderInfo[size];
        }
    };

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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeParcelable(providerInfo, i);
    }
}
