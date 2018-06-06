package com.philips.platform.csw.injection;

import com.philips.platform.appinfra.AppInfraInterface;
import com.philips.platform.appinfra.consentmanager.ConsentManagerInterface;
import com.philips.platform.appinfra.logging.LoggingInterface;
import com.philips.platform.appinfra.tagging.AppTaggingInterface;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class AppInfraModuleTest {

    @Mock
    private AppInfraInterface appInfraMock;
    @Mock
    private LoggingInterface loggingMock;
    @Mock
    private AppTaggingInterface taggingMock;
    @Mock
    private ConsentManagerInterface consentsMock;

    private AppInfraModule subject;

    @Before
    public void setUp() {
        initMocks(this);
        when(appInfraMock.getLogging()).thenReturn(loggingMock);
        when(appInfraMock.getTagging()).thenReturn(taggingMock);
        when(appInfraMock.getConsentManager()).thenReturn(consentsMock);

        subject = new AppInfraModule(appInfraMock);
    }

    @Test
    public void givenModuleCreated_whenProvidesLoggingInterface_thenShouldAlwaysReturnObject() {
        LoggingInterface result = subject.providesLoggingInterface();
        assertNotNull(result);
    }

    @Test
    public void givenModuleCreated_whenProvidesAppTaggingInterface_thenShouldAlwaysReturnObject() {
        AppTaggingInterface result = subject.providesAppTaggingInterface();
        assertNotNull(result);
    }

    @Test
    public void givenModuleCreated_whenProvidesConsentManagerInterface_thenShouldAlwaysReturnObject() {
        ConsentManagerInterface result = subject.providesConsentManagerInterface();
        assertNotNull(result);
    }
}