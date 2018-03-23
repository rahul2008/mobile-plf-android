package com.philips.platform.dscdemo.database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.j256.ormlite.android.AndroidConnectionSource;
import com.j256.ormlite.support.ConnectionSource;
import com.philips.platform.dscdemo.BuildConfig;
import com.philips.platform.dscdemo.database.table.OrmMoment;
import com.philips.platform.dscdemo.database.table.OrmSettings;
import com.philips.platform.securedblibrary.SqlLiteInitializer;

import org.apache.commons.io.FileUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 25)
public class DatabaseHelperTest {

    @Test
    public void createsDatabaseMomentTable() {
        whenCreatingTheSqlLiteDatabase();
        thenTableStructureIs(OrmMoment.class,
                "[[\"0\",\"id\",\"INTEGER\",\"0\",null,\"1\"]," +
                        "[\"1\",\"creatorId\",\"VARCHAR\",\"1\",null,\"0\"]," +
                        "[\"2\",\"subjectId\",\"VARCHAR\",\"1\",null,\"0\"]," +
                        "[\"3\",\"type_id\",\"INTEGER\",\"1\",null,\"0\"]," +
                        "[\"4\",\"dateTime\",\"BIGINT\",\"1\",null,\"0\"]," +
                        "[\"5\",\"synced\",\"SMALLINT\",\"0\",null,\"0\"]," +
                        "[\"6\",\"synchronisationData_id\",\"VARCHAR\",\"0\",null,\"0\"]," +
                        "[\"7\",\"expirationDate\",\"BIGINT\",\"0\",null,\"0\"]]");
    }

    @Test
    public void createsDatabaseSettingsTable() {
        whenCreatingTheSqlLiteDatabase();
        thenTableStructureIs(OrmSettings.class,
                "[[\"0\",\"id\",\"INTEGER\",\"0\",null,\"1\"]," +
                        "[\"1\",\"locale\",\"VARCHAR\",\"1\",null,\"0\"]," +
                        "[\"2\",\"unit\",\"VARCHAR\",\"1\",null,\"0\"]," +
                        "[\"3\",\"timeZone\",\"VARCHAR\",\"0\",null,\"0\"]]");
    }

    @Test
    public void migrateToV3_UserSettingsTimeZoneColumnAdded() {
        givenDatabaseContents("dataservicesDbV2.db");
        thenTableStructureIs(OrmSettings.class,
                "[[\"0\",\"id\",\"INTEGER\",\"0\",null,\"1\"]," +
                        "[\"1\",\"locale\",\"VARCHAR\",\"1\",null,\"0\"]," +
                        "[\"2\",\"unit\",\"VARCHAR\",\"1\",null,\"0\"]]");
        whenMigratingToVersion(2,3);
        thenTableStructureIs(OrmSettings.class,
                "[[\"0\",\"id\",\"INTEGER\",\"0\",null,\"1\"]," +
                        "[\"1\",\"locale\",\"VARCHAR\",\"1\",null,\"0\"]," +
                        "[\"2\",\"unit\",\"VARCHAR\",\"1\",null,\"0\"]," +
                        "[\"3\",\"timeZone\",\"VARCHAR\",\"0\",null,\"0\"]]");
    }

    @Test
    public void migrateToV3_UserSettingsExistingRowsGetNullTimeZone() {
        givenDatabaseContents("dataservicesDbV2.db");
        givenUserSettings("en_US","metric");
        thenTableContentsAre(OrmSettings.class, "[[\"1\",\"en_US\",\"metric\"]]");
        whenMigratingToVersion(2,3);
        thenTableContentsAre(OrmSettings.class, "[[\"1\",\"en_US\",\"metric\",null]]");
    }

    private void givenUserSettings(final String locale, final String unitSystem) {
        sqlLiteTestDb.getWritableDatabase().execSQL("INSERT INTO `" + OrmSettings.class.getSimpleName() + "` (locale, unit) VALUES(?,?)", new Object[] {locale, unitSystem});
    }

