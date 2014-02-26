package com.philips.cl.di.dev.pa.ews;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.philips.cl.di.dev.pa.R;
import com.philips.cl.di.dev.pa.utils.Fonts;

public class EWSDialogFactory implements OnClickListener{

	private Dialog errorDialogTS01_01, errorDialogTS01_02, errorDialogTS01_03, errorDialogTS01_04, errorDialogTS01_05;
	private Dialog supportDialogTS01, supportDialogTS02, supportDialogTS03, supportDialogTS05;
	private Dialog cancelWifiSetup, checkSignalStrength, connetToProduct;

	private int errorID;
	private int supportID;
	private Context context;
	private Activity activity;

	private static EWSDialogFactory _instance;

	public static EWSDialogFactory getInstance(Context context) {
		if(_instance == null) {
			_instance = new EWSDialogFactory(context);
		}
		return _instance;
	}

	private EWSDialogFactory(Context context) {
		this.context = context;
		this.activity = (Activity) context;
	}

	public Dialog getDialog(int id) {
		switch (id) {
		case SUPPORT_TS01:
			if(supportDialogTS01 == null)
				supportDialogTS01 = getSupportAlertDialog(context.getString(R.string.support_ts01_message), R.drawable.ews_help_bg1_2x, context.getString(R.string.next), 1);
			return supportDialogTS01;
		case SUPPORT_TS02:
			if(supportDialogTS02 == null)
				supportDialogTS02 = getSupportAlertDialog(context.getString(R.string.support_ts02_message), R.drawable.ews_help_bg2_2x, context.getString(R.string.next), 2);
			return supportDialogTS02;
		case SUPPORT_TS03:
			if(supportDialogTS03 == null)
				supportDialogTS03 = getSupportAlertDialog(context.getString(R.string.support_ts03_message), R.drawable.ews_help_bg3_2x, context.getString(R.string.next), 3);
			return supportDialogTS03;
		case SUPPORT_TS05:
			if(supportDialogTS05 == null)
				supportDialogTS05 = getSupportAlertDialogTS05(5);
			return supportDialogTS05;
		case ERROR_TS01_01:
			if(errorDialogTS01_01 == null)
				errorDialogTS01_01 = getErrorDialog(context.getString(R.string.error_ts01_01_title), context.getString(R.string.error_ts01_01_message), context.getString(R.string.next), 1);
			return errorDialogTS01_01;
		case ERROR_TS01_02:
			if(errorDialogTS01_02 == null)
				errorDialogTS01_02 = getErrorDialog(context.getString(R.string.error_ts01_02_title), context.getString(R.string.error_ts01_02_message), context.getString(R.string.next), 2);
			return errorDialogTS01_02;
		case ERROR_TS01_03:
			if(errorDialogTS01_03 == null)
				errorDialogTS01_03 = getErrorDialog(context.getString(R.string.error_ts01_03_title), context.getString(R.string.error_ts01_03_message), context.getString(R.string.error_purifier_not_detect_btn_txt), 3);
			return errorDialogTS01_03;
		case ERROR_TS01_04:
			if(errorDialogTS01_04 == null)
				errorDialogTS01_04 = getErrorDialog(context.getString(R.string.error_ts01_04_title), context.getString(R.string.error_ts01_04_message), context.getString(R.string.error_purifier_not_detect_btn_txt), 4);
			return errorDialogTS01_04;
		case ERROR_TS01_05:
			if(errorDialogTS01_05 == null)
				errorDialogTS01_05 = getErrorDialog(context.getString(R.string.error_ts01_05_title), context.getString(R.string.error_ts01_05_message), context.getString(R.string.error_purifier_not_detect_btn_txt), 5);
			return errorDialogTS01_05;
		case CANCEL_WIFI_SETUP:
			if(cancelWifiSetup == null)
				cancelWifiSetup = getCancelWifiSetupDialog();
			return cancelWifiSetup;
		case CHECK_SIGNAL_STRENGTH:
			if(checkSignalStrength == null)
				checkSignalStrength = getCheckingSignalStrengthDialog();
			return checkSignalStrength;
		case CONNECTING_TO_PRODUCT:
			if(connetToProduct == null)
				connetToProduct = getConnectingToProductDialog();
			return connetToProduct;
		}
		return null;
	}

