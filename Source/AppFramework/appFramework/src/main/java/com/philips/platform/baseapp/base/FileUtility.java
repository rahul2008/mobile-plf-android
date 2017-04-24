/* Copyright (c) Koninklijke Philips N.V., 2016
* All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
*/
package com.philips.platform.baseapp.base;

import android.content.Context;
import android.support.annotation.Nullable;

import com.philips.platform.appinfra.logging.LoggingInterface;
import com.philips.platform.baseapp.screens.utility.BaseAppUtil;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import static com.philips.platform.baseapp.screens.utility.Constants.FILE_IO;

public class FileUtility {

    private Context context;
    public final String TAG = FileUtility.class.getSimpleName();

    public FileUtility(Context context){
            this.context = context;
    }
    @Nullable
    private InputStream getInputStream(final int resId) {
        InputStream inputStream = null;
        try {
            inputStream = context.getAssets().open(context.getString(resId));
        } catch (IOException e) {
            AppFrameworkApplication.loggingInterface.log(LoggingInterface.LogLevel.DEBUG, FILE_IO,
                    e.getMessage());        }
        return inputStream;
    }

    public File createFileFromInputStream(final int resId) {

        try {
            InputStream inputStream = getInputStream(resId);
            String filename = "tempFile";
            FileOutputStream outputStream;
            final File file = File.createTempFile(filename, null, context.getCacheDir());
            outputStream = new FileOutputStream(file);

            byte buffer[] = new byte[1024];
            int length;

            while ((length = inputStream.read(buffer)) > 0) {
                outputStream.write(buffer, 0, length);
            }
            outputStream.close();
            inputStream.close();
            return file;
        } catch (IOException e) {
            AppFrameworkApplication.loggingInterface.log(LoggingInterface.LogLevel.DEBUG, FILE_IO,
                   e.getMessage());
        }

        return null;
    }
}
