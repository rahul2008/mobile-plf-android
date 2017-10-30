package com.philips.platform.catk.injection;

import android.content.Context;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class CatkModule {
    private final Context context;

    public CatkModule(Context context) {
        this.context = context;
    }

    @Singleton
    @Provides
    public Context provideAppContext() {
        return context;
    }
}