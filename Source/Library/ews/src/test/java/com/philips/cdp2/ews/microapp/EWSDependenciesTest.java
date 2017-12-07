/**
 * Copyright (c) Koninklijke Philips N.V., 2017.
 * All rights reserved.
 */
package com.philips.cdp2.ews.microapp;

import com.philips.cdp2.commlib.core.CommCentral;
import com.philips.cdp2.ews.configuration.ContentConfiguration;
import com.philips.platform.appinfra.AppInfraInterface;

import org.junit.Test;
import org.mockito.Mock;

import java.util.Map;

import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.mock;

public class EWSDependenciesTest {

    @Mock
    private CommCentral mockCommCentral;

    @Test
    public void itShouldInitEWSDependencies() throws Exception {
        final AppInfraInterface appInfraMock = mock(AppInfraInterface.class);
        final Map<String, String> productKeyMap = mock(Map.class);
        final ContentConfiguration config = new ContentConfiguration();

        EWSDependencies dependencies = new EWSDependencies(appInfraMock, productKeyMap, config) {
            @Override
            public CommCentral getCommCentral() {
                return mockCommCentral;
            }
        };
        assertSame(appInfraMock, dependencies.getAppInfra());
        assertSame(productKeyMap, dependencies.getProductKeyMap());
        assertSame(config, dependencies.getContentConfiguration());
    }
}