package com.philips.cl.di.dev.pa.screens.adapters;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.philips.cl.di.dev.pa.constants.AppConstants;
import com.philips.cl.di.dev.pa.dto.AirPurifierEventDto;
import com.philips.cl.di.dev.pa.utils.PurifierDBHelper;
import com.philips.cl.di.dev.pa.utils.Utils;

/**
 * The Class DatabaseAdapter.
 */
public class DatabaseAdapter {
	
	private static final String TAG = DatabaseAdapter.class.getSimpleName() ;

	/** The context. */
	Context context;

	/** The db helper. */
	PurifierDBHelper dbHelper;

	/** The db. */
	SQLiteDatabase db;

	/**
	 * Instantiates a new database adapter.
	 * 
	 * @param context
	 *            the context
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
		Log.i(TAG, "Open") ;
		dbHelper = new PurifierDBHelper(context);
		File dbfile = new File(context.getFilesDir() +"/"+AppConstants.DATABASE);
		db = SQLiteDatabase.openOrCreateDatabase(dbfile, null);
		return db;

	}

	/**
	 * Close.
	 */
	public void close() {
		if(dbHelper!=null )
		dbHelper.close();
	}

	/**
	 * Insert into database.
	 * 
	 * @param sCityName
	 *            the city name
	 * @param iAQI
	 *            the aqi
	 * @param sProvinceName
	 *            the province name
	 * @param sDate
	 *            the date
	 * @param sTime
	 *            the time
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

	/**
	 * This will return the list of city names
	 * @return
	 */
	public List<String> getCityNames() {
		List<String> alCitiNames = new ArrayList<String>();
		Cursor curCityNames = db.rawQuery(AppConstants.sCityNameQuery, null);
		while (curCityNames.moveToNext()) {
			alCitiNames.add(curCityNames.getString(0));
		}

		return alCitiNames;
	}

	
	/**
	 * Insert into database.
	 * 
	 * @param aqi
	 *            Air Quality Index
	 * @return true, if successful
	 */
	public boolean insertAirPurifierEvent(int aqi) {
		ContentValues cvInsert = new ContentValues();
		cvInsert.put(AppConstants.INDOOR_AQI,aqi);
		cvInsert.put(AppConstants.LAST_SYNC_DATETIME, Utils.getCurrentDateTime()) ;
		
		db.delete(AppConstants.TABLE_AIRPURIFIER_EVENT, null, null) ;
		// Insert
		int iRowAffected = (int) db.insert(AppConstants.TABLE_AIRPURIFIER_EVENT, null,
				cvInsert);
		
		// return true if insert or update is successful
		return (iRowAffected > 0) ? true : false;
	}
	
	
	/**
	 * This will return the last updated Air Purifier Event
	 * @return
	 */
	public AirPurifierEventDto getLastUpdatedEvent() {
		AirPurifierEventDto dto = null ;
		Cursor event = db.rawQuery(AppConstants.airPurifierEventQuery, null);
		if (event.moveToNext()) {
			
			dto = new AirPurifierEventDto() ;	
			dto.setIndoorAQI(event.getInt(0)) ;
			dto.setTimeStamp(event.getString(1)) ;
		}
		return dto ;
	}
	
	
	
	/**
	 * Gets the aQI values for day.
	 *
	 * @param Day in the format mm/dd/yyyy
	 * @param iCityId the i city id
	 * @return the aQI values for day
	 */
	public  ArrayList<Integer>  getAQIValuesForDay(String sDay, int iCityId)
	{
		ArrayList<Integer> arrayAQI = new ArrayList<Integer>();
		String QUERY_AQIVALUES = "Select aqi from aqitable where cityid="+ iCityId+" and date like '"+sDay+"%' " + " order by date";
		
		Cursor curAQI = db.rawQuery(QUERY_AQIVALUES, null);
		while (curAQI.moveToNext())
		{
			arrayAQI.add(curAQI.getInt(0));
		}
		
		return arrayAQI;
	}
}
