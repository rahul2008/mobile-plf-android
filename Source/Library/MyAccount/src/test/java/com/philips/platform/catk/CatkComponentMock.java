package com.philips.platform.catk;

import android.content.Context;

import com.philips.cdp.registration.User;
import com.philips.platform.appinfra.logging.LoggingInterface;
import com.philips.platform.appinfra.rest.RestInterface;
import com.philips.platform.appinfra.servicediscovery.ServiceDiscoveryInterface;
import com.philips.platform.catk.injection.CatkComponent;
import com.philips.platform.catk.dto.CreateConsentModelRequest;
import com.philips.platform.catk.dto.GetConsentsModelRequest;
import com.philips.platform.catk.network.NetworkController;

public class CatkComponentMock implements CatkComponent {

    public User getUser_return;

    public CatkComponentMock() {
    }

    @Override
    public Context context() {
        return null;
    }

    @Override
    public LoggingInterface getLoggingInterface() {
        return null;
    }

    @Override
    public RestInterface getRestInterface() {
        return null;
    }

    @Override
    public User getUser() {
        return getUser_return;
    }

    @Override
    public void inject(NetworkController networkController) {

    }

    @Override
    public void inject(CreateConsentModelRequest createConsentModelRequest) {

    }

    @Override
    public void inject(GetConsentsModelRequest getConsentsModelRequest) {

    }

    @Override
    public void inject(ConsentAccessToolKit consentAccessToolKit) {

    }

    @Override
    public ServiceDiscoveryInterface getServiceDiscoveryInterface() {
        return null;
    }
}
