package com.philips.platform.datasevices.injection;

import android.content.Context;

import com.j256.ormlite.dao.Dao;
import com.philips.platform.datasevices.database.DatabaseHelper;
import com.philips.platform.datasevices.database.ORMDeletingInterfaceImpl;
import com.philips.platform.datasevices.database.ORMSavingInterfaceImpl;
import com.philips.platform.datasevices.database.ORMUpdatingInterfaceImpl;
import com.philips.platform.datasevices.database.OrmCreator;
import com.philips.platform.datasevices.database.OrmDeleting;
import com.philips.platform.datasevices.database.OrmFetchingInterfaceImpl;
import com.philips.platform.datasevices.database.OrmSaving;
import com.philips.platform.datasevices.database.OrmUpdating;
import com.philips.platform.datasevices.database.table.BaseAppDateTime;
import com.philips.platform.datasevices.database.table.OrmMeasurement;
import com.philips.platform.datasevices.database.table.OrmMeasurementDetail;
import com.philips.platform.datasevices.database.table.OrmMoment;
import com.philips.platform.datasevices.database.table.OrmMomentDetail;
import com.philips.platform.datasevices.database.table.OrmSynchronisationData;
import com.philips.platform.core.monitors.DBMonitors;
import com.philips.platform.core.monitors.DeletingMonitor;
import com.philips.platform.core.monitors.FetchingMonitor;
import com.philips.platform.core.monitors.SavingMonitor;
import com.philips.platform.core.monitors.UpdatingMonitor;
import com.philips.platform.core.utils.UuidGenerator;

import java.sql.SQLException;
import java.util.Arrays;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
@Module
public class MonitorModule {
    private Context context;
    private final DatabaseHelper databaseHelper;

    public MonitorModule(Context context) {
        this.context = context;
        databaseHelper = new DatabaseHelper(context, new UuidGenerator());
    }

    @Provides
    @Singleton
    DBMonitors providesDBMonitors() {
        try {
            Dao<OrmMoment, Integer> momentDao = databaseHelper.getMomentDao();
            Dao<OrmMomentDetail, Integer> momentDetailDao = databaseHelper.getMomentDetailDao();
            Dao<OrmMeasurement, Integer> measurementDao = databaseHelper.getMeasurementDao();
            Dao<OrmMeasurementDetail, Integer> measurementDetailDao = databaseHelper.getMeasurementDetailDao();
            Dao<OrmSynchronisationData, Integer> synchronisationDataDao = databaseHelper.getSynchronisationDataDao();


            OrmSaving saving = new OrmSaving(momentDao, momentDetailDao, measurementDao, measurementDetailDao,
                    synchronisationDataDao);
            OrmUpdating updating = new OrmUpdating(momentDao, momentDetailDao, measurementDao, measurementDetailDao);
            OrmFetchingInterfaceImpl fetching = new OrmFetchingInterfaceImpl(momentDao, synchronisationDataDao);
            OrmDeleting deleting = new OrmDeleting(momentDao, momentDetailDao, measurementDao,
                    measurementDetailDao, synchronisationDataDao);


            OrmCreator creator = new OrmCreator(new UuidGenerator());


            BaseAppDateTime uGrowDateTime = new BaseAppDateTime();
            ORMSavingInterfaceImpl ORMSavingInterfaceImpl = new ORMSavingInterfaceImpl(saving,updating,fetching,deleting,uGrowDateTime);
            SavingMonitor savingMonitor = new SavingMonitor(ORMSavingInterfaceImpl);
            FetchingMonitor fetchMonitor = new FetchingMonitor(fetching);
            ORMDeletingInterfaceImpl dbInterface = new ORMDeletingInterfaceImpl(deleting,saving);
            ORMUpdatingInterfaceImpl dbInterfaceOrmUpdatingInterface = new ORMUpdatingInterfaceImpl(saving,updating,fetching,deleting);
            OrmFetchingInterfaceImpl dbInterfaceOrmFetchingInterface = new OrmFetchingInterfaceImpl(momentDao,synchronisationDataDao);
            DeletingMonitor deletingMonitor = new DeletingMonitor(dbInterface);
            UpdatingMonitor updatingMonitor = new UpdatingMonitor(dbInterfaceOrmUpdatingInterface,dbInterface,dbInterfaceOrmFetchingInterface);

            return new DBMonitors(Arrays.asList(savingMonitor, fetchMonitor, deletingMonitor, updatingMonitor));
        } catch (SQLException exception) {
            throw new IllegalStateException("Can not instantiate database");
        }
    }
}
