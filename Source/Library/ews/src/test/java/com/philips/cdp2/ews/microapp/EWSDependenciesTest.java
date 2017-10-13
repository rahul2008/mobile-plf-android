/**
 * Copyright (c) Koninklijke Philips N.V., 2017.
 * All rights reserved.
 */
package com.philips.cdp2.ews.microapp;

import com.philips.platform.appinfra.AppInfraInterface;

import org.junit.Test;

import java.util.Map;

import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.mock;

public class EWSDependenciesTest {

    @Test
    public void shouldInitEWSDependencies() throws Exception {
        final AppInfraInterface appInfraMock = mock(AppInfraInterface.class);
        final Map<String, String> productKeyMap = mock(Map.class);

        EWSDependencies dependencies = new EWSDependencies(appInfraMock, productKeyMap);
        assertSame(appInfraMock, dependencies.getAppInfra());
        assertSame(productKeyMap, dependencies.getProductKeyMap());
    }
}