package com.philips.platform.dprdemo.database;

import com.philips.platform.core.datatypes.Characteristics;
import com.philips.platform.core.datatypes.ConsentDetail;
import com.philips.platform.core.datatypes.Insight;
import com.philips.platform.core.datatypes.Moment;
import com.philips.platform.core.datatypes.Settings;
import com.philips.platform.core.datatypes.SyncType;
import com.philips.platform.core.dbinterfaces.DBUpdatingInterface;
import com.philips.platform.core.listeners.DBRequestListener;
import com.philips.platform.dprdemo.utils.NotifyDBRequestListener;

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
    public boolean updateCharacteristics(List<Characteristics> list, DBRequestListener<Characteristics> dbRequestListener) throws SQLException {
        return false;
    }

    @Override
    public void updateSettings(Settings settings, DBRequestListener<Settings> dbRequestListener) throws SQLException {

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
    public boolean updateInsights(List<? extends Insight> list, DBRequestListener<Insight> dbRequestListener) throws SQLException {
        return false;
    }

    @Override
    public void updateMoment(Moment moment, DBRequestListener<Moment> dbRequestListener) throws SQLException {

    }

    @Override
    public boolean updateMoments(List<Moment> list, DBRequestListener<Moment> dbRequestListener) throws SQLException {
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


}
