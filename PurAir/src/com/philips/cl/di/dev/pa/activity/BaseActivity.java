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
public class BaseActivity extends ActionBarActivity implements PairingListener {

	protected ProgressDialog progressDialog;

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

	protected void showPairingDialog(final String purifierEui64) {
		// Created a new Dialog
		final Dialog dialog = new Dialog(this);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		// inflate the layout
		dialog.setContentView(R.layout.tutorial_custom_dialog);

		TextView message = (TextView) dialog
				.findViewById(R.id.take_tour_alert);
		Button btnClose = (Button) dialog.findViewById(R.id.btn_close);
		Button btn_pair = (Button) dialog.findViewById(R.id.btn_yes);

		message.setText(R.string.pair_title);
		btn_pair.setText(R.string.pair);

		btnClose.setTypeface(Fonts.getGillsans(this));
		btn_pair.setTypeface(Fonts.getGillsans(this));

		btnClose.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				dialog.dismiss();
			}
		});
		btn_pair.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				progressDialog = new ProgressDialog(BaseActivity.this);
				progressDialog
						.setMessage("pairing will take some time, please wait..");
				progressDialog.show();
				startPairing(purifierEui64) ;
				dialog.dismiss();
			}
		});

		// Display the dialog
		dialog.show();
	}
	
	protected void startPairing(String eui64) {
		PairingManager pm = new PairingManager(this,this, eui64);
		pm.startPairing(AppConstants.DI_COMM_RELATIONSHIP,
				AppConstants.PERMISSIONS.toArray(new String[AppConstants.PERMISSIONS.size()]));
	}

	@Override
	public void onPairingStateReceived(int status, int eventType, ICPClient obj) {
		if (progressDialog != null) {
			progressDialog.cancel();
		}
		if (eventType == Commands.PAIRING_GET_RELATIONSHIPS) {
			if (status != Errors.SUCCESS) {
				ALog.e(ALog.ACTIVITY, "GetRelation-FAILED");
				showAlert(R.string.error_title, R.string.paring_incomplete);
			} else {
				ALog.e(ALog.ACTIVITY, "GetRelation-AlreadyPaired");
			}
		} else if (eventType == Commands.PAIRING_ADD_RELATIONSHIP) {
			if (status == Errors.SUCCESS) {
				ALog.i(ALog.ACTIVITY, "AddRelation-SUCCESS");
				PairingService pairingObj = (PairingService) obj;
				String relationStatus = pairingObj.getAddRelationStatus();
				if (relationStatus.equalsIgnoreCase("completed")) {
					ALog.i(ALog.ACTIVITY, "AddRelation-COMPLETED");
					showAlert(R.string.congratulations, R.string.paring_done);
				} else {
					ALog.i(ALog.ACTIVITY, "AddRelation-PENDING");
					showAlert(R.string.error_title, R.string.paring_incomplete);
				}
			} else {
				ALog.e(ALog.ACTIVITY, "AddRelation-FAILED");
				showAlert(R.string.error_title, R.string.paring_incomplete);
			}
		}
	}
	
	
	@Override
	public void onPairingPortTaskFailed() {
		if (progressDialog != null) {
			progressDialog.cancel();
		}
		showAlert(R.string.error_title, R.string.paring_incomplete);
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
