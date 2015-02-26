package com.philips.cl.di.dev.digitalcare.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.telephony.TelephonyManager;
import android.view.Gravity;
import android.widget.Toast;

/**
 * @Description This class contains common utility methods required across
 *              framework under different scenario's.
 * @author naveen@philips.com
 * 
 */
public class Utils {

	public static boolean isConnected(Activity activity) {
		ConnectivityManager mConnectManager = (ConnectivityManager) activity
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (mConnectManager != null) {
			NetworkInfo[] mNetworkInfo = mConnectManager.getAllNetworkInfo();
			for (int i = 0; i < mNetworkInfo.length; i++) {
				if (mNetworkInfo[i].getState() == NetworkInfo.State.CONNECTED)
					return true;
			}
		}

		Toast toast = Toast.makeText(activity, "No internet connection",
				Toast.LENGTH_SHORT);
		toast.setGravity(Gravity.CENTER, 0, 0);
		toast.show();
		return false;
	}

	public static boolean isTablet(Context context) {
		return (context.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) >= Configuration.SCREENLAYOUT_SIZE_LARGE;
	}

	public static Bitmap readImage(Context context) {
		try {
			File mFile = internalStoragePath(context);
			return BitmapFactory.decodeStream(new FileInputStream(mFile));
		} catch (FileNotFoundException e) {
			return null;
		}
	}

	public static File internalStoragePath(Context c) {
		ContextWrapper mContext = new ContextWrapper(c);
		File mFile = mContext.getDir("DigitalCare Product Image",
				Context.MODE_PRIVATE);
		File mFilePath = new File(mFile, DigiCareContants.PHILIPS_PRODUCT_IMAGE);
		return mFilePath;
	}

	public static void sendEmail(Activity activity) {
		Intent intent = new Intent(Intent.ACTION_SEND);
		intent.setType("message/rfc822");
		intent.putExtra(Intent.EXTRA_EMAIL,
				"PhilipsCustomer@philipsSupport.com");
		intent.putExtra(Intent.EXTRA_SUBJECT,
				"My AirFryer HD9220/20 is gone case");
		intent.putExtra(
				Intent.EXTRA_TEXT,
				"Hi Team\n My Airfryer is not at all cooking actually. It is leaving ultimate smoke."
						+ " Please do let me know how i can correct my favourate Philips Machine!! ");

		intent.setPackage("com.google.android.gm");
		activity.startActivity(intent);
	}

	public static boolean isSimAvailable(Activity mContext) {
		TelephonyManager mTelephonyService = (TelephonyManager) mContext
				.getSystemService(Context.TELEPHONY_SERVICE);
		// int state = mTelephonyService.getPhoneType();
		// switch (state) {
		// case TelephonyManager.PHONE_TYPE_GSM:
		// return true;
		// case TelephonyManager.PHONE_TYPE_CDMA:
		// return true;
		// case TelephonyManager.PHONE_TYPE_SIP:
		// return true;
		// case TelephonyManager.PHONE_TYPE_NONE:
		// return false;
		// }
		// return false;

		int simState = mTelephonyService.getSimState();
		switch (simState) {
		case TelephonyManager.SIM_STATE_ABSENT:
		case TelephonyManager.SIM_STATE_NETWORK_LOCKED:
			return false;
		default:
			return true;
		}
	}
}
