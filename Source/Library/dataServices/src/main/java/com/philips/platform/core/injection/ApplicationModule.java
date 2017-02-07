/*
 * Copyright (c) 2016. Philips Electronics India Ltd
 * All rights reserved. Reproduction in whole or in part is prohibited without
 * the written consent of the copyright holder.
 */

package com.philips.platform.core.injection;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
@Module
public class ApplicationModule {

    private SharedPreferences sharedPreferences;
    private Context context;

    public ApplicationModule(Context context) {
        sharedPreferences = context.getSharedPreferences(context.getPackageName(), Context.MODE_PRIVATE);
        this.context = context;
    }

    @Provides
    @Singleton
    Context providesContext() {
        return context;
    }

    @Provides
    Handler providesHandler() {
        return new Handler();
    }

    @Provides
    @Singleton
    SharedPreferences provideSharedPreferences() {
        return sharedPreferences;
    }
}
