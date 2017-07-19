/*
 * Copyright (c) 2016. Philips Electronics India Ltd
 * All rights reserved. Reproduction in whole or in part is prohibited without
 * the written consent of the copyright holder.
 */

package cdp.philips.com.demoapp.database;

import android.support.annotation.NonNull;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.DeleteBuilder;
import com.philips.platform.core.datatypes.ConsentDetail;
import com.philips.platform.core.datatypes.ConsentDetailStatusType;
import com.philips.platform.core.datatypes.SyncType;
import com.philips.platform.devicepair.consents.ConsentDetailType;
import cdp.philips.com.demoapp.database.table.OrmConsentDetail;
import cdp.philips.com.demoapp.database.table.OrmDCSync;

import java.sql.SQLException;


/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public class OrmDeleting {


    @NonNull
    private final Dao<OrmConsentDetail, Integer> consentDetailDao;

    private final Dao<OrmDCSync, Integer> syncDao;


    public OrmDeleting(
            @NonNull final Dao<OrmConsentDetail, Integer> constentDetailsDao,
            Dao<OrmDCSync, Integer> syncDao) {


        this.consentDetailDao = constentDetailsDao;
        this.syncDao = syncDao;
    }

    public void deleteAll() throws SQLException {
        consentDetailDao.executeRawNoArgs("DELETE FROM `ormconsentdetail`");
        syncDao.executeRawNoArgs("DELETE FROM `ormdcsync`"); //OrmDCSync


        insertDefaultConsentAndSyncBit();
    }

    private void insertDefaultConsentAndSyncBit() {
        try {
            consentDetailDao.createOrUpdate(new OrmConsentDetail(ConsentDetailType.SLEEP, ConsentDetailStatusType.REFUSED.getDescription(), ConsentDetail.DEFAULT_DOCUMENT_VERSION,
                    ConsentDetail.DEFAULT_DEVICE_IDENTIFICATION_NUMBER));
            consentDetailDao.createOrUpdate(new OrmConsentDetail(ConsentDetailType.TEMPERATURE, ConsentDetailStatusType.REFUSED.getDescription(), ConsentDetail.DEFAULT_DOCUMENT_VERSION,
                    ConsentDetail.DEFAULT_DEVICE_IDENTIFICATION_NUMBER));
            consentDetailDao.createOrUpdate(new OrmConsentDetail(ConsentDetailType.WEIGHT, ConsentDetailStatusType.REFUSED.getDescription(), ConsentDetail.DEFAULT_DOCUMENT_VERSION,
                    ConsentDetail.DEFAULT_DEVICE_IDENTIFICATION_NUMBER));
            syncDao.createOrUpdate(new OrmDCSync(SyncType.CONSENT.getId(), SyncType.CONSENT.getDescription(), true));

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public void deleteAllConsentDetails() throws SQLException {
        consentDetailDao.executeRawNoArgs("DELETE FROM `ormconsentdetail`");
    }

    public int deleteSyncBit(SyncType type) throws SQLException {
        DeleteBuilder<OrmDCSync, Integer> deleteBuilder = syncDao.deleteBuilder();
        deleteBuilder.where().eq("tableid", type.getId());
        return deleteBuilder.delete();
    }
}
