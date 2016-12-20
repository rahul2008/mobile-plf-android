package cdp.philips.com.mydemoapp.database;

import com.philips.platform.core.datatypes.Consent;
import com.philips.platform.core.datatypes.Measurement;
import com.philips.platform.core.datatypes.MeasurementDetail;
import com.philips.platform.core.datatypes.MeasurementGroup;
import com.philips.platform.core.datatypes.MeasurementGroupDetail;
import com.philips.platform.core.datatypes.Moment;
import com.philips.platform.core.datatypes.MomentDetail;
import com.philips.platform.core.datatypes.SynchronisationData;
import com.philips.platform.core.dbinterfaces.DBUpdatingInterface;
import com.philips.platform.core.trackers.DataServicesManager;
import com.philips.platform.core.utils.DSLog;

import org.joda.time.DateTime;

import java.io.File;
import java.net.HttpURLConnection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

import cdp.philips.com.mydemoapp.consents.ConsentHelper;
import cdp.philips.com.mydemoapp.database.datatypes.MeasurementDetailType;
import cdp.philips.com.mydemoapp.database.datatypes.MeasurementGroupDetailType;
import cdp.philips.com.mydemoapp.database.datatypes.MeasurementType;
import cdp.philips.com.mydemoapp.database.datatypes.MomentDetailType;
import cdp.philips.com.mydemoapp.database.datatypes.MomentType;
import cdp.philips.com.mydemoapp.database.table.OrmConsent;
import cdp.philips.com.mydemoapp.database.table.OrmConsentDetail;
import cdp.philips.com.mydemoapp.database.table.OrmMoment;
import cdp.philips.com.mydemoapp.database.table.OrmSynchronisationData;
import cdp.philips.com.mydemoapp.listener.DBChangeListener;
import cdp.philips.com.mydemoapp.listener.EventHelper;
import cdp.philips.com.mydemoapp.listener.UserRegistrationFailureListener;
import cdp.philips.com.mydemoapp.temperature.TemperatureMomentHelper;
import retrofit.RetrofitError;

public class ORMUpdatingInterfaceImpl implements DBUpdatingInterface {
    private static final String TAG = ORMUpdatingInterfaceImpl.class.getSimpleName();
    private final OrmSaving saving;
    private final OrmUpdating updating;
    final private OrmFetchingInterfaceImpl fetching;
    final private OrmDeleting deleting;
    private TemperatureMomentHelper mTemperatureMomentHelper;

    public ORMUpdatingInterfaceImpl(OrmSaving saving,
                                    OrmUpdating updating,
                                    final OrmFetchingInterfaceImpl fetching,
                                    final OrmDeleting deleting) {
        this.saving = saving;
        this.updating = updating;
        this.fetching = fetching;
        this.deleting = deleting;
        mTemperatureMomentHelper = new TemperatureMomentHelper();
    }

    @Override
    public int processMomentsReceivedFromBackend(final List<? extends Moment> moments) {
        int updatedCount = 0;
        for (final Moment moment : moments) {
            if (!moment.getType().equalsIgnoreCase(MomentType.PHOTO) || photoFileExistsForPhotoMoments(moment)) {
                updatedCount = processMoment(updatedCount, moment);
            }
        }
        new TemperatureMomentHelper().notifyAllSuccess(moments);
        return updatedCount;
    }

    @Override
    public void processCreatedMoment(List<? extends Moment> moments) {
        for (final Moment moment : moments) {
            if (moment.getType() != MomentType.PHOTO || photoFileExistsForPhotoMoments(moment)) {
                final OrmMoment ormMoment = getOrmMoment(moment);
                ormMoment.setSynced(true);
                updateOrSaveMomentInDatabase(ormMoment);
            }
        }
        new TemperatureMomentHelper().notifyAllSuccess(moments);
    }

