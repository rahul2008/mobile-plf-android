/*
 * Copyright (c) 2017 Koninklijke Philips N.V.
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */

package com.philips.platform.mya.catk.infra;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.verify;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.philips.platform.appinfra.servicediscovery.ServiceDiscoveryInterface;
import com.philips.platform.appinfra.servicediscovery.model.ServiceDiscoveryService;
import com.philips.platform.mya.catk.provider.AppInfraInfo;
import com.philips.platform.mya.catk.provider.ServiceInfoProvider;

/**
 * Created by phnl310185349 on 29/11/2017.
 */
@RunWith(MockitoJUnitRunner.class)
public class InfraServiceInfoProviderTest {

    private InfraServiceInfoProvider givenInfraServiceProvider;
    @Mock
    private ServiceDiscoveryInterface mockServiceDiscover;
    @Mock
    private ServiceInfoProvider.ResponseListener mockResponseListener;
    @Captor
    private ArgumentCaptor<InfraServiceInfoProvider.DiscoveryListener> responseCaptor;
    private Map<String, ServiceDiscoveryService> givenMap;

    @Test
    public void itShouldReportErrorWhenResponseIsEmptyMap() throws Exception {
        givenInfraServiceInfoProvider();
        givenEmptyMap();
        whenRetrievingInfo();
        andSuccessfullResponseIs(givenMap);
        thenOnErrorIsCalled();
    }

    @Test
    public void itShouldReportErrorWhenResponseIsInvalidMap() throws Exception {
        givenInfraServiceInfoProvider();
        givenInvalidMap();
        whenRetrievingInfo();
        andSuccessfullResponseIs(givenMap);
        thenOnErrorIsCalled();
    }

    @Test
    public void itShouldReportErrorWhenResponseIsValidMapWithErrors() throws Exception {
        givenInfraServiceInfoProvider();
        givenValidMapWithError("oops");
        whenRetrievingInfo();
        andSuccessfullResponseIs(givenMap);
        thenOnErrorIsCalled();
    }

    @Test
    public void itShouldReportErrorWhenResponseIsError() throws Exception {
        givenInfraServiceInfoProvider();
        givenEmptyMap();
        whenRetrievingInfo();
        andErrorResponseIs(ServiceDiscoveryInterface.OnErrorListener.ERRORVALUES.NO_NETWORK, "oops");
        thenOnErrorIsCalled();
    }

    @Test
    public void itShouldReportSuccessWhenResponseIsValidMap() throws Exception {
        givenInfraServiceInfoProvider();
        givenValidMap();
        whenRetrievingInfo();
        andSuccessfullResponseIs(givenMap);
        thenOnSuccessIsCalled();
    }

    private void givenInfraServiceInfoProvider() {
        givenInfraServiceProvider = new InfraServiceInfoProvider();
    }

    private void givenEmptyMap() {
        givenMap = new HashMap<>();
    }

    private void givenInvalidMap(){
        givenMap = new HashMap<>();
        givenMap.put("key", new ServiceDiscoveryService());
    }

    private void givenValidMap() {
        givenMap = new HashMap<>();
        ServiceDiscoveryService serviceDiscoveryService = new ServiceDiscoveryService();
        serviceDiscoveryService.init("locale", "config");
        serviceDiscoveryService.setConfigUrl("configUrl");
        givenMap.put("key", serviceDiscoveryService);
    }

    private void givenValidMapWithError(String error) {
        givenMap = new HashMap<>();
        ServiceDiscoveryService serviceDiscoveryService = new ServiceDiscoveryService();
        serviceDiscoveryService.init("locale", "config");
        serviceDiscoveryService.setConfigUrl("configUrl");
        serviceDiscoveryService.setmError(error);
        givenMap.put("key", serviceDiscoveryService);
    }

    private void whenRetrievingInfo() {
        givenInfraServiceProvider.retrieveInfo(mockServiceDiscover, mockResponseListener);
        verify(mockServiceDiscover).getServicesWithCountryPreference(any(ArrayList.class), responseCaptor.capture());
    }

    private void andSuccessfullResponseIs(Map<String, ServiceDiscoveryService> response) {
        responseCaptor.getValue().onSuccess(response);

    }

    private void andErrorResponseIs(ServiceDiscoveryInterface.OnErrorListener.ERRORVALUES errorValues, String error) {
        responseCaptor.getValue().onError(errorValues, error);
    }

    private void thenOnErrorIsCalled() {
        verify(mockResponseListener).onError(anyString());
    }

    private void thenOnSuccessIsCalled() {
        verify(mockResponseListener).onResponse(any(AppInfraInfo.class));
    }

}