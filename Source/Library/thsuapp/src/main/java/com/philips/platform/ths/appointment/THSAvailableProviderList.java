/* Copyright (c) Koninklijke Philips N.V., 2016
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */

package com.philips.platform.ths.appointment;

import android.os.Parcel;
import android.os.Parcelable;

import com.americanwell.sdk.entity.provider.AvailableProvider;
import com.americanwell.sdk.entity.provider.AvailableProviders;

import java.util.Date;
import java.util.List;

public class THSAvailableProviderList implements Parcelable{

    private AvailableProviders availableProviders;

    public THSAvailableProviderList(){

    }

    protected THSAvailableProviderList(Parcel in) {
        availableProviders = in.readParcelable(AvailableProviders.class.getClassLoader());
    }

    public static final Creator<THSAvailableProviderList> CREATOR = new Creator<THSAvailableProviderList>() {
        @Override
        public THSAvailableProviderList createFromParcel(Parcel in) {
            return new THSAvailableProviderList(in);
        }

        @Override
        public THSAvailableProviderList[] newArray(int size) {
            return new THSAvailableProviderList[size];
        }
    };

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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeParcelable(availableProviders, i);
    }
}
