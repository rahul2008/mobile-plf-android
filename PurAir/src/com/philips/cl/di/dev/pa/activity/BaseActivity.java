package com.philips.cl.di.dev.pa.activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.philips.cl.di.dev.pa.R;
import com.philips.cl.di.dev.pa.constant.AppConstants;
import com.philips.cl.di.dev.pa.cpp.PairingListener;
import com.philips.cl.di.dev.pa.cpp.PairingManager;
import com.philips.cl.di.dev.pa.util.ALog;
import com.philips.cl.di.dev.pa.util.Fonts;
import com.philips.icpinterface.ICPClient;
import com.philips.icpinterface.PairingService;
import com.philips.icpinterface.data.Commands;
import com.philips.icpinterface.data.Errors;

/**
 * The Class BaseActivity. This class contains all the base / common
 * functionalities.
 */
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

	/**
	 * method display an alert box with custom title and message
	 * 
	 * @param title
	 * @param message
	 */
	protected void showAlert(final int title, final int message) {
		this.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				new AlertDialog.Builder(BaseActivity.this)
						.setMessage(message)
						.setTitle(title)
						.setCancelable(true)
						.setNeutralButton(android.R.string.ok,
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,
											int whichButton) {
									}
								}).show();
			}
		});

	}

}
