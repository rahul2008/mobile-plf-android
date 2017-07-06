package com.philips.platform.pthdemolaunch;

public class AppInitializationCallback {

    public interface AppInfraInitializationCallback{
        void onAppInfraInitialization();
    }

    public interface AppStatesInitializationCallback{
        void onAppStatesInitialization();
    }
}