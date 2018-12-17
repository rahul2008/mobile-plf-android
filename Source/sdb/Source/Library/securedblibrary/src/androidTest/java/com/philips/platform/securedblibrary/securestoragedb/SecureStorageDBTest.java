/*
 * Copyright (c) 2015-2018 Koninklijke Philips N.V.
 * All rights reserved.
 */
package com.philips.platform.securedblibrary.securestoragedb;

import android.content.Context;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import com.philips.platform.appinfra.AppInfra;
import com.philips.platform.appinfra.AppInfraInterface;
import com.philips.platform.securedblibrary.SecureDbOrmLiteSqliteOpenHelper;

import net.sqlcipher.database.SQLiteDatabase;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

import static androidx.test.platform.app.InstrumentationRegistry.getInstrumentation;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class SecureStorageDBTest {

    private static final String DATABASE_NAME = "address.db";
    private static String DATABASE_PASSWORD_KEY = "hi";
    private static int DATABASE_VERSION = 3;

    private Context context;
    private SecureDbOrmLiteSqliteOpenHelper secureDbOrmLiteSqliteOpenHelper;
    private AppInfraInterface appInfra;
    private SecureDbOrmLiteSqliteOpenHelper secondHelper;

    @Before
    public void setUp() throws Exception {
        context = getInstrumentation().getContext();
        assertNotNull(context);

        appInfra = new AppInfra.Builder().build(context);

        secureDbOrmLiteSqliteOpenHelper = new AddressBookHelper(context, appInfra, DATABASE_NAME, null, DATABASE_VERSION, DATABASE_PASSWORD_KEY);
    }

    @After
    public void tearDown() throws Exception {
        TableUtils.clearTable(secureDbOrmLiteSqliteOpenHelper.getConnectionSource(), AddressBook.class);

        if (secondHelper != null) {
            TableUtils.clearTable(secondHelper.getConnectionSource(), AddressBook.class);
        }
    }

    @Test
    public void testDatabaseTable() throws Exception {
        assertNotNull(secureDbOrmLiteSqliteOpenHelper);
    }

    @Test
    public void testAddressBookStorage() throws SQLException {
        secureDbOrmLiteSqliteOpenHelper.getWriteDbPermission();

        final Dao<AddressBook, Integer> dao = secureDbOrmLiteSqliteOpenHelper.getDao(AddressBook.class);
        dao.createOrUpdate(new AddressBook());

        final List<AddressBook> addressBooks = dao.queryBuilder().query();
        assertEquals(1, addressBooks.size());
    }

    @Test
    public void testAddressBookUpdate() throws SQLException {
        secureDbOrmLiteSqliteOpenHelper.getWriteDbPermission();
        final Dao<AddressBook, Integer> dao = secureDbOrmLiteSqliteOpenHelper.getDao(AddressBook.class);
        dao.createOrUpdate(new AddressBook());

        final List<AddressBook> addressBooks = dao.queryBuilder().limit(1L).query();
        final String testFirstName = "Test";
        addressBooks.get(0).firstName = testFirstName;
        dao.createOrUpdate(addressBooks.get(0));

        final List<AddressBook> updateAddressBooks = dao.queryBuilder().limit(1L).query();
        assertEquals(testFirstName, updateAddressBooks.get(0).firstName);
    }

    @Test
    public void testSecondDatabase() throws SQLException {
        secondHelper = new AddressBookHelper(context, appInfra, "address2.db", null, 1, "second_key");

        secondHelper.getWriteDbPermission();

        final Dao<AddressBook, Integer> dao = secondHelper.getDao(AddressBook.class);
        dao.createOrUpdate(new AddressBook());

        final List<AddressBook> addressBooks = dao.queryBuilder().query();
        assertEquals(1, addressBooks.size());
    }

    @Test
    public void testFirstDatabaseEncrypted() throws SQLException, FileNotFoundException {
        secureDbOrmLiteSqliteOpenHelper.getWriteDbPermission();

        final Dao<AddressBook, Integer> dao = secureDbOrmLiteSqliteOpenHelper.getDao(AddressBook.class);
        final AddressBook data = new AddressBook();
        final String testName = "testName";
        data.firstName = testName;
        dao.createOrUpdate(data);

        final File databasePath = getInstrumentation().getContext().getDatabasePath(DATABASE_NAME);
        assertTrue(databasePath.exists());
        Scanner input = new Scanner(databasePath);
        while (input.hasNextLine()) {
            final String nextLine = input.nextLine();
            System.out.println(nextLine);
            assertFalse(nextLine.contains(testName));
        }
    }

    @Test
    public void testSecondDatabaseEncrypted() throws SQLException, FileNotFoundException {
        final String dataBaseName = "address2.db";
        secondHelper = new AddressBookHelper(context, appInfra, dataBaseName, null, 1, "second_key");

        secondHelper.getWriteDbPermission();

        final Dao<AddressBook, Integer> dao = secondHelper.getDao(AddressBook.class);
        final AddressBook data = new AddressBook();
        final String testName = "testName";
        data.firstName = testName;
        dao.createOrUpdate(data);

        final File databasePath = getInstrumentation().getContext().getDatabasePath(dataBaseName);
        assertTrue(databasePath.exists());
        Scanner input = new Scanner(databasePath);
        while (input.hasNextLine()) {
            final String nextLine = input.nextLine();
            System.out.println(nextLine);
            assertFalse(nextLine.contains(testName));
        }
    }

    @Test
    public void testClose() throws SQLException {
        secureDbOrmLiteSqliteOpenHelper.getWriteDbPermission();
        secureDbOrmLiteSqliteOpenHelper.close();
        secureDbOrmLiteSqliteOpenHelper.getWriteDbPermission();

        final Dao<AddressBook, Integer> dao = secureDbOrmLiteSqliteOpenHelper.getDao(AddressBook.class);
        final List<AddressBook> data = dao.queryBuilder().query();

        assertTrue(data.isEmpty());
    }

    protected static class AddressBook {
        static final String ID_FIELD = "address_id";

        // Primary key defined as an auto generated integer
        // If the database table column name differs than the Model class variable name, the way to map to use columnName
        @DatabaseField(generatedId = true, columnName = ID_FIELD)
        public int addressId;

        @DatabaseField(columnName = "first_name")
        String firstName;

        @DatabaseField(columnName = "last_name")
        public String lastName;

        @DatabaseField
        public String address;

        @DatabaseField(columnName = "contact_number")
        public long contactNumber;

        AddressBook() {

        }
    }

    protected static class AddressBookHelper extends SecureDbOrmLiteSqliteOpenHelper<AddressBook> {

        AddressBookHelper(Context context, AppInfraInterface mAppInfraInterface, String dataBaseName, SQLiteDatabase.CursorFactory factory, int databaseVersion, String databaseKey) {
            super(context, mAppInfraInterface, dataBaseName, factory, databaseVersion, databaseKey);
        }

        @Override
        public void onCreate(SQLiteDatabase database, ConnectionSource connectionSource) {
            try {
                TableUtils.createTable(connectionSource, AddressBook.class);
            } catch (SQLException e) {
                System.out.println("Error in create Database Table" + e.getMessage());
            }
        }

        @Override
        public void onUpgrade(SQLiteDatabase database, ConnectionSource connectionSource, int oldVersion, int newVersion) {

        }

        @Override
        public String getSecureDbAppVersion() {
            return "1.1.1";
        }
    }
}












































