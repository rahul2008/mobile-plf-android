package com.philips.cdp.registration.injection;

import android.content.Context;

import com.philips.cdp.registration.User;
import com.philips.cdp.registration.update.UpdateUserProfile;
import com.philips.cdp.registration.update.UpdateJanRainUserProfile;

import dagger.Module;
import dagger.Provides;

@Module
public class UserModule {

    private final Context context;

    public UserModule(Context context) {
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
}
