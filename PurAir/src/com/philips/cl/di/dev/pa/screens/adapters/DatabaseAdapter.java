package com.philips.cl.di.dev.pa.screens.adapters;

import com.philips.cl.di.dev.pa.constants.AppConstants;
import com.philips.cl.di.dev.pa.utils.DBHelper;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

/**
 * The Class DatabaseAdapter.
 */
public class DatabaseAdapter {
	
	/** The context. */
	Context context;
	
	/** The db helper. */
	DBHelper dbHelper;
	
	/** The db. */
	SQLiteDatabase db;

	/**
	 * Instantiates a new database adapter.
	 *
	 * @param context the context
	 */
	public DatabaseAdapter(Context context) {
		this.context = context;
	}

	/**
	 * Open.
	 *
	 * @return the SQlite database
	 */
	public SQLiteDatabase open() {
		dbHelper = new DBHelper(context);
		db = dbHelper.getWritableDatabase();
		return db;

	}

	/**
	 * Close.
	 */
	public void close() {
		dbHelper.close();
	}

	/**
	 * Insert into database.
	 *
	 * @param sCityName the  city name
	 * @param iAQI the  aqi
	 * @param sProvinceName the  province name
	 * @param sDate the  date
	 * @param sTime the  time
	 * @return true, if successful
	 */
	public boolean insertIntoDatabase(String sCityName, int iAQI,
			String sProvinceName, String sDate, String sTime) {
		ContentValues cvInsert = new ContentValues();
		cvInsert.put(AppConstants.KEY_CITY, sCityName);
		cvInsert.put(AppConstants.KEY_AQI, iAQI);
		cvInsert.put(AppConstants.KEY_DATE, sDate);
		cvInsert.put(AppConstants.KEY_PROVINCE, sProvinceName);
		cvInsert.put(AppConstants.KEY_TIME, "HARDCODE");
		String[] args = { sDate, sCityName };
		int iRowAffected = db.update(AppConstants.TABLENAME, cvInsert,
				AppConstants.KEY_DATE + "=? And " + AppConstants.KEY_CITY
						+ "=?", args);
		if (iRowAffected == 0) {
			// Insert
			iRowAffected = (int) db.insert(AppConstants.TABLENAME, null,
					cvInsert);
		}
		// return true if insert or update is successful
		return (iRowAffected > 0) ? true : false;
	}

}
