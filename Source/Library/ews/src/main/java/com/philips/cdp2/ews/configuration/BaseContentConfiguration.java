/*
 * Copyright (c) Koninklijke Philips N.V., 2017.
 * All rights reserved.
 */
package com.philips.cdp2.ews.configuration;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.StringRes;

import com.philips.cdp2.ews.R;

/**
 * BaseContentConfiguration is a model class for configuration,
 * being used to keep deviceName and appName provided By Proposition.
 */
public class BaseContentConfiguration implements Parcelable {

    @StringRes
    private final int deviceName;

    @StringRes
    private final int appName;

    /**
     * Default constructor
     */
    public BaseContentConfiguration() {
        deviceName = R.string.ews_device_name_default;
        appName = R.string.ews_app_name_default;
    }

    /**
     * This constructor creates BaseContentConfiguration by providing Name of device used for EWS and Name of app.
     * @param deviceName  DeviceName @StringRes
     * @param appName  AppName @StringRes
     */
    public BaseContentConfiguration(@StringRes int deviceName, @StringRes int appName) {
        this.deviceName = deviceName;
        this.appName = appName;
    }

    protected BaseContentConfiguration(Parcel in) {
        this.deviceName = in.readInt();
        this.appName = in.readInt();
    }

    /**
     * Returns DeviceName String Resources
     * @return DeviceName
     */
    public int getDeviceName() {
        return deviceName;
    }

    /**
     * Returns AppName String Resources
     * @return AppName
     */
    public int getAppName() {
        return appName;
    }

    /**
     * Returns Content
     * @return content
     */
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.deviceName);
        dest.writeInt(this.appName);
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