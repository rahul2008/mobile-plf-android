package com.philips.cdp.registration.injection;

import android.content.Context;

import com.philips.cdp.registration.User;
import com.philips.cdp.registration.events.EventHelper;
import com.philips.cdp.registration.settings.RegistrationHelper;
import com.philips.cdp.registration.update.UpdateJanRainUserProfile;
import com.philips.cdp.registration.update.UpdateUserProfile;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class RegistrationModule {

    private final Context context;

    public RegistrationModule(Context context) {
        this.context = context;
    }

    @Provides
    public User providesUser() {
        return new User(context);
    }

    @Provides
    public UpdateUserProfile providesUpdateUserProfile() {
        return new UpdateJanRainUserProfile();
    }

    @Singleton
    @Provides
    public RegistrationHelper providesRegistrationHelper() {
        return RegistrationHelper.getInstance();
    }
    @Singleton
    @Provides
    public EventHelper providesEventHelper() {
        return EventHelper.getInstance();
    }
}
