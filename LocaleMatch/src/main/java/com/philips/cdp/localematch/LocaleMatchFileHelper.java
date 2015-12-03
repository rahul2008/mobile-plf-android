package com.philips.cdp.localematch;

import android.content.Context;
import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;


public class LocaleMatchFileHelper {
	
	public static final String DIR_NAME = "LocaleMatch";
	
	public static final String LOG_TAG ="LocaleMatchFileHelper";

	public static synchronized void writeResponseToFile(InputStream ipStream,
	        String inputLocale, Context context) {
		if(context==null || ipStream==null || inputLocale==null){
			return;
		}
		Log.i(LOG_TAG,"writeResponseToFile(), inputLocale = " + inputLocale);

		String filename = inputLocale+".json";

		saveFile(context,ipStream,filename);
	}

	public static synchronized String getJsonStringFromFile(Context context, String locale) {
		Log.i(LOG_TAG, "getJsonStringFromFile, locale = "+ locale);

		FileInputStream ipStream = null;
		String strLine;
		String responseStr = "";

		if(context==null){
			return null;
		}

		try {
		File fileDirectory = context.getDir(DIR_NAME, Context.MODE_PRIVATE);
		String fileName = locale+".json";
		File createdJsonFile = new File(fileDirectory, fileName);

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
		} catch (Exception e) {
			e.printStackTrace();
		}finally
		{
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
	
	public static boolean verifyJsonExists(Context context, String inputLocale) {
		try {
			File fileDirectory = context.getDir(DIR_NAME, Context.MODE_PRIVATE);
			String fileName = inputLocale + ".json";
			if(fileDirectory!=null && fileDirectory.isDirectory() ) {
				String[] fileNamesList = fileDirectory.list();
				if (fileNamesList != null && fileNamesList.length > 0) {
					for (String file:fileNamesList) {
					if(file!=null && file.equals(fileName))
						Log.i(LOG_TAG,"verifyJsonExists, file and filename = "+file + fileName);
						return true;
					}
				}
			}
		}catch (Exception e){
			e.printStackTrace();
		}
		return false;
	}

	private static void saveFile(Context context, InputStream inputStream, String fileName) {
		Log.d(LOG_TAG,"Locale Match SaveFile call, fileName = "+fileName);
		try {
			File fileDirectory = context.getDir(DIR_NAME, Context.MODE_PRIVATE);
			if (fileDirectory != null && fileDirectory.isDirectory()) {
				File[] fileList = fileDirectory.listFiles();
				if (fileList != null && fileList.length > 0) {
					for (File file : fileList) {
						file.delete();
					}
				}
				File outPutFile = new File(fileDirectory, fileName);

				byte[] ipStreamByteArray = null;
				BufferedReader br = new BufferedReader(
						new InputStreamReader(inputStream));
				String strLine;
				String responseStr = "";
				while ((strLine = br.readLine()) != null) {
					responseStr += strLine;
				}
				Log.d(LOG_TAG, "Save response, responseStr = " + responseStr);
				inputStream.close();

				ipStreamByteArray = responseStr.getBytes();

				FileOutputStream outputStream = new FileOutputStream(outPutFile);

				outputStream.write(ipStreamByteArray);
				outputStream.flush();
				outputStream.close();
			}
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		}

}
