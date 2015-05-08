package com.philips.cl.di.dev.pa.purifier;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;

import com.philips.cl.di.dev.pa.constant.AppConstants;
import com.philips.cl.di.dev.pa.database.NetworkNodeDatabaseHelper;
import com.philips.cl.di.dev.pa.util.ALog;

/**
 * The Class DBHelper.
 */
public class PurifierDatabaseHelper extends NetworkNodeDatabaseHelper {
	
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
		
		super.onCreate(db);
		
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
			super.onCreate(db);
			
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
					+ "FROM " + AppConstants.TABLE_AIRPUR_INFO;
			db.execSQL(migrateDataToNetworkNodeTableQuery);
			
			String createAirPurifierTableQuery = "CREATE TABLE IF NOT EXISTS " + AppConstants.TABLE_AIRPUR_DEVICE + "(" 
					+ AppConstants.KEY_ID + " INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT UNIQUE,"
					+ AppConstants.KEY_AIRPUR_CPP_ID + " TEXT NOT NULL UNIQUE,"
					+ AppConstants.KEY_AIRPUR_USN + " TEXT UNIQUE," 
					+ AppConstants.KEY_LATITUDE + " TEXT," 
					+ AppConstants.KEY_LONGITUDE + " TEXT,"
					+ ");";
			db.execSQL(createAirPurifierTableQuery);
			
			String migrateDataToAirPurifierDevicesTableQuery = "INSERT INTO " + AppConstants.TABLE_AIRPUR_DEVICE + " ("
					+ AppConstants.KEY_ID + COMMA
					+ AppConstants.KEY_AIRPUR_CPP_ID + COMMA
					+ AppConstants.KEY_AIRPUR_USN + COMMA
					+ AppConstants.KEY_LATITUDE + COMMA 
					+ AppConstants.KEY_LONGITUDE + COMMA
					+ ") SELECT "
					+ "NULL" + COMMA
					+ AppConstants.KEY_AIRPUR_CPP_ID + COMMA
					+ AppConstants.KEY_AIRPUR_USN + COMMA
					+ AppConstants.KEY_LATITUDE + COMMA 
					+ AppConstants.KEY_LONGITUDE + COMMA
					+ "FROM " + AppConstants.TABLE_AIRPUR_INFO;
			db.execSQL(migrateDataToAirPurifierDevicesTableQuery);

			String dropDeviceInfoTableQuery = "DROP TABLE IF EXISTS " + AppConstants.TABLE_AIRPUR_INFO;
			db.execSQL(dropDeviceInfoTableQuery);
			
			break;
		}
	}

}
