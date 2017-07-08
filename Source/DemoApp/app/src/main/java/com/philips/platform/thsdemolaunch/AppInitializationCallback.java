package com.philips.platform.thsdemolaunch;

public class AppInitializationCallback {

    public interface AppInfraInitializationCallback{
        void onAppInfraInitialization();
    }

    public interface AppStatesInitializationCallback{
        void onAppStatesInitialization();
    }
}