/*
 *  Copyright (c) Koninklijke Philips N.V., 2016
 *  All rights are reserved. Reproduction or dissemination
 *  in whole or in part is prohibited without the prior written
 *  consent of the copyright holder.
 *
 */

package com.philips.cdp.registration.coppa.ui.customviews;

import android.app.Activity;
import android.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.philips.cdp.registration.coppa.R;

public class RegCoppaAlertDialog {
	private static AlertDialog dialogBuilder;
	public static void showCoppaDialogMsg(String title, String content, Activity activity,
										  View.OnClickListener continueBtnClickListener) {

		dialogBuilder = new AlertDialog.Builder(activity).create();
		dialogBuilder.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialogBuilder.setCancelable(false);
		final LayoutInflater li = activity.getLayoutInflater();
		View dialog_view = li.inflate(R.layout.reg_coppa_alert_dialog, null);
		Button contBtn = (Button) dialog_view.findViewById(R.id.btn_reg_continue);
		TextView tvTitle = (TextView)dialog_view.findViewById(R.id.tv_reg_header_dialog_title);
		TextView tvContent = (TextView)dialog_view.findViewById(R.id.tv_reg_dialog_content);

		tvTitle.setText(title);
		tvContent.setText(content);

		contBtn.setOnClickListener(continueBtnClickListener);

		dialogBuilder.setView(dialog_view);
		dialogBuilder.show();
	}

	public static void dismissDialog(){
		if(dialogBuilder!=null){
			dialogBuilder.dismiss();
		}
	}
}

