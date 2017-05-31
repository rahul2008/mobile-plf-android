package com.philips.platform.baseapp.base;

/**
 * Created by philips on 25/05/17.
 */

public class AppInitializationCallback {

    public interface AppInfraInitializationCallback{
        void onAppInfraInitialization();
    }

    public interface AppStatesInitializationCallback{
        void onAppStatesInitialization();
    }
}
