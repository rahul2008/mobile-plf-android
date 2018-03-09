package com.philips.platform.dscdemo.database;

import android.content.Context;

import com.j256.ormlite.support.ConnectionSource;
import com.philips.platform.securedblibrary.SqlLiteInitializer;

import org.junit.Before;
import org.junit.Test;

import java.sql.SQLException;

public class DatabaseHelperTest {

    private DatabaseHelper databaseHelper;
    private ContextStub context;
    private AppInfraStub appInfra;

    @Before
    public void setUp() {
        context = new ContextStub();
        appInfra = new AppInfraStub();

        databaseHelper = new DatabaseHelper(context, appInfra, new SqlLiteInitializer() {
            @Override
            public void loadLibs(final Context context) {
            }
        });
    }

    @Test
    public void createsAllTables() {
        ConnectionSource source = new ConnectionSourceStub();
        DatabaseTable database = new DatabaseTable() {
            @Override
            public void createTable(final ConnectionSource connectionSource, final Class<?> dataClass) throws SQLException {

            }
        };
        databaseHelper.onCreate(source, database, new DaoProviderStub());
    }
}
