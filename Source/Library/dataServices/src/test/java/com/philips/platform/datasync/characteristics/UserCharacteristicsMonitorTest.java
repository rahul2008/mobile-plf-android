package com.philips.platform.datasync.characteristics;

import com.philips.platform.core.Eventing;
import com.philips.platform.core.datatypes.Characteristics;
import com.philips.platform.core.datatypes.Moment;
import com.philips.platform.core.events.CharacteristicsBackendGetRequest;
import com.philips.platform.core.events.CharacteristicsBackendSaveRequest;
import com.philips.platform.datasync.UCoreAdapter;

import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.robolectric.RobolectricTestRunner;

import java.util.Collections;
import java.util.List;

import retrofit.RetrofitError;
import retrofit.converter.GsonConverter;

import static org.mockito.Matchers.any;
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
    private ArgumentCaptor<List<? extends Characteristics>> captor;

    @Mock
    private UserCharacteristicsFetcher userCharacteristicsFetcherMock;

    @Mock
    private UserCharacteristicsConverter userCharacteristicsConvertorMock;

    @Mock
    private Characteristics characteristicsMock;

    @Mock
    private CharacteristicsBackendSaveRequest characteristicsBackendSaveRequestMock;

    @Before
    public void setUp() throws Exception {
        initMocks(this);

        userCharacteristicsMonitor = new UserCharacteristicsMonitor(
                userCharacteristicsSenderMock,
                userCharacteristicsFetcherMock);
    }


    @Test
    public void ShouldCallSaveUserCharacteristics_WhenSyncUserCharacteristicsIsCalled() throws Exception {
        when(characteristicsBackendSaveRequestMock.getCharacteristic()).thenReturn(characteristicsMock);

        userCharacteristicsMonitor.onEventAsync(characteristicsBackendSaveRequestMock);

        verify(userCharacteristicsSenderMock).sendDataToBackend(Collections.singletonList(characteristicsMock));

    }

    @Test
    public void ShouldFetchMoment_WhenMomentConflictOccursDuringSend() throws Exception {
        when(characteristicsBackendSaveRequestMock.getCharacteristic()).thenReturn(characteristicsMock);
        when(userCharacteristicsSenderMock.sendDataToBackend(Collections.singletonList((characteristicsMock)))).thenReturn(true);

        userCharacteristicsMonitor.onEventAsync(characteristicsBackendSaveRequestMock);

        RetrofitError retrofitError = verify(userCharacteristicsFetcherMock).fetchDataSince(any(DateTime.class));

    }
}