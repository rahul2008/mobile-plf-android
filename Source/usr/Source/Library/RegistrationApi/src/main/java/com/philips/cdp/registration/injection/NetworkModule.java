package com.philips.cdp.registration.injection;

import android.content.Context;

import com.philips.cdp.registration.ui.utils.NetworkUtility;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;


@Module
public class NetworkModule {

    private final Context context;

    public NetworkModule(Context context) {
        this.context = context;
    }

    @Singleton
    @Provides
    public NetworkUtility provideNetworkUtility() {
        return new NetworkUtility(context);
    }
}
