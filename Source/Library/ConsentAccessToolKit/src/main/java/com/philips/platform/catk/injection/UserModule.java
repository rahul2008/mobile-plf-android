package com.philips.platform.catk.injection;

import com.philips.cdp.registration.User;

import dagger.Module;
import dagger.Provides;

/**
 * Created by philips on 10/30/17.
 */

@Module
public class UserModule {

    private final User user;

    public UserModule(User user) {
        this.user = user;
    }

    @Provides
    public User providesUser() {
        return user;
    }
}
