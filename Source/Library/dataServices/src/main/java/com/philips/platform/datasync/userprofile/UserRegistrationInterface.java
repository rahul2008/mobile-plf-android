package com.philips.platform.datasync.userprofile;

import android.support.annotation.NonNull;

import com.philips.platform.core.datatypes.UserProfile;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public interface UserRegistrationInterface {

    boolean isUserLoggedIn();

    @NonNull
    String getHSDPAccessToken();

    @NonNull
    UserProfile getUserProfile();

    String getHSDPUrl();

    void refreshAccessTokenUsingWorkAround();


}