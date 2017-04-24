package com.philips.platform.referenceapp.interfaces;

/**
 * Created by philips on 20/04/17.
 */

public class RegistrationCallbacks {
    public interface RegisterCallbackListener{
        void onResponse(boolean isRegistered);

        void onError(int errorCode,String errorMessage);
    }

    public interface DergisterCallbackListener{
        void onResponse(boolean isDeRegistered);

        void onError(int errorCode,String errorMessage);
    }
}
