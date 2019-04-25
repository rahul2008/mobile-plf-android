package com.philips.platform.pim.models;

import com.philips.platform.pim.utilities.PIMCustomClaims;

import java.util.Map;

// TODO: Deepthi Rename this class as PIMUserProfileResponse
public class PIMUserProfileResponse {
    String given_name;
    String family_name;
    String email;
    String gender;
    String phone_number;
    String birthday;
    String email_verified;
    String phone_number_verified;
    Map<String, String> address;
    String sub;
    PIMCustomClaims userClaims;

    public String getGiven_name() {
        return given_name;
    }

    public String getFamily_name() {
        return family_name;
    }

    public String getEmail() {
        return email;
    }

    public String getGender() {
        return gender;
    }

    public String getPhone_number() {
        return phone_number;
    }

    public String getBirthday() {
        return birthday;
    }

    public String getEmail_verified() {
        return email_verified;
    }

    public String getPhone_number_verified() {
        return phone_number_verified;
    }

    public Map<String, String> getAddress() {
        return address;
    }

    public String getSub() {
        return sub;
    }

    public PIMCustomClaims getUserClaims() {
        return userClaims;
    }
}
