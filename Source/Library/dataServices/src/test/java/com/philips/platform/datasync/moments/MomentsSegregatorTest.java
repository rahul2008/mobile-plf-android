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
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

/**
 * Created by 310218660 on 1/10/2017.
 */

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
    private static final DateTime NOW = new DateTime();

    private List<Moment> momentList = new ArrayList<>();
    private int count;
    private Moment moment;

    @Before
    public void setUp() throws Exception {
        initMocks(this);
        DataServicesManager.getInstance().setAppComponant(appComponantMock);
        when(ormMomentMock.getSynchronisationData()).thenReturn(ormSynchronisationDataMock);
        momentsSegregator = new MomentsSegregator();
        momentsSegregator.updatingInterface = updatingInterface;
        momentsSegregator.dbFetchingInterface = dbFetchingInterface;
        momentsSegregator.dbDeletingInterface = dbDeletingInterface;
        momentsSegregator.dbSavingInterface = dbSavingInterface;
        momentsSegregator.mBaseAppDataCreator = dataCreatorMock;

        moment = new OrmMoment(CREATOR_ID, SUBJECT_ID, new OrmMomentType(-1, MomentType.TEMPERATURE), NOW);
        SynchronisationData ormSynchronisationData = new OrmSynchronisationData(GUID_ID, false, NOW, 1);
        moment.setSynchronisationData(ormSynchronisationData);
        momentList.add(moment);

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

    private void givenMomentsInDataBase() throws SQLException {
        when((Moment) dbFetchingInterface.fetchMomentByGuid(GUID_ID)).thenReturn(moment);
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

    private void whenProcessMomentsReceivedFromBackendIsInvoked() throws SQLException {
        count = momentsSegregator.processMomentsReceivedFromBackend(momentList, dbRequestListener);
    }

    private void thenAssertUpdateCountIs(int count) {
        assertEquals(count, this.count);
    }

    @Test
    public void should_processMoment_when_processMomentsReceivedFromBackend_called() throws SQLException {
        int count = 1;
        Moment moment1 = new OrmMoment(null, null, new OrmMomentType(-1, MomentType.TEMPERATURE), null);
        momentsSegregator.processMoments(Arrays.asList(moment1), dbRequestListener);
        assertEquals(count, 1);
    }

    @Test
    public void should_processMoment_when_processMomentsReceivedFromBackend_called_not_active() throws SQLException {
        int count = 1;

        when(dbFetchingInterface.fetchMomentByGuid("1234")).thenReturn(ormMomentMock);

        Moment moment1 = new OrmMoment(null, null, new OrmMomentType(-1, MomentType.TEMPERATURE), null);
        SynchronisationData synchronisationData = new OrmSynchronisationData("1234", true, new DateTime().minus(1), 1);
        synchronisationData.setInactive(true);
        moment1.setSynchronisationData(synchronisationData);
        momentsSegregator.processMoments(Arrays.asList(moment1), dbRequestListener);
        assertEquals(count, 1);
    }

    @Test
    public void should_processMoment_when_processMomentsReceivedFromBackend_Moment_deleted_locally() throws SQLException {
        int count = 1;
        Moment moment1 = new OrmMoment(null, null, new OrmMomentType(-1, MomentType.TEMPERATURE), null);
        SynchronisationData synchronisationData = new OrmSynchronisationData("1234", false, new DateTime().minus(1), 1);
        synchronisationData.setVersion(2);
        moment1.setSynchronisationData(synchronisationData);

        when(dbFetchingInterface.fetchMomentByGuid(synchronisationData.getGuid())).thenReturn(ormMomentMock);

        when(dbFetchingInterface.fetchMomentByGuid("1234")).thenReturn(ormMomentMock);
        when(ormSynchronisationDataMock.getGuid()).thenReturn("-1");
        momentsSegregator.processMoments(Arrays.asList(moment1), dbRequestListener);
        assertEquals(count, 1);
    }

    @Test
    public void should_processMoment_when_processMomentsReceivedFromBackend_called_withSynData() throws SQLException {
        Moment moment1 = new OrmMoment(null, null, new OrmMomentType(-1, MomentType.TEMPERATURE), null);
        SynchronisationData synchronisationData = new OrmSynchronisationData("abc", false, new DateTime(), 1);
        moment1.setSynchronisationData(synchronisationData);
        int count = momentsSegregator.processMomentsReceivedFromBackend(Arrays.asList(moment1), dbRequestListener);
        assertEquals(count, 1);
    }

    @Test
    public void should_processMoment_when_processMomentsReceivedFromBackend_called_whenNotActive() throws SQLException {
        Moment moment1 = new OrmMoment(null, null, new OrmMomentType(-1, MomentType.TEMPERATURE), null);
        SynchronisationData synchronisationData = new OrmSynchronisationData("abc", false, new DateTime(), 1);
        moment1.setSynchronisationData(synchronisationData);
        int count = momentsSegregator.processMomentsReceivedFromBackend(Arrays.asList(moment1), dbRequestListener);
        assertEquals(count, 1);
    }

    @Test
    public void should_processCreatedMoment_update_only_sync_data() throws SQLException {
        Moment moment1 = new OrmMoment(null, null, new OrmMomentType(-1, MomentType.TEMPERATURE), null);
        SynchronisationData synchronisationData = new OrmSynchronisationData("abc", false, new DateTime(), 1);
        moment1.setSynchronisationData(synchronisationData);
        momentsSegregator.processCreatedMoment(Arrays.asList(moment1), dbRequestListener);
        verify(dbSavingInterface).saveMoment(moment1, dbRequestListener);
    }

    @Test
    public void should_processCreatedMoment_save_data_throws_exception() throws SQLException {
        Moment moment1 = new OrmMoment(null, null, new OrmMomentType(-1, MomentType.TEMPERATURE), null);
        SynchronisationData synchronisationData = new OrmSynchronisationData("abc", false, new DateTime(), 1);
        moment1.setSynchronisationData(synchronisationData);

        SQLException exception = new SQLException();
        doThrow(exception).when(dbSavingInterface).saveMoment(moment1, dbRequestListener);

        momentsSegregator.processCreatedMoment(Arrays.asList(moment1), dbRequestListener);
        verify(dbSavingInterface).saveMoment(moment1, dbRequestListener);
    }

    @Test
    public void should_putMomentsForSync_return_data() throws SQLException {
        OrmMoment moment1 = new OrmMoment(null, null, new OrmMomentType(-1, MomentType.TEMPERATURE), null);
        Map<Class, List<?>> dataToSync = new HashMap<>();
        dataToSync.put(Moment.class, Arrays.asList(moment1));
        momentsSegregator.putMomentsForSync(dataToSync);
        verify(dbFetchingInterface).fetchNonSynchronizedMoments();
    }

    @Test
    public void should_putMomentsForSync_throw_exception() throws SQLException {
        OrmMoment moment1 = new OrmMoment(null, null, new OrmMomentType(-1, MomentType.TEMPERATURE), null);
        Map<Class, List<?>> dataToSync = new HashMap<>();
        dataToSync.put(Moment.class, Arrays.asList(moment1));

        SQLException exception = new SQLException();
        doThrow(exception).when(dbFetchingInterface).fetchNonSynchronizedMoments();

        momentsSegregator.putMomentsForSync(dataToSync);
        verify(dbFetchingInterface).fetchNonSynchronizedMoments();
    }

    @Test
    public void should_deleteAndSaveMoments_called() throws SQLException {
        OrmMoment moment1 = new OrmMoment(null, null, new OrmMomentType(-1, MomentType.TEMPERATURE), null);
        List listOfMoments = new ArrayList();
        listOfMoments.add(moment1);

        SQLException exception = new SQLException();
        doThrow(exception).when(dbFetchingInterface).fetchNonSynchronizedMoments();

        momentsSegregator.deleteAndSaveMoments(listOfMoments, dbRequestListener);
        verify(dbSavingInterface).saveMoments(listOfMoments, null);
    }

   /* @Test
    public void should_deleteMomentsInDatabaseIfExists_called() throws SQLException {
        OrmMoment moment1 = new OrmMoment(null, null, new OrmMomentType(-1,MomentType.TEMPERATURE), null);
        List listOfMoments = new ArrayList();
        listOfMoments.add(moment1);

        momentsSegregator.deleteMomentsInDatabaseIfExists(listOfMoments,dbRequestListener);
        verify(dbDeletingInterface).deleteMoments(listOfMoments, dbRequestListener);
    }*/

}
