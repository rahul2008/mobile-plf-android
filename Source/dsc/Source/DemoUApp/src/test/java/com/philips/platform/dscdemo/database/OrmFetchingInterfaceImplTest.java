package com.philips.platform.dscdemo.database;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.QueryBuilder;
import com.philips.platform.core.listeners.DBFetchRequestListner;
import com.philips.platform.dscdemo.database.table.OrmCharacteristics;
import com.philips.platform.dscdemo.database.table.OrmConsentDetail;
import com.philips.platform.dscdemo.database.table.OrmDCSync;
import com.philips.platform.dscdemo.database.table.OrmInsight;
import com.philips.platform.dscdemo.database.table.OrmMoment;
import com.philips.platform.dscdemo.database.table.OrmMomentType;
import com.philips.platform.dscdemo.database.table.OrmSettings;
import com.philips.platform.dscdemo.database.table.OrmSynchronisationData;

public class OrmFetchingInterfaceImplTest {

    @Mock
    private Dao<OrmMoment, Integer> momentDao;
    @Mock
    private Dao<OrmSynchronisationData, Integer> synchronisationDataDao;
    @Mock
    private Dao<OrmConsentDetail, Integer> consentDetailsDao;
    @Mock
    private Dao<OrmCharacteristics, Integer> characteristicsDao;
    @Mock
    private Dao<OrmSettings, Integer> settingsDao;
    @Mock
    private Dao<OrmDCSync, Integer> ormDCSyncDao;
    @Mock
    private Dao<OrmInsight, Integer> ormInsightDao;
    @Mock
    private DBFetchRequestListner dbFetchRequestListenerMock;
    @Mock
    private QueryBuilder<OrmMoment, Integer> queryBuilderMock;
    @Mock
    private PreparedQuery<OrmMoment> preparedQueryMock;
    private List<OrmMoment> ormMoments = new ArrayList<>();
    private OrmFetchingInterfaceImpl objectUnderTest;
    private List<OrmMoment> activeOrmMoments;
    private static final String CREATOR_ID = "CREATOR_ID";
    private static final String SUBJECT_ID = "SUBJECT_ID";
    private static final String GUID = "GUID";
    private static final OrmMomentType ORM_MOMENT_TYPE = new OrmMomentType(-1, "Temperature");

    @Before
    public void setUp() {
        initMocks(this);
        objectUnderTest = new OrmFetchingInterfaceImpl(momentDao, synchronisationDataDao, consentDetailsDao,
                characteristicsDao, settingsDao, ormDCSyncDao, ormInsightDao);
    }

    @Test
    public void fetchMoments_returnMomentsWithActiveSyncDataAndMomentWithoutSyncData() throws SQLException {
        givenQueryBuilderSetupCorrectly();
        givenMomentWithSyncData(CREATOR_ID, SUBJECT_ID, ORM_MOMENT_TYPE, GUID, false, 1);
        givenMomentWithoutSyncData(CREATOR_ID, SUBJECT_ID, ORM_MOMENT_TYPE);
        whenFetchMoments();
        thenMomentDaoIsCalled();
        thenActiveMomentsAreReturned(2);
    }

    @Test
    public void fetchMoments_notReturningMomentsWithInactiveSyncData() throws SQLException {
        givenQueryBuilderSetupCorrectly();
        givenMomentWithSyncData(CREATOR_ID, SUBJECT_ID, ORM_MOMENT_TYPE, GUID, true, 1);
        whenFetchMoments();
        thenMomentDaoIsCalled();
        thenActiveMomentsAreReturned(0);
    }

    private void givenQueryBuilderSetupCorrectly() throws SQLException {
        when(momentDao.queryBuilder()).thenReturn(queryBuilderMock);
        when(queryBuilderMock.prepare()).thenReturn(preparedQueryMock);
        when(momentDao.query(preparedQueryMock)).thenReturn(ormMoments);
    }

    private void givenMomentWithoutSyncData(String creatorId, String subjectId, OrmMomentType ormMomentType) {
        OrmMoment moment = new OrmMoment(creatorId, subjectId, ormMomentType, null);
        ormMoments.add(moment);
    }

    private void givenMomentWithSyncData(String creatorId, String subjectId, OrmMomentType ormMomentType, String syncGuid, boolean syncInactivity, int version) {
        OrmMoment moment = new OrmMoment(creatorId, subjectId, ormMomentType, null);
        moment.setSynchronisationData(new OrmSynchronisationData(syncGuid, syncInactivity, new DateTime("2018-01-01"), version));
        ormMoments.add(moment);
    }

    private void whenFetchMoments() throws SQLException {
        activeOrmMoments = objectUnderTest.fetchMoments(dbFetchRequestListenerMock);
    }

    private void thenActiveMomentsAreReturned(int expectedSize) {
        assertEquals(expectedSize, activeOrmMoments.size());
    }

    private void thenMomentDaoIsCalled() throws SQLException {
        verify(momentDao).query(preparedQueryMock);
    }
}
