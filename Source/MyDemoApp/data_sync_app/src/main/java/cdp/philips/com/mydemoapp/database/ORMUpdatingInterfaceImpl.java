package cdp.philips.com.mydemoapp.database;

import android.util.Log;

import com.philips.platform.core.datatypes.Consent;
import com.philips.platform.core.datatypes.Moment;
import com.philips.platform.core.dbinterfaces.DBUpdatingInterface;
import com.philips.platform.core.listeners.DBRequestListener;
import com.philips.platform.core.trackers.DataServicesManager;
import com.philips.platform.core.utils.DSLog;

import java.sql.SQLException;
import java.util.List;

import cdp.philips.com.mydemoapp.database.table.OrmConsent;
import cdp.philips.com.mydemoapp.database.table.OrmConsentDetail;
import cdp.philips.com.mydemoapp.database.table.OrmMoment;
import cdp.philips.com.mydemoapp.temperature.TemperatureMomentHelper;

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
    public void updateFailed(Exception e, DBRequestListener dbRequestListener) {
        mTemperatureMomentHelper.notifyFailure(e, dbRequestListener);
    }

    @Override
    public boolean updateConsent(Consent consent, DBRequestListener dbRequestListener) throws SQLException {
        if (consent == null) {
            mTemperatureMomentHelper.notifyOrmTypeCheckingFailure(dbRequestListener, new OrmTypeChecking.OrmTypeException("No consent Found on DataCore ."), "Callback Not registered");
            return false;
        }
        OrmConsent ormConsent = null;
        try {
            ormConsent = OrmTypeChecking.checkOrmType(consent, OrmConsent.class);
            ormConsent = getModifiedConsent(ormConsent);
            saving.saveConsent(ormConsent);
            mTemperatureMomentHelper.notifySuccess(dbRequestListener, ormConsent);
            return true;
        } catch (OrmTypeChecking.OrmTypeException e) {
            DSLog.e(TAG, "Exception occurred during updateDatabaseWithMoments" + e);
            mTemperatureMomentHelper.notifyOrmTypeCheckingFailure(dbRequestListener, e, "Callback Not registered");
            return false;
        }
    }

    private OrmConsent getModifiedConsent(OrmConsent ormConsent) throws SQLException {
        DSLog.d("Creator ID MODI", ormConsent.getCreatorId());
        OrmConsent consentInDatabase = fetching.fetchConsentByCreatorId(ormConsent.getCreatorId());

        if (consentInDatabase != null) {
            int id = consentInDatabase.getId();
            final List<OrmConsentDetail> ormNonSynConsentDetails = fetching.fetchNonSynchronizedConsentDetails();

            for (OrmConsentDetail ormFromBackEndConsentDetail : ormConsent.getConsentDetails()) {

                for (OrmConsentDetail ormNonSynConsentDetail : ormNonSynConsentDetails) {
                    if (ormFromBackEndConsentDetail.getType() == ormNonSynConsentDetail.getType()) {
                        ormFromBackEndConsentDetail.setBackEndSynchronized(ormNonSynConsentDetail.getBackEndSynchronized());
                        ormFromBackEndConsentDetail.setStatus(ormNonSynConsentDetail.getStatus());
                    }
                }
            }
            ormConsent.setId(id);
            for (OrmConsent ormConsentInDB : fetching.fetchAllConsent()) {
                deleting.deleteConsent(ormConsentInDB);
            }
            deleting.deleteConsent(consentInDatabase);
            // updating.updateConsent(consentInDatabase);

        } else {
            saving.saveConsent(ormConsent);
        }
        return ormConsent;
    }

    @Override
    public void updateMoment(final Moment moment, DBRequestListener dbRequestListener) throws SQLException {

        OrmMoment ormMoment = getOrmMoment(moment, dbRequestListener);
        if (ormMoment == null) {
            return;
        }
        saving.saveMoment(ormMoment);
        updating.updateMoment(ormMoment);

        mTemperatureMomentHelper.notifySuccess(dbRequestListener, ormMoment);
    }

    public OrmMoment getOrmMoment(final Moment moment, DBRequestListener dbRequestListener) {
        try {
            return OrmTypeChecking.checkOrmType(moment, OrmMoment.class);
        } catch (OrmTypeChecking.OrmTypeException e) {
            mTemperatureMomentHelper.notifyOrmTypeCheckingFailure(dbRequestListener, e, "Orm Type check failed");
            DSLog.e(TAG, "Eror while type checking");
        }
        return null;
    }
}
