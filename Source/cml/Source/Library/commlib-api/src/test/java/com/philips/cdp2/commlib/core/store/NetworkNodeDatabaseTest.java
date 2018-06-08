package com.philips.cdp2.commlib.core.store;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.SQLException;
import com.philips.cdp.dicommclient.networknode.NetworkNode;
import com.philips.cdp.dicommclient.util.DICommLog;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.robolectric.RobolectricTestRunner;

import java.util.List;

import static com.philips.cdp.dicommclient.networknode.NetworkNode.KEY_CPP_ID;
import static com.philips.cdp.dicommclient.networknode.NetworkNode.KEY_DEVICE_NAME;
import static com.philips.cdp.dicommclient.networknode.NetworkNode.KEY_IP_ADDRESS;
import static com.philips.cdp.dicommclient.networknode.NetworkNode.KEY_IS_PAIRED;
import static com.philips.cdp.dicommclient.networknode.NetworkNode.KEY_LAST_PAIRED;
import static com.philips.cdp.dicommclient.networknode.NetworkNode.KEY_MAC_ADDRESS;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

@RunWith(RobolectricTestRunner.class)
public class NetworkNodeDatabaseTest {

    @Mock
    private NetworkNodeDBHelper networkNodeDBHelperMock;

    @Mock
    private Cursor cursorMock;

    @Captor
    private ArgumentCaptor<ContentValues> contentValuesArgumentCaptor;

    private NetworkNodeDatabase networkNodeDatabase;
    
    private final String CPP_ID = "cppId";
    private final String DEVICE_NAME = "deviceName";
    private final String IP_ADDRESS = "198.162.1.0";
    private final String MAC_ADDRESS = "00:11:22:33:44:55";

    private final NetworkNode networkNode = new NetworkNode();

    @Before
    public void setUp() throws Exception {
        initMocks(this);

        DICommLog.disableLogging();

        networkNode.setCppId(CPP_ID);
        networkNode.setIpAddress(IP_ADDRESS);
        networkNode.setName(DEVICE_NAME);

        networkNodeDatabase = new NetworkNodeDatabase(networkNodeDBHelperMock);
    }

    @Test
    public void whenDataBaseContainsNoNodes_thenEmptyListIsReturned() {
        when(cursorMock.getCount()).thenReturn(0);
        when(networkNodeDBHelperMock.query(null, null)).thenReturn(cursorMock);

        List<NetworkNode> all = networkNodeDatabase.getAll();

        assertTrue(all.isEmpty());
    }

    @Test
    public void whenExceptionIsThrownWhileReadingDatabase_thenEmptyListIsReturned() {
        doThrow(Exception.class).when(networkNodeDBHelperMock).query(null, null);

        List<NetworkNode> all = networkNodeDatabase.getAll();

        assertTrue(all.isEmpty());
    }

    @Test
    public void whenDataBaseContainsSingleNode_thenSingleNodeIsReturned() {
        when(cursorMock.getCount()).thenReturn(1);
        when(networkNodeDBHelperMock.query(null, null)).thenReturn(cursorMock);

        List<NetworkNode> all = networkNodeDatabase.getAll();

        assertTrue(all.size() == 1);
    }

    @Test
    public void whenNodeIsReturned_thenCppIdIsReadFromDatabase() {
        when(cursorMock.getCount()).thenReturn(1);
        when(cursorMock.getColumnIndex(KEY_CPP_ID)).thenReturn(0);
        when(cursorMock.getString(0)).thenReturn(CPP_ID);
        when(networkNodeDBHelperMock.query(null, null)).thenReturn(cursorMock);

        List<NetworkNode> all = networkNodeDatabase.getAll();

        assertTrue(all.size() == 1);
        assertEquals(CPP_ID, all.get(0).getCppId());
    }

    @Test
    public void whenNodeIsReturned_thenMacAddressIsReadFromDatabase() {
        when(cursorMock.getCount()).thenReturn(1);
        when(cursorMock.getColumnIndex(KEY_MAC_ADDRESS)).thenReturn(0);
        when(cursorMock.getString(0)).thenReturn(MAC_ADDRESS);
        when(networkNodeDBHelperMock.query(null, null)).thenReturn(cursorMock);

        List<NetworkNode> all = networkNodeDatabase.getAll();

        assertTrue(all.size() == 1);
        assertEquals(MAC_ADDRESS, all.get(0).getMacAddress());
    }

