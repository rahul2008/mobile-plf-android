package com.philips.cl.di.dev.pa.outdoorlocations;

import com.philips.cl.di.dev.pa.constant.AppConstants;
import com.philips.cl.di.dev.pa.util.ALog;

import android.annotation.SuppressLint;
import android.database.Cursor;
import android.database.sqlite.SQLiteException;
import android.os.Handler;
import android.os.Message;

public class OutdoorLocationHandler {
	
	private static OutdoorLocationHandler mInstance;
	private OutdoorCityListener cityListener;
	private OutdoorSelectedCityListener selectedCityListener;
	
	private OutdoorLocationHandler() {
		//TODO
	}
	
	public synchronized static OutdoorLocationHandler getInstance() {
		if (mInstance == null) {
			mInstance = new OutdoorLocationHandler();
			readFromFileAddToDatabase();
		}
		return mInstance;
	}
	
	public void setCityListener(OutdoorCityListener cityListener) {
		this.cityListener = cityListener;
	}
	
	public void removeCityListener() {
		cityListener = null;
	}
	
	public void setSelectedCityListener(OutdoorSelectedCityListener selectedCityListener) {
		this.selectedCityListener = selectedCityListener;
	}
	
	public void removeSelectedCityListener() {
		selectedCityListener = null;
	}
	
	@SuppressLint("HandlerLeak")
	private static Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			if (msg.what == 0) {
				ALog.i(ALog.OUTDOOR_LOCATION, "Data loaded successfully");
			} else if (msg.what == 1) {
				//TODO failed
			} 
		};
	};
	
	public void fetchSelectedCity() {
		
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				OutdoorLocationDatabase database =  new OutdoorLocationDatabase();
				
				try {
					database.open();
					Cursor cursor = database.getDataFromOutdoorLoacation(
							AppConstants.SQL_SELECTION_GET_SHORTLIST_ITEMS);
					database.close();
					if (selectedCityListener != null) {
						selectedCityListener.onSelectedCityLoad(cursor);
					}
				} catch (SQLiteException e) {
					ALog.e(ALog.OUTDOOR_LOCATION, 
							"OutdoorLocationAbstractGetAsyncTask failed to retive data from DB: " + e.getMessage());
				}
			}
		}).start();
	}
	
	public void updateSelectedCity(final String areaId, final boolean selected) {
		
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				OutdoorLocationDatabase database =  new OutdoorLocationDatabase();

				database.open();
				database.updateOutdoorLocationShortListItem(areaId, selected);
				database.close();
				handler.sendEmptyMessage(0);//TODO change
			}
		}).start();
	}
	
	public void fetchCities(final String selection) {
		
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				OutdoorLocationDatabase database =  new OutdoorLocationDatabase();
				
				try {
					database.open();
					Cursor cursor = database.getDataFromOutdoorLoacation(selection);
					database.close();
					if (cityListener != null) {
						cityListener.onCityLoad(cursor);
					}
				} catch (SQLiteException e) {
					ALog.e(ALog.OUTDOOR_LOCATION, 
							"OutdoorLocationAbstractGetAsyncTask failed to retive data from DB: " + e.getMessage());
				}
			}
		}).start();
	}
	
	private static void readFromFileAddToDatabase() {
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				OutdoorLocationDatabase database =  new OutdoorLocationDatabase();
				
				try {
					database.open();
					database.fillDatabaseForCSV();
					database.close();
					handler.sendEmptyMessage(0);
				} catch (SQLiteException e) {
					ALog.e(ALog.OUTDOOR_LOCATION, 
							"OutdoorLocationAbstractFillAsyncTask failed to retive data from DB: " + e.getMessage());
					handler.sendEmptyMessage(1);
				}
			}
		}).start();
	}
	
	public static void reset() {
		mInstance = null;
	}
}
