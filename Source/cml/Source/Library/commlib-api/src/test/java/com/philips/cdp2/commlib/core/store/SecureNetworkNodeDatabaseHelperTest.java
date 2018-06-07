package com.philips.cdp2.commlib.core.store;

import android.content.ContentValues;
import android.content.Context;
import com.j256.ormlite.support.ConnectionSource;
import com.philips.cdp2.commlib.core.util.ContextProvider;
import com.philips.platform.appinfra.AppInfraInterface;
import com.philips.platform.appinfra.appidentity.AppIdentityInterface;
import com.philips.platform.appinfra.logging.LoggingInterface;
import com.philips.platform.appinfra.securestorage.SecureStorage;
import com.philips.platform.securedblibrary.SqlLiteInitializer;
import net.sqlcipher.database.SQLiteDatabase;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;

import java.sql.SQLException;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class SecureNetworkNodeDatabaseHelperTest {

    @Mock
    private Context contextMock;

    @Mock
    private AppInfraInterface appInfraInterfaceMock;

    @Mock
    private LoggingInterface loggingInterfaceMock;

    @Mock
    private AppIdentityInterface appIdentityInterfaceMock;

    @Mock
    private SecureStorage secureStorageMock;

    @Mock
    private SQLiteDatabase sqLiteDatabase;

    @Mock
    private ConnectionSource connectionSource;

    @Captor
    private ArgumentCaptor<String> stringArgumentCaptor;

    private SecureNetworkNodeDatabaseHelper subject;

    @Before
    public void setUp() throws Exception {
        initMocks(this);
        ContextProvider.setTestingContext(contextMock);

        when(appIdentityInterfaceMock.getAppVersion()).thenReturn("version");
        when(loggingInterfaceMock.createInstanceForComponent(any(String.class), any((String.class)))).thenReturn(loggingInterfaceMock);
        when(appInfraInterfaceMock.getLogging()).thenReturn(loggingInterfaceMock);
        when(appInfraInterfaceMock.getAppIdentity()).thenReturn(appIdentityInterfaceMock);
        when(appInfraInterfaceMock.getSecureStorage()).thenReturn(secureStorageMock);

        subject = new SecureNetworkNodeDatabaseHelper(appInfraInterfaceMock, new SqlLiteInitializer() {
            @Override
            public void loadLibs(Context context) {
                // ignore
            }
        }) {
            @Override
            public SQLiteDatabase getWriteDbPermission() {
                return sqLiteDatabase;
            }
        };
    }

    private String VERSION2 = "CREATE TABLE IF NOT EXISTS secure_network_node("
            + "_id INTEGER NOT NULL UNIQUE,"
            + "cppid TEXT UNIQUE,"
            + "mac_address TEXT,"
            + "bootid NUMERIC,"
            + "encryption_key TEXT,"
            + "dev_name TEXT,"
            + "lastknown_network TEXT,"
            + "is_paired SMALLINT NOT NULL DEFAULT 0,"
            + "last_paired NUMERIC,"
            + "ip_address TEXT,"
            + "device_type TEXT,"
            + "model_id TEXT,"
            + "https SMALLINT NOT NULL DEFAULT 0,"
            + "pin TEXT,"
            + "mismatched_pin TEXT,"
            + "PRIMARY KEY(_id)"
            + ");";

    @Test
    public void thenDatabaseIsCreated() {
        subject.onCreate(sqLiteDatabase, connectionSource);

        verify(sqLiteDatabase).execSQL(stringArgumentCaptor.capture());

        assertEquals(VERSION2, stringArgumentCaptor.getValue());
    }

    @Test
    public void thenDatabaseIsQueried() {
        final String selection = "selection";
        final String[] selectionArgs = {"1", "2"};
        subject.query(selection, selectionArgs);

        verify(sqLiteDatabase).query("secure_network_node", null, selection, selectionArgs, null, null, null);
    }

    @Test
    public void thenDatabaseRowIsAdded() {
        ContentValues contentValues = mock(ContentValues.class);
        subject.insertRow(contentValues);

        verify(sqLiteDatabase).insertWithOnConflict("secure_network_node", null, contentValues, SQLiteDatabase.CONFLICT_REPLACE);
    }

    @Test
    public void thenDatabaseRowIsDeleted() {
        final String id = "id";
        subject.deleteNetworkNodeWithCppId(id);

        verify(sqLiteDatabase).delete("secure_network_node", "cppid= ?", new String[]{id});
    }

    @Test(expected = android.database.SQLException.class)
    public void givenExceptionIsThrown_whenDataBaseIsQueried_thenExceptionIsReturned() {
        final String selection = "selection";
        final String[] selectionArgs = {"1", "2"};
        doThrow(SQLException.class).when(sqLiteDatabase).query("secure_network_node", null, selection, selectionArgs, null, null, null);

        subject.query(selection, selectionArgs);
    }

    @Test(expected = android.database.SQLException.class)
    public void givenExceptionIsThrown_whenDataBaseRowIsInserted_thenExceptionIsReturned() {
        ContentValues contentValues = mock(ContentValues.class);
        doThrow(SQLException.class).when(sqLiteDatabase).insertWithOnConflict("secure_network_node", null, contentValues, SQLiteDatabase.CONFLICT_REPLACE);

        subject.insertRow(contentValues);
    }

    @Test(expected = android.database.SQLException.class)
    public void givenExceptionIsThrown_whenDataBaseRowIsDeleted_thenExceptionIsReturned() {
        final String id = "id";
        doThrow(SQLException.class).when(sqLiteDatabase).delete("secure_network_node", "cppid= ?", new String[]{id});

        subject.deleteNetworkNodeWithCppId(id);
    }

    @Test
    public void whenDatabaseIsUpdatedFrom1To2_thenMacAddressColumnIsAddedAndFilledWithCppId() {
        subject.onUpgrade(sqLiteDatabase, connectionSource, 1,2 );

        verify(sqLiteDatabase, times(2)).rawExecSQL(stringArgumentCaptor.capture());

        assertEquals("ALTER TABLE secure_network_node ADD COLUMN mac_address STRING NULL", stringArgumentCaptor.getAllValues().get(0));
        assertEquals("UPDATE secure_network_node SET mac_address = cppid", stringArgumentCaptor.getAllValues().get(1));
    }
}