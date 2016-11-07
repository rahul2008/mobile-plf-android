package cdp.philips.com.mydemoapp.database;

import android.support.annotation.NonNull;
import android.util.Log;

import com.philips.platform.core.datatypes.Moment;
import com.philips.platform.core.dbinterfaces.DBDeletingInterface;

import org.joda.time.DateTime;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Map;
import java.util.Set;

import javax.inject.Inject;

import cdp.philips.com.mydemoapp.database.OrmDeleting;
import cdp.philips.com.mydemoapp.database.OrmSaving;
import cdp.philips.com.mydemoapp.database.OrmTypeChecking;
import cdp.philips.com.mydemoapp.database.table.OrmMoment;
import cdp.philips.com.mydemoapp.database.table.OrmSynchronisationData;
import cdp.philips.com.mydemoapp.listener.DBChangeListener;
import cdp.philips.com.mydemoapp.listener.EventHelper;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public class OrmDeletingInterfaceImpl implements DBDeletingInterface {

    @NonNull
    private final OrmDeleting ormDeleting;

    //SPOOO
    @NonNull
    private final OrmSaving ormSaving;

    @Inject
    public OrmDeletingInterfaceImpl(@NonNull final OrmDeleting ormDeleting,
                                    final OrmSaving ormSaving) {
        this.ormDeleting = ormDeleting;
        this.ormSaving = ormSaving;
    }

    @Override
    public void deleteAllMoments() {
        try {
            ormDeleting.deleteAll();
        } catch (SQLException e) {
            if(e.getMessage()!=null){
                Log.i("***SPO***","exception = " + e.getMessage());
            }
        }
    }

    @Override
    public void deleteMoment(final Moment moment) {
        try {
            if (isMomentSyncedToBackend(moment)) {
                prepareMomentForDeletion(moment);
            } else {
                moment.setSynchronisationData(
                        new OrmSynchronisationData(Moment.MOMENT_NEVER_SYNCED_AND_DELETED_GUID, true,
                                DateTime.now(), 0));
                saveMoment(moment);
            }
            //notifyAllSuccess(moment);
        }catch (SQLException e){
            notifyAllFailure(e);
        }
    }

    @Override
    public void ormDeletingDeleteMoment(Moment moment) {
        try {
            ormDeleting.ormDeleteMoment((OrmMoment) moment);
            //  notifyAllSuccess(moment);
        } catch (SQLException e) {
            if(e.getMessage()!=null){
                Log.i("***SPO***","exception = " + e.getMessage());
            }
        }
    }

    private boolean isMomentSyncedToBackend(final Moment moment) {
        return moment.getSynchronisationData() != null;
    }

    private void saveMoment(final Moment moment) throws SQLException {
        ormSaving.saveMoment(getOrmMoment(moment));
    }

    private OrmMoment getOrmMoment(final Moment moment) {
        try {
            return OrmTypeChecking.checkOrmType(moment, OrmMoment.class);
        } catch (OrmTypeChecking.OrmTypeException e) {
            if(e.getMessage()!=null){
                Log.i("***SPO***","Exception = " + e.getMessage());
            }
        }
        return null;
    }

    private void prepareMomentForDeletion(final Moment moment) throws SQLException {
        moment.setSynced(false);
        moment.getSynchronisationData().setInactive(true);
        saveMoment(moment);
    }


    private void notifyAllFailure(Exception e) {
        final Map<Integer, ArrayList<DBChangeListener>> eventMap = EventHelper.getInstance().getEventMap();
        final Set<Integer> integers = eventMap.keySet();
        if(integers.contains(EventHelper.MOMENT)){
            final ArrayList<DBChangeListener> dbChangeListeners = EventHelper.getInstance().
                    getEventMap().get(EventHelper.MOMENT);
            for (DBChangeListener listener : dbChangeListeners) {
                listener.onFailure(e);
            }
        }
    }

    /*private void notifyAllSuccess(Object ormMoments) {
        final Map<Integer, ArrayList<DBChangeListener>> eventMap = EventHelper.getInstance().getEventMap();
        final Set<Integer> integers = eventMap.keySet();
        if(integers.contains(EventHelper.MOMENT)){
            final ArrayList<DBChangeListener> dbChangeListeners = EventHelper.getInstance().
                    getEventMap().get(EventHelper.MOMENT);
            for (DBChangeListener listener : dbChangeListeners) {
                listener.onSuccess(ormMoments);
            }
        }
    }*/

}
