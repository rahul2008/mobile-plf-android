package com.philips.cl.di.dev.pa.outdoorlocations;

import com.philips.cl.di.dev.pa.util.ALog;

import android.database.sqlite.SQLiteException;
import android.os.AsyncTask;

public class OutdoorLocationAbstractFillAsyncTask extends AsyncTask<String, Void, Void> {
	
	@Override
	protected Void doInBackground(String... params) {
		OutdoorLocationDatabase database =  new OutdoorLocationDatabase();
		
		try {
			database.open();
			
			database.fillDatabaseForCSV();
			
			database.close();
		} catch (SQLiteException e) {
			ALog.e(ALog.OUTDOOR_LOCATION, 
					"OutdoorLocationAbstractFillAsyncTask failed to retive data from DB: " + e.getMessage());
		}
		return null;
	}

	@Override
	protected void onPostExecute(Void result) {};

}