	private Dialog getSupportAlertDialog(String message, int imageResourceID, String buttonText, int id) {
		Dialog temp = new Dialog(activity);
		temp.requestWindowFeature(Window.FEATURE_NO_TITLE);

		RelativeLayout alertLayout = (RelativeLayout) View.inflate(context, R.layout.support_alert_dialog, null);
		Button button = (Button) alertLayout.findViewById(R.id.btn_support_button);
		supportID = id;
		button.setText(buttonText);
		button.setTypeface(Fonts.getGillsansLight(context));
		button.setOnClickListener(this);
		TextView tvHeader = (TextView) alertLayout.findViewById(R.id.tv_support_popup_title);
		tvHeader.setTypeface(Fonts.getGillsansLight(context));
		TextView tvMessage = (TextView) alertLayout.findViewById(R.id.tv_support_popup_message);
		tvMessage.setTypeface(Fonts.getGillsansLight(context));
		tvMessage.setText(message);
		ImageView ivLeft = (ImageView) alertLayout.findViewById(R.id.iv_support);
		ivLeft.setOnClickListener(this);
		ImageView ivRight = (ImageView) alertLayout.findViewById(R.id.iv_close_popup);
		ivRight.setOnClickListener(this);
		ImageView ivCenter = (ImageView) alertLayout.findViewById(R.id.iv_support_popup_image);
		ivCenter.setImageResource(imageResourceID);
		temp.setCanceledOnTouchOutside(false);
		temp.setCancelable(false);
		temp.setContentView(alertLayout);
		return temp;
	}

	private Dialog getSupportAlertDialogTS05(int id) {
		Dialog temp = new Dialog(activity);
		temp.requestWindowFeature(Window.FEATURE_NO_TITLE);
		supportID = id;
		RelativeLayout alertLayout = (RelativeLayout) View.inflate(context, R.layout.ts05_confirm_enabled, null);
		Button confirmWifiEnabledYes = (Button) alertLayout.findViewById(R.id.btn_confirm_wifi_enabled_yes);
		confirmWifiEnabledYes.setOnClickListener(this);
		Button confirmWifiEnabledNo = (Button) alertLayout.findViewById(R.id.btn_confirm_wifi_enabled_no);
		confirmWifiEnabledNo.setOnClickListener(this);
		TextView tvMessage = (TextView) alertLayout.findViewById(R.id.tv_cancel_wifi_setup_message);
		String msg1 = context.getString(R.string.support_ts05_message) + " <font color=#EF6921>"+context.getString(R.string.orange)+"</font>" + " " + context.getString(R.string.now);
		tvMessage.setText(Html.fromHtml(msg1));
		temp.setCanceledOnTouchOutside(false);
		temp.setCancelable(false);
		temp.setContentView(alertLayout);
		return temp;
	}

	private Dialog getErrorDialog(String header, String message, String buttonText, int id) {
		Dialog temp = new Dialog(context);
		temp.requestWindowFeature(Window.FEATURE_NO_TITLE);
		errorID = id;
		RelativeLayout alertLayout = (RelativeLayout) View.inflate(context, R.layout.error_alert_layout, null);
		Button button = (Button) alertLayout.findViewById(R.id.btn_error_popup);
		button.setTypeface(Fonts.getGillsansLight(context));
		button.setText(buttonText);
		button.setOnClickListener(this);
		TextView tvHeader = (TextView) alertLayout.findViewById(R.id.tv_error_header);
		tvHeader.setTypeface(Fonts.getGillsansLight(context));;
		tvHeader.setText(header);
		TextView tvMessage = (TextView) alertLayout.findViewById(R.id.tv_error_message);
		tvMessage.setTypeface(Fonts.getGillsansLight(context));
		tvMessage.setText(message);
		ImageView ivGotoSupport = (ImageView) alertLayout.findViewById(R.id.iv_goto_support);
		ivGotoSupport.setOnClickListener(this);
		ImageView ivCloseErrorPopup = (ImageView) alertLayout.findViewById(R.id.iv_close_error_popup);
		ivCloseErrorPopup.setOnClickListener(this);

		temp.setCanceledOnTouchOutside(false);
		temp.setCancelable(false);
		temp.setContentView(alertLayout);
		return temp;
	}

