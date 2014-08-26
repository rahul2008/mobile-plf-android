package com.philips.cl.di.dev.pa.outdoorlocations;

import android.annotation.SuppressLint;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.Handler;
import android.os.Message;

import com.philips.cl.di.dev.pa.PurAirApplication;
import com.philips.cl.di.dev.pa.constant.AppConstants;
import com.philips.cl.di.dev.pa.dashboard.OutdoorCityInfo;
import com.philips.cl.di.dev.pa.dashboard.OutdoorManager;
import com.philips.cl.di.dev.pa.util.ALog;
import com.philips.cl.di.dev.pa.util.DatabaseHelper;

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
							AppConstants.SQL_SELECTION_GET_SHORTLIST_ITEMS_EXCEPT_CURR_LOC);
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
	
	private DatabaseHelper mDatabaseHelper = null;
	private SQLiteDatabase mOutdoorLocationDatabase = null;
	private static final String[] mTableColumns = new String[] {
			AppConstants.KEY_ID, AppConstants.KEY_CITY,
			AppConstants.KEY_AREA_ID, AppConstants.KEY_LONGITUDE,
			AppConstants.KEY_LATITUDE, AppConstants.KEY_CITY_CN,
			AppConstants.KEY_SHORTLIST, AppConstants.KEY_CITY_TW };
	
	
	public interface DashBoardDataFetchListener{
		void isCompleted(boolean isCompleted);
	}
	
	private static DashBoardDataFetchListener dashBoardDataFetchListener = null;
	
	public static void setDashBoardDataFetch(DashBoardDataFetchListener dashBoardListener){
		dashBoardDataFetchListener = dashBoardListener;
	}
	
	public synchronized void fetchAllCityList() {
		
		new Thread(new Runnable() {
			
			@Override
			public void run() {

				mDatabaseHelper = new DatabaseHelper(PurAirApplication.getAppContext());
				open();
				String filterText = null;
				Cursor cursor = null;
				cursor = mOutdoorLocationDatabase.query(true,
						AppConstants.TABLE_CITYDETAILS, mTableColumns, filterText,
						null, null, null, AppConstants.KEY_CITY + " ASC", null);

				if(dashBoardDataFetchListener != null){
					dashBoardDataFetchListener.isCompleted(true);
				}
				
				fillAllCitiesListFromDatabase(cursor);

				close();
			}
		}).start();
	}
	
	public synchronized void open() {
		if (mOutdoorLocationDatabase == null
				|| !mOutdoorLocationDatabase.isOpen()) {
			mOutdoorLocationDatabase = mDatabaseHelper.getWritableDatabase();
		}
	}

	public synchronized void close() {
		if (mOutdoorLocationDatabase.isOpen()) {
			mOutdoorLocationDatabase.close();
		}
	}

	private synchronized void fillAllCitiesListFromDatabase(Cursor cursor) {

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
//			mCitiesListAll = OutdoorManager.getInstance().getAllCitiesList();
		}
	}
}
