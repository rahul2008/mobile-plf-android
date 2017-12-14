/*
 * Copyright (c) Koninklijke Philips N.V., 2017.
 * All rights reserved.
 */
package com.philips.platform.ews.configuration;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

/**
 * ContentConfiguration is a model class for configuration,
 * Which is keeping BaseContentConfiguration, HappyFlowContentConfiguration, TroubleShootContentConfiguration in it.
 */
public class ContentConfiguration implements Parcelable {

    @NonNull private final BaseContentConfiguration baseContentConfiguration;
    @NonNull private final HappyFlowContentConfiguration happyFlowContentConfiguration;
    @NonNull private final TroubleShootContentConfiguration troubleShootContentConfiguration;

    /**
     * Default Constructor for ContentConfiguration
     */
    public ContentConfiguration() {
        baseContentConfiguration = new BaseContentConfiguration.Builder().build();
        happyFlowContentConfiguration = new HappyFlowContentConfiguration.Builder().build();
        troubleShootContentConfiguration = new TroubleShootContentConfiguration.Builder().build();
    }

    /**
     * This constructor creates ContentConfiguration by using BaseContentConfiguration,HappyFlowContentConfiguration and TroubleShootContentConfiguration provided by Proposition.
     * @param baseConfig  BaseContentConfiguration
     * @param happyFlowConfig  HappyFlowContentConfiguration
     * @param troubleShootContentConfiguration  TroubleShootContentConfiguration
     */
    public ContentConfiguration(@NonNull BaseContentConfiguration baseConfig,
                                @NonNull HappyFlowContentConfiguration happyFlowConfig,
                                @NonNull TroubleShootContentConfiguration troubleShootContentConfiguration) {
        this.baseContentConfiguration = baseConfig;
        this.happyFlowContentConfiguration = happyFlowConfig;
        this.troubleShootContentConfiguration = troubleShootContentConfiguration;
    }

    /**
     * Return BaseContentConfiguration
     * @return BaseContentConfiguration
     */
    @NonNull
    public BaseContentConfiguration getBaseContentConfiguration() {
        return baseContentConfiguration;
    }

    /**
     * Return HappyFlowContentConfiguration
     * @return HappyFlowContentConfiguration
     */
    @NonNull
    public HappyFlowContentConfiguration getHappyFlowContentConfiguration() {
        return happyFlowContentConfiguration;
    }

    /**
     * Return HappyFlowContentConfiguration
     * @return TroubleShootContentConfiguration
     */
    @NonNull
    public TroubleShootContentConfiguration getTroubleShootContentConfiguration(){
        return troubleShootContentConfiguration;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(this.baseContentConfiguration, flags);
        dest.writeParcelable(this.happyFlowContentConfiguration, flags);
        dest.writeParcelable(this.troubleShootContentConfiguration, flags);
    }

    protected ContentConfiguration(Parcel in) {
        this.baseContentConfiguration = in.readParcelable(BaseContentConfiguration.class.getClassLoader());
        this.happyFlowContentConfiguration = in.readParcelable(HappyFlowContentConfiguration.class.getClassLoader());
        this.troubleShootContentConfiguration = in.readParcelable(TroubleShootContentConfiguration.class.getClassLoader());
    }

    public static final Creator<ContentConfiguration> CREATOR = new Creator<ContentConfiguration>() {
        @Override
        public ContentConfiguration createFromParcel(Parcel source) {
            return new ContentConfiguration(source);
        }

        @Override
        public ContentConfiguration[] newArray(int size) {
            return new ContentConfiguration[size];
        }
    };
}