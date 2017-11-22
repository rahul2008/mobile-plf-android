/*
 *  Copyright (c) Koninklijke Philips N.V., 2017
 *  All rights are reserved. Reproduction or dissemination
 *  in whole or in part is prohibited without the prior written
 *  consent of the copyright holder.
 */

package com.philips.platform.appframework.connectivitypowersleep;

import com.philips.cdp.dicommclient.request.Error;
import com.philips.platform.appframework.connectivity.appliance.PortDataCallback;
import com.philips.platform.appframework.connectivity.appliance.RefAppBleReferenceAppliance;
import com.philips.platform.appframework.connectivitypowersleep.datamodels.SessionInfoPortProperties;
import com.philips.platform.appframework.connectivitypowersleep.datamodels.SessionsOldestToNewest;

import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyBoolean;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class SynchronizeSessionsUsecaseTest {

    public static final long START = 7L;
    public static final long END = 10L;

    @Mock AllSessionsProviderFactory mockAllSessionsProviderFactory;
    @Mock AllSessionsProvider mockAllSessionProvider;
    @Mock
    RefAppBleReferenceAppliance mockAppliance;
    @Mock SynchronizeSessionsUsecase.Callback mockCallback;
    @Mock
    SessionInfoPortProperties mockPortProperties;

    @Captor ArgumentCaptor<PortDataCallback<SessionInfoPortProperties>> portDataCallbackArgumentCaptor;
    @Captor ArgumentCaptor<AllSessionsProvider.Callback> allSessionProviderCaptorCallback;

    @InjectMocks
    SynchronizeSessionsUsecase subject;
    private DateTime latestMomentDateTime;

    @Before
    public void setUp() throws Exception {
        when(mockAllSessionsProviderFactory.createAllSessionProvider(eq(mockAppliance))).thenReturn(mockAllSessionProvider);

        when(mockPortProperties.getNewestSession()).thenReturn(END);
        when(mockPortProperties.getOldestSession()).thenReturn(START);
        latestMomentDateTime = new DateTime();
    }

    @Test
    public void itShouldSyncSessionInfo() throws Exception {
        subject.execute(mockAppliance, mockCallback, new DateTime());

        verify(mockAppliance).syncSessionInfo();
    }

    @Test
    public void itShouldReportErrorToCallbackWhenErrorOccuredWhileFetchingSessionInfo() throws Exception {
        subject.execute(mockAppliance, mockCallback, new DateTime());

        verify(mockAppliance).registerSessionInfoCallback(portDataCallbackArgumentCaptor.capture());
        portDataCallbackArgumentCaptor.getValue().onError(Error.CANNOT_CONNECT);

        verify(mockCallback).onError(any(Exception.class));
        verify(mockAppliance).unregisterSessionInfoCallback();
    }

    @Test
    public void itShouldReportErrorToCallbackWhenSessionInfoPortPropertiesAreNull() throws Exception {
        subject.execute(mockAppliance, mockCallback, new DateTime());

        verify(mockAppliance).registerSessionInfoCallback(portDataCallbackArgumentCaptor.capture());
        portDataCallbackArgumentCaptor.getValue().onDataReceived(null);

        verify(mockCallback).onError(any(Exception.class));
        verify(mockAppliance).unregisterSessionInfoCallback();
    }

    @Test
    public void itShouldReportErrorToCallbackWhenPortPropertiesAreNotComplete() throws Exception {
        subject.execute(mockAppliance, mockCallback, new DateTime());
        when(mockPortProperties.getNewestSession()).thenReturn(null);
        when(mockPortProperties.getOldestSession()).thenReturn(null);

        simulateSessionInfo();

        verify(mockCallback).onError(any(Exception.class));
    }

    @Test
    public void itShouldFetchSessionWhenSessionInforIsFetchSucceefully() throws Exception {
        subject.execute(mockAppliance, mockCallback, latestMomentDateTime);

        simulateSessionInfo();

        verify(mockAllSessionProvider).fetchAllSessionData(eq(END), eq(START), eq(false), eq(latestMomentDateTime), any(AllSessionsProvider.Callback.class));
        verify(mockAppliance).unregisterSessionInfoCallback();
    }

    @Test
    public void itShouldPassEmptyTrueWhenSessionInfoReturnsEmpty() throws Exception {
        when(mockPortProperties.isEmpty()).thenReturn(true);


        subject.execute(mockAppliance, mockCallback, latestMomentDateTime);
        simulateSessionInfo();

        verify(mockAllSessionProvider).fetchAllSessionData(eq(END), eq(START),
                eq(true), eq(latestMomentDateTime), any(AllSessionsProvider.Callback.class));
    }

    @Test
    public void itShouldPassEmptyFalseWhenSessionInfoReturnsNotEmpty() throws Exception {
        when(mockPortProperties.isEmpty()).thenReturn(false);

        subject.execute(mockAppliance, mockCallback, latestMomentDateTime);
        simulateSessionInfo();

        verify(mockAllSessionProvider).fetchAllSessionData(eq(END), eq(START),
                eq(false), eq(latestMomentDateTime), any(AllSessionsProvider.Callback.class));
    }

    @Test
    public void itShouldReportToCallbackWhenSessionsAreSynchronized() throws Exception {
        subject.execute(mockAppliance, mockCallback, latestMomentDateTime);

        simulateSessionInfo();
        verify(mockAllSessionProvider).fetchAllSessionData(eq(END), eq(START), eq(false), eq(latestMomentDateTime), allSessionProviderCaptorCallback.capture());
        SessionsOldestToNewest result = new SessionsOldestToNewest();
        allSessionProviderCaptorCallback.getValue().onResult(result);

        verify(mockCallback).onSynchronizeSucceed(result);
    }

    private void simulateSessionInfo() {
        verify(mockAppliance).registerSessionInfoCallback(portDataCallbackArgumentCaptor.capture());
        portDataCallbackArgumentCaptor.getValue().onDataReceived(mockPortProperties);
    }

    @Test
    public void itShouldReportToCallbackWhenSessionsAreNotSynchronized() throws Exception {
        subject.execute(mockAppliance, mockCallback, latestMomentDateTime);

        simulateSessionInfo();
        verify(mockAllSessionProvider).fetchAllSessionData(eq(END), eq(START), anyBoolean(), eq(latestMomentDateTime), allSessionProviderCaptorCallback.capture());
        allSessionProviderCaptorCallback.getValue().onError(new Throwable());

        verify(mockCallback).onError(any(Exception.class));
    }

    @Test
    public void itShouldReportToCallbackWhenNoSessionsAreAvailable() throws Exception {
        when(mockPortProperties.isEmpty()).thenReturn(true);

        subject.execute(mockAppliance, mockCallback, latestMomentDateTime);

        simulateSessionInfo();

        verify(mockAllSessionProvider).fetchAllSessionData(eq(END), eq(START), eq(true), eq(latestMomentDateTime), allSessionProviderCaptorCallback.capture());
        allSessionProviderCaptorCallback.getValue().onDeviceContainsNoSessions();

        verify(mockCallback).onNoNewSessionsAvailable();
    }

    @Test
    public void itShouldStoreSyncDateWhenSessionsAreSynchronized() throws Exception {
        subject.execute(mockAppliance, mockCallback, latestMomentDateTime);

        simulateSessionInfo();
        verify(mockAllSessionProvider).fetchAllSessionData(eq(END), eq(START), eq(false), eq(latestMomentDateTime), allSessionProviderCaptorCallback.capture());
        SessionsOldestToNewest result = new SessionsOldestToNewest();
        allSessionProviderCaptorCallback.getValue().onResult(result);
    }
}