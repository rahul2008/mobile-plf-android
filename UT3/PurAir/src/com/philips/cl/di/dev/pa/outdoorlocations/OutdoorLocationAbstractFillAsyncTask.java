package com.philips.cl.di.dev.pa.outdoorlocations;

import android.os.AsyncTask;

public class OutdoorLocationAbstractFillAsyncTask extends AsyncTask<String, Void, Void> {
	
	@Override
	protected Void doInBackground(String... params) {
		OutdoorLocationDatabase database =  new OutdoorLocationDatabase();
		
		database.open();
		
		database.fillDatabaseForCSV();
		
		database.close();
		return null;
	}

	@Override
	protected void onPostExecute(Void result) {};

}
