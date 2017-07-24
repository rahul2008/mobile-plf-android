package com.philips.cl.di.dev.pa.activity;

import net.hockeyapp.android.Tracking;
import android.annotation.SuppressLint;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.philips.cl.di.dev.pa.cpp.AppUpdater;
import com.philips.cl.di.dev.pa.cpp.AppUpdater.ShowAppUpdateDialogListener;
import com.philips.cl.di.dev.pa.fragment.AppUpdateDialogFragment;
import com.philips.cl.di.dev.pa.util.ALog;
import com.philips.cl.di.dev.pa.util.MetricsTracker;
import com.philips.cl.di.dev.pa.util.networkutils.NetworkReceiver;

/**
 * The Class BaseActivity. This class contains all the base / common
 * functionalities.
 */
@SuppressLint("Registered")
public class BaseActivity extends FragmentActivity implements ShowAppUpdateDialogListener {
	
	protected ConnectivityManager connectivityManager;
	protected WifiManager wifiManager;
	private AppUpdater mAppUpdater;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		ALog.d(ALog.ACTIVITY, "OnCreate on " + this.getClass().getSimpleName());
		
		connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE) ;
		wifiManager = (WifiManager) getSystemService(Context.WIFI_SERVICE) ;
		mAppUpdater = AppUpdater.getInstance(getApplicationContext());
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
		MetricsTracker.startCollectLifecycleData(this);
		NetworkReceiver.getInstance().registerNetworkReceiver();
		mAppUpdater.registerShowAppUpdateDialogListener(this);
	}

	@Override
	protected void onPause() {
		ALog.d(ALog.ACTIVITY, "OnPause on " + this.getClass().getSimpleName());
		Tracking.stopUsage(this);
		NetworkReceiver.getInstance().unregisterNetworkReceiver();
		super.onPause();
		MetricsTracker.stopCollectLifecycleData();
		mAppUpdater.unregisterShowAppUpdateDialogListener();
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

	@Override
	public void showAppUpdateDialog() {
		try {
			FragmentManager fragmentManager = getSupportFragmentManager();
			FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
			Fragment previousDialog= fragmentManager.findFragmentByTag("AppUpdate");
			if(previousDialog != null){
				fragmentTransaction.remove(previousDialog);

			}
			fragmentTransaction.add(new AppUpdateDialogFragment(), "AppUpdate") ;

			fragmentTransaction.commitAllowingStateLoss();
		}catch(IllegalStateException i) {
			ALog.i(ALog.ERROR, "Error: ");
		}		
	}

	
	/*@SuppressWarnings("deprecation")
	@SuppressLint("NewApi")
	@Override
	public void setBackgroundDrawable(ViewGroup view, Drawable drawable) {
		if (view == null || drawable == null) return;
		if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
			view.setBackgroundDrawable(drawable);
		} else {
			view.setBackground(drawable);
		}
	}
	
	@Override
	public void setBackground(ViewGroup view, int resourceId, int color, float height) {
		if (view != null) {
			Bitmap src = BitmapFactory.decodeResource(getResources(), resourceId);
			Bitmap shadow = Utils.getShadow(src.getHeight(), src.getWidth(), color, height);
	
			Drawable drawable = new BitmapDrawable(getResources(), shadow);
			
			setBackgroundDrawable(view, drawable);
		}
	}*/
}
