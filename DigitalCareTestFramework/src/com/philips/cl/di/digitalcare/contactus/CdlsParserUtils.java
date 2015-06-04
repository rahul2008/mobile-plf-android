package com.philips.cl.di.digitalcare.contactus;

import java.io.IOException;
import java.io.InputStream;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.os.SystemClock;

public class CdlsParserUtils {

	public static String loadJSONFromAsset(String assetPath, Context context) {
		String json = null;
		try {
			InputStream is = context.getAssets().open(assetPath);
			int size = is.available();
			byte[] buffer = new byte[size];
			is.read(buffer);
			is.close();
			json = new String(buffer, "UTF-8");
		} catch (IOException ex) {
			ex.printStackTrace();
			return null;
		}
		return json;
	}

	public static Fragment waitForFragment(Activity activity, String tag) {
		long endTime = SystemClock.uptimeMillis() + 10000;
		while (SystemClock.uptimeMillis() <= endTime) {

			Fragment fragment = activity.getFragmentManager()
					.findFragmentByTag(tag);
			if (fragment != null) {
				return fragment;
			}
		}
		return null;
	}

}
