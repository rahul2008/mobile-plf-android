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

	private static final String PURIFIERDB_NAME = "smart_air.db";

	// AirPurifier table
	static final String TABLE_AIRPUR_INFO = "device_info";
	static final String NETWORK_NODE_ID_KEY = "network_node_id";
	static final String KEY_AIRPUR_USN = "usn";
	static final String KEY_LONGITUDE = "LONGITUDE";
	static final String KEY_LATITUDE = "LATITUDE";
	static final String KEY_AIRPUR_KEY = "airpur_key";

	// AirPurifier event table
	static final String KEY_INDOOR_AQI = "aqi";
	static final String KEY_LAST_SYNC_DATETIME = "lastsyncdatetime";
	static final String TABLE_AIRPURIFIER_EVENT = "AirPurifierEvent";

	// City data provider table
	static final String TABLE_USER_SELECTED_CITY = "UserSelectedCity";
	static final String KEY_DATA_PROVIDER = "DATA_PROVIDER";// 0 CMA and 1 US
															// Embassy

	// City detail table
	static final String TABLE_CITYDETAILS = "CityDetails";
	static final String KEY_CITY_TW = "CITY_TW";
	static final String KEY_AREA_ID = "AREA_ID";

	/**
	 * Instantiates a new dB helper.
	 * 
	 * @param context
	 *            the context
	 */
	public PurifierDatabaseHelper(Context context) {
		super(context, PURIFIERDB_NAME, null, AppConstants.PURIFIERDB_VERSION);
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
		String createTableAirPurifierEvent = "CREATE TABLE "
				+ TABLE_AIRPURIFIER_EVENT + "(" + KEY_INDOOR_AQI + " INTEGER ,"
				+ KEY_LAST_SYNC_DATETIME + " TEXT )";

		String createDeviceInfo = "CREATE TABLE IF NOT EXISTS "
				+ TABLE_AIRPUR_INFO + "(" + NetworkNodeDatabaseHelper.KEY_ID
				+ " INTEGER NOT NULL UNIQUE," + KEY_AIRPUR_USN
				+ " TEXT UNIQUE," + KEY_LATITUDE + " TEXT," + KEY_LONGITUDE
				+ " TEXT," + KEY_AIRPUR_KEY + " TEXT," + NETWORK_NODE_ID_KEY
				+ " INTEGER NOT NULL UNIQUE" + ")";

		String createCityProviderTableSQL = getUserSelectedCityQuery();

		db.execSQL(createTableAirPurifierEvent);
		db.execSQL(createDeviceInfo);
		db.execSQL(createCityProviderTableSQL);
	}

	private String getUserSelectedCityQuery() {
		String query = "CREATE TABLE  IF NOT EXISTS "
				+ TABLE_USER_SELECTED_CITY + " ("
				+ NetworkNodeDatabaseHelper.KEY_ID
				+ " INTEGER PRIMARY KEY AUTOINCREMENT," + KEY_AREA_ID
				+ " TEXT," + KEY_DATA_PROVIDER + " NUMERIC" + ")";
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
			db.execSQL("ALTER TABLE  " + TABLE_CITYDETAILS + " ADD COLUMN "
					+ KEY_LONGITUDE + " NUMERIC");
		} catch (SQLException e) {
			e.printStackTrace();
		}
		try {
			db.execSQL("ALTER TABLE  " + TABLE_CITYDETAILS + " ADD COLUMN "
					+ KEY_LATITUDE + " NUMERIC");
		} catch (SQLException e) {
			e.printStackTrace();
		}
		try {
			db.execSQL("ALTER TABLE  " + TABLE_CITYDETAILS + " ADD COLUMN "
					+ KEY_CITY_TW + " TEXT");
		} catch (Exception e) {
			e.printStackTrace();
		}

		try {
			db.execSQL("ALTER TABLE  " + TABLE_AIRPUR_INFO + " ADD COLUMN "
					+ KEY_LATITUDE + " TEXT");
		} catch (Exception e) {
			e.printStackTrace();
		}

		try {
			db.execSQL("ALTER TABLE  " + TABLE_AIRPUR_INFO + " ADD COLUMN "
					+ KEY_LONGITUDE + " TEXT");
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
			String upgradeQuery = "";
			db.execSQL(upgradeQuery);
			break;
		}

	}

}
