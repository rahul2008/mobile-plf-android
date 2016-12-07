package com.philips.platform.uid.utils;

import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.philips.platform.uid.R;

public class ToolbarWrapper {

    /**
     * This API will setup toolbar with title navigation icon
     *
     * @param activity          reference of activity you want to setup toolbar
     * @param toolbarResourceId reference for toolbar resource id
     */
    public Toolbar setupToolbar(@NonNull final AppCompatActivity activity, @IdRes final int toolbarResourceId) {
        final Toolbar toolbar = (Toolbar) activity.findViewById(toolbarResourceId);
        if (toolbar == null) {
            final String formattedException = String.format("Please include a layout with view android.support.v7.widget.toolbar containing id %id in  you layout", toolbar);
            throw new RuntimeException(formattedException);
        }
        toolbar.setTitleMarginStart(activity.getResources().getDimensionPixelOffset(R.dimen.uid_navigation_bar_title_margin_left_right));
        toolbar.setTitleMarginEnd(activity.getResources().getDimensionPixelOffset(R.dimen.uid_navigation_bar_title_margin_left_right));
        activity.setSupportActionBar(toolbar);
        return toolbar;
    }
}
