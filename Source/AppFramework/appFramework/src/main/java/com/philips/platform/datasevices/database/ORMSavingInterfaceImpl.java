/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
package com.philips.platform.datasevices.database;

import android.util.Log;

import com.philips.platform.datasevices.listener.DBChangeListener;
import com.philips.platform.datasevices.listener.EventHelper;
import com.philips.platform.core.datatypes.Moment;
import com.philips.platform.core.dbinterfaces.DBSavingInterface;
import com.philips.platform.datasevices.database.table.BaseAppDateTime;
import com.philips.platform.datasevices.database.table.OrmMoment;
import com.philips.platform.datasevices.temperature.TemperatureMomentHelper;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Map;
import java.util.Set;

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
}
