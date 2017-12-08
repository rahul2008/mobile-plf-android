package com.philips.platform.core.events;

import com.philips.platform.core.listeners.RegisterDeviceTokenListener;

public class UnRegisterDeviceToken extends Event {
    String mAppToken;
    String mAppVariant;
    RegisterDeviceTokenListener mRegisterDeviceTokenListener;

    public UnRegisterDeviceToken(String appToken, String appVariant, RegisterDeviceTokenListener registerDeviceTokenListener) {
        mAppToken = appToken;
        mAppVariant = appVariant;
        mRegisterDeviceTokenListener = registerDeviceTokenListener;
    }

    public String getAppToken() {
        return mAppToken;
    }

    public String getAppVariant() {
        return mAppVariant;
    }

    public RegisterDeviceTokenListener getRegisterDeviceTokenListener() {
        return mRegisterDeviceTokenListener;
    }

}
