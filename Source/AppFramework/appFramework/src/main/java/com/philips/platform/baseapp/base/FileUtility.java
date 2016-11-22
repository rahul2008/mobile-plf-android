package com.philips.platform.baseapp.base;

import android.content.Context;
import android.support.annotation.Nullable;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class FileUtility {

    private Context context;

    public FileUtility(Context context){
            this.context = context;
    }
    @Nullable
    private InputStream getInputStream(final int resId) {
        InputStream inputStream = null;
        try {
            inputStream = context.getAssets().open(context.getString(resId));
        } catch (IOException e) {
            e.printStackTrace();
        }
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
            e.printStackTrace();
        }

        return null;
    }
}
