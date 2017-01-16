package cdp.philips.com.mydemoapp.database;

import com.philips.platform.core.datatypes.Characteristics;
import com.philips.platform.core.datatypes.Consent;
import com.philips.platform.core.datatypes.Moment;
import com.philips.platform.core.dbinterfaces.DBUpdatingInterface;
import com.philips.platform.core.listeners.DBRequestListener;
import com.philips.platform.core.utils.DSLog;

import java.sql.SQLException;
import java.util.List;

import cdp.philips.com.mydemoapp.database.datatypes.MeasurementDetailType;
import cdp.philips.com.mydemoapp.database.datatypes.MeasurementGroupDetailType;
import cdp.philips.com.mydemoapp.database.datatypes.MeasurementType;
import cdp.philips.com.mydemoapp.database.datatypes.MomentDetailType;
import cdp.philips.com.mydemoapp.database.datatypes.MomentType;
import cdp.philips.com.mydemoapp.database.table.OrmCharacteristics;
import cdp.philips.com.mydemoapp.database.table.OrmConsent;
import cdp.philips.com.mydemoapp.database.table.OrmConsentDetail;
import cdp.philips.com.mydemoapp.database.table.OrmMoment;
import cdp.philips.com.mydemoapp.database.table.OrmSynchronisationData;
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
        OrmConsent ormConsent = null;
        try {
            ormConsent = OrmTypeChecking.checkOrmType(consent, OrmConsent.class);
            OrmConsent consentInDatabase = fetching.fetchConsentByCreatorId(ormConsent.getCreatorId());
            if (consentInDatabase == null) {
                saving.saveConsent(ormConsent);
                mTemperatureMomentHelper.notifySuccess(dbRequestListener, ormConsent);
                return true;
            } else {
                ormConsent = fetching.getModifiedConsent(ormConsent, consentInDatabase);
                ormConsent.setId(consentInDatabase.getId());
                for (OrmConsent ormConsentInDB : fetching.fetchAllConsent()) {
                    deleting.deleteConsent(ormConsentInDB);
                }
                saving.saveConsent(ormConsent);
                mTemperatureMomentHelper.notifySuccess(dbRequestListener, ormConsent);
                return true;
            }

        } catch (OrmTypeChecking.OrmTypeException e) {
            DSLog.e(TAG, "Exception occurred during updateDatabaseWithMoments" + e);
            mTemperatureMomentHelper.notifyOrmTypeCheckingFailure(dbRequestListener, e, "Callback Not registered");
            return false;
        }
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

    //User Characteristics
    @Override
    public boolean updateCharacteristics(Characteristics characteristics,DBRequestListener dbRequestListener) throws SQLException {
        OrmCharacteristics ormCharacteristics = null;
        try {
            ormCharacteristics = OrmTypeChecking.checkOrmType(characteristics, OrmCharacteristics.class);
            deleting.deleteCharacteristics();
            saving.saveCharacteristics(ormCharacteristics);
            dbRequestListener.fetchData();
            return true;
        } catch (OrmTypeChecking.OrmTypeException e) {
            e.printStackTrace();
            return false;
        }

    }

    //TODO: SPoorti - put the Logic in segrater
    @Override
    public void processCharacteristicsReceivedFromDataCore(Characteristics characteristics, DBRequestListener dbRequestListener) throws SQLException {
        Characteristics dbUC=fetching.fetchUCByCreatorId(characteristics.getCreatorId());
        if(dbUC!=null){
            if(!dbUC.isSynchronized()){
                return;
            }else{
                updateCharacteristics(characteristics,dbRequestListener);
            }
        }else{
            updateCharacteristics(characteristics,dbRequestListener);
        }
    }

}
