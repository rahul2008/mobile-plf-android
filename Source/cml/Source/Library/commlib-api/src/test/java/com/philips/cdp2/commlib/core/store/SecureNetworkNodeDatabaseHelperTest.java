package com.philips.cdp2.commlib.core.store;

import android.content.ContentValues;
import android.content.Context;
import com.philips.cdp.dicommclient.util.DICommLog;
import com.philips.cdp2.commlib.core.util.ContextProvider;
import com.philips.platform.appinfra.AppInfraInterface;
import com.philips.platform.appinfra.appidentity.AppIdentityInterface;
import com.philips.platform.appinfra.logging.LoggingInterface;
import com.philips.platform.appinfra.securestorage.SecureStorage;
import com.philips.platform.securedblibrary.SqlLiteInitializer;
import net.sqlcipher.database.SQLiteDatabase;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import java.sql.SQLException;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class SecureNetworkNodeDatabaseHelperTest {

    public static final String TABLE_NAME = "network_node";
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
    private SQLiteDatabase sqLiteDatabaseMock;

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
                return sqLiteDatabaseMock;
            }
        };
    }

    @Test
    public void whenDatabaseIsQueried_thenCorrectTableAndSelectionArgsAreUsed() {
        final String selection = "selection";
        final String[] selectionArgs = {"1", "2"};
        subject.query(selection, selectionArgs);

        verify(sqLiteDatabaseMock).query(TABLE_NAME, null, selection, selectionArgs, null, null, null);
    }

    @Test
    public void whenRowInsertionIsRequested_thenRowIsInsertedWithReplaceStrategy() {
        ContentValues contentValues = mock(ContentValues.class);
        subject.insertRow(contentValues);

        verify(sqLiteDatabaseMock).insertWithOnConflict(TABLE_NAME, null, contentValues, SQLiteDatabase.CONFLICT_REPLACE);
    }

    @Test
    public void whenDeletionOfNodeWithCppIdIsRequested_thenRowIsDeletedWithCurrectWhereClause() {
        final String id = "id";
        subject.delete(id);

        verify(sqLiteDatabaseMock).delete(TABLE_NAME, "cppid= ?", new String[]{id});
    }

    @Test(expected = android.database.SQLException.class)
    public void givenDataBaseIsQueried_whenExceptionIsThrown_thenExceptionIsReturned() {
        final String selection = "selection";
        final String[] selectionArgs = {"1", "2"};
        doThrow(SQLException.class).when(sqLiteDatabaseMock).query(TABLE_NAME, null, selection, selectionArgs, null, null, null);

        subject.query(selection, selectionArgs);
    }

    @Test(expected = android.database.SQLException.class)
    public void givenDataBaseRowIsInserted_whenExceptionIsThrown_thenExceptionIsReturned() {
        ContentValues contentValues = mock(ContentValues.class);
        doThrow(SQLException.class).when(sqLiteDatabaseMock).insertWithOnConflict(TABLE_NAME, null, contentValues, SQLiteDatabase.CONFLICT_REPLACE);

        subject.insertRow(contentValues);
    }

    @Test(expected = android.database.SQLException.class)
    public void givenDataBaseRowIsDeleted_whenExceptionIsThrown_thenExceptionIsReturned() {
        final String id = "id";
        doThrow(SQLException.class).when(sqLiteDatabaseMock).delete(TABLE_NAME, "cppid= ?", new String[]{id});

        subject.delete(id);
    }
}