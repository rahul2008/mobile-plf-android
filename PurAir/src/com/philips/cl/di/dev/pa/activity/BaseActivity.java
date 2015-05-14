package com.philips.cl.di.dev.pa.activity;

import net.hockeyapp.android.Tracking;
import android.annotation.SuppressLint;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.NotificationCompat;
import android.view.ViewGroup;

import com.philips.cl.di.dev.pa.IBackgroundDrawable;
import com.philips.cl.di.dev.pa.R;
import com.philips.cl.di.dev.pa.cpp.AppUpdateNotificationListener;
import com.philips.cl.di.dev.pa.cpp.CPPController;
import com.philips.cl.di.dev.pa.fragment.AppUpdateDialogFragment;
import com.philips.cl.di.dev.pa.util.ALog;
import com.philips.cl.di.dev.pa.util.MetricsTracker;
import com.philips.cl.di.dev.pa.util.Utils;
import com.philips.cl.di.dev.pa.util.networkutils.NetworkReceiver;

/**
 * The Class BaseActivity. This class contains all the base / common
 * functionalities.
 */
@SuppressLint("Registered")
public class BaseActivity extends FragmentActivity implements AppUpdateNotificationListener, IBackgroundDrawable {
	
	private NotificationManager notificationMan;
	private final int NOTIFICATION_ID=45;
	protected ConnectivityManager connectivityManager;
	protected WifiManager wifiManager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		ALog.d(ALog.ACTIVITY, "OnCreate on " + this.getClass().getSimpleName());
		CPPController.getInstance(this).setAppUpdateNotificationListener(this) ;
		connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE) ;
		wifiManager = (WifiManager) getSystemService(Context.WIFI_SERVICE) ;
		notificationMan=(NotificationManager) getSystemService(NOTIFICATION_SERVICE);
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
	}

	@Override
	protected void onPause() {
		ALog.d(ALog.ACTIVITY, "OnPause on " + this.getClass().getSimpleName());
		Tracking.stopUsage(this);
		NetworkReceiver.getInstance().unregisterNetworkReceiver();
		super.onPause();
		MetricsTracker.stopCollectLifecycleData();
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
	public void onAppUpdate() {
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

		}
	}

	@Override
	public void onAppUpdateFailed(final String message) {
		showNotification(message);
	}
	
	public void showNotification(String msg){
		PendingIntent contentIntent = PendingIntent.getActivity(this,
				0, new Intent(), 0);

		NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this);
		mBuilder.setSmallIcon(R.drawable.purair_icon); // TODO change notification icon
		mBuilder.setContentTitle(getString(R.string.app_update_failed));
		mBuilder.setWhen(System.currentTimeMillis());
		mBuilder.setStyle(new NotificationCompat.BigTextStyle().bigText(msg));
		mBuilder.setContentText(msg);
		mBuilder.setAutoCancel(true);

		mBuilder.setContentIntent(contentIntent);
		notificationMan.notify(NOTIFICATION_ID, mBuilder.build());
	}
	
	@SuppressWarnings("deprecation")
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
	
	protected void setBackground(ViewGroup view) {
		if (view != null) {
			Bitmap src = BitmapFactory.decodeResource(getResources(), R.drawable.ews_nav_bar_2x);
			Bitmap shadow = Utils.getShadow(src.getHeight(), src.getWidth(), Color.BLACK, .1F);
	
			Drawable drawable = new BitmapDrawable(getResources(), shadow);
			
			setBackgroundDrawable(view, drawable);
		}
	}
}
