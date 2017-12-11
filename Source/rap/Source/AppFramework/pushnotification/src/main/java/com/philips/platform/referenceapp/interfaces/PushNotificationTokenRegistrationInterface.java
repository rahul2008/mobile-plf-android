package com.philips.platform.referenceapp.interfaces;

/**
 * Created by philips on 20/04/17.
 */

public interface PushNotificationTokenRegistrationInterface {
    void registerToken(String deviceToken, String appVariant, String protocolProvider, RegistrationCallbacks.RegisterCallbackListener registerCallbackListener);

    void deregisterToken(String appToken, String appVariant, RegistrationCallbacks.DergisterCallbackListener dergisterCallbackListener);
}
