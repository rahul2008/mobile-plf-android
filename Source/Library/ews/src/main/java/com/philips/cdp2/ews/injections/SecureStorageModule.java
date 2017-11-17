/*
 * Copyright (c) Koninklijke Philips N.V., 2017.
 * All rights reserved.
 */

package com.philips.cdp2.ews.injections;

import com.philips.cdp2.ews.microapp.EWSDependencyProvider;
import com.philips.cdp2.ews.util.SecureStorageUtility;
import com.philips.platform.appinfra.securestorage.SecureStorageInterface;

import dagger.Module;
import dagger.Provides;

@Module
public class SecureStorageModule {

    @Provides
    SecureStorageInterface provideSecureStorageInterface() {
        return EWSDependencyProvider.getInstance().getAppInfra().getSecureStorage();
    }

    @Provides
    SecureStorageUtility provideSecureStorageUtility(){
        return new SecureStorageUtility(provideSecureStorageInterface());
    }
}
