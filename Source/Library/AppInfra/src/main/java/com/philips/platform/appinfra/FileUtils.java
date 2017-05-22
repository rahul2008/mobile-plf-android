package com.philips.platform.appinfra;



import android.content.Context;
import android.content.ContextWrapper;
import android.util.Log;


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

	private Context context;

	public FileUtils(Context context) {
		this.context = context;
	}

	public File getLanguagePackFilePath(String fileName, String filePath) {
		final ContextWrapper contextWrapper = new ContextWrapper(context);
		final File directory = contextWrapper.getCacheDir();
		final File file = new File(directory, filePath);
		final File jsonFile = new File(file.getPath(), fileName);
		if (!file.exists()) {
			final boolean mkdirs = file.mkdirs();
			if (!mkdirs) {
				Log.e(this.getClass() + "", "error in creating folders");
			} else {
				try {
					jsonFile.createNewFile();
				} catch (IOException var5) {
					var5.printStackTrace();
				}
			}
		}

		return jsonFile;
	}

	public void saveFile(String response, String fileName, String filePath ) {
		FileWriter fileWriter = null;
		try {
			fileWriter = new FileWriter(getLanguagePackFilePath(fileName ,filePath));
			fileWriter.write(response);
			fileWriter.close();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (fileWriter != null)
					fileWriter.close();
			} catch (IOException e) {
				e.printStackTrace();
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
			e.printStackTrace();
		} finally {
			try {
				if (fileInputStream != null)
					fileInputStream.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return (bytes.length == 0 ? null : new String(bytes));
	}

	public boolean deleteFile(String fileName) {
		final File file = getLanguagePackFilePath(fileName, LanguagePackConstants.LANGUAGE_PACK_PATH);
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
			e.printStackTrace();
		}
	}


	public boolean renameOnActivate() {
		final File from = getLanguagePackFilePath(LanguagePackConstants.LOCALE_FILE_DOWNLOADED,LanguagePackConstants.LANGUAGE_PACK_PATH);
		final File to = new File(getLanguagePackFilePath(LanguagePackConstants.LOCALE_FILE_ACTIVATED,LanguagePackConstants.LANGUAGE_PACK_PATH), "");
		if (from.exists()) {
			if (to.exists()) {
				to.delete();
			}
			return from.renameTo(to);
		} else return to.exists();
	}
}

