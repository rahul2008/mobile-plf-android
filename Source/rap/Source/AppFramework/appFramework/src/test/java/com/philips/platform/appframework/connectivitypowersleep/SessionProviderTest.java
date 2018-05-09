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
import com.philips.platform.appframework.connectivity.appliance.PortDataCallback;
import com.philips.platform.appframework.connectivity.appliance.RefAppBleReferenceAppliance;
import com.philips.platform.appframework.connectivitypowersleep.datamodels.Session;
import com.philips.platform.appframework.connectivitypowersleep.datamodels.SessionDataPortProperties;
import com.philips.platform.appframework.connectivitypowersleep.datamodels.Summary;
import com.philips.platform.appframework.connectivitypowersleep.error.InvalidPortPropertiesException;
import com.philips.platform.appframework.connectivitypowersleep.error.PortErrorException;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

@RunWith(MockitoJUnitRunner.class)
public class SessionProviderTest {

    private static final int SESSION_NUMBER = 1;

    @Mock
    private RefAppBleReferenceAppliance bleReferenceAppliance;
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
    static Session session;

    @Before
    public void setUp() throws Exception {
        initMocks(this);
        subject = new SessionProviderMock(bleReferenceAppliance, SESSION_NUMBER, mockSessionProviderCallback);
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
    public void processSessionProviderTest() throws InvalidPortPropertiesException {
        subject.processSessionData(mockSessionDataPortProperties);
        verify(mockSessionProviderCallback).onSuccess(any(Session.class));
    }


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


    public static class SessionProviderMock extends SessionProvider {

        SessionProviderMock(@NonNull RefAppBleReferenceAppliance appliance, long sessionNumber, @NonNull Callback SessionGroupCallback) {
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