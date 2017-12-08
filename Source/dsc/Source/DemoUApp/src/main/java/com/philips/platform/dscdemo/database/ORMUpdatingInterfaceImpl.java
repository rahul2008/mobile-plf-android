package com.philips.platform.dscdemo.database;

import com.philips.platform.core.datatypes.Characteristics;
import com.philips.platform.core.datatypes.ConsentDetail;
import com.philips.platform.core.datatypes.Insight;
import com.philips.platform.core.datatypes.Moment;
import com.philips.platform.core.datatypes.Settings;
import com.philips.platform.core.datatypes.SyncType;
import com.philips.platform.core.dbinterfaces.DBUpdatingInterface;
import com.philips.platform.core.listeners.DBRequestListener;
import com.philips.platform.dscdemo.database.table.OrmCharacteristics;
import com.philips.platform.dscdemo.database.table.OrmMoment;
import com.philips.platform.dscdemo.database.table.OrmSettings;
import com.philips.platform.dscdemo.utility.NotifyDBRequestListener;

import java.sql.SQLException;
import java.util.List;


public class ORMUpdatingInterfaceImpl implements DBUpdatingInterface {
    private static final String TAG = ORMUpdatingInterfaceImpl.class.getSimpleName();
    private final OrmSaving saving;
    private final OrmUpdating updating;
    final private OrmFetchingInterfaceImpl fetching;
    final private OrmDeleting deleting;
    private NotifyDBRequestListener notifyDBRequestListener;

    public ORMUpdatingInterfaceImpl(OrmSaving saving,
                                    OrmUpdating updating,
                                    final OrmFetchingInterfaceImpl fetching,
                                    final OrmDeleting deleting) {
        this.saving = saving;
        this.updating = updating;
        this.fetching = fetching;
        this.deleting = deleting;
        notifyDBRequestListener = new NotifyDBRequestListener();
    }

    @Override
    public void updateFailed(Exception e, DBRequestListener dbRequestListener) {
        notifyDBRequestListener.notifyFailure(e, dbRequestListener);
    }

    @Override
    public void updateSettings(Settings settings, DBRequestListener<Settings> dbRequestListener) {
        try {

            OrmSettings ormExistingSettings=null;
            OrmSettings ormSettings = OrmTypeChecking.checkOrmType(settings, OrmSettings.class);

            Settings existingSettings = fetching.fetchSettings();
            if(existingSettings!=null){
                ormExistingSettings = OrmTypeChecking.checkOrmType(existingSettings, OrmSettings.class);
            }


            if (ormExistingSettings == null) {
                saving.saveSettings(ormSettings);
                notifyDBRequestListener.notifySuccess(dbRequestListener, SyncType.SETTINGS);
                return;
            }

            ormSettings.setID(ormExistingSettings.getId());
            updating.updateSettings(ormSettings);
            notifyDBRequestListener.notifySuccess(dbRequestListener, SyncType.SETTINGS);

        } catch (SQLException e) {
            e.printStackTrace();
        } catch (OrmTypeChecking.OrmTypeException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean updateSyncBit(int tableID, boolean isSynced) {
        try {
            updating.updateDCSync(tableID, isSynced);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean updateConsent(final List<? extends ConsentDetail> consents, DBRequestListener<ConsentDetail> dbRequestListener) throws SQLException {

        for (ConsentDetail consentDetail : consents) {
            try {
                updating.updateConsentDetails(consentDetail);
            } catch (SQLException e) {
                e.printStackTrace();
                notifyDBRequestListener.notifyFailure(e, dbRequestListener);
                return false;
            }

        }
        notifyDBRequestListener.notifySuccess(consents, dbRequestListener, SyncType.CONSENT);
        return true;
    }

    @Override
    public void updateMoment(final Moment moment, DBRequestListener<Moment> dbRequestListener) throws SQLException {

        OrmMoment ormMoment = getOrmMoment(moment, dbRequestListener);
        if (ormMoment == null) {
            return;
        }
        updating.updateMoment(ormMoment);
        updating.refreshMoment(ormMoment);

        notifyDBRequestListener.notifySuccess(dbRequestListener, ormMoment, SyncType.MOMENT);
    }

    @Override
    public boolean updateMoments(List<Moment> moments, DBRequestListener<Moment> dbRequestListener) throws SQLException {

        boolean isUpdated = updating.updateMoments(moments, dbRequestListener);
        if (isUpdated) {
            notifyDBRequestListener.notifySuccess(dbRequestListener, SyncType.MOMENT);
        }
        return isUpdated;
    }

    public OrmMoment getOrmMoment(final Moment moment, DBRequestListener<Moment> dbRequestListener) {
        try {
            return OrmTypeChecking.checkOrmType(moment, OrmMoment.class);
        } catch (OrmTypeChecking.OrmTypeException e) {
            notifyDBRequestListener.notifyOrmTypeCheckingFailure(dbRequestListener, e, "Orm Type check failed");
        }
        return null;
    }

    //User AppUserCharacteristics
    @Override
    public boolean updateCharacteristics(List<Characteristics> characteristicsList, DBRequestListener<Characteristics> dbRequestListener) throws SQLException {

        try {

            deleting.deleteCharacteristics();

            for (Characteristics characteristics : characteristicsList) {
                OrmCharacteristics ormCharacteristics = OrmTypeChecking.checkOrmType(characteristics, OrmCharacteristics.class);
                saving.saveCharacteristics(ormCharacteristics);
            }
            notifyDBRequestListener.notifySuccess(dbRequestListener, SyncType.CHARACTERISTICS);
            return true;
        } catch (OrmTypeChecking.OrmTypeException e) {
            e.printStackTrace();
            return false;
        }

    }

    @Override
    public boolean updateInsights(List<? extends Insight> insights, DBRequestListener<Insight> dbRequestListener) throws SQLException {
        boolean isSaved = saving.saveInsights((List<Insight>) insights, dbRequestListener);
        if (isSaved)
            notifyDBRequestListener.notifySuccess(dbRequestListener, SyncType.INSIGHT); //Should notify DB change?
        else
            notifyDBRequestListener.notifyFailure(new Exception("Update failed"), dbRequestListener);
        return isSaved;
    }
}
