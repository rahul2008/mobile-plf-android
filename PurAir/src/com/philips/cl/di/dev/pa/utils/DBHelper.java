package com.philips.cl.di.dev.pa.utils;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.philips.cl.di.dev.pa.constants.AppConstants;

/**
 * The Class DBHelper.
 */
public class DBHelper extends SQLiteOpenHelper {

	/** The Constant TAG. */
	private static final String TAG = DBHelper.class.getName();

	/**
	 * Instantiates a new dB helper.
	 * 
	 * @param context
	 *            the context
	 */
	public DBHelper(Context context) {
		super(context, AppConstants.DB_NAME, null, AppConstants.DB_VERS);
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
	public DBHelper(Context context, String name, CursorFactory factory,
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
		Log.d(TAG, "ON CREATE for DATABASE");
		String createSQL = "CREATE TABLE " + AppConstants.TABLENAME + "("
				+ AppConstants.KEY_ID + " INTEGER PRIMARY KEY,"
				+ AppConstants.KEY_CITY + " TEXT," + AppConstants.KEY_PROVINCE
				+ " TEXT," + AppConstants.KEY_AQI + " NUMERIC,"
				+ AppConstants.KEY_DATE + " TEXT," + AppConstants.KEY_TIME
				+ " TEXT" + ")";
		String createTableAirPurifierEvent = "CREATE TABLE "+ AppConstants.TABLE_AIRPURIFIER_EVENT + "(" 
				+ AppConstants.INDOOR_AQI + " INTEGER ," + 
				AppConstants.LAST_SYNC_DATETIME + " TEXT )";
		
		db.execSQL(createSQL);
		db.execSQL(createTableAirPurifierEvent) ;

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
		Log.d(TAG, "ON UPGRADE for DATABASE");
		db.execSQL(String.format("DROP TABLE IF EXISTS %s",
				AppConstants.TABLENAME));
		db.execSQL(String.format("DROP TABLE IF EXISTS %s",
				AppConstants.TABLE_AIRPURIFIER_EVENT));
		this.onCreate(db);

	}

}
