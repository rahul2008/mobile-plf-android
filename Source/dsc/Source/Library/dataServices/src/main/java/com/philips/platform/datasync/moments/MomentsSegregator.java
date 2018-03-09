/* Copyright (c) Koninklijke Philips N.V., 2017
* All rights are reserved. Reproduction or dissemination
* in whole or in part is prohibited without the prior written
* consent of the copyright holder.
*/
package com.philips.platform.datasync.moments;

import com.philips.platform.core.BaseAppDataCreator;
import com.philips.platform.core.datatypes.Moment;
import com.philips.platform.core.datatypes.SynchronisationData;
import com.philips.platform.core.dbinterfaces.DBDeletingInterface;
import com.philips.platform.core.dbinterfaces.DBFetchingInterface;
import com.philips.platform.core.dbinterfaces.DBSavingInterface;
import com.philips.platform.core.dbinterfaces.DBUpdatingInterface;
import com.philips.platform.core.listeners.DBRequestListener;
import com.philips.platform.core.trackers.DataServicesManager;

import org.joda.time.DateTime;

import java.sql.SQLException;
import java.util.ArrayList;
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
    @Inject
    BaseAppDataCreator mBaseAppDataCreator;

    public MomentsSegregator() {
        DataServicesManager.getInstance().getAppComponent().injectMomentsSegregator(this);
    }

    public int processMomentsReceivedFromBackend(final List<Moment> moments, DBRequestListener<Moment> dbRequestListener) throws SQLException {
        int updatedCount = 0;
        updatedCount = processMoments(moments, dbRequestListener);
        return updatedCount;
    }

    public int processMoments(final List<Moment> momentList, DBRequestListener<Moment> dbRequestListener) throws SQLException {
        List<Moment> momentsToCreate = new ArrayList<>();
        List<Moment> momentsToUpdate = new ArrayList<>();
        List<Moment> momentsToDelete = new ArrayList<>();

        for (Moment moment : momentList) {
            if (moment.getExpirationDate() == null || moment.getExpirationDate().isAfterNow()) {
                final Moment momentInDatabase = getOrmMomentFromDatabase(moment);
                if (momentInDatabase == null) {
                    if (moment.getSynchronisationData() != null && !(moment.getSynchronisationData().isInactive())) {
                        createNewMomentInDB(momentsToCreate, moment);
                    }
                } else if (hasDifferentMomentVersion(moment, momentInDatabase) || hasNoExpirationDate(momentInDatabase)) {
                    syncAlreadyExistingMomentInDB(momentsToUpdate, momentsToDelete, moment, momentInDatabase);
                }
            }
        }
        if (momentsToCreate.size() > 0)
            dbSavingInterface.saveMoments(momentsToCreate, dbRequestListener);
        if (momentsToDelete.size() > 0)
            dbDeletingInterface.deleteMoments(momentsToDelete, dbRequestListener);
        if (momentsToUpdate.size() > 0)
            deleteAndSaveMoments(momentsToUpdate, dbRequestListener);

        return momentsToCreate.size() + momentsToDelete.size() + momentsToUpdate.size();
    }

    public void processCreatedMoment(List<? extends Moment> moments, DBRequestListener<Moment> dbRequestListener) {
        for (final Moment moment : moments) {
            moment.setSynced(true);
            try {
                dbSavingInterface.saveMoment(moment, dbRequestListener);
            } catch (SQLException e) {
                updatingInterface.updateFailed(e, dbRequestListener);
            }
        }
    }

    public Map<Class, List<?>> putMomentsForSync(final Map<Class, List<?>> dataToSync) {
        List<? extends Moment> ormMomentList = null;
        try {
            ormMomentList = (List<? extends Moment>) dbFetchingInterface.fetchNonSynchronizedMoments();
        } catch (SQLException e) {
            //Debug Log
        }
        dataToSync.put(Moment.class, ormMomentList);
        return dataToSync;
    }

    protected void deleteAndSaveMoments(final List<Moment> moments, DBRequestListener<Moment> dbRequestListener) throws SQLException {

        for (Moment moment : moments) {
            final Moment momentInDatabase = getOrmMomentFromDatabase(moment);
            deleteMeasurementAndMomentDetailsAndSetId(momentInDatabase, moment, dbRequestListener);
        }
        dbSavingInterface.saveMoments(moments, null);
    }

    private void createNewMomentInDB(List<Moment> momentsToCreate, Moment moment) {
        SynchronisationData synchronisationData =
                mBaseAppDataCreator.createSynchronisationData(moment.getSynchronisationData().getGuid(), moment.getSynchronisationData().isInactive(),
                        new DateTime(moment.getDateTime()), moment.getSynchronisationData().getVersion());
        moment.setSynchronisationData(synchronisationData);
        moment.setSynced(true);
        momentsToCreate.add(moment);
    }

    private void syncAlreadyExistingMomentInDB(List<Moment> momentsToUpdate, List<Moment> momentsToDelete, Moment moment, Moment momentInDatabase) {
        if (isMomentDeletedFromBackend(moment.getSynchronisationData())) {
            momentsToDelete.add(momentInDatabase);
        } else if (isMomentDeletedFromApplicationDB(momentInDatabase)) {
            moment.setSynced(false);
            moment.getSynchronisationData().setInactive(true);
            moment.setId(moment.getId());
            momentsToUpdate.add(moment);
        } else if (!isMomentUpdatedFromBackend(moment, momentInDatabase)) {
            moment.setSynced(true);
            moment.setId(momentInDatabase.getId());
            momentsToUpdate.add(moment);
        }
    }

    private void deleteMeasurementAndMomentDetailsAndSetId(final Moment momentInDatabase, Moment ormMoment, DBRequestListener<Moment> dbRequestListener) throws SQLException {
        if (momentInDatabase != null) {
            dbDeletingInterface.deleteMomentDetail(momentInDatabase, dbRequestListener);
            dbDeletingInterface.deleteMeasurementGroup(momentInDatabase, dbRequestListener);
        }
    }

    private Moment getOrmMomentFromDatabase(Moment moment) throws SQLException {
        Moment momentInDatabase = null;
        final SynchronisationData synchronisationData = moment.getSynchronisationData();

        if (synchronisationData != null) {
            momentInDatabase = (Moment) dbFetchingInterface.fetchMomentByGuid(synchronisationData.getGuid());
            if (momentInDatabase == null) {
                momentInDatabase = (Moment) dbFetchingInterface.fetchMomentById(moment.getId(), null);
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

    private boolean hasNoExpirationDate(Moment momentInDatabase) {
        if (momentInDatabase.getExpirationDate() == null) {
            return true;
        }

        return false;
    }

    private boolean isMomentDeletedFromBackend(final SynchronisationData synchronisationData) {
        return synchronisationData == null || synchronisationData.isInactive();
    }

    private boolean isMomentDeletedFromApplicationDB(final Moment momentInDatabase) {
        final SynchronisationData synchronisationData = momentInDatabase.getSynchronisationData();
        return synchronisationData != null && synchronisationData.getGuid().equals(Moment.MOMENT_NEVER_SYNCED_AND_DELETED_GUID);
    }

    private boolean isMomentUpdatedFromBackend(final Moment moment, final Moment momentInDatabase) {
        return momentInDatabase != null && !moment.getDateTime().equals(momentInDatabase.getDateTime());
    }
}
