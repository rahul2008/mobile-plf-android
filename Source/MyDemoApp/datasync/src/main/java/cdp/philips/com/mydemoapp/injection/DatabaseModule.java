/*
 * Copyright (c) 2016. Philips Electronics India Ltd
 * All rights reserved. Reproduction in whole or in part is prohibited without
 * the written consent of the copyright holder.
 */

package cdp.philips.com.mydemoapp.injection;

import android.content.Context;

import com.j256.ormlite.dao.Dao;
import com.philips.platform.core.BaseAppDataCreator;
import com.philips.platform.core.utils.UuidGenerator;

import java.sql.SQLException;
import java.util.Arrays;

import javax.inject.Singleton;

import cdp.philips.com.mydemoapp.database.Database;
import cdp.philips.com.mydemoapp.database.DatabaseHelper;
import cdp.philips.com.mydemoapp.database.OrmCreator;
import cdp.philips.com.mydemoapp.database.OrmDeleting;
import cdp.philips.com.mydemoapp.database.OrmDeletingMonitor;
import cdp.philips.com.mydemoapp.database.OrmFetching;
import cdp.philips.com.mydemoapp.database.OrmFetchingMonitor;
import cdp.philips.com.mydemoapp.database.OrmSaving;
import cdp.philips.com.mydemoapp.database.OrmSavingMonitor;
import cdp.philips.com.mydemoapp.database.OrmUpdating;
import cdp.philips.com.mydemoapp.database.OrmUpdatingMonitor;
import cdp.philips.com.mydemoapp.database.table.BaseAppDateTime;
import cdp.philips.com.mydemoapp.database.table.OrmMeasurement;
import cdp.philips.com.mydemoapp.database.table.OrmMeasurementDetail;
import cdp.philips.com.mydemoapp.database.table.OrmMoment;
import cdp.philips.com.mydemoapp.database.table.OrmMomentDetail;
import cdp.philips.com.mydemoapp.database.table.OrmSynchronisationData;
import dagger.Module;
import dagger.Provides;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
@Module
public class DatabaseModule {
    private final DatabaseHelper databaseHelper;

    private Context context;

    public DatabaseModule(Context context) {
        this.context = context;
        databaseHelper = new DatabaseHelper(context, new UuidGenerator());
    }

    @Provides
    @Singleton
    BaseAppDataCreator providesDataCreator() {
        return providesDatabase();
    }

    @Provides
    @Singleton
    Database providesDatabase() {
        try {
            Dao<OrmMoment, Integer> momentDao = databaseHelper.getMomentDao();
            Dao<OrmMomentDetail, Integer> momentDetailDao = databaseHelper.getMomentDetailDao();
            Dao<OrmMeasurement, Integer> measurementDao = databaseHelper.getMeasurementDao();
            Dao<OrmMeasurementDetail, Integer> measurementDetailDao = databaseHelper.getMeasurementDetailDao();
            Dao<OrmSynchronisationData, Integer> synchronisationDataDao = databaseHelper.getSynchronisationDataDao();


            OrmSaving saving = new OrmSaving(momentDao, momentDetailDao, measurementDao, measurementDetailDao,
                    synchronisationDataDao);
            OrmUpdating updating = new OrmUpdating(momentDao, momentDetailDao, measurementDao, measurementDetailDao);
            OrmFetching fetching = new OrmFetching(momentDao, synchronisationDataDao);
            OrmDeleting deleting = new OrmDeleting(momentDao, momentDetailDao, measurementDao,
                    measurementDetailDao, synchronisationDataDao);

            OrmCreator creator = new OrmCreator(new UuidGenerator());


            BaseAppDateTime uGrowDateTime = new BaseAppDateTime();

            OrmSavingMonitor savingMonitor = new OrmSavingMonitor(saving, updating, fetching, deleting, uGrowDateTime);
            OrmFetchingMonitor fetchMonitor = new OrmFetchingMonitor(fetching);
            OrmDeletingMonitor deletingMonitor = new OrmDeletingMonitor(deleting, saving);
            OrmUpdatingMonitor updatingMonitor = new OrmUpdatingMonitor(saving, updating, fetching, deleting, uGrowDateTime);

            return new Database(creator, Arrays.asList(savingMonitor, fetchMonitor, deletingMonitor, updatingMonitor));
        } catch (SQLException exception) {
            throw new IllegalStateException("Can not instantiate database");
        }
    }
}
