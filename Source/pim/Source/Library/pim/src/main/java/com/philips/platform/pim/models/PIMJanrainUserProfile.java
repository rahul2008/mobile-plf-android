package com.philips.platform.pim.models;

import com.philips.platform.pif.DataInterface.USR.UserProfileInterface;

import java.util.Date;
import java.util.HashMap;

public class PIMJanrainUserProfile implements UserProfileInterface {

    HashMap<String,Object> hsdpUserProfileMap;

    public PIMJanrainUserProfile(HashMap<String,Object> hsdpUserProfileMap) {
        this.hsdpUserProfileMap  = hsdpUserProfileMap;
    }

    @Override
    public String getFirstName() {
        return null;
    }

    @Override
    public String getLastName() {
        return null;
    }

    @Override
    public String getGender() {
        return null;
    }

    @Override
    public String getEmail() {
        return null;
    }

    @Override
    public String getMobileNumber() {
        return null;
    }

    @Override
    public Date getBirthDate() {
        return null;
    }

    @Override
    public String getAddress() {
        return null;
    }

    @Override
    public String getReceiveMarketingEmail() {
        return null;
    }

    @Override
    public String getUUID() {
        return null;
    }

    @Override
    public String getAccessToken() {
        return null;
    }
}
