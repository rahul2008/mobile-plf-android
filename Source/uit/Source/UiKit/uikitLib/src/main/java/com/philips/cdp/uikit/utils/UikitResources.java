package com.philips.cdp.uikit.utils;


import android.content.res.AssetManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.util.DisplayMetrics;

public class UikitResources extends Resources {

    /**
     * Create a new Resources object on top of an existing set of assets in an
     * AssetManager.
     *
     * @param assets  Previously created AssetManager.
     * @param metrics Current display metrics to consider when
     *                selecting/computing resource values.
     * @param config  Desired device configuration to consider when
     */
    public UikitResources(AssetManager assets, DisplayMetrics metrics, Configuration config) {
        super(assets, metrics, config);
    }

    public UikitResources(@NonNull final Resources res) {
        super(res.getAssets(), res.getDisplayMetrics(), res.getConfiguration());
    }

    @NonNull
    @Override
    public CharSequence getText(@StringRes int id) {
        if (UikitLocaleHelper.getUikitLocaleHelper().isLookUp()) {
            String resourceName = getResourceEntryName(id);
            if (resourceName != null) {
                String string = UikitLocaleHelper.getUikitLocaleHelper().lookUpString(resourceName);
                if (string != null) {
                    return string;
                }
            }
        }
        return super.getText(id);
    }
}