    private void givenDatabaseContents(final String name)  {
        final InputStream resourceAsStream = getClass().getClassLoader().getResourceAsStream(name);
        try {
            FileUtils.copyInputStreamToFile(resourceAsStream, new File(sqlLiteTestDb.getWritableDatabase().getPath()));
        } catch (IOException e) {
            e.printStackTrace();
            fail("Could not import DB");
        }
        sqlLiteTestDb.getWritableDatabase().close();
        flush();
    }

    private void whenMigratingToVersion(final int oldVersion, final int newVersion) {
        databaseHelper.onUpgrade(null, oldVersion, newVersion);
        flush();
    }

    private void thenTableStructureIs(final Class<?> klazz, final String expectedTableStructure) {
        final SQLiteDatabase sqlDatabase = sqlLiteTestDb.getReadableDatabase();
        final String sql = describeQueryForTable(klazz);
        final Cursor cursor = sqlDatabase.rawQuery(sql, null);

        String actualData = getTableData(cursor);
        assertEquals(expectedTableStructure, actualData);
    }

    private void thenTableContentsAre(final Class<?> klazz, final String expectedData) {
        final SQLiteDatabase sqlDatabase = sqlLiteTestDb.getReadableDatabase();
        final String sql = "SELECT * FROM `"+ klazz.getSimpleName() + "`";
        final Cursor cursor = sqlDatabase.rawQuery(sql, null);
        String actualData = getTableData(cursor);
        assertEquals(expectedData, actualData);
    }

    @Nullable
    private String getTableData(final Cursor cursor) {
        String actualTable = null;
        try {
            actualTable = new String(new ObjectMapper().writeValueAsBytes(getValues(cursor)));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return actualTable;
    }

    @NonNull
    private List<List<String>> getValues(final Cursor cursor) {
        final String[] columnNames = cursor.getColumnNames();
        List<List<String>> values = new ArrayList<>();
        while (cursor.moveToNext()) {
            List<String> rowValues = new ArrayList<>();
            for (String column : columnNames) {
                rowValues.add(cursor.getString(cursor.getColumnIndex(column)));
            }
            values.add(rowValues);
        }
        return values;
    }

    @NonNull
    private String describeQueryForTable(final Class<?> klazz) {
        final String tableName = klazz.getSimpleName();
        return "PRAGMA table_info(" + tableName + ")";
    }

    private void whenCreatingTheSqlLiteDatabase() {
        databaseHelper.onCreate(null, new com.j256.ormlite.android.AndroidConnectionSource(sqlLiteTestDb.getWritableDatabase()));
    }

    private void flush() {
        sqlLiteTestDb.close();
    }

    class SqlLiteTestDb extends SQLiteOpenHelper {

        public SqlLiteTestDb(final Context context, final String name, final SQLiteDatabase.CursorFactory factory, final int version) {
            super(context, name, factory, version);
        }

        @Override
        public void onCreate(final SQLiteDatabase sqLiteDatabase) {
        }

        @Override
        public void onUpgrade(final SQLiteDatabase sqLiteDatabase, final int i, final int i1) {
        }
    }

    @Before
    public void setUp() {
        sqlLiteTestDb = new SqlLiteTestDb(RuntimeEnvironment.application, "dsc_test_db", null, 2);

        context = new ContextStub();
        appInfra = new AppInfraStub();
        databaseHelper = new DatabaseHelper(context, appInfra, new SqlLiteInitializer() {
            @Override
            public void loadLibs(final Context context) {
            }
        }) {
            @Override
            public ConnectionSource getConnectionSource() {
                return new AndroidConnectionSource(sqlLiteTestDb.getWritableDatabase());
            }
        };
    }

    private SqlLiteTestDb sqlLiteTestDb;
    private DatabaseHelper databaseHelper;
    private ContextStub context;
    private AppInfraStub appInfra;
}
