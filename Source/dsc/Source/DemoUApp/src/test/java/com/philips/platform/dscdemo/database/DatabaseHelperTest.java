package com.philips.platform.dscdemo.database;

import android.content.Context;

import com.j256.ormlite.support.ConnectionSource;
import com.philips.platform.dscdemo.database.table.OrmCharacteristics;
import com.philips.platform.dscdemo.database.table.OrmConsentDetail;
import com.philips.platform.dscdemo.database.table.OrmDCSync;
import com.philips.platform.dscdemo.database.table.OrmInsight;
import com.philips.platform.dscdemo.database.table.OrmInsightMetaData;
import com.philips.platform.dscdemo.database.table.OrmMeasurement;
import com.philips.platform.dscdemo.database.table.OrmMeasurementDetail;
import com.philips.platform.dscdemo.database.table.OrmMeasurementDetailType;
import com.philips.platform.dscdemo.database.table.OrmMeasurementGroup;
import com.philips.platform.dscdemo.database.table.OrmMeasurementGroupDetail;
import com.philips.platform.dscdemo.database.table.OrmMeasurementGroupDetailType;
import com.philips.platform.dscdemo.database.table.OrmMeasurementType;
import com.philips.platform.dscdemo.database.table.OrmMoment;
import com.philips.platform.dscdemo.database.table.OrmMomentDetail;
import com.philips.platform.dscdemo.database.table.OrmMomentDetailType;
import com.philips.platform.dscdemo.database.table.OrmMomentType;
import com.philips.platform.dscdemo.database.table.OrmSettings;
import com.philips.platform.dscdemo.database.table.OrmSynchronisationData;
import com.philips.platform.securedblibrary.SqlLiteInitializer;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertArrayEquals;

public class DatabaseHelperTest {

    @Test
    public void createsAllTables() {
        whenCreatingTheOrmDatabase();
        thenTablesAreCreatedFor(
                OrmMoment.class
                , OrmMomentType.class
                , OrmMomentDetail.class
                , OrmMomentDetailType.class
                , OrmMeasurement.class
                , OrmMeasurementType.class
                , OrmMeasurementDetail.class
                , OrmMeasurementDetailType.class
                , OrmSynchronisationData.class
                , OrmConsentDetail.class
                , OrmMeasurementGroup.class
                , OrmMeasurementGroupDetail.class
                , OrmMeasurementGroupDetailType.class
                , OrmCharacteristics.class
                , OrmSettings.class
                , OrmDCSync.class
                , OrmInsight.class
                , OrmInsightMetaData.class);
    }

    private void whenCreatingTheOrmDatabase() {
        databaseHelper.onCreate(database);
    }

    private void thenTablesAreCreatedFor(final Class<?>... expectedOrmClasses) {
        assertArrayEquals(expectedOrmClasses, database.dataClasses.toArray(new Class<?>[database.dataClasses.size()]));
    }

    @Before
    public void setUp() {
        context = new ContextStub();
        appInfra = new AppInfraStub();
        daoProvider = new DaoProviderStub();
        databaseHelper = new DatabaseHelper(context, appInfra, new SqlLiteInitializer() {
            @Override
            public void loadLibs(final Context context) {
            }
        }, daoProvider);
        source = new ConnectionSourceStub();
        database = new OrmDatabaseStub();
    }

    private DatabaseHelper databaseHelper;
    private ContextStub context;
    private AppInfraStub appInfra;
    private DaoProviderStub daoProvider;
    private OrmDatabaseStub database;
    private ConnectionSource source;
}
