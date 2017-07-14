package com.philips.platform.ths.pojo;

import android.os.Parcel;
import android.support.annotation.NonNull;

import com.americanwell.sdk.entity.Address;
import com.americanwell.sdk.entity.State;

public class ShippingAddressPojo implements Address {
    @Override
    public String getAddress1() {
        return null;
    }

    @Override
    public void setAddress1(@NonNull String s) {

    }

    @Override
    public String getAddress2() {
        return null;
    }

    @Override
    public void setAddress2(@NonNull String s) {

    }

    @Override
    public String getCity() {
        return null;
    }

    @Override
    public void setCity(@NonNull String s) {

    }

    @Override
    public State getState() {
        return null;
    }

    @Override
    public void setState(@NonNull State state) {

    }

    @Override
    public String getZipCode() {
        return null;
    }

    @Override
    public void setZipCode(@NonNull String s) {

    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

    }
}
