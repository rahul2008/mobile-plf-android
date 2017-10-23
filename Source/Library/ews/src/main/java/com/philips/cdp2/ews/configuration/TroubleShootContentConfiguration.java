/*
 * Copyright (c) Koninklijke Philips N.V., 2017.
 * All rights reserved.
 */
package com.philips.cdp2.ews.configuration;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.DrawableRes;
import android.support.annotation.StringRes;


public class TroubleShootContentConfiguration implements Parcelable {

    @StringRes
    private final int resetConnTitle;
    @StringRes
    private final int resetConnBody;
    @DrawableRes
    private final int resetConnectionImage;

    @StringRes
    private final int resetDeviceTitle;
    @StringRes
    private final int resetDeviceBody;
    @DrawableRes
    private final int resetDeviceImage;

    @StringRes
    private final int setUpAccessPointTitle;
    @StringRes
    private final int setUpAccessPointBody;
    @DrawableRes
    private final int setUpAccessPointImage;

    @StringRes
    private final int connectWrongPhoneTitle;
    @StringRes
    private final int connectWrongPhoneBody;
    @DrawableRes
    private final int connectWrongPhoneImage;
    @StringRes
    private final int connectWrongPhoneQuestion;

    protected TroubleShootContentConfiguration(Parcel in) {
        resetConnTitle = in.readInt();
        resetConnBody = in.readInt();
        resetConnectionImage = in.readInt();
        resetDeviceTitle = in.readInt();
        resetDeviceBody = in.readInt();
        resetDeviceImage = in.readInt();
        setUpAccessPointTitle = in.readInt();
        setUpAccessPointBody = in.readInt();
        setUpAccessPointImage = in.readInt();
        connectWrongPhoneTitle = in.readInt();
        connectWrongPhoneBody = in.readInt();
        connectWrongPhoneImage = in.readInt();
        connectWrongPhoneQuestion = in.readInt();
    }

    public static final Creator<TroubleShootContentConfiguration> CREATOR = new Creator<TroubleShootContentConfiguration>() {
        @Override
        public TroubleShootContentConfiguration createFromParcel(Parcel in) {
            return new TroubleShootContentConfiguration(in);
        }

        @Override
        public TroubleShootContentConfiguration[] newArray(int size) {
            return new TroubleShootContentConfiguration[size];
        }
    };

    public int getResetConnTitle() {
        return resetConnTitle;
    }

    public int getResetConnBody() {
        return resetConnBody;
    }

    public int getResetConnectionImage() {
        return resetConnectionImage;
    }

    public int getResetDeviceTitle() {
        return resetDeviceTitle;
    }

    public int getResetDeviceBody() {
        return resetDeviceBody;
    }

    public int getResetDeviceImage() {
        return resetDeviceImage;
    }

    public int getSetUpAccessPointTitle() {
        return setUpAccessPointTitle;
    }

    public int getSetUpAccessPointBody() {
        return setUpAccessPointBody;
    }

    public int getSetUpAccessPointImage() {
        return setUpAccessPointImage;
    }

    public int getConnectWrongPhoneTitle() {
        return connectWrongPhoneTitle;
    }

    public int getConnectWrongPhoneBody() {
        return connectWrongPhoneBody;
    }

    public int getConnectWrongPhoneImage() {
        return connectWrongPhoneImage;
    }

    public int getConnectWrongPhoneQuestion() {
        return connectWrongPhoneQuestion;
    }

    protected TroubleShootContentConfiguration(Parcel in, int resetConnTitle, int resetConnBody, int resetConnectionImage, int resetDeviceTitle, int resetDeviceBody, int resetDeviceImage, int setUpAccessPointTitle, int setUpAccessPointBody, int setUpAccessPointImage, int connectWrongPhoneTitle, int connectWrongPhoneBody, int connectWrongPhoneImage, int connectWrongPhoneQuestion) {
        this.resetConnTitle = resetConnTitle;
        this.resetConnBody = resetConnBody;
        this.resetConnectionImage = resetConnectionImage;
        this.resetDeviceTitle = resetDeviceTitle;
        this.resetDeviceBody = resetDeviceBody;
        this.resetDeviceImage = resetDeviceImage;
        this.setUpAccessPointTitle = setUpAccessPointTitle;
        this.setUpAccessPointBody = setUpAccessPointBody;
        this.setUpAccessPointImage = setUpAccessPointImage;
        this.connectWrongPhoneTitle = connectWrongPhoneTitle;
        this.connectWrongPhoneBody = connectWrongPhoneBody;
        this.connectWrongPhoneImage = connectWrongPhoneImage;
        this.connectWrongPhoneQuestion = connectWrongPhoneQuestion;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(resetConnTitle);
        dest.writeInt(resetConnBody);
        dest.writeInt(resetConnectionImage);
        dest.writeInt(resetDeviceTitle);
        dest.writeInt(resetDeviceBody);
        dest.writeInt(resetDeviceImage);
        dest.writeInt(setUpAccessPointTitle);
        dest.writeInt(setUpAccessPointBody);
        dest.writeInt(setUpAccessPointImage);
        dest.writeInt(connectWrongPhoneTitle);
        dest.writeInt(connectWrongPhoneBody);
        dest.writeInt(connectWrongPhoneImage);
        dest.writeInt(connectWrongPhoneQuestion);
    }
}
