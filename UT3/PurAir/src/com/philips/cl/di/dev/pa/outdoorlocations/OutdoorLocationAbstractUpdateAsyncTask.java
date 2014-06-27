package com.philips.cl.di.dev.pa.outdoorlocations;

import android.os.AsyncTask;

public abstract class OutdoorLocationAbstractUpdateAsyncTask extends AsyncTask<String, Void, Void> {
	
	@Override
	protected Void doInBackground(String... params) {
		OutdoorLocationDatabase database =  new OutdoorLocationDatabase();
		
		database.open();
		
		if (params != null) {
			if (params[1] == "true") {
				database.updateOutdoorLocationShortListItem(params[0], true);
			} else if(params[1] == "false") {
				database.updateOutdoorLocationShortListItem(params[0], false);
			}
		} 
		
		database.close();
		return null;
	}

	@Override
	protected abstract void onPostExecute(Void result);

}
