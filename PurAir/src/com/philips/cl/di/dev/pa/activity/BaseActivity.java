package com.philips.cl.di.dev.pa.activity;

import net.hockeyapp.android.Tracking;
import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;

import com.philips.cl.di.dev.pa.util.ALog;
import com.philips.cl.di.dev.pa.util.networkutils.NetworkReceiver;

/**
 * The Class BaseActivity. This class contains all the base / common
 * functionalities.
 */
@SuppressLint("Registered")
public class BaseActivity extends ActionBarActivity {

	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		ALog.d(ALog.ACTIVITY, "OnCreate on " + this.getClass().getSimpleName());
		super.onCreate(savedInstanceState);
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
		Tracking.startUsage(this);
		NetworkReceiver.getInstance().registerNetworkReceiver();
	}

	@Override
	protected void onPause() {
		ALog.d(ALog.ACTIVITY, "OnPause on " + this.getClass().getSimpleName());
		Tracking.stopUsage(this);
		NetworkReceiver.getInstance().unregisterNetworkReceiver();
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
	
}
