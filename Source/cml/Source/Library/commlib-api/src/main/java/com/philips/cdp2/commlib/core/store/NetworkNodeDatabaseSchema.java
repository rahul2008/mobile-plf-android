package com.philips.cdp2.commlib.core.store;

import com.philips.cdp.dicommclient.util.DICommLog;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.philips.cdp.dicommclient.networknode.NetworkNode.KEY_BOOT_ID;
import static com.philips.cdp.dicommclient.networknode.NetworkNode.KEY_CPP_ID;
import static com.philips.cdp.dicommclient.networknode.NetworkNode.KEY_DEVICE_NAME;
import static com.philips.cdp.dicommclient.networknode.NetworkNode.KEY_DEVICE_TYPE;
import static com.philips.cdp.dicommclient.networknode.NetworkNode.KEY_ENCRYPTION_KEY;
import static com.philips.cdp.dicommclient.networknode.NetworkNode.KEY_HTTPS;
import static com.philips.cdp.dicommclient.networknode.NetworkNode.KEY_ID;
import static com.philips.cdp.dicommclient.networknode.NetworkNode.KEY_IP_ADDRESS;
import static com.philips.cdp.dicommclient.networknode.NetworkNode.KEY_IS_PAIRED;
import static com.philips.cdp.dicommclient.networknode.NetworkNode.KEY_LAST_KNOWN_NETWORK;
import static com.philips.cdp.dicommclient.networknode.NetworkNode.KEY_LAST_PAIRED;
import static com.philips.cdp.dicommclient.networknode.NetworkNode.KEY_MAC_ADDRESS;
import static com.philips.cdp.dicommclient.networknode.NetworkNode.KEY_MISMATCHED_PIN;
import static com.philips.cdp.dicommclient.networknode.NetworkNode.KEY_MODEL_ID;
import static com.philips.cdp.dicommclient.networknode.NetworkNode.KEY_MODEL_NAME;
import static com.philips.cdp.dicommclient.networknode.NetworkNode.KEY_PIN;

class NetworkNodeDatabaseSchema {

    static final int DB_VERSION = 7;
    static final String TABLE_NETWORK_NODE = "network_node";

    static final String CREATE_NETWORK_NODE_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_NETWORK_NODE + "("
            + KEY_ID + " INTEGER NOT NULL UNIQUE,"
            + KEY_CPP_ID + " TEXT UNIQUE,"
            + KEY_MAC_ADDRESS + " TEXT,"
            + KEY_BOOT_ID + " NUMERIC,"
            + KEY_ENCRYPTION_KEY + " TEXT,"
            + KEY_DEVICE_NAME + " TEXT,"
            + KEY_LAST_KNOWN_NETWORK + " TEXT,"
            + KEY_IS_PAIRED + " SMALLINT NOT NULL DEFAULT 0,"
            + KEY_LAST_PAIRED + " NUMERIC,"
            + KEY_IP_ADDRESS + " TEXT,"
            + KEY_DEVICE_TYPE + " TEXT,"
            + KEY_MODEL_ID + " TEXT,"
            + KEY_HTTPS + " SMALLINT NOT NULL DEFAULT 0,"
            + KEY_PIN + " TEXT,"
            + KEY_MISMATCHED_PIN + " TEXT,"
            + "PRIMARY KEY(" + KEY_ID + ")"
            + ");";

    static String[] getUpgradeQuery(int oldVersion, int newVersion) {
        List<String> queries = new ArrayList<>();

        for (int currentVersion = oldVersion; currentVersion < newVersion; currentVersion++) {

            switch (currentVersion + 1) {
                case 2:
                    queries.addAll(Arrays.asList(addHttpsColumn()));
                    break;
                case 3:
                    queries.addAll(Arrays.asList(renameModelTypeToModelId()));
                    break;
                case 4:
                    queries.addAll(Arrays.asList(addPinColumn()));
                    break;
                case 5:
                    queries.addAll(Arrays.asList(renameModelNameToDeviceType()));
                    break;
                case 6:
                    queries.addAll(Arrays.asList(addMismatchPinColumn()));
                    break;
                case 7:
                    queries.addAll(Arrays.asList(addMacAddressAndHomeSsidColumns()));
                default:
                    DICommLog.e(DICommLog.DATABASE, "Table creation error");
                    break;
            }
        }

        return queries.toArray(new String[queries.size()]);
    }

    private static String[] addHttpsColumn() {
        return new String[]{"ALTER TABLE " + TABLE_NETWORK_NODE + " ADD COLUMN " + KEY_HTTPS + " SMALLINT NOT NULL DEFAULT 0;"};
    }