	private Dialog getCheckingSignalStrengthDialog() {
		Dialog temp = new Dialog(context);
		temp.requestWindowFeature(Window.FEATURE_NO_TITLE);
		RelativeLayout alertLayout = (RelativeLayout) View.inflate(context, R.layout.checking_signal_strength, null); 
		TextView tvHeader = (TextView) alertLayout.findViewById(R.id.tv_check_signal_header);
		tvHeader.setTypeface(Fonts.getGillsansLight(context));
		TextView tvMessage = (TextView) alertLayout.findViewById(R.id.tv_check_signal_message);
		tvMessage.setTypeface(Fonts.getGillsansLight(context));

		temp.setCanceledOnTouchOutside(false);
		temp.setCancelable(false);
		temp.setContentView(alertLayout);
		return temp;
	}

	private Dialog getConnectingToProductDialog() {
		Dialog temp = new Dialog(context);
		temp.requestWindowFeature(Window.FEATURE_NO_TITLE);
		RelativeLayout alertLayout = (RelativeLayout) View.inflate(context, R.layout.connecting_to_product, null); 
		TextView tvHeader = (TextView) alertLayout.findViewById(R.id.tv_check_signal_header);
		tvHeader.setTypeface(Fonts.getGillsansLight(context));
		TextView tvMessage = (TextView) alertLayout.findViewById(R.id.tv_check_signal_message);
		tvMessage.setTypeface(Fonts.getGillsansLight(context));

		temp.setCanceledOnTouchOutside(false);
		temp.setCancelable(false);
		temp.setContentView(alertLayout);
		return temp;
	}

	private Dialog getCancelWifiSetupDialog() {
		Dialog temp = new Dialog(context);
		temp.requestWindowFeature(Window.FEATURE_NO_TITLE);
		RelativeLayout alertLayout = (RelativeLayout) View.inflate(context, R.layout.cancel_wifi_setup, null); 
		TextView tvHeader = (TextView) alertLayout.findViewById(R.id.tv_cancel_wifi_setup_header);
		tvHeader.setTypeface(Fonts.getGillsansLight(context));
		TextView tvMessage = (TextView) alertLayout.findViewById(R.id.tv_cancel_wifi_setup_message);
		tvMessage.setTypeface(Fonts.getGillsansLight(context));
		Button cancelWifiYes = (Button) alertLayout.findViewById(R.id.btn_cancel_wifi_yes);
		cancelWifiYes.setOnClickListener(this);
		Button cancelWifiNo = (Button) alertLayout.findViewById(R.id.btn_cancel_wifi_no);
		cancelWifiNo.setOnClickListener(this);

		temp.setCanceledOnTouchOutside(false);
		temp.setCancelable(false);
		temp.setContentView(alertLayout);
		return temp;
	}

	@Override
	public void onClick(View v) {
		switch(v.getId()) {
		case R.id.btn_error_popup:
			handleErrorDialog(errorID);
			break;

		case R.id.btn_support_button:
			handleSupportDialog(supportID);
			break;

		case R.id.iv_close_popup:
			//			Toast.makeText(context, "Close Pop up", Toast.LENGTH_SHORT).show();
			closePopUp(supportID);
			break;
		case R.id.iv_support:
			break;

		case R.id.iv_close_error_popup:
			closeErrorPopUp(errorID);

		case R.id.btn_cancel_wifi_yes:
			//			closeErrorPopUp(CANCEL_WIFI_SETUP) ;
			getDialog(CANCEL_WIFI_SETUP).dismiss();
			if ( context instanceof EwsActivity ) {
				EwsActivity activity = (EwsActivity) context ;

				activity.finish() ;
			}


			break;

		case R.id.btn_cancel_wifi_no:
			getDialog(CANCEL_WIFI_SETUP).dismiss();
			break;

			/** See TS05_CONFIRM_ENABLED
			 * This is to check if Wifi is enabled on the Purifier or not*/
		case R.id.btn_confirm_wifi_enabled_yes:
			getDialog(SUPPORT_TS05).dismiss() ;
			if ( context instanceof EwsActivity) {
				EwsActivity activity = (EwsActivity) context ;
				activity.connectToAirPurifier() ;
			}
			break;

		case R.id.btn_confirm_wifi_enabled_no:
			getDialog(SUPPORT_TS05).dismiss() ;
			if ( context instanceof EwsActivity) {
				EwsActivity activity = (EwsActivity) context ;
				activity.showSupportScreen() ;
			}
			break;
		}
	}

