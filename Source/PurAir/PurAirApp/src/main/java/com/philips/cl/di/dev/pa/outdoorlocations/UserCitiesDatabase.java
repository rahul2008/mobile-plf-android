package com.philips.cl.di.dev.pa.outdoorlocations;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.philips.cl.di.dev.pa.PurAirApplication;
import com.philips.cl.di.dev.pa.constant.AppConstants;
import com.philips.cl.di.dev.pa.purifier.PurifierDatabaseHelper;
import com.philips.cl.di.dev.pa.util.ALog;

public class UserCitiesDatabase {

	private SQLiteDatabase db;
	private PurifierDatabaseHelper dbHelper;
	
	public UserCitiesDatabase() {
		dbHelper = new PurifierDatabaseHelper(PurAirApplication.getAppContext());
	}
	
	public long insertCity(String areaID, int dataProvider) {
		ALog.i(ALog.DATABASE, "UserCitiesDatabase$insertCity areaId = " + areaID);
		long rowId = -1L;
		rowId = getRowIdOfCity(areaID);
		ALog.i(ALog.DATABASE, "UserCitiesDatabase$insertCity areaId alread exists. Return");
		if(rowId != -1) return rowId;
		
		try {
			db = dbHelper.getWritableDatabase();
			ContentValues values = new ContentValues();
			values.put(AppConstants.KEY_AREA_ID, areaID);
			values.put(AppConstants.KEY_DATA_PROVIDER, dataProvider);
			
			rowId = db.insert(AppConstants.TABLE_USER_SELECTED_CITY, null, values);
		} catch (Exception e) {
			ALog.e(ALog.DATABASE, "UserCitiesDatabase insertCity Exception");
		} finally {
			closeDb();
		}
		ALog.i(ALog.DATABASE, "UserCitiesDatabase$insertCity insert success " + rowId);
		return rowId;
	}
	
	public List<String> getAllCities() {
		List<String> userCities = new ArrayList<String>();
		Cursor cursor = null;
		
		try {
			db = dbHelper.getReadableDatabase();
			cursor = db.query(AppConstants.TABLE_USER_SELECTED_CITY, new String[]{AppConstants.KEY_AREA_ID}, null, null, null, null, null);
			
			if(cursor != null && cursor.getCount() > 0) {
				cursor.moveToFirst();
				do {
					String areaId = cursor.getString(cursor.getColumnIndex(AppConstants.KEY_AREA_ID));
					
					userCities.add(areaId);
				} while (cursor.moveToNext());
			}
		} catch (Exception e) {
			ALog.e(ALog.DATABASE, "UserCitiesDatabase getAllCities Exception");
		} finally {
			closeCursor(cursor);
			closeDb();
		}
		return userCities;
	}

	public long getRowIdOfCity(String areaID) {
		Cursor cursor = null;
		try {
			db = dbHelper.getReadableDatabase();
			cursor = db.query(AppConstants.TABLE_USER_SELECTED_CITY, 
					new String[]{AppConstants.KEY_ID, AppConstants.KEY_AREA_ID},
					AppConstants.KEY_AREA_ID + "= ?", new String[]{areaID}, null, null, null);
			if(cursor != null && cursor.getCount() > 0) {
				cursor.moveToFirst();
				return cursor.getLong(cursor.getColumnIndex(AppConstants.KEY_ID));
			}
		} catch (Exception e) {
			ALog.e(ALog.DATABASE, "UserCitiesDatabase getRowIdOfCity Exception");
		} finally {
			closeCursor(cursor);
			closeDb();
		}
		return -1;
	}
	
	public int deleteCity(String areaId) {
		int effectedRowId = -1;
		if (areaId == null) return effectedRowId;
		try {
			db = dbHelper.getWritableDatabase();

			effectedRowId = db.delete(AppConstants.TABLE_USER_SELECTED_CITY, 
					AppConstants.KEY_AREA_ID + "= ?", new String[]{areaId});
		} catch (Exception e) {
			ALog.e(ALog.DATABASE, "Failed to delete row "+"Error: " + e.getMessage());
		} finally {
			closeDb();
		}
		return effectedRowId;
	}
	
	private void closeDb() {
		try {
			if (db != null && db.isOpen()) {
				db.close();
			}
		} catch (Exception e) {
			ALog.e(ALog.DATABASE, "Error: " + e.getMessage());
		}
	}
	
	private void closeCursor(Cursor c) {
		try {
			if (c != null && !c.isClosed() ) {
				c.close();
			}
		} catch (Exception e) {
			ALog.e(ALog.DATABASE, "Error: " + e.getMessage());
		}

	}
}
