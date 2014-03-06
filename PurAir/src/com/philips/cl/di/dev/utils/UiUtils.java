package com.philips.cl.di.dev.utils;


import android.app.AlertDialog;
import android.content.Context;
import android.widget.Toast;

import com.philips.cl.di.dev.pa.R;

public class UiUtils {
	
	private Context context ;
	public UiUtils(Context context) {
		this.context = context ;
	}
	/**
	 * This shows an alert message to the user
	 * @param message
	 */
	public void showAlert(int message) {

		new AlertDialog.Builder(context)
				.setTitle(context.getResources().getString(R.string.warning))
				.setMessage(context.getResources().getString(message))
				.setPositiveButton(
						context.getResources().getString(R.string.ok), null)
				.show();
	}
	
	/**
	 * This shows a Toast message (PopUp) to the user for the specified duration
	 * @param message
	 */
	public void showToast(int message,int duration) {
		Toast.makeText(context, context.getResources().getString(message), duration).show() ;
	}
}
