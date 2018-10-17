package com.philips.cdp.registration.configuration;

import com.philips.platform.appinfra.appidentity.AppIdentityInterface;

public class MockAppIdentityInterface implements AppIdentityInterface {
    @Override
    public String getAppName() {
        return null;
    }

    @Override
    public String getAppVersion() {
        return null;
    }

    @Override
    public AppState getAppState() {
        return null;
    }

    @Override
    public String getLocalizedAppName() {
        return null;
    }

    @Override
    public String getMicrositeId() {
        return "qhq9jvkx35q8duef2fh6wwzceujjs9gs";
    }

    @Override
    public String getSector() {
        return null;
    }

    @Override
    public String getServiceDiscoveryEnvironment() {
        return null;
    }

    @Override
    public void validateServiceDiscoveryEnv(String serviceDiscoveryEnvironment) {

    }

    @Override
    public void validateMicrositeId(String micrositeId) {

    }
}
