/* Copyright (c) Koninklijke Philips N.V. 2016
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */
package com.philips.platform.appinfra.languagepack;

import android.content.Context;
import android.content.ContextWrapper;
import android.util.Log;

import com.philips.platform.appinfra.languagepack.model.LanguageModel;

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
        ContextWrapper contextWrapper = new ContextWrapper(context);
        File directory = contextWrapper.getCacheDir();
        File file = new File(directory, LanguagePackConstants.LANGUAGE_PACK_PATH);
        File jsonFile = new File(file.getPath(), fileName);
        if (!file.exists()) {
            boolean mkdirs = file.mkdirs();
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
        int length = (int) file.length();
        byte[] bytes = new byte[length];
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
        File file = getLanguagePackFilePath(fileName);
        return file.delete();
    }


    void saveLocaleMetaData(LanguageModel languageModel) {
        try {
            JSONObject metadataJsonObject = new JSONObject();
            metadataJsonObject.put(LanguagePackConstants.LOCALE,languageModel.getLocale());
            metadataJsonObject.put(LanguagePackConstants.VERSION, languageModel.getVersion());
            metadataJsonObject.put(LanguagePackConstants.URL,languageModel.getUrl());
            saveFile(metadataJsonObject.toString(),LanguagePackConstants.LOCALE_FILE_INFO);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    boolean renameOnActivate() {
        File from = getLanguagePackFilePath(LanguagePackConstants.LOCALE_FILE_DOWNLOADED);
        File to = new File(getLanguagePackFilePath(LanguagePackConstants.LOCALE_FILE_ACTIVATED), "");
        if (from.exists()) {
            if (to.exists()) {
                to.delete();
            }
            return from.renameTo(to);
        } else return to.exists();
    }
}
