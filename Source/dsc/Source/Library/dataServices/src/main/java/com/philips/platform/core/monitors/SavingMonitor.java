/* Copyright (c) Koninklijke Philips N.V., 2017
* All rights are reserved. Reproduction or dissemination
* in whole or in part is prohibited without the prior written
* consent of the copyright holder.
*/
package com.philips.platform.core.monitors;

import android.support.annotation.NonNull;

import com.philips.platform.core.datatypes.SyncType;
import com.philips.platform.core.dbinterfaces.DBDeletingInterface;
import com.philips.platform.core.dbinterfaces.DBSavingInterface;
import com.philips.platform.core.dbinterfaces.DBUpdatingInterface;
import com.philips.platform.core.events.CharacteristicsBackendSaveRequest;
import com.philips.platform.core.events.DatabaseSettingsSaveRequest;
import com.philips.platform.core.events.InsightsSaveRequest;
import com.philips.platform.core.events.MomentSaveRequest;
import com.philips.platform.core.events.MomentsSaveRequest;
import com.philips.platform.core.events.UserCharacteristicsSaveRequest;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.sql.SQLException;

public class SavingMonitor extends EventMonitor {

    @NonNull
    private DBSavingInterface dbInterface;

    @NonNull
    private final DBDeletingInterface dbDeletingInterface;

    @NonNull
    private final DBUpdatingInterface dbUpdatingInterface;

    public SavingMonitor(@NonNull DBSavingInterface dbInterface, @NonNull DBDeletingInterface dbDeletingInterface, @NonNull DBUpdatingInterface dbUpdatingInterface) {
        this.dbInterface = dbInterface;
        this.dbDeletingInterface = dbDeletingInterface;
        this.dbUpdatingInterface = dbUpdatingInterface;
    }

    //Moments
    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onEventBackGround(final MomentSaveRequest momentSaveRequest) throws SQLException {
        boolean savedSuccessfully = dbInterface.saveMoment(momentSaveRequest.getMoment(), momentSaveRequest.getDbRequestListener());
        if (!savedSuccessfully) {
            dbInterface.postError(new Exception("Failed to insert"), momentSaveRequest.getDbRequestListener());
        }
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onEventBackGround(final MomentsSaveRequest momentSaveRequest) throws SQLException {
        boolean savedSuccessfully = dbInterface.saveMoments(momentSaveRequest.getMoments(), momentSaveRequest.getDbRequestListener());
        if (!savedSuccessfully) {
            dbInterface.postError(new Exception("Failed to insert"), momentSaveRequest.getDbRequestListener());
        }
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onEventBackGround(final InsightsSaveRequest insightsSaveRequest) throws SQLException {
        final boolean savedSuccessfully = dbInterface.saveInsights(insightsSaveRequest.getInsights(), insightsSaveRequest.getDbRequestListener());
        if (!savedSuccessfully) {
            dbInterface.postError(new Exception("Failed to insert"), insightsSaveRequest.getDbRequestListener());
        }
    }

    //Settings
    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onEventBackGround(final DatabaseSettingsSaveRequest databaseSettingsSaveRequest) throws SQLException {
        dbDeletingInterface.deleteSyncBit(SyncType.SETTINGS);
        dbInterface.saveSyncBit(SyncType.SETTINGS, true);
        dbInterface.saveSettings(databaseSettingsSaveRequest.getSettings(), databaseSettingsSaveRequest.getDbRequestListener());
    }
    //Characteristics

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onEventBackGround(final UserCharacteristicsSaveRequest userCharacteristicsSaveRequest) throws SQLException {
        if (userCharacteristicsSaveRequest.getUserCharacteristicsList() == null)
            return;

        dbDeletingInterface.deleteUserCharacteristics();
        boolean isSaved = dbInterface.saveUserCharacteristics(userCharacteristicsSaveRequest.getUserCharacteristicsList(), userCharacteristicsSaveRequest.getDbRequestListener());
        dbDeletingInterface.deleteSyncBit(SyncType.CHARACTERISTICS);
        dbInterface.saveSyncBit(SyncType.CHARACTERISTICS, false);
        if (!isSaved) {
            dbInterface.postError(new Exception("Failed to insert"), userCharacteristicsSaveRequest.getDbRequestListener());
            return;
        }
        eventing.post(new CharacteristicsBackendSaveRequest(CharacteristicsBackendSaveRequest.RequestType.UPDATE,
                userCharacteristicsSaveRequest.getUserCharacteristicsList()));
    }
}
