package com.philips.cdp2.ews.configuration;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

public class ContentConfiguration implements Parcelable {

    @NonNull private final BaseContentConfiguration baseContentConfiguration;
    @NonNull private final HappyFlowContentConfiguration happyFlowContentConfiguration;

    public ContentConfiguration() {
        baseContentConfiguration = new BaseContentConfiguration();
        happyFlowContentConfiguration = new HappyFlowContentConfiguration.Builder().build();
    }

    public ContentConfiguration(@NonNull BaseContentConfiguration baseConfig,
                                @NonNull HappyFlowContentConfiguration happyFlowConfig) {
        this.baseContentConfiguration = baseConfig;
        this.happyFlowContentConfiguration = happyFlowConfig;
    }

    protected ContentConfiguration(Parcel in) {
        baseContentConfiguration = in.readParcelable(BaseContentConfiguration.class.getClassLoader());
        happyFlowContentConfiguration = in.readParcelable(HappyFlowContentConfiguration.class.getClassLoader());
    }

    @NonNull
    public BaseContentConfiguration getBaseContentConfiguration() {
        return baseContentConfiguration;
    }

    @NonNull
    public HappyFlowContentConfiguration getHappyFlowContentConfiguration() {
        return happyFlowContentConfiguration;
    }

    public static final Creator<ContentConfiguration> CREATOR =
            new Creator<ContentConfiguration>() {
                @Override
                public ContentConfiguration createFromParcel(Parcel in) {
                    return new ContentConfiguration(in);
                }

                @Override
                public ContentConfiguration[] newArray(int size) {
                    return new ContentConfiguration[size];
                }
            };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(baseContentConfiguration, flags);
        dest.writeParcelable(happyFlowContentConfiguration, flags);
    }
}