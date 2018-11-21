/*
 * Copyright (c) Koninklijke Philips N.V., 2017.
 * All rights reserved.
 */
package com.philips.platform.ews.configuration;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.StringRes;

import com.philips.platform.ews.R;
import com.philips.platform.ews.microapp.EwsResultListener;

import java.util.concurrent.TimeUnit;

/**
 * BaseContentConfiguration is a model class for configuration,
 * being used to keep deviceName and appName provided By Proposition.
 */
public class BaseContentConfiguration implements Parcelable {

    @StringRes
    private final int deviceName;
    @StringRes
    private final int appName;
    private EwsResultListener ewsResultListener;
    /**
     * Allows apps to configure device discovery timeout in EWS.
     * Default value is 60.
     * If appliance is not discovered within timeout, EWS will result in failure and user will be redirected to connection error screen.
     * @since  2018.5.0
     */
    public static long deviceDiscoveryTimeoutInterval = TimeUnit.SECONDS.toMillis(60);


    /**
     * This constructor creates BaseContentConfiguration by providing Name of device used for EWS and Name of app.
     * @param deviceName  DeviceName @StringRes
     * @param appName  AppName @StringRes
     */
    private BaseContentConfiguration(@StringRes int deviceName, @StringRes int appName) {
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

    public EwsResultListener getEwsResultListener() {
        return ewsResultListener;
    }

    /**
     * Setter for EwsResultListener.
     * @since  2018.5.0
     */
    public void setEwsResultListener(EwsResultListener ewsResultListener) {
        if(this.ewsResultListener == null){
            this.ewsResultListener = ewsResultListener;
        }
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

    public static class Builder {

        @StringRes private int deviceName;
        @StringRes private int appName;

        /**
         * Builder to build {@link BaseContentConfiguration} with default string resource ids.
         */
        public Builder() {
            deviceName = R.string.ews_device_name_default;
            appName = R.string.ews_app_name_default;
        }

        /**
         * Setter for DeviceName
         * @param deviceName Device name string resource id
         * @return Builder
         */
        public Builder setDeviceName(@StringRes int deviceName) {
            this.deviceName = deviceName;
            return this;
        }

        /**
         * Setter for AppName
         * @param appName App name string resource id
         * @return Builder
         */
        public Builder setAppName(@StringRes int appName) {
            this.appName = appName;
            return this;
        }

        /**
         * Return BaseContentConfiguration.
         * @return BaseContentConfiguration
         */
        public BaseContentConfiguration build() {
            return new BaseContentConfiguration(deviceName,appName);
        }
    }
}