    public Moment createMoment(Moment old) {
        DataServicesManager manager = DataServicesManager.getInstance();
        Moment newMoment= manager.createMoment(MomentType.TEMPERATURE);

        newMoment.setId(old.getId());
        newMoment.setSynced(true);
        if (old.getSynchronisationData() != null) {
            newMoment.setSynchronisationData(old.getSynchronisationData());
        }

        ArrayList<? extends MomentDetail> momentDetails = new ArrayList<>(old.getMomentDetails());
        for(MomentDetail detail : momentDetails){
            MomentDetail momentDetail = manager.
                    createMomentDetail(MomentDetailType.PHASE, newMoment);
            momentDetail.setValue(detail.getValue());
        }

        ArrayList<? extends MeasurementGroup> oldMeasurementGroups = new ArrayList<>(old.getMeasurementGroups());

        for(MeasurementGroup oldMeasurementGroup : oldMeasurementGroups){
            MeasurementGroup measurementGroup = manager.
                    createMeasurementGroup(newMoment);

            //null coming here
            ArrayList<? extends MeasurementGroupDetail> measurementGroupDetails = new ArrayList<>(oldMeasurementGroup.getMeasurementGroupDetails());
            for(MeasurementGroupDetail detail : measurementGroupDetails) {
                MeasurementGroupDetail measurementGroupDetail = manager.
                        createMeasurementGroupDetail(MeasurementGroupDetailType.TEMP_OF_DAY, measurementGroup);
                measurementGroupDetail.setValue(detail.getValue());
                measurementGroup.addMeasurementGroupDetail(measurementGroupDetail);
            }
            MeasurementGroup measurementGroupInside = null;
            Collection<? extends MeasurementGroup> oldMeasurementGroupsInide = oldMeasurementGroup.getMeasurementGroups();
            for(MeasurementGroup oldMeasurementGroupInside : oldMeasurementGroupsInide){
                measurementGroupInside = manager.
                        createMeasurementGroup(measurementGroup);

                ArrayList<? extends Measurement> measurements = new ArrayList<>(oldMeasurementGroupInside.getMeasurements());
                for(Measurement measurement : measurements){
                    Measurement measurementValue = manager.createMeasurement(MeasurementType.TEMPERATURE, measurementGroupInside);
                    measurementValue.setValue(measurement.getValue());
                    measurementValue.setDateTime(DateTime.now());

                    ArrayList<? extends MeasurementDetail> measurementDetails = new ArrayList<>(measurement.getMeasurementDetails());
                    for(MeasurementDetail detail : measurementDetails) {
                        MeasurementDetail measurementDetail = manager.createMeasurementDetail(MeasurementDetailType.LOCATION, measurementValue);
                        measurementDetail.setValue(detail.getValue());
                        measurementValue.addMeasurementDetail(measurementDetail);
                    }
                    measurementGroupInside.addMeasurement(measurementValue);
                }

            }
            measurementGroup.addMeasurementGroup(measurementGroupInside);
            newMoment.addMeasurementGroup(measurementGroup);
        }
        return newMoment;
    }

    @Override
    public void updateFailed(Exception e) {
        notifyAllFailure(e);
    }

    @Override
    public boolean updateConsent(Consent consent) {
        if(consent==null){
            new ConsentHelper().notifyFailConsent(new OrmTypeChecking.OrmTypeException("No consent Found on DataCore ."));;
            return false;
        }
        OrmConsent ormConsent = null;
        try {
            ormConsent = OrmTypeChecking.checkOrmType(consent, OrmConsent.class);
            ormConsent=getModifiedConsent(ormConsent);
            saving.saveConsent(ormConsent);
            new ConsentHelper().notifyAllSuccess(ormConsent);
            return true;
        } catch (OrmTypeChecking.OrmTypeException e) {
            DSLog.e(TAG, "Exception occurred during updateDatabaseWithMoments" + e);
            new ConsentHelper().notifyFailConsent(e);
            return false;
        } catch (SQLException e) {
            e.printStackTrace();
            DSLog.e(TAG, "Exception occurred during updateDatabaseWithMoments" + e);
            new ConsentHelper().notifyFailConsent(e);
            return false;
        }

    }

