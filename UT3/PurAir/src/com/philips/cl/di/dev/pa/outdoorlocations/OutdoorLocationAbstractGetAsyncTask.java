package com.philips.cl.di.dev.pa.outdoorlocations;

import android.database.Cursor;
import android.os.AsyncTask;

public abstract class OutdoorLocationAbstractGetAsyncTask extends AsyncTask<String, Void, Cursor> {
	
	@Override
	protected Cursor doInBackground(String... params) {
		OutdoorLocationDatabase database =  new OutdoorLocationDatabase();
		Cursor cursor = null;
		
		database.open();
		
		if (params != null) {
			cursor = database.getDataFromOutdoorLoacation(params[0]);
		}
		
		database.close();
		return cursor;
	}

	@Override
	protected abstract void onPostExecute(Cursor result);

}
