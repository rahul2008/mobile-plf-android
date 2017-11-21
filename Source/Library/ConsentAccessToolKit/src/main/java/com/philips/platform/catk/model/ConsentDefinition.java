package com.philips.platform.catk.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Locale;

public class ConsentDefinition implements Parcelable {
    private String text;
    private String helpText;
    private String type;
    private int version;
    private String locale;

    public ConsentDefinition(String text, String helpText, String type, int version, Locale locale) {
        this.text = text;
        this.helpText = helpText;
        this.type = type;
        this.version = version;
        this.locale = locale.toString().replace('_','-');
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getHelpText() {
        return helpText;
    }

    public void setHelpText(String helpText) {
        this.helpText = helpText;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public String getLocaleString() {
        return locale;
    }

    public void setLocale(String locale) {
        this.locale = locale;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(text);
        parcel.writeString(helpText);
        parcel.writeString(type);
        parcel.writeInt(version);
        parcel.writeString(locale);
    }

    protected ConsentDefinition(Parcel in) {
        text = in.readString();
        helpText = in.readString();
        type = in.readString();
        version = in.readInt();
        locale = in.readString();
    }

    public static final Creator<ConsentDefinition> CREATOR = new Creator<ConsentDefinition>() {
        @Override
        public ConsentDefinition createFromParcel(Parcel in) {
            return new ConsentDefinition(in);
        }

        @Override
        public ConsentDefinition[] newArray(int size) {
            return new ConsentDefinition[size];
        }
    };


}
