package com.philips.platform.pim.models;

import android.os.Parcel;
import android.os.Parcelable;

public class PIMUserAddress implements Parcelable {
    private String house_number;
    private String address1;
    private String address2;
    private String address3;
    private String postal_code;
    private String locality;
    private String country;
    private String region;
    private String phone_number_daytime;
    private String phone_number_main;

    protected PIMUserAddress(Parcel in) {
        house_number = in.readString();
        address1 = in.readString();
        address2 = in.readString();
        address3 = in.readString();
        postal_code = in.readString();
        locality = in.readString();
        country = in.readString();
        region = in.readString();
        phone_number_daytime = in.readString();
        phone_number_main = in.readString();
    }

    public static final Creator<PIMUserAddress> CREATOR = new Creator<PIMUserAddress>() {
        @Override
        public PIMUserAddress createFromParcel(Parcel in) {
            return new PIMUserAddress(in);
        }

        @Override
        public PIMUserAddress[] newArray(int size) {
            return new PIMUserAddress[size];
        }
    };

    public String getHouse_number() {
        return house_number;
    }

    public void setHouse_number(String house_number) {
        this.house_number = house_number;
    }

    public String getAddress1() {
        return address1;
    }

    public void setAddress1(String address1) {
        this.address1 = address1;
    }

    public String getAddress2() {
        return address2;
    }

    public void setAddress2(String address2) {
        this.address2 = address2;
    }

    public String getAddress3() {
        return address3;
    }

    public void setAddress3(String address3) {
        this.address3 = address3;
    }

    public String getPostal_code() {
        return postal_code;
    }

    public void setPostal_code(String postal_code) {
        this.postal_code = postal_code;
    }

    public String getLocality() {
        return locality;
    }

    public void setLocality(String locality) {
        this.locality = locality;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getPhone_number_daytime() {
        return phone_number_daytime;
    }

    public void setPhone_number_daytime(String phone_number_daytime) {
        this.phone_number_daytime = phone_number_daytime;
    }

    public String getPhone_number_main() {
        return phone_number_main;
    }

    public void setPhone_number_main(String phone_number_main) {
        this.phone_number_main = phone_number_main;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(house_number);
        dest.writeString(address1);
        dest.writeString(address2);
        dest.writeString(address3);
        dest.writeString(postal_code);
        dest.writeString(locality);
        dest.writeString(country);
        dest.writeString(region);
        dest.writeString(phone_number_daytime);
        dest.writeString(phone_number_main);
    }
}
