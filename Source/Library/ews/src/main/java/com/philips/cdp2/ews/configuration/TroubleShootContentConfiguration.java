/*
 * Copyright (c) Koninklijke Philips N.V., 2017.
 * All rights reserved.
 */
package com.philips.cdp2.ews.configuration;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.DrawableRes;
import android.support.annotation.StringRes;

import com.philips.cdp2.ews.R;


/**
 * TroubleShootContentConfiguration is a model class for Trouble-Shoot configuration,
 * Which is setting different string and drawables to Trouble-Shoot Screens.
 */
public class TroubleShootContentConfiguration implements Parcelable {

    // configuration for EWS_H_03.2 reset connnection screen.
    @StringRes
    private final int resetConnectionTitle;
    @StringRes
    private final int resetConnectionBody;
    @DrawableRes
    private final int resetConnectionImage;

    // configuration for EWS_H_03.3 reset device screen.
    @StringRes
    private final int resetDeviceTitle;
    @StringRes
    private final int resetDeviceBody;
    @DrawableRes
    private final int resetDeviceImage;

    // configuration for EWS_H_03.4 setup access point screen
    @StringRes
    private final int setUpAccessPointTitle;
    @StringRes
    private final int setUpAccessPointBody;
    @DrawableRes
    private final int setUpAccessPointImage;

    // configuration for EWS_H_03.1 connect to wrong phone screen
    @StringRes
    private final int connectWrongPhoneTitle;
    @StringRes
    private final int connectWrongPhoneBody;
    @DrawableRes
    private final int connectWrongPhoneImage;
    @StringRes
    private final int connectWrongPhoneQuestion;

