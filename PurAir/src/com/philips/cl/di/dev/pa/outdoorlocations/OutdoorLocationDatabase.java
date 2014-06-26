package com.philips.cl.di.dev.pa.outdoorlocations;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.philips.cl.di.dev.pa.PurAirApplication;
import com.philips.cl.di.dev.pa.R;
import com.philips.cl.di.dev.pa.constant.AppConstants;
import com.philips.cl.di.dev.pa.util.ALog;
import com.philips.cl.di.dev.pa.util.DatabaseHelper;

public class OutdoorLocationDatabase {
	
	private static final String TAG = OutdoorLocationDatabase.class.getSimpleName();
	
	private DatabaseHelper mDatabaseHelper = null;
	private SQLiteDatabase mOutdoorLocationDatabase = null;
	private static final String[] mTableColumns = new String[] {
									AppConstants.KEY_ID,
									AppConstants.KEY_CITY,
									AppConstants.KEY_AREA_ID,
									AppConstants.KEY_DISTRICT,
									AppConstants.KEY_CITY_CN,
									AppConstants.KEY_SHORTLIST };
	
	public OutdoorLocationDatabase() {
		mDatabaseHelper = new DatabaseHelper(PurAirApplication.getAppContext());
	}
	
	public synchronized void open() {
		if (mOutdoorLocationDatabase == null || !mOutdoorLocationDatabase.isOpen()) {
			mOutdoorLocationDatabase = mDatabaseHelper.getWritableDatabase();
		}
	}
	
	public synchronized void close() {
		if (mOutdoorLocationDatabase.isOpen()) {
			mOutdoorLocationDatabase.close();
		}
	}
	
	synchronized void fillDatabaseForCSV() {
		InputStream inputStream = PurAirApplication.getAppContext().getResources().openRawResource(R.raw.outdoor_locations_short);
		BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, Charset.defaultCharset()));
		
		ALog.i(ALog.OUTDOOR_LOCATION, "OutdoorLocationDatabase fillDatabaseForCSV inputStream " + inputStream + " reader " + reader);
		
		String outdoorLocation = "";
        StringTokenizer stringTokenizer = null;
        
        if (!isCityDetailsTableFilled()) {
	        mOutdoorLocationDatabase.beginTransaction();
	        try {
	        	
	            while ((outdoorLocation = reader.readLine()) != null) {
	            	stringTokenizer = new StringTokenizer(outdoorLocation, ";");
	            	
	                ContentValues values = new ContentValues();
	                values.put(AppConstants.KEY_CITY, stringTokenizer.nextToken());

	                String areaID = stringTokenizer.nextToken();
	        		values.put(AppConstants.KEY_AREA_ID, areaID);
	        		values.put(AppConstants.KEY_DISTRICT, stringTokenizer.nextToken());
	        		values.put(AppConstants.KEY_CITY_CN, stringTokenizer.nextToken());
	        		
	        		List<String> defaultCitiesList = new ArrayList<String>();
	        		defaultCitiesList.add("101010100");
	        		defaultCitiesList.add("101020100");
	        		defaultCitiesList.add("101270101");
	        		values.put(AppConstants.KEY_SHORTLIST, defaultCitiesList.contains(areaID) ? 1 : 0);
	        		mOutdoorLocationDatabase.insert(AppConstants.TABLE_CITYDETAILS, null, values);
	            }
	            mOutdoorLocationDatabase.setTransactionSuccessful();
	        
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
	        finally {
	        	mOutdoorLocationDatabase.endTransaction();
	        }
        }
	}
	
	/**
	 * Get outdoor locations that matches the filter
	 * 
	 * @param filterText Text that is used to filter outdoor locations
	 * @return cursor with outdoor locations
	 */
	public synchronized Cursor getDataFromOutdoorLoacation(String filterText) {
		Log.d(TAG, "Text to filter: " + filterText);
		
		Cursor cursor = null;
		
		if (filterText == null  ||  filterText.length () == 0)  {
			cursor = mOutdoorLocationDatabase.query(AppConstants.TABLE_CITYDETAILS, mTableColumns, 
					null, null, null, null, AppConstants.KEY_CITY + " ASC");
		}
		else {
			cursor = mOutdoorLocationDatabase.query(true, AppConstants.TABLE_CITYDETAILS, mTableColumns, 
			filterText, null, null, null, AppConstants.KEY_CITY + " ASC", null);
		}
		if (cursor != null) {
			cursor.moveToFirst();
		}
		return cursor;
	}
	
	/**
	 * Function to add the specified outdoor location to the short list
	 * 
	 * @param outdoorLocation location that needs to be updated
	 */
	public synchronized void updateOutdoorLocationShortListItem(String areaId, boolean inShortList) {
		ContentValues values = new ContentValues();
		if (inShortList) {
			values.put(AppConstants.KEY_SHORTLIST, "1");
		} else {
			values.put(AppConstants.KEY_SHORTLIST, "0");
		}
		
		mOutdoorLocationDatabase.update(AppConstants.TABLE_CITYDETAILS, values, AppConstants.KEY_AREA_ID + " = " + areaId, null);
	}
	
	private boolean isCityDetailsTableFilled() {
		ALog.i(ALog.DASHBOARD, "isCityDetailsTableFilled");
		Cursor cursor = getDataFromOutdoorLoacation(null);
		try {
			cursor.getString(cursor.getColumnIndex(AppConstants.KEY_AREA_ID));
		} catch (Exception e) {
			Log.w(ALog.DATABASE, "CityDetails database not yet filled " + e);
			return false;
		}
		return true;
	}
}



