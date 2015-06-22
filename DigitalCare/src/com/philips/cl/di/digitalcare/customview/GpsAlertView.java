package com.philips.cl.di.digitalcare.customview;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;

public class GpsAlertView {

	private AlertDialog.Builder mdialogBuilder = null;
	private AlertDialog malertDialog = null;
	private static GpsAlertView gpsAlertView = null;

	private GpsAlertView() {
	}

	public static GpsAlertView getInstance() {

		if (gpsAlertView == null) {
			gpsAlertView = new GpsAlertView();
		}
		return gpsAlertView;
	}

	public void showAlert(final Activity activity, int title_res_id,
			int message_res_id, int positiveButtonText_res_id,
			int negativeButtonText_res_id) {
		if (mdialogBuilder == null) {
			mdialogBuilder = new AlertDialog.Builder(activity);

			// mdialogBuilder.setTitle(activity.getResources().getString(
			// title_res_id));
			mdialogBuilder.setMessage(activity.getResources().getString(
					message_res_id));

			mdialogBuilder.setPositiveButton(positiveButtonText_res_id,
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {

							activity.startActivityForResult(new Intent(
									android.provider.Settings.ACTION_SETTINGS),
									0);

							dialog.dismiss();
							mdialogBuilder = null;

						}
					});

			mdialogBuilder.setNegativeButton(negativeButtonText_res_id,
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {

							dialog.dismiss();
							mdialogBuilder = null;
							//
						}
					}).setIcon(android.R.drawable.ic_dialog_alert);

			malertDialog = mdialogBuilder.create();
			malertDialog.show();
		}
	}

	public void removeAlert() {
		if (malertDialog != null) {
			malertDialog.dismiss();
			malertDialog = null;
			mdialogBuilder = null;
		}
	}

}
