package com.philips.platform.datasync.userprofile;

import android.support.annotation.NonNull;

import com.philips.platform.core.datatypes.UserCredentials;
import com.philips.platform.core.datatypes.UserProfile;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public interface UserRegistrationFacade {

    void init();

    boolean isUserLoggedIn();

    @NonNull
    String getAccessToken();

    @NonNull
    UserProfile getUserProfile();

    void setUserSkippedOrAddedPhoto();

    void clearUserData();

    boolean isSameUser();

    UserCredentials getUserCredentials();

    void setHsdpUrl();
}