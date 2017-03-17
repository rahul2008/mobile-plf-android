/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */

package cdp.philips.com.mydemoapp.database;

import com.philips.platform.core.datatypes.Characteristics;
import com.philips.platform.core.datatypes.ConsentDetail;
import com.philips.platform.core.datatypes.Insight;
import com.philips.platform.core.datatypes.SyncType;
import com.philips.platform.core.datatypes.Settings;

import com.philips.platform.core.datatypes.Moment;
import com.philips.platform.core.dbinterfaces.DBSavingInterface;
import com.philips.platform.core.listeners.DBRequestListener;
import com.philips.platform.core.utils.DSLog;

import java.sql.SQLException;
import java.util.List;

import cdp.philips.com.mydemoapp.database.table.BaseAppDateTime;
import cdp.philips.com.mydemoapp.database.table.OrmCharacteristics;
import cdp.philips.com.mydemoapp.database.table.OrmConsentDetail;
import cdp.philips.com.mydemoapp.database.table.OrmInsight;
import cdp.philips.com.mydemoapp.database.table.OrmMoment;
import cdp.philips.com.mydemoapp.database.table.OrmSettings;
import cdp.philips.com.mydemoapp.utility.NotifyDBRequestListener;

public class ORMSavingInterfaceImpl implements DBSavingInterface {

    private static final String TAG = ORMSavingInterfaceImpl.class.getSimpleName();
    private final OrmSaving saving;
    private final OrmUpdating updating;
    private OrmFetchingInterfaceImpl fetching;
    private OrmDeleting deleting;
    private NotifyDBRequestListener notifyDBRequestListener;

    public ORMSavingInterfaceImpl(OrmSaving saving, OrmUpdating updating, final OrmFetchingInterfaceImpl fetching, final OrmDeleting deleting, final BaseAppDateTime baseAppDateTime) {
        this.saving = saving;
        this.updating = updating;
        this.fetching = fetching;
        this.deleting = deleting;
        notifyDBRequestListener = new NotifyDBRequestListener();
    }

    @Override
    public boolean saveMoment(final Moment moment, DBRequestListener dbRequestListener) throws SQLException {
        OrmMoment ormMoment = null;
        try {
            ormMoment = OrmTypeChecking.checkOrmType(moment, OrmMoment.class);
            saving.saveMoment(ormMoment);
            updating.refreshMoment(ormMoment);
            notifyDBRequestListener.notifySuccess(dbRequestListener, ormMoment, SyncType.MOMENT);
            return true;
        } catch (OrmTypeChecking.OrmTypeException e) {
            DSLog.e(TAG, "Exception occurred during updateDatabaseWithMoments" + e);
            notifyDBRequestListener.notifyOrmTypeCheckingFailure(dbRequestListener, e, "OrmType check failed!!");
            return false;
        }
    }

    @Override
    public boolean saveMoments(final List<Moment> moments, final DBRequestListener dbRequestListener) throws SQLException {
        boolean isSaved = saving.saveMoments(moments, dbRequestListener);
        ///notifyDBRequestListener.notifyDBChange(SyncType.MOMENT);
        notifyDBRequestListener.notifyMomentsSaveSuccess(moments, dbRequestListener);
        return isSaved;
    }


    @Override
    public boolean saveConsentDetails(List<ConsentDetail> consentDetails, DBRequestListener dbRequestListener) throws SQLException {

        deleting.deleteAllConsentDetails();

        for (ConsentDetail consentDetail : consentDetails) {
            try {
                OrmConsentDetail ormConsent = OrmTypeChecking.checkOrmType(consentDetail, OrmConsentDetail.class);
                saving.saveConsentDetail(ormConsent);
            } catch (OrmTypeChecking.OrmTypeException e) {
                e.printStackTrace();
            }

        }
        updating.updateDCSync(SyncType.CONSENT.getId(), true);
        notifyDBRequestListener.notifySuccess(consentDetails, dbRequestListener, SyncType.CONSENT);
        return true;

    }

    @Override
    public boolean saveUserCharacteristics(List<Characteristics> characteristicsList, DBRequestListener dbRequestListener) throws SQLException {

        try {
            deleting.deleteCharacteristics();
            for (Characteristics characteristics : characteristicsList) {
                OrmCharacteristics ormCharacteristics = OrmTypeChecking.checkOrmType(characteristics, OrmCharacteristics.class);
                saving.saveCharacteristics(ormCharacteristics);
            }
            updating.updateDCSync(SyncType.CHARACTERISTICS.getId(), false);
            updateUCUI(characteristicsList, dbRequestListener);
            return true;
        } catch (OrmTypeChecking.OrmTypeException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean saveSettings(Settings settings, DBRequestListener dbRequestListener) throws SQLException {

        try {
            deleting.deleteSettings();
            OrmSettings ormSettings = OrmTypeChecking.checkOrmType(settings, OrmSettings.class);
            saving.saveSettings(ormSettings);
            notifyDBRequestListener.notifySuccess(dbRequestListener, SyncType.CONSENT);
            return true;
        } catch (OrmTypeChecking.OrmTypeException e) {
            notifyDBRequestListener.notifyOrmTypeCheckingFailure(dbRequestListener, e, "OrmType check failed");
            return false;
        }

    }

    @Override
    public boolean saveInsights(List<Insight> insights, DBRequestListener dbRequestListener) throws SQLException {
        boolean isSaved = saving.saveInsights(insights, dbRequestListener);
        notifyDBRequestListener.notifyDBChange(SyncType.INSIGHT);
        return isSaved;
    }

    private void updateUCUI(List<Characteristics> characteristicsList, DBRequestListener dbRequestListener) {
        if (dbRequestListener == null) {
            return;
        }
        if (characteristicsList != null) {
            dbRequestListener.onSuccess(characteristicsList);
        } else {
            dbRequestListener.onSuccess(null);
        }
    }

    @Override
    public void postError(Exception e, DBRequestListener dbRequestListener) {
        notifyDBRequestListener.notifyFailure(e, dbRequestListener);
    }
}
