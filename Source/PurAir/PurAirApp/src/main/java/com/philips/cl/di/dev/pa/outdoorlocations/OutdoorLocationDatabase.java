package com.philips.cl.di.dev.pa.outdoorlocations;

import java.util.List;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.util.Log;

import com.philips.cl.di.dev.pa.PurAirApplication;
import com.philips.cl.di.dev.pa.constant.AppConstants;
import com.philips.cl.di.dev.pa.dashboard.OutdoorCityInfo;
import com.philips.cl.di.dev.pa.dashboard.OutdoorManager;
import com.philips.cl.di.dev.pa.purifier.PurifierDatabaseHelper;
import com.philips.cl.di.dev.pa.util.LocationUtils;

public class OutdoorLocationDatabase {

	private static final String TAG = OutdoorLocationDatabase.class.getSimpleName();

	private PurifierDatabaseHelper mDatabaseHelper = null;
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
		mDatabaseHelper = new PurifierDatabaseHelper(PurAirApplication.getAppContext());
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

	synchronized void readDataFromDatabase() {
		if (!isOldDataAddedIntoNewTable() && !LocationUtils.getDefaultOldCityAddedState()) {
			//Default city Beijing
			String areaId = "101010100";
			long rowId = new UserCitiesDatabase().insertCity(areaId, OutdoorDataProvider.CMA.ordinal());
			if (rowId != -1) OutdoorManager.getInstance().addAreaIDToUsersList(areaId);
			LocationUtils.saveDefaultOldCityAddedState(true);
		} else {
			List<String> userCities = new UserCitiesDatabase().getAllCities();
			if (!userCities.isEmpty()) {

				List<OutdoorCityInfo> outdoorCityInfoList = AddOutdoorLocationHelper.getSortedUserSelectedCitiesInfo(userCities) ;

				for (OutdoorCityInfo outdoorCityInfo : outdoorCityInfoList) {
					String key = AddOutdoorLocationHelper.getCityKeyWithRespectDataProvider(outdoorCityInfo);
					OutdoorManager.getInstance().addAreaIDToUsersList(key);
				}
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

	public synchronized void dropTable(String tableName) {
		if (mOutdoorLocationDatabase == null) return; //Due to synchronized DB open null
		try {
			mOutdoorLocationDatabase.execSQL("DROP TABLE IF EXISTS " + tableName);

		} catch (SQLiteConstraintException e) {
			e.printStackTrace();
		} catch (SQLiteException e) {
			e.printStackTrace();
		}
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

	public boolean isOldDataAddedIntoNewTable() {
		if (!isCityTableExists())  return false;

		try {

			String selction = AppConstants.KEY_SHORTLIST + " = '1'";
			Cursor cursor = getDataFromOutdoorLoacation(selction);
			if (cursor != null && cursor.getCount() > 0) {
				do {
					String areaId = cursor.getString(cursor.getColumnIndex(AppConstants.KEY_AREA_ID));
					long rowId = new UserCitiesDatabase().insertCity(areaId, OutdoorDataProvider.CMA.ordinal());
					if (rowId != -1) OutdoorManager.getInstance().addAreaIDToUsersList(areaId);
				} while(cursor.moveToNext());
			}
			dropTable(AppConstants.TABLE_CITYDETAILS);
			LocationUtils.saveDefaultOldCityAddedState(true);
		} catch (SQLiteException e) {
			e.printStackTrace();
			return false;
		} 
		return true;
	}

	private boolean isCityTableExists() {

		if (mOutdoorLocationDatabase == null) return false; //Due to synchronized DB open null
		try {
			Cursor cursor = mOutdoorLocationDatabase.query(AppConstants.TABLE_CITYDETAILS, null, 
					null, null, null, null, null);
			if (cursor != null) {
				cursor.moveToFirst();
			}
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		} 
		return true;

	}
}
