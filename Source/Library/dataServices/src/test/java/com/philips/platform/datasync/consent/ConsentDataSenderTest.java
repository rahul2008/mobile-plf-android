package com.philips.platform.datasync.consent;

import com.philips.platform.core.Eventing;
import com.philips.platform.core.datatypes.Consent;
import com.philips.platform.core.events.ConsentBackendListSaveRequest;
import com.philips.platform.core.events.ConsentBackendListSaveResponse;
import com.philips.platform.core.events.GetNonSynchronizedDataResponse;
import com.philips.platform.core.injection.AppComponent;
import com.philips.platform.core.trackers.DataServicesManager;
import com.philips.platform.datasync.synchronisation.DataSender;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;

import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.initMocks;

/**
 * Created by sangamesh on 28/11/16.
 */
public class ConsentDataSenderTest {

    private ConsentDataSender consentDataSender;

    @Mock
    private Eventing eventingMock;

    @Mock
    private GetNonSynchronizedDataResponse getNonSynchronizedDataResponseMock;

    @Mock
    private Consent consentMock;

    @Mock
    private ConsentBackendListSaveResponse consentListSaveResponseMock;

    @Captor
    private ArgumentCaptor<ConsentBackendListSaveRequest> consentListSaveRequestEventCaptor;

    @Mock
    private AppComponent appComponantMock;


    @Before
    public void setUp() {
        initMocks(this);
        DataServicesManager.mAppComponent = appComponantMock;
        consentDataSender = new ConsentDataSender();
        consentDataSender.eventing = eventingMock;
    }


    @Test
    public void ShouldNotPostAnEventToBackend_WhenConsentListIsEmpty() throws Exception {

        consentDataSender.sendDataToBackend(Collections.<Consent>emptyList());

        verify(eventingMock, never()).post(consentListSaveRequestEventCaptor.capture());
    }

    @Test
    public void ShouldNotPostAnEventToBackend_WhenStateIsNotIdle() throws Exception {
        consentDataSender.synchronizationState.set(DataSender.State.BUSY.getCode());

        consentDataSender.sendDataToBackend(Collections.singletonList(consentMock));

        verify(eventingMock, never()).post(consentListSaveRequestEventCaptor.capture());
    }

    @Test
    public void ShouldPostConsentListSaveEvent_WhenConsentListIsNotEmpty() throws Exception {

        consentDataSender.sendDataToBackend(Collections.singletonList(consentMock));

        verify(eventingMock).post(consentListSaveRequestEventCaptor.capture());
        assertThat(consentListSaveRequestEventCaptor.getValue().getConsentList()).hasSize(1);
    }

    @Test
    public void ShouldBeIdle_WhenNothingToSync() throws Exception {
        consentDataSender.onEventAsync(consentListSaveResponseMock);

        assertThat(consentDataSender.synchronizationState.get()).isEqualTo(DataSender.State.IDLE.getCode());
    }

    @Test
    public void ShouldGetClassForSyncData_WhenCalled() throws Exception {
        consentDataSender.getClassForSyncData();
    }

}