package com.philips.cl.di.dev.pa.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.philips.cl.di.dev.pa.util.ALog;

/**
 * The Class BaseActivity. This class contains all the base / common
 * functionalities.
 */
@SuppressLint("Registered")
public class BaseActivity extends ActionBarActivity {

	private int googlePlayServiceStatus;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		ALog.d(ALog.ACTIVITY, "OnCreate on " + this.getClass().getSimpleName());
		super.onCreate(savedInstanceState);
		googlePlayServiceStatus = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
	}

	@Override
	protected void onStart() {
		ALog.d(ALog.ACTIVITY, "OnStart on " + this.getClass().getSimpleName());
		super.onStart();
	}

	@Override
	protected void onResume() {
		ALog.d(ALog.ACTIVITY, "OnResume on " + this.getClass().getSimpleName());
		super.onResume();
	}

	@Override
	protected void onPause() {
		ALog.d(ALog.ACTIVITY, "OnPause on " + this.getClass().getSimpleName());
		super.onPause();
	}

	@Override
	protected void onStop() {
		ALog.d(ALog.ACTIVITY, "OnStop on " + this.getClass().getSimpleName());
		super.onStop();
	}

	@Override
	protected void onDestroy() {
		ALog.d(ALog.ACTIVITY, "OnDestoy on " + this.getClass().getSimpleName());
		super.onDestroy();
	}
	
	public boolean isGooglePlayServiceAvailable() {
		if (ConnectionResult.SUCCESS == googlePlayServiceStatus) {
			return true;
		}
		return false;
	}

}
