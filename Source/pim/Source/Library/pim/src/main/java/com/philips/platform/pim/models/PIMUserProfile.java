package com.philips.platform.pim.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.HashMap;

public class PIMUserProfile implements Parcelable {
    private String given_name;
    private String family_name;
    private String email;
    private String gender;
    private String phone_number;
    private String birthday;
    private boolean email_verified;
    private boolean phone_number_verified;
    private HashMap<String,Object> address;
    private PIMUserCustomClaims userClaims;

    protected PIMUserProfile(Parcel in) {
        given_name = in.readString();
        family_name = in.readString();
        email = in.readString();
        gender = in.readString();
        phone_number = in.readString();
        birthday = in.readString();
        email_verified = in.readByte() != 0;
        phone_number_verified = in.readByte() != 0;
        userClaims = in.readParcelable(PIMUserCustomClaims.class.getClassLoader());
    }

    public PIMUserProfile() {
    }

    public static final Creator<PIMUserProfile> CREATOR = new Creator<PIMUserProfile>() {
        @Override
        public PIMUserProfile createFromParcel(Parcel in) {
            return new PIMUserProfile(in);
        }

        @Override
        public PIMUserProfile[] newArray(int size) {
            return new PIMUserProfile[size];
        }
    };

    public String getGiven_name() {
        return given_name;
    }

    public void setGiven_name(String given_name) {
        this.given_name = given_name;
    }

    public String getFamily_name() {
        return family_name;
    }

    public void setFamily_name(String family_name) {
        this.family_name = family_name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getPhone_number() {
        return phone_number;
    }

    public void setPhone_number(String phone_number) {
        this.phone_number = phone_number;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public boolean isEmail_verified() {
        return email_verified;
    }

    public void setEmail_verified(boolean email_verified) {
        this.email_verified = email_verified;
    }

    public boolean isPhone_number_verified() {
        return phone_number_verified;
    }

    public void setPhone_number_verified(boolean phone_number_verified) {
        this.phone_number_verified = phone_number_verified;
    }

    public HashMap<String, Object> getAddress() {
        return address;
    }

    public void setAddress(HashMap<String, Object> address) {
        this.address = address;
    }

    public PIMUserCustomClaims getUserClaims() {
        return userClaims;
    }

    public void setUserClaims(PIMUserCustomClaims userClaims) {
        this.userClaims = userClaims;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(given_name);
        dest.writeString(family_name);
        dest.writeString(email);
        dest.writeString(gender);
        dest.writeString(phone_number);
        dest.writeString(birthday);
        dest.writeByte((byte) (email_verified ? 1 : 0));
        dest.writeByte((byte) (phone_number_verified ? 1 : 0));
        dest.writeParcelable(userClaims, flags);
    }
}

