package com.philips.platform.core.events;

import com.philips.platform.core.listeners.RegisterDeviceTokenListener;

public class RegisterDeviceToken extends Event {
    String mDeviceToken;
    String mAppVariant;
    RegisterDeviceTokenListener mRegisterDeviceTokenListener;

    public RegisterDeviceToken(String deviceToken, String appVariant, RegisterDeviceTokenListener registerDeviceTokenListener) {
        mDeviceToken = deviceToken;
        mAppVariant = appVariant;
        mRegisterDeviceTokenListener = registerDeviceTokenListener;
    }

    public RegisterDeviceTokenListener getRegisterDeviceTokenListener() {
        return mRegisterDeviceTokenListener;
    }

    public String getDeviceToken() {
        return mDeviceToken;
    }

    public String getAppVariant() {
        return mAppVariant;
    }
}
