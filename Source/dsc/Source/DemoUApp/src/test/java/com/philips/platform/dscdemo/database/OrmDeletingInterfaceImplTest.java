package com.philips.platform.dscdemo.database;

import com.philips.platform.core.listeners.DBRequestListener;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import java.sql.SQLException;
import java.util.List;

import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.initMocks;


public class OrmDeletingInterfaceImplTest {

    @Mock
    private OrmDeleting ormDeletingMock;
    @Mock
    private OrmSaving ormSavingMock;
    @Mock
    private OrmFetchingInterfaceImpl ormFetchingMock;
    @Mock
    private DBRequestListener dbRequestListenerMock;

    private OrmDeletingInterfaceImpl objectUnderTest;

    @Before
    public void setUp() throws Exception {
        initMocks(this);
        objectUnderTest = new OrmDeletingInterfaceImpl(ormDeletingMock, ormSavingMock, ormFetchingMock);
    }

    @Test
    public void givenObjectCreated_whenDeleteAllExpiredInsights_andListenerProvided_thenShouldCallOrmDeleting() throws SQLException{
        objectUnderTest.deleteAllExpiredInsights(dbRequestListenerMock);

        verify(ormDeletingMock).deleteAllExpiredInsights();
    }

    @Test
    public void givenObjectCreated_whenDeleteAllExpiredInsights_andNoListenerProvided_thenShouldCallOrmDeleting() throws SQLException{
        objectUnderTest.deleteAllExpiredInsights(null);

        verify(ormDeletingMock).deleteAllExpiredInsights();
    }

    @Test
    @SuppressWarnings("unchecked")
    public void givenObjectCreated_whenDeleteAllExpiredInsights_andListenerProvided_thenShouldCallback() throws SQLException{
        objectUnderTest.deleteAllExpiredInsights(dbRequestListenerMock);

        verify(dbRequestListenerMock).onSuccess((List) isNull());
    }

    @Test
    @SuppressWarnings("unchecked")
    public void givenObjectCreated_whenDeleteAllExpiredInsights_andNoListenerProvided_thenShouldCallback() throws SQLException{
        objectUnderTest.deleteAllExpiredInsights(null);

        verify(dbRequestListenerMock, never()).onSuccess((List) isNull());
    }
}