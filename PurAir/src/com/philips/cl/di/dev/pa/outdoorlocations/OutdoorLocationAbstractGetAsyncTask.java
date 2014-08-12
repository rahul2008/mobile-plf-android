package com.philips.cl.di.dev.pa.outdoorlocations;

import com.philips.cl.di.dev.pa.util.ALog;

import android.database.Cursor;
import android.database.sqlite.SQLiteException;
import android.os.AsyncTask;

public abstract class OutdoorLocationAbstractGetAsyncTask extends AsyncTask<String, Void, Cursor> {
	
	@Override
	protected Cursor doInBackground(String... params) {
		OutdoorLocationDatabase database =  new OutdoorLocationDatabase();
		Cursor cursor = null;
		
		try {
			database.open();
			ALog.i(ALog.OUTDOOR_LOCATION, 
					"OutdoorLocationAbstractGetAsyncTask before getting data from DB" );
			if (params != null) {
				cursor = database.getDataFromOutdoorLoacation(params[0]);
			}
			ALog.i(ALog.OUTDOOR_LOCATION, 
					"OutdoorLocationAbstractGetAsyncTask after getting data from DB" );
			database.close();
		} catch (SQLiteException e) {
			ALog.e(ALog.OUTDOOR_LOCATION, 
					"OutdoorLocationAbstractGetAsyncTask failed to retive data from DB: " + e.getMessage());
		}
		return cursor;
	}

	@Override
	protected abstract void onPostExecute(Cursor result);

}
