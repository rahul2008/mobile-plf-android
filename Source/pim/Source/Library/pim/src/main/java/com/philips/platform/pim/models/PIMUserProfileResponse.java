package com.philips.platform.pim.models;


import com.philips.platform.pim.utilities.UserCustomClaims;

import java.util.Map;

public class PIMUserProfileResponse {
    private String given_name;
    private String family_name;
    private String email;
    private String gender;
    private String phone_number;
    private String birthday;
    private String email_verified;
    private String phone_number_verified;
    private Map<String, String> address;
    private String sub;
    private UserCustomClaims userClaims;

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

    public UserCustomClaims getUserClaims() {
        return userClaims;
    }
}
