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
import com.philips.platform.util.MomentListVersionMatcher;
import com.philips.platform.util.MomentsListSizeMatcher;
import com.philips.testing.verticals.datatyes.MomentType;
import com.philips.testing.verticals.table.OrmMoment;
import com.philips.testing.verticals.table.OrmMomentType;
import com.philips.testing.verticals.table.OrmSynchronisationData;

import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class MomentsSegregatorTest {

    @Mock
    private AppComponent appComponentMock;
    @Mock
    private DBUpdatingInterface updatingInterface;
    @Mock
    private DBSavingInterface savingInterface;
    @Mock
    private DBFetchingInterface fetchingInterface;
    @Mock
    private DBDeletingInterface deletingInterface;
    @Mock
    private OrmMoment ormMomentMock;
    @Mock
    private OrmSynchronisationData ormSynchronisationDataMock;
    @Mock
    private DBRequestListener<Moment> dbRequestListener;
    @Mock
    private BaseAppDataCreator dataCreatorMock;

    // Subject of this test
    private MomentsSegregator momentsSegregator;

    private static final String CREATOR_ID = "creator";
    private static final String SUBJECT_ID = "SUBJECT";
    private static final String GUID_ID = UUID.randomUUID().toString();
    private static final String DELETED_GUID = "-1";
    private static final DateTime NOW = new DateTime();
    private static final DateTime BEFORE_NOW = new DateTime().minusDays(1);
    private static final DateTime DATE_TIME = new DateTime("2015-01-10");

    private List<Moment> momentsFromBackend = new ArrayList<>();
    private int count;
    private Moment moment;
    private Moment momentWithoutExpirationDate;
    private Map<Class, List<?>> dataToSync;
    private SynchronisationData momentSyncData;

    @Before
    public void setUp() throws Exception {
        initMocks(this);
        DataServicesManager.getInstance().setAppComponent(appComponentMock);
        when(ormMomentMock.getSynchronisationData()).thenReturn(ormSynchronisationDataMock);
        momentsSegregator = new MomentsSegregator();
        momentsSegregator.updatingInterface = updatingInterface;
        momentsSegregator.fetchingInterface = fetchingInterface;
        momentsSegregator.deletingInterface = deletingInterface;
        momentsSegregator.savingInterface = savingInterface;
        momentsSegregator.mBaseAppDataCreator = dataCreatorMock;

        momentSyncData = new OrmSynchronisationData(GUID_ID, false, NOW, 1);
        whenCreatingMoment();
        moment.setSynchronisationData(momentSyncData);
        momentsFromBackend.add(moment);

        momentWithoutExpirationDate = new OrmMoment(CREATOR_ID, SUBJECT_ID, new OrmMomentType(-1, MomentType.TEMPERATURE), null);
    }

    @Test
    public void processMomentsReceivedFromBackend() throws SQLException {
        givenMomentInDatabase(moment);
        whenProcessMomentsReceivedFromBackendIsInvoked(momentsFromBackend);
        thenAssertUpdateCountIs(0);
    }

    @Test
    public void processMomentsReceivedFromBackend_whenMomentInDBIsNull() throws SQLException {
        givenMomentInDatabase(null);
        whenProcessMomentsReceivedFromBackendIsInvoked(momentsFromBackend);
        thenAssertUpdateCountIs(1);
    }

    @Test
    public void processMomentsReceivedFromBackend_whenUpdatedVersion() throws SQLException {
        givenMomentsInDataBaseWithUpdatedVersion(2);
        whenProcessMomentsReceivedFromBackendIsInvoked(momentsFromBackend);
        thenAssertUpdateCountIs(1);
    }

    @Test
    public void processMomentsReceivedFromBackend_whenUpdatedVersion_shouldCallSavingInterface() throws SQLException {
        givenMomentsInDataBaseWithUpdatedVersion(2);

        whenProcessMomentsReceivedFromBackendIsInvoked(momentsFromBackend);

        //noinspection unchecked
        verify(savingInterface).saveMoments((List<Moment>) any(), eq(dbRequestListener));
        verify(deletingInterface).deleteMeasurementGroup((Moment) any(), eq(dbRequestListener));
        verify(deletingInterface).deleteMomentDetail((Moment) any(), eq(dbRequestListener));
    }

    @Test
    public void processMomentsReceivedFromBackend_whenTimeStampIsUpdatedInBackend_shouldUpdateInApp() throws SQLException {
        givenMomentsInDataBaseWithDifferentTimestamp();

        whenProcessMomentsReceivedFromBackendIsInvoked(momentsFromBackend);

        //noinspection unchecked
        verify(savingInterface).saveMoments((List<Moment>) any(), eq(dbRequestListener));
        verify(deletingInterface).deleteMeasurementGroup((Moment) any(), eq(dbRequestListener));
        verify(deletingInterface).deleteMomentDetail((Moment) any(), eq(dbRequestListener));
    }

    @Test
    public void processMomentsReceivedFromBackend_whenUpdatedVersion_shouldUpdateMoments() throws SQLException {
        givenMomentsInDataBaseWithUpdatedVersion(2);

        whenProcessMomentsReceivedFromBackendIsInvoked(momentsFromBackend);

        verify(savingInterface).saveMoments(argThat(new MomentsListSizeMatcher(1)), eq(dbRequestListener));
    }

    @Test
    public void processMomentsReceivedFromBackend_whenUpdatedVersion_shouldSelectHighestMomentVersion() throws SQLException {
        givenMomentsInDataBaseWithUpdatedVersion(2);

        whenProcessMomentsReceivedFromBackendIsInvoked(momentsFromBackend);

        verify(savingInterface).saveMoments(argThat(new MomentListVersionMatcher(2)), eq(dbRequestListener));
    }

    @Test
    public void processMomentsReceivedFromBackend_whenUpdatedVersionOnServer() throws SQLException {
        momentSyncData.setVersion(2);
        givenMomentsInDataBaseWithUpdatedVersion(1);

        whenProcessMomentsReceivedFromBackendIsInvoked(momentsFromBackend);

        thenAssertUpdateCountIs(1);
    }

    @Test
    public void processMomentsReceivedFromBackend_whenUpdatedVersionOnServer_shouldSelectHighestMomentVersion() throws SQLException {
        momentSyncData.setVersion(2);
        givenMomentsInDataBaseWithUpdatedVersion(1);

        whenProcessMomentsReceivedFromBackendIsInvoked(momentsFromBackend);

        //noinspection unchecked
        verify(savingInterface).saveMoments((List<Moment>) any(), eq(dbRequestListener));
    }

    @Test
    public void processMomentsReceivedFromBackend_whenUpdatedVersionOnServer_shouldVerifySavingInterfaceCalled() throws SQLException {
        momentSyncData.setVersion(2);
        givenMomentsInDataBaseWithUpdatedVersion(1);

        whenProcessMomentsReceivedFromBackendIsInvoked(momentsFromBackend);

        verify(savingInterface).saveMoments(argThat(new MomentListVersionMatcher(2)), eq(dbRequestListener));
    }

    @Test
    public void processMomentsReceivedFromBackend_whenMomentsDeletedFromDB() throws SQLException {
        givenMomentsInDataBaseWithMomentsDeletedFromDB();
        whenProcessMomentsReceivedFromBackendIsInvoked(momentsFromBackend);
        thenAssertUpdateCountIs(1);
    }

    @Test
    public void processMomentsReceivedFromBackend_whenDeletedFromBackend() throws SQLException {
        givenMomentsIsDeletedFromBackend();
        whenProcessMomentsReceivedFromBackendIsInvoked(momentsFromBackend);
        thenAssertUpdateCountIs(1);
    }

    @Test
    public void processMomentsReceivedFromBackend_shouldProcessExpirationDate() throws SQLException {
        givenMomentsInDataBaseWithoutExpirationDate();
        whenProcessMomentsReceivedFromBackendIsInvoked(momentsFromBackend);
        thenAssertUpdateCountIs(1);
    }

    @Test
    public void should_not_processMoment_when_momentExpired() throws SQLException {
        Moment moment1 = new OrmMoment("", "", new OrmMomentType(-1, MomentType.TEMPERATURE), new DateTime().minusMinutes(1));
        SynchronisationData synchronisationData = new OrmSynchronisationData("1234", false, new DateTime().minus(1), 1);
        synchronisationData.setVersion(2);
        moment1.setSynchronisationData(synchronisationData);
        when(fetchingInterface.fetchMomentByGuid(synchronisationData.getGuid())).thenReturn(ormMomentMock);
        when(fetchingInterface.fetchMomentByGuid("1234")).thenReturn(ormMomentMock);
        when(ormSynchronisationDataMock.getGuid()).thenReturn("-1");
        int count = momentsSegregator.processMoments(Collections.singletonList(moment1), dbRequestListener);
        assertEquals(0, count);
    }

    @Test
    public void putMomentsForSync() throws SQLException {
        givenMomentsToSynchronizeInDatabase(moment);
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

    private void givenMomentsInDataBaseWithoutExpirationDate() throws SQLException {
        SynchronisationData ormSynchronisationData = new OrmSynchronisationData(GUID_ID, false, BEFORE_NOW, 1);
        momentWithoutExpirationDate.setSynchronisationData(ormSynchronisationData);
        when((Moment) fetchingInterface.fetchMomentByGuid(GUID_ID)).thenReturn(momentWithoutExpirationDate);
        List momentList = new ArrayList<>();
        momentList.add(momentWithoutExpirationDate);
        when((List<? extends Moment>) fetchingInterface.fetchNonSynchronizedMoments()).thenReturn(momentList);
    }

    private void givenMomentsIsDeletedFromBackend() throws SQLException {
        Moment moment2 = momentsFromBackend.get(0);
        SynchronisationData ormSynchronisationData2 = new OrmSynchronisationData(GUID_ID, true, NOW, 2);
        moment2.setSynchronisationData(ormSynchronisationData2);
        Moment moment3 = new OrmMoment(CREATOR_ID, SUBJECT_ID, new OrmMomentType(-1, MomentType.TEMPERATURE), NOW);
        SynchronisationData ormSynchronisationData3 = new OrmSynchronisationData(GUID_ID, true, NOW, 3);
        moment2.setSynchronisationData(ormSynchronisationData3);
        when((Moment) fetchingInterface.fetchMomentByGuid(GUID_ID)).thenReturn(moment3);
    }

    private void givenMomentsInDataBaseWithMomentsDeletedFromDB() throws SQLException {
        Moment moment2 = new OrmMoment(CREATOR_ID, SUBJECT_ID, new OrmMomentType(-1, MomentType.TEMPERATURE), NOW);
        SynchronisationData ormSynchronisationData2 = new OrmSynchronisationData(DELETED_GUID, false, NOW, 2);
        moment2.setSynchronisationData(ormSynchronisationData2);
        when((Moment) fetchingInterface.fetchMomentByGuid(GUID_ID)).thenReturn(moment2);
    }

    private void givenMomentInDatabase(final Moment moment) throws SQLException {
        when((Moment) fetchingInterface.fetchMomentByGuid(GUID_ID)).thenReturn(moment);
    }

    private void givenMomentsToSynchronizeInDatabase(final Moment moment) throws SQLException {
        List momentList = Collections.singletonList(moment);
        when(fetchingInterface.fetchNonSynchronizedMoments()).thenReturn(momentList);
    }

    private void givenMomentsInDataBaseWithDifferentTimestamp() throws SQLException {
        Moment dbMoment = new OrmMoment(moment.getCreatorId(),moment.getSubjectId(),new OrmMomentType(-1, MomentType.TEMPERATURE), null);
        dbMoment.setDateTime(DATE_TIME);
        SynchronisationData syncData = new OrmSynchronisationData(GUID_ID, false, BEFORE_NOW, 1);
        dbMoment.setSynchronisationData(syncData);
        givenMomentInDatabase(dbMoment);
    }

    private void givenMomentsInDataBaseWithUpdatedVersion(int existingMomentVersion) throws SQLException {
        Moment moment2 = new OrmMoment(CREATOR_ID, SUBJECT_ID, new OrmMomentType(-1, MomentType.TEMPERATURE), NOW);
        SynchronisationData ormSynchronisationData2 = new OrmSynchronisationData(GUID_ID, false, BEFORE_NOW, existingMomentVersion);
        moment2.setSynchronisationData(ormSynchronisationData2);
        when((Moment) fetchingInterface.fetchMomentByGuid(GUID_ID)).thenReturn(moment2);
    }

    private void whenProcessMomentsReceivedFromBackendIsInvoked(final List<Moment> moments) throws SQLException {
        count = momentsSegregator.processMomentsReceivedFromBackend(moments, dbRequestListener);
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

    private void whenCreatingMoment() {
        moment = new OrmMoment(CREATOR_ID, SUBJECT_ID, new OrmMomentType(-1, MomentType.TEMPERATURE), NOW.plusMinutes(10));
    }

    private void thenVerifyDbFetchingInterfaceIsCalled() throws SQLException {
        verify(fetchingInterface).fetchNonSynchronizedMoments();
    }

    private void thenVerifyDbSavingInterfaceIsCalled() throws SQLException {
        verify(savingInterface).saveMoment(moment, null);
    }

    private void thenVerifyDataToSync() {
        assertEquals(moment, dataToSync.get(Moment.class).get(0));
    }

    private void thenAssertUpdateCountIs(int count) {
        assertEquals(count, this.count);
    }
}