    private TroubleShootContentConfiguration(int resetConnectionTitle, int resetConnectionBody, int resetConnectionImage, int resetDeviceTitle, int resetDeviceBody, int resetDeviceImage, int setUpAccessPointTitle, int setUpAccessPointBody, int setUpAccessPointImage, int connectWrongPhoneTitle, int connectWrongPhoneBody, int connectWrongPhoneImage, int connectWrongPhoneQuestion) {
        this.resetConnectionTitle = resetConnectionTitle;
        this.resetConnectionBody = resetConnectionBody;
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

    protected TroubleShootContentConfiguration(Parcel in) {
        resetConnectionTitle = in.readInt();
        resetConnectionBody = in.readInt();
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

    /**
     * Return ResetConnectionTitle
     * @return @StringRes ResetConnectionTitle
     */
    public int getResetConnectionTitle() {
        return resetConnectionTitle;
    }

    /**
     * Return ResetConnectionBody
     * @return @StringRes ResetConnectionBody
     */
    public int getResetConnectionBody() {
        return resetConnectionBody;
    }

    /**
     * Return ResetConnectionImage
     * @return @DrawableRes ResetConnectionImage
     */
    public int getResetConnectionImage() {
        return resetConnectionImage;
    }

    /**
     * Return ResetDeviceTitle
     * @return @StringRes ResetDeviceTitle
     */
    public int getResetDeviceTitle() {
        return resetDeviceTitle;
    }

    /**
     * Return ResetDeviceBody
     * @return @StringRes ResetDeviceBody
     */
    public int getResetDeviceBody() {
        return resetDeviceBody;
    }

    /**
     * Return ResetDeviceImage
     * @return @DrawableRes ResetDeviceImage
     */
    public int getResetDeviceImage() {
        return resetDeviceImage;
    }

    /**
     * Return SetUpAccessPointTitle
     * @return @StringRes SetUpAccessPointTitle
     */
    public int getSetUpAccessPointTitle() {
        return setUpAccessPointTitle;
    }

    /**
     * Return SetUpAccessPointBody
     * @return @StringRes SetUpAccessPointBody
     */
    public int getSetUpAccessPointBody() {
        return setUpAccessPointBody;
    }

    /**
     * Return SetUpAccessPointImage
     * @return @DrawableRes SetUpAccessPointImage
     */
    public int getSetUpAccessPointImage() {
        return setUpAccessPointImage;
    }

    /**
     * Return ConnectWrongPhoneTitle
     * @return @StringRes ConnectWrongPhoneTitle
     */
    public int getConnectWrongPhoneTitle() {
        return connectWrongPhoneTitle;
    }

    /**
     * Return ConnectWrongPhoneBody
     * @return @StringRes ConnectWrongPhoneBody
     */
    public int getConnectWrongPhoneBody() {
        return connectWrongPhoneBody;
    }

    /**
     * Return ConnectWrongPhoneImage
     * @return @DrawableRes ConnectWrongPhoneImage
     */
    public int getConnectWrongPhoneImage() {
        return connectWrongPhoneImage;
    }

    /**
     * Return ConnectWrongPhoneQuestion
     * @return @StringRes ConnectWrongPhoneQuestion
     */
    public int getConnectWrongPhoneQuestion() {
        return connectWrongPhoneQuestion;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(resetConnectionTitle);
        dest.writeInt(resetConnectionBody);
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


    public static class Builder{

        @StringRes
        private  int resetConnectionTitle;
        @StringRes
        private  int resetConnectionBody;
        @DrawableRes
        private  int resetConnectionImage;

        @StringRes
        private  int resetDeviceTitle;
        @StringRes
        private  int resetDeviceBody;
        @DrawableRes
        private  int resetDeviceImage;

        @StringRes
        private  int setUpAccessPointTitle;
        @StringRes
        private  int setUpAccessPointBody;
        @DrawableRes
        private  int setUpAccessPointImage;

        @StringRes
        private  int connectWrongPhoneTitle;
        @StringRes
        private  int connectWrongPhoneBody;
        @DrawableRes
        private  int connectWrongPhoneImage;
        @StringRes
        private  int connectWrongPhoneQuestion;

        /**
         * Build TroubleShootContent with default strings and drawables.
         */
        public Builder(){
            resetConnectionTitle = R.string.label_ews_support_reset_connection_title_default;
            resetConnectionBody = R.string.label_ews_support_reset_connection_body_default;
            resetConnectionImage = R.drawable.philips_logo_default;

            resetDeviceTitle = R.string.label_ews_support_reset_device_title_default;
            resetDeviceBody = R.string.label_ews_support_reset_device_body_default;
            resetDeviceImage = R.drawable.philips_logo_default;

            setUpAccessPointTitle = R.string.label_ews_support_setup_access_point_title_default;
            setUpAccessPointBody = R.string.label_ews_support_setup_access_point_body_default;
            setUpAccessPointImage = R.drawable.philips_logo_default;

            connectWrongPhoneTitle = R.string.label_ews_support_wrong_phone_title_default;
            connectWrongPhoneBody = R.string.label_ews_support_wrong_phone_body_default;
            connectWrongPhoneImage = R.drawable.philips_logo_default;
            connectWrongPhoneQuestion = R.string.label_ews_support_wrong_phone_question_default;

        }

        /**
         * Setter for ResetConnectionTitle
         * @param resetConnectionTitle  ResetConnectionTitle
         * @return Builder
         */
        public Builder setResetConnectionTitle(@StringRes int resetConnectionTitle){
            this.resetConnectionTitle = resetConnectionTitle;
            return this;
        }

        /**
         * Setter for ResetConnectionBody
         * @param resetConnectionBody ResetConnectionBody
         * @return Builder
         */
        public Builder setResetConnectionBody(@StringRes int resetConnectionBody){
            this.resetConnectionBody = resetConnectionBody;
            return this;
        }

        /**
         * Setter for ResetConnectionImage
         * @param resetConnectionImage ResetConnectionImage
         * @return Builder
         */
        public Builder setResetConnectionImage(@DrawableRes int resetConnectionImage){
            this.resetConnectionImage = resetConnectionImage;
            return this;
        }

        /**
         * Setter for ResetDeviceTitle
         * @param resetDeviceTitle ResetDeviceTitle
         * @return Builder
         */
        public Builder setResetDeviceTitle(@StringRes int resetDeviceTitle){
            this.resetDeviceTitle = resetDeviceTitle;
            return this;
        }

        /**
         * Setter for ResetDeviceBody
         * @param resetDeviceBody ResetDeviceBody
         * @return Builder
         */
        public Builder setResetDeviceBody(@StringRes int resetDeviceBody){
            this.resetDeviceBody = resetDeviceBody;
            return this;
        }

        /**
         * Setter for ResetDeviceImage
         * @param resetDeviceImage ResetDeviceImage
         * @return Builder
         */
        public Builder setResetDeviceImage(@DrawableRes int resetDeviceImage){
            this.resetDeviceImage = resetDeviceImage;
            return this;
        }

        /**
         * Setter for SetUpAccessPointTitle
         * @param setUpAccessPointTitle SetUpAccessPointTitle
         * @return Builder
         */
        public Builder setSetUpAccessPointTitle(@StringRes int setUpAccessPointTitle) {
            this.setUpAccessPointTitle = setUpAccessPointTitle;
            return this;
        }

        /**
         * Setter for SetUpAccessPointBody
         * @param setUpAccessPointBody SetUpAccessPointBody
         * @return Builder
         */
        public Builder setSetUpAccessPointBody(@StringRes int setUpAccessPointBody) {
            this.setUpAccessPointBody = setUpAccessPointBody;
            return this;
        }

        /**
         * Setter for SetUpAccessPointImage
         * @param setUpAccessPointImage SetUpAccessPointImage
         * @return Builder
         */
        public Builder setSetUpAccessPointImage(@DrawableRes int setUpAccessPointImage) {
            this.setUpAccessPointImage = setUpAccessPointImage;
            return this;
        }

        /**
         * Setter for ConnectWrongPhoneTitle
         * @param connectWrongPhoneTitle ConnectWrongPhoneTitle
         * @return Builder
         */
        public Builder setConnectWrongPhoneTitle(@StringRes int connectWrongPhoneTitle) {
            this.connectWrongPhoneTitle = connectWrongPhoneTitle;
            return this;
        }

        /**
         * Setter for ConnectWrongPhoneBody
         * @param connectWrongPhoneBody ConnectWrongPhoneBody
         * @return Builder
         */
        public Builder setConnectWrongPhoneBody(@StringRes int connectWrongPhoneBody) {
            this.connectWrongPhoneBody = connectWrongPhoneBody;
            return this;
        }

        /**
         * Setter for ConnectWrongPhoneImage
         * @param connectWrongPhoneImage ConnectWrongPhoneImage
         * @return Builder
         */
        public Builder setConnectWrongPhoneImage(@DrawableRes int connectWrongPhoneImage) {
            this.connectWrongPhoneImage = connectWrongPhoneImage;
            return this;
        }

        /**
         * Setter for ConnectWrongPhoneQuestion
         * @param connectWrongPhoneQuestion ConnectWrongPhoneQuestion
         * @return Builder
         */
        public Builder setConnectWrongPhoneQuestion(@StringRes int connectWrongPhoneQuestion) {
            this.connectWrongPhoneQuestion = connectWrongPhoneQuestion;
            return this;
        }

        /**
         * Return TroubleShootContentConfiguration.
         * @return TroubleShootContentConfiguration
         */
        public TroubleShootContentConfiguration build(){
            return new TroubleShootContentConfiguration(resetConnectionTitle, resetConnectionBody, resetConnectionImage, resetDeviceTitle, resetDeviceBody,
                    resetDeviceImage, setUpAccessPointTitle, setUpAccessPointBody, setUpAccessPointImage, connectWrongPhoneTitle, connectWrongPhoneBody,
                    connectWrongPhoneImage, connectWrongPhoneQuestion);
        }
    }
}
