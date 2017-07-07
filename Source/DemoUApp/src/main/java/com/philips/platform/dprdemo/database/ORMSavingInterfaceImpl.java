/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */

package com.philips.platform.dprdemo.database;

import com.philips.platform.core.datatypes.Characteristics;
import com.philips.platform.core.datatypes.ConsentDetail;
import com.philips.platform.core.datatypes.Insight;
import com.philips.platform.core.datatypes.Moment;
import com.philips.platform.core.datatypes.Settings;
import com.philips.platform.core.datatypes.SyncType;
import com.philips.platform.core.dbinterfaces.DBSavingInterface;
import com.philips.platform.core.listeners.DBRequestListener;
import com.philips.platform.core.utils.DSLog;
import com.philips.platform.dprdemo.database.table.BaseAppDateTime;
import com.philips.platform.dprdemo.database.table.OrmConsentDetail;
import com.philips.platform.dprdemo.utils.NotifyDBRequestListener;


import java.sql.SQLException;
import java.util.List;

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
    public boolean saveMoment(final Moment moment, DBRequestListener<Moment> dbRequestListener) throws SQLException {
            return false;
    }

    @Override
    public boolean saveMoments(final List<Moment> moments, final DBRequestListener<Moment> dbRequestListener) throws SQLException {
        return false;
    }


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

    @Override
    public void postError(Exception e, DBRequestListener dbRequestListener) {
        notifyDBRequestListener.notifyFailure(e, dbRequestListener);
    }

    @Override
    public boolean saveUserCharacteristics(List<Characteristics> list, DBRequestListener<Characteristics> dbRequestListener) throws SQLException {
        return false;
    }

    @Override
    public boolean saveSettings(Settings settings, DBRequestListener<Settings> dbRequestListener) throws SQLException {
        return false;
    }

    @Override
    public boolean saveInsights(List<Insight> list, DBRequestListener<Insight> dbRequestListener) throws SQLException {
        return false;
    }


    @Override
    public boolean saveSyncBit(SyncType type, boolean isSynced) throws SQLException {
        saving.saveSyncBit(type, isSynced);
        return true;
    }
}
