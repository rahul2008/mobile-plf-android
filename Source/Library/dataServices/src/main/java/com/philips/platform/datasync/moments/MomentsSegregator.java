package com.philips.platform.datasync.moments;

import com.philips.platform.core.datatypes.Moment;
import com.philips.platform.core.datatypes.SynchronisationData;
import com.philips.platform.core.dbinterfaces.DBDeletingInterface;
import com.philips.platform.core.dbinterfaces.DBFetchingInterface;
import com.philips.platform.core.dbinterfaces.DBSavingInterface;
import com.philips.platform.core.dbinterfaces.DBUpdatingInterface;
import com.philips.platform.core.listeners.DBRequestListener;
import com.philips.platform.core.trackers.DataServicesManager;
import com.philips.platform.core.utils.DSLog;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

@SuppressWarnings({"rawtypes", "unchecked"})
public class MomentsSegregator {

    @Inject
    DBUpdatingInterface updatingInterface;
    @Inject
    DBFetchingInterface dbFetchingInterface;
    @Inject
    DBDeletingInterface dbDeletingInterface;
    @Inject
    DBSavingInterface dbSavingInterface;


    public MomentsSegregator(){
        DataServicesManager.getInstance().getAppComponant().injectMomentsSegregator(this);
    }

    public int processMomentsReceivedFromBackend(final List<? extends Moment> moments,DBRequestListener dbRequestListener) throws SQLException {
        int updatedCount = 0;
        for (final Moment moment : moments) {
                updatedCount = processMoment(updatedCount, moment ,dbRequestListener);
        }
        return updatedCount;
    }

    private Moment getOrmMomentFromDatabase(Moment moment,DBRequestListener dbRequestListener) throws SQLException {
        Moment momentInDatabase = null;
        final SynchronisationData synchronisationData = moment.getSynchronisationData();

        if (synchronisationData != null) {
            momentInDatabase = (Moment) dbFetchingInterface.fetchMomentByGuid(synchronisationData.getGuid());
            if (momentInDatabase == null) {
                momentInDatabase = (Moment) dbFetchingInterface.fetchMomentById(moment.getId(),null);
            }
        }
        return momentInDatabase;
    }

    private int getVersionInDatabase(final Moment momentInDatabase) {
        if (momentInDatabase != null && momentInDatabase.getSynchronisationData() != null) {
            return momentInDatabase.getSynchronisationData().getVersion();
        }
        return -1;
    }

    private boolean hasDifferentMomentVersion(final Moment moment,
                                              final Moment momentInDatabase) throws SQLException {
        boolean isVersionDifferent = true;
        final SynchronisationData synchronisationData = moment.getSynchronisationData();

        if (synchronisationData != null) {
            final int versionInDatabase = getVersionInDatabase(momentInDatabase);
            if (versionInDatabase != -1) {
                isVersionDifferent = versionInDatabase != synchronisationData.getVersion();
            }
        }
        return isVersionDifferent;
    }

    private boolean isActive(final SynchronisationData synchronisationData) {
        return synchronisationData == null || !synchronisationData.isInactive();
    }

    private void deleteMomentInDatabaseIfExists(final Moment momentInDatabase,DBRequestListener dbRequestListener)
            throws SQLException {
        if (momentInDatabase != null) {
            dbDeletingInterface.deleteMoment(momentInDatabase,dbRequestListener);
        }
    }

    private void deleteMomentsInDatabaseIfExists(final List<Moment> momentsInDatabase,DBRequestListener dbRequestListener)
            throws SQLException {
            dbDeletingInterface.deleteMoments(momentsInDatabase,dbRequestListener);
    }

    protected boolean MomentDeletedLocallyDuringSync(final Moment momentInDatabase) {
        if (momentInDatabase != null) {
            final SynchronisationData synchronisationData = momentInDatabase.getSynchronisationData();
            if (synchronisationData != null) {
                return synchronisationData.getGuid().
                        equals(Moment.MOMENT_NEVER_SYNCED_AND_DELETED_GUID);
            }
        }
        return false;
    }

