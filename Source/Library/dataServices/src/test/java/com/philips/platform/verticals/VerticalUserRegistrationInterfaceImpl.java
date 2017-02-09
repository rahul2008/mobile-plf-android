package com.philips.platform.verticals;

import android.content.Context;
import android.support.annotation.NonNull;

import com.philips.platform.core.datatypes.UserProfile;
import com.philips.platform.datasync.userprofile.UserRegistrationInterface;

import org.mockito.Mock;
import org.robolectric.RuntimeEnvironment;

public class VerticalUserRegistrationInterfaceImpl implements UserRegistrationInterface {
    @Override
    public boolean isUserLoggedIn() {
        return false;
    }

    @NonNull
    @Override
    public String getHSDPAccessToken() {
        return "abc";
    }

    @NonNull
    @Override
    public UserProfile getUserProfile() {
        return null;
    }

    @Override
    public String getHSDPUrl() {
        return "http://google.com";
    }

    @Override
    public void refreshAccessTokenUsingWorkAround() {

    }
}
