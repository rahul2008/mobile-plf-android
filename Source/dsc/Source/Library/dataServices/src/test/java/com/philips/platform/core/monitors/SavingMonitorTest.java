package com.philips.platform.core.monitors;

import com.philips.platform.core.Eventing;
import com.philips.platform.core.datatypes.Characteristics;
import com.philips.platform.core.datatypes.Moment;
import com.philips.platform.core.datatypes.Settings;
import com.philips.platform.core.dbinterfaces.DBDeletingInterface;
import com.philips.platform.core.dbinterfaces.DBSavingInterface;
import com.philips.platform.core.events.DatabaseSettingsSaveRequest;
import com.philips.platform.core.events.InsightsSaveRequest;
import com.philips.platform.core.events.MomentSaveRequest;
import com.philips.platform.core.events.MomentsSaveRequest;
import com.philips.platform.core.events.UserCharacteristicsSaveRequest;
import com.philips.platform.core.listeners.DBRequestListener;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.initMocks;

public class SavingMonitorTest {

    @Mock
    private Eventing eventingMock;

    @Mock
    private DBSavingInterface savingMock;

    @Mock
    private DBDeletingInterface deletingMock;

    @Mock
    Characteristics characteristicsMock;

    @Mock
    private Moment moment;

    @Mock
    Settings settingsMock;

    private SavingMonitor savingMonitor;

    @Mock
    DBRequestListener dbRequestListener;

    @Before
    public void setUp() {
        initMocks(this);
        savingMonitor = new SavingMonitor(savingMock, deletingMock);
        savingMonitor.start(eventingMock);
    }

    @Test
    public void ShouldSaveMoment_WhenSaveMomentRequestIsReceived() throws Exception {
        savingMonitor.onEventBackGround(new MomentSaveRequest(moment, dbRequestListener));
        verify(savingMock).saveMoment(moment, dbRequestListener);
    }

    @Test
    public void ShouldPostExceptionEvent_WhenSQLInsertionFails() throws Exception {
        savingMonitor.onEventBackGround(new MomentSaveRequest(moment, dbRequestListener));
        doThrow(SQLException.class).when(savingMock).saveMoment(moment, dbRequestListener);
        verify(savingMock).postError(any(Exception.class), eq(dbRequestListener));
    }

    @Test
    public void Test_MomentsSaveRequest() throws Exception {
        List list = new ArrayList();
        list.add(moment);
        savingMonitor.onEventBackGround(new MomentsSaveRequest(list, dbRequestListener));
        verify(savingMock).saveMoments(list, dbRequestListener);
    }

    @Test
    public void InsightsSaveRequestForwardedToSavingInterface() throws SQLException {
        savingMonitor.onEventBackGround(new InsightsSaveRequest(Collections.emptyList(), dbRequestListener));
        verify(savingMock).saveInsights(Collections.emptyList(), dbRequestListener);
    }

    @Test
    public void InsightsSaveRequest_WhenSqlInsertionFails_ThenErrorIsPosted() throws SQLException {
        savingMonitor.onEventBackGround(new InsightsSaveRequest(Collections.emptyList(), dbRequestListener));
        doThrow(SQLException.class).when(savingMock).saveInsights(Collections.emptyList(), dbRequestListener);
        verify(savingMock).postError(any(Exception.class), eq(dbRequestListener));
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
