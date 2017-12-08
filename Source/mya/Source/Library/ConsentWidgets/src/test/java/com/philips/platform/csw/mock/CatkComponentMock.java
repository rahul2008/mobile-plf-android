/*
 * Copyright (c) 2017 Koninklijke Philips N.V.
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */

package com.philips.platform.csw.mock;

import android.content.Context;

import com.philips.cdp.registration.User;
import com.philips.platform.appinfra.logging.LoggingInterface;
import com.philips.platform.appinfra.rest.RestInterface;
import com.philips.platform.appinfra.servicediscovery.ServiceDiscoveryInterface;
import com.philips.platform.catk.ConsentAccessToolKit;
import com.philips.platform.catk.injection.CatkComponent;
import com.philips.platform.catk.NetworkController;

import java.util.Map;

public class CatkComponentMock implements CatkComponent {

    public ServiceDiscoveryInterfaceMock getServiceDiscoveryInterface_return;

    public LoggingInterface  getLoggingInterface_return;

    public User getUser_return;

    public CatkComponentMock() {
        getServiceDiscoveryInterface_return = new ServiceDiscoveryInterfaceMock();
    }

    @Override
    public Context context() {
        return null;
    }

    @Override
    public LoggingInterface getLoggingInterface() {
        return new LoggingInterface() {
            @Override
            public LoggingInterface createInstanceForComponent(String s, String s1) {
                return this;
            }

            @Override
            public void log(LogLevel logLevel, String s, String s1) {

            }

            @Override
            public void log(LogLevel logLevel, String s, String s1, Map<String, ?> map) {

            }
        };
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
    public void inject(ConsentAccessToolKit consentAccessToolKit) {

    }

    @Override
    public ServiceDiscoveryInterface getServiceDiscoveryInterface() {
        return getServiceDiscoveryInterface_return;
    }
}
