/* Copyright (c) Koninklijke Philips N.V., 2017
* All rights are reserved. Reproduction or dissemination
* in whole or in part is prohibited without the prior written
* consent of the copyright holder.
*/
package com.philips.platform.dscdemo.database;

import com.philips.platform.core.datatypes.Characteristics;
import com.philips.platform.core.datatypes.ConsentDetail;
import com.philips.platform.core.datatypes.Insight;
import com.philips.platform.core.datatypes.Moment;
import com.philips.platform.core.datatypes.Settings;
import com.philips.platform.core.datatypes.SyncType;
import com.philips.platform.core.dbinterfaces.DBSavingInterface;
import com.philips.platform.core.listeners.DBRequestListener;
import com.philips.platform.dscdemo.database.table.OrmCharacteristics;
import com.philips.platform.dscdemo.database.table.OrmConsentDetail;
import com.philips.platform.dscdemo.database.table.OrmMoment;
import com.philips.platform.dscdemo.database.table.OrmSettings;
import com.philips.platform.dscdemo.utility.NotifyDBRequestListener;

import java.sql.SQLException;
import java.util.List;

public class ORMSavingInterfaceImpl implements DBSavingInterface {
    private final OrmSaving saving;
    private final OrmUpdating updating;
    private OrmDeleting deleting;
    private NotifyDBRequestListener notifyDBRequestListener;

    public ORMSavingInterfaceImpl(OrmSaving saving, OrmUpdating updating, final OrmDeleting deleting) {
        this.saving = saving;
        this.updating = updating;
        this.deleting = deleting;
        notifyDBRequestListener = new NotifyDBRequestListener();
    }

    //Moments
    @Override
    public boolean saveMoment(final Moment moment, DBRequestListener<Moment> dbRequestListener) throws SQLException {
        OrmMoment ormMoment;
        try {
            ormMoment = OrmTypeChecking.checkOrmType(moment, OrmMoment.class);
            saving.saveMoment(ormMoment);
            updating.refreshMoment(ormMoment);

            if (dbRequestListener == null) {
                notifyDBRequestListener.notifyDBChange(SyncType.MOMENT);
            } else {
                notifyDBRequestListener.notifySuccess(dbRequestListener, ormMoment, SyncType.MOMENT);
            }
            return true;
        } catch (OrmTypeChecking.OrmTypeException e) {
            notifyDBRequestListener.notifyOrmTypeCheckingFailure(dbRequestListener, e, "OrmType check failed!!");
            return false;
        }
    }

    @Override
    public boolean saveMoments(final List<Moment> moments, final DBRequestListener<Moment> dbRequestListener) throws SQLException {
        boolean isSaved = saving.saveMoments(moments, dbRequestListener);
        notifyDBRequestListener.notifyMomentsSaveSuccess(moments, dbRequestListener);
        return isSaved;
    }

    //Consents
    @Override
    public boolean saveConsentDetails(List<ConsentDetail> consentDetails, DBRequestListener<ConsentDetail> dbRequestListener) throws SQLException {
        deleting.deleteAllConsentDetails();

        for (ConsentDetail consentDetail : consentDetails) {
            try {
                OrmConsentDetail ormConsent = OrmTypeChecking.checkOrmType(consentDetail, OrmConsentDetail.class);
                saving.saveConsentDetail(ormConsent);
            } catch (OrmTypeChecking.OrmTypeException e) {
                e.printStackTrace();
            }

        }
        notifyDBRequestListener.notifySuccess(consentDetails, dbRequestListener, SyncType.CONSENT);
        return true;
    }

    //User Characteristics
    @Override
    public boolean saveUserCharacteristics(List<Characteristics> characteristicsList, DBRequestListener<Characteristics> dbRequestListener) throws SQLException {
        try {
            for (Characteristics characteristics : characteristicsList) {
                OrmCharacteristics ormCharacteristics = OrmTypeChecking.checkOrmType(characteristics, OrmCharacteristics.class);
                saving.saveCharacteristics(ormCharacteristics);
            }
            updateUCUI(characteristicsList, dbRequestListener);
            return true;
        } catch (OrmTypeChecking.OrmTypeException e) {
            e.printStackTrace();
            return false;
        }
    }

    private void updateUCUI(List<Characteristics> characteristicsList, DBRequestListener<Characteristics> dbRequestListener) {
        if (dbRequestListener == null) {
            return;
        }
        if (characteristicsList != null) {
            dbRequestListener.onSuccess(characteristicsList);
        } else {
            dbRequestListener.onSuccess(null);
        }
    }

    //Settings
    @Override
    public boolean saveSettings(Settings settings, DBRequestListener<Settings> dbRequestListener) throws SQLException {
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

    //Insights
    @Override
    public boolean saveInsights(List<Insight> insights, DBRequestListener<Insight> dbRequestListener) throws SQLException {
        boolean isSaved = saving.saveInsights(insights, dbRequestListener);
        notifyDBRequestListener.notifyDBChange(SyncType.INSIGHT);
        return isSaved;
    }

    //Sync
    @Override
    public boolean saveSyncBit(SyncType type, boolean isSynced) throws SQLException {
        saving.saveSyncBit(type, isSynced);
        return true;
    }

    //Post Error
    @Override
    public void postError(Exception e, DBRequestListener dbRequestListener) {
        notifyDBRequestListener.notifyFailure(e, dbRequestListener);
    }
}
