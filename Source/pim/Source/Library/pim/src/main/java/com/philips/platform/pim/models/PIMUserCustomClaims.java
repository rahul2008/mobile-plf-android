package com.philips.platform.pim.models;

import android.os.Parcel;
import android.os.Parcelable;

public class PIMUserCustomClaims implements Parcelable {
    private String created;
    private String consent_data_transfer;
    private String consent_data_usage;
    private String consent_email_marketing;
    private String consent_terms_and_conditions;
    private String uuid;
    private String locale;

    protected PIMUserCustomClaims(Parcel in) {
        created = in.readString();
        consent_data_transfer = in.readString();
        consent_data_usage = in.readString();
        consent_email_marketing = in.readString();
        consent_terms_and_conditions = in.readString();
        uuid = in.readString();
        locale = in.readString();
    }

    public static final Creator<PIMUserCustomClaims> CREATOR = new Creator<PIMUserCustomClaims>() {
        @Override
        public PIMUserCustomClaims createFromParcel(Parcel in) {
            return new PIMUserCustomClaims(in);
        }

        @Override
        public PIMUserCustomClaims[] newArray(int size) {
            return new PIMUserCustomClaims[size];
        }
    };

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public String getConsent_data_transfer() {
        return consent_data_transfer;
    }

    public void setConsent_data_transfer(String consent_data_transfer) {
        this.consent_data_transfer = consent_data_transfer;
    }

    public String getConsent_data_usage() {
        return consent_data_usage;
    }

    public void setConsent_data_usage(String consent_data_usage) {
        this.consent_data_usage = consent_data_usage;
    }

    public String getConsent_email_marketing() {
        return consent_email_marketing;
    }

    public void setConsent_email_marketing(String consent_email_marketing) {
        this.consent_email_marketing = consent_email_marketing;
    }

    public String getConsent_terms_and_conditions() {
        return consent_terms_and_conditions;
    }

    public void setConsent_terms_and_conditions(String consent_terms_and_conditions) {
        this.consent_terms_and_conditions = consent_terms_and_conditions;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getLocale() {
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
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(created);
        dest.writeString(consent_data_transfer);
        dest.writeString(consent_data_usage);
        dest.writeString(consent_email_marketing);
        dest.writeString(consent_terms_and_conditions);
        dest.writeString(uuid);
        dest.writeString(locale);
    }
}
