package com.philips.platform.datasync.characteristics;

import com.philips.platform.core.Eventing;
import com.philips.platform.core.datatypes.Characteristics;
import com.philips.platform.core.events.CharacteristicsBackendGetRequest;
import com.philips.platform.core.events.CharacteristicsBackendSaveRequest;
import com.philips.platform.core.events.FetchUserCharacteristicsFromBackendEvent;
import com.philips.platform.datasync.UCoreAdapter;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.robolectric.RobolectricTestRunner;

import java.util.ArrayList;
import java.util.List;

import retrofit.converter.GsonConverter;

import static org.junit.Assert.*;
import static org.mockito.Mockito.verify;

@RunWith(RobolectricTestRunner.class)
public class UserCharacteristicsMonitorTest {

    private UserCharacteristicsMonitor userCharacteristicsMonitor;
    @Mock
    private UserCharacteristicsSender userCharacteristicsSender;
    @Mock
    private GsonConverter gsonConverterMock;

    @Mock
    private UCoreAdapter uCoreAdapterMock;

    @Mock
    private Eventing eventingMock;

    @Captor
    private ArgumentCaptor<List<? extends Characteristics>> captor;

    @Mock
    private UserCharacteristicsFetcher userCharacteristicsFetcher;

    @Mock
    private UserCharacteristicsConvertor userCharacteristicsConvertorMock;

    @Before
    public void setUp() throws Exception {
        initMocks(this);

        userCharacteristicsMonitor = new UserCharacteristicsMonitor(uCoreAdapterMock,
                gsonConverterMock,
                eventingMock,
                userCharacteristicsConvertorMock);
    }

    private void initMocks(UserCharacteristicsMonitorTest userCharacteristicsMonitorTest) {
    }

//    @Test
//    public void ShouldSendDataToBackend_WhenAsked() throws Exception {
//        ArrayList<String> articleUid = new ArrayList<>();
//        articleUid.add("ttl1");
//        userCharacteristicsMonitor.onEventAsync(new CharacteristicsBackendSaveRequest(CharacteristicsBackendSaveRequest.RequestType.UPDATE, articleUid));
//
//        verify(userCharacteristicsMonitor).sendDataToBackend(captor.capture());
//        assertThat(captor.getValue()).hasSize(1);
//        assertThat(captor.getValue().get(0).getType()).isEqualTo(Characteristics.USER_CHARACTERISTIC_TYPE);
//    }

    @Test
    public void ShouldFetchDataFromBackend_WhenAsked() throws Exception {
        userCharacteristicsMonitor.onEventAsync(new CharacteristicsBackendGetRequest());

        verify(userCharacteristicsFetcher).fetchDataSince(null);
    }

}