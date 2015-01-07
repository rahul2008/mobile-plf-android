package com.philips.cl.di.dev.pa.outdoorlocations;

import android.annotation.SuppressLint;
import android.database.Cursor;
import android.database.sqlite.SQLiteException;
import android.os.Handler;
import android.os.Message;

import com.philips.cl.di.dev.pa.constant.AppConstants;
import com.philips.cl.di.dev.pa.util.ALog;

public class OutdoorLocationHandler {

	private static OutdoorLocationHandler mInstance;
	private OutdoorSelectedCityListener selectedCityListener;

	private OutdoorLocationHandler() {
		// Force singleton
	}

	public synchronized static OutdoorLocationHandler getInstance() {
		if (mInstance == null) {
			mInstance = new OutdoorLocationHandler();
//			readFromFileAddToDatabase();
		}
		return mInstance;
	}

	public void setSelectedCityListener(
			OutdoorSelectedCityListener selectedCityListener) {
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
				// TODO failed
			}
		};
	};

	public void fetchSelectedCity() {

		new Thread(new Runnable() {

			@Override
			public void run() {
				OutdoorLocationDatabase database = new OutdoorLocationDatabase();
				try {
					database.open();
					String selction = AppConstants.KEY_SHORTLIST + " = '1'";
					Cursor cursor = database.getDataFromOutdoorLoacation(selction);
					database.close();
					if (selectedCityListener != null) {
						selectedCityListener.onSelectedCityLoad(cursor);
					}
				} catch (SQLiteException e) {
					ALog.e(ALog.OUTDOOR_LOCATION,
							"OutdoorLocationAbstractGetAsyncTask failed to retive data from DB: "+ e.getMessage());
				} finally {
					handler.sendEmptyMessage(0);// TODO change
				}
			}
		}).start();
	}

	public void updateSelectedCity(final String areaId, final boolean selected) {

		new Thread(new Runnable() {

			@Override
			public void run() {
				try {
					OutdoorLocationDatabase database = new OutdoorLocationDatabase();

					database.open();

					database.updateOutdoorLocationShortListItem(areaId,	selected);
					database.close();
				} catch (SQLiteException e) {
					ALog.e(ALog.OUTDOOR_LOCATION,
							"OutdoorLocationAbstractGetAsyncTask failed to retive data from DB: "
									+ "Error: " + e.getMessage());
				} finally {
					handler.sendEmptyMessage(0);// TODO change
				}
			}
		}).start();
	}

	/*private static void readFromFileAddToDatabase() {
		new Thread(new Runnable() {

			@Override
			public void run() {
				OutdoorLocationDatabase database = new OutdoorLocationDatabase();

				try {
					database.open();
					database.readDataFromDatabase();
					database.close();

				} catch (SQLiteException e) {
					ALog.e(ALog.OUTDOOR_LOCATION,
							"OutdoorLocationAbstractFillAsyncTask failed to retive data from DB: "
									+ "Error: " + e.getMessage());
				} finally {
					handler.sendEmptyMessage(0);// TODO change
				}
			}
		}).start();
	}*/

	public static void reset() {
		mInstance = null;
	}
}
