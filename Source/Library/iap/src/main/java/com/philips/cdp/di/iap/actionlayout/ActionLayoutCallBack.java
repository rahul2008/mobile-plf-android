/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
package com.philips.cdp.di.iap.actionlayout;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.View;

public interface ActionLayoutCallBack {
    /**
     *
     * @return {@link View} View which can be set to custom layout or action bar
     */
    View getCustomView(Context context);

    /**
     * App can provide custom background drawable.
     * @param {@link Drawable} Drawable for back image.
     */
    void setBackGroundDrawable(Drawable drawable);

    /**
     * App can provide custom background back button drawable.
     * @param {@link Drawable} Drawable for back image.
     */
    void setBackButtonDrawable(Drawable drawable);
    /**
     * Notifies Fragments for the hardware back events.
     * @return TRUE if the fragment consumes the back event, else false and its safe for the
     * activity to handle.
     */
    boolean onHWBackPressed();
}