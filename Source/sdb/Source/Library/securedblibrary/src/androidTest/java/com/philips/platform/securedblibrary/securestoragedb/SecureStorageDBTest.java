/* Copyright (c) Koninklijke Philips N.V. 2016
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */
package com.philips.platform.securedblibrary.securestoragedb;

import android.content.Context;
import android.util.Log;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import com.philips.platform.appinfra.AppInfra;
import com.philips.platform.appinfra.AppInfraInterface;
import com.philips.platform.appinfra.securestorage.SecureStorageInterface;
import com.philips.platform.securedblibrary.MockitoTestCase;
import com.philips.platform.securedblibrary.SecureDbOrmLiteSqliteOpenHelper;

import net.sqlcipher.database.SQLiteDatabase;

import java.sql.SQLException;

import static org.mockito.Mockito.mock;


/**
 * Created by 310238114 on 4/7/2016.
 */

public class SecureStorageDBTest extends MockitoTestCase {
    SecureStorageInterface mSecureStorage = null;
    // Context context = Mockito.mock(Context.class);
    private static final String DATABASE_NAME = "address.db";
    public static  String DATABASE_PASSWORD_KEY = "hi";
    private static  int DATABASE_VERSION = 3;

    private Context context;
    private SecureDbOrmLiteSqliteOpenHelper secureDbOrmLiteSqliteOpenHelper;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        context = getInstrumentation().getContext();
       assertNotNull(context);

    }

    public void testDatabaseTable() throws Exception {
       AppInfraInterface appInfra = new AppInfra.Builder().build(context);
        secureDbOrmLiteSqliteOpenHelper = new SecureDbOrmLiteSqliteOpenHelper(context,appInfra,DATABASE_NAME,null,DATABASE_VERSION,DATABASE_PASSWORD_KEY) {
            @Override
            public void onCreate(SQLiteDatabase database, ConnectionSource connectionSource)  {
                try {
                    TableUtils.createTable(connectionSource, AddressBook.class);
                }catch (SQLException e){
                    Log.e("SecureDataBaseTest","Error in ctreate Database Table"+e.getMessage());
                }

            }

            @Override
            public void onUpgrade(SQLiteDatabase database, ConnectionSource connectionSource, int oldVersion, int newVersion) {

            }
        };
        assertNotNull(secureDbOrmLiteSqliteOpenHelper);


    }





    protected static class AddressBook {
        public static final String ID_FIELD = "address_id";

        // Primary key defined as an auto generated integer
        // If the database table column name differs than the Model class variable name, the way to map to use columnName
        @DatabaseField(generatedId = true, columnName = ID_FIELD)
        public int addressId;

        @DatabaseField(columnName = "first_name")
        public String firstName;


        @DatabaseField(columnName = "last_name")
        public String lastName;

        @DatabaseField
        public String address;

        @DatabaseField(columnName = "contact_number")
        public long contactNumber;

        public AddressBook() {

        }


    }

}












































