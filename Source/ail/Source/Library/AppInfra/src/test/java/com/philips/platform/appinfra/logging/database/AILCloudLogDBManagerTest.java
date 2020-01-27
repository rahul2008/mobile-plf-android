package com.philips.platform.appinfra.logging.database;

import androidx.lifecycle.LiveData;
import androidx.sqlite.db.SupportSQLiteDatabase;
import androidx.sqlite.db.SupportSQLiteOpenHelper;

import com.philips.platform.appinfra.AppInfraInterface;
import com.philips.platform.appinfra.logging.CloudLoggingConstants;

import junit.framework.TestCase;

import org.junit.Before;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class AILCloudLogDBManagerTest extends TestCase {

    private AILCloudLogDBManager ailCloudLogDBManager;

    @Mock
    private AppInfraInterface appInfra;

    @Mock
    private AILCloudLogDatabase ailCloudLogDatabase;

    @Mock
    private SupportSQLiteOpenHelper supportSQLiteOpenHelper;

    @Mock
    private SupportSQLiteDatabase supportSQLiteDatabase;

    @Mock
    private AILCloudLogDao ailCloudLogDao;

    @Before
    public void setUp() throws Exception {
        super.setUp();
        MockitoAnnotations.initMocks(this);
        when(ailCloudLogDatabase.getOpenHelper()).thenReturn(supportSQLiteOpenHelper);
        when(supportSQLiteOpenHelper.getWritableDatabase()).thenReturn(supportSQLiteDatabase);
        when(ailCloudLogDatabase.logModel()).thenReturn(ailCloudLogDao);
        ailCloudLogDBManager = new AILCloudLogDBManager(appInfra) {
            @Override
            AILCloudLogDatabase getPersistenceDatabase(AppInfraInterface appInfra) {
                return ailCloudLogDatabase;
            }
        };
    }

    public void testInsertingCloudLogData() {
        AILCloudLogData ailCloudLogData = new AILCloudLogData();
        ailCloudLogDBManager.insertLog(ailCloudLogData);
        verify(ailCloudLogDao).insertLog(ailCloudLogData);
    }

    public void testGettingLogCount() {
        LiveData<Integer> data = new LiveData<Integer>() {
            @Override
            public Integer getValue() {
                return 100;
            }
        };
        when(ailCloudLogDao.getNumberOfRows()).thenReturn(data);
        assertEquals(ailCloudLogDBManager.getLogCount().getValue().intValue(), 100);
    }

    public void testFetchingNewAILCloudLogRecords() {
        List<AILCloudLogData> ailCloudLogDataList = new ArrayList<>();
        AILCloudLogData ailCloudLogData = new AILCloudLogData();
        ailCloudLogData.status = "new";
        ailCloudLogDataList.add(ailCloudLogData);
        when(ailCloudLogDao.getOldestRowsWithMaxLimit(CloudLoggingConstants.HSDP_LOG_MAX_LIMIT)).thenReturn(ailCloudLogDataList);
        List<AILCloudLogData> newAILCloudLogRecords = ailCloudLogDBManager.getNewAILCloudLogRecords();
        assertEquals(newAILCloudLogRecords.size(), 1);
        assertEquals(newAILCloudLogRecords.get(0).status, AILCloudLogDBManager.DBLogState.PROCESSING.getState());
        verify(ailCloudLogDao).updateLogs(ailCloudLogDataList);
    }

    public void testUpdatingAILCloudLogListToNewState() {
        List<AILCloudLogData> ailCloudLogDataList = new ArrayList<>();
        AILCloudLogData ailCloudLogData = new AILCloudLogData();
        ailCloudLogDataList.add(ailCloudLogData);
        ailCloudLogDBManager.updateAILCloudLogListToNewState(ailCloudLogDataList);
        assertEquals(ailCloudLogDataList.size(), 1);
        assertEquals(ailCloudLogDataList.get(0).status, AILCloudLogDBManager.DBLogState.NEW.getState());
        verify(ailCloudLogDao).updateLogs(ailCloudLogDataList);
    }

    public void testDeletingCloudLogRecords() {
        List<AILCloudLogData> ailCloudLogDataList = new ArrayList<>();
        AILCloudLogData ailCloudLogData = new AILCloudLogData();
        ailCloudLogDataList.add(ailCloudLogData);
        ailCloudLogDBManager.deleteLogRecords(ailCloudLogDataList);
        verify(ailCloudLogDao).deleteLogs(ailCloudLogDataList);
    }
}
