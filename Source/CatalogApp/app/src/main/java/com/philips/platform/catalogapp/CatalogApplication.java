/*
 * (C) Koninklijke Philips N.V., 2016.
 * All rights reserved.
 *
 */
package com.philips.platform.catalogapp;

import android.app.Application;

import com.philips.platform.uid.thememanager.UIDHelper;
import com.philips.platform.uid.utils.UIDLocaleHelper;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

public class CatalogApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        UIDHelper.injectCalligraphyFonts();

        File f = new File(getCacheDir()+"/catalogapp.json");
        if (!f.exists()) try {

            InputStream is = getAssets().open("catalogapp.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();


            FileOutputStream fos = new FileOutputStream(f);
            fos.write(buffer);
            fos.close();
        } catch (Exception e)
        {
            throw new RuntimeException(e);
        }
        UIDLocaleHelper.getUidLocaleHelper().setFilePath(f.getPath());
        UIDLocaleHelper.getUidLocaleHelper().setIsParseJSON(true);
        UIDLocaleHelper.parseJSON();
    }
}
