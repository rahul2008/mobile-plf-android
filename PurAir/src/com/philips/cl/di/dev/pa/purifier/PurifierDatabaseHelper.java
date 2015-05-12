package com.philips.cl.di.dev.pa.purifier;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

import com.philips.cdp.dicommclient.networknode.ConnectionState;
import com.philips.cdp.dicommclient.networknode.NetworkNode;
import com.philips.cdp.dicommclient.networknode.NetworkNodeDatabase;
import com.philips.cl.di.dev.pa.constant.AppConstants;
import com.philips.cl.di.dev.pa.util.ALog;

import static com.philips.cdp.dicommclient.networknode.NetworkNodeDatabaseHelper.*;

/**
 * The Class DBHelper.
 */
public class PurifierDatabaseHelper extends SQLiteOpenHelper {
	
	private static final String COMMA = ", ";
	
	/**
	 * Instantiates a new dB helper.
	 * 
	 * @param context
	 *            the context
	 */
	public PurifierDatabaseHelper(Context context) {
		super(context, AppConstants.PURIFIERDB_NAME, null, AppConstants.PURIFIERDB_VERSION);
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
	public PurifierDatabaseHelper(Context context, String name,
			CursorFactory factory, int version) {
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
		ALog.w(ALog.DATABASE, "Create table");
		
		String createTableAirPurifierEvent = "CREATE TABLE IF NOT EXISTS "
				+ AppConstants.TABLE_AIRPURIFIER_EVENT + "(" + AppConstants.KEY_INDOOR_AQI + " INTEGER ,"
				+ AppConstants.KEY_LAST_SYNC_DATETIME + " TEXT )";

		String createAirPurifierTableQuery = "CREATE TABLE IF NOT EXISTS " + AppConstants.TABLE_AIRPUR_DEVICE + "(" 
				+ AppConstants.KEY_ID + " INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT UNIQUE,"
				+ AppConstants.KEY_AIRPUR_CPP_ID + " TEXT NOT NULL UNIQUE,"
				+ AppConstants.KEY_AIRPUR_USN + " TEXT UNIQUE," //TODO remove usn
				+ AppConstants.KEY_LATITUDE + " TEXT," 
				+ AppConstants.KEY_LONGITUDE + " TEXT"
				+ ");";
		db.execSQL(createAirPurifierTableQuery);
		
		String createCityProviderTableSQL = getUserSelectedCityQuery();

		db.execSQL(createTableAirPurifierEvent);
		db.execSQL(createCityProviderTableSQL);
	}

	private String getUserSelectedCityQuery() {
		String query = "CREATE TABLE  IF NOT EXISTS "
				+ AppConstants.TABLE_USER_SELECTED_CITY + " ("
				+ AppConstants.KEY_ID
				+ " INTEGER PRIMARY KEY AUTOINCREMENT," + AppConstants.KEY_AREA_ID
				+ " TEXT," + AppConstants.KEY_DATA_PROVIDER + " NUMERIC" + ")";
		return query;
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
		try {
			db.execSQL("ALTER TABLE  " + AppConstants.TABLE_CITYDETAILS + " ADD COLUMN "
					+ AppConstants.KEY_LONGITUDE + " NUMERIC");
		} catch (SQLException e) {
			e.printStackTrace();
		}
		try {
			db.execSQL("ALTER TABLE  " + AppConstants.TABLE_CITYDETAILS + " ADD COLUMN "
					+ AppConstants.KEY_LATITUDE + " NUMERIC");
		} catch (SQLException e) {
			e.printStackTrace();
		}
		try {
			db.execSQL("ALTER TABLE  " + AppConstants.TABLE_CITYDETAILS + " ADD COLUMN "
					+ AppConstants.KEY_CITY_TW + " TEXT");
		} catch (Exception e) {
			e.printStackTrace();
		}

		try {
			db.execSQL("ALTER TABLE  " + AppConstants.TABLE_AIRPUR_INFO + " ADD COLUMN "
					+ AppConstants.KEY_LATITUDE + " TEXT");
		} catch (Exception e) {
			e.printStackTrace();
		}

		try {
			db.execSQL("ALTER TABLE  " + AppConstants.TABLE_AIRPUR_INFO + " ADD COLUMN "
					+ AppConstants.KEY_LONGITUDE + " TEXT");
		} catch (Exception e) {
			e.printStackTrace();
		}

		try {
			db.execSQL(getUserSelectedCityQuery());
		} catch (SQLException e) {
			e.printStackTrace();
		}

		
		switch (oldVersion) {
		case 10:
			// create the NetworkNode table
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
			
			// migrate the data from the 'device_info' to the 'network_node' table
			String migrateDataToNetworkNodeTableQuery = "INSERT INTO " + TABLE_NETWORK_NODE + "("
					+ KEY_ID + COMMA
					+ KEY_CPP_ID + COMMA 
					+ KEY_BOOT_ID + COMMA 
					+ KEY_ENCRYPTION_KEY + COMMA 
					+ KEY_DEVICE_NAME + COMMA 
					+ KEY_LASTKNOWN_NETWORK + COMMA 
					+ KEY_IS_PAIRED + COMMA 
					+ KEY_LAST_PAIRED + COMMA 
					+ KEY_IP_ADDRESS + COMMA
					+ KEY_MODEL_NAME
					+ ") SELECT "
					+ "NULL" + COMMA
					+ AppConstants.KEY_AIRPUR_CPP_ID + COMMA 
					+ AppConstants.KEY_AIRPUR_BOOT_ID + COMMA 
					+ AppConstants.KEY_AIRPUR_KEY + COMMA 
					+ AppConstants.KEY_AIRPUR_DEVICE_NAME + COMMA 
					+ AppConstants.KEY_AIRPUR_LASTKNOWN_NETWORK + COMMA 
					+ AppConstants.KEY_AIRPUR_IS_PAIRED + COMMA 
					+ AppConstants.KEY_AIRPUR_LAST_PAIRED + COMMA
					+ "NULL" + COMMA 
					+ "NULL" 
					+ " FROM " + AppConstants.TABLE_AIRPUR_INFO;
			try {
				db.execSQL(migrateDataToNetworkNodeTableQuery);
			} catch (SQLException e) {
				e.printStackTrace();
			}
			

			List<NetworkNode> result = new ArrayList<NetworkNode>();
			Cursor cursor = null;
			try {
				cursor = db.query(TABLE_NETWORK_NODE, null, null, null, null, null, null);
			
				if (cursor != null && cursor.getCount() > 0) {
					cursor.moveToFirst();
					
					do {
						String cppId = cursor.getString(cursor.getColumnIndex(KEY_CPP_ID));
						long bootId = cursor.getLong(cursor.getColumnIndex(KEY_BOOT_ID));
						String encryptionKey = cursor.getString(cursor.getColumnIndex(KEY_ENCRYPTION_KEY));
						String name = cursor.getString(cursor.getColumnIndex(KEY_DEVICE_NAME));
						String lastKnownNetwork = cursor.getString(cursor.getColumnIndex(KEY_LASTKNOWN_NETWORK));
						int pairedStatus = cursor.getInt(cursor.getColumnIndex(KEY_IS_PAIRED));
						long lastPairedTime = cursor.getLong(cursor.getColumnIndexOrThrow(KEY_LAST_PAIRED));
						String ipAddress = cursor.getString(cursor.getColumnIndex(KEY_IP_ADDRESS));
						String modelName = cursor.getString(cursor.getColumnIndex(KEY_MODEL_NAME));
						
						NetworkNode networkNode = new NetworkNode();
						networkNode.setConnectionState(ConnectionState.DISCONNECTED);
						networkNode.setCppId(cppId);
						networkNode.setBootId(bootId);
						networkNode.setEncryptionKey(encryptionKey);
						networkNode.setName(name);
						networkNode.setHomeSsid(lastKnownNetwork);
						networkNode.setPairedState(NetworkNode.getPairedStatusKey(pairedStatus));
						networkNode.setLastPairedTime(lastPairedTime);
						networkNode.setIpAddress(ipAddress);
						networkNode.setModelName(modelName);
						
						result.add(networkNode);
						ALog.d(ALog.DATABASE, "Loaded NetworkNode from db: " + networkNode);
					} while (cursor.moveToNext());
				} else {
					ALog.i(ALog.DATABASE, "Empty network node table");
				}
			} catch (Exception e) {
				ALog.e(ALog.DATABASE, "Error: " + e.getMessage());
			} finally {
				closeCursor(cursor);
			}
			
			NetworkNodeDatabase networkNodeDatabase = new NetworkNodeDatabase();
			for (NetworkNode networkNode : result) {
				networkNodeDatabase.save(networkNode);
			}
			
			String createAirPurifierTableQuery = "CREATE TABLE IF NOT EXISTS " + AppConstants.TABLE_AIRPUR_DEVICE + "(" 
					+ AppConstants.KEY_ID + " INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT UNIQUE,"
					+ AppConstants.KEY_AIRPUR_CPP_ID + " TEXT NOT NULL UNIQUE,"
					+ AppConstants.KEY_AIRPUR_USN + " TEXT UNIQUE," 
					+ AppConstants.KEY_LATITUDE + " TEXT," 
					+ AppConstants.KEY_LONGITUDE + " TEXT"
					+ ");";
			try {
				db.execSQL(createAirPurifierTableQuery);
			} catch (SQLException e) {
				e.printStackTrace();
			}
			
			String migrateDataToAirPurifierDevicesTableQuery = "INSERT INTO " + AppConstants.TABLE_AIRPUR_DEVICE + " ("
					+ AppConstants.KEY_ID + COMMA
					+ AppConstants.KEY_AIRPUR_CPP_ID + COMMA
					+ AppConstants.KEY_AIRPUR_USN + COMMA
					+ AppConstants.KEY_LATITUDE + COMMA 
					+ AppConstants.KEY_LONGITUDE
					+ ") SELECT "
					+ "NULL" + COMMA
					+ AppConstants.KEY_AIRPUR_CPP_ID + COMMA
					+ AppConstants.KEY_AIRPUR_USN + COMMA
					+ AppConstants.KEY_LATITUDE + COMMA 
					+ AppConstants.KEY_LONGITUDE
					+ " FROM " + AppConstants.TABLE_AIRPUR_INFO;
			try {
				db.execSQL(migrateDataToAirPurifierDevicesTableQuery);
			} catch (SQLException e) {
				e.printStackTrace();
			}

			String dropDeviceInfoTableQuery = "DROP TABLE IF EXISTS " + AppConstants.TABLE_AIRPUR_INFO;
			try {
				db.execSQL(dropDeviceInfoTableQuery);
			} catch (SQLException e) {
				e.printStackTrace();
			}

			String dropNetworkNodeTableQuery = "DROP TABLE IF EXISTS " + TABLE_NETWORK_NODE;
			try {
				db.execSQL(dropNetworkNodeTableQuery);
			} catch (SQLException e) {
				e.printStackTrace();
			}
			
			ALog.d(ALog.DATABASE, createNetworkNodeTable);
			ALog.d(ALog.DATABASE, migrateDataToNetworkNodeTableQuery);
			ALog.d(ALog.DATABASE, createAirPurifierTableQuery);
			ALog.d(ALog.DATABASE, migrateDataToAirPurifierDevicesTableQuery);
			ALog.d(ALog.DATABASE, dropDeviceInfoTableQuery);
			ALog.d(ALog.DATABASE, dropNetworkNodeTableQuery);
			
			break;
		}
	}
	
	private void closeCursor(Cursor cursor) {
		try {
			if (cursor != null && !cursor.isClosed() ) {
				cursor.close();
			}
		} catch (Exception e) {
			ALog.e(ALog.DATABASE, "Error: " + e.getMessage());
		}
	}

}
