package com.philips.cl.di.dev.pa.purifier;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.philips.cl.di.dev.pa.PurAirApplication;
import com.philips.cl.di.dev.pa.constant.AppConstants;
import com.philips.cl.di.dev.pa.database.ApplianceDatabase;
import com.philips.cl.di.dev.pa.newpurifier.AirPurifier;
import com.philips.cl.di.dev.pa.util.ALog;

public class PurifierDatabase implements ApplianceDatabase<AirPurifier> {

	private PurifierDatabaseHelper dbHelper;
	/**
	 *
	 * @param context
	 */
	public PurifierDatabase() {
		dbHelper = new PurifierDatabaseHelper(PurAirApplication.getAppContext());
	}

	@Override
	public long save(AirPurifier purifier) {
		long rowId = -1L;
		
		if (purifier == null)
			return rowId;
		
		SQLiteDatabase db = null;
		try {
			db = dbHelper.getWritableDatabase();
			
			ContentValues values = new ContentValues();
			values.put(AppConstants.KEY_AIRPUR_CPP_ID, purifier.getNetworkNode().getCppId());
			values.put(AppConstants.KEY_LATITUDE, purifier.getLatitude());
			values.put(AppConstants.KEY_LONGITUDE, purifier.getLongitude());

			rowId = db.insertWithOnConflict(AppConstants.TABLE_AIRPUR_DEVICE, null, values, SQLiteDatabase.CONFLICT_REPLACE);
			ALog.d(ALog.DATABASE, "Saved Purifier in db: " + purifier);
		} catch (Exception e) {
			ALog.e(ALog.DATABASE, "Failed to update row " +"Error: " + e.getMessage());
		} finally {
			closeDb(db);
		}
		
		return rowId;
	}

	@Override
	public void loadDataForAppliance(AirPurifier purifier) {
		String cppId = purifier.getNetworkNode().getCppId();
		
		Cursor cursor = null;
		
		SQLiteDatabase db = null;
		try {
			db = dbHelper.getReadableDatabase();
			cursor = db.query(AppConstants.TABLE_AIRPUR_DEVICE, null, AppConstants.KEY_AIRPUR_CPP_ID + "= ?", 
					new String[] { cppId }, null, null, null);
			
			if (cursor != null && cursor.getCount() > 0) {
				cursor.moveToNext();
				
				String latitude = cursor.getString(cursor.getColumnIndex(AppConstants.KEY_LATITUDE));
				String longitude = cursor.getString(cursor.getColumnIndex(AppConstants.KEY_LONGITUDE));
				
				purifier.setLatitude(latitude);
				purifier.setLongitude(longitude);
			}
			ALog.d(ALog.DATABASE, "Loaded Purifier from db: " + purifier);
		} catch (Exception e) {
			ALog.e(ALog.DATABASE, "Error: " + e.getMessage());
		} finally {
			closeCursor(cursor);
			closeDb(db);
		}
	}
	
	@Override
	public int delete(AirPurifier purifier) {
		SQLiteDatabase db = null;
		int rowsDeleted = 0;
		try {
			db = dbHelper.getReadableDatabase();

			rowsDeleted = db.delete(AppConstants.TABLE_AIRPUR_DEVICE, AppConstants.KEY_AIRPUR_CPP_ID + "= ?", new String[] { purifier.getNetworkNode().getCppId() });
			ALog.d(ALog.DATABASE, "Deleted Purifier from db: " + purifier + "  ("+rowsDeleted+")");
		} catch (Exception e) {
			ALog.e(ALog.DATABASE, "Error: " + e.getMessage());
		} finally {
			closeDb(db);
		}
		
		return rowsDeleted;
	}
	
	private void closeDb(SQLiteDatabase db) {
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
