/* Copyright (c) Koninklijke Philips N.V., 2016
* All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
*/
package com.philips.platform.uappdemo.screens.base;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.Log;

import com.philips.platform.flowmanager.utility.UappBaseAppUtil;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;


public class UappFileUtility {

    private Context context;

    public UappFileUtility(Context context) {
        this.context = context;
    }

    @Nullable
    private InputStream getInputStream(final int resId) {
        InputStream inputStream = null;
        try {
            inputStream = context.getAssets().open(context.getString(resId));
        } catch (IOException e) {
            Log.e("IO-Exception", " error while reading appflow.json");
        }
        return inputStream;
    }

    public File createFileFromInputStream(final int resId, boolean sdCardFileCreated) {

        try {
            InputStream inputStream = getInputStream(resId);
            String filename = "tempFile";
            FileOutputStream outputStream;
            FileOutputStream jsonFileOutputStream = null;
            final File file = File.createTempFile(filename, null, context.getCacheDir());
            outputStream = new FileOutputStream(file);
            if (sdCardFileCreated)
                jsonFileOutputStream = new FileOutputStream(new UappBaseAppUtil().getJsonFilePath());
            byte buffer[] = new byte[1024];
            int length;

            while ((length = inputStream.read(buffer)) > 0) {
                outputStream.write(buffer, 0, length);
                if (sdCardFileCreated)
                    jsonFileOutputStream.write(buffer, 0, length);
            }
            outputStream.close();
            if (sdCardFileCreated)
                jsonFileOutputStream.close();
            inputStream.close();
            return file;
        } catch (IOException e) {
            Log.e("IO-Exception", " error while closing stream");
        }

        return null;
    }
}
