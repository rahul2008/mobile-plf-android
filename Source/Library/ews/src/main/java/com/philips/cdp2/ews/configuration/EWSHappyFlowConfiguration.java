package com.philips.cdp2.ews.configuration;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.StringRes;

public class EWSHappyFlowConfiguration implements Parcelable {

    @StringRes
    private int title;

    @StringRes
    private int appName;


    public EWSHappyFlowConfiguration(@StringRes int title, @StringRes int appName) {
        this.title = title;
        this.appName = appName;
    }

    public int getTitle() {
        return title;
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
        dest.writeInt(this.title);
        dest.writeInt(this.appName);
    }

    protected EWSHappyFlowConfiguration(Parcel in) {
        this.title = in.readInt();
        this.appName = in.readInt();
    }

    public static final Parcelable.Creator<EWSHappyFlowConfiguration> CREATOR = new Parcelable.Creator<EWSHappyFlowConfiguration>() {
        @Override
        public EWSHappyFlowConfiguration createFromParcel(Parcel source) {
            return new EWSHappyFlowConfiguration(source);
        }

        @Override
        public EWSHappyFlowConfiguration[] newArray(int size) {
            return new EWSHappyFlowConfiguration[size];
        }
    };
}
