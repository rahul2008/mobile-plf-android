/*
 * Copyright (c) 2016. Philips Electronics India Ltd
 * All rights reserved. Reproduction in whole or in part is prohibited without
 * the written consent of the copyright holder.
 */

package com.philips.platform.dprdemo.database;

import android.support.annotation.NonNull;

import com.j256.ormlite.dao.Dao;
import com.philips.platform.core.datatypes.SyncType;
import com.philips.platform.dprdemo.database.table.OrmConsentDetail;
import com.philips.platform.dprdemo.database.table.OrmDCSync;

import java.sql.SQLException;


/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public class OrmSaving {

    @NonNull
    private final Dao<OrmConsentDetail, Integer> consentDetailsDao;
    @NonNull
    private final Dao<OrmDCSync, Integer> dcSyncDao;

    public OrmSaving(
            @NonNull final Dao<OrmConsentDetail, Integer> constentDetailsDao,
            @NonNull Dao<OrmDCSync, Integer> dcSyncDao) {
        this.consentDetailsDao = constentDetailsDao;
        this.dcSyncDao = dcSyncDao;
    }


    public void saveConsentDetail(OrmConsentDetail consentDetail) throws SQLException {
        consentDetailsDao.createOrUpdate(consentDetail);
    }


    public void saveSyncBit(SyncType type, boolean isSynced) throws SQLException {
        dcSyncDao.createOrUpdate(new OrmDCSync(type.getId(), type.getDescription(), isSynced));
    }
}
