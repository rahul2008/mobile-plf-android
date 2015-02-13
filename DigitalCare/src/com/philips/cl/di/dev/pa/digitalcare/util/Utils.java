package com.philips.cl.di.dev.pa.digitalcare.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

/**
 * @Description This class contains common utility methods required across
 *              framework under different scenario's.
 * @author naveen@philips.com
 *
 */
public class Utils {

	private static String CAMERA_IMAGE = null;

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
		Toast.makeText(activity, "No internet connection", Toast.LENGTH_SHORT)
				.show();
		return false;
	}

	public static boolean isTablet(Context context) {
		return (context.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) >= Configuration.SCREENLAYOUT_SIZE_LARGE;
	}

	public static void copyImage(Context context, Bitmap bitmap)
			throws IOException {

		FileOutputStream mFileOutStream = null;
		mFileOutStream = new FileOutputStream(internalStoragePath(context));
		bitmap.compress(Bitmap.CompressFormat.PNG, 100, mFileOutStream);
		mFileOutStream.close();
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

}
