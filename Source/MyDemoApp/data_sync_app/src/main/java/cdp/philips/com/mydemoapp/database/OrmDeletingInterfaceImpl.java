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
import cdp.philips.com.mydemoapp.temperature.TemperatureMomentHelper;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public class OrmDeletingInterfaceImpl implements DBDeletingInterface {

    @NonNull
    private final OrmDeleting ormDeleting;

    @NonNull
    private final OrmSaving ormSaving;

    TemperatureMomentHelper mTemperatureMomentHelper;

    @Inject
    public OrmDeletingInterfaceImpl(@NonNull final OrmDeleting ormDeleting,
                                    final OrmSaving ormSaving) {
        this.ormDeleting = ormDeleting;
        this.ormSaving = ormSaving;
        mTemperatureMomentHelper = new TemperatureMomentHelper();
    }

    @Override
    public void deleteAllMoments() {
        try {
            ormDeleting.deleteAll();
        } catch (SQLException e) {
            mTemperatureMomentHelper.notifyAllFailure(e);
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
            mTemperatureMomentHelper.notifyAllFailure(e);
        }
    }

    @Override
    public void ormDeletingDeleteMoment(Moment moment) {
        try {
            ormDeleting.ormDeleteMoment((OrmMoment) moment);
            //  notifyAllSuccess(moment);
        } catch (SQLException e) {
            mTemperatureMomentHelper.notifyAllFailure(e);
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
            mTemperatureMomentHelper.notifyAllFailure(e);
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
}
