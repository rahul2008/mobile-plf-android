package com.philips;


import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;

import java.util.Locale;

/**
 * Created by philips on 09/01/18.
 */

public class AppNameToEnglish {

    // It is a temporary fix in jump library. To convert the appname into english.
    public  String getApplicationName(Context context) {
        ApplicationInfo applicationInfo = context.getApplicationInfo();
        int stringId = applicationInfo.labelRes;
        return stringId == 0 ? applicationInfo.nonLocalizedLabel.toString() : getLocaleStringResource(new Locale("en"),stringId,context);
    }

    private  String getLocaleStringResource(Locale requestedLocale, int resourceId, Context context) {
        String result;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) { // use latest api
            Configuration config = new Configuration(context.getResources().getConfiguration());
            config.setLocale(requestedLocale);
            result = context.createConfigurationContext(config).getText(resourceId).toString();
        }
        else { // support older android versions
            Resources resources = context.getResources();
            Configuration conf = resources.getConfiguration();
            Locale savedLocale = conf.locale;
            conf.locale = requestedLocale;
            resources.updateConfiguration(conf, null);

            // retrieve resources from desired locale
            result = resources.getString(resourceId);

            // restore original locale
            conf.locale = savedLocale;
            resources.updateConfiguration(conf, null);
        }

        return result;
    }
}
