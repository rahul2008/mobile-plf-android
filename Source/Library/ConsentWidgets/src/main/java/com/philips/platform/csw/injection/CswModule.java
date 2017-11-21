package com.philips.platform.csw.injection;

import android.content.Context;

import com.philips.platform.catk.ConsentAccessToolKit;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class CswModule {
    private final Context context;

    public CswModule(Context context) {
        this.context = context;
    }

    @Singleton
    @Provides
    public Context provideAppContext() {
        return context;
    }
}