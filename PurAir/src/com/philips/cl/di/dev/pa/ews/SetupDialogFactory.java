package com.philips.cl.di.dev.pa.ews;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.text.Html;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.philips.cl.di.dev.pa.R;
import com.philips.cl.di.dev.pa.demo.DemoModeActivity;
import com.philips.cl.di.dev.pa.util.ALog;
import com.philips.cl.di.dev.pa.util.Fonts;
import com.philips.cl.di.dev.pa.util.MetricsTracker;

public class SetupDialogFactory implements OnClickListener {

	private Dialog errorDialogTS0101;
	private Dialog supportUnplugPurifierDialog, supportWifiLEDOrangeDialog, supportDialogTS03, supportDialogTS05;
	private Dialog supportPlugAndPowerOnDialog, supportDialogTS02PowerOn;
	private Dialog checkSignalStrength, connetToProduct;

	private int errorID;
	private int supportID;
	private Context context;
	private Activity activity;
	private String networkName = "";
	
	public void setNetworkName(String networkName) {
		ALog.i(ALog.EWS, "setNetworkName : networkName " + networkName);
		this.networkName = networkName;
	}

	private static SetupDialogFactory _instance;

	public static SetupDialogFactory getInstance(Context context) {
		if(_instance == null) {
			_instance = new SetupDialogFactory(context);
		}
		return _instance;
	}

	private SetupDialogFactory(Context context) {
		this.context = context;
		this.activity = (Activity) context;
	}

