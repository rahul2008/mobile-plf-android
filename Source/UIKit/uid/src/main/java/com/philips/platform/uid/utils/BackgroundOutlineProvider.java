/*
 * (C) Koninklijke Philips N.V., 2017.
 * All rights reserved.
 *
 */
package com.philips.platform.uid.utils;

import android.graphics.Outline;
import android.view.View;
import android.view.ViewOutlineProvider;

public class BackgroundOutlineProvider extends ViewOutlineProvider {

    @Override
    public void getOutline(View view, Outline outline) {
        if(view.getBackground() != null){
            view.getBackground().getOutline(outline);
            outline.setAlpha(1);
        }
    }
}

