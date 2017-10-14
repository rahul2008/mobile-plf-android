/*
 *  Copyright (c) Koninklijke Philips N.V., 2017
 *  All rights are reserved. Reproduction or dissemination
 *  in whole or in part is prohibited without the prior written
 *  consent of the copyright holder.
 */

package com.philips.platform.appframework.connectivitypowersleep;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.philips.cdp.dicommclient.port.DICommPort;
import com.philips.cdp.dicommclient.request.Error;
import com.philips.platform.appframework.connectivity.appliance.BleReferenceAppliance;
import com.philips.platform.appframework.connectivity.appliance.PortDataCallback;
import com.philips.platform.appframework.connectivitypowersleep.datamodels.Session;
import com.philips.platform.appframework.connectivitypowersleep.datamodels.SessionDataPortProperties;
import com.philips.platform.appframework.connectivitypowersleep.datamodels.Summary;
import com.philips.platform.appframework.connectivitypowersleep.error.PortErrorException;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.powermock.modules.junit4.PowerMockRunner;

import static junit.framework.Assert.assertNotNull;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

@RunWith(PowerMockRunner.class)
public class SessionProviderTest {

    private static final int SESSION_NUMBER = 1;

    @Mock
    private BleReferenceAppliance bleReferenceAppliance;
    @Mock
    private SessionProvider.Callback mockSessionProviderCallback;
    @Mock
    private SessionDataPortProperties mockSessionDataPortProperties;
    @Mock
    private DICommPort<?> mockDiCommPort;
    @Mock
    private Summary mockSummary;
    @Captor
    private ArgumentCaptor<PortDataCallback<SessionDataPortProperties>> dataCallbackArgumentCaptor;
    @Captor
    private ArgumentCaptor<Session> sessionArgumentCaptor;
    private SessionProvider subject;
    @Mock
    Summary summary;
    @Mock
    Session session;

    @Before
    public void setUp() throws Exception {
        initMocks(this);
        subject = new SessionProviderForTest(bleReferenceAppliance, SESSION_NUMBER, mockSessionProviderCallback);
    }

    @Test
    public void itShouldReportErrorWhenSessionDataReportsError() throws Exception {
        simulateSessionDataError(Error.OUT_OF_MEMORY);
        verifySessionDataFailure();
    }

    @Test
    public void itShouldReportOnSuccessWithEmptySessionWhenSessionIsEmptySession() throws Exception {
        simulateEmptySession();
        verify(bleReferenceAppliance).unregisterSessionDataCallback();
        verify(mockSessionProviderCallback).onSuccessForEmptyOrInvalidTimeSession();
    }

    @Test
    public void itShouldReportOnSuccessForEmptyOrInvalidTimeSessionWhenSessionTimeIsInvalid() throws Exception {
        simulateSessionWithInvalidTime();

        verify(bleReferenceAppliance).unregisterSessionDataCallback();
        verify(mockSessionProviderCallback).onSuccessForEmptyOrInvalidTimeSession();
    }

    @Test
    public void itShouldStoreSleepSessionWhenSessionDataIsReceived() throws Exception {
        simulateSessionDataReceived(summary);
        assertNotNull(subject.summary);
    }
//
//    @Test
//    public void itShouldNotReportToListenerWhenSessionDataIsReceived() throws Exception {
//        simulateSessionDataReceived(mockSummary);
//
//        verifyZeroInteractions(mockSessionProviderCallback);
//    }
//
//    @Test
//    public void itShouldCallOnErrorWhenExceptionIsThrownDuringOverviewDataConstruction() throws Exception {
//        simulateSessionDataInvalid(new InvalidPortPropertiesException());
//
//        verify(mockSessionProviderCallback).onError(any(IllegalStateException.class));
//    }


//    @Test
//    public void itShouldReportSuccessWhenTherapyPortReportData() throws Exception {
//        simulateSuccess(mockSummary, START_TIME, timeOffsets, pre, post);
//
//        verify(mockSessionProviderCallback).onSuccess(any(Session.class));
//    }
//
//    @Test
//    public void itShouldContainSessionDataWhenSuccessIsReported() throws Exception {
//        simulateSuccess(mockSummary, START_TIME, timeOffsets, pre, post);
//
//        verify(mockSessionProviderCallback).onSuccess(sessionArgumentCaptor.capture());
//        assertNotNull(sessionArgumentCaptor.getValue().getSummary());
//        assertEquals(mockSummary, sessionArgumentCaptor.getValue().getSummary());
//    }
//
//
//    @Test
//    public void itShouldCleanUpWhenSuccessIsReported() throws Exception {
//        simulateSuccess(mockSummary, START_TIME, timeOffsets, pre, post);
//
//        verify(bleReferenceAppliance).unregisterSessionDataCallback();
//    }

