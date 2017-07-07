/*
 * Copyright (c) 2016. Philips Electronics India Ltd
 * All rights reserved. Reproduction in whole or in part is prohibited without
 * the written consent of the copyright holder.
 */

package com.philips.platform.dprdemo.database;

import android.support.annotation.NonNull;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.UpdateBuilder;
import com.philips.platform.core.datatypes.ConsentDetail;
import com.philips.platform.dprdemo.database.table.OrmConsentDetail;
import com.philips.platform.dprdemo.database.table.OrmDCSync;

import java.sql.SQLException;


/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public class OrmUpdating {

    @NonNull
    private final Dao<OrmConsentDetail, Integer> ormConsentDetailDao;

    @NonNull
    private final Dao<OrmDCSync, Integer> dcSyncDao;


    public OrmUpdating(@NonNull Dao<OrmConsentDetail, Integer> ormConsentDetailDao, @NonNull Dao<OrmDCSync, Integer> dcSyncDao) {

        this.ormConsentDetailDao = ormConsentDetailDao;
        this.dcSyncDao = dcSyncDao;
    }


    public void updateDCSync(int tableID, boolean isSynced) throws SQLException {

        UpdateBuilder<OrmDCSync, Integer> updateBuilder = dcSyncDao.updateBuilder();
        updateBuilder.updateColumnValue("isSynced", isSynced);
        updateBuilder.where().eq("tableID", tableID);

        updateBuilder.update();
    }


    public void updateConsentDetails(ConsentDetail consentDetail) throws SQLException {

        UpdateBuilder<OrmConsentDetail, Integer> updateBuilder = ormConsentDetailDao.updateBuilder();
        updateBuilder.updateColumnValue("status", consentDetail.getStatus());
        updateBuilder.updateColumnValue("version", consentDetail.getVersion());
        updateBuilder.updateColumnValue("deviceIdentificationNumber", consentDetail.getDeviceIdentificationNumber());
        updateBuilder.where().eq("type", consentDetail.getType());

        updateBuilder.update();

    }

}
