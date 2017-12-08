package com.philips.platform.appinfra;



import android.content.Context;

import com.philips.platform.appinfra.languagepack.LanguagePackConstants;
import com.philips.platform.appinfra.languagepack.model.LanguagePackModel;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;

/**
 * The Language Pack Util class.
 */

public class FileUtils {

	private static final String DIRECTORY_FILE_NAME = "AppInfra";

	private Context context;

	public FileUtils(Context context) {
		this.context = context;
	}

	public File getFilePath(String fileName, String filePath) {
		final File directory = context.getDir(DIRECTORY_FILE_NAME, Context.MODE_PRIVATE);
		final File file = new File(directory, filePath);
		final File jsonFile = new File(file.getPath(), fileName);
		if (!file.exists()) {
			final boolean mkdirs = file.mkdirs();
			if (!mkdirs) {
				//Log.e(this.getClass() + "", "error in creating folders");
			} else {
				try {
					jsonFile.createNewFile();
				} catch (IOException var5) {
					//Log.e("IO-Exception "," 1 ");
				}
			}
		}

		return jsonFile;
	}

	public void saveFile(String response, String fileName, String filePath) {
		FileWriter fileWriter = null;
		try {
			fileWriter = new FileWriter(getFilePath(fileName ,filePath));
			fileWriter.write(response);
			fileWriter.close();
		} catch (IOException e) {
			//Log.e("IO-Exception "," 2 ");
			
		} finally {
			try {
				if (fileWriter != null)
					fileWriter.close();
			} catch (IOException e) {
				//Log.e("IO-Exception "," 3 ");
			}
		}
	}

	public String readFile(File file) {
		final int length = (int) file.length();
		final byte[] bytes = new byte[length];
		FileInputStream fileInputStream = null;
		try {
			fileInputStream = new FileInputStream(file);
			fileInputStream.read(bytes);
		} catch (IOException e) {
//			Log.e("IO-Exception "," 4 ");
		} finally {
			try {
				if (fileInputStream != null)
					fileInputStream.close();
			} catch (IOException e) {
//				Log.e("IO-Exception "," 5 ");
			}
		}
		return (bytes.length == 0 ? null : new String(bytes));
	}

	public boolean deleteFile(String fileName , String filePath) {
		final File file = getFilePath(fileName, filePath);
		return file.delete();
	}


	public void saveLocaleMetaData(LanguagePackModel languagePackModel) {
		try {
			final JSONObject metadataJsonObject = new JSONObject();
			metadataJsonObject.put(LanguagePackConstants.LOCALE, languagePackModel.getLocale());
			metadataJsonObject.put(LanguagePackConstants.VERSION, languagePackModel.getVersion());
			metadataJsonObject.put(LanguagePackConstants.URL, languagePackModel.getUrl());
			saveFile(metadataJsonObject.toString(),LanguagePackConstants.LOCALE_FILE_INFO,LanguagePackConstants.LANGUAGE_PACK_PATH);
		} catch (JSONException e) {
//			Log.e(getClass()+"", " error while parsing Json ");
		}
	}


	public boolean renameOnActivate() {
		final File from = getFilePath(LanguagePackConstants.LOCALE_FILE_DOWNLOADED,LanguagePackConstants.LANGUAGE_PACK_PATH);
		final File to = new File(getFilePath(LanguagePackConstants.LOCALE_FILE_ACTIVATED,LanguagePackConstants.LANGUAGE_PACK_PATH), "");
		if (from.exists()) {
			if (to.exists()) {
				to.delete();
			}
			return from.renameTo(to);
		} else {
			return to.exists();
		}
	}
}

