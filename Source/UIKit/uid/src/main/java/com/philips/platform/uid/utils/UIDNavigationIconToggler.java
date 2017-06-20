package com.philips.platform.uid.utils;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.philips.platform.uid.R;


public class UIDNavigationIconToggler {
    private Toolbar toolBar;
    private Drawable navigationIconDrawable;

    public UIDNavigationIconToggler(Activity activity) {
        View view = activity.findViewById(R.id.uid_toolbar);
        if (view instanceof Toolbar) {
            toolBar = (Toolbar) view;
            navigationIconDrawable = toolBar.getNavigationIcon();
        }
    }

    public void hideNavigationIcon() {
        if (isToolBarContainsNavigationIcon()) {
            toolBar.setNavigationIcon(null);
        }
    }

    public void restoreNavigationIcon() {
        if (isToolBarContainsNavigationIcon()) {
            toolBar.setNavigationIcon(navigationIconDrawable);
        }
    }

    private boolean isToolBarContainsNavigationIcon() {
        return toolBar != null && navigationIconDrawable != null;
    }
}