	public Dialog getDialog(int id) {
		switch (id) {
		case SUPPORT_UNPLUG_PURIFIER:
			if(supportUnplugPurifierDialog == null)
				supportUnplugPurifierDialog = getSupportAlertDialog(context.getString(R.string.support_ts01_message), R.drawable.ews_help_bg1_2x, context.getString(R.string.next), SUPPORT_UNPLUG_PURIFIER);
			supportID = SUPPORT_UNPLUG_PURIFIER;
			return supportUnplugPurifierDialog;
		case SUPPORT_PLUG_AND_POWER_ON:
			if(supportPlugAndPowerOnDialog == null)
				supportPlugAndPowerOnDialog = getSupportAlertDialog(context.getString(R.string.support_ts01_message), R.drawable.ews_help_bg1_2x, context.getString(R.string.next), SUPPORT_PLUG_AND_POWER_ON);
			supportID = SUPPORT_PLUG_AND_POWER_ON;
			return supportPlugAndPowerOnDialog;
		case SUPPORT_WIFI_LED_ORANGE:
			if(supportWifiLEDOrangeDialog == null)
				supportWifiLEDOrangeDialog = getSupportAlertDialog(context.getString(R.string.support_ts02_message), R.drawable.ews_help_bg2_2x, context.getString(R.string.next), SUPPORT_WIFI_LED_ORANGE);
			supportID = SUPPORT_WIFI_LED_ORANGE;
			return supportWifiLEDOrangeDialog;
		case SUPPORT_TS02_POWERON:
			if(supportDialogTS02PowerOn == null)
				supportDialogTS02PowerOn = getSupportAlertDialog(context.getString(R.string.support_ts02_message), R.drawable.ews_help_bg2_2x, context.getString(R.string.done), SUPPORT_TS02_POWERON);
			supportID = SUPPORT_TS02_POWERON;
			return supportDialogTS02PowerOn;
		case SUPPORT_TS03:
			if(supportDialogTS03 == null)
				supportDialogTS03 = getSupportAlertDialog(context.getString(R.string.support_ts03_message), R.drawable.ews_help_bg3_2x, context.getString(R.string.next), SUPPORT_TS03);
			supportID = SUPPORT_TS03;
			return supportDialogTS03;
		case SUPPORT_TS05:
			if(supportDialogTS05 == null)
				supportDialogTS05 = getSupportAlertDialogTS05(SUPPORT_TS05);
			supportID = SUPPORT_TS05;
			return supportDialogTS05;
		case ERROR_TS01_01:
			if(errorDialogTS0101 == null)
				errorDialogTS0101 = getErrorDialog(context.getString(R.string.error_ts01_01_title), context.getString(R.string.error_ts01_01_message), context.getString(R.string.next), ERROR_TS01_01);
			errorID = ERROR_TS01_01;
			return errorDialogTS0101;
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
		button.setTypeface(Fonts.getCentraleSansLight(context));
		button.setOnClickListener(this);
		TextView tvHeader = (TextView) alertLayout.findViewById(R.id.tv_support_popup_title);
		tvHeader.setTypeface(Fonts.getCentraleSansLight(context));
		TextView tvMessage = (TextView) alertLayout.findViewById(R.id.tv_support_popup_message);
		tvMessage.setTypeface(Fonts.getCentraleSansLight(context));
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
		confirmWifiEnabledYes.setTypeface(Fonts.getCentraleSansLight(context));
		confirmWifiEnabledYes.setOnClickListener(this);
		Button confirmWifiEnabledNo = (Button) alertLayout.findViewById(R.id.btn_confirm_wifi_enabled_no);
		confirmWifiEnabledNo.setTypeface(Fonts.getCentraleSansLight(context));
		confirmWifiEnabledNo.setOnClickListener(this);
		((TextView) alertLayout.findViewById(
				R.id.tv_ts05_confirm_wifi_mode_enabled_header)).setTypeface(Fonts.getCentraleSansLight(context));
		TextView tvMessage = (TextView) alertLayout.findViewById(R.id.tv_cancel_wifi_setup_message);
		tvMessage.setTypeface(Fonts.getCentraleSansLight(context));
		String msg1 = context.getString(R.string.support_ts05_message) + "<font color=#EF6921>"+context.getString(R.string.orange)+"</font>" + " " + context.getString(R.string.now);
		tvMessage.setText(Html.fromHtml(msg1));
		ImageView ivGotoSupport = (ImageView) alertLayout.findViewById(R.id.iv_support);
		ivGotoSupport.setOnClickListener(this);
		ImageView ivCloseErrorPopup = (ImageView) alertLayout.findViewById(R.id.iv_close_popup);
		ivCloseErrorPopup.setOnClickListener(this);
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
		button.setTypeface(Fonts.getCentraleSansLight(context));
		button.setText(buttonText);
		button.setOnClickListener(this);
		TextView tvHeader = (TextView) alertLayout.findViewById(R.id.tv_error_header);
		tvHeader.setTypeface(Fonts.getCentraleSansLight(context));
		tvHeader.setText(header);
		TextView tvMessage = (TextView) alertLayout.findViewById(R.id.tv_error_message);
		tvMessage.setTypeface(Fonts.getCentraleSansLight(context));
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
		tvHeader.setTypeface(Fonts.getCentraleSansLight(context));
		TextView tvMessage = (TextView) alertLayout.findViewById(R.id.tv_check_signal_message);
		tvMessage.setTypeface(Fonts.getCentraleSansLight(context));

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
		ALog.i(ALog.EWS, "getDialog networkName " + networkName);
		tvHeader.setText(context.getString(R.string.checking_signal_strength_title) + " " + networkName + ".");
		tvHeader.setTypeface(Fonts.getCentraleSansLight(context));
		TextView tvMessage = (TextView) alertLayout.findViewById(R.id.tv_check_signal_message);
		tvMessage.setTypeface(Fonts.getCentraleSansLight(context));

		temp.setCanceledOnTouchOutside(false);
		temp.setCancelable(false);
		temp.setContentView(alertLayout);
		return temp;
	}

	@Override
	public void onClick(View v) {
		ALog.i(ALog.EWS, "onClick");
		switch(v.getId()) {
		case R.id.btn_error_popup:
			handleErrorDialog(errorID);
			break;

		case R.id.btn_support_button:
			handleSupportDialog(supportID);
			break;

		case R.id.iv_close_popup:
			closePopUp(supportID);
			break;
		case R.id.iv_support:
			if ( context instanceof EWSActivity ) {
				EWSActivity activity = (EWSActivity) context ;

				activity.showSupportFragment() ;
			} else if ( context instanceof DemoModeActivity) {
				DemoModeActivity activity = (DemoModeActivity) context ;
				activity.showSupportScreen() ;
			}  
			closePopUp(supportID);
			break;

		case R.id.iv_close_error_popup:
			closeErrorPopUp(errorID);
			break;
			
		case R.id.iv_goto_support:
			if ( context instanceof EWSActivity ) {
				EWSActivity activity = (EWSActivity) context ;

				activity.showSupportFragment() ;
			} else if ( context instanceof DemoModeActivity) {
				DemoModeActivity activity = (DemoModeActivity) context ;
				activity.showSupportScreen() ;
			} 
			closeErrorPopUp(errorID);
			break;

			/** See TS05_CONFIRM_ENABLED
			 * This is to check if Wifi is enabled on the Purifier or not*/
		case R.id.btn_confirm_wifi_enabled_yes:
			getDialog(SUPPORT_TS05).dismiss() ;
			if ( context instanceof EWSActivity) {
				EWSActivity activity = (EWSActivity) context ;
				activity.airPurifierInSetupMode() ;
			} else if ( context instanceof DemoModeActivity) {
				DemoModeActivity activity = (DemoModeActivity) context ;
				activity.showStepOneScreen() ;
			}  
			break;

		case R.id.btn_confirm_wifi_enabled_no:
			getDialog(SUPPORT_TS05).dismiss() ;
			if ( context instanceof EWSActivity) {
				EWSActivity activity = (EWSActivity) context ;
				activity.showSupportFragment() ;
			} else if ( context instanceof DemoModeActivity) {
				DemoModeActivity activity = (DemoModeActivity) context ;
				activity.showSupportScreen() ;
			} 
			break;
		default:
			break;
		}
	}

	private void closeErrorPopUp(int errorID2) {
		if(ERROR_TS01_01 == errorID2) {
			errorDialogTS0101.dismiss();
		}
	}

	private void closePopUp(int supportDialogID2) {
		switch (supportDialogID2) {
		case SUPPORT_UNPLUG_PURIFIER:
			supportUnplugPurifierDialog.dismiss();
			break;
		case SUPPORT_PLUG_AND_POWER_ON:
			supportPlugAndPowerOnDialog.dismiss();
			break;
		case SUPPORT_WIFI_LED_ORANGE:
			supportWifiLEDOrangeDialog.dismiss();
			break;
		case SUPPORT_TS02_POWERON:
			supportDialogTS02PowerOn.dismiss();
			break;
		case SUPPORT_TS03:
			supportDialogTS03.dismiss();
			break;
		case SUPPORT_TS05:
			supportDialogTS05.dismiss();
			break;
		default:
			break;
		}

	}

	private void handleSupportDialog(int supportDialogID2) {
		ALog.i(ALog.EWS, "handleSupportDialog dialogId" + supportDialogID2);
		switch (supportDialogID2) {
		case SUPPORT_UNPLUG_PURIFIER:
			getDialog(SUPPORT_UNPLUG_PURIFIER).dismiss();
			getDialog(SUPPORT_WIFI_LED_ORANGE).show();
            MetricsTracker.trackPage("WiFi Setup : Powercycle Purifier");
			break;
		case SUPPORT_PLUG_AND_POWER_ON:
			getDialog(SUPPORT_PLUG_AND_POWER_ON).dismiss();
			getDialog(SUPPORT_TS02_POWERON).show();
            MetricsTracker.trackPage("WiFi Setup : Power on Purifier");
			break;
		case SUPPORT_WIFI_LED_ORANGE:
			getDialog(SUPPORT_WIFI_LED_ORANGE).dismiss();
			getDialog(SUPPORT_TS03).show();
            MetricsTracker.trackPage("WiFi Setup : Enter setup mode");
			break;
		case SUPPORT_TS02_POWERON:
			getDialog(SUPPORT_TS02_POWERON).dismiss();
			break;
		case SUPPORT_TS03:
			getDialog(SUPPORT_TS03).dismiss();
			getDialog(SUPPORT_TS05).show();
            MetricsTracker.trackPage("WiFi Setup : Check WiFi LED");
			break;
		case SUPPORT_TS05:
			getDialog(SUPPORT_TS05).dismiss();
			break;
		default:
			break;
		}

	}

	private void handleErrorDialog(int errorDialogID2) {
		ALog.i(ALog.EWS, "handleErrorDialog dialogId "  + errorDialogID2);	
		if(ERROR_TS01_01 == errorDialogID2) {
			getDialog(ERROR_TS01_01).dismiss();
		}
		getDialog(SUPPORT_UNPLUG_PURIFIER).show();
        MetricsTracker.trackPage("WiFi Setup : Unplug purifier");
	}
	
	public void dismissSignalStrength() {
		if (checkSignalStrength != null && checkSignalStrength.isShowing()) {
			checkSignalStrength.dismiss();
		}
	}

	public void cleanUp() {
		errorDialogTS0101 = null;

		supportPlugAndPowerOnDialog = null;
		supportDialogTS02PowerOn = null;
		supportUnplugPurifierDialog = null;
		supportWifiLEDOrangeDialog = null;
		supportDialogTS03 = null;
		supportDialogTS05 = null;

		context = null;
		cleanUpInstance();
	}
	
	private static void cleanUpInstance() {
		_instance = null;
	}

	//Dialog constants
	public static final int ERROR_TS01_01 = 1001;

	public static final int SUPPORT_UNPLUG_PURIFIER = 2001;
	public static final int SUPPORT_WIFI_LED_ORANGE = 2002;
	public static final int SUPPORT_TS03 = 2003;
	public static final int SUPPORT_TS05 = 2005;
	public static final int SUPPORT_PLUG_AND_POWER_ON = 2006;
	public static final int SUPPORT_TS02_POWERON = 2007; 

	public static final int CHECK_SIGNAL_STRENGTH = 3001;
	public static final int CONNECTING_TO_PRODUCT = 3002;
}
