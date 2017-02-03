package com.philips.platform.datasync.settings;

import com.philips.platform.core.Eventing;
import com.philips.platform.core.datatypes.Settings;
import com.philips.platform.core.events.SettingsBackendGetRequest;
import com.philips.platform.core.injection.AppComponent;
import com.philips.platform.core.trackers.DataServicesManager;
import com.philips.platform.datasync.UCoreAdapter;
import com.philips.platform.datasync.synchronisation.DataSender;

import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;

import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.initMocks;

/**
 * Created by sangamesh on 31/01/17.
 */
public class SettingsDataFetcherTest {

    private SettingsDataFetcher settingsDataFetcher;

    @Mock
    UCoreAdapter uCoreAdapterMock;

    @Mock
    Eventing eventingMock;

    @Mock
    Settings settingsMock;

    @Captor
    private ArgumentCaptor<SettingsBackendGetRequest> settingsBackendGetRequestArgumentCaptor;

    @Mock
    private AppComponent appComponantMock;

    @Before
    public void setUp() {
        initMocks(this);
        DataServicesManager.getInstance().setAppComponant(appComponantMock);
        settingsDataFetcher=new SettingsDataFetcher(uCoreAdapterMock);
        settingsDataFetcher.eventing = eventingMock;
    }

    @Test
    public void shouldNotFetchDataSince_WhenDataSenderIsBusy() throws Exception {

        settingsDataFetcher.synchronizationState.set(DataSender.State.BUSY.getCode());
        settingsDataFetcher.fetchDataSince(new DateTime());
        verify(eventingMock, never()).post(settingsBackendGetRequestArgumentCaptor.capture());
    }

    @Test
    public void shouldFetchDataSince_WhenDataSenderIsNotBusy() throws Exception {

        settingsDataFetcher.synchronizationState.set(DataSender.State.IDLE.getCode());
        settingsDataFetcher.fetchDataSince(new DateTime());
        verify(eventingMock).post(settingsBackendGetRequestArgumentCaptor.capture());
    }


}