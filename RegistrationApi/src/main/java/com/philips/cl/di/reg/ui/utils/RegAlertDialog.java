
package com.philips.cl.di.reg.ui.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;

import com.philips.cl.di.reg.R;
import com.philips.cl.di.reg.adobe.analytics.AnalyticsConstants;
import com.philips.cl.di.reg.adobe.analytics.AnalyticsUtils;

public class RegAlertDialog {

	public static void showResetPasswordDialog(Activity activity) {

		final AlertDialog alertDialogBuilder = new AlertDialog.Builder(activity).create();
		alertDialogBuilder.requestWindowFeature(Window.FEATURE_NO_TITLE);
		alertDialogBuilder.setCancelable(true);
		LayoutInflater myLayoutInflater = activity.getLayoutInflater();
		View myView = myLayoutInflater.inflate(R.layout.dialog_reset_password, null);
		Button continueBtn = (Button) myView.findViewById(R.id.btn_reg_continue);

		continueBtn.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				alertDialogBuilder.dismiss();
			}
		});

		alertDialogBuilder.setView(myView);
		alertDialogBuilder.show();
	}

	private static void trackActionForResetPasswordNotification(String state,
            String statusNotification, String continueStatus) {
		AnalyticsUtils.trackAction(state, statusNotification, continueStatus);
    }

}
