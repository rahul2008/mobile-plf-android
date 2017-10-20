/*
 * Copyright (c) 2017 Koninklijke Philips N.V.
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */

package com.philips.platform.datasync.spy;

import com.philips.platform.datasync.UserAccessProvider;


public class UserAccessProviderSpy implements UserAccessProvider {
    public boolean isLoggedIn;

    @Override
    public boolean isLoggedIn() {
        return isLoggedIn;
    }
}
