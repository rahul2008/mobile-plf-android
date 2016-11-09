package cdp.philips.com.mydemoapp.database;

import android.util.Log;

import com.philips.platform.core.datatypes.Consent;
import com.philips.platform.core.datatypes.Moment;
import com.philips.platform.core.dbinterfaces.DBSavingInterface;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Map;
import java.util.Set;

import cdp.philips.com.mydemoapp.database.table.BaseAppDateTime;
import cdp.philips.com.mydemoapp.database.table.OrmConsent;
import cdp.philips.com.mydemoapp.database.table.OrmMoment;
import cdp.philips.com.mydemoapp.listener.DBChangeListener;
import cdp.philips.com.mydemoapp.listener.EventHelper;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public class ORMSavingInterfaceImpl implements DBSavingInterface{

    private static final String TAG = ORMSavingInterfaceImpl.class.getSimpleName();
    private final OrmSaving saving;
    private final OrmUpdating updating;
    private OrmFetchingInterfaceImpl fetching;
    private OrmDeleting deleting;
    private BaseAppDateTime baseAppDateTime;

    public ORMSavingInterfaceImpl(OrmSaving saving, OrmUpdating updating, final OrmFetchingInterfaceImpl fetching, final OrmDeleting deleting, final BaseAppDateTime baseAppDateTime) {
        this.saving = saving;
        this.updating = updating;
        this.fetching = fetching;
        this.deleting = deleting;
        this.baseAppDateTime = baseAppDateTime;
    }

    @Override
    public boolean saveMoment(final Moment moment) throws SQLException {
        OrmMoment ormMoment = null;
        try {
            ormMoment = OrmTypeChecking.checkOrmType(moment, OrmMoment.class);
            saving.saveMoment(ormMoment);
            updating.updateMoment(ormMoment);

            notifyAllSuccess(ormMoment);

            return true;
        } catch (OrmTypeChecking.OrmTypeException e) {
            Log.wtf(TAG, "Exception occurred during updateDatabaseWithMoments", e);
            notifyAllFailure(e);
            return false;
        }

    }

    @Override
    public boolean saveConsent(Consent consent) throws SQLException {
        OrmConsent ormConsent = null;
        try {
            ormConsent = OrmTypeChecking.checkOrmType(consent, OrmConsent.class);
            deleteConsentAndSetIdIfConsentExists(ormConsent);
            saving.saveConsent(ormConsent);
            notifyAllSuccess(ormConsent);
            return true;
        } catch (OrmTypeChecking.OrmTypeException e) {
            Log.wtf(TAG, "Exception occurred during updateDatabaseWithMoments", e);
            notifyAllFailure(e);
            return false;
        }

    }

    private void deleteConsentAndSetIdIfConsentExists(OrmConsent ormConsent) throws SQLException {
        OrmConsent consentInDatabase = fetching.fetchConsentByCreatorId(ormConsent.getCreatorId());
        if (consentInDatabase != null) {
            int id = consentInDatabase.getId();
            deleting.deleteConsent(consentInDatabase);
            ormConsent.setId(id);
        }
    }

    private void notifyAllSuccess(Object ormMoments) {
        Map<Integer, ArrayList<DBChangeListener>> eventMap = EventHelper.getInstance().getEventMap();
        Set<Integer> integers = eventMap.keySet();
        if(integers.contains(EventHelper.MOMENT)){
            ArrayList<DBChangeListener> dbChangeListeners = EventHelper.getInstance().getEventMap().get(EventHelper.MOMENT);
            for (DBChangeListener listener : dbChangeListeners) {
                listener.onSuccess(ormMoments);
            }
        }
    }

    private void notifyAllFailure(Exception e) {
        Map<Integer, ArrayList<DBChangeListener>> eventMap = EventHelper.getInstance().getEventMap();
        Set<Integer> integers = eventMap.keySet();
        if(integers.contains(EventHelper.MOMENT)){
            ArrayList<DBChangeListener> dbChangeListeners = EventHelper.getInstance().getEventMap().get(EventHelper.MOMENT);
            for (DBChangeListener listener : dbChangeListeners) {
                listener.onFailure(e);
            }
        }
    }
}
