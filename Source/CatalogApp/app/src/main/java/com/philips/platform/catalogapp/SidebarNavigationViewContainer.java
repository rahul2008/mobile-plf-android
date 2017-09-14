/*
 * (C) Koninklijke Philips N.V., 2017.
 * All rights reserved.
 *
 */
package com.philips.platform.catalogapp;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.util.AttributeSet;

import com.philips.platform.uid.R;
import com.philips.platform.uid.thememanager.UIDHelper;

/**
 *  This class is created only for demo purpose to get either Content Themed or Navigation Themed Context for Sidebar Container.
 *  It is not recommended to use this class in your application.
 *
 */

public class SidebarNavigationViewContainer extends NavigationView {

    public SidebarNavigationViewContainer(@NonNull final Context context) {
        super(context);
    }

    public SidebarNavigationViewContainer(@NonNull final Context context, @NonNull final AttributeSet attrs) {
        this(context, attrs, -1);
    }

    public SidebarNavigationViewContainer(@NonNull final Context context, @NonNull final AttributeSet attrs, final int defStyleAttr) {
        super(setThemedContext(context, attrs), attrs, defStyleAttr);
    }

    private static Context setThemedContext(Context context, AttributeSet attrs) {

        TypedArray typedArray = context.obtainStyledAttributes(attrs, new int[]{R.attr.uidSidebarContextType});
        int resourceId = typedArray.getInt(0, 0);
        typedArray.recycle();
        switch (resourceId){
            case 0: return UIDHelper.getContentThemedContext(context);
            case 1: return UIDHelper.getNavigationThemedContext(context);
            default: return context;
        }
    }
}
