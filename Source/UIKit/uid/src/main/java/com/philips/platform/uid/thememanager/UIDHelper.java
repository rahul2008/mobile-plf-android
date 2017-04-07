/*
 * (C) Koninklijke Philips N.V., 2017.
 * All rights reserved.
 *
 */

package com.philips.platform.uid.thememanager;

import android.content.res.Resources;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.philips.platform.uid.R;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

public class UIDHelper {

    public static final String CONTENT_TONAL_RANGE = "CONTENT_TONAL_RANGE";
    public static final String COLOR_RANGE = "COLOR_RANGE";
    public static final String NAVIGATION_RANGE = "NAVIGATION_RANGE";
    public static final String ACCENT_RANGE = "ACCENT_RANGE";

    public static void injectCalligraphyFonts() {
        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/centralesansbook.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build());
    }

    public static void init(@NonNull ThemeConfiguration themeConfiguration) {
        Resources.Theme theme = themeConfiguration.getContext().getTheme();
        Log.d(UIDHelper.class.getName(), " init ");

        for (ThemeConfig config : themeConfiguration.getConfigurations()) {
            Log.d(UIDHelper.class.getName(), " config " + config);
            config.injectStyle(theme);
        }
    }

    /**
     * This API will setup toolbar
     * Be-aware that this API will override the title from toolbar to make title center aligned, <br>Make sure to include uid_toolbar_layout in your activity.
     * <br> If you have used your own toobar then you can setup toolbar on your own using activity.setSupportActionBar(toolbar);
     *
     * @param activity Reference of activity you want to setup toolbar
     */
    public static void setupToolbar(@NonNull final AppCompatActivity activity) {
        final View toolbarView = activity.findViewById(com.philips.platform.uid.R.id.uid_toolbar);
        if (toolbarView instanceof Toolbar) {
            Toolbar toolbar = (Toolbar) toolbarView;
            activity.setSupportActionBar(toolbar);
            activity.getSupportActionBar().setDisplayShowTitleEnabled(false);
            return;
        }
        throw new RuntimeException("Please include a uid_toolbar_layout in your main activity layout xml");
    }

    /**
     * This API will help you to set title of your page on toolbar
     *
     * @param activity        Reference of activity
     * @param titleResourceId String resourceId for title
     */
    public static void setTitle(AppCompatActivity activity, @StringRes int titleResourceId) {
        setTitle(activity, activity.getString(titleResourceId));
    }

    /**
     * This API will help you to set title of your page on toolbar
     *
     * @param activity Reference of activity
     * @param title    String which you would like to set as title
     */
    public static void setTitle(AppCompatActivity activity, String title) {
        final View titleView = activity.findViewById(com.philips.platform.uid.R.id.uid_toolbar_title);
        if (titleView instanceof TextView) {
            ((TextView) activity.findViewById(R.id.uid_toolbar_title)).setText(title);
            return;
        }
        throw new RuntimeException("Please include a uid_toolbar_layout in your main activity layout xml");
    }


}