    @Test
    public void whenNodeIsReturned_thenDeviceNameIsReadFromDatabase() {
        when(cursorMock.getCount()).thenReturn(1);
        when(cursorMock.getColumnIndex(KEY_DEVICE_NAME)).thenReturn(0);
        when(cursorMock.getString(0)).thenReturn(DEVICE_NAME);
        when(networkNodeDBHelperMock.query(null, null)).thenReturn(cursorMock);

        List<NetworkNode> all = networkNodeDatabase.getAll();

        assertTrue(all.size() == 1);
        assertEquals(DEVICE_NAME, all.get(0).getName());
    }

    @Test
    public void whenNodeIsReturned_thenIpAddressIsReadFromDatabase() {
        when(cursorMock.getCount()).thenReturn(1);
        when(cursorMock.getColumnIndex(KEY_IP_ADDRESS)).thenReturn(0);
        when(cursorMock.getString(0)).thenReturn(IP_ADDRESS);
        when(networkNodeDBHelperMock.query(null, null)).thenReturn(cursorMock);

        List<NetworkNode> all = networkNodeDatabase.getAll();

        assertTrue(all.size() == 1);
        assertEquals(IP_ADDRESS, all.get(0).getIpAddress());
    }

    @Test
    public void whenDataBaseContainsMultipleNode_thenAllNodeasAreReturned() {
        when(cursorMock.getCount()).thenReturn(3);
        when(cursorMock.moveToNext()).thenReturn(true).thenReturn(true).thenReturn(false);
        when(networkNodeDBHelperMock.query(null, null)).thenReturn(cursorMock);

        List<NetworkNode> all = networkNodeDatabase.getAll();

        assertTrue(all.size() == 3);
    }

    @Test
    public void whenExceptionIsThrownWhileReadingOneNode_thenSuccessfullyReadNodesAreReturned() {
        when(cursorMock.getCount()).thenReturn(3);
        when(cursorMock.moveToNext()).thenReturn(true).thenReturn(true).thenReturn(false);
        when(networkNodeDBHelperMock.query(null, null)).thenReturn(cursorMock);

        List<NetworkNode> all = networkNodeDatabase.getAll();

        assertTrue(all.size() == 3);
    }

    @Test
    public void whenNodeIsNull_thenNodeIsNotSaved() {
        long index = networkNodeDatabase.save(null);

        assertEquals(-1, index);
    }

    @Test
    public void whenNodeIsSaved_thenIpAddressIsSaved() {
        networkNodeDatabase.save(networkNode);

        verify(networkNodeDBHelperMock).insertRow(contentValuesArgumentCaptor.capture());
        assertEquals(IP_ADDRESS, contentValuesArgumentCaptor.getValue().get(KEY_IP_ADDRESS));
    }

    @Test
    public void whenNodeIsSaved_thenDeviceNameIsSaved() {
        networkNode.setName(DEVICE_NAME);

        networkNodeDatabase.save(networkNode);

        verify(networkNodeDBHelperMock).insertRow(contentValuesArgumentCaptor.capture());
        assertEquals(DEVICE_NAME, contentValuesArgumentCaptor.getValue().get(KEY_DEVICE_NAME));
    }

    @Test
    public void whenNodeIsSaved_thenDeviceMacAddressIsSaved() {
        networkNode.setMacAddress(MAC_ADDRESS);

        networkNodeDatabase.save(networkNode);

        verify(networkNodeDBHelperMock).insertRow(contentValuesArgumentCaptor.capture());
        assertEquals(MAC_ADDRESS, contentValuesArgumentCaptor.getValue().get(KEY_MAC_ADDRESS));
    }

    @Test
    public void whenNodeIsSaved_thenCppIdIsSaved() {
        networkNodeDatabase.save(networkNode);

        verify(networkNodeDBHelperMock).insertRow(contentValuesArgumentCaptor.capture());
        assertEquals(CPP_ID, contentValuesArgumentCaptor.getValue().get(KEY_CPP_ID));
    }

    @Test
    public void whenPairedNodeIsSaved_thenStatePairedIsSaved() {
        networkNode.setPairedState(NetworkNode.PairingState.PAIRED);

        networkNodeDatabase.save(networkNode);

        verify(networkNodeDBHelperMock).insertRow(contentValuesArgumentCaptor.capture());
        assertEquals(NetworkNode.PairingState.PAIRED.ordinal(), contentValuesArgumentCaptor.getValue().get(KEY_IS_PAIRED));
    }

    @Test
    public void whenPairedNodeIsSaved_thenLastPairingDateIsSaved() {
        networkNode.setPairedState(NetworkNode.PairingState.PAIRED);
        long lastPairedTime = 100L;
        networkNode.setLastPairedTime(lastPairedTime);

        networkNodeDatabase.save(networkNode);

        verify(networkNodeDBHelperMock).insertRow(contentValuesArgumentCaptor.capture());
        assertEquals(lastPairedTime, contentValuesArgumentCaptor.getValue().get(KEY_LAST_PAIRED));
    }

