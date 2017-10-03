/**
 * Copyright (c) Koninklijke Philips N.V., 2017.
 * All rights reserved.
 */
package com.philips.cdp2.ews.microapp;

import com.philips.cdp.dicommclient.discovery.DiscoveryManager;
import com.philips.platform.appinfra.AppInfraInterface;

import org.junit.Test;

import java.util.Map;

import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.mock;

public class EWSDependenciesTest {

    @Test
    public void shouldInitEWSDependencies() throws Exception {
        final AppInfraInterface appInfraMock = mock(AppInfraInterface.class);
        final DiscoveryManager discoveryManagerMock = mock(DiscoveryManager.class);
        final Map<String, String> productKeyMap = mock(Map.class);

        EWSDependencies dependencies = new EWSDependencies(appInfraMock, discoveryManagerMock, productKeyMap);
        assertSame(appInfraMock, dependencies.getAppInfra());
        assertSame(discoveryManagerMock, dependencies.getDiscoveryManager());
        assertSame(productKeyMap, dependencies.getProductKeyMap());
    }
}