package com.philips.cl.di.dev.pa.outdoorlocations;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.StringTokenizer;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.util.Log;

import com.philips.cl.di.dev.pa.PurAirApplication;
import com.philips.cl.di.dev.pa.R;
import com.philips.cl.di.dev.pa.constant.AppConstants;
import com.philips.cl.di.dev.pa.dashboard.OutdoorManager;
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
									AppConstants.KEY_LONGITUDE,
									AppConstants.KEY_LATITUDE,
									AppConstants.KEY_CITY_CN,
									AppConstants.KEY_SHORTLIST,
									AppConstants.KEY_CITY_TW };
	
	public static String CURR_LOC_PREF = "current_loc_pref";
	public static String CURR_LOC_AREAID = "current_loc_aid";
	
	public OutdoorLocationDatabase() {
		mDatabaseHelper = new DatabaseHelper(PurAirApplication.getAppContext());
	}
	
	public synchronized void open() {
		try {
			if (mOutdoorLocationDatabase == null || !mOutdoorLocationDatabase.isOpen()) {
				mOutdoorLocationDatabase = mDatabaseHelper.getWritableDatabase();
			}
		} catch (SQLiteConstraintException e) {
			e.printStackTrace();
		} catch (SQLiteException e) {
			e.printStackTrace();
		}
	}
	
	public synchronized void close() {
		try {
			if (mOutdoorLocationDatabase != null && mOutdoorLocationDatabase.isOpen()) {
				mOutdoorLocationDatabase.close();
			}
		} catch (SQLiteConstraintException e) {
			e.printStackTrace();
		} catch (SQLiteException e) {
			e.printStackTrace();
		}
	}
	
	synchronized void fillDatabaseForCSV() {
        if (!isCityDetailsTableFilled()) {
        	
        	InputStream inputStream = PurAirApplication.getAppContext().getResources().openRawResource(R.raw.city_list_hk);
    		BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, Charset.defaultCharset()));
    		
    		ALog.i(ALog.OUTDOOR_LOCATION,
    				"OutdoorLocationDatabase fillDatabaseForCSV inputStream " + inputStream + " reader " + reader);
    		
    		String outdoorLocation = "";
            StringTokenizer stringTokenizer = null;
            
            //CMA
    		OutdoorManager.getInstance().addAreaIDToUsersList("101010100");
    		OutdoorManager.getInstance().addCityDataToMap(null, null, null, "101010100");
    		
    		if (mOutdoorLocationDatabase == null) return;//Due to synchronized DB open null
    		
	        mOutdoorLocationDatabase.beginTransaction();
	        try {
	        	
	            while ((outdoorLocation = reader.readLine()) != null) {
	            	stringTokenizer = new StringTokenizer(outdoorLocation, ";");
	            	
	                ContentValues values = new ContentValues();
	                values.put(AppConstants.KEY_CITY, stringTokenizer.nextToken());
	                
	                String areaID = stringTokenizer.nextToken();
	        		values.put(AppConstants.KEY_AREA_ID, areaID);
	        		values.put(AppConstants.KEY_LONGITUDE, stringTokenizer.nextToken());
	        		values.put(AppConstants.KEY_LATITUDE, stringTokenizer.nextToken());
	        		values.put(AppConstants.KEY_CITY_CN, stringTokenizer.nextToken());
	        		values.put(AppConstants.KEY_CITY_TW, stringTokenizer.nextToken());

	        		int state = OutdoorManager.getInstance().getUsersCitiesList().contains(areaID) ? 1 : 0;
	        		values.put(AppConstants.KEY_SHORTLIST, state);
	        		long rowId = mOutdoorLocationDatabase.insert(AppConstants.TABLE_CITYDETAILS, null, values);
	        		if (rowId != -1 && state == 1) {
	        			insertCityProviderDataIntoTable(mOutdoorLocationDatabase, areaID, OutdoorDataProvider.CMA.ordinal());
	        		}
	        	}
	            
	            mOutdoorLocationDatabase.setTransactionSuccessful();
	        } catch (IOException e) {
	            e.printStackTrace();
	        } catch (SQLiteConstraintException e) {
	            e.printStackTrace();
	        } catch (SQLiteException e) {
	            e.printStackTrace();
	        }
	        finally {
	        	mOutdoorLocationDatabase.endTransaction();
	        }
        }
	}
	
	/*
	private boolean isOutdoorDetailProviderUSEmbassy(String city) {
		List<String> usEmbassyCitiesList = Arrays.asList(AppConstants.USEMBASSY_CITIES);
		if (usEmbassyCitiesList.contains(city)) return true;
		return false;
	}
	*/
	
	private void insertCityProviderDataIntoTable(SQLiteDatabase sqLiteDatabase,
			String areaID,  int provider) {
		if (sqLiteDatabase == null) return;
		ContentValues values = new ContentValues();
		values.put(AppConstants.KEY_DATA_PROVIDER, provider);
		values.put(AppConstants.KEY_AREA_ID, areaID);
		sqLiteDatabase.insert(AppConstants.TABLE_USER_SELECTED_CITY, null, values);
	}
	/**
	 * Get outdoor locations that matches the filter
	 * 
	 * @param filterText Text that is used to filter outdoor locations
	 * @return cursor with outdoor locations
	 */
	public synchronized Cursor getDataFromOutdoorLoacation(String filterText) {
		Log.d(TAG, "Text to filter: " + filterText);
		if (mOutdoorLocationDatabase == null) return null; //Due to synchronized DB open null
		Cursor cursor = null;
		try {
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
		} catch (SQLiteConstraintException e) {
			e.printStackTrace();
		} catch (SQLiteException e) {
			e.printStackTrace();
		}
		return cursor;
	}
	
	public synchronized Cursor getUserSelectedCityData() {
		if (mOutdoorLocationDatabase == null) return null; //Due to synchronized DB open null
		Cursor cursor = null;
		try {
			cursor = mOutdoorLocationDatabase.query(AppConstants.TABLE_USER_SELECTED_CITY, null, 
					null, null, null, null, null);
			if (cursor != null) {
				cursor.moveToFirst();
			}
		} catch (SQLiteConstraintException e) {
			e.printStackTrace();
		} catch (SQLiteException e) {
			e.printStackTrace();
		}
		return cursor;
	}
	
	public synchronized Cursor getDataCurrentLoacation(String areaID) {
		if (mOutdoorLocationDatabase == null) return null;//Due to synchronized DB open null
		try {
			Cursor cursor = mOutdoorLocationDatabase.query(true, AppConstants.TABLE_CITYDETAILS, mTableColumns, 
					AppConstants.KEY_AREA_ID + "= ?", new String[]{areaID}, null, null, null, null);
			return cursor;
		} catch (SQLiteConstraintException e) {
			e.printStackTrace();
		} catch (SQLiteException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * Function to add the specified outdoor location to the short list
	 * 
	 * @param outdoorLocation location that needs to be updated
	 */
	public synchronized void updateOutdoorLocationShortListItem(String areaId, boolean inShortList) {
		if (mOutdoorLocationDatabase == null) return;
		try {
			ContentValues values = new ContentValues();
			if (inShortList) {
				values.put(AppConstants.KEY_SHORTLIST, "1");
			} else {
				values.put(AppConstants.KEY_SHORTLIST, "0");
			}
			
			mOutdoorLocationDatabase.update(
					AppConstants.TABLE_CITYDETAILS, values, AppConstants.KEY_AREA_ID + " = " + areaId, null);
		} catch (SQLiteConstraintException e) {
			e.printStackTrace();
		} catch (SQLiteException e) {
			e.printStackTrace();
		}
	}
	
	private boolean isCityDetailsTableFilled() {
		ALog.i(ALog.DASHBOARD, "isCityDetailsTableFilled");
		
		Cursor cursor = getDataCurrentLoacation("101010100");
		if (cursor == null || cursor.getCount() != 1) return false;
		cursor.moveToFirst();
		try {
			cursor.getString(cursor.getColumnIndex(AppConstants.KEY_AREA_ID));
			String cityTW = cursor.getString(cursor.getColumnIndex(AppConstants.KEY_CITY_TW));
			if (cityTW == null) {
				updateDatabaseForCSV();
			}
		} catch (Exception e) {
			Log.e(ALog.DATABASE, "CityDetails database not yet filled " + e);
			return false;
		}
		addOldDataToUserTable();
		return true;
	}
	
	private void addOldDataToUserTable() {
		if (isUserSelectedTableEmpty()) {
			try {
				String selction = AppConstants.KEY_SHORTLIST + " = '1'";
				Cursor cursor = getDataFromOutdoorLoacation(selction);
				if (cursor != null && cursor.getCount() > 0) {
					do {
						String areaId = cursor.getString(cursor.getColumnIndex(AppConstants.KEY_AREA_ID));
						insertCityProviderDataIntoTable(mOutdoorLocationDatabase, areaId, OutdoorDataProvider.CMA.ordinal());
					} while(cursor.moveToNext());
				}
			} catch (SQLiteException e) {
				e.printStackTrace();
			} 
		}
	}
	
	private boolean isUserSelectedTableEmpty() {
		if (mOutdoorLocationDatabase == null) return true;//Due to synchronized DB open null
		try {
			Cursor cursor = mOutdoorLocationDatabase.query(
					AppConstants.TABLE_USER_SELECTED_CITY, null,null, null, null, null, null);
			if (cursor == null || cursor.getCount() < 1) return true;
			cursor.moveToFirst();
		} catch (Exception e) {
			e.printStackTrace();
			return true;
		}
		return false;
	}
	
	/**
	 * Update city table with traditional Chinese city name, if city name is null
	 */
	public synchronized void updateDatabaseForCSV() {
		InputStream inputStream = PurAirApplication.getAppContext().getResources().openRawResource(R.raw.city_list_hk);
		BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, Charset.defaultCharset()));

		String outdoorLocation = "";
		StringTokenizer stringTokenizer = null;
		
		if (mOutdoorLocationDatabase == null) return;//Due to synchronized DB open null

		mOutdoorLocationDatabase.beginTransaction();
		try {
			while ((outdoorLocation = reader.readLine()) != null) {
				stringTokenizer = new StringTokenizer(outdoorLocation, ";");

				ContentValues values = new ContentValues();
				values.put(AppConstants.KEY_CITY, stringTokenizer.nextToken());

				String areaID = stringTokenizer.nextToken();
				values.put(AppConstants.KEY_AREA_ID, areaID);
				values.put(AppConstants.KEY_LONGITUDE, stringTokenizer.nextToken());
				values.put(AppConstants.KEY_LATITUDE, stringTokenizer.nextToken());
				values.put(AppConstants.KEY_CITY_CN, stringTokenizer.nextToken());
				values.put(AppConstants.KEY_CITY_TW, stringTokenizer.nextToken());
				mOutdoorLocationDatabase.update(AppConstants.TABLE_CITYDETAILS,
						values, AppConstants.KEY_AREA_ID + "= ?", new String[] {areaID});
			}
			mOutdoorLocationDatabase.setTransactionSuccessful();
		} catch (IOException e) {
            e.printStackTrace();
        } catch (SQLiteConstraintException e) {
            e.printStackTrace();
        } catch (SQLiteException e) {
            e.printStackTrace();
        }
		finally {
			try {
				inputStream.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			mOutdoorLocationDatabase.endTransaction();
		}
	}
}
