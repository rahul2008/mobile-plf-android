package com.philips.platform.ths.intake;

import android.os.Parcel;
import android.os.Parcelable;

import com.americanwell.sdk.entity.health.Condition;

public class PTHConditions implements Parcelable{
    Condition condition;

    public PTHConditions(){

    }

    protected PTHConditions(Parcel in) {
        condition = in.readParcelable(Condition.class.getClassLoader());
    }

    public static final Creator<PTHConditions> CREATOR = new Creator<PTHConditions>() {
        @Override
        public PTHConditions createFromParcel(Parcel in) {
            return new PTHConditions(in);
        }

        @Override
        public PTHConditions[] newArray(int size) {
            return new PTHConditions[size];
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