	private void closeErrorPopUp(int errorID2) {
		switch (errorID2) {
		case 1:
			errorDialogTS01_01.dismiss();
			break;
		case 2:
			errorDialogTS01_02.dismiss();
			break;
		case 3:
			errorDialogTS01_03.dismiss();
			break;
		case 4:
			errorDialogTS01_04.dismiss();
			break;
		case 5:
			errorDialogTS01_05.dismiss();
			break;
		}
	}

	private void closePopUp(int supportDialogID2) {
		switch (supportDialogID2) {
		case 1:
			supportDialogTS01.dismiss();
			break;
		case 2:
			supportDialogTS02.dismiss();
			break;
		case 3:
			supportDialogTS03.dismiss();
			break;
		case 4:
			supportDialogTS05.dismiss();
			break;
		case CANCEL_WIFI_SETUP:
			cancelWifiSetup.dismiss() ;
			break;
		default:
			break;
		}

	}

	private void handleSupportDialog(int supportDialogID2) {
		switch (supportDialogID2) {
		case 1:
			getDialog(SUPPORT_TS01).dismiss();
			getDialog(SUPPORT_TS02).show();
			break;
		case 2:
			getDialog(SUPPORT_TS02).dismiss();
			getDialog(SUPPORT_TS03).show();
			break;
		case 3:
			getDialog(SUPPORT_TS03).dismiss();
			getDialog(SUPPORT_TS05).show();
			break;
		case 5:
			getDialog(SUPPORT_TS05).dismiss();
			break;
		default:
			break;
		}

	}

	private void handleErrorDialog(int errorDialogID2) {
		switch (errorDialogID2) {
		case 1:
			getDialog(ERROR_TS01_01).dismiss();
			getDialog(SUPPORT_TS01).show();
			break;
		case 2 : 
			getDialog(ERROR_TS01_02).dismiss();
			getDialog(SUPPORT_TS01).show();
			break;
		case 3:
			getDialog(ERROR_TS01_03).dismiss();
			getDialog(SUPPORT_TS01).show();
			break;
		case 4:
			getDialog(ERROR_TS01_04).dismiss();
			getDialog(SUPPORT_TS01).show();
			break;
		case 5:
			getDialog(ERROR_TS01_05).dismiss();
			break;

		default:
			break;
		}
	}

	public void cleanUp() {
		errorDialogTS01_01 = null;
		errorDialogTS01_02 = null;
		errorDialogTS01_03 = null;
		errorDialogTS01_04 = null;
		errorDialogTS01_05 = null;

		supportDialogTS01 = null;
		supportDialogTS02 = null;
		supportDialogTS03 = null;
		supportDialogTS05 = null;

		context = null;
		_instance = null;
	}

	//Dialog constants
	public static final int ERROR_TS01_01 = 1001;
	public static final int ERROR_TS01_02 = 1002;
	public static final int ERROR_TS01_03 = 1003;
	public static final int ERROR_TS01_04 = 1004;
	public static final int ERROR_TS01_05 = 1005;

	public static final int SUPPORT_TS01 = 2001;
	public static final int SUPPORT_TS02 = 2002;
	public static final int SUPPORT_TS03 = 2003;
	public static final int SUPPORT_TS05 = 2005;

	public static final int CHECK_SIGNAL_STRENGTH = 3001;
	public static final int CONNECTING_TO_PRODUCT = 3002;
	public static final int CANCEL_WIFI_SETUP = 3003;
}
