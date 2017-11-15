package com.philips.cdp2.ews;

import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.StyleRes;
import android.util.Log;

import com.philips.cdp2.ews.R;
import com.philips.cdp2.ews.microapp.EWSDependencyProvider;
import com.philips.platform.uid.thememanager.ColorRange;
import com.philips.platform.uid.thememanager.ContentColor;
import com.philips.platform.uid.thememanager.NavigationColor;
import com.philips.platform.uid.thememanager.ThemeConfig;
import com.philips.platform.uid.thememanager.ThemeConfiguration;
import com.philips.platform.uid.thememanager.UIDHelper;
import com.philips.platform.uid.utils.UIDActivity;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class DynamicThemeApplyingActivity extends UIDActivity {

    private ContentColor contentColor;
    private ColorRange colorRange;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        setTheme(R.style.Theme_DLS_GroupBlue_UltraLight);
        UIDHelper.init(new ThemeConfiguration(this, ColorRange.GROUP_BLUE, NavigationColor.BRIGHT,
                ContentColor.ULTRA_LIGHT));
        initTheme();
        UIDHelper.injectCalligraphyFonts();
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    private void initTheme() {
        final ThemeConfiguration themeConfig = EWSDependencyProvider.getInstance().getThemeConfiguration();
        if(themeConfig != null) {
            for (ThemeConfig config : themeConfig.getConfigurations()) {
                Log.d(UIDHelper.class.getName(), " config " + config);
                //config.injectStyle(theme);
                if (config instanceof ColorRange) {
                    colorRange = (ColorRange) config;
                } else if (config instanceof ContentColor) {
                    contentColor = (ContentColor) config;
                }
            }

            final int themeResourceId = getThemeResourceId(getResources(), getPackageName(), colorRange, contentColor);
            setTheme(themeResourceId);
            UIDHelper.init(themeConfig);
        }
    }

    static String toProperCase(String s) {
        return s.substring(0, 1).toUpperCase() +
                s.substring(1).toLowerCase();
    }

    static String toCamelCase(String s) {
        String[] parts = s.split("_");
        String camelCaseString = "";
        for (String part : parts) {
            camelCaseString = camelCaseString + toProperCase(part);
        }
        return camelCaseString;
    }

    @StyleRes
    int getThemeResourceId(Resources resources, final String packageName, final ColorRange colorRange, final ContentColor contentColor) {
        final String themeName = String.format("Theme.DLS.%s.%s", toCamelCase(colorRange.name()), toCamelCase(contentColor.name()));
        return resources.getIdentifier(themeName, "style", packageName);
    }
}
