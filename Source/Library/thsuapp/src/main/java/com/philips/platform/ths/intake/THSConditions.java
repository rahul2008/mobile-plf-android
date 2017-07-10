package com.philips.platform.ths.intake;

import android.os.Parcel;
import android.os.Parcelable;

import com.americanwell.sdk.entity.health.Condition;

public class THSConditions implements Parcelable{
    Condition condition;

    public THSConditions(){

    }

    protected THSConditions(Parcel in) {
        condition = in.readParcelable(Condition.class.getClassLoader());
    }

    public static final Creator<THSConditions> CREATOR = new Creator<THSConditions>() {
        @Override
        public THSConditions createFromParcel(Parcel in) {
            return new THSConditions(in);
        }

        @Override
        public THSConditions[] newArray(int size) {
            return new THSConditions[size];
        }
    };

    public Condition getCondition() {
        return condition;
    }

    public void setCondition(Condition condition) {
        this.condition = condition;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeParcelable(condition, i);
    }
}