    private static String[] renameModelTypeToModelId() {
        return new String[]{
                "BEGIN TRANSACTION;",
                "ALTER TABLE " + TABLE_NETWORK_NODE + " RENAME TO tmp_" + TABLE_NETWORK_NODE + ";",
                "CREATE TABLE IF NOT EXISTS network_node("
                        + "_id INTEGER NOT NULL UNIQUE,"
                        + "cppid TEXT UNIQUE,"
                        + "bootid NUMERIC,"
                        + "encryption_key TEXT,"
                        + "dev_name TEXT,"
                        + "lastknown_network TEXT,"
                        + "is_paired SMALLINT NOT NULL DEFAULT 0,"
                        + "last_paired NUMERIC,"
                        + "ip_address TEXT,"
                        + "model_name TEXT,"
                        + "model_id TEXT,"
                        + "https SMALLINT NOT NULL DEFAULT 0,"
                        + "PRIMARY KEY(_id)"
                        + ");",
                "INSERT INTO " + TABLE_NETWORK_NODE + "(" + KEY_ID + "," + KEY_CPP_ID + "," + KEY_BOOT_ID + "," + KEY_ENCRYPTION_KEY + "," +
                        KEY_DEVICE_NAME + "," + KEY_LAST_KNOWN_NETWORK + "," + KEY_IS_PAIRED + "," + KEY_LAST_PAIRED + "," + KEY_IP_ADDRESS + "," + KEY_MODEL_NAME + "," + KEY_MODEL_ID + "," + KEY_HTTPS + ")\n" +
                        "SELECT " + KEY_ID + "," + KEY_CPP_ID + "," + KEY_BOOT_ID + "," + KEY_ENCRYPTION_KEY + "," +
                        KEY_DEVICE_NAME + "," + KEY_LAST_KNOWN_NETWORK + "," + KEY_IS_PAIRED + "," + KEY_LAST_PAIRED + "," + KEY_IP_ADDRESS + "," + KEY_MODEL_NAME + ",model_type," + KEY_HTTPS + "\n" +
                        "FROM tmp_" + TABLE_NETWORK_NODE + ";",

                "DROP TABLE tmp_" + TABLE_NETWORK_NODE + ";",
                "COMMIT;"};
    }

    private static String[] addPinColumn() {
        return new String[]{"ALTER TABLE " + TABLE_NETWORK_NODE + " ADD COLUMN " + KEY_PIN + " TEXT;"};
    }

    private static String[] renameModelNameToDeviceType() {
        return new String[]{"BEGIN TRANSACTION;",

                "ALTER TABLE " + TABLE_NETWORK_NODE + " RENAME TO tmp_" + TABLE_NETWORK_NODE + ";",

                "CREATE TABLE IF NOT EXISTS network_node("
                        + "_id INTEGER NOT NULL UNIQUE,"
                        + "cppid TEXT UNIQUE,"
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
                        + "PRIMARY KEY(_id)"
                        + ");",

                "INSERT INTO " + TABLE_NETWORK_NODE + "(" + KEY_ID + "," + KEY_CPP_ID + "," + KEY_BOOT_ID + "," + KEY_ENCRYPTION_KEY + "," +
                        KEY_DEVICE_NAME + "," + KEY_LAST_KNOWN_NETWORK + "," + KEY_IS_PAIRED + "," + KEY_LAST_PAIRED + "," + KEY_IP_ADDRESS + "," + KEY_DEVICE_TYPE + "," + KEY_MODEL_ID + "," + KEY_HTTPS + ")\n" +

                        "SELECT " + KEY_ID + "," + KEY_CPP_ID + "," + KEY_BOOT_ID + "," + KEY_ENCRYPTION_KEY + "," +
                        KEY_DEVICE_NAME + "," + KEY_LAST_KNOWN_NETWORK + "," + KEY_IS_PAIRED + "," + KEY_LAST_PAIRED + "," + KEY_IP_ADDRESS + ",model_name," + KEY_MODEL_ID + "," + KEY_HTTPS + "\n" +

                        "FROM tmp_" + TABLE_NETWORK_NODE + ";",

                "DROP TABLE tmp_" + TABLE_NETWORK_NODE + ";",

                "COMMIT;"};
    }

    private static String[] addMismatchPinColumn() {
        return new String[]{"ALTER TABLE " + TABLE_NETWORK_NODE + " ADD COLUMN " + KEY_MISMATCHED_PIN + " TEXT;"};
    }

    private static String[] addMacAddressAndHomeSsidColumns() {
        return new String[]{"ALTER TABLE " + NetworkNodeDatabaseSchema.TABLE_NETWORK_NODE + " ADD COLUMN " +
                KEY_MAC_ADDRESS + " STRING NULL",
                "UPDATE " + NetworkNodeDatabaseSchema.TABLE_NETWORK_NODE + " SET " +
                        KEY_MAC_ADDRESS + " = " + KEY_CPP_ID};
    }

}
