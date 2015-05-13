package com.philips.cdp.dicommclient.networknode;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

import com.philips.cdp.dicommclient.util.DLog;

public class NetworkNodeDatabaseHelper extends SQLiteOpenHelper {

	private static final int DB_VERSION = 1;

	// NetworkNode table
	public static final String DB_NAME = "network_node.db";
	public static final String TABLE_NETWORK_NODE = "network_node";
	public static final String KEY_ID = "_id";
	public static final String KEY_CPP_ID = "cppid";
	public static final String KEY_BOOT_ID = "bootid";
	public static final String KEY_ENCRYPTION_KEY = "encryption_key"; //TODO was airpur_key
	public static final String KEY_DEVICE_NAME = "dev_name";
	public static final String KEY_LASTKNOWN_NETWORK = "lastknown_network";
	public static final String KEY_IS_PAIRED = "is_paired";
	public static final String KEY_LAST_PAIRED = "last_paired";
	public static final String KEY_IP_ADDRESS = "ip_address";
	public static final String KEY_MODEL_NAME = "model_name";

	/**
	 * Instantiates a new dB helper.
	 *
	 * @param context
	 *            the context
	 */
	public NetworkNodeDatabaseHelper(Context context) {
		super(context, DB_NAME, null, DB_VERSION);
	}

	/**
	 * Instantiates a new dB helper.
	 *
	 * @param context
	 *            the context
	 * @param name
	 *            the name
	 * @param factory
	 *            the factory
	 * @param version
	 *            the version
	 */
	public NetworkNodeDatabaseHelper(Context context, String name, CursorFactory factory, int version) {
		super(context, name, factory, version);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * android.database.sqlite.SQLiteOpenHelper#onCreate(android.database.sqlite
	 * .SQLiteDatabase)
	 */
	@Override
	public void onCreate(SQLiteDatabase db) {
		DLog.w(DLog.DATABASE, "Create table " + TABLE_NETWORK_NODE);

		String createNetworkNodeTable = "CREATE TABLE IF NOT EXISTS " + TABLE_NETWORK_NODE + "("
				+ KEY_ID + " INTEGER NOT NULL UNIQUE,"
				+ KEY_CPP_ID + " TEXT UNIQUE,"
				+ KEY_BOOT_ID + " NUMERIC,"
				+ KEY_ENCRYPTION_KEY + " TEXT,"
				+ KEY_DEVICE_NAME + " TEXT,"
				+ KEY_LASTKNOWN_NETWORK + " TEXT,"
				+ KEY_IS_PAIRED + " SMALLINT NOT NULL  DEFAULT 0,"
				+ KEY_LAST_PAIRED + " NUMERIC,"
				+ KEY_IP_ADDRESS + " TEXT,"
				+ KEY_MODEL_NAME + " TEXT,"
				+ "PRIMARY KEY(" + KEY_ID + ")"
				+ ");";

		try {
			db.execSQL(createNetworkNodeTable);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * android.database.sqlite.SQLiteOpenHelper#onUpgrade(android.database.sqlite
	 * .SQLiteDatabase, int, int)
	 */
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		switch(oldVersion) {
		case 1:
			// code to migrate from DB version 1 to 2
			break;
		}
	}

}
