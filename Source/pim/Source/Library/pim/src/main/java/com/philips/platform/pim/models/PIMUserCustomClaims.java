package com.philips.platform.pim.models;

import android.os.Parcel;
import android.os.Parcelable;

public class PIMUserCustomClaims implements Parcelable {
    private String uuid;
    private String social_profiles;
    private boolean consent_email_marketing;
    private String consent_email_marketing_timestamp;
    private boolean consent_terms_and_conditions;
    private String consent_terms_and_conditions_timestamp;

    protected PIMUserCustomClaims(Parcel in) {
        uuid = in.readString();
        social_profiles = in.readString();
        consent_email_marketing = in.readByte() != 0;
        consent_email_marketing_timestamp = in.readString();
        consent_terms_and_conditions = in.readByte() != 0;
        consent_terms_and_conditions_timestamp = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(uuid);
        dest.writeString(social_profiles);
        dest.writeByte((byte) (consent_email_marketing ? 1 : 0));
        dest.writeString(consent_email_marketing_timestamp);
        dest.writeByte((byte) (consent_terms_and_conditions ? 1 : 0));
        dest.writeString(consent_terms_and_conditions_timestamp);
    }

    @Override
    public int describeContents() {
        return 0;
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

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getSocial_profiles() {
        return social_profiles;
    }

    public void setSocial_profiles(String social_profiles) {
        this.social_profiles = social_profiles;
    }

    public boolean isConsent_email_marketing() {
        return consent_email_marketing;
    }

    public void setConsent_email_marketing(boolean consent_email_marketing) {
        this.consent_email_marketing = consent_email_marketing;
    }

    public String getConsent_email_marketing_timestamp() {
        return consent_email_marketing_timestamp;
    }

    public void setConsent_email_marketing_timestamp(String consent_email_marketing_timestamp) {
        this.consent_email_marketing_timestamp = consent_email_marketing_timestamp;
    }

    public boolean isConsent_terms_and_conditions() {
        return consent_terms_and_conditions;
    }

    public void setConsent_terms_and_conditions(boolean consent_terms_and_conditions) {
        this.consent_terms_and_conditions = consent_terms_and_conditions;
    }

    public String getConsent_terms_and_conditions_timestamp() {
        return consent_terms_and_conditions_timestamp;
    }

    public void setConsent_terms_and_conditions_timestamp(String consent_terms_and_conditions_timestamp) {
        this.consent_terms_and_conditions_timestamp = consent_terms_and_conditions_timestamp;
    }
}
