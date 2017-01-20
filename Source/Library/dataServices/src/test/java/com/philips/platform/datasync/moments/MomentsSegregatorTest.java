package com.philips.platform.datasync.moments;

import com.philips.platform.core.datatypes.Moment;
import com.philips.platform.core.datatypes.SynchronisationData;
import com.philips.platform.core.dbinterfaces.DBDeletingInterface;
import com.philips.platform.core.dbinterfaces.DBFetchingInterface;
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
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.verify;
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
    DBFetchingInterface dbFetchingInterface;
    @Mock
    DBDeletingInterface dbDeletingInterface;
    @Mock
    private OrmMoment ormMomentMock;

    @Mock
    DBRequestListener dbRequestListener;

    @Before
    public void setUp() throws Exception {
        initMocks(this);
        DataServicesManager.getInstance().mAppComponent = appComponantMock;
        momentsSegregator = new MomentsSegregator();
        momentsSegregator.updatingInterface = updatingInterface;
        momentsSegregator.dbFetchingInterface = dbFetchingInterface;
        momentsSegregator.dbDeletingInterface = dbDeletingInterface;
    }

    @Test
    public void should_processMoment_when_processMomentsReceivedFromBackend_called(){
        Moment moment1 = new OrmMoment(null, null, new OrmMomentType(-1,MomentType.TEMPERATURE));
        int count = momentsSegregator.processMomentsReceivedFromBackend(Arrays.asList(moment1),dbRequestListener);
        assertEquals(count, 1);
    }

    @Test
    public void should_processMoment_when_processMomentsReceivedFromBackend_called_withSynData(){
        Moment moment1 = new OrmMoment(null, null, new OrmMomentType(-1,MomentType.TEMPERATURE));
        SynchronisationData synchronisationData = new OrmSynchronisationData("abc",false,new DateTime(),1);
        moment1.setSynchronisationData(synchronisationData);
        int count = momentsSegregator.processMomentsReceivedFromBackend(Arrays.asList(moment1),dbRequestListener);
        assertEquals(count, 1);
    }

    @Test
    public void should_processMoment_when_processMomentsReceivedFromBackend_called_whenNotActive(){
        Moment moment1 = new OrmMoment(null, null, new OrmMomentType(-1,MomentType.TEMPERATURE));
        SynchronisationData synchronisationData = new OrmSynchronisationData("abc",false,new DateTime(),1);
        moment1.setSynchronisationData(synchronisationData);
        int count = momentsSegregator.processMomentsReceivedFromBackend(Arrays.asList(moment1),dbRequestListener);
        assertEquals(count, 1);
    }

    @Test
    public void should_processCreatedMoment_update_only_sync_data() throws SQLException {
        Moment moment1 = new OrmMoment(null, null, new OrmMomentType(-1,MomentType.TEMPERATURE));
        SynchronisationData synchronisationData = new OrmSynchronisationData("abc",false,new DateTime(),1);
        moment1.setSynchronisationData(synchronisationData);
        momentsSegregator.processCreatedMoment(Arrays.asList(moment1),dbRequestListener);
        verify(updatingInterface).updateMoment(moment1,dbRequestListener);
    }

    /*@Test
    public void should_isNeverSyncedMomentDeletedLocallyDuringSync_returnsFalse() throws SQLException {
        OrmMoment moment1 = new OrmMoment(null, null, new OrmMomentType(-1,MomentType.TEMPERATURE));
        SynchronisationData synchronisationData = new OrmSynchronisationData("abc",false,new DateTime(),1);
        moment1.setSynchronisationData(synchronisationData);
        boolean isSynced = momentsSegregator.isNeverSyncedMomentDeletedLocallyDuringSync(moment1);
        assertEquals(isSynced, false);
    }

    @Test
    public void should_isNeverSyncedMomentDeletedLocallyDuringSync_returnsTrue() throws SQLException {
        OrmMoment moment1 = new OrmMoment(null, null, new OrmMomentType(-1,MomentType.TEMPERATURE));
        SynchronisationData synchronisationData = new OrmSynchronisationData(Moment.MOMENT_NEVER_SYNCED_AND_DELETED_GUID,true,new DateTime(),1);
        moment1.setSynchronisationData(synchronisationData);
        boolean isSynced = momentsSegregator.isNeverSyncedMomentDeletedLocallyDuringSync(moment1);
        assertEquals(isSynced, true);
    }*/

    @Test
    public void should_putMomentsForSync_return_data() throws SQLException {
        OrmMoment moment1 = new OrmMoment(null, null, new OrmMomentType(-1,MomentType.TEMPERATURE));
        Map<Class, List<?>> dataToSync = new HashMap<>();
        dataToSync.put(Moment.class,Arrays.asList(moment1));
        momentsSegregator.putMomentsForSync(dataToSync);
        verify(dbFetchingInterface).fetchNonSynchronizedMoments();
    }

}
