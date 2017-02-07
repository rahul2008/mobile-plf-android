package com.philips.platform.datasync.consent;

import com.philips.platform.core.Eventing;
import com.philips.platform.core.datatypes.ConsentDetail;
import com.philips.platform.core.events.ConsentBackendListSaveResponse;
import com.philips.platform.core.events.ConsentBackendSaveRequest;
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
public class ConsentDetailDataSenderTest {

    private ConsentDataSender consentDataSender;

    @Mock
    private Eventing eventingMock;

    @Mock
    private GetNonSynchronizedDataResponse getNonSynchronizedDataResponseMock;

    @Mock
    private ConsentDetail consentDetailMock;

    @Mock
    private ConsentBackendListSaveResponse consentListSaveResponseMock;

    @Captor
    private ArgumentCaptor<ConsentBackendSaveRequest> consentListSaveRequestEventCaptor;

    @Mock
    private AppComponent appComponantMock;


    @Before
    public void setUp() {
        initMocks(this);
        DataServicesManager.getInstance().setAppComponant(appComponantMock);
        consentDataSender = new ConsentDataSender(uCoreAdapter, gsonConverter, consentsConverter);
        consentDataSender.eventing = eventingMock;
    }


    @Test
    public void ShouldNotPostAnEventToBackend_WhenConsentListIsEmpty() throws Exception {

        consentDataSender.sendDataToBackend(Collections.<ConsentDetail>emptyList());

        verify(eventingMock, never()).post(consentListSaveRequestEventCaptor.capture());
    }

    @Test
    public void ShouldNotPostAnEventToBackend_WhenStateIsNotIdle() throws Exception {
        consentDataSender.synchronizationState.set(DataSender.State.BUSY.getCode());

        consentDataSender.sendDataToBackend(Collections.singletonList(consentDetailMock));

        verify(eventingMock, never()).post(consentListSaveRequestEventCaptor.capture());
    }

    @Test
    public void ShouldPostConsentListSaveEvent_WhenConsentListIsNotEmpty() throws Exception {

        consentDataSender.sendDataToBackend(Collections.singletonList(consentDetailMock));

        verify(eventingMock).post(consentListSaveRequestEventCaptor.capture());
        assertThat(consentListSaveRequestEventCaptor.getValue().getConsentDetailList()).hasSize(1);
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