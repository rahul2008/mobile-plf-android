/*
 * Copyright (c) Koninklijke Philips N.V., 2017.
 * All rights reserved.
 */
package com.philips.cdp2.ews.configuration;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.StringRes;

public class BaseContentConfiguration implements Parcelable {

    @StringRes
    private int deviceName;

    @StringRes
    private int appName;


    public BaseContentConfiguration(@StringRes int deviceName, @StringRes int appName) {
        this.deviceName = deviceName;
        this.appName = appName;
    }

    public BaseContentConfiguration() {

    }

    public int getDeviceName() {
        return deviceName;
    }

    public int getAppName() {
        return appName;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.deviceName);
        dest.writeInt(this.appName);
    }

    protected BaseContentConfiguration(Parcel in) {
        this.deviceName = in.readInt();
        this.appName = in.readInt();
    }

    public static final Parcelable.Creator<BaseContentConfiguration> CREATOR = new Parcelable.Creator<BaseContentConfiguration>() {
        @Override
        public BaseContentConfiguration createFromParcel(Parcel source) {
            return new BaseContentConfiguration(source);
        }

        @Override
        public BaseContentConfiguration[] newArray(int size) {
            return new BaseContentConfiguration[size];
        }
    };
}
