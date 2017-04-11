package com.philips.platform.baseapp.screens.dataservices.database;

import com.philips.platform.appinfra.logging.LoggingInterface;
import com.philips.platform.baseapp.base.AppFrameworkApplication;
import com.philips.platform.baseapp.screens.dataservices.database.table.OrmCharacteristics;
import com.philips.platform.baseapp.screens.dataservices.database.table.OrmMoment;
import com.philips.platform.baseapp.screens.dataservices.database.table.OrmSettings;
import com.philips.platform.baseapp.screens.dataservices.utility.NotifyDBRequestListener;
import com.philips.platform.core.datatypes.Characteristics;
import com.philips.platform.core.datatypes.ConsentDetail;
import com.philips.platform.core.datatypes.Insight;
import com.philips.platform.core.datatypes.Moment;
import com.philips.platform.core.datatypes.Settings;
import com.philips.platform.core.datatypes.SyncType;
import com.philips.platform.core.dbinterfaces.DBUpdatingInterface;
import com.philips.platform.core.listeners.DBRequestListener;
import com.philips.platform.core.utils.DSLog;

import java.sql.SQLException;
import java.util.List;

import static com.philips.platform.baseapp.screens.utility.Constants.ORM_TYPE_EXCEPTION;
import static com.philips.platform.baseapp.screens.utility.Constants.SQLITE_EXCEPTION;

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
            OrmSettings ormSettings = OrmTypeChecking.checkOrmType(settings, OrmSettings.class);

            Settings existingSettings = fetching.fetchSettings();
            OrmSettings ormExistingSettings = OrmTypeChecking.checkOrmType(existingSettings, OrmSettings.class);

            if (ormExistingSettings == null) {
                saving.saveSettings(ormSettings);
                notifyDBRequestListener.notifySuccess(dbRequestListener, SyncType.SETTINGS);
                return;
            }

            ormSettings.setID(ormExistingSettings.getId());
            updating.updateSettings(ormSettings);
            notifyDBRequestListener.notifySuccess(dbRequestListener, SyncType.SETTINGS);

        } catch (SQLException e) {
            AppFrameworkApplication.loggingInterface.log(LoggingInterface.LogLevel.DEBUG, SQLITE_EXCEPTION,e.getMessage());

        } catch (OrmTypeChecking.OrmTypeException e) {
            AppFrameworkApplication.loggingInterface.log(LoggingInterface.LogLevel.DEBUG, ORM_TYPE_EXCEPTION,e.getMessage());
        }
    }

    @Override
    public boolean updateSyncBit(int tableID, boolean isSynced) {
        try {
            updating.updateDCSync(tableID, isSynced);
        } catch (SQLException e) {
            AppFrameworkApplication.loggingInterface.log(LoggingInterface.LogLevel.DEBUG, SQLITE_EXCEPTION,e.getMessage());
        }
        return false;
    }

    @Override
    public boolean updateConsent(final List<? extends ConsentDetail> consents, DBRequestListener<ConsentDetail> dbRequestListener) throws SQLException {

        for (ConsentDetail consentDetail : consents) {
            try {
                updating.updateConsentDetails(consentDetail);
            } catch (SQLException e) {
                AppFrameworkApplication.loggingInterface.log(LoggingInterface.LogLevel.DEBUG, SQLITE_EXCEPTION,e.getMessage());

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
            DSLog.e(TAG, "Eror while type checking");
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
            AppFrameworkApplication.loggingInterface.log(LoggingInterface.LogLevel.DEBUG, ORM_TYPE_EXCEPTION,e.getMessage());

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
