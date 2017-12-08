package com.philips.testing.verticals;

import android.support.annotation.NonNull;

import com.philips.platform.core.datatypes.UserProfile;
import com.philips.platform.datasync.userprofile.UserRegistrationInterface;

/**
 * Created by indrajitkumar on 13/12/16.
 */

public class ErrorHandlerImplTest implements UserRegistrationInterface {
    @Override
    public boolean isUserLoggedIn() {
        return true;
    }

    @NonNull
    @Override
    public String getHSDPAccessToken() {
        return "dfsdfsd3423";
    }

    @NonNull
    @Override
    public UserProfile getUserProfile() {
        return new UserProfile("jhon", "Deo", "jhon.deo@gmail.com", "GUID");
    }

    @Override
    public String getHSDPUrl() {
        return "sdfsd";
    }

    @Override
    public void refreshAccessTokenUsingWorkAround() {

    }
}
