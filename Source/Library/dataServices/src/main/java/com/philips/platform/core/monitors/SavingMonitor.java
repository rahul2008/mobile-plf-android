package com.philips.platform.core.monitors;

import android.support.annotation.NonNull;

import com.philips.platform.core.datatypes.Consent;
import com.philips.platform.core.dbinterfaces.DBSavingInterface;
import com.philips.platform.core.events.ConsentBackendSaveRequest;
import com.philips.platform.core.events.DatabaseConsentSaveRequest;
import com.philips.platform.core.events.MomentSaveRequest;

import java.sql.SQLException;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public class SavingMonitor extends EventMonitor {
    private static final String TAG = SavingMonitor.class.getSimpleName();
    @NonNull
    DBSavingInterface dbInterface;

    public SavingMonitor(DBSavingInterface dbInterface) {
        this.dbInterface = dbInterface;
    }

    public void onEventAsync(final MomentSaveRequest momentSaveRequest) throws SQLException {
        boolean saved = dbInterface.saveMoment(momentSaveRequest.getMoment(),momentSaveRequest.getDbRequestListener());
        if (saved) {
            //eventing.post(new MomentChangeEvent(momentSaveRequest.getReferenceId(), momentSaveRequest.getMoment()));
        } else {
            dbInterface.postError(new Exception("Failed to insert"),momentSaveRequest.getDbRequestListener());
        }
    }

    public void onEventAsync(final DatabaseConsentSaveRequest consentSaveRequest) throws SQLException {
        Consent consent = consentSaveRequest.getConsent();
        boolean saved = dbInterface.saveConsent(consent,consentSaveRequest.getDbRequestListener());

        if (!saved) {
            dbInterface.postError(new Exception("Failed to insert"), consentSaveRequest.getDbRequestListener());
            return;
        }
        if (!consentSaveRequest.isUpdateSyncFlag()) {
            eventing.post(new ConsentBackendSaveRequest(ConsentBackendSaveRequest.RequestType.SAVE, consentSaveRequest.getConsent()));
        }

    }
}
