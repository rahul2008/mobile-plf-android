package com.philips.platform.pim.models;

import com.philips.platform.appinfra.securestorage.SecureStorageInterface;
import com.philips.platform.pif.DataInterface.USR.UserProfileInterface;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class PIMOIDCUserProfile implements UserProfileInterface {

    private HashMap<String, Object> mUserProfileMap;
    private String profileJson;

    public PIMOIDCUserProfile(SecureStorageInterface pSecureStorageInterface,HashMap<String, Object> pUserProfileMap) {
        mUserProfileMap = pUserProfileMap;
        //get data from Secure Storage
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
    public Map getAddress() {
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

    String getReceiveMarketingEmail() {
        return null;
    }

    Date getReceiveMarketingEmailGivenTimestamp() {
        return null;
    }

}
