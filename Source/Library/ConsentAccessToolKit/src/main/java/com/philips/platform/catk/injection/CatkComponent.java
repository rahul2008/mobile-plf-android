
/*
 * Copyright (c) 2017 Koninklijke Philips N.V.
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */

package com.philips.platform.catk.injection;

import javax.inject.Singleton;

import com.philips.cdp.registration.User;
import com.philips.platform.appinfra.logging.LoggingInterface;
import com.philips.platform.appinfra.rest.RestInterface;
import com.philips.platform.appinfra.servicediscovery.ServiceDiscoveryInterface;
import com.philips.platform.catk.ConsentAccessToolKit;
import com.philips.platform.catk.model.CreateConsentModelRequest;
import com.philips.platform.catk.model.GetConsentsModelRequest;
import com.philips.platform.catk.network.NetworkController;

import android.content.Context;

import dagger.Component;

@Component(modules = { CatkModule.class, UserModule.class })
@Singleton
public interface CatkComponent {
    Context context();

    LoggingInterface getLoggingInterface();

    RestInterface getRestInterface();

    User getUser();

    void inject(NetworkController networkController);

    void inject(CreateConsentModelRequest createConsentModelRequest);

    void inject(GetConsentsModelRequest getConsentsModelRequest);

    void inject(ConsentAccessToolKit consentAccessToolKit);

    ServiceDiscoveryInterface getServiceDiscoveryInterface();
}
