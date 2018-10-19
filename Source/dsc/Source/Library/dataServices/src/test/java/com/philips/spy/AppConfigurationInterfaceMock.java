package com.philips.spy;

import com.philips.platform.appinfra.appconfiguration.AppConfigurationInterface;

public class AppConfigurationInterfaceMock implements AppConfigurationInterface {
    @Override
    public Object getPropertyForKey(final String key, final String group, final AppConfigurationError configError) throws IllegalArgumentException {
        return null;
    }

    @Override
    public boolean setPropertyForKey(final String key, final String group, final Object object, final AppConfigurationError configError) throws IllegalArgumentException {
        return false;
    }

    @Override
    public Object getDefaultPropertyForKey(final String key, final String group, final AppConfigurationError configError) throws IllegalArgumentException {
        return null;
    }

    @Override
    public void refreshCloudConfig(final OnRefreshListener onRefreshListener) {

    }

    @Override
    public void resetConfig() {

    }
}
