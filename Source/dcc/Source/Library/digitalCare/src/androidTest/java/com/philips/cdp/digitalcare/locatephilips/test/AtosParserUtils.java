package com.philips.cdp.digitalcare.locatephilips.test;

import java.io.IOException;
import java.io.InputStream;

import android.content.Context;

public class AtosParserUtils {

	static String json = null;

	public static String loadJSONFromAsset(String assetPath, Context context) {

		try {
			InputStream is = context.getAssets().open("atos/" + assetPath);
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

}
