package com.philips.platform.core.events;

import com.philips.platform.core.listeners.RegisterDeviceTokenListener;

public class RegisterDeviceToken extends Event {
    String mDeviceToken;
    String mAppVariant;
    String mProtocolProvider;
    RegisterDeviceTokenListener mRegisterDeviceTokenListener;

    public RegisterDeviceToken(String deviceToken, String appVariant, String protocolProvider, RegisterDeviceTokenListener registerDeviceTokenListener) {
        mDeviceToken = deviceToken;
        mAppVariant = appVariant;
        mProtocolProvider = protocolProvider;
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

    public String getProtocolProvider() {
        return mProtocolProvider;
    }
}
