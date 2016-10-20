package com.philips.platform.appframework.database;

import android.util.Log;

import com.philips.platform.core.datatypes.Moment;
import com.philips.platform.core.dbinterfaces.DBSavingInterface;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Map;
import java.util.Set;

import com.philips.platform.appframework.database.table.BaseAppDateTime;
import com.philips.platform.appframework.database.table.OrmMoment;
import com.philips.platform.appframework.listener.DBChangeListener;
import com.philips.platform.appframework.listener.EventHelper;

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
