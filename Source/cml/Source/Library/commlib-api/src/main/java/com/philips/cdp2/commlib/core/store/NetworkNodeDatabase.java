/*
 * Copyright (c) 2015-2018 Koninklijke Philips N.V.
 * All rights reserved.
 */

package com.philips.cdp2.commlib.core.store;

import android.content.ContentValues;
import android.database.Cursor;
import com.philips.cdp.dicommclient.networknode.NetworkNode;
import com.philips.cdp.dicommclient.util.DICommLog;

import java.util.ArrayList;
import java.util.List;

import static com.philips.cdp.dicommclient.networknode.NetworkNode.KEY_BOOT_ID;
import static com.philips.cdp.dicommclient.networknode.NetworkNode.KEY_CPP_ID;
import static com.philips.cdp.dicommclient.networknode.NetworkNode.KEY_DEVICE_NAME;
import static com.philips.cdp.dicommclient.networknode.NetworkNode.KEY_DEVICE_TYPE;
import static com.philips.cdp.dicommclient.networknode.NetworkNode.KEY_ENCRYPTION_KEY;
import static com.philips.cdp.dicommclient.networknode.NetworkNode.KEY_HTTPS;
import static com.philips.cdp.dicommclient.networknode.NetworkNode.KEY_IP_ADDRESS;
import static com.philips.cdp.dicommclient.networknode.NetworkNode.KEY_IS_PAIRED;
import static com.philips.cdp.dicommclient.networknode.NetworkNode.KEY_LAST_KNOWN_NETWORK;
import static com.philips.cdp.dicommclient.networknode.NetworkNode.KEY_LAST_PAIRED;
import static com.philips.cdp.dicommclient.networknode.NetworkNode.KEY_MODEL_ID;
import static com.philips.cdp.dicommclient.networknode.NetworkNode.KEY_PIN;

/**
 * This database stores {@link NetworkNode}s.
 *
 * @publicApi
 */

public class NetworkNodeDatabase {

    private NetworkNodeDBHelper networkNodeDBHelper;

    public NetworkNodeDatabase(NetworkNodeDBHelper networkNodeDBHelper) {
        this.networkNodeDBHelper = networkNodeDBHelper;
    }

    /**
     * List all {@link NetworkNode}s currently stored.
     *
     * @return List of networkNodes.
     */
    public List<NetworkNode> getAll() {
        List<NetworkNode> result = new ArrayList<>();

        Cursor cursor = null;
        try {
            cursor = networkNodeDBHelper.query(null, null);

            if (cursor != null && cursor.getCount() > 0) {
                cursor.moveToFirst();

                do {
                    String cppId = cursor.getString(cursor.getColumnIndex(KEY_CPP_ID));
                    long bootId = cursor.getLong(cursor.getColumnIndex(KEY_BOOT_ID));
                    String encryptionKey = cursor.getString(cursor.getColumnIndex(KEY_ENCRYPTION_KEY));
                    String name = cursor.getString(cursor.getColumnIndex(KEY_DEVICE_NAME));
                    String lastKnownNetwork = cursor.getString(cursor.getColumnIndex(KEY_LAST_KNOWN_NETWORK));
                    int pairedStatus = cursor.getInt(cursor.getColumnIndex(KEY_IS_PAIRED));
                    long lastPairedTime = cursor.getLong(cursor.getColumnIndexOrThrow(KEY_LAST_PAIRED));
                    String ipAddress = cursor.getString(cursor.getColumnIndex(KEY_IP_ADDRESS));
                    String deviceType = cursor.getString(cursor.getColumnIndex(KEY_DEVICE_TYPE));
                    String modelId = cursor.getString(cursor.getColumnIndex(KEY_MODEL_ID));
                    String pin = cursor.getString(cursor.getColumnIndex(KEY_PIN));

                    NetworkNode networkNode = new NetworkNode();
                    networkNode.setCppId(cppId);
                    networkNode.setBootId(bootId);
                    networkNode.setEncryptionKey(encryptionKey);
                    networkNode.setName(name);
                    networkNode.setHomeSsid(lastKnownNetwork);
                    networkNode.setPairedState(NetworkNode.getPairedStatusKey(pairedStatus));
                    networkNode.setLastPairedTime(lastPairedTime);
                    networkNode.setIpAddress(ipAddress);
                    networkNode.setDeviceType(deviceType);
                    networkNode.setModelId(modelId);
                    networkNode.setPin(pin);

                    result.add(networkNode);
                    DICommLog.d(DICommLog.DATABASE, "Loaded NetworkNode from db: " + networkNode);
                } while (cursor.moveToNext());
            } else {
                DICommLog.i(DICommLog.DATABASE, "Empty network node table");
            }
        } catch (Exception e) {
            DICommLog.e(DICommLog.DATABASE, "Error: " + e.getMessage());
        } finally {
            closeCursor(cursor);
        }

        return result;
    }

