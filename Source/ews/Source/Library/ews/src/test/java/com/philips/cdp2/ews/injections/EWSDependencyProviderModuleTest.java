/**
 * Copyright (c) Koninklijke Philips N.V., 2017.
 * All rights reserved.
 */
package com.philips.cdp2.ews.injections;

import com.philips.cdp2.ews.microapp.EWSUapp;
import com.philips.platform.appinfra.AppInfraInterface;
import com.philips.platform.appinfra.logging.LoggingInterface;
import com.philips.platform.appinfra.tagging.AppTaggingInterface;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.Mock;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class EWSDependencyProviderModuleTest {


    private EWSDependencyProviderModule subject;

    @Mock
    private AppInfraInterface appInfraInterfaceMock;

    @Mock
    private AppTaggingInterface mockAppTaggingInterface;

    @Mock
    private LoggingInterface mockLoggingInterface;

    private Map<String, String> productKeyMap;
    @Rule
    public ExpectedException thrownException = ExpectedException.none();

    @Before
    public void setUp() throws Exception {
        initMocks(this);
        productKeyMap = new HashMap<>();
        productKeyMap.put(EWSUapp.PRODUCT_NAME, "product");
        when(appInfraInterfaceMock.getTagging()).thenReturn(mockAppTaggingInterface);
        when(appInfraInterfaceMock.getLogging()).thenReturn(mockLoggingInterface);
        subject = new EWSDependencyProviderModule(appInfraInterfaceMock, productKeyMap);
    }


    @Test
    public void itShouldCheckIfProductMapIsNullForEWSDependencyProviderModule() throws Exception {
        thrownException.expect(IllegalArgumentException.class);
        thrownException.expectMessage("productKeyMap does not contain the productName");
        productKeyMap = new HashMap<>();
        subject = new EWSDependencyProviderModule(appInfraInterfaceMock, productKeyMap);
    }


    @Test
    public void itShouldProvideEWSTagger() throws Exception {
        when(appInfraInterfaceMock.getTagging()).thenReturn(mockAppTaggingInterface);
        subject.provideEWSTagger();
        verify(appInfraInterfaceMock.getTagging()).createInstanceForComponent("EasyWifiSetupTagger", "1.0.0");
    }


    @Test
    public void itShouldProvideEWSLogger() throws Exception {
        when(appInfraInterfaceMock.getLogging()).thenReturn(mockLoggingInterface);
        subject.provideEWSLogger();
        verify(appInfraInterfaceMock.getLogging()).createInstanceForComponent("EasyWifiSetupLogger", "1.0.0");
    }

    @Test
    public void itShouldProvideProductName() throws Exception {
        assertEquals(subject.provideProductName(), "product");
    }

    @Test
    public void itShouldThrowExceptionWhenProvideProductNameCalled() throws Exception {
        subject.productKeyMap = null;
        thrownException.expect(IllegalStateException.class);
        thrownException.expectMessage("Product keymap not initialized");

        assertNotNull(subject.provideProductName());
    }

}