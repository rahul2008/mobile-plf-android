package com.philips.platform.datasync.moments;

import com.philips.platform.core.BaseAppDataCreator;
import com.philips.platform.core.datatypes.Moment;
import com.philips.platform.core.datatypes.SynchronisationData;
import com.philips.platform.core.dbinterfaces.DBDeletingInterface;
import com.philips.platform.core.dbinterfaces.DBFetchingInterface;
import com.philips.platform.core.dbinterfaces.DBSavingInterface;
import com.philips.platform.core.dbinterfaces.DBUpdatingInterface;
import com.philips.platform.core.injection.AppComponent;
import com.philips.platform.core.listeners.DBRequestListener;
import com.philips.platform.core.trackers.DataServicesManager;
import com.philips.testing.verticals.datatyes.MomentType;
import com.philips.testing.verticals.table.OrmMoment;
import com.philips.testing.verticals.table.OrmMomentType;
import com.philips.testing.verticals.table.OrmSynchronisationData;

import org.joda.time.DateTime;
import org.joda.time.DateTimeUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class MomentsSegregatorTest {

    @Mock
    AppComponent appComponantMock;

    MomentsSegregator momentsSegregator;

    @Mock
    DBUpdatingInterface updatingInterface;

    @Mock
    DBSavingInterface dbSavingInterface;

    @Mock
    DBFetchingInterface dbFetchingInterface;

    @Mock
    DBDeletingInterface dbDeletingInterface;

    @Mock
    private OrmMoment ormMomentMock;

    @Mock
    private OrmSynchronisationData ormSynchronisationDataMock;

    @Mock
    DBRequestListener dbRequestListener;

    @Mock
    BaseAppDataCreator dataCreatorMock;

    private static final String CREATOR_ID = "creator";
    private static final String SUBJECT_ID = "SUBJECT";
    private static final String GUID_ID = UUID.randomUUID().toString();
    private static final String DELETED_GUID = "-1";
    private static final DateTime NOW = new DateTime();

    private List<Moment> momentList = new ArrayList<>();
    private int count;
    private Moment moment, moment2;
    private Map<Class, List<?>> dataToSync;

    @Before
    public void setUp() throws Exception {
        // Ensure all moments are created at the same timestamp to be able to compare their creation times.
        givenDateTimeIsFixed();

        initMocks(this);
        DataServicesManager.getInstance().setAppComponent(appComponantMock);
        when(ormMomentMock.getSynchronisationData()).thenReturn(ormSynchronisationDataMock);
        momentsSegregator = new MomentsSegregator();
        momentsSegregator.updatingInterface = updatingInterface;
        momentsSegregator.dbFetchingInterface = dbFetchingInterface;
        momentsSegregator.dbDeletingInterface = dbDeletingInterface;
        momentsSegregator.dbSavingInterface = dbSavingInterface;
        momentsSegregator.mBaseAppDataCreator = dataCreatorMock;

        moment = new OrmMoment(CREATOR_ID, SUBJECT_ID, new OrmMomentType(-1, MomentType.TEMPERATURE), NOW.plusMinutes(10));

        SynchronisationData ormSynchronisationData = new OrmSynchronisationData(GUID_ID, false, NOW, 1);
        moment.setSynchronisationData(ormSynchronisationData);
        momentList.add(moment);
    }

    @After
    public void tearDown() {
        givenDateTimeIsNotFixed();
    }

    @Test
    public void processMomentsReceivedFromBackend() throws SQLException {
        givenMomentsInDataBase();
        whenProcessMomentsReceivedFromBackendIsInvoked();
        thenAssertUpdateCountIs(0);
    }

    @Test
    public void processMomentsReceivedFromBackend_whenMomentInDBIsNull() throws SQLException {
        givenNullMomentsInDataBase();
        whenProcessMomentsReceivedFromBackendIsInvoked();
        thenAssertUpdateCountIs(1);
    }

    @Test
    public void processMomentsReceivedFromBackend_whenUpdatedVersion() throws SQLException {
        givenMomentsInDataBaseWithUpdatedVersion();
        whenProcessMomentsReceivedFromBackendIsInvoked();
        thenAssertUpdateCountIs(1);
    }

    @Test
    public void processMomentsReceivedFromBackend_whenMomentsDeletedFromDB() throws SQLException {
        givenMomentsInDataBaseWithMomentsDeletedFromDB();
        whenProcessMomentsReceivedFromBackendIsInvoked();
        thenAssertUpdateCountIs(1);
    }

    @Test
    public void processMomentsReceivedFromBackend_whenDeletedFromBackend() throws SQLException {
        givenMomentsIsDeletedFromBackend();
        whenProcessMomentsReceivedFromBackendIsInvoked();
        thenAssertUpdateCountIs(1);
    }

    @Test
    public void should_not_processMoment_when_momentExpired() throws SQLException {
        Moment moment1 = new OrmMoment(null, null, new OrmMomentType(-1, MomentType.TEMPERATURE), new DateTime().minusMinutes(1));
        SynchronisationData synchronisationData = new OrmSynchronisationData("1234", false, new DateTime().minus(1), 1);
        synchronisationData.setVersion(2);
        moment1.setSynchronisationData(synchronisationData);
        when(dbFetchingInterface.fetchMomentByGuid(synchronisationData.getGuid())).thenReturn(ormMomentMock);
        when(dbFetchingInterface.fetchMomentByGuid("1234")).thenReturn(ormMomentMock);
        when(ormSynchronisationDataMock.getGuid()).thenReturn("-1");
        int count = momentsSegregator.processMoments(Arrays.asList(moment1), dbRequestListener);
        assertEquals(0, count);
    }

    @Test
    public void putMomentsForSync() throws SQLException {
        givenMomentsInDataBase();
        whenPutMomentForSyncIsInvoked();
        thenVerifyDbFetchingInterfaceIsCalled();
        thenVerifyDataToSync();
    }

    @Test
    public void processCreatedMoment() throws SQLException {
        whenProcessCreatedMomentIsInvoked();
        thenVerifyDbSavingInterfaceIsCalled();
    }

    @Test
    public void processCreatedMoment_whenSQLException() throws SQLException {
        whenProcessCreatedMomentIsInvoked();
        thenVerifyDbSavingInterfaceIsCalled();
    }

    @Test
    public void momentTimeStampMustBeEqualWhenTimeIsFixed() throws InterruptedException {
        givenDateTimeIsFixed();
        when2ndMomentIsCreatedAfter5MilliSeconds();
        thenMomentsHaveSameTimestamp();
    }

    @Test
    public void momentTimeStampMustBeEqualWhenTimeIsNotFixed() throws InterruptedException {
        givenDateTimeIsNotFixed();
        when2ndMomentIsCreatedAfter5MilliSeconds();
        thenMomentsDoNotHaveSameTimestamp();
    }

    private void givenMomentsIsDeletedFromBackend() throws SQLException {
        Moment moment2 = momentList.get(0);
        SynchronisationData ormSynchronisationData2 = new OrmSynchronisationData(GUID_ID, true, NOW, 2);
        moment2.setSynchronisationData(ormSynchronisationData2);
        Moment moment3 = new OrmMoment(CREATOR_ID, SUBJECT_ID, new OrmMomentType(-1, MomentType.TEMPERATURE), NOW);
        SynchronisationData ormSynchronisationData3 = new OrmSynchronisationData(GUID_ID, true, NOW, 3);
        moment2.setSynchronisationData(ormSynchronisationData3);
        when((Moment) dbFetchingInterface.fetchMomentByGuid(GUID_ID)).thenReturn(moment3);
    }

    private void givenMomentsInDataBaseWithMomentsDeletedFromDB() throws SQLException {
        Moment moment2 = new OrmMoment(CREATOR_ID, SUBJECT_ID, new OrmMomentType(-1, MomentType.TEMPERATURE), NOW);
        SynchronisationData ormSynchronisationData2 = new OrmSynchronisationData(DELETED_GUID, false, NOW, 2);
        moment2.setSynchronisationData(ormSynchronisationData2);
        when((Moment) dbFetchingInterface.fetchMomentByGuid(GUID_ID)).thenReturn(moment2);
    }

    private void givenMomentsInDataBase() throws SQLException {
        when((Moment) dbFetchingInterface.fetchMomentByGuid(GUID_ID)).thenReturn(moment);
        List momentList = new ArrayList<>();
        momentList.add(moment);
        when((List<? extends Moment>) dbFetchingInterface.fetchNonSynchronizedMoments()).thenReturn(momentList);
    }

    private void givenNullMomentsInDataBase() throws SQLException {
        when((Moment) dbFetchingInterface.fetchMomentByGuid(GUID_ID)).thenReturn(null);
    }

    private void givenMomentsInDataBaseWithUpdatedVersion() throws SQLException {
        Moment moment2 = new OrmMoment(CREATOR_ID, SUBJECT_ID, new OrmMomentType(-1, MomentType.TEMPERATURE), NOW);
        SynchronisationData ormSynchronisationData2 = new OrmSynchronisationData(GUID_ID, false, NOW, 2);
        moment2.setSynchronisationData(ormSynchronisationData2);
        when((Moment) dbFetchingInterface.fetchMomentByGuid(GUID_ID)).thenReturn(moment2);
    }

    private void givenDateTimeIsFixed() {
        DateTimeUtils.setCurrentMillisFixed(DateTime.now().getMillis());
    }

    private void givenDateTimeIsNotFixed() {
        DateTimeUtils.setCurrentMillisSystem();
    }

    private void whenProcessMomentsReceivedFromBackendIsInvoked() throws SQLException {
        count = momentsSegregator.processMomentsReceivedFromBackend(momentList, dbRequestListener);
    }

    private void whenProcessCreatedMomentIsInvoked() {
        List momentList = new ArrayList<>();
        momentList.add(moment);
        momentsSegregator.processCreatedMoment(momentList, null);
    }

    private void whenPutMomentForSyncIsInvoked() {
        Map<Class, List<?>> dataToSync = new HashMap<>();
        dataToSync.put(Moment.class, Arrays.asList(moment));
        this.dataToSync = momentsSegregator.putMomentsForSync(dataToSync);
    }

    private void when2ndMomentIsCreatedAfter5MilliSeconds() throws InterruptedException {
        Thread.sleep(5);
        moment2 = new OrmMoment(CREATOR_ID, SUBJECT_ID, new OrmMomentType(-1, MomentType.TEMPERATURE), NOW);
    }

    private void thenVerifyDbFetchingInterfaceIsCalled() throws SQLException {
        verify(dbFetchingInterface).fetchNonSynchronizedMoments();
    }

    private void thenVerifyDbSavingInterfaceIsCalled() throws SQLException {
        verify(dbSavingInterface).saveMoment(moment, null);
    }

    private void thenVerifyDataToSync() {
        assertEquals(moment, dataToSync.get(Moment.class).get(0));
    }

    private void thenAssertUpdateCountIs(int count) {
        assertEquals(count, this.count);
    }

    private void thenMomentsHaveSameTimestamp() {
        assertTrue(moment.getDateTime().equals(moment2.getDateTime()));
    }

    private void thenMomentsDoNotHaveSameTimestamp() {
        assertFalse(moment.getDateTime().equals(moment2.getDateTime()));
    }
}
