/*
 * Copyright (c) 2017 Koninklijke Philips N.V.
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */

package com.philips.platform.mya.catk;

import com.philips.platform.appinfra.AppInfraInterface;
import com.philips.platform.appinfra.internationalization.InternationalizationInterface;
import com.philips.platform.mya.catk.listener.ConsentResponseListener;
import com.philips.platform.mya.catk.listener.CreateConsentListener;
import com.philips.platform.mya.catk.mock.LoggingInterfaceMock;
import com.philips.platform.mya.catk.utils.CatkLogger;
import com.philips.platform.pif.chi.FetchConsentTypeStateCallback;
import com.philips.platform.pif.chi.FetchConsentTypesStateCallback;
import com.philips.platform.pif.chi.PostConsentTypeCallback;
import com.philips.platform.pif.chi.datamodel.BackendConsent;
import com.philips.platform.pif.chi.datamodel.ConsentDefinition;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.mockito.Matchers.eq;
import static org.mockito.Matchers.isA;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ConsentInteractorTest {

    @Test
    public void fetchConsentTypeState() throws Exception {
        givenAccessToolkitWithConsentDefinitions();
        whenFetchConsentTypeStateIsCalled();
        thenGetStatusForConsentTypeIsCalled();
    }

    @Test
    public void fetchConsentTypeStates() throws Exception {
        givenAccessToolkitWithConsentDefinitions();
        whenFetchConsentTypeStatesIsCalled();
        thenGetStatusForConsentTypesIsCalled();
    }

    @Test
    public void storeConsentTypeState() throws Exception {
        whenStoreConsentTypeStateIsCalled();
        thenCreateConsentIsCalledOnTheCatk();
    }

    private void givenAccessToolkitWithConsentDefinitions(ConsentDefinition... consentDefinitions) {
        List<ConsentDefinition> givenConsentDefinitions = new ArrayList<>(consentDefinitions.length);
        Collections.addAll(givenConsentDefinitions, consentDefinitions);
        when(mockContentAccessToolkit.getConsentDefinitions()).thenReturn(Arrays.asList(consentDefinitions));
        interactor = new ConsentInteractor(mockContentAccessToolkit);
    }

    private void whenFetchConsentTypeStateIsCalled() {
        interactor.fetchConsentTypeState(CONSENT_TYPE, fetchConsentTypeStateCallback);
    }

    private void whenFetchConsentTypeStatesIsCalled() {
        interactor.fetchConsentTypeStates(Collections.singletonList(CONSENT_TYPE), fetchConsentTypeStatesCallback);
    }

    private void whenStoreConsentTypeStateIsCalled() {
        interactor.storeConsentTypeState(CONSENT_TYPE, true, 1, postConsentTypeCallback);
    }

    private void thenGetStatusForConsentTypeIsCalled() {
        verify(mockContentAccessToolkit).getStatusForConsentType(eq(CONSENT_TYPE), isA(ConsentResponseListener.class));
    }

    private void thenGetStatusForConsentTypesIsCalled() {
        verify(mockContentAccessToolkit).getConsentDetails(isA(ConsentResponseListener.class));
    }

    private void thenCreateConsentIsCalledOnTheCatk() {
        verify(mockCatk).createConsent(captorConsent.capture(), isA(CreateConsentListener.class));
    }

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        CatkLogger.disableLogging();
        CatkLogger.setLoggerInterface(new LoggingInterfaceMock());

        when(mockCatk.getAppInfra()).thenReturn(appInfraMock);
        when(appInfraMock.getInternationalization()).thenReturn(internationalizationMock);

        interactor = new ConsentInteractor(mockCatk);
    }

    @Mock
    private ConsentsClient mockContentAccessToolkit;

    @Mock
    private FetchConsentTypeStateCallback fetchConsentTypeStateCallback;

    @Mock
    private FetchConsentTypesStateCallback fetchConsentTypeStatesCallback;

    @Mock
    private PostConsentTypeCallback postConsentTypeCallback;

    @Mock
    private ConsentsClient mockCatk;

    @Captor
    private ArgumentCaptor<BackendConsent> captorConsent;

    @Mock
    private AppInfraInterface appInfraMock;

    @Mock
    private InternationalizationInterface internationalizationMock;

    private static final String CONSENT_TYPE = "moment";
    private ConsentInteractor interactor;

}