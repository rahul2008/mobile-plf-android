package com.philips.platform.datasevices.injection;

import android.content.Context;
import android.support.annotation.NonNull;

import com.philips.cdp.registration.User;
import com.philips.cdp.registration.configuration.RegistrationConfiguration;
import com.philips.cdp.registration.hsdp.HsdpUser;
import com.philips.cdp.registration.settings.RegistrationHelper;
import com.philips.platform.datasevices.registration.UserRegistrationFacadeImpl;
import com.philips.platform.datasync.userprofile.UserRegistrationFacade;

import dagger.Module;
import dagger.Provides;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
@Module
public class RegistrationModule {

    @Provides
    User getUser(@NonNull final Context context) {
        return new User(context);
    }

    @Provides
    HsdpUser getHsdpUser(@NonNull final Context context) {
        return new HsdpUser(context);
    }

    @Provides
    RegistrationHelper providesRegistrationHelper() {
        return RegistrationHelper.getInstance();
    }

    @Provides
    UserRegistrationFacade providesUserRegistrationFacade(@NonNull UserRegistrationFacadeImpl userRegistrationFacade) {
        return userRegistrationFacade;
    }

    @Provides
    RegistrationConfiguration providesRegistrationConfiguration() {
        return RegistrationConfiguration.getInstance();
    }
}
