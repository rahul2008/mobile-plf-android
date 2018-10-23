/* Copyright (c) Koninklijke Philips N.V., 2016
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */

package com.philips.platform.ths.providerslist;

import android.os.Parcel;
import android.os.Parcelable;

import com.americanwell.sdk.entity.practice.OnDemandSpecialty;

public class THSOnDemandSpeciality implements Parcelable{

    private OnDemandSpecialty onDemandSpecialty;

    public THSOnDemandSpeciality(){

    }

    protected THSOnDemandSpeciality(Parcel in) {
        onDemandSpecialty = in.readParcelable(OnDemandSpecialty.class.getClassLoader());
    }

    public static final Creator<THSOnDemandSpeciality> CREATOR = new Creator<THSOnDemandSpeciality>() {
        @Override
        public THSOnDemandSpeciality createFromParcel(Parcel in) {
            return new THSOnDemandSpeciality(in);
        }

        @Override
        public THSOnDemandSpeciality[] newArray(int size) {
            return new THSOnDemandSpeciality[size];
        }
    };

    public OnDemandSpecialty getOnDemandSpecialty() {
        return onDemandSpecialty;
    }

    public void setOnDemandSpecialty(OnDemandSpecialty onDemandSpecialty) {
        this.onDemandSpecialty = onDemandSpecialty;
    }

    boolean isIntakeConditionEnabled(){
        return onDemandSpecialty.isIntakeConditionEnabled();
    }

    boolean isIntakeAllergiesEnabled(){
        return onDemandSpecialty.isIntakeAllergiesEnabled();
    }

    boolean isIntakeMedicationsEnabled(){
        return onDemandSpecialty.isIntakeMedicationsEnabled();
    }

    boolean isIntakeVitalsEnabled(){
        return onDemandSpecialty.isIntakeVitalsEnabled();
    }

    boolean areProvidersAvailable(){
        return onDemandSpecialty.areProvidersAvailable();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeParcelable(onDemandSpecialty, i);
    }
}
