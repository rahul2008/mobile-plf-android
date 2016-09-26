/*
 * Copyright (c) 2016. Philips Electronics India Ltd
 * All rights reserved. Reproduction in whole or in part is prohibited without
 * the written consent of the copyright holder.
 */

package cdp.philips.com.mydemoapp.database;

import android.util.Log;

import com.philips.platform.core.datatypes.Moment;
import com.philips.platform.core.events.ExceptionEvent;
import com.philips.platform.core.events.MomentChangeEvent;
import com.philips.platform.core.events.MomentSaveRequest;
import com.philips.platform.core.monitors.EventMonitor;

import java.sql.SQLException;

import cdp.philips.com.mydemoapp.database.table.BaseAppDateTime;
import cdp.philips.com.mydemoapp.database.table.OrmMoment;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public class OrmSavingMonitor extends EventMonitor {
    private static final String TAG = OrmSavingMonitor.class.getSimpleName();
    private final OrmSaving saving;
    private final OrmUpdating updating;
    private OrmFetching fetching;
    private OrmDeleting deleting;
    private BaseAppDateTime uGrowDateTime;

    public OrmSavingMonitor(OrmSaving saving, OrmUpdating updating, final OrmFetching fetching, final OrmDeleting deleting, final BaseAppDateTime baseAppDateTime) {
        this.saving = saving;
        this.updating = updating;
        this.fetching = fetching;
        this.deleting = deleting;
        this.uGrowDateTime = baseAppDateTime;
    }

    public void onEventAsync(final MomentSaveRequest momentSaveRequest) {
        boolean saved = true;
        try {
            saveMoment(momentSaveRequest.getMoment());
        } catch (OrmTypeChecking.OrmTypeException | SQLException e) {
            saved = false;
            Log.wtf(TAG, "Exception occurred during updateDatabaseWithMoments", e);
        }

        if (saved) {
            eventing.post(new MomentChangeEvent(momentSaveRequest.getReferenceId(), momentSaveRequest.getMoment()));
        } else {
            eventing.post(new ExceptionEvent("Failed to insert", new SQLException()));
        }
    }

    private void saveMoment(final Moment moment) throws OrmTypeChecking.OrmTypeException, SQLException {
        OrmMoment ormMoment = OrmTypeChecking.checkOrmType(moment, OrmMoment.class);
        saving.saveMoment(ormMoment);
        updating.updateMoment(ormMoment);
    }


}
