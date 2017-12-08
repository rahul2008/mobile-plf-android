package com.philips.cdp.ui.catalog.activity;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import com.philips.cdp.ui.catalog.R;
import com.philips.cdp.ui.catalog.themeutils.ThemeUtils;
import com.philips.cdp.uikit.UiKitActivity;
import com.philips.cdp.uikit.utils.UikitLocaleHelper;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public class CatalogActivity extends UiKitActivity {
    protected ThemeUtils themeUtils;
    private int noActionBarTheme = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (noActionBarTheme > 0) {
            setTheme(noActionBarTheme);
        } else {
            themeUtils = new ThemeUtils(this.getSharedPreferences(this.getString(R.string.app_name),
                    Context.MODE_PRIVATE));
            setTheme(themeUtils.getTheme());
        }
        UikitLocaleHelper.getUikitLocaleHelper().setFilePath(getCatalogAppJSONAssetPath());
        super.onCreate(savedInstanceState);
    }

    protected void setNoActionBarTheme() {
        themeUtils = new ThemeUtils(this.getSharedPreferences(this.getString(R.string.app_name),
                Context.MODE_PRIVATE));
        noActionBarTheme = themeUtils.getNoActionBarTheme();
    }

    public String getCatalogAppJSONAssetPath(){
        try {
            File f = new File(getCacheDir()+"/catalogapp.json");
            InputStream is = getAssets().open("catalogapp.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();

            FileOutputStream fos = new FileOutputStream(f);
            fos.write(buffer);
            fos.close();
            return f.getPath();
        } catch (FileNotFoundException e) {
            Log.e("",e.getMessage());
        } catch (IOException e) {
            Log.e("",e.getMessage());
        }
        return null;
    }

}
