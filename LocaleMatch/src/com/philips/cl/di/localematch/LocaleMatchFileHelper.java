package com.philips.cl.di.localematch;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import android.util.Log;


public class LocaleMatchFileHelper {
	
	public static final String FILEPATH = "/storage/emulated/0/LocaleMatch/";
	
	public static final String LOG_TAG ="LocaleMatchFileHelper";
	
	public static synchronized void writeResponseToFile(InputStream ipStream,
	        String inputLocale) {
		Log.i(LOG_TAG,
		        "writeResponseToFile(), inputLocale = " + inputLocale);
		if (ipStream != null) {

			byte[] ipStreamByteArray = null;
			try {
				File localeMatchDir = new File(
				        FILEPATH);
				boolean dirCreated = false;
				if (!localeMatchDir.exists()) {
					dirCreated = localeMatchDir.mkdir();
					Log.i(LOG_TAG,
					        "writeResponseToFile(), Directory doesnt exist, dirCreated = "
					                + dirCreated);
					localeMatchDir.setReadable(true, true);
					localeMatchDir.setWritable(true, true);
				} else {
					Log.i(LOG_TAG,
					        "writeResponseToFile(), Directory exists, delete existing files");
					if (localeMatchDir.isDirectory()) {
						File[] fileList = localeMatchDir.listFiles();
						if (fileList != null && fileList.length > 0) {
							for (File file : fileList) {
								file.delete();
							}
						}
					}
				}

				BufferedReader br = new BufferedReader(
				        new InputStreamReader(ipStream));
				String strLine;
				String responseStr = "";
				while ((strLine = br.readLine()) != null) {
					responseStr += strLine;
				}
				Log.i(LOG_TAG,"writeresponse, responseStr = "+responseStr);
				ipStream.close();

				ipStreamByteArray = responseStr.getBytes();

				File jsonFile = new File(FILEPATH
				        + "/" + inputLocale + ".json");
				FileOutputStream outputStream = new FileOutputStream(
				        jsonFile);

				outputStream.write(ipStreamByteArray);
				outputStream.flush();
				outputStream.close();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}

		}
	}

	public static synchronized String getJsonStringFromFile(String locale) {
		Log.i(LOG_TAG, "getJsonStringFromFile, locale = "
				+ locale);

		File createdJsonFile = new File(FILEPATH + locale
				+ ".json");

		FileInputStream ipStream = null;
		String strLine;
		String responseStr = "";

		try {
			ipStream = new FileInputStream(createdJsonFile);
			BufferedReader br = new BufferedReader(new InputStreamReader(
					ipStream));
			while ((strLine = br.readLine()) != null) {
				responseStr += strLine;
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			Log.i(LOG_TAG,
					"readsamplejson, FileNotFoundException");
		} catch (IOException e) {
			e.printStackTrace();
			Log.i(LOG_TAG, "readsamplejson, IOException");
		} finally {
			if (ipStream != null) {
				try {
					ipStream.close();
				} catch (IOException e) {
					e.printStackTrace();
					Log.e(LOG_TAG,
							"getJsonStringFromFile, ipStream not closed caused exception");
				}
			}
		}

		return responseStr;

	}
	
	public static boolean verifyJsonExists(String inputLocale) {
		File localeMatchDir = new File(FILEPATH
				+ inputLocale + ".json");
		return localeMatchDir.exists();
	}

}
