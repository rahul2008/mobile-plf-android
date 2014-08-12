package com.philips.cl.di.dev.pa.outdoorlocations;

import android.database.Cursor;

public interface OutdoorCityListener {
	void onCityLoad(Cursor cursor);
}
