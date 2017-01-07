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
    String getAccessToken();

    @NonNull
    UserProfile getUserProfile();

    String getHSDHsdpUrl();

    void refreshAccessTokenUsingWorkAround();
}