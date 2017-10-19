package com.philips.platform.datasync.spy;

import com.philips.platform.datasync.UserAccessProvider;

public class UserAccessProviderSpy implements UserAccessProvider {
    @Override
    public boolean isLoggedIn() {
        return false;
    }
}
