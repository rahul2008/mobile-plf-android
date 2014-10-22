package com.philips.cl.di.dev.pa.activity;

import net.hockeyapp.android.Tracking;
import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;
import android.widget.Toast;

import com.philips.cl.di.dev.pa.PurAirApplication;
import com.philips.cl.di.dev.pa.R;
import com.philips.cl.di.dev.pa.cpp.AppUpdateNotificationListener;
import com.philips.cl.di.dev.pa.cpp.CPPController;
import com.philips.cl.di.dev.pa.fragment.AppUpdateDialogFragment;
import com.philips.cl.di.dev.pa.util.ALog;
import com.philips.cl.di.dev.pa.util.networkutils.NetworkReceiver;

/**
 * The Class BaseActivity. This class contains all the base / common
 * functionalities.
 */
@SuppressLint("Registered")
public class BaseActivity extends ActionBarActivity implements AppUpdateNotificationListener {



	@Override
	protected void onCreate(Bundle savedInstanceState) {
		ALog.d(ALog.ACTIVITY, "OnCreate on " + this.getClass().getSimpleName());
		CPPController.getInstance(this).setAppUpdateNotificationListener(this) ;
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
	public void onAppUpdateFailed() {
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				Toast.makeText(PurAirApplication.getAppContext(),getString(R.string.app_update_failed),Toast.LENGTH_LONG).show() ;
			}
		});
	}
}
