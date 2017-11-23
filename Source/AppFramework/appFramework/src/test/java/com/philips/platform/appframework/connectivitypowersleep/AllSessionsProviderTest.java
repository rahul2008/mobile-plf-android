/*
 *  Copyright (c) Koninklijke Philips N.V., 2017
 *  All rights are reserved. Reproduction or dissemination
 *  in whole or in part is prohibited without the prior written
 *  consent of the copyright holder.
 */

package com.philips.platform.appframework.connectivitypowersleep;


import com.philips.platform.appframework.connectivity.appliance.RefAppBleReferenceAppliance;
import com.philips.platform.appframework.connectivitypowersleep.datamodels.Session;
import com.philips.platform.appframework.connectivitypowersleep.datamodels.SessionsOldestToNewest;
import com.philips.platform.appframework.connectivitypowersleep.datamodels.Summary;

import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Date;

import static junit.framework.Assert.assertTrue;
import static junit.framework.TestCase.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class AllSessionsProviderTest {

    private static final long NEWEST_SESSION_ID = 10L;
    private static final long OLDEST_SESSION_ID = 8L;

    private static final long INVALID_NEWEST_SESSION_ID = 7L;
    private static final long INVALID_OLDEST_SESSION_ID = 10L;

    @InjectMocks private AllSessionsProvider subject;

    @Mock private RefAppBleReferenceAppliance mockPowerSleepAppliance;
    @Mock private AllSessionsProvider.Callback mockSessionsDataCallback;
    @Mock private SessionProviderFactory mockSessionProviderFactory;
    @Mock private Throwable mockThrowable;
    @Mock private SessionProvider mockSessionProvider1;
    @Mock private SessionProvider mockSessionProvider2;
    @Mock private SessionProvider mockSessionProvider3;

    private Session sleepData1;
    private Session sleepData2;
    private Session sleepData3;

    @Mock private Summary summary1;
    @Mock private Summary summary2;
    @Mock private Summary summary3;
    private DateTime lastestMomentDateTime;

    @Captor private ArgumentCaptor<SessionProvider.Callback> sleepDataCallbackArgumentCaptor;
    @Captor private ArgumentCaptor<SessionsOldestToNewest> listArgumentCaptor;

    @Before
    public void setUp() throws Exception {
        when(summary1.getDate()).thenReturn(new Date(10));
        when(summary2.getDate()).thenReturn(new Date(30));
        when(summary3.getDate()).thenReturn(new Date(40));

        sleepData1 = new Session(summary1);
        sleepData2 = new Session(summary2);
        sleepData3 = new Session(summary3);

        lastestMomentDateTime = new DateTime(Long.MIN_VALUE);

        when(mockSessionProviderFactory.createBleSessionProvider(eq(mockPowerSleepAppliance), eq(10L), any(SessionProvider.Callback.class))).thenReturn(
                mockSessionProvider1);
        when(mockSessionProviderFactory.createBleSessionProvider(eq(mockPowerSleepAppliance), eq(9L), any(SessionProvider.Callback.class))).thenReturn(
                mockSessionProvider2);
        when(mockSessionProviderFactory.createBleSessionProvider(eq(mockPowerSleepAppliance), eq(8L), any(SessionProvider.Callback.class))).thenReturn(
                mockSessionProvider3);
    }

    @Test
    public void itShouldReportAnEmptyListIfNewestAndOldestValuesAreInvalid() throws Exception {
        subject.fetchAllSessionData(INVALID_NEWEST_SESSION_ID, INVALID_OLDEST_SESSION_ID, false, lastestMomentDateTime, mockSessionsDataCallback);

        verify(mockSessionsDataCallback).onResult(listArgumentCaptor.capture());
        assertTrue(listArgumentCaptor.getValue().isEmpty());
    }

    @Test
    public void itShouldFetchNewestSessionFirst() throws Exception {
        subject.fetchAllSessionData(NEWEST_SESSION_ID, OLDEST_SESSION_ID, false, lastestMomentDateTime, mockSessionsDataCallback);

        verify(mockSessionProviderFactory).createBleSessionProvider(eq(mockPowerSleepAppliance), eq(NEWEST_SESSION_ID), sleepDataCallbackArgumentCaptor.capture());
        verify(mockSessionProvider1).fetchSession();
    }

    @Test
    public void itShouldNotReportWhenFirstResponseIsReceived() throws Exception {
        itShouldFetchNewestSessionFirst();

        sleepDataCallbackArgumentCaptor.getValue().onSuccess(sleepData1);

        verify(mockSessionsDataCallback, never()).onResult(any(SessionsOldestToNewest.class));
    }

    @Test
    public void itShouldFetchNextWhenFirstSuccessResponseIsReceived() throws Exception {
        itShouldFetchNewestSessionFirst();

        sleepDataCallbackArgumentCaptor.getValue().onSuccess(sleepData1);

        verify(mockSessionProviderFactory).createBleSessionProvider(eq(mockPowerSleepAppliance), eq(NEWEST_SESSION_ID - 1), sleepDataCallbackArgumentCaptor.capture());
        verify(mockSessionProvider2).fetchSession();
    }

    @Test
    public void itShouldRetryFetchingSessionWhenErrorIsReported() throws Exception {
        itShouldFetchNextWhenFirstSuccessResponseIsReceived();

        sleepDataCallbackArgumentCaptor.getValue().onError(mockThrowable);

        verify(mockSessionProvider2, times(1)).fetchSession();
    }

    @Test
    public void itShouldReportErrorWhenErrorWasReported3Times() throws Exception {
        itShouldFetchNextWhenFirstSuccessResponseIsReceived();

        sleepDataCallbackArgumentCaptor.getValue().onError(mockThrowable);

        verify(mockSessionsDataCallback).onError(any(Throwable.class));
    }

    @Test
    public void itShouldResetHelperWhenSessionIsFetchedAfterRetry() throws Exception {
        itShouldFetchNextWhenFirstSuccessResponseIsReceived();

        sleepDataCallbackArgumentCaptor.getValue().onError(mockThrowable);
        sleepDataCallbackArgumentCaptor.getValue().onSuccess(sleepData2);

    }


    @Test
    public void itShouldFetchNextWhenSecondSuccessResponseIsReceived() throws Exception {
        itShouldFetchNextWhenFirstSuccessResponseIsReceived();

        sleepDataCallbackArgumentCaptor.getValue().onSuccess(sleepData2);

        verify(mockSessionProviderFactory).createBleSessionProvider(eq(mockPowerSleepAppliance), eq(NEWEST_SESSION_ID - 2), sleepDataCallbackArgumentCaptor.capture());
        verify(mockSessionProvider3).fetchSession();
    }

    @Test
    public void itShouldReportAllSuccessfulSessionWhenLastResponseIsReceived() throws Exception {
        itShouldFetchNextWhenSecondSuccessResponseIsReceived();

        reset(mockSessionProviderFactory);

        sleepDataCallbackArgumentCaptor.getValue().onSuccess(sleepData3);

        verify(mockSessionProviderFactory, never()).createBleSessionProvider(eq(
                mockPowerSleepAppliance), anyInt(), sleepDataCallbackArgumentCaptor.capture());
        verify(mockSessionsDataCallback).onResult(listArgumentCaptor.capture());

        SessionsOldestToNewest resultList = listArgumentCaptor.getValue();
        assertEquals(3, resultList.size());
    }

    @Test
    public void itShouldReportAllSuccessfulSessionWhenASessionIsOlderThanTheLastOneInTheDB() throws Exception {
        lastestMomentDateTime = new DateTime(35);

        itShouldFetchNewestSessionFirst();

        sleepDataCallbackArgumentCaptor.getValue().onSuccess(sleepData3);
        sleepDataCallbackArgumentCaptor.getValue().onSuccess(sleepData2);

        verify(mockSessionsDataCallback).onResult(listArgumentCaptor.capture());

        SessionsOldestToNewest resultList = listArgumentCaptor.getValue();
        assertEquals(1, resultList.size());
    }

    @Test
    public void itShouldReportAllSuccessfulSessionWhenASessionIsTheSameAsTheLastOneInTheDB() throws Exception {
        lastestMomentDateTime = new DateTime(30);

        itShouldFetchNewestSessionFirst();

        sleepDataCallbackArgumentCaptor.getValue().onSuccess(sleepData3);
        sleepDataCallbackArgumentCaptor.getValue().onSuccess(sleepData2);

        verify(mockSessionsDataCallback).onResult(listArgumentCaptor.capture());

        SessionsOldestToNewest resultList = listArgumentCaptor.getValue();
        assertEquals(1, resultList.size());
    }

    @Test
    public void itShouldReportOnlyNewResultWhenFetchAllSessionsIsCalledForTheSecondTime() throws Exception {
        // First syncing succeeding with 3 sessions
        itShouldReportAllSuccessfulSessionWhenLastResponseIsReceived();

        // Second syncing succeeding with 1 session
        when(mockSessionProviderFactory.createBleSessionProvider(eq(mockPowerSleepAppliance), eq(0L), any(SessionProvider.Callback.class))).thenReturn(
                mockSessionProvider1);
        subject.fetchAllSessionData(0L, 0L, false, lastestMomentDateTime, mockSessionsDataCallback);
        verify(mockSessionsDataCallback).onResult(listArgumentCaptor.capture());
        sleepDataCallbackArgumentCaptor.getValue().onSuccess(sleepData1);

        assertEquals(1, listArgumentCaptor.getValue().size());
    }

    @Test
    public void itShouldReportErrorWhenOldestSessionEqualsNewestSessionAndTheyAreNotZero() throws Exception {
        // Rationale -> oldest:0 && newest:0 -> we have 1 session
        //              oldest:Integer.MAX && newest:Integer.MAX -> device is empty
        subject.fetchAllSessionData(1L, 1L, true, lastestMomentDateTime, mockSessionsDataCallback);
        verify(mockSessionsDataCallback, never()).onResult(listArgumentCaptor.capture());
        verify(mockSessionsDataCallback).onDeviceContainsNoSessions();
    }

    @Test
    public void itShouldReturnSortedList() throws Exception {
        itShouldReportAllSuccessfulSessionWhenLastResponseIsReceived();

        SessionsOldestToNewest resultList = listArgumentCaptor.getValue();
        assertEquals(sleepData1, resultList.get(0));
        assertEquals(sleepData2, resultList.get(1));
        assertEquals(sleepData3, resultList.get(2));
    }

    @Test
    public void itShouldFetchNextWhenSecondOnSuccessForEmptyOrInvalidTimeSessionResponseIsReceived() throws Exception {
        itShouldFetchNewestSessionFirst();

        sleepDataCallbackArgumentCaptor.getValue().onSuccessForEmptyOrInvalidTimeSession();

        verify(mockSessionProviderFactory).createBleSessionProvider(eq(mockPowerSleepAppliance), eq(NEWEST_SESSION_ID - 1), sleepDataCallbackArgumentCaptor.capture());
        verify(mockSessionProvider2).fetchSession();
    }

    @Test
    public void itShouldFetchThirdWhenSecondOnSuccessForEmptyOrInvalidTimeSessionResponseIsReceived() throws Exception {
        itShouldFetchNextWhenSecondOnSuccessForEmptyOrInvalidTimeSessionResponseIsReceived();


        sleepDataCallbackArgumentCaptor.getValue().onSuccess(sleepData2);

        verify(mockSessionProviderFactory).createBleSessionProvider(eq(mockPowerSleepAppliance), eq(NEWEST_SESSION_ID - 2), sleepDataCallbackArgumentCaptor.capture());
        verify(mockSessionProvider3).fetchSession();
    }

    @Test
    public void itShouldExcludeEmptySessionWhenResultIsReported() throws Exception {
        itShouldFetchThirdWhenSecondOnSuccessForEmptyOrInvalidTimeSessionResponseIsReceived();

        reset(mockSessionProviderFactory);

        sleepDataCallbackArgumentCaptor.getValue().onSuccess(sleepData3);

        verify(mockSessionProviderFactory, never()).createBleSessionProvider(eq(
                mockPowerSleepAppliance), anyInt(), sleepDataCallbackArgumentCaptor.capture());
        verify(mockSessionsDataCallback).onResult(listArgumentCaptor.capture());

        SessionsOldestToNewest resultList = listArgumentCaptor.getValue();
        assertEquals(2, resultList.size());
    }
}