    private void verifySessionDataFailure() {
        verify(bleReferenceAppliance).unregisterSessionDataCallback();
        verify(mockSessionProviderCallback).onError(any(PortErrorException.class));
    }

    private void simulateSessionDataError(Error error) throws Exception {
        subject.fetchSession();
        verify(bleReferenceAppliance).registerSessionDataCallback(dataCallbackArgumentCaptor.capture());
        dataCallbackArgumentCaptor.getValue().onError(error);
    }

    private void simulateEmptySession() throws Exception {
        when(mockSessionDataPortProperties.isEmptySession()).thenReturn(true);
        subject.fetchSession();

        verify(bleReferenceAppliance).registerSessionDataCallback(dataCallbackArgumentCaptor.capture());

        dataCallbackArgumentCaptor.getValue().onDataReceived(mockSessionDataPortProperties);
    }

    private void simulateSessionWithInvalidTime() throws Exception {
        when(mockSessionDataPortProperties.isSessionTimeValid()).thenReturn(false);
        subject.fetchSession();

        verify(bleReferenceAppliance).registerSessionDataCallback(dataCallbackArgumentCaptor.capture());

        dataCallbackArgumentCaptor.getValue().onDataReceived(mockSessionDataPortProperties);

    }

    private void simulateSessionDataReceived(Summary summary) throws Exception {
        when(mockSessionDataPortProperties.isSessionTimeValid()).thenReturn(true);
        subject.fetchSession();
        verify(bleReferenceAppliance).registerSessionDataCallback(dataCallbackArgumentCaptor.capture());
        dataCallbackArgumentCaptor.getValue().onDataReceived(mockSessionDataPortProperties);
    }

//    private void simulateSessionDataInvalid(Exception exception) throws Exception {
//        doThrow(exception).when(mockSummaryBuilder).build(mockSessionDataPortProperties);
//        when(mockSessionDataPortProperties.isSessionTimeValid()).thenReturn(true);
//        subject.fetchSession();
//
//        verify(bleReferenceAppliance).registerSessionDataCallback(dataCallbackArgumentCaptor.capture());
//
//        dataCallbackArgumentCaptor.getValue().onDataReceived(mockSessionDataPortProperties);
//    }
//
//
//
//    private void simulateSuccess(Summary summary, long time, int[] timeOffsets, int[] pre, int[] post) throws Exception {
//        when(mockSummary.getDate()).thenReturn(new Date(time));
//
//        verify(bleReferenceAppliance).registerTherapyDataCallback(therapyPortDataCallbackArgumentCaptor.capture());
//        therapyPortDataCallbackArgumentCaptor.getValue().onDataReceived(mockTherapyDataPortProperties);
//    }

    private class SessionProviderForTest extends SessionProvider {

        SessionProviderForTest(@NonNull BleReferenceAppliance appliance, long sessionNumber, @NonNull Callback SessionGroupCallback) {
            super(appliance, sessionNumber, SessionGroupCallback);
        }

        @NonNull
        @Override
        protected Summary getSummary(@Nullable SessionDataPortProperties sessionProperties) {
            return summary;
        }

        @NonNull
        @Override
        protected Session getResult() {
            return session;
        }
    }
}