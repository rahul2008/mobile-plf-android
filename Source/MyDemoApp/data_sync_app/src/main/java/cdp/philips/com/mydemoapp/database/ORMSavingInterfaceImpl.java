/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */

package cdp.philips.com.mydemoapp.database;

import android.util.Log;

import com.philips.platform.core.datatypes.Consent;
import com.philips.platform.core.datatypes.Moment;
import com.philips.platform.core.dbinterfaces.DBSavingInterface;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import cdp.philips.com.mydemoapp.database.table.BaseAppDateTime;
import cdp.philips.com.mydemoapp.database.table.OrmConsent;
import cdp.philips.com.mydemoapp.database.table.OrmConsentDetail;
import cdp.philips.com.mydemoapp.database.table.OrmMoment;
import cdp.philips.com.mydemoapp.listener.DBChangeListener;
import cdp.philips.com.mydemoapp.listener.EventHelper;
import cdp.philips.com.mydemoapp.temperature.TemperatureMomentHelper;

public class ORMSavingInterfaceImpl implements DBSavingInterface {

    private static final String TAG = ORMSavingInterfaceImpl.class.getSimpleName();
    private final OrmSaving saving;
    private final OrmUpdating updating;
    private OrmFetchingInterfaceImpl fetching;
    private OrmDeleting deleting;
    private BaseAppDateTime baseAppDateTime;
    private TemperatureMomentHelper mTemperatureMomentHelper;

    public ORMSavingInterfaceImpl(OrmSaving saving, OrmUpdating updating, final OrmFetchingInterfaceImpl fetching, final OrmDeleting deleting, final BaseAppDateTime baseAppDateTime) {
        this.saving = saving;
        this.updating = updating;
        this.fetching = fetching;
        this.deleting = deleting;
        this.baseAppDateTime = baseAppDateTime;
        mTemperatureMomentHelper = new TemperatureMomentHelper();
    }

    @Override
    public boolean saveMoment(final Moment moment) throws SQLException {
        OrmMoment ormMoment = null;
        try {
            ormMoment = OrmTypeChecking.checkOrmType(moment, OrmMoment.class);
            saving.saveMoment(ormMoment);
            updating.updateMoment(ormMoment);

            mTemperatureMomentHelper.notifyAllSuccess(ormMoment);

            return true;
        } catch (OrmTypeChecking.OrmTypeException e) {
            Log.wtf(TAG, "Exception occurred during updateDatabaseWithMoments", e);
            mTemperatureMomentHelper.notifyAllFailure(e);
            return false;
        }

    }

    //TODO: Spoorti - For particularly consent, updating shoud be done instead of deleting and saving for avoiding duplication
    @Override
    public boolean saveConsent(Consent consent) throws SQLException {
        OrmConsent ormConsent = null;
        try {
            ormConsent = OrmTypeChecking.checkOrmType(consent, OrmConsent.class);
            updateConsentAndSetIdIfConsentExists(ormConsent);
            notifyAllSuccess(ormConsent);
            return true;
        } catch (OrmTypeChecking.OrmTypeException e) {
            Log.wtf(TAG, "Exception occurred during updateDatabaseWithMoments", e);
            notifyFailConsent(e);
            return false;
        }

    }

    //TODO: Spoorti - Not sure if this API can be clubed with the above
    @Override
    public boolean saveBackEndConsent(Consent consent) throws SQLException {

        if(consent==null){
            notifyFailConsent(new OrmTypeChecking.OrmTypeException("consent null"));
            return false;
        }
        OrmConsent ormConsent = null;
        try {
            ormConsent = OrmTypeChecking.checkOrmType(consent, OrmConsent.class);
            ormConsent=getModifiedConsent(ormConsent);
            saving.saveConsent(ormConsent);
            notifyAllSuccess(ormConsent);
            return true;
        } catch (OrmTypeChecking.OrmTypeException e) {
            Log.wtf(TAG, "Exception occurred during updateDatabaseWithMoments", e);
            notifyFailConsent(e);
            return false;
        }

    }

    private void updateConsentAndSetIdIfConsentExists(OrmConsent ormConsent) throws SQLException {
        OrmConsent consentInDatabase = fetching.fetchConsentByCreatorId(ormConsent.getCreatorId());
        Log.d("Creator ID MODI",ormConsent.getCreatorId());
        if (consentInDatabase != null) {
            int id = consentInDatabase.getId();
            for(OrmConsent ormConsentInDB:fetching.fetchAllConsent()) {
                deleting.deleteConsent(ormConsentInDB);
            }
            ormConsent.setId(id);
            saving.saveConsent(ormConsent);
        }else{
            saving.saveConsent(ormConsent);
        }
    }
    private OrmConsent getModifiedConsent(OrmConsent ormConsent) throws SQLException {
        Log.d("Creator ID MODI",ormConsent.getCreatorId());
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

    //TODO: Spoorti - Move it to ConsentHelper class to avoid code duplication
    private void notifyAllSuccess(Consent ormConsent) {
        Map<Integer, ArrayList<DBChangeListener>> eventMap = EventHelper.getInstance().getEventMap();
        Set<Integer> integers = eventMap.keySet();
        if (integers.contains(EventHelper.CONSENT)) {
            ArrayList<DBChangeListener> dbChangeListeners = EventHelper.getInstance().getEventMap().get(EventHelper.CONSENT);
            for (DBChangeListener listener : dbChangeListeners) {
                listener.onSuccess(ormConsent);
            }
        }
    }



    //TODO: Spoorti - Move it to ConsentHelper class
    private void notifyFailConsent(Exception e) {
        Map<Integer, ArrayList<DBChangeListener>> eventMap = EventHelper.getInstance().getEventMap();
        Set<Integer> integers = eventMap.keySet();
        if (integers.contains(EventHelper.CONSENT)) {
            ArrayList<DBChangeListener> dbChangeListeners = EventHelper.getInstance().getEventMap().get(EventHelper.CONSENT);
            for (DBChangeListener listener : dbChangeListeners) {
                listener.onFailure(e);
            }
        }
    }


}
