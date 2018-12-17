package com.philips.platform.baseapp.base;

/**
 * Created by philips on 25/05/17.
 */

public interface AppInitializationCallback {

    interface AppInfraInitializationCallback{
        void onAppInfraInitialization();
    }

    interface AppStatesInitializationCallback{
        void onAppStatesInitialization();
    }
}