    private OrmConsent getModifiedConsent(OrmConsent ormConsent) throws SQLException {
        DSLog.d("Creator ID MODI",ormConsent.getCreatorId());
        OrmConsent consentInDatabase = fetching.fetchConsentByCreatorId(ormConsent.getCreatorId());

        if (consentInDatabase != null) {
            int id = consentInDatabase.getId();
            final List<OrmConsentDetail> ormNonSynConsentDetails = fetching.fetchNonSynchronizedConsentDetails();

            for(OrmConsentDetail ormFromBackEndConsentDetail:ormConsent.getConsentDetails()){

                for(OrmConsentDetail ormNonSynConsentDetail:ormNonSynConsentDetails){
                    if(ormFromBackEndConsentDetail.getType() == ormNonSynConsentDetail.getType()){
                        ormFromBackEndConsentDetail.setBackEndSynchronized(ormNonSynConsentDetail.getBackEndSynchronized());
                        ormFromBackEndConsentDetail.setStatus(ormNonSynConsentDetail.getStatus());
                    }
                }
            }
            ormConsent.setId(id);
            for(OrmConsent ormConsentInDB:fetching.fetchAllConsent()) {
                deleting.deleteConsent(ormConsentInDB);
            }
            deleting.deleteConsent(consentInDatabase);
            // updating.updateConsent(consentInDatabase);

        }else{
            saving.saveConsent(ormConsent);
        }
        return ormConsent;
    }


    private boolean photoFileExistsForPhotoMoments(final Moment moment) {
        final Collection<? extends MomentDetail> momentDetails = moment.getMomentDetails();
        if (momentDetails == null) {
            return false;
        }
        for (final MomentDetail momentDetail : momentDetails) {
            if (momentDetail.getType().equalsIgnoreCase(MomentDetailType.PHOTO)) {
                final File file = new File(momentDetail.getValue());
                if (file.exists()) {
                    return true;
                }
                break; // Breaking as we show ONLY the first photo in timeline.
            }
        }
        return false;
    }

    @Override
    public int processMoment(int count, final Moment moment) {
        try {
            final OrmMoment momentInDatabase = getOrmMomentFromDatabase(moment);
           // updating.updateMoment(momentInDatabase);
            if (hasDifferentMomentVersion(moment, momentInDatabase)) {
                final OrmMoment ormMoment = getOrmMoment(moment);
                /*if(isSyncedMomentUpdatedBeforeSync(momentInDatabase)){
                    momentInDatabase.getSynchronisationData().setVersion(moment.getSynchronisationData().getVersion());
                    momentInDatabase.getSynchronisationData().setGuid(moment.getSynchronisationData().getGuid());
                }*/
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
                    //This is required for deleting duplicate
                    // measurements, measurementDetails and momentDetails
                    deleteAndSaveMoment(momentInDatabase, ormMoment);
                }
                count++;
            } else {
                // tagRoomTemperatureAndHumidity(moment);
            }
        } catch (SQLException e) {
            notifyAllFailure(e);
        }

