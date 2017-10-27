/*
 * Copyright (c) Koninklijke Philips N.V., 2017.
 * All rights reserved.
 */
package com.philips.cdp2.ews.configuration;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

public class ContentConfiguration implements Parcelable {

    @NonNull private final BaseContentConfiguration baseContentConfiguration;
    @NonNull private final HappyFlowContentConfiguration happyFlowContentConfiguration;
    @NonNull private final TroubleShootContentConfiguration troubleShootContentConfiguration;

    public ContentConfiguration() {
        baseContentConfiguration = new BaseContentConfiguration();
        happyFlowContentConfiguration = new HappyFlowContentConfiguration.Builder().build();
        troubleShootContentConfiguration = new TroubleShootContentConfiguration.Builder().build();
    }

    public ContentConfiguration(@NonNull BaseContentConfiguration baseConfig,
                                @NonNull HappyFlowContentConfiguration happyFlowConfig,
                                @NonNull TroubleShootContentConfiguration troubleShootContentConfiguration) {
        this.baseContentConfiguration = baseConfig;
        this.happyFlowContentConfiguration = happyFlowConfig;
        this.troubleShootContentConfiguration = troubleShootContentConfiguration;
    }

    @NonNull
    public BaseContentConfiguration getBaseContentConfiguration() {
        return baseContentConfiguration;
    }

    @NonNull
    public HappyFlowContentConfiguration getHappyFlowContentConfiguration() {
        return happyFlowContentConfiguration;
    }

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