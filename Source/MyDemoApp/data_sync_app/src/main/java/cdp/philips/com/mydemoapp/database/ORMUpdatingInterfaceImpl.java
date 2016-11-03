package cdp.philips.com.mydemoapp.database;

import android.support.annotation.NonNull;
import android.util.Log;

import com.philips.cdp.registration.HttpClient;
import com.philips.platform.core.datatypes.Moment;
import com.philips.platform.core.datatypes.MomentDetail;
import com.philips.platform.core.datatypes.MomentDetailType;
import com.philips.platform.core.datatypes.MomentType;
import com.philips.platform.core.datatypes.SynchronisationData;
import com.philips.platform.core.dbinterfaces.DBUpdatingInterface;

import java.io.File;
import java.net.HttpURLConnection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

import cdp.philips.com.mydemoapp.database.table.OrmMoment;
import cdp.philips.com.mydemoapp.database.table.OrmSynchronisationData;
import cdp.philips.com.mydemoapp.listener.DBChangeListener;
import cdp.philips.com.mydemoapp.listener.EventHelper;
import cdp.philips.com.mydemoapp.listener.UserRegistrationFailureListener;
import retrofit.RetrofitError;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public class ORMUpdatingInterfaceImpl implements DBUpdatingInterface{
    private static final String TAG = ORMUpdatingInterfaceImpl.class.getSimpleName();
    private final OrmSaving saving;
    private final OrmUpdating updating;
    private OrmFetchingInterfaceImpl fetching;
    private OrmDeleting deleting;

    public ORMUpdatingInterfaceImpl(OrmSaving saving, OrmUpdating updating, final OrmFetchingInterfaceImpl fetching, final OrmDeleting deleting) {
        this.saving = saving;
        this.updating = updating;
        this.fetching = fetching;
        this.deleting = deleting;
    }

    @Override
    public int processMomentsReceivedFromBackend(final List<? extends Moment> moments) {
        int updatedCount = 0;
        for (Moment moment : moments) {
            if (moment.getType() != MomentType.PHOTO || photoFileExistsForPhotoMoments(moment)) {
                updatedCount = processMoment(updatedCount, moment);
            }
        }
            notifyAllSuccess(moments);
        return updatedCount;
    }

    @Override
    public void updateFailed(Exception e) {
        notifyAllFailure(e);
    }

    @Override
    public void postRetrofitError(final Throwable error) {
        notifyAllFailure((Exception) error);
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

    @Override
    public int processMoment(int updatedCount, final Moment moment) {
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

    @Override
    public void updateOrSaveMomentInDatabase(final Moment ormMoment){
        try {
            saving.saveMoment((OrmMoment) ormMoment);
            updating.updateMoment((OrmMoment) ormMoment);
            notifyAllSuccess(ormMoment);
        }catch (SQLException e) {
           notifyAllFailure(e);
        }
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

    @Override
    public OrmMoment getOrmMoment(final Moment moment) {
        try {
            return OrmTypeChecking.checkOrmType(moment, OrmMoment.class);
        } catch (OrmTypeChecking.OrmTypeException e) {
            Log.e(TAG, "Eror while type checking");
        }
        return null;
    }

    private void deleteMomentInDatabaseIfExists(final OrmMoment momentInDatabase) throws SQLException {
        if (momentInDatabase != null) {
            deleting.ormDeleteMoment(momentInDatabase);
        }
    }

    private boolean isMomentModifiedLocallyDuringSync(final OrmMoment momentInDatabase, final OrmMoment ormMoment) {
        return momentInDatabase != null && !ormMoment.getDateTime().equals(momentInDatabase.getDateTime());
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
            momentInDatabase = (OrmMoment)fetching.fetchMomentByGuid(guid);
            if (momentInDatabase == null) {
                momentInDatabase = (OrmMoment)fetching.fetchMomentById(moment.getId());
            }
        }
        return momentInDatabase;
    }

    private void notifyAllSuccess(Object ormMoments) {
        Map<Integer, ArrayList<DBChangeListener>> eventMap = EventHelper.getInstance().getEventMap();
        Set<Integer> integers = eventMap.keySet();
        if(integers.contains(EventHelper.MOMENT)){
            ArrayList<DBChangeListener> dbChangeListeners = EventHelper.getInstance().getEventMap().get(EventHelper.MOMENT);
            for (DBChangeListener listener : dbChangeListeners) {
                listener.onSuccess(ormMoments);
            }
        }
    }

    private void notifyAllFailure(Exception e) {
        RetrofitError error = (RetrofitError) e;
        int status = -1000;
        if(error!=null && error.getResponse()!=null) {
            status = error.getResponse().getStatus();
        }

        Map<Integer, ArrayList<UserRegistrationFailureListener>> urMap = EventHelper.getInstance().getURMap();
        Map<Integer, ArrayList<DBChangeListener>> eventMap = EventHelper.getInstance().getEventMap();
        Set<Integer> integers;

        if (status == HttpURLConnection.HTTP_UNAUTHORIZED || status == HttpURLConnection.HTTP_FORBIDDEN) {
            integers = urMap.keySet();
            if (integers.contains(EventHelper.UR)) {
                ArrayList<UserRegistrationFailureListener> dbChangeListeners = EventHelper.getInstance().getURMap().get(EventHelper.UR);
                for (UserRegistrationFailureListener listener : dbChangeListeners) {
                    listener.onFailure((RetrofitError) e);
                }
            }
        } else {
            integers = eventMap.keySet();
            if (integers.contains(EventHelper.MOMENT)) {
                ArrayList<DBChangeListener> dbChangeListeners = EventHelper.getInstance().getEventMap().get(EventHelper.MOMENT);
                for (DBChangeListener listener : dbChangeListeners) {
                    listener.onFailure(e);
                }
            }
        }
    }
}
