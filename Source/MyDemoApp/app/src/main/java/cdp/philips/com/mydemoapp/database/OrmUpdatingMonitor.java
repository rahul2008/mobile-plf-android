/*
 * Copyright (c) 2016. Philips Electronics India Ltd
 * All rights reserved. Reproduction in whole or in part is prohibited without
 * the written consent of the copyright holder.
 */

package cdp.philips.com.mydemoapp.database;

import android.support.annotation.NonNull;
import android.util.Log;

import com.philips.platform.core.datatypes.Moment;
import com.philips.platform.core.datatypes.MomentDetail;
import com.philips.platform.core.datatypes.MomentDetailType;
import com.philips.platform.core.datatypes.MomentType;
import com.philips.platform.core.datatypes.SynchronisationData;
import com.philips.platform.core.events.BackendMomentListSaveRequest;
import com.philips.platform.core.events.ExceptionEvent;
import com.philips.platform.core.events.ListSaveResponse;
import com.philips.platform.core.events.MomentChangeEvent;
import com.philips.platform.core.events.MomentUpdateRequest;
import com.philips.platform.core.monitors.EventMonitor;

import java.io.File;
import java.sql.SQLException;
import java.util.Collection;
import java.util.List;

import cdp.philips.com.mydemoapp.database.table.BaseAppDateTime;
import cdp.philips.com.mydemoapp.database.table.OrmMoment;
import cdp.philips.com.mydemoapp.database.table.OrmSynchronisationData;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public class OrmUpdatingMonitor extends EventMonitor {
    private static final String ERROR_UPDATING_BABY_PROFILE = "Exception occurred during update database with baby profile";
    private static final String ERROR_UPDATING_MOMENTS = "Exception occurred during update database with moments";
    private static final String ERROR_UPDATING_INSIGHTS = "Exception occurred during update database with insights";
    private final String TAG = "OrmUpdatingMonitor";
    private final OrmSaving saving;
    private final OrmUpdating updating;
    private OrmFetching fetching;
    private OrmDeleting deleting;
    private BaseAppDateTime baseAppDateTime;

    public OrmUpdatingMonitor(OrmSaving saving, OrmUpdating updating, final OrmFetching fetching, final OrmDeleting deleting, final BaseAppDateTime baseAppDateTime) {
        this.saving = saving;
        this.updating = updating;
        this.fetching = fetching;
        this.deleting = deleting;
        this.baseAppDateTime = baseAppDateTime;
    }

    public void onEventAsync(final MomentUpdateRequest momentUpdateRequest) {
        int requestId = momentUpdateRequest.getEventId();
        Moment moment = momentUpdateRequest.getMoment();
        moment.setSynced(false);
        OrmMoment ormMoment = getOrmMoment(moment);
        if (ormMoment == null) {
            return;
        }

        try {
            deleting.deleteMoment(ormMoment);
            updateOrSaveMomentInDatabase(ormMoment);
            eventing.post(new MomentChangeEvent(requestId, moment));
        } catch (SQLException e) {
            eventing.post(new ExceptionEvent("Failed to Update", new SQLException()));
        }
    }

    public void onEventBackgroundThread(final BackendMomentListSaveRequest momentSaveRequest) {
        List<? extends Moment> moments = momentSaveRequest.getList();
        if (moments == null || moments.isEmpty()) {
            return;
        }
        int requestId = momentSaveRequest.getEventId();

        int updatedCount = processMomentsReceivedFromBackend(moments);
        boolean savedAllMoments = updatedCount == moments.size();

        eventing.post(new ListSaveResponse(requestId, savedAllMoments));
    }

    private int processMomentsReceivedFromBackend(final List<? extends Moment> moments) {
        int updatedCount = 0;
        for (Moment moment : moments) {
            if (moment.getType() != MomentType.PHOTO || photoFileExistsForPhotoMoments(moment)) {
                updatedCount = processMoment(updatedCount, moment);
            }
        }
        return updatedCount;
    }

    private boolean photoFileExistsForPhotoMoments(@NonNull final Moment moment) {
        Collection<? extends MomentDetail> momentDetails = moment.getMomentDetails();
        if (momentDetails == null) {
            return false;
        }
        for (MomentDetail momentDetail : momentDetails) {
            if (momentDetail.getType() == MomentDetailType.PHOTO) {
                File file = new File(momentDetail.getValue());
                if (file.exists()) {
                    return true;
                }
                break; // Breaking as we show ONLY the first photo in timeline.
            }
        }
        return false;
    }

    private int processMoment(int updatedCount, final Moment moment) {
        try {
            OrmMoment momentInDatabase = getOrmMomentFromDatabase(moment);
            if (hasDifferentMomentVersion(moment, momentInDatabase)) {
                OrmMoment ormMoment = getOrmMoment(moment);
                if (!isActive(ormMoment.getSynchronisationData())) {
                    deleteMomentInDatabaseIfExists(momentInDatabase);
                } else if (isNeverSyncedMomentDeletedLocallyDuringSync(momentInDatabase)) {
                    ormMoment.setSynced(false);
                    ormMoment.getSynchronisationData().setInactive(true);

                    deleteAndSaveMoment(momentInDatabase, ormMoment);
                } else {
                    if (!isMomentModifiedLocallyDuringSync(momentInDatabase, ormMoment)) {
                        ormMoment.setSynced(true);
                    }

                    //This is required for deleting duplicate measurements, measurementDetails and momentDetails
                    deleteAndSaveMoment(momentInDatabase, ormMoment);
                }
                updatedCount++;
            } else {
               // tagRoomTemperatureAndHumidity(moment);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return updatedCount;
    }

    private void deleteAndSaveMoment(final OrmMoment momentInDatabase, final OrmMoment ormMoment) throws SQLException {
        deleteMeasurementAndMomentDetailsAndSetID(momentInDatabase, ormMoment);
        updateOrSaveMomentInDatabase(ormMoment);
    }

    private boolean isNeverSyncedMomentDeletedLocallyDuringSync(final OrmMoment momentInDatabase) {
        if (momentInDatabase != null) {
            OrmSynchronisationData synchronisationData = momentInDatabase.getSynchronisationData();
            if (synchronisationData != null) {
                return synchronisationData.getGuid().equals(Moment.MOMENT_NEVER_SYNCED_AND_DELETED_GUID);
            }
        }
        return false;
    }

    private void deleteMeasurementAndMomentDetailsAndSetID(final OrmMoment momentInDatabase, final OrmMoment ormMoment) throws SQLException {
        if (momentInDatabase != null) {
            ormMoment.setId(momentInDatabase.getId());
            deleting.deleteMomentAndMeasurementDetails(momentInDatabase);
        }
    }

    private OrmMoment getOrmMoment(final Moment moment) {
        try {
            return OrmTypeChecking.checkOrmType(moment, OrmMoment.class);
        } catch (OrmTypeChecking.OrmTypeException e) {
            Log.e(TAG, "Eror while type checking");
        }
        return null;
    }

    private void deleteMomentInDatabaseIfExists(final OrmMoment momentInDatabase) throws SQLException {
        if (momentInDatabase != null) {
            deleting.deleteMoment(momentInDatabase);
        }
    }

    private boolean isMomentModifiedLocallyDuringSync(final OrmMoment momentInDatabase, final OrmMoment ormMoment) {
        return momentInDatabase != null && !ormMoment.getDateTime().equals(momentInDatabase.getDateTime());
    }

    private void updateOrSaveMomentInDatabase(final OrmMoment ormMoment) throws SQLException {
        saving.saveMoment(ormMoment);
        updating.updateMoment(ormMoment);
    }

    private boolean isActive(final OrmSynchronisationData synchronisationData) {
        return synchronisationData == null || !synchronisationData.isInactive();
    }

    private boolean hasDifferentMomentVersion(final Moment moment, final OrmMoment momentInDatabase) throws SQLException {
        boolean isVersionDifferent = true;
        final SynchronisationData synchronisationData = moment.getSynchronisationData();

        if (synchronisationData != null) {
            int versionInDatabase = getVersionInDatabase(momentInDatabase);
            if (versionInDatabase != -1) {
                isVersionDifferent = versionInDatabase != synchronisationData.getVersion();
            }
        }
        return isVersionDifferent;
    }

    private int getVersionInDatabase(final OrmMoment momentInDatabase) {
        if (momentInDatabase != null && momentInDatabase.getSynchronisationData() != null) {
            return momentInDatabase.getSynchronisationData().getVersion();
        }
        return -1;
    }

    private OrmMoment getOrmMomentFromDatabase(Moment moment) throws SQLException {
        OrmMoment momentInDatabase = null;
        final SynchronisationData synchronisationData = moment.getSynchronisationData();

        if (synchronisationData != null) {
            String guid = synchronisationData.getGuid();
            momentInDatabase = fetching.fetchMomentByGuid(guid);
            if (momentInDatabase == null) {
                momentInDatabase = fetching.fetchMomentById(moment.getId());
            }
        }
        return momentInDatabase;
    }


}
