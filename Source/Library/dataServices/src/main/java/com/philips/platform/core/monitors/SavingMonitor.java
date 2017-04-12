/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
package com.philips.platform.core.monitors;

import android.support.annotation.NonNull;

import com.philips.platform.core.dbinterfaces.DBBlobSavingInterface;
import com.philips.platform.core.dbinterfaces.DBSavingInterface;
import com.philips.platform.core.events.CharacteristicsBackendSaveRequest;
import com.philips.platform.core.events.DatabaseConsentSaveRequest;
import com.philips.platform.core.events.DatabaseSettingsSaveRequest;
import com.philips.platform.core.events.MomentSaveRequest;
import com.philips.platform.core.events.MomentsSaveRequest;
import com.philips.platform.core.events.SavingBlobMetaDataRequest;
import com.philips.platform.core.events.UserCharacteristicsSaveRequest;
import com.philips.platform.core.utils.DSLog;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.sql.SQLException;

public class SavingMonitor extends EventMonitor {
    private static final String TAG = SavingMonitor.class.getSimpleName();
    @NonNull
    DBSavingInterface dbInterface;

    public SavingMonitor(DBSavingInterface dbInterface) {
        this.dbInterface = dbInterface;
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onEventBackGround(final MomentSaveRequest momentSaveRequest) throws SQLException {
        boolean saved = dbInterface.saveMoment(momentSaveRequest.getMoment(),momentSaveRequest.getDbRequestListener());
        if (saved) {
            //eventing.post(new MomentChangeEvent(momentSaveRequest.getReferenceId(), momentSaveRequest.getMoments()));
        } else {
            dbInterface.postError(new Exception("Failed to insert"),momentSaveRequest.getDbRequestListener());
        }
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onEventBackGround(final MomentsSaveRequest momentSaveRequest) throws SQLException {
        boolean saved = dbInterface.saveMoments(momentSaveRequest.getMoments(),momentSaveRequest.getDbRequestListener());
        if (saved) {
            //eventing.post(new MomentChangeEvent(momentSaveRequest.getReferenceId(), momentSaveRequest.getMoments()));

        } else {
            dbInterface.postError(new Exception("Failed to insert"),momentSaveRequest.getDbRequestListener());
        }
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onEventBackGround(final DatabaseConsentSaveRequest consentSaveRequest) throws SQLException {

        boolean saved = dbInterface.saveConsentDetails(consentSaveRequest.getConsentDetails(),consentSaveRequest.getDbRequestListener());
        if (!saved) {
            dbInterface.postError(new Exception("Failed to insert"), consentSaveRequest.getDbRequestListener());
            return;
        }
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onEventBackGround(final DatabaseSettingsSaveRequest databaseSettingsSaveRequest) throws SQLException {
        dbInterface.saveSettings(databaseSettingsSaveRequest.getSettings(),databaseSettingsSaveRequest.getDbRequestListener());
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onEventBackGround(final UserCharacteristicsSaveRequest userCharacteristicsSaveRequest) throws SQLException {
        DSLog.d(DSLog.LOG, "SavingMonitor = UserCharacteristicsSaveRequest onEventAsync");
        if (userCharacteristicsSaveRequest.getUserCharacteristicsList() == null)
            return;

        boolean isSaved = dbInterface.saveUserCharacteristics(userCharacteristicsSaveRequest.getUserCharacteristicsList(),userCharacteristicsSaveRequest.getDbRequestListener());



        DSLog.d(DSLog.LOG, "SavingMonitor = UserCharacteristicsSaveRequest isSaved ="+isSaved);
        if(!isSaved){
            dbInterface.postError(new Exception("Failed to insert"),userCharacteristicsSaveRequest.getDbRequestListener());
            return;
        }

        //

        //if (!userCharacteristicsSaveRequest.getUserCharacteristics().isSynchronized()) {
            eventing.post(new CharacteristicsBackendSaveRequest(CharacteristicsBackendSaveRequest.RequestType.UPDATE,
                    userCharacteristicsSaveRequest.getUserCharacteristicsList()));
        //}
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onEventBackGround(final SavingBlobMetaDataRequest savingBlobMetaDataRequest) throws SQLException {
        ((DBBlobSavingInterface)dbInterface).saveBlobMetaData(savingBlobMetaDataRequest.getBlobMetaData(),null);
    }

}
