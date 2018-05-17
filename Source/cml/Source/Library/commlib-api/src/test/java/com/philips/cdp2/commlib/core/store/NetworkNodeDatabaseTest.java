package com.philips.cdp2.commlib.core.store;

import android.database.Cursor;
import com.philips.cdp.dicommclient.networknode.NetworkNode;
import com.philips.cdp.dicommclient.util.DICommLog;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.List;

import static com.philips.cdp.dicommclient.networknode.NetworkNode.KEY_CPP_ID;
import static com.philips.cdp.dicommclient.networknode.NetworkNode.KEY_DEVICE_NAME;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class NetworkNodeDatabaseTest {

    @Mock
    private NetworkNodeDBHelper networkNodeDBHelperMock;

    @Mock
    private Cursor cursor;

    @InjectMocks
    NetworkNodeDatabase networkNodeDatabase;

    @Before
    public void setUp() throws Exception {
        DICommLog.disableLogging();
    }

    @Test
    public void whenDataBaseContainsNoNodes_thenEmptyListIsReturned() throws Exception {
        when(cursor.getCount()).thenReturn(0);
        when(networkNodeDBHelperMock.query(null, null)).thenReturn(cursor);

        List<NetworkNode> all = networkNodeDatabase.getAll();

        assertTrue(all.isEmpty());
    }

    @Test
    public void whenExceptionIsThrownWhileREadingDatabase_thenEmptyListIsReturned() throws Exception {
        doThrow(Exception.class).when(networkNodeDBHelperMock).query(null, null);

        List<NetworkNode> all = networkNodeDatabase.getAll();

        assertTrue(all.isEmpty());
    }

    @Test
    public void whenDataBaseContainsSingleNode_thenSingleNodeIsReturned() throws Exception {
        when(cursor.getCount()).thenReturn(1);
        when(networkNodeDBHelperMock.query(null, null)).thenReturn(cursor);

        List<NetworkNode> all = networkNodeDatabase.getAll();

        assertTrue(all.size() == 1);
    }

    @Test
    public void whenNodeIsReturned_thenCppIdIsRedFromDatabase() throws Exception {
        when(cursor.getCount()).thenReturn(1);
        String cppId = "cppId";
        when(cursor.getColumnIndex(KEY_CPP_ID)).thenReturn(0);
        when(cursor.getString(0)).thenReturn(cppId);
        when(networkNodeDBHelperMock.query(null, null)).thenReturn(cursor);

        List<NetworkNode> all = networkNodeDatabase.getAll();

        assertTrue(all.size() == 1);
        assertEquals(cppId, all.get(0).getCppId());
    }

    @Test
    public void whenNodeIsReturned_thenDeviceNameIsRedFromDatabase() throws Exception {
        when(cursor.getCount()).thenReturn(1);
        String deviceName = "deviceName";
        when(cursor.getColumnIndex(KEY_DEVICE_NAME)).thenReturn(0);
        when(cursor.getString(0)).thenReturn(deviceName);
        when(networkNodeDBHelperMock.query(null, null)).thenReturn(cursor);

        List<NetworkNode> all = networkNodeDatabase.getAll();

        assertTrue(all.size() == 1);
        assertEquals(deviceName, all.get(0).getName());
    }

    @Test
    public void whenNodeIsReturned_thenIpAddressIsRedFromDatabase() throws Exception {
        when(cursor.getCount()).thenReturn(1);
        String ip = "198.162.1.0";
        when(cursor.getColumnIndex(KEY_DEVICE_NAME)).thenReturn(0);
        when(cursor.getString(0)).thenReturn(ip);
        when(networkNodeDBHelperMock.query(null, null)).thenReturn(cursor);

        List<NetworkNode> all = networkNodeDatabase.getAll();

        assertTrue(all.size() == 1);
        assertEquals(ip, all.get(0).getIpAddress());
    }

    @Test
    public void whenDataBaseContainsMultipleNode_thenAllNodeasAreReturned() throws Exception {
        when(cursor.getCount()).thenReturn(3);
        when(cursor.moveToNext()).thenReturn(true).thenReturn(true).thenReturn(false);
        when(networkNodeDBHelperMock.query(null, null)).thenReturn(cursor);

        List<NetworkNode> all = networkNodeDatabase.getAll();

        assertTrue(all.size() == 3);
    }

    @Test
    public void whenExceptionIsThrownWhileReadingOneNode_thenSuccessfullyRedNodesAreReturned() throws Exception {
        when(cursor.getCount()).thenReturn(3);
        when(cursor.moveToNext()).thenReturn(true).thenReturn(true).thenReturn(false);
        when(networkNodeDBHelperMock.query(null, null)).thenReturn(cursor);

        List<NetworkNode> all = networkNodeDatabase.getAll();

        assertTrue(all.size() == 3);
    }
}