package com.philips.platform.datasync.settings;

import com.philips.platform.core.Eventing;
import com.philips.platform.core.datatypes.Consent;
import com.philips.platform.core.datatypes.Settings;
import com.philips.platform.core.events.ConsentBackendListSaveRequest;
import com.philips.platform.core.events.ConsentBackendListSaveResponse;
import com.philips.platform.core.events.GetNonSynchronizedDataResponse;
import com.philips.platform.core.events.SettingsBackendSaveRequest;
import com.philips.platform.core.events.SettingsBackendSaveResponse;
import com.philips.platform.core.injection.AppComponent;
import com.philips.platform.core.trackers.DataServicesManager;
import com.philips.platform.datasync.consent.ConsentDataSender;
import com.philips.platform.datasync.synchronisation.DataSender;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;

import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.*;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.initMocks;

/**
 * Created by sangamesh on 31/01/17.
 */
public class SettingsDataSenderTest {

    private SettingsDataSender settingsDataSender;

    @Mock
    private Eventing eventingMock;

    @Mock
    private GetNonSynchronizedDataResponse getNonSynchronizedDataResponseMock;

    @Mock
    private Settings settingsMock;

    @Mock
    private SettingsBackendSaveResponse settingsBackendSaveResponseMock;

    @Captor
    private ArgumentCaptor<SettingsBackendSaveRequest> settingsBackendSaveRequestEventCaptor;

    @Mock
    private AppComponent appComponantMock;

    @Before
    public void setUp() {
        initMocks(this);
        DataServicesManager.getInstance().setAppComponant(appComponantMock);
        settingsDataSender = new SettingsDataSender();
        settingsDataSender.eventing = eventingMock;
    }

    @Test
    public void ShouldNotPostAnEventToBackend_WhenSettingsListIsEmpty() throws Exception {

        settingsDataSender.sendDataToBackend(Collections.<Settings>emptyList());

        verify(eventingMock, never()).post(settingsBackendSaveRequestEventCaptor.capture());
    }

    @Test
    public void ShouldNotPostAnEventToBackend_WhenStateIsNotIdle() throws Exception {
        settingsDataSender.synchronizationState.set(DataSender.State.BUSY.getCode());

        settingsDataSender.sendDataToBackend(Collections.singletonList(settingsMock));

        verify(eventingMock, never()).post(settingsBackendSaveRequestEventCaptor.capture());
    }

   @Test
    public void ShouldPostSettingsBackendSaveRequest_WhenSettingsIsNotNull() throws Exception {

        settingsDataSender.sendDataToBackend(Collections.singletonList(settingsMock));

        verify(eventingMock).post(settingsBackendSaveRequestEventCaptor.capture());
        assertThat(settingsBackendSaveRequestEventCaptor.getValue().getSettings()).isNotNull();

    }

    @Test
    public void ShouldGetClassForSyncData_WhenCalled() throws Exception {
        settingsDataSender.getClassForSyncData();
    }

}