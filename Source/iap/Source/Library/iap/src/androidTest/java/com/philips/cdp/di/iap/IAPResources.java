package com.philips.cdp.di.iap;

import android.content.res.AssetManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.util.DisplayMetrics;

/**
 * Created by philips on 5/2/17.
 */

class IAPResources extends Resources {
    public IAPResources(AssetManager assets, DisplayMetrics metrics, Configuration config) {
        super(assets, metrics, config);
    }

    public IAPResources(final Resources res) {
        super(res.getAssets(), res.getDisplayMetrics(), res.getConfiguration());
    }

    @Override
    public CharSequence getText(int id) {
//        if (IAPLocaleHelper.getInstance().isLookUp()) {
//            String resourceName = getResourceEntryName(id);
//            if (resourceName != null) {
//                String string = IAPLocaleHelper.getInstance().lookUpString(resourceName);
//                if (string != null) {
//                    return string;
//                }
//            }
//        }
        return super.getText(id);
    }

}
