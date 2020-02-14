package com.philips.cdp.digitalcare.contactus;

import android.content.Context;
import android.os.SystemClock;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import com.philips.cdp.digitalcare.util.DigiCareLogger;

import java.io.IOException;
import java.io.InputStream;

public class CdlsParserUtils {

	private static final String TAG = CdlsParserUtils.class.getSimpleName();

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
			DigiCareLogger.e(TAG, ex.getMessage());
			return null;
		}
		return json;
	}

	public static Fragment waitForFragment(FragmentActivity activity, String tag) {
		long endTime = SystemClock.uptimeMillis() + 10000;
		while (SystemClock.uptimeMillis() <= endTime) {

			Fragment fragment = activity.getSupportFragmentManager()
					.findFragmentByTag(tag);
			if (fragment != null) {
				return fragment;
			}
		}
		return null;
	}

}
