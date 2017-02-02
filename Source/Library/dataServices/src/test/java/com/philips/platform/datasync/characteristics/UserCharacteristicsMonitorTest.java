package com.philips.platform.datasync.characteristics;

import com.philips.platform.core.Eventing;
import com.philips.platform.core.datatypes.UserCharacteristics;
import com.philips.platform.core.events.CharacteristicsBackendGetRequest;
import com.philips.platform.core.events.CharacteristicsBackendSaveRequest;
import com.philips.platform.core.injection.AppComponent;
import com.philips.platform.core.trackers.DataServicesManager;
import com.philips.platform.datasync.UCoreAccessProvider;
import com.philips.platform.datasync.UCoreAdapter;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;

import java.util.Collections;
import java.util.List;

import retrofit.converter.GsonConverter;

import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.isA;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class UserCharacteristicsMonitorTest {

    private UserCharacteristicsMonitor userCharacteristicsMonitor;
    @Mock
    private UserCharacteristicsSender userCharacteristicsSenderMock;
    @Mock
    private GsonConverter gsonConverterMock;

    @Mock
    private UCoreAdapter uCoreAdapterMock;

    @Mock
    private Eventing eventingMock;

    @Captor
    private ArgumentCaptor<List<? extends UserCharacteristics>> captor;

    @Mock
    private UserCharacteristicsFetcher userCharacteristicsFetcherMock;

    @Mock
    private UserCharacteristicsConverter userCharacteristicsConvertorMock;

    @Mock
    private UserCharacteristics userCharacteristicsMock;

    @Mock
    private CharacteristicsBackendSaveRequest characteristicsBackendSaveRequestMock;
    @Mock
    private CharacteristicsBackendGetRequest characteristicsBackendGetRequestMock;

    @Mock
    private AppComponent mAppComponentMock;

    @Mock
    private UCoreAccessProvider uCoreAccessProviderMock;

    @Before
    public void setUp() throws Exception {
        initMocks(this);

        DataServicesManager.getInstance().setAppComponant(mAppComponentMock);

        userCharacteristicsMonitor = new UserCharacteristicsMonitor(
                userCharacteristicsSenderMock,
                userCharacteristicsFetcherMock);
        userCharacteristicsMonitor.mEventing = eventingMock;
        userCharacteristicsMonitor.uCoreAccessProvider = uCoreAccessProviderMock;
    }


    @Test
    public void ShouldCallSaveUserCharacteristics_WhenSyncUserCharacteristicsIsCalled() throws Exception {
        when(characteristicsBackendSaveRequestMock.getCharacteristic()).thenReturn(userCharacteristicsMock);
        when(uCoreAccessProviderMock.isLoggedIn()).thenReturn(true);
        when(uCoreAccessProviderMock.getAccessToken()).thenReturn("3423sdfs324234");

        userCharacteristicsMonitor.onEventAsync(characteristicsBackendSaveRequestMock);

        verify(userCharacteristicsSenderMock).sendDataToBackend(Collections.singletonList(userCharacteristicsMock));

    }

    @Test
    public void ShouldSendCharacteristics_WhenCharacteristicsSend() throws Exception {
        when(characteristicsBackendSaveRequestMock.getCharacteristic()).thenReturn(userCharacteristicsMock);
        when(userCharacteristicsSenderMock.sendDataToBackend(Collections.singletonList((userCharacteristicsMock)))).thenReturn(true);

        userCharacteristicsMonitor.onEventAsync(characteristicsBackendSaveRequestMock);

        //verify(userCharacteristicsFetcherMock).fetchDataSince(any(DateTime.class));

        verify(eventingMock, never()).post(isA(CharacteristicsBackendSaveRequest.class));

    }

    @Test
    public void ShouldFetchCharacteristics_WhenCharacteristicsGetRequest() throws Exception {
        when(characteristicsBackendGetRequestMock.getEventId()).thenReturn(anyInt());
        //when(userCharacteristicsFetcherMock.fetchDataSince(null)).thenReturn(new UserCharacteristicsFetcher());

        userCharacteristicsMonitor.onEventAsync(characteristicsBackendGetRequestMock);

        //verify(userCharacteristicsFetcherMock).fetchDataSince(any(DateTime.class));

        verify(eventingMock, never()).post(isA(CharacteristicsBackendGetRequest.class));

    }
}