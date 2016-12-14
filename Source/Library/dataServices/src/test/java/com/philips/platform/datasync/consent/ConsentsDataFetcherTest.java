package com.philips.platform.datasync.consent;

import android.support.annotation.Nullable;

import com.philips.platform.core.Eventing;
import com.philips.platform.core.datatypes.ConsentDetail;
import com.philips.platform.core.events.ConsentBackendGetRequest;
import com.philips.platform.core.events.ConsentBackendListSaveRequest;
import com.philips.platform.datasync.UCoreAdapter;
import com.philips.platform.datasync.synchronisation.DataSender;

import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;

import java.util.Collections;
import java.util.List;

import retrofit.RetrofitError;

import static org.junit.Assert.*;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.initMocks;

/**
 * Created by sangamesh on 07/12/16.
 */
public class ConsentsDataFetcherTest {

    private ConsentsDataFetcher consentDataFetcher;

    @Mock
    UCoreAdapter uCoreAdapterMock;

    @Mock
    Eventing eventingMock;

    @Mock
    ConsentDetail consentDetailMock;

    @Captor
    private ArgumentCaptor<ConsentBackendGetRequest> ConsentBackendGetRequestEventCaptor;

    @Before
    public void setUp() {
        initMocks(this);
        consentDataFetcher=new ConsentsDataFetcher(uCoreAdapterMock,eventingMock);
    }

    @Test
    public void shouldNotFetchDataSince_WhenDataSenderIsBusy() throws Exception {

        consentDataFetcher.synchronizationState.set(DataSender.State.BUSY.getCode());
        consentDataFetcher.fetchDataSince(new DateTime());
        verify(eventingMock, never()).post(ConsentBackendGetRequestEventCaptor.capture());
    }

    @Test
    public void shouldFetchDataSince_WhenDataSenderIsNotBusy() throws Exception {

        consentDataFetcher.synchronizationState.set(DataSender.State.IDLE.getCode());
        consentDataFetcher.fetchDataSince(new DateTime());
        verify(eventingMock).post(ConsentBackendGetRequestEventCaptor.capture());
    }

    @Test
    public void shouldReturnConsentDetails_WhenGetConsentDetailsIsCalled() throws Exception {
        consentDataFetcher.getConsentDetails();

    }
    @Test
    public void shouldFetchDataSince_WhenDataSenderToSetConsentDetail() throws Exception {
        consentDataFetcher.setConsentDetails(Collections.singletonList(consentDetailMock));
    }

    @Test
    public void shouldFetchDataSince_WhenDataSenderTofetchAllData() throws Exception {
        consentDataFetcher.fetchAllData();
    }
}