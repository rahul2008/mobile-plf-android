/* Copyright (c) Koninklijke Philips N.V. 2016
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */
package com.philips.platform.appinfra.languagepack;

import android.content.Context;
import android.content.ContextWrapper;
import android.util.Log;

import com.philips.platform.appinfra.languagepack.model.LanguagePackModel;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
 
class LanguagePackUtil {

    private Context context;

    LanguagePackUtil(Context context) {
        this.context = context;
    }

    File getLanguagePackFilePath(String fileName) {
        final ContextWrapper contextWrapper = new ContextWrapper(context);
        final File directory = contextWrapper.getCacheDir();
        final File file = new File(directory, LanguagePackConstants.LANGUAGE_PACK_PATH);
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

     void saveFile(String response, String fileName) {
        FileWriter fileWriter = null;
        try {
            fileWriter = new FileWriter(getLanguagePackFilePath(fileName));
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

     String readFile(File file) {
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

    boolean deleteFile(String fileName) {
        final File file = getLanguagePackFilePath(fileName);
        return file.delete();
    }


    void saveLocaleMetaData(LanguagePackModel languagePackModel) {
        try {
            final JSONObject metadataJsonObject = new JSONObject();
            metadataJsonObject.put(LanguagePackConstants.LOCALE, languagePackModel.getLocale());
            metadataJsonObject.put(LanguagePackConstants.VERSION, languagePackModel.getVersion());
            metadataJsonObject.put(LanguagePackConstants.URL, languagePackModel.getUrl());
            saveFile(metadataJsonObject.toString(),LanguagePackConstants.LOCALE_FILE_INFO);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    boolean renameOnActivate() {
        final File from = getLanguagePackFilePath(LanguagePackConstants.LOCALE_FILE_DOWNLOADED);
        final File to = new File(getLanguagePackFilePath(LanguagePackConstants.LOCALE_FILE_ACTIVATED), "");
        if (from.exists()) {
            if (to.exists()) {
                to.delete();
            }
            return from.renameTo(to);
        } else return to.exists();
    }
}
