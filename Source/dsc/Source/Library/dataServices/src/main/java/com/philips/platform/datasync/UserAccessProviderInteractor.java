/*
 * Copyright (c) 2017 Koninklijke Philips N.V.
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */

package com.philips.platform.datasync;

public class UserAccessProviderInteractor implements UserAccessProvider {
    private UCoreAccessProvider accessProvider;

    public UserAccessProviderInteractor(final UCoreAccessProvider accessProvider) {

        this.accessProvider = accessProvider;
    }

    @Override
    public boolean isLoggedIn() {
        return accessProvider.isLoggedIn();
    }
}