    @Test
    public void whenNotPairedNodeIsSaved_thenStateNotPairedIsSaved() {
        networkNode.setPairedState(NetworkNode.PairingState.PAIRING);

        networkNodeDatabase.save(networkNode);

        verify(networkNodeDBHelperMock).insertRow(contentValuesArgumentCaptor.capture());
        assertEquals(NetworkNode.PairingState.NOT_PAIRED.ordinal(), contentValuesArgumentCaptor.getValue().get(KEY_IS_PAIRED));
    }

    @Test
    public void whenNotPairedNodeIsSaved_thenLastPairingDateIsDefaultValue() {
        networkNode.setPairedState(NetworkNode.PairingState.PAIRING);
        networkNode.setLastPairedTime(100);

        networkNodeDatabase.save(networkNode);

        verify(networkNodeDBHelperMock).insertRow(contentValuesArgumentCaptor.capture());
        assertEquals(-1L, contentValuesArgumentCaptor.getValue().get(KEY_LAST_PAIRED));
    }

    @Test
    public void whenExceptionIsThrownWhileInsertingToDatabase_thenNodeIsNotSaved() {
        doThrow(SQLException.class).when(networkNodeDBHelperMock).insertRow(any(ContentValues.class));

        long index = networkNodeDatabase.save(networkNode);

        assertEquals(-1, index);
    }

    @Test
    public void whenNodeIsNull_thenDatabaseReturnsFalseForContains() {
        boolean contains = networkNodeDatabase.contains(null);

        assertFalse(contains);
    }

    @Test
    public void whenHelperContainsCursorForNode_thenDatabaseReturnsTrueForContains() {
        when(networkNodeDBHelperMock.query(KEY_CPP_ID + " = ?", new String[]{CPP_ID})).thenReturn(cursorMock);
        when(cursorMock.getCount()).thenReturn(1);

        boolean contains = networkNodeDatabase.contains(networkNode);

        assertTrue(contains);
    }

    @Test
    public void whenHelperContainsMultipleCursorForNode_thenDatabaseReturnsTrueForContains() {
        networkNode.setCppId(CPP_ID);
        when(networkNodeDBHelperMock.query(KEY_CPP_ID + " = ?", new String[]{CPP_ID})).thenReturn(cursorMock);
        when(cursorMock.getCount()).thenReturn(3);

        boolean contains = networkNodeDatabase.contains(networkNode);

        assertTrue(contains);
    }

    @Test
    public void whenHelperContainsNoCursorForNode_thenDatabaseReturnsFalseForContains() {
        networkNode.setCppId(CPP_ID);
        when(networkNodeDBHelperMock.query(KEY_CPP_ID + " = ?", new String[]{CPP_ID})).thenReturn(cursorMock);
        when(cursorMock.getCount()).thenReturn(0);

        boolean contains = networkNodeDatabase.contains(networkNode);

        assertFalse(contains);
    }

    @Test
    public void whenExceptionIsThrownWhileQueringTheHelper_thenDatabaseReturnsFalseForContains() {
        doThrow(SQLException.class).when(networkNodeDBHelperMock).query(KEY_CPP_ID + " = ?", new String[]{CPP_ID});

        boolean contains = networkNodeDatabase.contains(networkNode);

        assertFalse(contains);
    }

    @Test
    public void whenNetworkNodeIsDeleted_thenDatabaseReturns1DeletedRows() {
        when(networkNodeDBHelperMock.deleteNetworkNodeWithCppId(CPP_ID)).thenReturn(1);

        int count = networkNodeDatabase.delete(networkNode);

        assertEquals(1, count);
    }

    @Test
    public void whenMultipleNetworkNodeAreDeleted_thenDatabaseReturnsCountOfDeletedRows() {
        when(networkNodeDBHelperMock.deleteNetworkNodeWithCppId(CPP_ID)).thenReturn(3);

        int count = networkNodeDatabase.delete(networkNode);

        assertEquals(3, count);
    }

    @Test
    public void whenNullNetworkNodeIsDeleted_thenDatabaseReturns0DeletedRows() {
        int count = networkNodeDatabase.delete(null);

        assertEquals(0, count);
    }

    @Test
    public void whenExceptionIsThrownWhileDeletingNodeWithTheHelper_thenDatabaseReturns0DeletedRows() throws SQLException {
        doThrow(SQLException.class).when(networkNodeDBHelperMock).deleteNetworkNodeWithCppId(CPP_ID);

        int count = networkNodeDatabase.delete(networkNode);

        assertEquals(0, count);
    }
}