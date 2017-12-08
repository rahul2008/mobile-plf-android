package com.philips.platform.flowmanager.utility;

import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.IOException;

public class UappBaseAppUtil {

    private File jsonFile;

    public boolean createDirIfNotExists() {
        boolean ret = false;
        File file = new File(Environment.getExternalStorageDirectory(), "/uApp");
        jsonFile = new File(file.getPath(), "appflow.json");
        if (!file.exists()) {
            final boolean mkdirs = file.mkdir();
            if (!mkdirs) {
                Log.e(getClass() + "", "error in creating folders");
            } else {
                try {
                    ret = jsonFile.createNewFile();
                } catch (IOException e) {
                    Log.e("IO-Exception"," while creating new file ");
                }
            }
        }
        return ret;
    }

    public File getJsonFilePath() {
        File file = new File(Environment.getExternalStorageDirectory(), "/uApp");
        jsonFile = new File(file.getPath(), "appflow.json");
        return jsonFile;
    }
}
