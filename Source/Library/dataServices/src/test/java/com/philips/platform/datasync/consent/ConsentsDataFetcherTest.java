package com.philips.platform.datasync.consent;

import com.philips.platform.core.Eventing;
import com.philips.platform.core.datatypes.ConsentDetail;
import com.philips.platform.core.injection.AppComponent;
import com.philips.platform.core.trackers.DataServicesManager;
import com.philips.platform.datasync.UCoreAdapter;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import java.util.Collections;

import retrofit.converter.GsonConverter;

import static org.mockito.MockitoAnnotations.initMocks;

/**
 * Created by sangamesh on 07/12/16.
 */
public class ConsentsDataFetcherTest {

    private ConsentsDataFetcher consentDataFetcher;

    @Mock
    UCoreAdapter uCoreAdapterMock;

    @Mock
    GsonConverter gsonConverterMock;

    @Mock
    ConsentsConverter consentsConverterMock;

    @Mock
    Eventing eventingMock;

    @Mock
    ConsentDetail consentDetailMock;
/*
    @Captor
    private ArgumentCaptor<ConsentBackendGetRequest> ConsentBackendGetRequestEventCaptor;*/

    @Mock
    private AppComponent appComponantMock;

    @Before
    public void setUp() {
        initMocks(this);
        DataServicesManager.getInstance().setAppComponant(appComponantMock);
        consentDataFetcher = new ConsentsDataFetcher(uCoreAdapterMock, gsonConverterMock, consentsConverterMock);
        consentDataFetcher.eventing = eventingMock;
    }

   /* @Test
    public void shouldNotFetchDataSince_WhenDataSenderIsBusy() throws Exception {

        consentDataFetcher.synchronizationState.set(DataSender.State.BUSY.getCode());
        consentDataFetcher.fetchDataSince(new DateTime());
        verify(eventingMock, never()).post(ConsentBackendGetRequestEventCaptor.capture());
    }*/

   /* @Test
    public void shouldFetchDataSince_WhenDataSenderIsNotBusy() throws Exception {

        consentDataFetcher.synchronizationState.set(DataSender.State.IDLE.getCode());
        consentDataFetcher.fetchDataSince(new DateTime());
        verify(eventingMock).post(ConsentBackendGetRequestEventCaptor.capture());
    }*/

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