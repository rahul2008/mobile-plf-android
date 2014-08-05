package com.philips.cl.di.dev.pa.util;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

import com.philips.cl.di.dev.pa.constant.AppConstants;

/**
 * The Class DBHelper.
 */
public class PurifierDBHelper extends SQLiteOpenHelper {

	/**
	 * Instantiates a new dB helper.
	 * 
	 * @param context
	 *            the context
	 */
	public PurifierDBHelper(Context context) {
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
	public PurifierDBHelper(Context context, String name, CursorFactory factory,
			int version) {
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
		ALog.d(ALog.DATABASE, "Create table");
		String createSQL = "CREATE TABLE " + AppConstants.TABLE_CITYDETAILS + "("
				+ AppConstants.KEY_ID + " INTEGER PRIMARY KEY,"
				+ AppConstants.KEY_CITY + " TEXT," 
				+ AppConstants.KEY_PROVINCE + " TEXT," 
				+ AppConstants.KEY_AQI + " NUMERIC,"
				+ AppConstants.KEY_DATE + " TEXT," 
				+ AppConstants.KEY_TIME	+ " TEXT" + ")";
		
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
				+ AppConstants.KEY_AIRPUR_KEY + " TEXT" + ")";
		
		db.execSQL(createSQL);
		db.execSQL(createTableAirPurifierEvent) ;
		db.execSQL(createDeviceInfo);

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
		ALog.d(ALog.DATABASE, "Upgrade table");
		db.execSQL(String.format("DROP TABLE IF EXISTS %s",
				AppConstants.TABLE_CITYDETAILS));
		db.execSQL(String.format("DROP TABLE IF EXISTS %s",
				AppConstants.TABLE_AIRPURIFIER_EVENT));
		db.execSQL(String.format("DROP TABLE IF EXISTS %s",
				AppConstants.TABLE_AIRPUR_INFO));
		this.onCreate(db);

	}

}
