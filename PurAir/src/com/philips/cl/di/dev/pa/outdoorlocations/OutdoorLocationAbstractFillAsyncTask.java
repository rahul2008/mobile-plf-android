package com.philips.cl.di.dev.pa.outdoorlocations;

import android.database.sqlite.SQLiteException;
import android.os.AsyncTask;

import com.philips.cl.di.dev.pa.util.ALog;

public class OutdoorLocationAbstractFillAsyncTask extends AsyncTask<String, Void, Void> {
	
	@Override
	protected Void doInBackground(String... params) {
		OutdoorLocationDatabase database =  new OutdoorLocationDatabase();
		
		try {
			database.open();
			ALog.i(ALog.OUTDOOR_LOCATION, 
					"OutdoorLocationAbstractFillAsyncTask before filling data from DB" );
			database.fillDatabaseForCSV();
			ALog.i(ALog.OUTDOOR_LOCATION, 
					"OutdoorLocationAbstractFillAsyncTask after filling data from DB" );
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
