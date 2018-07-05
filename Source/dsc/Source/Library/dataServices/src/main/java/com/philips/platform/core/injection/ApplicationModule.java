/* Copyright (c) Koninklijke Philips N.V., 2017
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */

package com.philips.platform.core.injection;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;

import com.philips.platform.appinfra.AppInfraInterface;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class ApplicationModule {

    private SharedPreferences sharedPreferences;
    private Context context;
    private AppInfraInterface appInfra;

    public ApplicationModule(Context context, AppInfraInterface appInfra) {
        sharedPreferences = context.getSharedPreferences(context.getPackageName(), Context.MODE_PRIVATE);
        this.context = context;
        this.appInfra = appInfra;
    }

    @Provides
    public AppInfraInterface provideAppInfra() { return appInfra; }

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
