package com.philips.cl.di.digitalcare.locatephilips.test;

import java.io.IOException;
import java.io.InputStream;

import android.content.Context;

public class AtosParserUtils {

	public static String loadJSONFromAsset(String assetPath, Context context) {
		String json = null;
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
