/*
 * Copyright (c) 2016. Philips Electronics India Ltd
 * All rights reserved. Reproduction in whole or in part is prohibited without
 * the written consent of the copyright holder.
 */

package cdp.philips.com.mydemoapp.database;

import android.support.annotation.NonNull;

import com.philips.platform.core.datatypes.Moment;
import com.philips.platform.core.events.DataClearRequest;
import com.philips.platform.core.events.DataClearResponse;
import com.philips.platform.core.events.ExceptionEvent;
import com.philips.platform.core.events.MomentBackendDeleteResponse;
import com.philips.platform.core.events.MomentChangeEvent;
import com.philips.platform.core.events.MomentDeleteRequest;
import com.philips.platform.core.monitors.EventMonitor;

import org.joda.time.DateTime;

import java.sql.SQLException;

import cdp.philips.com.mydemoapp.database.table.OrmMoment;
import cdp.philips.com.mydemoapp.database.table.OrmSynchronisationData;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public class OrmDeletingMonitor extends EventMonitor {
    @NonNull
    private final OrmDeleting ormDeleting;

    @NonNull
    private final OrmSaving ormSaving;

    public OrmDeletingMonitor(@NonNull final OrmDeleting ormDeleting, @NonNull final OrmSaving ormSaving) {
        this.ormDeleting = ormDeleting;
        this.ormSaving = ormSaving;
    }

    public void onEventBackgroundThread(@NonNull DataClearRequest event) {
        try {
            ormDeleting.deleteAll();

            eventing.post(new DataClearResponse(event.getEventId()));
        } catch (SQLException e) {
            eventing.post(new ExceptionEvent(event.getEventId(), "Error while deleting moments", e));
        }
    }

    public void onEventAsync(@NonNull MomentDeleteRequest event) {
        try {
            Moment moment = event.getMoment();
            if (!isMomentSyncedToBackend(moment)) {
                moment.setSynchronisationData(new OrmSynchronisationData(Moment.MOMENT_NEVER_SYNCED_AND_DELETED_GUID, true, DateTime.now(), 0));
                saveMoment(moment);
            } else {
                prepareMomentForDeletion(moment);
            }

            eventing.post(new MomentChangeEvent(event.getEventId(), moment));
        } catch (SQLException e) {
            eventing.post(new ExceptionEvent(event.getEventId(), "Error while deleting moment", e));
        }
    }



    private void saveMoment(final Moment moment) throws SQLException {
        OrmMoment ormMoment = getOrmMoment(moment);
        ormSaving.saveMoment(ormMoment);
    }

    public void onEventBackgroundThread(@NonNull MomentBackendDeleteResponse backendDeleteResponse) {
        Moment moment = backendDeleteResponse.getMoment();
        try {
            ormDeleting.deleteMoment((OrmMoment) moment);
        } catch (SQLException e) {
            eventing.post(new ExceptionEvent(backendDeleteResponse.getEventId(), "Error while deleting moment", e));
        }
    }

    private void prepareMomentForDeletion(final Moment moment) throws SQLException {
        moment.setSynced(false);
        moment.getSynchronisationData().setInactive(true);
        saveMoment(moment);
    }

    private OrmMoment getOrmMoment(final Moment moment) {
        try {
            return OrmTypeChecking.checkOrmType(moment, OrmMoment.class);
        } catch (OrmTypeChecking.OrmTypeException e) {
        }
        return null;
    }

    private boolean isMomentSyncedToBackend(final Moment moment) {
        return moment.getSynchronisationData() != null;
    }


}
