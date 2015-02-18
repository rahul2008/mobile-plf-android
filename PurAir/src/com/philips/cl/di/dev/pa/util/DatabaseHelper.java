package com.philips.cl.di.dev.pa.util;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

import com.philips.cl.di.dev.pa.constant.AppConstants;

/**
 * The Class DBHelper.
 */
public class DatabaseHelper extends SQLiteOpenHelper {

	/**
	 * Instantiates a new dB helper.
	 * 
	 * @param context
	 *            the context
	 */
	public DatabaseHelper(Context context) {
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
	public DatabaseHelper(Context context, String name, CursorFactory factory, int version) {
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
		String createTableAirPurifierEvent = "CREATE TABLE "+ AppConstants.TABLE_AIRPURIFIER_EVENT + "(" 
				+ AppConstants.KEY_INDOOR_AQI + " INTEGER ," + 
				AppConstants.KEY_LAST_SYNC_DATETIME + " TEXT )";
		
		String createDeviceInfo = "CREATE TABLE IF NOT EXISTS " + AppConstants.TABLE_AIRPUR_INFO + "("
				+ AppConstants.KEY_ID + " INTEGER PRIMARY KEY,"
				+ AppConstants.KEY_AIRPUR_USN + " TEXT UNIQUE," 
				+ AppConstants.KEY_AIRPUR_CPP_ID + " TEXT UNIQUE," 
				+ AppConstants.KEY_AIRPUR_DEVICE_NAME + " TEXT," 
				+ AppConstants.KEY_AIRPUR_BOOT_ID + " NUMERIC,"
				+ AppConstants.KEY_AIRPUR_LASTKNOWN_NETWORK + " TEXT,"
				+ AppConstants.KEY_AIRPUR_IS_PAIRED + " SMALLINT NOT NULL  DEFAULT 0,"
				+ AppConstants.KEY_AIRPUR_LAST_PAIRED + " NUMERIC,"
				+ AppConstants.KEY_LATITUDE + " TEXT,"
				+ AppConstants.KEY_LONGITUDE + " TEXT,"
				+ AppConstants.KEY_AIRPUR_KEY + " TEXT" + ")";
		
		String createCityProviderTableSQL = getUserSelectedCityQuery();
		
		db.execSQL(createTableAirPurifierEvent) ;
		db.execSQL(createDeviceInfo);
		db.execSQL(createCityProviderTableSQL);
	}
	
	private String getUserSelectedCityQuery() {
		String query = "CREATE TABLE  IF NOT EXISTS " + AppConstants.TABLE_USER_SELECTED_CITY + " ("
				+ AppConstants.KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
				+ AppConstants.KEY_AREA_ID + " TEXT,"
				+ AppConstants.KEY_DATA_PROVIDER + " NUMERIC" 
				+ ")";
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
		try{
		db.execSQL("ALTER TABLE  " + AppConstants.TABLE_CITYDETAILS + " ADD COLUMN " + AppConstants.KEY_LONGITUDE + " NUMERIC");
		}catch(SQLException e){
			e.printStackTrace();
		}
		try {
			db.execSQL("ALTER TABLE  " + AppConstants.TABLE_CITYDETAILS + " ADD COLUMN " + AppConstants.KEY_LATITUDE + " NUMERIC");
		} catch (SQLException e) {
			e.printStackTrace();
		}
		try {
			db.execSQL("ALTER TABLE  " + AppConstants.TABLE_CITYDETAILS + " ADD COLUMN " + AppConstants.KEY_CITY_TW + " TEXT");
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		try {
			db.execSQL("ALTER TABLE  " + AppConstants.TABLE_AIRPUR_INFO + " ADD COLUMN " + AppConstants.KEY_LATITUDE + " TEXT");
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		try {
			db.execSQL("ALTER TABLE  " + AppConstants.TABLE_AIRPUR_INFO + " ADD COLUMN " + AppConstants.KEY_LONGITUDE + " TEXT");
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		try {
			db.execSQL(getUserSelectedCityQuery());
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}

}
