package cdp.philips.com.mydemoapp.injection;

import android.content.Context;

import com.j256.ormlite.dao.Dao;
import com.philips.platform.core.monitors.DBMonitors;
import com.philips.platform.core.monitors.DeletingMonitor;
import com.philips.platform.core.monitors.FetchingMonitor;
import com.philips.platform.core.monitors.SavingMonitor;
import com.philips.platform.core.monitors.UpdatingMonitor;
import com.philips.platform.core.utils.UuidGenerator;

import java.sql.SQLException;
import java.util.Arrays;

import javax.inject.Singleton;

import cdp.philips.com.mydemoapp.database.DatabaseHelper;
import cdp.philips.com.mydemoapp.database.ORMDeletingInterfaceImpl;
import cdp.philips.com.mydemoapp.database.ORMSavingInterfaceImpl;
import cdp.philips.com.mydemoapp.database.ORMUpdatingInterfaceImpl;
import cdp.philips.com.mydemoapp.database.OrmCreator;
import cdp.philips.com.mydemoapp.database.OrmDeleting;
import cdp.philips.com.mydemoapp.database.OrmFetchingInterfaceImpl;
import cdp.philips.com.mydemoapp.database.OrmSaving;
import cdp.philips.com.mydemoapp.database.OrmUpdating;
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
            DeletingMonitor deletingMonitor = new DeletingMonitor(dbInterface);
            UpdatingMonitor updatingMonitor = new UpdatingMonitor(dbInterfaceOrmUpdatingInterface,dbInterface);

            return new DBMonitors(Arrays.asList(savingMonitor, fetchMonitor, deletingMonitor, updatingMonitor));
        } catch (SQLException exception) {
            throw new IllegalStateException("Can not instantiate database");
        }
    }
}
