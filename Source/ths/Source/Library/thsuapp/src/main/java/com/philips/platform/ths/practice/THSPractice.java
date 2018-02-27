/* Copyright (c) Koninklijke Philips N.V., 2016
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */

package com.philips.platform.ths.practice;

import android.os.Parcel;
import android.os.Parcelable;

import com.americanwell.sdk.entity.Address;
import com.americanwell.sdk.entity.practice.Practice;

@SuppressWarnings("serial")
public class THSPractice implements Parcelable{
    private Practice practice;

    public THSPractice(){

    }

    protected THSPractice(Parcel in) {
        practice = in.readParcelable(Practice.class.getClassLoader());
    }

    public static final Creator<THSPractice> CREATOR = new Creator<THSPractice>() {
        @Override
        public THSPractice createFromParcel(Parcel in) {
            return new THSPractice(in);
        }

        @Override
        public THSPractice[] newArray(int size) {
            return new THSPractice[size];
        }
    };

    public Practice getPractice() {
        return practice;
    }

    public void setPractice(Practice practice) {
        this.practice = practice;
    }

    Address getAddress(){
        return practice.getAddress();
    }

    String getPhone(){
        return practice.getPhone();
    }

    String getFax(){
        return practice.getFax();
    }

    String getHours(){
        return practice.getHours();
    }

    String getWelcomeMessage(){
        return practice.getWelcomeMessage();
    }

    boolean isShowScheduling(){
        return practice.isShowScheduling();
    }

    boolean isShowAvailableNow(){
        return practice.isShowAvailableNow();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeParcelable(practice, i);
    }
}
