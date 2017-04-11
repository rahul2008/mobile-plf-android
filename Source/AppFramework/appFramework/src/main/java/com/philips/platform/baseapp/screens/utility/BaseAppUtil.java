package com.philips.platform.baseapp.screens.utility;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import com.philips.platform.baseapp.base.AppFrameworkApplication;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class BaseAppUtil {

    private File jsonFile;

    public boolean createDirIfNotExists() {
        boolean ret = false;
        File file = new File(Environment.getExternalStorageDirectory(), "/ReferenceApp");
        jsonFile = new File(file.getPath(), "appflow.json");
        if (!file.exists()) {
            final boolean mkdirs = file.mkdir();
            if (!mkdirs) {
                Log.e(getClass() + "", "error in creating folders");
            } else {
                try {
                    ret = jsonFile.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return ret;
    }

    public File getJsonFilePath() {
        File file = new File(Environment.getExternalStorageDirectory(), "/ReferenceApp");
        jsonFile = new File(file.getPath(), "appflow.json");
        return jsonFile;
    }

    public String readJsonFileFromSdCard() {
        File file = new File(Environment.getExternalStorageDirectory(), "/ReferenceApp");
        StringBuilder text = new StringBuilder();
        File jsonFile = new File(file.getPath(), "appflow.json");
        try {
            BufferedReader br = new BufferedReader(new FileReader(jsonFile));
            String line;
            while ((line = br.readLine()) != null) {
                text.append(line);
                text.append('\n');
            }
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return text.toString();
    }

    /**
     * Check whether network is available or not
     *
     * @return
     */
    public static boolean isNetworkAvailable(Context context) {
        return ((AppFrameworkApplication) context.getApplicationContext()).getAppInfra().getRestClient().isInternetReachable();
    }
}
