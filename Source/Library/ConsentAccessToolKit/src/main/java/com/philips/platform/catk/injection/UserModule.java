/*
 * Copyright (c) 2017 Koninklijke Philips N.V.
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */

package com.philips.platform.catk.injection;

import com.philips.cdp.registration.User;

import dagger.Module;
import dagger.Provides;

@Module
class UserModule {

    private final User user;

    public UserModule(User user) {
        this.user = user;
    }

    @Provides
    public User providesUser() {
        return user;
    }
}
