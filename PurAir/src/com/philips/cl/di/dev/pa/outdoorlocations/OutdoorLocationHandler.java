package com.philips.cl.di.dev.pa.outdoorlocations;

import android.annotation.SuppressLint;
import android.database.Cursor;
import android.database.sqlite.SQLiteException;
import android.os.Handler;
import android.os.Message;

import com.philips.cl.di.dev.pa.constant.AppConstants;
import com.philips.cl.di.dev.pa.dashboard.OutdoorCityInfo;
import com.philips.cl.di.dev.pa.dashboard.OutdoorManager;
import com.philips.cl.di.dev.pa.util.ALog;
import com.philips.cl.di.dev.pa.util.LocationUtils;

public class OutdoorLocationHandler {

	private static OutdoorLocationHandler mInstance;
	private OutdoorCityListener cityListener;
	private OutdoorSelectedCityListener selectedCityListener;

	private OutdoorLocationHandler() {
		// Force singleton
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
					String selction = AppConstants.KEY_SHORTLIST + " = '1' and " +
							AppConstants.KEY_AREA_ID + " != '"+LocationUtils.getCurrentLocationAreaId()+"' ";
					Cursor cursor = database.getDataFromOutdoorLoacation(selction);
					database.close();
					if (selectedCityListener != null) {
						selectedCityListener.onSelectedCityLoad(cursor);
					}
				} catch (SQLiteException e) {
					ALog.e(ALog.OUTDOOR_LOCATION,
							"OutdoorLocationAbstractGetAsyncTask failed to retive data from DB: "
									+ e.getMessage());
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
									+ e.getMessage());
				} finally {
					handler.sendEmptyMessage(0);// TODO change
				}
			}
		}).start();
	}

	public void fetchCities(final String selection) {

		new Thread(new Runnable() {

			@Override
			public void run() {
				OutdoorLocationDatabase database = new OutdoorLocationDatabase();

				try {
					database.open();
					Cursor cursor = database.getDataFromOutdoorLoacation(selection);
					database.close();
					if (cityListener != null) {
						cityListener.onCityLoad(cursor);
					}
				} catch (SQLiteException e) {
					ALog.e(ALog.OUTDOOR_LOCATION,
							"OutdoorLocationAbstractGetAsyncTask failed to retive data from DB: "
									+ e.getMessage());
				} finally {
					handler.sendEmptyMessage(0);// TODO change
				}
			}
		}).start();
	}

	private static void readFromFileAddToDatabase() {
		new Thread(new Runnable() {

			@Override
			public void run() {
				OutdoorLocationDatabase database = new OutdoorLocationDatabase();

				try {
					database.open();
					database.fillDatabaseForCSV();
					fetchAllCityList(database) ;
					database.close();

				} catch (SQLiteException e) {
					ALog.e(ALog.OUTDOOR_LOCATION,
							"OutdoorLocationAbstractFillAsyncTask failed to retive data from DB: "
									+ e.getMessage());
				} finally {
					handler.sendEmptyMessage(0);// TODO change
				}
			}
		}).start();
	}

	public static void reset() {
		mInstance = null;
	}

	public static synchronized void fetchAllCityList(final OutdoorLocationDatabase database) {
		if(OutdoorManager.getInstance().getAllCitiesList() != null 
				&& OutdoorManager.getInstance().getAllCitiesList().size() <= 0){
			Cursor cursor = database.getDataFromOutdoorLoacation(null);
			fillAllCitiesListFromDatabase(cursor);
		}

	}

	private static synchronized void fillAllCitiesListFromDatabase(Cursor cursor) {

		if (cursor != null && cursor.getCount() > 0) {
			cursor.moveToFirst();
			do {
				String city = cursor.getString(cursor
						.getColumnIndex(AppConstants.KEY_CITY));
				String cityCN = cursor.getString(cursor
						.getColumnIndex(AppConstants.KEY_CITY_CN));
				String cityTW = cursor.getString(cursor
						.getColumnIndex(AppConstants.KEY_CITY_TW));
				String areaID = cursor.getString(cursor
						.getColumnIndex(AppConstants.KEY_AREA_ID));
				float longitude = cursor.getFloat(cursor
						.getColumnIndex(AppConstants.KEY_LONGITUDE));
				float latitude = cursor.getFloat(cursor
						.getColumnIndex(AppConstants.KEY_LATITUDE));

				ALog.i(ALog.OUTDOOR_LOCATION,
						"Add cities from DB to outdoor dashboard city " + city
						+ " areaID " + areaID);
				OutdoorCityInfo info = new OutdoorCityInfo(city, cityCN,
						cityTW, longitude, latitude, areaID);
				OutdoorManager.getInstance().addAreaIdToAllCitiesList(areaID);
				OutdoorManager.getInstance().addAllCityDataToMap(info, null,
						null, areaID);
			} while (cursor.moveToNext());
			// mCitiesListAll = OutdoorManager.getInstance().getAllCitiesList();
		}
	}
}
