package com.philips.cdp2.ews.configuration;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.DrawableRes;
import android.support.annotation.StringRes;

public class HappyFlowContentConfiguration implements Parcelable{

    //content used for screen: EWS_01
    @StringRes private int ews01_title;

    //content used for screen: EWS_02_01_setup
    @StringRes private int ews02_01_title;
    @StringRes private int ews02_01_body;
    @DrawableRes private int ews02_01_image;

    //content used for screen: EWS_02_02_setup
    @StringRes private int ews02_02_title;
    @StringRes private int ews02_02_subtitle;
    @StringRes private int ews02_02_subtitle2;
    @DrawableRes private int ews02_02_image;
    @StringRes private int ews02_02_positiveBtnText;
    @StringRes private int ews02_02_negativeBtnText;

    public int getEws02_02_title() {
        return ews02_02_title;
    }

    public int getEws02_02_subtitle() {
        return ews02_02_subtitle;
    }

    public int getEws02_02_subtitle2() {
        return ews02_02_subtitle2;
    }

    public int getEws02_02_image() {
        return ews02_02_image;
    }

    public int getEws02_02_positiveBtnText() {
        return ews02_02_positiveBtnText;
    }

    public int getEws02_02_negativeBtnText() {
        return ews02_02_negativeBtnText;
    }

    public int getEws02_01_image() {
        return ews02_01_image;
    }

    public int getEws01_title() {
        return ews01_title;
    }

    public int getEws02_01_title() {
        return ews02_01_title;
    }

    public int getEws02_01_body() {
        return ews02_01_body;
    }


    private HappyFlowContentConfiguration(HappyFlowConfigurationBuilder builder){
        this.ews01_title = builder.ews01_title;
        this.ews02_01_body = builder.ews02_01_body;
        this.ews02_01_title = builder.ews02_01_title;
        this.ews02_01_image = builder.ews02_01_image;
        this.ews02_02_title = builder.ews02_02_title;
        this.ews02_02_subtitle = builder.ews02_02_subtitle;
        this.ews02_02_subtitle2 = builder.ews02_02_subtitle2;
        this.ews02_02_image = builder.ews02_02_image;
        this.ews02_02_positiveBtnText = builder.ews02_02_positiveBtnText;
        this.ews02_02_negativeBtnText = builder.ews02_02_negativeBtnText;
    }

    public static class HappyFlowConfigurationBuilder{

        @StringRes private int ews01_title;
        @StringRes private int ews02_01_title;
        @StringRes private int ews02_01_body;

        @DrawableRes private int ews02_01_image;
        @StringRes private int ews02_02_title;
        @StringRes private int ews02_02_subtitle;
        @StringRes private int ews02_02_subtitle2;
        @DrawableRes private int ews02_02_image;
        @StringRes private int ews02_02_positiveBtnText;
        @StringRes private int ews02_02_negativeBtnText;

        public HappyFlowConfigurationBuilder setEWS_01_Title(@StringRes int ews01_title){
            this.ews01_title = ews01_title;
            return this;
        }

        public HappyFlowConfigurationBuilder setEWS02_01_Title(@StringRes int ews02_01_title){
            this.ews02_01_title = ews02_01_title;
            return this;
        }

        public HappyFlowConfigurationBuilder setEWS02_01_Body(@StringRes int ews02_01_body){
            this.ews02_01_body = ews02_01_body;
            return this;
        }

        public HappyFlowConfigurationBuilder setEWS02_01_image(@DrawableRes int ews02_01_image) {
            this.ews02_01_image = ews02_01_image;
            return this;
        }

        public HappyFlowConfigurationBuilder setEWS02_02_Title(@StringRes int ews02_02_title){
            this.ews02_02_title = ews02_02_title;
            return this;
        }

        public HappyFlowConfigurationBuilder setEWS02_02_Subtitle(@StringRes int ews02_02_subtitle){
            this.ews02_02_subtitle = ews02_02_subtitle;
            return this;
        }

        public HappyFlowConfigurationBuilder setEWS02_02_Subtitle2(@StringRes int ews02_02_subtitle2){
            this.ews02_02_subtitle2 = ews02_02_subtitle2;
            return this;
        }

        public HappyFlowConfigurationBuilder setEWS02_02_image(@DrawableRes int ews02_02_image) {
            this.ews02_02_image = ews02_02_image;
            return this;
        }

        public HappyFlowConfigurationBuilder setEWS02_02_PositiveBtnText(@StringRes int ews02_02_positiveBtnText){
            this.ews02_02_positiveBtnText = ews02_02_positiveBtnText;
            return this;
        }

        public HappyFlowConfigurationBuilder setEWS02_02_NegativeBtnText(@StringRes int ews02_02_negativeBtnText){
            this.ews02_02_negativeBtnText = ews02_02_negativeBtnText;
            return this;
        }

        public HappyFlowContentConfiguration build(){
            return new HappyFlowContentConfiguration(this);
        }
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.ews01_title);
        dest.writeInt(this.ews02_01_title);
        dest.writeInt(this.ews02_01_body);
        dest.writeInt(this.ews02_01_image);
        dest.writeInt(this.ews02_02_title);
        dest.writeInt(this.ews02_02_subtitle);
        dest.writeInt(this.ews02_02_subtitle2);
        dest.writeInt(this.ews02_02_image);
        dest.writeInt(this.ews02_02_positiveBtnText);
        dest.writeInt(this.ews02_02_negativeBtnText);
    }

    protected HappyFlowContentConfiguration(Parcel in) {
        this.ews01_title = in.readInt();
        this.ews02_01_title = in.readInt();
        this.ews02_01_body = in.readInt();
        this.ews02_01_image = in.readInt();
        this.ews02_02_title = in.readInt();
        this.ews02_02_subtitle = in.readInt();
        this.ews02_02_subtitle2 = in.readInt();
        this.ews02_02_image = in.readInt();
        this.ews02_02_positiveBtnText = in.readInt();
        this.ews02_02_negativeBtnText = in.readInt();
    }

    public static final Creator<HappyFlowContentConfiguration> CREATOR = new Creator<HappyFlowContentConfiguration>() {
        @Override
        public HappyFlowContentConfiguration createFromParcel(Parcel source) {
            return new HappyFlowContentConfiguration(source);
        }

        @Override
        public HappyFlowContentConfiguration[] newArray(int size) {
            return new HappyFlowContentConfiguration[size];
        }
    };
}