    /**
     * Save a {@link NetworkNode}.
     * <p>
     * If the node was already in the database it will be overwritten.
     *
     * @param networkNode {@link NetworkNode} to save.
     * @return rowId of the saved node, -1L if not successful.
     */
    public long save(NetworkNode networkNode) {
        long rowId = -1L;

        if (networkNode == null) {
            return rowId;
        }

        if (networkNode.getPairedState() != NetworkNode.PairingState.PAIRED) {
            networkNode.setPairedState(NetworkNode.PairingState.NOT_PAIRED);
        }

        try {
            ContentValues values = new ContentValues();
            values.put(KEY_CPP_ID, networkNode.getCppId());
            values.put(KEY_BOOT_ID, networkNode.getBootId());
            values.put(KEY_ENCRYPTION_KEY, networkNode.getEncryptionKey());
            values.put(KEY_DEVICE_NAME, networkNode.getName());
            values.put(KEY_LAST_KNOWN_NETWORK, networkNode.getHomeSsid());
            values.put(KEY_IS_PAIRED, networkNode.getPairedState().ordinal());

            if (networkNode.getPairedState() == NetworkNode.PairingState.PAIRED) {
                values.put(KEY_LAST_PAIRED, networkNode.getLastPairedTime());
            } else {
                values.put(KEY_LAST_PAIRED, -1L);
            }

            values.put(KEY_IP_ADDRESS, networkNode.getIpAddress());
            values.put(KEY_DEVICE_TYPE, networkNode.getDeviceType());
            values.put(KEY_MODEL_ID, networkNode.getModelId());
            values.put(KEY_HTTPS, networkNode.isHttps());
            values.put(KEY_PIN, networkNode.getPin());

            rowId = networkNodeDBHelper.insertRow(values);
            DICommLog.d(DICommLog.DATABASE, "Saved NetworkNode in db: " + networkNode);
        } catch (Exception e) {
            DICommLog.e(DICommLog.DATABASE, "Failed to save NetworkNode" + " ,Error: " + e.getMessage());
        }

        return rowId;
    }

    /**
     * Check to see if this database contains a specific {@link NetworkNode}.
     *
     * @param networkNode {@link NetworkNode} to find.
     * @return <code>true</code> if the {@link NetworkNode} is contained in this database.
     */
    public boolean contains(NetworkNode networkNode) {
        if (networkNode == null) return false;

        Cursor cursor = null;
        try {
            cursor = networkNodeDBHelper.query(KEY_CPP_ID + " = ?", new String[]{networkNode.getCppId()});

            if (cursor.getCount() > 0) {
                DICommLog.d(DICommLog.DATABASE, "NetworkNode already in db - " + networkNode);
                return true;
            }
        } catch (Exception e) {
            DICommLog.e(DICommLog.DATABASE, "Error: " + e.getMessage());
        } finally {
            closeCursor(cursor);
        }

        DICommLog.d(DICommLog.DATABASE, "NetworkNode not yet in db - " + networkNode);
        return false;
    }

    /**
     * Delete a {@link NetworkNode} from this database.
     *
     * @param networkNode {@link NetworkNode} to deleteNetworkNodeWithCppId.
     * @return the number of rows deleted.
     */
    public int delete(NetworkNode networkNode) {
        int rowsDeleted = 0;
        try {
            rowsDeleted = networkNodeDBHelper.deleteNetworkNodeWithCppId(networkNode.getCppId());
            DICommLog.d(DICommLog.DATABASE, "Deleted NetworkNode from db: " + networkNode + "  (" + rowsDeleted + ")");
        } catch (Exception e) {
            DICommLog.e(DICommLog.DATABASE, "Error: " + e.getMessage());
        }
        return rowsDeleted;
    }

    private void closeCursor(Cursor cursor) {
        try {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        } catch (Exception e) {
            DICommLog.e(DICommLog.DATABASE, "Error: " + e.getMessage());
        }
    }
}
