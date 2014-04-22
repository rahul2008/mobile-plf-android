package com.philips.cl.di.dev.pa.activity;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.philips.cl.di.dev.pa.R;
import com.philips.cl.di.dev.pa.util.ALog;
import com.philips.cl.di.dev.pa.util.Fonts;

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
	 * method display a dialog with custom title and message
	 * 
	 * @param title
	 * @param message
	 */
	protected void showAlert(final int title, final int message) {
		this.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				// Created a new Dialog
				final Dialog dialog = new Dialog(BaseActivity.this);
				// inflate the layout
				dialog.setContentView(R.layout.tutorial_custom_dialog);
				dialog.setTitle(title);

				TextView dialogMessage = (TextView) dialog
						.findViewById(R.id.take_tour_alert);
				Button btnNo = (Button) dialog.findViewById(R.id.btn_close);
				Button btnYes = (Button) dialog.findViewById(R.id.btn_yes);

				dialogMessage.setText(message);
				btnYes.setVisibility(View.GONE);
				btnNo.setText(android.R.string.ok);

				btnNo.setTypeface(Fonts.getGillsans(BaseActivity.this));
				btnNo.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						dialog.dismiss();
					}
				});
				
				// Display the dialog
				dialog.show();
			}
		});

	}

}
