package com.philips.cl.di.dev.pa.util;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import com.philips.cl.di.dev.pa.outdoorlocations.OutdoorLocationHandler;

public class DataBaseOperationService extends Service{

	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public void onCreate() {
		super.onCreate();
		ALog.i("testing", "Service Started DataBaseOperationService context : " + this);
//		new Thread(new Runnable() {
//
//			@Override
//			public void run() {
//				ALog.i("testing", "Thread Started (inside) DataBaseOperationService");
				OutdoorLocationHandler.getInstance().fetchAllCityList();
//			}
//		}).start();
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		ALog.i("testing","Service Stopped DataBaseOperationService");
	}
}
