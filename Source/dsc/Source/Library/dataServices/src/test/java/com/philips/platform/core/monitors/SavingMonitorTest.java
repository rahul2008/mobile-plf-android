package com.philips.platform.core.monitors;

import com.philips.platform.core.Eventing;
import com.philips.platform.core.datatypes.Characteristics;
import com.philips.platform.core.datatypes.ConsentDetail;
import com.philips.platform.core.datatypes.Moment;
import com.philips.platform.core.datatypes.Settings;
import com.philips.platform.core.dbinterfaces.DBDeletingInterface;
import com.philips.platform.core.dbinterfaces.DBFetchingInterface;
import com.philips.platform.core.dbinterfaces.DBSavingInterface;
import com.philips.platform.core.dbinterfaces.DBUpdatingInterface;
import com.philips.platform.core.events.DatabaseConsentSaveRequest;
import com.philips.platform.core.events.DatabaseSettingsSaveRequest;
import com.philips.platform.core.events.Event;
import com.philips.platform.core.events.MomentSaveRequest;
import com.philips.platform.core.events.MomentsSaveRequest;
import com.philips.platform.core.events.UserCharacteristicsSaveRequest;
import com.philips.platform.core.listeners.DBRequestListener;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.initMocks;

public class SavingMonitorTest {

    public static final String TEST_GUID = "TEST_GUID";
    public static final int TEST_ID = 111;
    public static final String TEST_BABY_NAME = "TEST_BABY_NAME";
    //    public static final String TEST_DATE_OF_BIRTH = new DateTime().withTimeAtStartOfDay().toString();
    public static final double TEST_WEIGHT = 12.;
    private static final String TEST_BABY_ID = "TEST_BABY_ID";

    @Mock
    private Eventing eventingMock;

    @Mock
    private DBSavingInterface savingMock;

    @Mock
    private DBUpdatingInterface updatingMock;

    @Mock
    private DBFetchingInterface fetchingMock;

    @Mock
    private DBDeletingInterface deletingMock;

    @Mock
    UserCharacteristicsSaveRequest userCharacteristicsSaveRequestMock;

    @Mock
    Characteristics characteristicsMock;

    @Mock
    Exception exceptionMock;

    @Mock
    private Moment moment;

    @Mock
    Settings settingsMock;

    @Mock
    ConsentDetail consentDetailMock;

    private SavingMonitor savingMonitor;

/*
    @Captor
    private ArgumentCaptor<MomentChangeEvent> changeEventArgumentCaptor;*/

    @Mock
    DBRequestListener dbRequestListener;

    @Before
    public void setUp() {
        initMocks(this);
        savingMonitor = new SavingMonitor(savingMock, deletingMock, updatingMock);
        savingMonitor.start(eventingMock);
    }

    @Test
    public void ShouldSaveMoment_WhenSaveMomentRequestIsReceived() throws Exception {

        savingMonitor.onEventBackGround(new MomentSaveRequest(moment, dbRequestListener));

        verify(savingMock).saveMoment(moment, dbRequestListener);
        //verify(eventingMock).post(changeEventArgumentCaptor.capture());
        //   assertThat(changeEventArgumentCaptor.getValue().getMoments()).isEqualTo(moment);
    }

    @Test
    public void ShouldPostExceptionEvent_WhenSQLInsertionFails() throws Exception {

        //doThrow(SQLException.class).when(savingMock).saveMoment(moment);
        savingMonitor.onEventBackGround(new MomentSaveRequest(moment, dbRequestListener));
        doThrow(SQLException.class).when(savingMock).saveMoment(moment, dbRequestListener);
        verify(savingMock).saveMoment(moment, dbRequestListener);

        //verify(eventingMock).post(exceptionEventArgumentCaptor.capture());
        //assertThat(exceptionEventArgumentCaptor.getValue().getCause()).isInstanceOf(SQLException.class);
    }
//
//    @Test
//    public void ShouldSaveConsent_WhenSaveConsentRequestIsReceived() throws Exception {
//
//        savingMonitor.onEventAsync(new DatabaseConsentSaveRequest(anyListOf(ConsentDetail.class), dbRequestListener));
//
//       // verify(savingMock).saveConsentDetails(anyListOf(ConsentDetail.class), dbRequestListener);
//    }

//    @Test
//    public void ShouldPostSuccessEvent_WhenConsentIsProcessed() throws Exception {
//        savingMonitor.onEventAsync(new DatabaseConsentSaveRequest(anyListOf(ConsentDetail.class), dbRequestListener));
//    }


    private <T extends Event> T captureEvent(Class<T> clazz, final Eventing eventingMock, final int wantedNumberOfInvocations) throws SQLException {
        final ArgumentCaptor<T> captor = ArgumentCaptor.forClass(clazz);
        verify(eventingMock, times(wantedNumberOfInvocations)).post(captor.capture());
        return captor.getAllValues().get(wantedNumberOfInvocations - 1);
    }

    @Test
    public void Test_MomentsSaveRequest() throws Exception {
        List list = new ArrayList();
        list.add(moment);
        savingMonitor.onEventBackGround(new MomentsSaveRequest(list, dbRequestListener));
        verify(savingMock).saveMoments(list, dbRequestListener);
    }

    @Test
    public void Test_DatabaseConsentSaveRequest() throws Exception {
        List list = new ArrayList();
        list.add(moment);
        savingMonitor.onEventBackGround(new DatabaseConsentSaveRequest(list, dbRequestListener));
        verify(savingMock).saveConsentDetails(list, dbRequestListener);
    }

    @Test
    public void Test_DatabaseSettingsSaveRequest() throws Exception {
        savingMonitor.onEventBackGround(new DatabaseSettingsSaveRequest(settingsMock, dbRequestListener));
        verify(savingMock).saveSettings(settingsMock, dbRequestListener);
    }

    @Test
    public void Test_UserCharacteristicsSaveRequest() throws Exception {
        List list = new ArrayList();
        list.add(characteristicsMock);
        savingMonitor.onEventBackGround(new UserCharacteristicsSaveRequest(list, dbRequestListener));
        verify(savingMock).saveUserCharacteristics(list, dbRequestListener);
    }


}

