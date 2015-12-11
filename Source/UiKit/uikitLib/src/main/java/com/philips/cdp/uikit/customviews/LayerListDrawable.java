package com.philips.cdp.uikit.customviews;

import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;

/**
 * Created by 310213373 on 12/7/2015.
 */
public class LayerListDrawable extends LayerDrawable {
    /**
     * Creates a new layer drawable with the list of specified layers.
     *
     * @param layers a list of drawables to use as layers in this new drawable,
     *               must be non-null
     */
    public LayerListDrawable(Drawable[] layers) {
        super(layers);
    }


}