        return count;
    }

    @Override
    public void updateOrSaveMomentInDatabase(final Moment ormMoment) {
        try {
            saving.saveMoment((OrmMoment) ormMoment);
            updating.updateMoment((OrmMoment) ormMoment);
            new TemperatureMomentHelper().notifyAllSuccess(ormMoment);
        } catch (SQLException e) {
            notifyAllFailure(e);
        }
    }

    private void deleteAndSaveMoment(final OrmMoment momentInDatabase,
                                     final OrmMoment ormMoment) throws SQLException {
        if (momentInDatabase != null) {
            ormMoment.setId(momentInDatabase.getId());
        }
     //   OrmMoment moment = (OrmMoment) createMoment(ormMoment);
        deleteMeasurementAndMomentDetailsAndSetId(momentInDatabase,ormMoment);
      //  OrmMoment moment = (OrmMoment) createMoment(ormMoment);
        updateOrSaveMomentInDatabase(ormMoment);
    }

    private boolean isNeverSyncedMomentDeletedLocallyDuringSync(final OrmMoment momentInDatabase) {
        if (momentInDatabase != null) {
            final OrmSynchronisationData synchronisationData = momentInDatabase.getSynchronisationData();
            if (synchronisationData != null) {
                return synchronisationData.getGuid().
                        equals(Moment.MOMENT_NEVER_SYNCED_AND_DELETED_GUID);
            }
        }
        return false;
    }

    private boolean isSyncedMomentUpdatedBeforeSync(final OrmMoment momentInDatabase) {
        if (momentInDatabase != null) {
            final OrmSynchronisationData synchronisationData = momentInDatabase.getSynchronisationData();
            if (synchronisationData != null && !momentInDatabase.isSynced()) {
                return true;
            }
        }
        return false;
    }

    private boolean isNeverSynced(final OrmMoment momentInDatabase) {
        if (momentInDatabase != null) {
           /*// final OrmSynchronisationData synchronisationData = momentInDatabase.getSynchronisationData();
            if (synchronisationData != null) {
                return synchronisationData.getGuid().
                        equals(Moment.MOMENT_NEVER_SYNCED_AND_DELETED_GUID);
            }*/
            return momentInDatabase.isSynced();
        }
        return false;
    }

    private boolean isUpdatedMomentNotSynced(final OrmMoment momentInDatabase) {
        if (momentInDatabase != null) {
            return momentInDatabase.isSynced();
        }
        return false;
    }

    private void deleteMeasurementAndMomentDetailsAndSetId(final OrmMoment momentInDatabase,OrmMoment ormMoment) throws SQLException {
        if (momentInDatabase != null) {
            //Check Y this was commented, is it that while creating its been assigned ?
            deleting.deleteMomentAndMeasurementGroupDetails(momentInDatabase);
        }
    }

    @Override
    public OrmMoment getOrmMoment(final Moment moment) {
        try {
            return OrmTypeChecking.checkOrmType(moment, OrmMoment.class);
        } catch (OrmTypeChecking.OrmTypeException e) {
            mTemperatureMomentHelper.notifyAllFailure(e);
            DSLog.e(TAG, "Eror while type checking");
        }
        return null;
    }

    private void deleteMomentInDatabaseIfExists(final OrmMoment momentInDatabase)
            throws SQLException {
        if (momentInDatabase != null) {
            deleting.ormDeleteMoment(momentInDatabase);
        }
    }

    private boolean isMomentModifiedLocallyDuringSync(final OrmMoment momentInDatabase,
                                                      final OrmMoment ormMoment) {
        return momentInDatabase != null &&
                !ormMoment.getDateTime().equals(momentInDatabase.getDateTime());
    }


    private boolean isActive(final OrmSynchronisationData synchronisationData) {
        return synchronisationData == null || !synchronisationData.isInactive();
    }

    private boolean hasDifferentMomentVersion(final Moment moment,
                                              final OrmMoment momentInDatabase) throws SQLException {
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
            momentInDatabase = (OrmMoment) fetching.fetchMomentByGuid(synchronisationData.getGuid());
            if (momentInDatabase == null) {
                momentInDatabase = (OrmMoment) fetching.fetchMomentById(moment.getId());
            }
        }
        return momentInDatabase;
    }


    private void notifyAllFailure(Exception e) {
        RetrofitError error = null;
        if (e instanceof RetrofitError)
            error = (RetrofitError) e;
        int status = -1000;
        if (error != null && error.getResponse() != null) {
            status = error.getResponse().getStatus();
        }

        final Map<Integer, ArrayList<UserRegistrationFailureListener>> urMap =
                EventHelper.getInstance().getURMap();
        final Map<Integer, ArrayList<DBChangeListener>> eventMap =
                EventHelper.getInstance().getEventMap();
        Set<Integer> integers;

        if (status == HttpURLConnection.HTTP_UNAUTHORIZED ||
                status == HttpURLConnection.HTTP_FORBIDDEN) {
            integers = urMap.keySet();
            if (integers.contains(EventHelper.UR)) {
                final ArrayList<UserRegistrationFailureListener> dbChangeListeners =
                        EventHelper.getInstance().getURMap().get(EventHelper.UR);
                List<UserRegistrationFailureListener> objList = Collections.synchronizedList(new ArrayList(dbChangeListeners));
                for (final UserRegistrationFailureListener listener : objList) {
                    listener.onFailure((RetrofitError) e);
                }
            }
        } else {
            integers = eventMap.keySet();
            if (integers.contains(EventHelper.MOMENT)) {
                final ArrayList<DBChangeListener> dbChangeListeners =
                        EventHelper.getInstance().getEventMap().get(EventHelper.MOMENT);
                for (final DBChangeListener listener : dbChangeListeners) {
                    listener.onFailure(e);
                }
            }
        }
    }
}
