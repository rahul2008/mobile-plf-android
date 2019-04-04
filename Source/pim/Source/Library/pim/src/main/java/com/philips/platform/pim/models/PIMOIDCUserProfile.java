package com.philips.platform.pim.models;

import com.philips.platform.appinfra.securestorage.SecureStorageInterface;
import com.philips.platform.pif.DataInterface.USR.UserProfileInterface;

import net.openid.appauth.AuthState;

import java.util.ArrayList;
import java.util.Date;
import java.util.Map;

public class PIMOIDCUserProfile implements UserProfileInterface {

    private Map<String, String> mUserProfileMap;
    private String profileJson;
    private AuthState authState;

    //AuthState can be null.
    public PIMOIDCUserProfile(SecureStorageInterface secureStorageInterface, AuthState authState) {
        this.authState = authState;
        //get data from Secure Storage
        mUserProfileMap = (Map<String, String>) secureStorageInterface.getKey("UserProfile", null);

    }

    //TODO: Fill user profile map information to all values
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

    Map<String, String> fetchUserDetails(ArrayList<String> stringArrayList) {

        //Get data from user profile map
        return null;
    }
}
