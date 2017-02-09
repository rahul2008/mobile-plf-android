package com.philips.platform.verticals;

import android.support.annotation.NonNull;

import com.philips.platform.core.datatypes.UserProfile;
import com.philips.platform.datasync.userprofile.UserRegistrationInterface;

/**
 * Created by 310218660 on 1/3/2017.
 */

public class VerticalUserRegistrationInterface implements UserRegistrationInterface {
    @Override
    public boolean isUserLoggedIn() {
        return false;
    }

    @NonNull
    @Override
    public String getHSDPAccessToken() {
        return "";
    }

    @NonNull
    @Override
    public UserProfile getUserProfile() {
        return new UserProfile("spoorti","hallur","spo","hey");
    }

    @Override
    public String getHSDPUrl() {
        return "";
    }

    @Override
    public void refreshAccessTokenUsingWorkAround() {

    }
}
