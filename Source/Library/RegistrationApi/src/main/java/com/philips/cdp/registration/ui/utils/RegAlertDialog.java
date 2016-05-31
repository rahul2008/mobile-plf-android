
/*
 *  Copyright (c) Koninklijke Philips N.V., 2016
 *  All rights are reserved. Reproduction or dissemination
 *  * in whole or in part is prohibited without the prior written
 *  * consent of the copyright holder.
 * /
 */

package com.philips.cdp.registration.ui.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.philips.cdp.registration.R;

public class RegAlertDialog {
	private static AlertDialog alertDialogBuilder;
	public static void showResetPasswordDialog(String title, String content, Activity activity, View.OnClickListener continueBtnClickListener) {

		alertDialogBuilder = new AlertDialog.Builder(activity).create();
		alertDialogBuilder.requestWindowFeature(Window.FEATURE_NO_TITLE);
		alertDialogBuilder.setCancelable(false);
		LayoutInflater layoutInflater = activity.getLayoutInflater();
		View view = layoutInflater.inflate(R.layout.dialog_reset_password, null);
		Button continueBtn = (Button) view.findViewById(R.id.btn_reg_continue);
		TextView titleView = (TextView)view.findViewById(R.id.tv_reg_header_dialog_title);
		TextView contentView = (TextView)view.findViewById(R.id.tv_reg_dialog_content);
		titleView.setText(title);
		contentView.setText(content);
		continueBtn.setOnClickListener(continueBtnClickListener);
		alertDialogBuilder.setView(view);
		alertDialogBuilder.show();
	}


	public static void dismissDialog(){
		if(alertDialogBuilder!=null){
			alertDialogBuilder.dismiss();
		}
	}
}