    public int processMoment(int count, final Moment moment, DBRequestListener dbRequestListener) throws SQLException {
        //try {
        DSLog.i(DSLog.LOG,"1");
            final Moment momentInDatabase = getOrmMomentFromDatabase(moment,dbRequestListener);
        DSLog.i(DSLog.LOG,"2");
            if (hasDifferentMomentVersion(moment, momentInDatabase)) {
                DSLog.i(DSLog.LOG,"3");
                if (!isActive(moment.getSynchronisationData())) {
                    DSLog.i(DSLog.LOG,"4");
                    deleteMomentInDatabaseIfExists(momentInDatabase,dbRequestListener);
                    DSLog.i(DSLog.LOG,"5");
                } else if (MomentDeletedLocallyDuringSync(momentInDatabase)) {
                    DSLog.i(DSLog.LOG,"6");
                    moment.setSynced(false);
                    DSLog.i(DSLog.LOG,"7");
                    moment.getSynchronisationData().setInactive(true);
                    DSLog.i(DSLog.LOG,"8");

                    deleteAndSaveMoment(momentInDatabase, moment,dbRequestListener);
                    DSLog.i(DSLog.LOG,"9");
                } else {
                    DSLog.i(DSLog.LOG,"10");
                    if (!isMomentModifiedLocallyDuringSync(momentInDatabase, moment)) {
                        DSLog.i(DSLog.LOG,"11");
                        moment.setSynced(true);
                        DSLog.i(DSLog.LOG,"12");
                    }
                    //This is required for deleting duplicate
                    // measurements, measurementDetails and momentDetails
                    DSLog.i(DSLog.LOG,"13");
                    deleteAndSaveMoment(momentInDatabase, moment,dbRequestListener);
                    DSLog.i(DSLog.LOG,"14");
                }
                DSLog.i(DSLog.LOG,"15");
                count++;
                DSLog.i(DSLog.LOG,"16");
            } else {
                DSLog.i(DSLog.LOG,"17");
            }
        /*} catch (SQLException e) {
            updatingInterface.updateFailed(e,dbRequestListener);
        }*/
        DSLog.i(DSLog.LOG,"18");
        return count;
    }

    private boolean isMomentModifiedLocallyDuringSync(final Moment momentInDatabase,
                                                      final Moment ormMoment) {
        return momentInDatabase != null &&
                !ormMoment.getDateTime().equals(momentInDatabase.getDateTime());
    }

    private void deleteMeasurementAndMomentDetailsAndSetId(final Moment momentInDatabase,Moment ormMoment,DBRequestListener dbRequestListener) throws SQLException {
        if (momentInDatabase != null) {
            dbDeletingInterface.deleteMomentDetail(momentInDatabase,dbRequestListener);
            dbDeletingInterface.deleteMeasurementGroup(momentInDatabase,dbRequestListener);
        }
    }

    private void deleteAndSaveMoment(final Moment momentInDatabase,
                                     final Moment ormMoment,DBRequestListener dbRequestListener) throws SQLException {

        if (momentInDatabase != null) {
            ormMoment.setId(momentInDatabase.getId());
            //updatingInterface.updateMoment(ormMoment,null);
            //return;
        }
        deleteMeasurementAndMomentDetailsAndSetId(momentInDatabase,ormMoment,dbRequestListener);
        dbSavingInterface.saveMoment(ormMoment,dbRequestListener);
    }

    private void deleteAndSaveMoments(final List<Moment> moments,DBRequestListener dbRequestListener) throws SQLException {

        for(Moment moment:moments) {
            final Moment momentInDatabase = getOrmMomentFromDatabase(moment, dbRequestListener);
            deleteMeasurementAndMomentDetailsAndSetId(momentInDatabase,moment,dbRequestListener);
        }
        dbSavingInterface.saveMoments(moments,null);
    }

    public void processCreatedMoment(List<? extends Moment> moments,DBRequestListener dbRequestListener) {
        for (final Moment moment : moments) {
                moment.setSynced(true);
            try {
                dbSavingInterface.saveMoment(moment,dbRequestListener);
            } catch (SQLException e) {
                updatingInterface.updateFailed(e,dbRequestListener);
                e.printStackTrace();
            }
        }
    }

    public Map<Class, List<?>> putMomentsForSync(final Map<Class, List<?>> dataToSync) {
        DSLog.i(DSLog.LOG, "In OrmFetchingInterfaceImpl before fetchNonSynchronizedMoments");
        List<? extends Moment> ormMomentList = null;
        try {
            ormMomentList = (List<? extends Moment>) dbFetchingInterface.fetchNonSynchronizedMoments();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        DSLog.i(DSLog.LOG, "In OrmFetchingInterfaceImpl dataToSync.put");
        dataToSync.put(Moment.class, ormMomentList);
        return dataToSync;
    }

}
